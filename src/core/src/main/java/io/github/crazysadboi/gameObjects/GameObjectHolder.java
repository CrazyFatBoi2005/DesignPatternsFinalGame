package io.github.crazysadboi.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameObjectHolder {
    private static GameObjectHolder instance;
    private Map<Integer, GameObject> objects;
    private Integer objectId;

    private GameObjectHolder() {
        objects = new HashMap<>();
        objectId = 0;
    }

    public static GameObjectHolder getInstance() {
        if (instance == null) {
            instance = new GameObjectHolder();
        }
        return instance;
    }

    public void registerObject(GameObject object) {
        objects.put(objectId, object);
        objectId += 1;
    }

    public GameObject getObject(Integer key) {
        return objects.get(key);
    }

    public void clearDestroyed(){
        objects.entrySet().removeIf(entry -> entry.getValue().isDestroyed());
    }

    public void renderObjects(SpriteBatch batch) {
        ArrayList<GameObject> sortedByLayer = new ArrayList<>(objects.values());
        Collections.sort(sortedByLayer, (obj1, obj2) -> Integer.compare(obj1.getLayer(), obj2.getLayer()));
        for (GameObject object : objects.values()) {
            if (!object.isDestroyed()) {
                object.render(batch);
            }
        }
    }
}

/*Шаблон:
Singleton: Используется через getInstance для создания единственного экземпляра хранилища,
 обеспечивая глобальный доступ к объектам (например, игроку из PlayerMovementStrategy).*/
