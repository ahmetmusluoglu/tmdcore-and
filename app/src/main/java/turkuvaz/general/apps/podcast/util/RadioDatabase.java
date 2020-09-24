package turkuvaz.general.apps.podcast.util;

import android.content.Context;

import turkuvaz.general.apps.podcast.event.PlaybackEvent;


/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */

public class RadioDatabase {
    public PlaybackEvent playbackState = PlaybackEvent.STOP;
    public Station selectedStation = null;


    public RadioDatabase(Context context) {
    }
}
