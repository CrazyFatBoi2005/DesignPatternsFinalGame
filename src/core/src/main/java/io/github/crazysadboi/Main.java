package io.github.crazysadboi;

import com.badlogic.gdx.ApplicationAdapter;
<<<<<<< Updated upstream
import io.github.crazysadboi.GameManager;
import io.github.crazysadboi.GameEventManager;
=======
>>>>>>> Stashed changes
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter implements EventListener {
    private GameState currentState;

    @Override
    public void create() {
        GameManager.getInstance().initialize();
        GameEventManager.getInstance().addListener(this);

        currentState = new PlayingState();
        currentState.initialize();
    }

    @Override
    public void render() {
        float deltaTime = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        currentState.update(deltaTime);
        currentState.render(GameManager.getInstance().getBatch(), GameManager.getInstance().getFont());
    }

    @Override
    public void dispose() {
        GameManager.getInstance().dispose();
    }

    @Override
    public void onEvent(String event) {
        switch (event) {
            case "gameOver":
                currentState = new GameOverState(((PlayingState) currentState).getEnemiesKilled());
                currentState.initialize();
                break;
            case "restartGame":
                currentState = new PlayingState();
                currentState.initialize();
                break;
            // можно добавить и другие события:
            case "exitGame":
                Gdx.app.exit();
                break;
            case "pauseGame":
                // можно установить флаг паузы
                break;
        }
    }
}
