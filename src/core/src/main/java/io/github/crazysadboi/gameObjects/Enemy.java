package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import io.github.crazysadboi.MovementStrategy;

import java.util.ArrayList;

public class Enemy extends BaseGameObject {
    private float attackCooldown;

    public Enemy(float x, float y) {
        super(x, y, new Texture("enemy.png"));
        this.attackCooldown = 0f;
    }

    public void moveTowards(float targetX, float targetY, float deltaTime, MovementStrategy strategy) {
        strategy.move(this, targetX, targetY, deltaTime);
    }

    public boolean isOnBlock(ArrayList<Block> blocks, float blockSize) {
        for (Block block : blocks) {
            if (x < block.getX() + blockSize && x + blockSize > block.getX() &&
                y < block.getY() + blockSize && y + blockSize > block.getY()) {
                return true;
            }
        }
        return false;
    }

    public void destroyBlock(ArrayList<Block> blocks, float blockSize, Player player) {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            Block block = blocks.get(i);
            if (x < block.getX() + blockSize && x + blockSize > block.getX() &&
                y < block.getY() + blockSize && y + blockSize > block.getY()) {
                boolean isUnderPlayer = player.getX() >= block.getX() && player.getX() <= block.getX() + blockSize &&
                    player.getY() >= block.getY() && player.getY() <= block.getY() + blockSize;
                if (!isUnderPlayer) {
                    blocks.remove(i);
                    break;
                }
            }
        }
    }

    public void attackPlayer(Player player, float deltaTime) {
        attackCooldown -= deltaTime;
        if (attackCooldown <= 0) {
            if (x < player.getX() + 50 && x + 50 > player.getX() &&
                y < player.getY() + 50 && y + 50 > player.getY()) {
                player.loseLife();
                attackCooldown = 2f;
            }
        }
    }
}



/*Класс Enemy представляет врагов в игре DesignPatternsFinalGame.
Враги появляются на игровом поле, преследуют игрока, атакуют его и могут разрушать блоки, если находятся на них достаточно долго.

- Поля: `x, y` — координаты врага; `texture` — изображение врага; `timeOnBlock` — время, проведённое на блоке (публичное для доступа); `attackCooldown` — задержка между атаками.
- Конструктор: Инициализирует врага с координатами и текстурой, устанавливая начальные значения времени и кулдауна.
- Реализует интерфейс GameObject (методы `render`, `getX`, `getY`, `setPosition`): `render` рисует врага размером 50x50 пикселей.
- Метод `moveTowards`: Использует стратегию движения (MovementStrategy) для преследования игрока, передавая текущие координаты и время.
- Метод `isOnBlock`: Проверяет, находится ли враг на блоке, сравнивая координаты с массивом блоков.
- Метод `destroyBlock`: Удаляет блок под врагом, если он не занят игроком, после 4 секунд нахождения на нём.
- Метод `attackPlayer`: Атакует игрока каждые 2 секунды, если враг рядом, уменьшая жизни игрока.

**Шаблоны**:
- **Composite**: Enemy реализует GameObject, обеспечивая унификацию с другими объектами.
- **Strategy**: Использует MovementStrategy для гибкого управления движением врагов.*/
