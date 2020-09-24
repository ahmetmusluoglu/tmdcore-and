package turkuvaz.general.apps.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import turkuvaz.general.apps.utils.InitSettings;

/**
 * Created by Ahmet MUŞLUOĞLU on 1/31/2020.
 */
class SettingsCache {
    private static SharedPreferences preferences;
    private static final String SETTINGS_CACHE = "SETTINGS_CACHE";


    SettingsCache(Context context) {
        preferences = context.getSharedPreferences(this.getClass().getName(), Context.MODE_PRIVATE);
    }

    synchronized void cachedSettings(InitSettings settings) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SETTINGS_CACHE, new Gson().toJson(settings));
        editor.apply();
    }

    InitSettings getCachedSettings() {
        try {
            String cachedSettings = preferences.getString(SETTINGS_CACHE, "");
            return cachedSettings.isEmpty() ? null : new Gson().fromJson(cachedSettings, InitSettings.class);
        } catch (Exception e) {
            Log.i("TAG", "getCachedSettings: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
