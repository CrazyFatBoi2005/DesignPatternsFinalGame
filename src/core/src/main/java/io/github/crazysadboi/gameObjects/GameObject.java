package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    void render(SpriteBatch batch);
    float getX();
    float getY();
    void destroy();
    void setPosition(float x, float y);

    boolean isDestroyed();
    int getLayer();
}

/**
 * Интерфейс GameObject определяет общие методы для всех игровых объектов
 * (блоки, игроки, враги, пули) в проекте DesignPatternsFinalGame.
 * Используется в рамках шаблона Composite для унификации управления объектами.
 * Реализуйте этот интерфейс для новых объектов, добавляемых в игру.
 */
