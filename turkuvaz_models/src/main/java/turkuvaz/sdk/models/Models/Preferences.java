package turkuvaz.sdk.models.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

import turkuvaz.sdk.models.Models.Enums.ApiTypes;


/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class Preferences {
    static String SHARED_PREFS_FILE_NAME = "userSettings";
    private static SharedPreferences getPrefs(Context context) {
        try {
            return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeCustomKeys(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : preferences.getAll().keySet()) {
            if (key.toLowerCase().startsWith("custom_"))
                editor.remove(key);
        }
        editor.apply();
    }

    //Save Booleans
    public static void save(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).apply();
    }

    //Get Booleans
    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }


    //Strings
    public static void save(Context context, String key, String value) { // uygulama ilk kurulduğunda varsayılan olarak canlı ortam linki gelecek
        try {
            String mCroppedValue = value.replace(Preferences.getString(context, "connectedurl", "https://api.tmgrup.com.tr"), "");
            getPrefs(context).edit().putString(key, mCroppedValue).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Strings
    public static void saveApiLink(Context context, String key, String value) { // FKR-765 işi kapsamında artık enum'dan değil sharedpref'ten bu değerleri alacak. varsayılan olarak canlı db gelecek. tek değer olduğu için tek replace yapılacak
        getPrefs(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        if (Preferences.getBoolean(context, "isOpenCustomPage", false) && !Preferences.getString(context, "custom_" + key).equals(""))
            return getPrefs(context).getString("custom_" + key, defaultValue);
//        if (!Preferences.getString(context, "custom_" + key).equals("")) // custom sayfadaysak, custom olan renk gösterilecek. burası dolu ise zaten custom sayfadayız demektir.
//            return getPrefs(context).getString("custom_" + key, defaultValue);
        else
            return getPrefs(context).getString(key, defaultValue);
    }

    //Integers
    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }


    //Floats
    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).apply();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }


    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).apply();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }


    //StringSets
    public static void save(Context context, String key, Set<String> value) {
        getPrefs(context).edit().putStringSet(key, value).apply();
    }

    public static Set<String> getStringSet(Context context, String key) {
        return getPrefs(context).getStringSet(key, null);
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defaultValue) {
        return getPrefs(context).getStringSet(key, defaultValue);
    }


    //ClearUser
    public static boolean clearData(Context context) {
        return getPrefs(context).edit().clear().commit();
    }
}