package io.github.crazysadboi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState implements GameState {
    private float finalGameTime;
    private int finalEnemiesKilled;

    public GameOverState(float gameTime, int enemiesKilled) {
        this.finalGameTime = gameTime;
        this.finalEnemiesKilled = enemiesKilled;
    }

    @Override
    public void initialize() {
        // Ничего не инициализируем
    }

    @Override
    public void update(float deltaTime) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            GameManager.getInstance().setState(new PlayingState());
            System.out.println("Рестарт игры!");
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        try {
            batch.begin();
            font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 30);
            font.draw(batch, "Time: " + (int)finalGameTime + " sec", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
            font.draw(batch, "Kills: " + finalEnemiesKilled, Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 - 15);
            font.draw(batch, "Press R to Restart", Gdx.graphics.getWidth() / 2 - 70, Gdx.graphics.getHeight() / 2 - 60);
            batch.end();
        } catch (Exception e) {
            System.err.println("Ошибка в render GameOverState: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


/*Класс GameOverState представляет состояние "Game Over" в игре DesignPatternsFinalGame, отображая итоговую статистику и позволяя перезапустить игру.

- Поля: `finalGameTime` — общее время игры; `finalEnemiesKilled` — количество убитых врагов.
- Конструктор: Принимает время игры и количество убитых врагов, сохраняя их для отображения.
- Метод `initialize`: Пустой, так как в состоянии "Game Over" ничего не инициализируется.
- Метод `update`: Проверяет нажатие клавиши R и, если нажата, переключает состояние игры на `PlayingState` через `GameManager`.
- Метод `render`: Отрисовывает экран "Game Over", показывая текст "Game Over", статистику (время и убийства) и подсказку о рестарте по клавише R, с обработкой ошибок.

**Шаблон**:
- **State**: GameOverState реализует интерфейс `GameState`, представляя одно из состояний игры (вместе с `PlayingState`),
 что позволяет легко переключаться между состояниями через `GameManager`.*/
