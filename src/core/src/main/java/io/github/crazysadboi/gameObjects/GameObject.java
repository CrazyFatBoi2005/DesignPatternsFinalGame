package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    void render(SpriteBatch batch);
    float getX();
    float getY();
    void destroy();
    void setPosition(float x, float y);
    boolean isDestroyed();
    int getLayer();
    void dispose();
}


