package turkuvaz.sdk.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ahmet MUŞLUOĞLU on 3/30/2020.
 */
public class IntersitialTimeCache {


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static DateFormat dateFormat;
    private static final String INTERSITIAL_TIME_CACHE = "INTERSITIAL_TIME_CACHE";

    public IntersitialTimeCache(Context context) {
        preferences = context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyyMMddHHmm", new Locale("tr"));
    }

    private void cacheCurrentTime(String currentTime) {
        editor = preferences.edit();
        editor.putString(INTERSITIAL_TIME_CACHE, currentTime);
        editor.apply();
    }

    public boolean isTimeExpired(int minutes) {
        Date date = new Date();
        String currentTime = dateFormat.format(date);
        String cachedTime = preferences.getString(INTERSITIAL_TIME_CACHE, "");
        if (Objects.requireNonNull(cachedTime).isEmpty() || getDifferenceBetweenExpiredTimes(cachedTime, currentTime) > minutes) {// Canlıda ise: "minutes" dakika, Test ise: 3 dakika beklenir.
            cacheCurrentTime(currentTime);
            return true;
        }
        return false;
    }

    private int getDifferenceBetweenExpiredTimes(String start, String end) {
        try {
            Date date1 = dateFormat.parse(start);
            Date date2 = dateFormat.parse(end);
            long diff = date2.getTime() - date1.getTime();
            return (int) TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
