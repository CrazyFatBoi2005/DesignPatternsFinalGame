package io.github.crazysadboi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameOverState implements GameState {
    private int finalScore;

    public GameOverState(int score) {
        this.finalScore = score;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            GameEventManager.getInstance().notify("restartGame");
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        batch.begin();
        font.draw(batch, "Game Over!", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 30);
        font.draw(batch, "Score: " + finalScore, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        font.draw(batch, "Press ENTER or R to restart", Gdx.graphics.getWidth() / 2 - 70, Gdx.graphics.getHeight() / 2 - 30);
        batch.end();
    }
}
