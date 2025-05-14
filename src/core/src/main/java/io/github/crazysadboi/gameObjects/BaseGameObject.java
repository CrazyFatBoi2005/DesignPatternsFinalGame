package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BaseGameObject implements GameObject{
    protected float x, y;
    protected Texture texture;
    protected int layer;
    protected boolean destroyed = false;


    public BaseGameObject(float x, float y, Texture texture, boolean register, int layer){
        this.x = x;
        this.y = y;
        if (texture != null) this.texture = texture;
        else this.texture = new Texture("empty.png");
        if (register) GameObjectHolder.getInstance().registerObject(this);
        this.layer = layer;
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
    public int getLayer() {
        return this.layer;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(!destroyed) batch.draw(texture, x, y, 50, 50);
    }
}

