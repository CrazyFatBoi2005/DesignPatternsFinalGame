package io.github.crazysadboi.eventSystem;

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


