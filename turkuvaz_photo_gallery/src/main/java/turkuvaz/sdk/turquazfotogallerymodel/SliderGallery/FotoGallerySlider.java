package turkuvaz.sdk.turquazfotogallerymodel.SliderGallery;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.FotoGallerySlider.FotoGallerySliderData;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;
import turkuvaz.sdk.turquazfotogallerymodel.SliderGallery.PhotoGalleryView.FotoGalleryPagerAdapter;


import turkuvaz.sdk.turquazfotogallerymodel.SliderGallery.Utils.LoopViewPager;

import com.turquaz.turquazfotogallerymodel.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by turgay.ulutas on 14/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class FotoGallerySlider extends RelativeLayout implements FotoGalleryPagerAdapter.onPagerSizeComplate {

    private RestInterface mRestInterface;
    private FotoGalleryPagerAdapter pagerAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private LoopViewPager pager;
    private FotoGalleryPagerAdapter.onSelectedNewsListener mOnSelectedNewsListener;
    private Button mBtn_tryAgain;
    private Integer Width;
    private String mApiPath;
    private Activity mActivity;
    private boolean autoSlide;

    public FotoGallerySlider(Activity context) {
        super(context);
        mActivity = context;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
    }

    public FotoGallerySlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FotoGallerySlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FotoGallerySlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.photo_gallery_slider_main, this, true);
        mBtn_tryAgain = findViewById(R.id.btn_mansetTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.pager = findViewById(R.id.pager_fotoGallery);
        this.pagerAdapter = new FotoGalleryPagerAdapter(mActivity, this.mResponse);
        this.pagerAdapter.setSelectedListener(this.mOnSelectedNewsListener);
        this.pagerAdapter.setOnPagerSizeComplate(this);

        getMansetNews(mApiPath);

        //TODO Hata oluşması durumunda yeniden dene butonu
        mBtn_tryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getMansetNews(mApiPath);
            }
        });
    }

    public boolean isAutoSlide() {
        return autoSlide;
    }

    public void setAutoSlide(boolean autoSlide) {
        this.autoSlide = autoSlide;
    }

    public void setApiPath(String apiPath) {
        mApiPath = apiPath;
    }

    public void setOnSelectedNewsListener(FotoGalleryPagerAdapter.onSelectedNewsListener onSelectedNewsListener) {
        mOnSelectedNewsListener = onSelectedNewsListener;
    }

    private void getMansetNews(String apiUrl) {
        mRestInterface.getPhotoGallerySlider(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FotoGallerySliderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FotoGallerySliderData sliderData) {
                        if (sliderData != null && sliderData.getData() != null && sliderData.getData().getGalleries() != null && sliderData.getData().getGalleries().getResponse() != null) {
                            mResponse.addAll(sliderData.getData().getGalleries().getResponse());
                            mBtn_tryAgain.setVisibility(GONE);

                        } else {
                            mBtn_tryAgain.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBtn_tryAgain.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        if (pager != null) {
                            pager.setAdapter(pagerAdapter);

                            CirclePageIndicator indicator = findViewById(R.id.titles);
                            indicator.setSnap(true);
                            indicator.setViewPager(pager);
                        }
                    }
                });
    }


    @Override
    public void pagerSize(final int pagerSize) {
        try {
            if (mActivity != null)
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pager != null)
                            pager.getLayoutParams().height = pagerSize;
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}