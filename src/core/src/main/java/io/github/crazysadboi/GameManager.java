package io.github.crazysadboi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameManager extends ApplicationAdapter {
    private static GameManager instance;
    private SpriteBatch batch;
    private BitmapFont font;
    private GameState currentState;

    private GameManager() {
        try {
            batch = new SpriteBatch();
            font = new BitmapFont();
            currentState = new PlayingState();
            System.out.println("GameManager создан");
        } catch (Exception e) {
            System.err.println("Ошибка при создании GameManager: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        try {
            if (Gdx.graphics == null) {
                throw new IllegalStateException("Gdx.graphics не инициализирован!");
            }
            currentState.initialize();
            System.out.println("GameManager.create() вызван");
        } catch (Exception e) {
            System.err.println("Ошибка в GameManager.create: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        try {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            currentState.update(Gdx.graphics.getDeltaTime());
            currentState.render(batch, font);
        } catch (Exception e) {
            System.err.println("Ошибка в GameManager.render: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        try {
            batch.dispose();
            font.dispose();
            System.out.println("GameManager.dispose() вызван");
        } catch (Exception e) {
            System.err.println("Ошибка в GameManager.dispose: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setState(GameState state) {
        try {
            currentState = state;
            currentState.initialize();
            System.out.println("Состояние изменено на: " + state.getClass().getSimpleName());
        } catch (Exception e) {
            System.err.println("Ошибка в setState: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

/*Шаблоны:
Singleton: Используется через getInstance для единственного экземпляра GameManager.
State: Управляет состоянием игры через currentState и setState, хотя в текущем коде используется только PlayingState.*/
