package io.github.crazysadboi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.eventSystem.GameEventManager;
import io.github.crazysadboi.eventSystem.SoundListener;
import io.github.crazysadboi.gameObjects.*;
import io.github.crazysadboi.states.GameState;
import io.github.crazysadboi.states.GameStateManager;
import io.github.crazysadboi.states.MainMenuState;
import io.github.crazysadboi.strategies.EnemyMovementStrategy;
import io.github.crazysadboi.strategies.PlayerMovementStrategy;
import io.github.crazysadboi.utils.EnemyFactory;
import io.github.crazysadboi.utils.SoundManager;

import java.util.ArrayList;

public class Main extends ApplicationAdapter{
    private SpriteBatch batch;
    private BitmapFont font;
    private GameStateManager gsm;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        gsm = new GameStateManager();
        gsm.changeState(new MainMenuState(gsm, font)); // Начинаем с меню
    }

    @Override
    public void render() {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        gsm.update(delta);

        batch.begin();
        gsm.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        // Можно вызвать dispose у состояний, если нужно
    }
}

/*Используемый шаблон проектирования:
 * - **Шаблон "Состояние" (State Pattern)**: интерфейс GameState может быть частью реализации различных состояний игры.
 * - **Шаблон "Стратегия" (Strategy Pattern)**: используется для управления движением игрока и врагов
 *   через PlayerMovementStrategy и EnemyMovementStrategy.
 * - **Шаблон "Фабрика" (Factory Pattern)**: для создания врагов применяется EnemyFactory.
 * - **Синглтон (Singleton Pattern)**: GameEventManager — синглтон, который позволяет централизованно обрабатывать события (например, "bulletFired", "enemyKilled").
 *
 * Основная логика:
 * - Управление движением игрока (клавиши: стрелки).
 * - Стрельба (пробел), ограниченный боезапас и перезарядка.
 * - Враги спавнятся, двигаются к игроку и разрушают блоки.
 * - Игрок может размещать и удалять блоки.
 * - Потеря жизней, если игрок не стоит на блоке.
 * - Подсчёт убитых врагов и отображение состояния игры (время, жизни, пули).
 *
 * Игра завершается, когда у игрока заканчиваются жизни.
 * Нажатие R перезапускает игру.
 */
