package io.github.crazysadboi.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState implements GameState {
    private float finalGameTime;
    private int finalEnemiesKilled;
    private BitmapFont font;
    private GameStateManager gsm;

    public GameOverState(float gameTime, int enemiesKilled, BitmapFont font, GameStateManager gsm) {
        this.finalGameTime = gameTime;
        this.finalEnemiesKilled = enemiesKilled;
        this.font = font;
        this.gsm = gsm;
    }

    @Override
    public void enter() {
        System.out.println("Entered GameOverState");
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gsm.changeState(new PlayingState(gsm, font));
            System.out.println("Restarting game!");
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        try {
            font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f + 30);
            font.draw(batch, "Time: " + (int)finalGameTime + " sec", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f);
            font.draw(batch, "Kills: " + finalEnemiesKilled, Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f - 15);
            font.draw(batch, "Press R to Restart", Gdx.graphics.getWidth() / 2f - 70, Gdx.graphics.getHeight() / 2f - 60);
        } catch (Exception e) {
            System.err.println("Error in GameOverState.render: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        System.out.println("Exiting GameOverState");
    }
}
