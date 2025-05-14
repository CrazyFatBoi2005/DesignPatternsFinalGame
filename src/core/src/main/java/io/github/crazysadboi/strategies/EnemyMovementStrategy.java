package io.github.crazysadboi.strategies;

import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Enemy;
import io.github.crazysadboi.gameObjects.GameObject;

public class EnemyMovementStrategy implements MovementStrategy {
    @Override
    public void move(GameObject object, float deltaX, float deltaY, float deltaTime) {
        Enemy enemy = (Enemy) object;
        float targetX = deltaX;
        float targetY = deltaY;
        Vector2 direction = new Vector2(targetX - (enemy.getX() + 25), targetY - (enemy.getY() + 25)).nor();
        enemy.setPosition(enemy.getX() + direction.x * 50 * deltaTime, enemy.getY() + direction.y * 50 * deltaTime);
    }
}

/*Класс EnemyMovementStrategy определяет стратегию движения врагов в игре DesignPatternsFinalGame. Он отвечает за то, как враги преследуют игрока.

- Реализует интерфейс MovementStrategy, метод `move` управляет движением врага.
- Метод `move`: Принимает объект (врага), целевые координаты (`deltaX`, `deltaY` — позиция игрока) и время (`deltaTime`).
Приводит объект к типу Enemy, вычисляет направление движения через Vector2 (нормализованный вектор от врага к цели с учётом смещения центра 25 пикселей),
 и перемещает врага со скоростью 50 пикселей/сек, корректируя позицию с учётом времени.

**Шаблон**: Используется **Strategy** — EnemyMovementStrategy реализует интерфейс MovementStrategy, позволяя гибко менять алгоритм движения врагов.*/
