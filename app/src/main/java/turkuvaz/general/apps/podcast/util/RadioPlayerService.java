package turkuvaz.general.apps.podcast.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/26/2020.
 */

public class RadioPlayerService extends Service {
    public static final String TAG = RadioPlayerService.class.getSimpleName();
    public static final String EXTRA_STATION = Station.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }


}