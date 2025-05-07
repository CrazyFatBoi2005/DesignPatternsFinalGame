package io.github.crazysadboi;

import io.github.crazysadboi.gameObjects.GameObject;

import java.util.HashMap;
import java.util.Map;

public class GameObjectHolder {
    private static GameObjectHolder instance;
    private Map<String, GameObject> objects;

    private GameObjectHolder() {
        objects = new HashMap<>();
    }

    public static GameObjectHolder getInstance() {
        if (instance == null) {
            instance = new GameObjectHolder();
        }
        return instance;
    }

    public void registerObject(String key, GameObject object) {
        objects.put(key, object);
    }

    public GameObject getObject(String key) {
        return objects.get(key);
    }

    public void clearDestroyed(){
        objects.entrySet().removeIf(entry -> entry.getValue().isDestroyed());
    }
}

/*Шаблон:
Singleton: Используется через getInstance для создания единственного экземпляра хранилища,
 обеспечивая глобальный доступ к объектам (например, игроку из PlayerMovementStrategy).*/
