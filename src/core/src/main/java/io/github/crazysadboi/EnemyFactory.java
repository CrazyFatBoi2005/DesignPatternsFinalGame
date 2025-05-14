package io.github.crazysadboi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import io.github.crazysadboi.gameObjects.Block;
import io.github.crazysadboi.gameObjects.Enemy;

import java.util.ArrayList;

public class EnemyFactory {
    private Texture enemyTexture;
    private ArrayList<Enemy> enemies;
    private ArrayList<Block> blocks;

    public EnemyFactory(Texture enemyTexture, ArrayList<Enemy> enemies, ArrayList<Block> blocks) {
        this.enemyTexture = enemyTexture;
        this.enemies = enemies;
        this.blocks = blocks;
    }

    public Enemy createEnemy() {
        float x, y;
        boolean validPosition;
        int attempts = 0;
        final int maxAttempts = 100;

        do {
            x = MathUtils.random(0, Gdx.graphics.getWidth() - 50);
            y = MathUtils.random(0, Gdx.graphics.getHeight() - 50);
            validPosition = true;

            for (Block block : blocks) {
                if (x < block.getX() + 50 && x + 50 > block.getX() &&
                    y < block.getY() + 50 && y + 50 > block.getY()) {
                    validPosition = false;
                    break;
                }
            }
            attempts++;
            if (attempts >= maxAttempts) {
                x = 0;
                y = 0;
                break;
            }
        } while (!validPosition);

        Enemy enemy = new Enemy(x, y, enemyTexture);
        enemies.add(enemy);
        return enemy;
    }
}

/*Класс EnemyFactory отвечает за создание врагов в игре DesignPatternsFinalGame.
Он генерирует врагов в случайных местах игрового поля, избегая пересечения с блоками.

- Поля: `enemyTexture` — текстура для врагов; `enemies` — список всех врагов в игре; `blocks` — список блоков для проверки пересечений.
- Конструктор: Принимает текстуру врагов, список врагов и список блоков, чтобы использовать их при создании.
- Метод `createEnemy`: Генерирует случайные координаты (`x`, `y`) в пределах экрана (с учётом размера 50x50). Проверяет, чтобы враг не пересекался с блоками,
и делает до 100 попыток найти валидную позицию. Если попытки исчерпаны, ставит врага в точку (0,0).
 Создаёт нового врага с текстурой, добавляет его в список врагов и возвращает.

**Шаблон**: Используется **Factory Method** — класс EnemyFactory предоставляет метод для создания объектов Enemy, инкапсулируя логику их генерации (включая проверку позиций).*/
