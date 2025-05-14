package io.github.crazysadboi.eventSystem;

public interface EventListener {
    void onEvent(String eventType);
}

/*Шаблон: Используется Observer — интерфейс EventListener позволяет классам подписываться на события через GameEventManager,
реализуя паттерн наблюдателя для обработки игровых событий (например, уведомления о смерти врага).
НЕ РЕАЛИЗОВАН*/
