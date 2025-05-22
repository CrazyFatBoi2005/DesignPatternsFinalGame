package io.github.crazysadboi.utils;

import io.github.crazysadboi.gameObjects.Enemy;
import java.util.ArrayList;
import io.github.crazysadboi.gameObjects.Block;

public interface IEnemyFactory {
    Enemy createEnemy(ArrayList<Block> blocks);
}
