package turkuvaz.general.turkuvazgeneralactivitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

import turkuvaz.sdk.models.Models.Preferences;

public class InstallReferrerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "onReceive: DEEP LINK CONTROL1");
        /** DEEP LINK CONTROL */
        if (intent.getExtras() != null && intent.getExtras().containsKey("referrer")) {
            try {
              /*  String[] dizi = getIntent().getExtras().getString("deepLink").replace(BuildConfig.DEEP_LINK + "://open?", "").split("&");
                Log.i("TAG", "isUpdateReady: BuildConfig.DEEP_LINK-3");
                String id = dizi[dizi.length - 2].replace("id=", "");
                String type = dizi[dizi.length - 1].replace("type=", "");
                Preferences.save(context, "deepLinkDefault", intent.getExtras().getString("referrer"));
                if (intent.getExtras().getString("referrer").contains("---")) { // iki parametreyi --- işaretiyle ayıracağız. & işaretiyle ayrılmadı. ilk & işaretine kadar olanı referrer olarak alıyor.
                    String id = intent.getExtras().getString("referrer").split("---")[0].replace("id=", ""); // id alınır
                    String type = intent.getExtras().getString("referrer").split("---")[1].replace("type=", ""); // tip alınır
                    if (id != null && !id.equals("") && type != null && !type.equals("")) {
                        Preferences.save(context, "deepLinkId", id);
                        Preferences.save(context, "deepLinkType", type);
                    }
                }*/

                String data = intent.getExtras().getString("referrer");
                Preferences.save(context, "deepLinkDefault", intent.getExtras().getString("referrer"));
                if (data != null && data.length() > 0) {
                    String[] items = Objects.requireNonNull(data).split("~~type=");
                    if (items.length > 1) {
                        String id = items[0].replace("id=", ""); // id alınır
                        String type = items[1]; // type alınır
                        if (!id.equals("") && type != null && !type.equals("")) {
                            Preferences.save(context, "deepLinkId", id);
                            Preferences.save(context, "deepLinkType", type);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
