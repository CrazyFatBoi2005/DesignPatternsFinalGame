package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class Enemy extends BaseGameObject {
    private float speed = 100f;

    public Enemy(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    public void update(float deltaTime) {
        float dx = 640 - getX();
        float dy = 360 - getY();
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
        }
        setPosition(getX() + dx * speed * deltaTime, getY() + dy * speed * deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getTexture(), getX(), getY(), 50, 50);
    }
}
