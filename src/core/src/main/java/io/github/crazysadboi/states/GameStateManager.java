package io.github.crazysadboi.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {
    private GameState currentState;

    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter();
    }

    public void update(float delta) {
        if (currentState != null) currentState.update(delta);
    }

    public void render(SpriteBatch batch) {
        if (currentState != null) currentState.render(batch);
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
