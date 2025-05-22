package io.github.crazysadboi.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import io.github.crazysadboi.gameObjects.Block;
import io.github.crazysadboi.gameObjects.Enemy;

import java.util.ArrayList;
import java.util.Random;

public class EnemyFactory implements IEnemyFactory {
    private Texture enemyTexture;
    private float speed;
    private int health;

    public EnemyFactory(Texture enemyTexture, float speed, int health) {
        this.enemyTexture = enemyTexture;
        this.speed = speed;
        this.health = health;
        System.out.println("EnemyFactory initialized with texture: " + (enemyTexture != null ? "Valid" : "Null"));
    }

    @Override
    public Enemy createEnemy(ArrayList<Block> blocks) {
        Random rand = new Random();
        float x, y;
        do {
            x = rand.nextFloat() * Gdx.graphics.getWidth();
            y = rand.nextFloat() * Gdx.graphics.getHeight();
        } while (isOnBlock(x, y, blocks));

        Enemy enemy = new Enemy(x, y, enemyTexture, speed, health);
        System.out.println("Created enemy at (" + x + ", " + y + ") with texture: " + (enemyTexture != null ? "Valid" : "Null"));
        return enemy;
    }

    private boolean isOnBlock(float x, float y, ArrayList<Block> blocks) {
        for (Block block : blocks) {
            if (x < block.getX() + 50 && x + 50 > block.getX() &&
                y < block.getY() + 50 && y + 50 > block.getY()) {
                return true;
            }
        }
        return false;
    }
}
