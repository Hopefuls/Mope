package me.hope.franxxmin.listeners.Music;

import me.hope.franxxmin.MusicPack.Music;
import org.javacord.api.event.audio.AudioSourceFinishedEvent;
import org.javacord.api.listener.audio.AudioSourceFinishedListener;

public class FinishedListener implements AudioSourceFinishedListener {
    @Override
    public void onAudioSourceFinished(AudioSourceFinishedEvent event) {
        event.getConnection().close();
        Music.audioSourceHashMap.remove(event.getServer());

    }
}
