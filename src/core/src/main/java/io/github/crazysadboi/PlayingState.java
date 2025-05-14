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
    private BitmapFont font;
    private Texture background, blockTexture, playerTexture, heartTexture, enemyTexture, bulletTexture;
    private ArrayList<Block> blocks;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private Player player;
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
    private EnemyFactory enemyFactory;

    @Override
    public void initialize() {
        batch = GameManager.getInstance().getBatch();
        font = new BitmapFont();
        background = new Texture("sea.png");
        blockTexture = new Texture("block.png");
        playerTexture = new Texture("player.png");
        heartTexture = new Texture("heart.png");
        enemyTexture = new Texture("enemy.png");
        bulletTexture = new Texture("bullet.png");

        blocks = new ArrayList<>();
        initialX = Math.round(Gdx.graphics.getWidth() / 2 / 50) * 50;
        initialY = Math.round(Gdx.graphics.getHeight() / 2 / 50) * 50;

        blocks.add(new SimpleBlock(initialX, initialY, blockTexture));
        blocks.add(new SimpleBlock(initialX + 50, initialY, blockTexture));
        blocks.add(new SimpleBlock(initialX, initialY + 50, blockTexture));
        blocks.add(new SimpleBlock(initialX + 50, initialY + 50, blockTexture));

        player = new Player(initialX, initialY, playerTexture);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        enemyFactory = new EnemyFactory(enemyTexture, enemies, blocks);
        for (int i = 0; i < 2; i++) {
            enemyFactory.createEnemy();
        }

        enemiesKilled = 0;
        currentBullets = maxBullets;
    }

    @Override
    public void update(float deltaTime) {
        stateTime += deltaTime;
        enemySpawnTime += deltaTime;
        bulletRechargeTime += deltaTime;
        gameTime += deltaTime;

        float speed = 100f;
        float moveDistance = speed * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) player.move(0, moveDistance, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) player.move(0, -moveDistance, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.move(-moveDistance, 0, new PlayerMovementStrategy());
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.move(moveDistance, 0, new PlayerMovementStrategy());

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentBullets > 0) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            Vector2 direction = new Vector2(mouseX - (player.getX() + 25), mouseY - (player.getY() + 25)).nor();
            bullets.add(new Bullet(player.getX() + 20, player.getY() + 20, direction, bulletTexture));
            currentBullets--;
            GameEventManager.getInstance().notify("bulletFired");
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
            enemyFactory.createEnemy();
            enemySpawnTime = 0f;
            GameEventManager.getInstance().notify("enemySpawned");
        }

        if (player.getLives() <= 0) {
            if (enemiesKilled > recordEnemiesKilled) {
                recordEnemiesKilled = enemiesKilled;
            }
            GameEventManager.getInstance().notify("gameOver");
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            float blockX = Math.round(mouseX / 50) * 50;
            float blockY = Math.round(mouseY / 50) * 50;
            if (!blockExists(blockX, blockY)) {
                blocks.add(new SimpleBlock(blockX, blockY, blockTexture));
                GameEventManager.getInstance().notify("blockPlaced");
            }
        }

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
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        for (Block block : blocks) block.render(batch);
        boolean isOnBlock = isPlayerOnBlock();
        if (!isOnBlock && timeOffBlock > 0) {
            float alpha = 0.5f + 0.5f * (float) Math.sin(stateTime * 5);
            batch.setColor(1, 1, 1, alpha);
            player.render(batch);
            batch.setColor(1, 1, 1, 1);
        } else {
            player.render(batch);
        }
        for (Enemy enemy : enemies) enemy.render(batch);
        for (Bullet bullet : bullets) bullet.render(batch);
        for (int i = 0; i < player.getLives(); i++) {
            batch.draw(heartTexture, 10 + i * 40, Gdx.graphics.getHeight() - 40, 32, 32);
        }
        font.draw(batch, "Time: " + (int) gameTime + " sec", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Kills: " + enemiesKilled, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 25);
        font.draw(batch, "Record: " + recordEnemiesKilled, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 40);
        font.draw(batch, "Bullets: " + currentBullets + "/" + maxBullets, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 55);
        batch.end();
    }

    public void dispose() {
        background.dispose();
        blockTexture.dispose();
        playerTexture.dispose();
        heartTexture.dispose();
        enemyTexture.dispose();
        bulletTexture.dispose();
        font.dispose();
        player.destroy();
        for (Enemy enemy : enemies) enemy.destroy();
        for (Bullet bullet : bullets) bullet.destroy();
        for (Block block : blocks) block.destroy();
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
                    break;
                }
            }
        }
    }

    private void updateEnemies(float deltaTime) {
        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
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

    public int getEnemiesKilled() {
        return enemiesKilled;
    }
}
