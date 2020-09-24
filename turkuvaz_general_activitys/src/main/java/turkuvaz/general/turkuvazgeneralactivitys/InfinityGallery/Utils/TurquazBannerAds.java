package turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class TurquazBannerAds extends FrameLayout {
    private PublisherAdView mPublisherAdView;
    private LayoutParams mLayoutParams;

    public TurquazBannerAds(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER;
    }

    public void loadAds(Context context, AdSize adSize, String adUnitId) {
        try {
            mPublisherAdView = new PublisherAdView(context);
            mPublisherAdView.setVisibility(GONE);
            mPublisherAdView.setAdSizes(adSize);
            mPublisherAdView.setAdUnitId(adUnitId);
            addView(mPublisherAdView, mLayoutParams);
            mPublisherAdView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    mPublisherAdView.setVisibility(VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    Log.e("TurquazBannerAds", "An error occurred when loading ads");
                }
            });
            mPublisherAdView.loadAd(new PublisherAdRequest.Builder().build());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
