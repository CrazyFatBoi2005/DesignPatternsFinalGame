package io.github.crazysadboi.strategies;

import io.github.crazysadboi.gameObjects.GameObject;
import io.github.crazysadboi.gameObjects.Player;

public class PlayerMovementStrategy implements MovementStrategy {
    @Override
    public void move(GameObject object, float deltaX, float deltaY, float deltaTime) {
        Player player = (Player) object;
        player.setPosition(player.getX() + deltaX, player.getY() + deltaY);
    }
}
/*Класс PlayerMovementStrategy определяет стратегию движения игрока в игре DesignPatternsFinalGame.

- Реализует интерфейс MovementStrategy, метод `move` управляет перемещением игрока.
- Метод `move`: Принимает объект (игрока), смещение (`deltaX`, `deltaY`) и время (`deltaTime`, не используется).
Приводит объект к типу Player и обновляет его позицию, добавляя смещение к текущим координатам.

**Шаблон**:
- **Strategy**: PlayerMovementStrategy реализует интерфейс MovementStrategy, предоставляя конкретный алгоритм движения для игрока (прямое перемещение),
что позволяет легко заменить стратегию, если нужно изменить поведение.*/
