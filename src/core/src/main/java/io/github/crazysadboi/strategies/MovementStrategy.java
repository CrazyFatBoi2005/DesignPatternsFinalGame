package io.github.crazysadboi.strategies;

import io.github.crazysadboi.gameObjects.GameObject;

public interface MovementStrategy {
    void move(GameObject object, float deltaX, float deltaY, float deltaTime);
}
