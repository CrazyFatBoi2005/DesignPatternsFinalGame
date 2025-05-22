package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BaseGameObject implements GameObject {
    protected float x, y;
    protected Texture texture;
    protected int layer;
    protected boolean destroyed = false;

    public BaseGameObject(float x, float y, Texture texture, boolean register, int layer) {
        this.x = x;
        this.y = y;
        if (texture != null) {
            this.texture = texture;
        } else {
            this.texture = new Texture("empty.png");
        }
        if (register) {
            GameObjectHolder.getInstance().registerObject(this);
        }
        this.layer = layer;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void destroy() {
        destroyed = true;
        // НЕ освобождаем текстуру, так как она управляется AssetManager
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
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void dispose() {
        // НЕ освобождаем текстуру, так как это делает AssetManager
        texture = null; // Очищаем ссылку для сборки мусора
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!destroyed && texture != null) {
            batch.draw(texture, x, y, 50, 50);
        }
    }
}
