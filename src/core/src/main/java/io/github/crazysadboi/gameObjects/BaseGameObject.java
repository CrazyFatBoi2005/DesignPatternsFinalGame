package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BaseGameObject implements GameObject{
    private float x, y;
    private Texture texture;
    private boolean destroyed = false;


    public BaseGameObject(float x, float y, Texture texture){
        this.x = x;
        this.y = y;
        this.texture = texture;
    }
    @Override
    public float getX() { return x; }
    @Override
    public float getY() { return y; }

    @Override
    public void destroy() {
        if (texture != null) {
            texture.dispose();
            texture = null;
        }
        destroyed = true;
    }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 50, 50);
    }
}

