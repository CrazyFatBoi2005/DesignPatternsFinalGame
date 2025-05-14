package io.github.crazysadboi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager{
    private Sound shootSound;

    public SoundManager() {
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
    }
    public void playShootSound() {
        shootSound.play();
    }

    public void dispose() {
        shootSound.dispose();
    }
}
