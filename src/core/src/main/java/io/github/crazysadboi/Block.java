package io.github.crazysadboi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Block implements GameObject {
    private float x, y;
    private Texture texture;

    public Block(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 50, 50);
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


/*Это часть игровой среды, где игрок может ставить или убирать блоки, а враги могут
* взаимодействовать с ними (например, разрушать). Класс реализует интерфейс GameObject,
* что соответствует шаблону проектирования Composite, позволяя унифицировать управление
* различными игровыми объектами (блоки) через общий интерфейс.*/
