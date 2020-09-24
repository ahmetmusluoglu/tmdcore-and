package turkuvaz.general.apps.podcast.util;

import android.app.Notification;
import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import turkuvaz.general.apps.podcast.event.PlaybackEvent;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */

public class RadioNotificationManager {
    private static final int NOTIFICATION_ID = 1;

    RadioNotificationManager(Context context, MediaSessionCompat mediaSession) {
    }

    void hideNotification() {

    }

    void setStation(Station station) {
    }

    void setPlaybackState(PlaybackEvent playbackState) {
    }

    void setExtraText(String extraText) {
    }

    void clearExtraText() {
    }

    int getNotificationId() {
        return NOTIFICATION_ID;
    }

    Notification getNotification() {
        return null;
    }
}
