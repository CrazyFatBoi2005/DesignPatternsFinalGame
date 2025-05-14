package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.crazysadboi.MovementStrategy;

public class Player extends BaseGameObject {
    private int lives;

    public Player(float startX, float startY) {
        super(startX, startY, new Texture("player.png"), false, 2);
        this.lives = 5;
    }

    public void move(float deltaX, float deltaY, MovementStrategy strategy) {
        strategy.move(this, deltaX, deltaY, 0);
    }

    public void loseLife() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

}
/*
 * Player.java
 *
 * Этот класс представляет игрока в игре.
 * Реализует интерфейс GameObject, предоставляя координаты, метод отрисовки и управление позицией.
 *
 * Используемый шаблон проектирования:
 * - **Стратегия (Strategy Pattern)**: для перемещения игрока используется объект MovementStrategy,
 *   который инкапсулирует логику движения. Это позволяет легко подменить стратегию поведения при необходимости.
 *
 * Основные функции:
 * - Хранит позицию игрока (x, y) и количество жизней.
 * - Реализует метод move(), делегируя логику перемещения стратегии.
 * - Метод loseLife() уменьшает количество жизней.
 * - Метод render() отрисовывает игрока с помощью переданной текстуры.
 */
