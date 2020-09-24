package turkuvaz.general.turkuvazgeneralactivitys.Intro;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 3/13/2020.
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_ACTIVE = "IS_ACTIVE";

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor = pref.edit();
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public  void setIsActive(boolean isActive) {
        editor = pref.edit();
        editor.putBoolean(IS_ACTIVE, isActive);
        editor.apply();
    }

    public boolean isActive() {
        return pref.getBoolean(IS_ACTIVE, false);
    }
}
