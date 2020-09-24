package turkuvaz.general.apps.base;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;
import com.comscore.UsagePropertiesAutoUpdateMode;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import turkuvaz.general.apps.podcast.event.PlaybackEvent;
import turkuvaz.general.apps.podcast.util.RadioDatabase;
import turkuvaz.general.apps.utils.InitSettings;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.PageOverlayAds;
import turkuvaz.general.turkuvazgeneralwidgets.GdprDialog.GdprPreferences;
import turkuvaz.sdk.global.Constants;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 1/24/2020.
 */
public class CoreApp extends Application implements LifecycleObserver {
    private PageOverlayAds pageOverlayAds;
    private static CoreApp coreApp;
    private static InitSettings settings;
    private static SettingsCache settingsCache;

    public static RadioDatabase sDatabase;
    public static Bus bus;
    /****/

    @Override
    public void onCreate() {
        super.onCreate();
        //Test
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        Preferences.save(this, "appIsOpen", false);
        settingsCache = new SettingsCache(this);
        coreApp = this;
        bus = new Bus(ThreadEnforcer.MAIN);

        /** PAGE OVERLAY */
        Preferences.save(this, "startTime", System.currentTimeMillis());
        pageOverlayAds = new PageOverlayAds(this, GdprPreferences.getAppHedefleme(this));
        pageOverlayAds.setMinute(30);
    }

    public void generalControls() {
        GlobalMethods.setActivePlayer(Preferences.getBoolean(this, ApiListEnums.SETTINGS_NATIVE_PLAYER_IS_ACTIVE.getType(), false));
        boolean isPreRollActive = Preferences.getBoolean(this, ApiListEnums.SETTINGS_PRE_ROLL_ADS_IS_ACTIVE.getType(), false);
        if (!isPreRollActive) {
            Preferences.save(getInstance(), ApiListEnums.ADS_PREROLL.getType(), "");
        }
    }

    public PageOverlayAds getPageOverlayAds() {
        return pageOverlayAds;
    }

    public static CoreApp getInstance() {
        return coreApp;
    }

    public static InitSettings getSettings() {
        if (settings == null) {
            settings = getSettingsCache();
        }
        return settings;
    }

    public static void setSettings(InitSettings settings) {
        CoreApp.settings = settings;
        settingsCache.cachedSettings(settings);
    }

    public static InitSettings getSettingsCache() {
        settings = settingsCache.getCachedSettings();
        if (settings == null) {
            settings = new InitSettings(getInstance());

        }
        return settings;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void setupRadioSettings() {

    }

    public static void changeRadio(boolean isNext) {

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {

    }
}
