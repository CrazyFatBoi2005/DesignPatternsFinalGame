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
