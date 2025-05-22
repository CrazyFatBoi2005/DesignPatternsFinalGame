package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import io.github.crazysadboi.strategies.MovementStrategy;

public class Player extends BaseGameObject {
    private int lives;

    public Player(float startX, float startY) {
        super(startX, startY, new Texture("player.png"), false, 2);
        this.lives = 5;
    }

    public void move(float deltaX, float deltaY, MovementStrategy strategy) {
        strategy.move(this, deltaX, deltaY, 0);
    }

    public void loseLife() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

}
