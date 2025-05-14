package io.github.crazysadboi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface GameState {
    void initialize();
    void update(float deltaTime);
    void render(SpriteBatch batch, BitmapFont font);
}
