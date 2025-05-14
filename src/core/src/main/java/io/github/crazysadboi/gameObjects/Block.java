package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Block extends BaseGameObject {

    public Block(float x, float y){
        super(x, y, new Texture("block.png"), true, 1);

    }
}


/*Это часть игровой среды, где игрок может ставить или убирать блоки, а враги могут
* взаимодействовать с ними (например, разрушать). Класс реализует интерфейс GameObject,
* что соответствует шаблону проектирования Composite, позволяя унифицировать управление
* различными игровыми объектами (блоки) через общий интерфейс.*/
