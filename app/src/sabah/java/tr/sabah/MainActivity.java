package tr.sabah;

import android.annotation.SuppressLint;
import android.util.Log;

import turkuvaz.general.apps.R;
import turkuvaz.general.apps.base.CoreApp;
import turkuvaz.general.apps.base.CoreSplashActivity;
import turkuvaz.general.apps.utils.Constants;
import turkuvaz.general.apps.utils.InitSettings;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.Preferences;

public class MainActivity extends CoreSplashActivity {

    @SuppressLint("ResourceType")
    @Override
    public void setInitialValues() {
        InitSettings settings = new InitSettings(this);
        /**ZORUNLU ALANLAR...*/
        settings.setConfigId(Constants.SABAH_ID);//Default = 0

        /*String token = Preferences.getString(getApplicationContext(), ApiListEnums.DEVICE_PUSH_TOKEN.getType());
        Log.i("TAG", "my-token: " + token);*/
        /**DEFAULT ALANLAR*/
//        settings.setMenuHeaderColor(getResources().getString(R.color.statusBarColor));// Default degeri alir
//        settings.setSplashImageResId(R.drawable.main_back);
        settings.setAppTheme("light");
        settings.setSplashOnlyImage(true);
        settings.setShowBottomNavigationView(true);
        settings.setShowBottomNavigationViewCustom(false); // sabah spor gibi custom sayfalar için

        CoreApp.setSettings(settings);

        Preferences.save(MainActivity.this, ApiListEnums.THEME.getType(), CoreApp.getSettings().getAppTheme());
        Preferences.save(MainActivity.this, ApiListEnums.SPLASH_BACK.getType(), getResources().getString(R.color.splash_back));
        Preferences.save(MainActivity.this, ApiListEnums.SPLASH_TEXT.getType(), getResources().getString(R.color.splash_text));

        if (Preferences.getBoolean(MainActivity.this, SettingsTypes.IS_FIRST_SET_FONT.getType(), true)) {
            Preferences.save(MainActivity.this, SettingsTypes.NEWS_WEBVIEW_FONT_SIZE.getType(), 20);
            Preferences.save(MainActivity.this, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16);
            Preferences.save(MainActivity.this, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25);
            Preferences.save(MainActivity.this, SettingsTypes.IS_FIRST_SET_FONT.getType(), false);
        }
    }
}