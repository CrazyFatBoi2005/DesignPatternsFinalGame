package io.github.crazysadboi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.*;

import java.util.ArrayList;

public class PlayingState implements GameState {
    private SpriteBatch batch;
    private Texture background, blockTexture, playerTexture, heartTexture, enemyTexture, bulletTexture;
    private ArrayList<Block> blocks;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private Player player;
    private float initialX, initialY;
    private float timeOffBlock = 0f;
    private final float timeToLoseLife = 3.5f;
    private float stateTime = 0f;
    private float debugTime = 0f;
    private float enemySpawnTime = 0f;
    private float bulletRechargeTime = 0f;
    private float gameTime = 0f;
    private int enemiesKilled = 0;
    private int currentBullets = 25;
    private final int maxBullets = 25;
    private EnemyFactory enemyFactory;

    @Override
    public void initialize() {
        try {
            batch = GameManager.getInstance().getBatch();
            background = new Texture("sea.png");
            blockTexture = new Texture("block.png");
            playerTexture = new Texture("player.png");
            heartTexture = new Texture("heart.png");
            enemyTexture = new Texture("enemy.png");
            bulletTexture = new Texture("bullet.png");

            blocks = new ArrayList<>();
            initialX = Math.round(Gdx.graphics.getWidth() / 2 / 50) * 50;
            initialY = Math.round(Gdx.graphics.getHeight() / 2 / 50) * 50;
            blocks.add(new Block(initialX, initialY, blockTexture));
            blocks.add(new Block(initialX + 50, initialY, blockTexture));
            blocks.add(new Block(initialX, initialY + 50, blockTexture));
            blocks.add(new Block(initialX + 50, initialY + 50, blockTexture));

            player = new Player(initialX, initialY, playerTexture);
            enemies = new ArrayList<>();
            enemyFactory = new EnemyFactory(enemyTexture, enemies, blocks);
            for (int i = 0; i < 2; i++) {
                enemyFactory.createEnemy();
                System.out.println("Начальный враг " + i + " создан");
            }
            bullets = new ArrayList<>();
            System.out.println("PlayingState инициализирован: блоки=" + blocks.size() + ", враги=" + enemies.size());
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации PlayingState: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(float deltaTime) {
        try {
            stateTime += deltaTime;
            debugTime += deltaTime;
            enemySpawnTime += deltaTime;
            bulletRechargeTime += deltaTime;
            gameTime += deltaTime;

            // Движение игрока
            float speed = 100f;
            float moveDistance = speed * deltaTime;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) player.move(0, moveDistance, new PlayerMovementStrategy());
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.move(0, -moveDistance, new PlayerMovementStrategy());
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.move(-moveDistance, 0, new PlayerMovementStrategy());
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.move(moveDistance, 0, new PlayerMovementStrategy());

            // Выстрел
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentBullets > 0) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                Vector2 direction = new Vector2(mouseX - (player.getX() + 25), mouseY - (player.getY() + 25)).nor();
                bullets.add(new Bullet(player.getX() + 25 - 5, player.getY() + 25 - 5, direction, bulletTexture));
                currentBullets--;
                GameEventManager.getInstance().notify("bulletFired");
                System.out.println("Выстрел! Осталось пуль: " + currentBullets);
            }

            // Восстановление пуль
            if (bulletRechargeTime >= 0.5f && currentBullets < maxBullets) {
                currentBullets++;
                bulletRechargeTime = 0f;
                GameEventManager.getInstance().notify("bulletRecharged");
                System.out.println("Пуля восстановлена, всего: " + currentBullets);
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
                    System.out.println("Жизнь потеряна! Осталось: " + player.getLives());
                }
            } else {
                timeOffBlock = 0f;
            }

            // Спавн врагов
            if (enemySpawnTime >= 10f) {
                enemyFactory.createEnemy();
                enemySpawnTime = 0f;
                GameEventManager.getInstance().notify("enemySpawned");
                System.out.println("Враг заспавнен, всего врагов: " + enemies.size());
            }

            // Проверка конца игры
            if (player.getLives() <= 0) {
                GameManager.getInstance().setState(new GameOverState(gameTime, enemiesKilled));
                System.out.println("Game Over!");
            }

            // Добавление блока
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                float blockX = Math.round(mouseX / 50) * 50;
                float blockY = Math.round(mouseY / 50) * 50;
                if (!blockExists(blockX, blockY)) {
                    blocks.add(new Block(blockX, blockY, blockTexture));
                    GameEventManager.getInstance().notify("blockPlaced");
                    System.out.println("Блок добавлен: x=" + blockX + ", y=" + blockY);
                }
            }

            // Удаление блока
            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                blocks.removeIf(block -> {
                    boolean isClicked = mouseX >= block.getX() && mouseX <= block.getX() + 50 &&
                        mouseY >= block.getY() && mouseY <= block.getY() + 50;
                    boolean isUnderPlayer = player.getX() >= block.getX() && player.getX() <= block.getX() + 50 &&
                        player.getY() >= block.getY() && player.getY() <= block.getY() + 50;
                    if (isClicked && !isUnderPlayer) {
                        GameEventManager.getInstance().notify("blockRemoved");
                        System.out.println("Блок удалён: x=" + block.getX() + ", y=" + block.getY());
                        return true;
                    }
                    return false;
                });
            }

            // Отладка
            if (debugTime >= 0.5f) {
                System.out.println("Игрок: x=" + player.getX() + ", y=" + player.getY() + ", на блоке: " + isOnBlock +
                    ", жизней: " + player.getLives() + ", пуль: " + currentBullets + ", врагов: " + enemies.size());
                debugTime = 0f;
            }
        } catch (Exception e) {
            System.err.println("Ошибка в update PlayingState: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        try {
            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            for (GameObject block : blocks) block.render(batch);
            boolean isOnBlock = isPlayerOnBlock();
            if (!isOnBlock && timeOffBlock > 0) {
                float alpha = 0.5f + 0.5f * (float) Math.sin(stateTime * 5);
                batch.setColor(1, 1, 1, alpha);
                player.render(batch);
                batch.setColor(1, 1, 1, 1);
            } else {
                player.render(batch);
            }
            for (GameObject enemy : enemies) enemy.render(batch);
            for (GameObject bullet : bullets) bullet.render(batch);
            for (int i = 0; i < player.getLives(); i++) {
                batch.draw(heartTexture, 10 + i * 40, Gdx.graphics.getHeight() - 40, 32, 32);
            }
            font.draw(batch, "Time: " + (int)gameTime + " sec, Kills: " + enemiesKilled,
                Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 10);
            font.draw(batch, "Bullets: " + currentBullets + "/" + maxBullets,
                Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 25);
            batch.end();
        } catch (Exception e) {
            System.err.println("Ошибка в render PlayingState: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateBullets(float deltaTime) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update(deltaTime);
            if (bullet.getX() < 0 || bullet.getX() > Gdx.graphics.getWidth() ||
                bullet.getY() < 0 || bullet.getY() > Gdx.graphics.getHeight()) {
                bullets.remove(i);
                continue;
            }
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.getX() < enemy.getX() + 50 && bullet.getX() + 10 > enemy.getX() &&
                    bullet.getY() < enemy.getY() + 50 && bullet.getY() + 10 > enemy.getY()) {
                    enemies.remove(j);
                    bullets.remove(i);
                    enemiesKilled++;
                    enemyFactory.createEnemy();
                    GameEventManager.getInstance().notify("enemyKilled");
                    System.out.println("Враг убит! Убийств: " + enemiesKilled);
                    break;
                }
            }
        }
    }

    private void updateEnemies(float deltaTime) {
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

    public float getGameTime() { return gameTime; }
    public int getEnemiesKilled() { return enemiesKilled; }
}



/*Класс PlayingState представляет основное игровое состояние в DesignPatternsFinalGame, где происходит вся игровая логика (движение, стрельба, спавн врагов, управление блоками).

- Поля: Хранит ресурсы (`batch`, текстуры), списки объектов (`blocks`, `enemies`, `bullets`), игрока (`player`), таймеры (`timeOffBlock`, `gameTime`, `enemySpawnTime`, `bulletRechargeTime`), счётчики (`enemiesKilled`, `currentBullets`).
- Метод `initialize`: Загружает ресурсы, создаёт начальные блоки, игрока, врагов через `EnemyFactory`, инициализирует списки.
- Метод `update`: Обрабатывает ввод (движение игрока, выстрелы), обновляет пули и врагов, управляет спавном врагов (каждые 10 сек),
восстанавливает пули (каждые 0.5 сек), проверяет условия потери жизней (вне блоков 3.5 сек), переключает на `GameOverState` при 0 жизней, добавляет/удаляет блоки (клик/D), выводит отладку.
- Метод `render`: Отрисовывает фон, блоки, игрока (с миганием вне блоков), врагов, пули, UI (жизни слева, время/убийства/пули справа).
- Вспомогательные методы: `updateBullets` — обновляет пули, проверяет столкновения с врагами; `updateEnemies` — управляет движением и действиями врагов; `
isPlayerOnBlock` — проверяет, на блоке ли игрок; `blockExists` — проверяет, есть ли блок в позиции; `getGameTime`, `getEnemiesKilled` — возвращают статистику.

**Шаблоны**:
- **State**: PlayingState реализует `GameState`, представляя игровое состояние, переключаемое через `GameManager`.
- **Strategy**: Использует `PlayerMovementStrategy` и `EnemyMovementStrategy` для движения игрока и врагов.
- **Factory Method**: `EnemyFactory` создаёт врагов.
- **Composite**: Работает с объектами (`Block`, `Enemy`, `Bullet`, `Player`) через интерфейс `GameObject`.*/
