package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
<<<<<<< Updated upstream
=======
import com.badlogic.gdx.math.Vector2;
>>>>>>> Stashed changes
import io.github.crazysadboi.MovementStrategy;

public class Player extends BaseGameObject {
    private float x, y;
    private int lives;
    private Texture texture;

    public Player(float startX, float startY, Texture texture) {
        super(startX, startY, texture);
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

    @Override
    public float getX() { return x; }
    @Override
    public float getY() { return y; }

    @Override
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 50, 50);
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
