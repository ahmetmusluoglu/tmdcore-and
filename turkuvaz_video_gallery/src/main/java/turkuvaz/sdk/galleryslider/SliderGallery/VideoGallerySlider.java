package turkuvaz.sdk.galleryslider.SliderGallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;




import com.turquaz.videogallery.R;

import turkuvaz.sdk.galleryslider.SliderGallery.MansetView.VideoGalleryPagerAdapter;
import turkuvaz.sdk.galleryslider.SliderGallery.Utils.LoopViewPager;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GallerySlider.GallerySliderData;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

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
public class VideoGallerySlider extends RelativeLayout implements VideoGalleryPagerAdapter.onPagerSizeComplate {

    private RestInterface mRestInterface;
    private VideoGalleryPagerAdapter pagerAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private LoopViewPager pager;
    private VideoGalleryPagerAdapter.onSelectedNewsListener mOnSelectedNewsListener;
    private Button mBtn_tryAgain;
    private Integer Width;
    private int mWeight, mHeight;
    private String mApiPath;
    private Activity mActivity;
    private boolean autoSlide;
    private boolean isTitle;
    private String customPageName = "";

    public VideoGallerySlider(Activity context) {
        super(context);
        mActivity = context;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
    }

    public VideoGallerySlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoGallerySlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VideoGallerySlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(customPageName.equals("") ? R.layout.gallery_slider : R.layout.gallery_slider_custom, this, true);
        mBtn_tryAgain = findViewById(R.id.btn_mansetTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.pager = findViewById(R.id.pager);
        this.pagerAdapter = new VideoGalleryPagerAdapter(mActivity, this.mResponse, isTitle);
        this.pagerAdapter.setCustomPageName(customPageName);
        this.pagerAdapter.setSelectedListener(this.mOnSelectedNewsListener);
        this.pagerAdapter.setOnPagerSizeComplate(this);

        try {
            if (getContext() instanceof Activity)
                this.pager.getLayoutParams().height = Preferences.getInt(mActivity, SettingsTypes.SCREEN_WIDTH.getType(),-1) * mHeight / mWeight;
        } catch (Exception e) {
            e.printStackTrace();
        }

        getMansetNews(mApiPath);

        //TODO Hata oluşması durumunda yeniden dene butonu
        mBtn_tryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getMansetNews(mApiPath);
            }
        });
    }

    public void setSliderSize(int height, int weight) {
        mWeight = weight;
        mHeight = height;
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

    public void setOnSelectedNewsListener(VideoGalleryPagerAdapter.onSelectedNewsListener onSelectedNewsListener) {
        mOnSelectedNewsListener = onSelectedNewsListener;
    }

    private void getMansetNews(String apiUrl) {
        mRestInterface.getVideoGallerySlider(apiUrl).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<GallerySliderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GallerySliderData sliderData) {
                        if (sliderData != null && sliderData.getData() != null && sliderData.getData().getVideos() != null && sliderData.getData().getVideos().getResponse() != null) {
                            mResponse.addAll(sliderData.getData().getVideos().getResponse());
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

                            if (!customPageName.equals("")) {
                                pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                        LinePageIndicator indicator = findViewById(R.id.titles);
                                        indicator.setSelectedColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
                                        if (mResponse != null && mResponse.size() > 0) {
//                                        indicator.setGapWidth(pager.getWidth() / (10 * mResponse.size())); // indicator arasındaki boşluklar hesaplanıyor önce
//                                        indicator.setLineWidth(((pager.getWidth() - (indicator.getGapWidth() * mResponse.size() + 1)) / mResponse.size())); // boşluğa göre de indicator genişliği hesaplanır
                                        }
                                        indicator.setViewPager(pager);
                                    }
                                });
                            } else {
                                CirclePageIndicator indicator = findViewById(R.id.titles);
                                indicator.setSnap(true);
                                indicator.setViewPager(pager);
                            }
                        }
                    }
                });
    }

    public void setTitleVisible(boolean title) {
        isTitle = title;
    }

    public void setCustomPageName(String customPage) {
        this.customPageName = customPage;
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