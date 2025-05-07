package io.github.crazysadboi.weapons;

public interface Gun {
    int getDamage();
    void setDamage(int damage);

    float getReloadSpeed();
    void setReloadSpeed(float reloadSpeed);
    void shoot();
    void reloading();
}
