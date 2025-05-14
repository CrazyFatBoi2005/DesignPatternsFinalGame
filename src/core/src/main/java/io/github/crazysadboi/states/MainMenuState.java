package io.github.crazysadboi.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenuState implements GameState {
    private GameStateManager gsm;
    private BitmapFont font;

    public MainMenuState(GameStateManager gsm, BitmapFont font) {
        this.gsm = gsm;
        this.font = font;
    }

    @Override
    public void enter() {
        System.out.println("Entered MainMenuState");
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            gsm.changeState(new PlayingState(gsm, font));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        font.draw(batch, "Press 'SPACE' to start game",
            Gdx.graphics.getWidth() / 2f - 100,
            Gdx.graphics.getHeight() / 2f);
    }

    @Override
    public void exit() {
        System.out.println("Exiting MainMenuState");
    }
}
