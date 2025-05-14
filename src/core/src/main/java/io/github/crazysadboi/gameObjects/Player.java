package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.github.tommyettinger.digital.Base;
import io.github.crazysadboi.MovementStrategy;
import io.github.crazysadboi.weapons.BaseGun;

public class Player extends BaseGameObject {
    private float x, y;
    private int lives;
    private Texture texture;
    private BaseGun weapon;

    public Player(float startX, float startY, Texture texture, BaseGun weapon) {
        super(startX, startY, texture);
        this.lives = 5;
        this.weapon = weapon;
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

    public Bullet shoot(Vector2 direction){
        return weapon.shoot(x, y, direction);
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
