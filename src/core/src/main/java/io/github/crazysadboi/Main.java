package io.github.crazysadboi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.eventSystem.GameEventManager;
import io.github.crazysadboi.eventSystem.SoundListener;
import io.github.crazysadboi.gameObjects.*;
import io.github.crazysadboi.strategies.EnemyMovementStrategy;
import io.github.crazysadboi.strategies.PlayerMovementStrategy;
import io.github.crazysadboi.utils.SoundManager;

import java.util.ArrayList;

public class Main extends ApplicationAdapter implements GameState {
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture background, blockTexture, playerTexture, heartTexture, enemyTexture, bulletTexture;
    private ArrayList<Block> blocks;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private Player player;
    private GameEventManager eventManager;
    private SoundListener soundListener;
    private SoundManager soundManager;
    private float initialX, initialY;
    private float timeOffBlock = 0f;
    private final float timeToLoseLife = 3.5f;
    private float stateTime = 0f;
    private float enemySpawnTime = 0f;
    private float bulletRechargeTime = 0f;
    private float gameTime = 0f;
    private int enemiesKilled = 0;
    private int recordEnemiesKilled = 0;
    private int currentBullets = 25;
    private final int maxBullets = 25;
    private boolean gameOver = false;
    private EnemyFactory enemyFactory;

    @Override
    public void create() {
        initialize();
    }

    @Override
    public void initialize() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        background = new Texture("sea.png");
        heartTexture = new Texture("heart.png");
        blocks = new ArrayList<>();
        initialX = Math.round((float) Gdx.graphics.getWidth() / 2 / 50) * 50;
        initialY = Math.round((float) Gdx.graphics.getHeight() / 2 / 50) * 50;
        blocks.add(new Block(initialX, initialY));
        blocks.add(new Block(initialX + 50, initialY));
        blocks.add(new Block(initialX, initialY + 50));
        blocks.add(new Block(initialX + 50, initialY + 50));

        // Singleton init
        eventManager = GameEventManager.getInstance();

        soundManager = new SoundManager();
        soundListener = new SoundListener(soundManager);

        eventManager.subscribe("PLAYER_SHOOT", soundListener);

        player = new Player(initialX, initialY);
        enemies = new ArrayList<>();
        enemyFactory = new EnemyFactory(enemyTexture, enemies, blocks);
        for (int i = 0; i < 2; i++) {
            enemyFactory.createEnemy();
        }
        bullets = new ArrayList<>();

        // Сброс текущего счёта убийств и пуль при новой игре
        enemiesKilled = 0;
        currentBullets = maxBullets; // Устанавливаем полное количество пуль в начале игры
        // Обновление рекорда, если текущий счёт выше
        if (enemiesKilled > recordEnemiesKilled) {
            recordEnemiesKilled = enemiesKilled;
        }
    }

    @Override
    public void update(float deltaTime) {
        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                gameOver = false;
                initialize();
            }
            return;
        }

        stateTime += deltaTime;
        enemySpawnTime += deltaTime;
        bulletRechargeTime += deltaTime;
        gameTime += deltaTime;

        // Движение игрока
        float speed = 100f;
        float moveDistance = speed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.move(0, moveDistance, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.move(0, -moveDistance, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.move(-moveDistance, 0, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.move(moveDistance, 0, new PlayerMovementStrategy());

        // Выстрел
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentBullets > 0) {
            System.out.println("PEW");
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            Vector2 direction = new Vector2(mouseX - (player.getX() + 25), mouseY - (player.getY() + 25)).nor();
            bullets.add(new Bullet(player.getX() + 25 - 5, player.getY() + 25 - 5, direction));
            currentBullets--;
            eventManager.notify("PLAYER_SHOOT");
        }

        // Восстановление пуль
        if (bulletRechargeTime >= 0.5f && currentBullets < maxBullets) {
            currentBullets++;
            bulletRechargeTime = 0f;
            GameEventManager.getInstance().notify("bulletRecharged");
        }

        // Обновление пуль и врагов
        updateBullets(deltaTime);
        updateEnemies(deltaTime);

        // Потеря жизней
        boolean isOnBlock = isPlayerOnBlock();
        if (!isOnBlock) {
            timeOffBlock += deltaTime;
            if (timeOffBlock >= timeToLoseLife && player.getLives() > 0) {
                player.loseLife();
                timeOffBlock = 0f;
                GameEventManager.getInstance().notify("lifeLost");
            }
        } else {
            timeOffBlock = 0f;
        }

        // Спавн врагов
        if (enemySpawnTime >= 10f) {
            enemyFactory.createEnemy();
            enemySpawnTime = 0f;
            GameEventManager.getInstance().notify("enemySpawned");
        }

        // Проверка конца игры
        if (player.getLives() <= 0) {
            gameOver = true;
            // Обновление рекорда перед сбросом
            if (enemiesKilled > recordEnemiesKilled) {
                recordEnemiesKilled = enemiesKilled;
            }
            GameEventManager.getInstance().notify("gameOver");
        }

        // Добавление блока
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            float blockX = Math.round(mouseX / 50) * 50;
            float blockY = Math.round(mouseY / 50) * 50;
            if (!blockExists(blockX, blockY)) {
                blocks.add(new Block(blockX, blockY));
                GameEventManager.getInstance().notify("blockPlaced");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        GameObjectHolder.getInstance().clearDestroyed();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.batch.begin();
        this.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        GameObjectHolder.getInstance().renderObjects(this.batch);
//        for (GameObject block : blocks) block.render(this.batch);

        // Player render
        boolean isOnBlock = isPlayerOnBlock();
        if (!isOnBlock && timeOffBlock > 0) {
            float alpha = 0.5f + 0.5f * (float) Math.sin(stateTime * 5);
            this.batch.setColor(1, 1, 1, alpha);
            player.render(this.batch);
            this.batch.setColor(1, 1, 1, 1);
        } else {
            player.render(this.batch);
        }
//        for (GameObject enemy : enemies) enemy.render(this.batch);
//        for (GameObject bullet : bullets) bullet.render(this.batch);
        for (int i = 0; i < player.getLives(); i++) {
            this.batch.draw(heartTexture, 10 + i * 40, Gdx.graphics.getHeight() - 40, 32, 32);
        }
        font.draw(this.batch, "Time: " + (int)gameTime + " sec", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 10);
        font.draw(this.batch, "Kills: " + enemiesKilled, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 25);
        font.draw(this.batch, "Record: " + recordEnemiesKilled, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 40);
        font.draw(this.batch, "Bullets: " + currentBullets + "/" + maxBullets, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 55);

        if (gameOver) {
            font.draw(this.batch, "Game Over", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 30);
            font.draw(this.batch, "Time: " + (int)gameTime + " sec", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
            font.draw(this.batch, "Kills: " + enemiesKilled, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 15);
            font.draw(this.batch, "Record: " + recordEnemiesKilled, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 30);
            font.draw(this.batch, "Press R to Restart", Gdx.graphics.getWidth() / 2 - 70, Gdx.graphics.getHeight() / 2 - 60);
        }

        this.batch.end();
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());
        render(batch, font);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        background.dispose();
        blockTexture.dispose();
        playerTexture.dispose();
        heartTexture.dispose();
        enemyTexture.dispose();
        bulletTexture.dispose();
    }

    private void updateBullets(float deltaTime) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(deltaTime);
            if (bullet.getX() < 0 || bullet.getX() > Gdx.graphics.getWidth() ||
                bullet.getY() < 0 || bullet.getY() > Gdx.graphics.getHeight()) {
                bullets.get(i).destroy();
                bullets.remove(i);
                continue;
            }
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.getX() < enemy.getX() + 50 && bullet.getX() + 10 > enemy.getX() &&
                    bullet.getY() < enemy.getY() + 50 && bullet.getY() + 10 > enemy.getY()) {
                    enemies.get(j).destroy();
                    bullets.get(i).destroy();
                    enemies.remove(j);
                    bullets.remove(i);
                    enemiesKilled++;
                    enemyFactory.createEnemy();
                    GameEventManager.getInstance().notify("enemyKilled");
                    break;
                }
            }
        }
    }

    private void updateEnemies(float deltaTime) {
        enemies.removeIf(Enemy::isDestroyed);
        for (Enemy enemy : enemies) {
            enemy.moveTowards(player.getX() + 25, player.getY() + 25, deltaTime, new EnemyMovementStrategy());
            if (enemy.isOnBlock(blocks, 50)) {
                enemy.destroyBlock(blocks, 50, player);
                enemy.destroy();
            }
            enemy.attackPlayer(player, deltaTime);
        }
    }


    private boolean isPlayerOnBlock() {
        for (Block block : blocks) {
            if (player.getX() < block.getX() + 50 && player.getX() + 50 > block.getX() &&
                player.getY() < block.getY() + 50 && player.getY() + 50 > block.getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean blockExists(float x, float y) {
        for (Block block : blocks) {
            if (block.getX() == x && block.getY() == y) return true;
        }
        return false;
    }
}

/*Используемый шаблон проектирования:
 * - **Шаблон "Состояние" (State Pattern)**: интерфейс GameState может быть частью реализации различных состояний игры.
 * - **Шаблон "Стратегия" (Strategy Pattern)**: используется для управления движением игрока и врагов
 *   через PlayerMovementStrategy и EnemyMovementStrategy.
 * - **Шаблон "Фабрика" (Factory Pattern)**: для создания врагов применяется EnemyFactory.
 * - **Синглтон (Singleton Pattern)**: GameEventManager — синглтон, который позволяет централизованно обрабатывать события (например, "bulletFired", "enemyKilled").
 *
 * Основная логика:
 * - Управление движением игрока (клавиши: стрелки).
 * - Стрельба (пробел), ограниченный боезапас и перезарядка.
 * - Враги спавнятся, двигаются к игроку и разрушают блоки.
 * - Игрок может размещать и удалять блоки.
 * - Потеря жизней, если игрок не стоит на блоке.
 * - Подсчёт убитых врагов и отображение состояния игры (время, жизни, пули).
 *
 * Игра завершается, когда у игрока заканчиваются жизни.
 * Нажатие R перезапускает игру.
 */
