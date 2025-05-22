package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import io.github.crazysadboi.strategies.MovementStrategy;
import java.util.ArrayList;

public class Enemy extends BaseGameObject {
    private float attackCooldown;
    private float speed;
    private int health;

    public Enemy(float x, float y, Texture texture, float speed, int health) {
        super(x, y, texture, true, 2);
        this.attackCooldown = 0f;
        this.speed = speed;
        this.health = health;
    }

    public float getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            destroy();
        }
    }

    public void moveTowards(float targetX, float targetY, float deltaTime, MovementStrategy strategy) {
        strategy.move(this, targetX, targetY, deltaTime);
    }

    public boolean isOnBlock(ArrayList<Block> blocks, float blockSize) {
        for (Block block : blocks) {
            if (x < block.getX() + blockSize && x + blockSize > block.getX() &&
                y < block.getY() + blockSize && y + blockSize > block.getY()) {
                return true;
            }
        }
        return false;
    }

    public void destroyBlock(ArrayList<Block> blocks, float blockSize, Player player) {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            if (x < block.getX() + blockSize && x + blockSize > block.getX() &&
                y < block.getY() + blockSize && y + blockSize > block.getY()) {
                boolean isUnderPlayer = player.getX() >= block.getX() && player.getX() <= block.getX() + blockSize &&
                    player.getY() >= block.getY() && player.getY() <= block.getY() + blockSize;
                if (!isUnderPlayer) {
                    blocks.get(i).destroy();
                    blocks.remove(i);
                    this.destroy();
                    break;
                }
            }
        }
    }

    public void attackPlayer(Player player, float deltaTime) {
        attackCooldown -= deltaTime;
        if (attackCooldown <= 0) {
            if (x < player.getX() + 50 && x + 50 > player.getX() &&
                y < player.getY() + 50 && y + 50 > player.getY()) {
                player.loseLife();
                destroy();
            }
            attackCooldown = 2f; // Сбрасываем кулдаун после атаки
        }
    }
}
