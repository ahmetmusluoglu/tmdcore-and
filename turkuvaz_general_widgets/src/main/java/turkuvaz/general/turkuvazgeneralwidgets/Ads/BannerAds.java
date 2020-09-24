package turkuvaz.general.turkuvazgeneralwidgets.Ads;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import turkuvaz.general.turkuvazgeneralwidgets.GdprDialog.GdprPreferences;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;

public class BannerAds extends FrameLayout {
    private bannerStatusListener mBannerStatusListener;
    private View view;

    public BannerAds(final Context context, AdSize adSize, final String adUnitId) {
        super(context);

        if (adUnitId.equals(ApiListEnums.ADS_GENERAL_320x50.getApi(context))) // anasayfadaki sticky reklamda boşluk olmayacak
            setPadding(0, 0, 0, 0);
        else
            setPadding(0, 15, 0, 15);
        PublisherAdRequest request;

        LayoutParams mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;

        final PublisherAdView mPublisherAdView = new PublisherAdView(context);
        mPublisherAdView.setVisibility(GONE);
        mPublisherAdView.setAdSizes(adSize);
        mPublisherAdView.setAdUnitId(adUnitId);
        mPublisherAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                if (mBannerStatusListener != null)
                    mBannerStatusListener.onLoad();

                if (adUnitId.equals(ApiListEnums.ADS_GENERAL_320x50.getApi(context))) {
                    setPadding(0, 0, 0, 0);
                    if (view != null)
                        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), (int) (50 * getResources().getDisplayMetrics().density));
                } else
                    setPadding(0, 15, 0, 15);
                mPublisherAdView.setVisibility(VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                if (mBannerStatusListener != null)
                    mBannerStatusListener.onError();
                setPadding(0, 0, 0, 0); // hata varsa padding'leri de kaldır fazla boşluk kalıyordu
                Log.e("TurquazBannerAds", "An error occurred when loading ads");
            }
        });

        if (GdprPreferences.getAppHedefleme(context)) { // isNpaActive == true --> Hedeflemeli Ads Gösterimi    isNpaActive == false --> Hedeflemesiz Ads Gösterimi
            request = new PublisherAdRequest.Builder().build();
        } else {
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            request = new PublisherAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
        }
        try {
            mPublisherAdView.loadAd(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addView(mPublisherAdView, mLayoutParams);
    }

    public void setPaddingView(View view){
        this.view = view;
    }
    
    public bannerStatusListener getmBannerStatusListener() {
        return mBannerStatusListener;
    }

    public void setBannerStatusListener(bannerStatusListener mBannerStatusListener) {
        this.mBannerStatusListener = mBannerStatusListener;
    }

    public interface bannerStatusListener {
        void onLoad();

        void onError();
    }
}