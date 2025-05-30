package io.github.crazysadboi.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.assets.AssetManager;
import io.github.crazysadboi.eventSystem.GameEventManager;
import io.github.crazysadboi.eventSystem.SoundListener;
import io.github.crazysadboi.gameObjects.*;
import io.github.crazysadboi.strategies.EnemyMovementStrategy;
import io.github.crazysadboi.strategies.PlayerMovementStrategy;
import io.github.crazysadboi.utils.EnemyFactory;
import io.github.crazysadboi.utils.IEnemyFactory;
import io.github.crazysadboi.utils.SoundManager;
import io.github.crazysadboi.gameObjects.GameObjectHolder;

import java.util.ArrayList;

public class PlayingState implements GameState {
    private SpriteBatch batch;
    private BitmapFont font;
    private AssetManager assetManager;
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
    private float blockBuildCooldown = 6f;
    private boolean gameOver = false;
    private IEnemyFactory enemyFactory;

    private GameStateManager gsm;

    public PlayingState(GameStateManager gsm, BitmapFont font) {
        this.gsm = gsm;
        this.font = font;
        this.assetManager = new AssetManager();
    }

    @Override
    public void enter() {
        try {
            batch = new SpriteBatch();
            font = new BitmapFont();
            System.out.println("SpriteBatch and BitmapFont initialized");

            assetManager.load("sea.png", Texture.class);
            assetManager.load("heart.png", Texture.class);
            assetManager.load("enemy.png", Texture.class);
            System.out.println("Texture loading initiated");
            assetManager.finishLoading();
            System.out.println("Texture loading completed");

            if (assetManager.isLoaded("enemy.png")) {
                System.out.println("Enemy texture loaded successfully");
            } else {
                System.err.println("Failed to load enemy texture");
                throw new RuntimeException("Enemy texture not loaded");
            }

            blocks = new ArrayList<>();
            initialX = Math.round((float) Gdx.graphics.getWidth() / 2 / 50) * 50;
            initialY = Math.round((float) Gdx.graphics.getHeight() / 2 / 50) * 50;
            blocks.add(new Block(initialX, initialY));
            blocks.add(new Block(initialX + 50, initialY));
            blocks.add(new Block(initialX, initialY + 50));
            blocks.add(new Block(initialX + 50, initialY + 50));
            System.out.println("Blocks initialized");

            eventManager = GameEventManager.getInstance();
            soundManager = new SoundManager();
            soundListener = new SoundListener(soundManager);
            eventManager.subscribe("PLAYER_SHOOT", soundListener);
            System.out.println("Event and sound systems initialized");

            player = new Player(initialX, initialY);
            System.out.println("Player initialized");

            enemies = new ArrayList<>();
            enemyFactory = new EnemyFactory(assetManager.get("enemy.png", Texture.class), 100f, 1);
            for (int i = 0; i < 2; i++) {
                enemies.add(enemyFactory.createEnemy(blocks));
                System.out.println("Created enemy " + (i + 1));
            }
            bullets = new ArrayList<>();
            System.out.println("Enemies and bullets initialized");

            enemiesKilled = 0;
            currentBullets = maxBullets;
            if (enemiesKilled > recordEnemiesKilled) {
                recordEnemiesKilled = enemiesKilled;
            }
        } catch (Exception e) {
            System.err.println("Error in PlayingState.enter: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize PlayingState", e);
        }
    }

    @Override
    public void update(float deltaTime) {
        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                gameOver = false;
                gsm.changeState(new GameOverState(gameTime, enemiesKilled, font, gsm));
                return;
            }
            return;
        }

        stateTime += deltaTime;
        blockBuildCooldown += deltaTime;
        enemySpawnTime += deltaTime;
        bulletRechargeTime += deltaTime;
        gameTime += deltaTime;

        float speed = 100f;
        float moveDistance = speed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.move(0, moveDistance, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.move(0, -moveDistance, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.move(-moveDistance, 0, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.move(moveDistance, 0, new PlayerMovementStrategy());

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentBullets > 0) {
            System.out.println("PEW");
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            Vector2 direction = new Vector2(mouseX - (player.getX() + 25), mouseY - (player.getY() + 25)).nor();
            bullets.add(new Bullet(player.getX() + 25 - 5, player.getY() + 25 - 5, direction));
            currentBullets--;
            eventManager.notify("PLAYER_SHOOT");
        }

        if (bulletRechargeTime >= 0.5f && currentBullets < maxBullets) {
            currentBullets++;
            bulletRechargeTime = 0f;
            GameEventManager.getInstance().notify("bulletRecharged");
        }

        updateBullets(deltaTime);
        updateEnemies(deltaTime);

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

        if (enemySpawnTime >= 10f) {
            Enemy newEnemy = enemyFactory.createEnemy(blocks);
            enemies.add(newEnemy);
            System.out.println("Spawned new enemy: " + (newEnemy != null ? "Enemy created" : "Enemy is null"));
            enemySpawnTime = 0f;
            GameEventManager.getInstance().notify("enemySpawned");
        }

        if (player.getLives() <= 0) {
            gameOver = true;
            if (enemiesKilled > recordEnemiesKilled) {
                recordEnemiesKilled = enemiesKilled;
            }
            GameEventManager.getInstance().notify("gameOver");
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            if (blockBuildCooldown >= 6f) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                float blockX = Math.round(mouseX / 50) * 50;
                float blockY = Math.round(mouseY / 50) * 50;
                if (!blockExists(blockX, blockY)) {
                    blocks.add(new Block(blockX, blockY));
                    GameEventManager.getInstance().notify("blockPlaced");
                    blockBuildCooldown = 0f;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        try {
            // Временно отключаем очистку уничтоженных объектов для теста
            // GameObjectHolder.getInstance().clearDestroyed();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.draw(assetManager.get("enemy.png", Texture.class), 10, Gdx.graphics.getHeight() - 60, 50, 50);
            batch.draw(assetManager.get("sea.png", Texture.class), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            GameObjectHolder.getInstance().renderObjects(batch);

            boolean isOnBlock = isPlayerOnBlock();
            if (!isOnBlock && timeOffBlock > 0) {
                float alpha = 0.5f + 0.5f * (float) Math.sin(stateTime * 5);
                batch.setColor(1, 1, 1, alpha);
                player.render(batch);
                batch.setColor(1, 1, 1, 1);
            } else {
                player.render(batch);
            }

            for (int i = 0; i < player.getLives(); i++) {
                batch.draw(assetManager.get("heart.png", Texture.class), 10 + i * 40, Gdx.graphics.getHeight() - 40, 32, 32);
            }
            font.draw(batch, "Time: " + (int) gameTime + " sec", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 10);
            font.draw(batch, "Kills: " + enemiesKilled, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 25);
            font.draw(batch, "Record: " + recordEnemiesKilled, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 40);
            font.draw(batch, "Bullets: " + currentBullets + "/" + maxBullets, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 55);

            if (gameOver) {
                font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 30);
                font.draw(batch, "Time: " + (int) gameTime + " sec", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
                font.draw(batch, "Kills: " + enemiesKilled, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 15);
                font.draw(batch, "Record: " + recordEnemiesKilled, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 30);
                font.draw(batch, "Press R to Restart", Gdx.graphics.getWidth() / 2 - 70, Gdx.graphics.getHeight() / 2 - 60);
            }
        } catch (Exception e) {
            System.err.println("Error in PlayingState.render: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to render PlayingState", e);
        }
    }

    @Override
    public void exit() {
        dispose();
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
                    enemy.takeDamage(1);
                    bullets.get(i).destroy();
                    bullets.remove(i);
                    if (enemy.isDestroyed()) {
                        enemies.remove(j);
                        enemiesKilled++;
                        Enemy newEnemy = enemyFactory.createEnemy(blocks);
                        enemies.add(newEnemy);
                        System.out.println("Enemy killed, new enemy spawned: " + (newEnemy != null ? "Enemy created" : "Enemy is null"));
                        GameEventManager.getInstance().notify("enemyKilled");
                    }
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

    public void dispose() {
        try {
            if (batch != null) {
                batch.dispose();
                batch = null;
            }
            if (font != null) {
                font.dispose();
                font = null;
            }
            if (assetManager != null) {
                assetManager.dispose();
                assetManager = null;
            }

            GameObjectHolder.getInstance().dispose();

            if (blocks != null) {
                blocks.clear();
                blocks = null;
            }
            if (enemies != null) {
                enemies.clear();
                enemies = null;
            }
            if (bullets != null) {
                bullets.clear();
                bullets = null;
            }

            if (eventManager != null && soundListener != null) {
                eventManager.unsubscribe("PLAYER_SHOOT", soundListener);
            }

            player = null;
            enemyFactory = null;
            eventManager = null;
            soundListener = null;

            System.out.println("PlayingState disposed");
        } catch (Exception e) {
            System.err.println("Error in PlayingState.dispose: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
