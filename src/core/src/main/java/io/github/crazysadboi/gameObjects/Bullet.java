package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends BaseGameObject {
    private Vector2 direction;
    private float speed = 400f;

    public Bullet(float x, float y, Vector2 direction) {
        super(x, y, new Texture("bullet.png"), true, 2);
        this.direction = direction;
    }

    public void update(float deltaTime) {
        x += direction.x * speed * deltaTime;
        y += direction.y * speed * deltaTime;
    }

    @Override
    public void render(SpriteBatch batch) {
        if(!destroyed) batch.draw(texture, x, y, 10, 10);
    }
}


/*Поля: x, y — координаты пули; direction — вектор направления движения (Vector2); texture — изображение пули; speed — скорость (200 пикселей/сек).
Конструктор: Принимает начальные координаты, направление и текстуру, инициализируя пулю.
Метод update: Обновляет позицию пули, смещая её по направлению с учётом скорости и времени (deltaTime).
Реализует интерфейс GameObject (методы render, getX, getY, setPosition): render рисует пулю размером 10x10 пикселей, остальные методы управляют координатами.
Шаблон: Используется Composite — Bullet реализует интерфейс GameObject, что позволяет унифицировать работу с пулями, игроками, врагами и блоками через общий интерфейс.*/
