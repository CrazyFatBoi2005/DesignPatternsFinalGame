package io.github.crazysadboi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameManager {
    private static GameManager instance;
    private SpriteBatch batch;
    private BitmapFont font;

    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void initialize() {
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public void initialize() {
    }
}
