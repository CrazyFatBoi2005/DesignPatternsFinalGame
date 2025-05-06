package io.github.crazysadboi;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    void render(SpriteBatch batch);
    float getX();
    float getY();
    void setPosition(float x, float y);
}

/**
 * Интерфейс GameObject определяет общие методы для всех игровых объектов
 * (блоки, игроки, враги, пули) в проекте DesignPatternsFinalGame.
 * Используется в рамках шаблона Composite для унификации управления объектами.
 * Реализуйте этот интерфейс для новых объектов, добавляемых в игру.
 */
