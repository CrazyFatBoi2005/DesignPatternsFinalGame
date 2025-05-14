package io.github.crazysadboi.strategies;

import io.github.crazysadboi.gameObjects.GameObject;

public interface MovementStrategy {
    void move(GameObject object, float deltaX, float deltaY, float deltaTime);
}
/*Интерфейс MovementStrategy определяет контракт для стратегий движения объектов в игре DesignPatternsFinalGame.

- Метод `move`: Устанавливает, как объект (типа GameObject) должен двигаться, принимая объект,
целевые координаты (`deltaX`, `deltaY`) и время (`deltaTime`).

**Шаблон**:
- **Strategy**: MovementStrategy позволяет реализовать различные алгоритмы движения (например, для игрока или врагов),
 обеспечивая гибкость и возможность замены стратегий во время выполнения.*/
