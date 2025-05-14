package io.github.crazysadboi.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameState {
    void enter();
    void update(float delta);
    void render(SpriteBatch batch);
    void exit();
}

/*Интерфейс GameState определяет контракт для состояний игры в DesignPatternsFinalGame, управляя их поведением.

- Метод `initialize`: Инициализирует состояние (например, загрузка ресурсов или объектов).
- Метод `update`: Обновляет логику состояния с учётом времени (deltaTime).
- Метод `render`: Отрисовывает состояние на экране с использованием `SpriteBatch` и `BitmapFont`.

**Шаблон**:
- **State**: GameState служит базовым интерфейсом для разных состояний (например, `PlayingState`, `GameOverState`),
что позволяет реализовать паттерн состояния для переключения между режимами игры через `GameManager`.*/
