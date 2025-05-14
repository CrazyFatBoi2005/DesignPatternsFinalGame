package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    void render(SpriteBatch batch);
    float getX();
    float getY();
    void setPosition(float x, float y);
    void destroy();
    boolean isDestroyed();
}
