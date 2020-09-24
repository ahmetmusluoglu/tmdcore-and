package turkuvaz.general.turkuvazgeneralwidgets.Ads;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.ConfigBase.Settings;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Preferences;

public class PageOverlayAds {
    private PublisherInterstitialAd mPublisherInterstitialAd;
    private Context context;
    private int minute = 30;

    public PageOverlayAds(Context context, boolean isNpaActive) {
        try {
            this.context = context;
            mPublisherInterstitialAd = new PublisherInterstitialAd(context);
            mPublisherInterstitialAd.setAdUnitId(ApiListEnums.ADS_PAGE_OVERLAY.getApi(context));

            /**
             * isNpaActive == true --> Hedeflemeli Ads Gösterimi
             * isNpaActive == false --> Hedeflemesiz Ads Gösterimi
             */

            PublisherAdRequest request;
            if (isNpaActive) {
                request = new PublisherAdRequest.Builder().build();
            } else {
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                request = new PublisherAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
            }

            mPublisherInterstitialAd.loadAd(request);
            mPublisherInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    Log.e("PageOverlayAds", "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e("PageOverlayAds onAdFail", String.valueOf(errorCode));
                }

                @Override
                public void onAdOpened() {

                }

                @Override
                public void onAdLeftApplication() {

                }

                @Override
                public void onAdClosed() {
                    mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        try {
            /*long startTime = Preferences.getLong(context, "startTime", 0);
            if (startTime != 0 && mPublisherInterstitialAd.isLoaded() && (System.currentTimeMillis() - startTime) > TimeUnit.MINUTES.toMillis(minute)) {
                mPublisherInterstitialAd.show();
                Preferences.save(context, "startTime", System.currentTimeMillis());
            } else {
                Log.e("PageOverlayAds", "isLoaded:" + mPublisherInterstitialAd.isLoaded());
            }*/

            new Handler().postDelayed(() -> {
                String appSettingsJson = Preferences.getString(context, ApiListEnums.CATEGORY_SETTINGS.getType(), "");
                Settings appSettings = new Gson().fromJson(appSettingsJson, Settings.class);
                if (appSettings != null && appSettings.getAdvertisement() != null && appSettings.getAdvertisement().getInterstitialAd() != null && appSettings.getAdvertisement().getInterstitialAd().isActive())
                    GlobalMethods.showInterstitialAd(context, appSettings.getAdvertisement().getInterstitialAd().getMinutes());
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PageOverlayAds setMinute(int minute) {
        this.minute = minute;
        return this;
    }

}