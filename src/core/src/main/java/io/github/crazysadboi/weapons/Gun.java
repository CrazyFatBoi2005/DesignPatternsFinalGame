package io.github.crazysadboi.weapons;

import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Bullet;

public interface Gun {
    int getDamage();
    void setDamage(int damage);

    float getReloadSpeed();
    void setReloadSpeed(float reloadSpeed);
    Bullet shoot(float x, float y, Vector2 direction);
    void reloading();

    boolean isReloading();
}
