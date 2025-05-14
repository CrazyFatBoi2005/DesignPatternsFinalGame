package io.github.crazysadboi.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Bullet;

public interface Gun {
    Bullet shoot(float x, float y, Vector2 direction, Texture texture);
    void update(float deltaTime);
    boolean isReloading();
}
