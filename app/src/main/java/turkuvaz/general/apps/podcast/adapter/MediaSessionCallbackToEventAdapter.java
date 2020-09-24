package turkuvaz.general.apps.podcast.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;

import turkuvaz.general.apps.base.CoreApp;
import turkuvaz.general.apps.podcast.util.RadioPlayerService;
import turkuvaz.general.apps.podcast.util.Station;
import turkuvaz.general.apps.podcast.event.PlaybackEvent;
import turkuvaz.general.apps.podcast.event.SelectStationEvent;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */
public class MediaSessionCallbackToEventAdapter extends MediaSessionCompat.Callback {

    @Override
    public void onPlay() {
        super.onPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
    }

    @Override
    public boolean onMediaButtonEvent(final Intent mediaButtonIntent) {
        // superclass calls appropriate onPlay/onPause etc.
        return super.onMediaButtonEvent(mediaButtonIntent);
    }
}
