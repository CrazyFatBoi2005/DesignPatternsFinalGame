package io.github.crazysadboi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameEventManager {
    private static GameEventManager instance;
    private Map<String, ArrayList<EventListener>> listeners;

    private GameEventManager() {
        listeners = new HashMap<>();
    }

    public static GameEventManager getInstance() {
        if (instance == null) {
            instance = new GameEventManager();
        }
        return instance;
    }

    public void subscribe(String eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        listeners.getOrDefault(eventType, new ArrayList<>()).remove(listener);
    }

    public void notify(String eventType) {
        listeners.getOrDefault(eventType, new ArrayList<>()).forEach(listener -> listener.onEvent(eventType));
    }
}



/*Класс GameEventManager управляет системой событий в игре DesignPatternsFinalGame, позволяя уведомлять объекты о происходящих изменениях.

- Поля: `instance` — единственный экземпляр (Singleton); `listeners` — Map, где ключи — типы событий (строки), а значения — списки слушателей (`EventListener`).
- Конструктор: Приватный, инициализирует `listeners` как HashMap, реализуя шаблон Singleton.
- Метод `getInstance`: Возвращает единственный экземпляр класса, создавая его при первом вызове (Singleton).
- Метод `subscribe`: Добавляет слушателя к указанному типу события, создавая новый список, если его нет.
- Метод `unsubscribe`: Удаляет слушателя из списка для указанного типа события.
- Метод `notify`: Оповещает всех слушателей определённого типа события, вызывая их метод `onEvent`.

**Шаблоны**:
- **Singleton**: Используется через `getInstance` для обеспечения единственного экземпляра менеджера событий.
- **Observer**: Реализует паттерн наблюдателя, позволяя объектам подписываться и получать уведомления о событиях (хотя слушатели пока не реализованы).*/
