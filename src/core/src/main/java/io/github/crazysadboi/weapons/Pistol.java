package io.github.crazysadboi.weapons;

import com.badlogic.gdx.math.Vector2;
import io.github.crazysadboi.gameObjects.Bullet;

public class Pistol extends BaseGun{
    public Pistol(int damage, float reloadSpeed){
        super(damage, reloadSpeed);
    }

    @Override
    public Bullet shoot(float x, float y, Vector2 direction) {
//        player.getX() + 25 - 5, player.getY() + 25 - 5, direction, bulletTexture
        return super.shoot(x, y, direction);
    }
}
