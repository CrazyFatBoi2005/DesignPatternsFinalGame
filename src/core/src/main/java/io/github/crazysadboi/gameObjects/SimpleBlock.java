package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SimpleBlock extends Block {
    public SimpleBlock(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(getTexture(), getX(), getY(), 50, 50);
    }
}
