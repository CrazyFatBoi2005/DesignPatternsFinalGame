package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public class Bullet extends BaseGameObject {
    private Vector2 direction;
    private float speed = 300f;

    public Bullet(float x, float y, Vector2 direction, Texture texture) {
        super(x, y, texture);
        this.direction = direction.nor();
    }

    public void update(float deltaTime) {
        setPosition(getX() + direction.x * speed * deltaTime, getY() + direction.y * speed * deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getTexture(), getX(), getY(), 10, 10);
    }
}
