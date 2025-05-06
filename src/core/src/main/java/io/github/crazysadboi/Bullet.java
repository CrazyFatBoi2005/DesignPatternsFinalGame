package io.github.crazysadboi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet implements GameObject {
    private float x, y;
    private Vector2 direction;
    private Texture texture;
    private float speed = 200f;

    public Bullet(float x, float y, Vector2 direction, Texture texture) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.texture = texture;
    }

    public void update(float deltaTime) {
        x += direction.x * speed * deltaTime;
        y += direction.y * speed * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 10, 10);
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
}


/*Поля: x, y — координаты пули; direction — вектор направления движения (Vector2); texture — изображение пули; speed — скорость (200 пикселей/сек).
Конструктор: Принимает начальные координаты, направление и текстуру, инициализируя пулю.
Метод update: Обновляет позицию пули, смещая её по направлению с учётом скорости и времени (deltaTime).
Реализует интерфейс GameObject (методы render, getX, getY, setPosition): render рисует пулю размером 10x10 пикселей, остальные методы управляют координатами.
Шаблон: Используется Composite — Bullet реализует интерфейс GameObject, что позволяет унифицировать работу с пулями, игроками, врагами и блоками через общий интерфейс.*/
