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
        // Можно сюда добавить логику инициализации, если нужна
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gsm.changeState(new PlayingState(gsm, font));
            System.out.println("Рестарт игры!");
        }
    }

    private void dispose() {
    }

    @Override
    public void render(SpriteBatch batch) {
        try {
            batch.begin();
            font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f + 30);
            font.draw(batch, "Time: " + (int)finalGameTime + " sec", Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f);
            font.draw(batch, "Kills: " + finalEnemiesKilled, Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f - 15);
            font.draw(batch, "Press R to Restart", Gdx.graphics.getWidth() / 2f - 70, Gdx.graphics.getHeight() / 2f - 60);
            batch.end();
        } catch (Exception e) {
            System.err.println("Ошибка в render GameOverState: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void exit() {
        // Очистка ресурсов, если нужно
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
