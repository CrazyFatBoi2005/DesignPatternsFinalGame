package io.github.crazysadboi.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Bullet;

public abstract class BaseGun implements Gun {
    protected float reloadTime;
    protected float reloadTimer;
    protected boolean reloading;

    public BaseGun(float reloadTime) {
        this.reloadTime = reloadTime;
        this.reloadTimer = 0;
        this.reloading = false;
    }

    @Override
    public Bullet shoot(float x, float y, Vector2 direction, Texture texture) {
        reloading = true;
        reloadTimer = reloadTime;
        return new Bullet(x, y, direction, texture);
    }

    @Override
    public void update(float deltaTime) {
        if (reloading) {
            reloadTimer -= deltaTime;
            if (reloadTimer <= 0) {
                reloading = false;
            }
        }
    }

    @Override
    public boolean isReloading() {
        return reloading;
    }
}
