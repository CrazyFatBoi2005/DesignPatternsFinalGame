package io.github.crazysadboi.strategies;

import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Enemy;
import io.github.crazysadboi.gameObjects.GameObject;

public class EnemyMovementStrategy implements MovementStrategy {
    @Override
    public void move(GameObject object, float targetX, float targetY, float deltaTime) {
        Enemy enemy = (Enemy) object;
        Vector2 direction = new Vector2(targetX - (enemy.getX() + 25),
            targetY - (enemy.getY() + 25)).nor();
        enemy.setPosition(enemy.getX() + direction.x * 50 * deltaTime,
            enemy.getY() + direction.y * 50 * deltaTime);
    }
}
