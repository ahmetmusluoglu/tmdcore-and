package turkuvaz.general.apps.podcast.util;

import android.content.Context;
import android.media.MediaPlayer;

import turkuvaz.general.apps.base.CoreApp;
import turkuvaz.general.apps.podcast.event.BufferEvent;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class RadioPlayerManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener {

    public RadioPlayerManager(Context context) {
    }

    private void initPlayer() {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
    }

    public void play() {
    }

    public void pause() {
    }

    public void stop() {
    }

    void restart() {
    }

    public void switchStation(Station station) {
    }

    public void setVolume(float volume) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                CoreApp.bus.post(BufferEvent.BUFFERING);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                CoreApp.bus.post(BufferEvent.DONE);
                break;
        }

        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}

