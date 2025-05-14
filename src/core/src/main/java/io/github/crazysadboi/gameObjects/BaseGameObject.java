package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;

public abstract class BaseGameObject implements GameObject {
    private float x, y;
    private Texture texture;
    private boolean destroyed;

    public BaseGameObject(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.destroyed = false;
    }

    @Override
    public float getX() {
        return x;
    }

    @Ovrride
    public float getY() {
        return y;
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void destroy() {
        destroyed = true;
        texture.dispose();
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    protected Texture getTexture() {
        return texture;
    }
}
