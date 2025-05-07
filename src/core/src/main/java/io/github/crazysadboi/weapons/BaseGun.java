package io.github.crazysadboi.weapons;

public class BaseGun implements Gun{
    private int damage;
    private final float reloadingTime = 1f;
    private float reloadSpeed;
    public BaseGun(){

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
    public void shoot() {
        return;
    }

    @Override
    public void reloading() {

    }
}
