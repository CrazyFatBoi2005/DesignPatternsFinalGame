package io.github.crazysadboi.weapons;

import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Bullet;

public class BaseGun implements Gun{
    private int damage;
    private final float reloadingTime = 1f;
    private float reloadSpeed;

    // добавить reload time и проверять можно ли выстрелить, в случае, если нет - return null;
    public BaseGun(int damage, float reloadSpeed){
        this.damage = damage;
        this.reloadSpeed = reloadSpeed;
    }
    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public float getReloadSpeed() {
        return reloadSpeed;
    }

    @Override
    public void setReloadSpeed(float reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }

    @Override
    public Bullet shoot(float x, float y, Vector2 direction) {
        return new Bullet(x, y, direction, null);
    }

    @Override
    public void reloading() {

    }

    @Override
    public boolean isReloading() {
        return false;
    }
}
