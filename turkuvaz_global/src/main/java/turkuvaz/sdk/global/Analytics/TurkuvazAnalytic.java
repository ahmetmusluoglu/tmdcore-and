package turkuvaz.sdk.global.Analytics;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.gemius.sdk.audience.AudienceConfig;
import com.gemius.sdk.audience.AudienceEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import turkuvaz.sdk.global.R;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.GemiusIdentifier;
import turkuvaz.sdk.models.Models.Preferences;

public class TurkuvazAnalytic {
    private String mEventName = "None";
    private boolean mIsPageView = false, isLogOn = false;
    private Context mContext;

    private AudienceEvent mGemiusEvent;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Bundle mFirebaseBundle;
    private boolean isAddParameter = false;

    public TurkuvazAnalytic(Context context) {
        this.mContext = context;
        mFirebaseBundle = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);

        String versionApp;
        try {
            versionApp = mContext.getPackageManager().getPackageInfo(mContext.getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionApp = "Non";
        }

//        com.gemius.sdk.Config.setAppInfo(mContext.getApplicationInfo().packageName, versionApp);
        com.gemius.sdk.Config.setAppInfo(context.getResources().getString(R.string.app_name), versionApp);
        com.gemius.sdk.Config.disableCookies(mContext); // cookie'ler reklam sayımı için olduğundan false olacak
        AudienceConfig.getSingleton().setHitCollectorHost("https://gatr.hit.gemius.pl/");
        AudienceConfig.getSingleton().setScriptIdentifier(Preferences.getString(mContext, ApiListEnums.SETTINGS_GEMIUS_KEY.getType(), GemiusIdentifier.getGemiusIdentifier(mContext.getApplicationInfo().packageName)));
        mGemiusEvent = new AudienceEvent(mContext);
        mGemiusEvent.setScriptIdentifier(Preferences.getString(mContext, ApiListEnums.SETTINGS_GEMIUS_KEY.getType(), GemiusIdentifier.getGemiusIdentifier(mContext.getApplicationInfo().packageName)));
        mGemiusEvent.setHitCollectorHost("https://gatr.hit.gemius.pl/"); // kaldırılabilir ? sdk da göndermiş, dökümanda göndermemiş
    }

    public TurkuvazAnalytic addParameter(String key, String value) {
        isAddParameter = true;
        value = getSafeValue(value);

        mGemiusEvent.addExtraParameter(key, value);
        mFirebaseBundle.putString(key, value);

        sendLog("PARAMETERS:  [Key] ->  " + key + " [Value] -> " + value, false);
        sendLog("FIREBASE:   " + " [Event Name] -> " + mEventName + "  [Page View] -> " + mIsPageView, false);
        return this;
    }

    public void sendEvent() {
        if (mContext == null)
            return;
        if (mEventName == null || TextUtils.isEmpty(mEventName)) {
            Log.e("T. Analytic: ", "EventName cannot be empty!");
            return;
        }

        try {
            mGemiusEvent.addExtraParameter(AnalyticEnums.EVENT_NAME.getEnum(), mEventName);
            mGemiusEvent.setEventType(mIsPageView ? AudienceEvent.EventType.FULL_PAGEVIEW : AudienceEvent.EventType.ACTION);
            if (Preferences.getBoolean(mContext, ApiListEnums.SETTINGS_GEMIUS_IS_ACTIVE.getType(), false)) {
//                Log.e("Gemiuss", mGemiusEvent.getEventType().toString() + "-" + mGemiusEvent.getScriptIdentifier() + "-" + mGemiusEvent.getExtraParameters());
                mGemiusEvent.sendEvent();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Preferences.getBoolean(mContext, ApiListEnums.SETTINGS_FIREBASE_IS_ACTIVE.getType(), false)) {
            mFirebaseAnalytics.logEvent(mEventName, mFirebaseBundle);
            if (mIsPageView) {
                mFirebaseAnalytics.logEvent(AnalyticEnums.VIEW_PAGE.getEnum(), new Bundle());
            }
            if (!isAddParameter)
                sendLog("FIREBASE:   " + " [Event Name] -> " + mEventName + "  [Page View] -> " + mIsPageView, false);
        }

        sendLog("COMPLETED", true);
    }

    private void sendLog(String log, boolean isStartEndMode) {
        if (!isLogOn)
            return;
        if (TextUtils.isEmpty(log))
            return;
        if (isStartEndMode)
            Log.e("T. Analytic: ", "------------- " + log + " -------------");
        else
            Log.e("T. Analytic: ", String.valueOf(log));
    }

    public TurkuvazAnalytic setEventName(String eventName) {
        this.mEventName = eventName;
        return this;
    }

    public TurkuvazAnalytic setIsPageView(boolean isPageView) {
        mIsPageView = isPageView;
        return this;
    }

    public TurkuvazAnalytic setLoggable(boolean isLogOn) {
        this.isLogOn = isLogOn;
//        com.gemius.sdk.Config.setLoggingEnabled(isLogOn);
        com.gemius.sdk.Config.setLoggingEnabled(false); // release olunca false gönderilmeli. dökümanda o şekilde yazılmış
        return this;
    }

    private String getSafeValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            if (value.length() >= 99) {
                return value.substring(0, 99);
            }
        } else {
            value = "None";
        }
        return value;
    }
}