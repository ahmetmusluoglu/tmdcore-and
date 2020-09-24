package turkuvaz.general.turkuvazgeneralwidgets.RateApp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.Date;

public class RateApp {

    private static final String TAG = RateApp.class.getSimpleName();

    private static final String PREF_NAME = "RateApp";
    private static final String KEY_INSTALL_DATE = "rta_install_date";
    private static final String KEY_LAUNCH_TIMES = "rta_launch_times";
    private static final String KEY_OPT_OUT = "rta_opt_out";
    private static final String KEY_ASK_LATER_DATE = "rta_ask_later_date";

    private static Date mInstallDate = new Date();
    private static int mLaunchTimes = 0;
    private static boolean mOptOut = false;
    private static Date mAskLaterDate = new Date();

    private static Config sConfig = new Config();
    private static Callback sCallback = null;


    public static final boolean DEBUG = false;

    public static void init(Config config) {
        sConfig = config;
    }

    public static void setCallback(Callback callback) {
        sCallback = callback;
    }

    public static void onCreate(Activity context) {

    }

    @Deprecated
    public static void onStart(Activity context) {
        onCreate(context);
    }

    public static boolean showRateDialogIfNeeded(final Activity context) {
        if (shouldShowRateDialog()) {
            showRateDialog(context);
            return true;
        } else {
            return false;
        }
    }

    public static boolean showRateDialogIfNeeded(final Activity context, int themeId) {
        if (shouldShowRateDialog()) {
            showRateDialog(context);
            return true;
        } else {
            return false;
        }
    }

    public static boolean shouldShowRateDialog() {
        if (mOptOut) {
            return false;
        } else {


            return false;
        }
    }

    public static void stopRateDialog(final Activity context) {
        setOptOut(context, true);
    }

    public static int getLaunchCount(final Activity context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(KEY_LAUNCH_TIMES, 0);
    }

    private static void showRateDialog(final Activity context) {


    }

    private static void clearSharedPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.remove(KEY_INSTALL_DATE);
        editor.remove(KEY_LAUNCH_TIMES);
        editor.apply();
    }

    private static void setOptOut(final Context context, boolean optOut) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putBoolean(KEY_OPT_OUT, optOut);
        editor.apply();
        mOptOut = optOut;
    }

    private static void storeInstallDate(final Context context, Editor editor) {
        Date installDate = new Date();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            PackageManager packMan = context.getPackageManager();
            try {
                PackageInfo pkgInfo = packMan.getPackageInfo(context.getPackageName(), 0);
                installDate = new Date(pkgInfo.firstInstallTime);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        editor.putLong(KEY_INSTALL_DATE, installDate.getTime());
        log("First install: " + installDate.toString());
    }

    private static void log(String message) {
        if (DEBUG) {
            Log.v(TAG, message);
        }
    }

    public static class Config {
    }

    public interface Callback {

    }
}
