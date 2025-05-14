package io.github.crazysadboi.eventSystem;

import io.github.crazysadboi.SoundManager;

public class SoundListener implements EventListener {

    private final SoundManager soundManager;

    public SoundListener(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    @Override
    public void onEvent(String eventType) {
        if ("PLAYER_SHOOT".equals(eventType)) {
            soundManager.playShootSound();
        }
    }
}
