package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;

public abstract class Block extends BaseGameObject {
    private float x, y;
    private Texture texture;

    public Block(float x, float y, Texture texture) {
        super(x, y, texture);
    }
}


/*Это часть игровой среды, где игрок может ставить или убирать блоки, а враги могут
* взаимодействовать с ними (например, разрушать). Класс реализует интерфейс GameObject,
* что соответствует шаблону проектирования Composite, позволяя унифицировать управление
* различными игровыми объектами (блоки) через общий интерфейс.*/
