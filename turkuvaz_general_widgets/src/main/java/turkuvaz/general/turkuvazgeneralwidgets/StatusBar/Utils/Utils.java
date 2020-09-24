package turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsAppRef;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 17/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class Utils {
    public static PrayerEnums getCurrentPrayer(String fajrTime, String sunriseTime, String dhuhrTime, String asrTime, String maghribTime, String ishaTime) {

        PrayerEnums currentPrayer = null;

        Date fajr = stringToDate(fajrTime);
        Date sunrise = stringToDate(sunriseTime);
        Date dhuhr = stringToDate(dhuhrTime);
        Date asr = stringToDate(asrTime);
        Date maghrib = stringToDate(maghribTime);
        Date isha = stringToDate(ishaTime);

        try {
            if ((fajr.before(new Date()) && sunrise.after(new Date()))) {
                currentPrayer = PrayerEnums.SUNSHINE;
            } else if ((sunrise.before(new Date()) && dhuhr.after(new Date()))) {
                currentPrayer = PrayerEnums.DHUHR;
            } else if ((dhuhr.before(new Date()) && asr.after(new Date()))) {
                currentPrayer = PrayerEnums.ASR;
            } else if ((asr.before(new Date()) && maghrib.after(new Date()))) {
                currentPrayer = PrayerEnums.MAGHRIB;
            } else if ((maghrib.before(new Date()) && isha.after(new Date()))) {
                currentPrayer = PrayerEnums.ISHA;
            } else {
                currentPrayer = PrayerEnums.FAJR;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentPrayer;
    }


    private static Date stringToDate(String prayerTime) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = format.parse(prayerTime);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(String irregularDate) {
        String cleanDate = "NONE.";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);

            format = new SimpleDateFormat("HH:mm");
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }

    public static String convertDate(String irregularDate, Context context) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM, yyyy", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cleanDate;
    }

    public static int isUpExchangeColor(Context context, Double last, Double current, boolean bist) {
        if (current > last)
            return ResourcesCompat.getColor(context.getResources(), R.color.exchange_up, null);

        else if (current < last)
            return ResourcesCompat.getColor(context.getResources(), R.color.exchange_down, null);
        else {
            if (bist)// bist kapalıyken fikriyatta bu beyaz olduğu için görünmüyordu. borsa kapalıyken herhangi bir rengi olmadığı durumlarda pref'ten alacak
                return Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz))));
            else
                return ResourcesCompat.getColor(context.getResources(), R.color.exchange_stab, null);
        }
    }

    public static Drawable isUpExchangeImage(Context context, Double last, Double current) {
        if (current > last)
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_up_arrow, null);
        else if (current < last)
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_down_arrow, null);
        else
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_stab_arrow, null);
    }

    public static Drawable isUpExchangeImage2(Context context, Double last, Double current) {
        return ResourcesCompat.getDrawable(context.getResources(), current > last ? R.drawable.price_up : R.drawable.price_down, null);
    }

    public static void setOpenedPush(Context context, int pushID) {
        RestInterface mRestInterfacePush = ApiClient.getClientAPNS().create(RestInterface.class);
        mRestInterfacePush.setOpenedPush(
                ApplicationsAppRef.getAppRef(context.getApplicationInfo().packageName),
                context.getApplicationInfo().packageName,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID),
                ApiListEnums.DEVICE_PUSH_TOKEN.getApi(context),
                pushID,
                false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("PUSH OPENED", s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
