package io.github.crazysadboi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface GameState {
    void initialize();
    void update(float deltaTime);
    void render(SpriteBatch batch, BitmapFont font);
}
/*Интерфейс GameState определяет контракт для состояний игры в DesignPatternsFinalGame, управляя их поведением.

- Метод `initialize`: Инициализирует состояние (например, загрузка ресурсов или объектов).
- Метод `update`: Обновляет логику состояния с учётом времени (deltaTime).
- Метод `render`: Отрисовывает состояние на экране с использованием `SpriteBatch` и `BitmapFont`.

**Шаблон**:
- **State**: GameState служит базовым интерфейсом для разных состояний (например, `PlayingState`, `GameOverState`),
что позволяет реализовать паттерн состояния для переключения между режимами игры через `GameManager`.*/
