package turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.GeneralUtils.LoopViewPager;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider.HeadLineAdapter.HeadlinePagerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.MansetSlider.MansetSliderData;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 14/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class HeadLineSlider extends RelativeLayout implements HeadlinePagerAdapter.onPagerSizeComplate {

    private RestInterface mRestInterface;
    private HeadlinePagerAdapter pagerAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private LoopViewPager pager;
    private HeadlinePagerAdapter.onSelectedNewsListener mOnSelectedNewsListener;
    private Button mBtn_tryAgain;
    private Timer mTimer;
    private String mApiPath;
    private int limitListCount = -1;
    private String categoryID = null;
    private Activity mActivity;
    private boolean isTitleVisible = true;
    private boolean isHomepage = false;
    private boolean isSurmanset = false;
    private boolean autoSlide;
    private String customPageName = "";

    public HeadLineSlider(Activity context) {
        super(context);
        mActivity = context;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
    }

    public HeadLineSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadLineSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeadLineSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setLimitSize(int limitSize) {
        limitListCount = limitSize;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        final LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(customPageName.equals("") ? R.layout.head_line_main : R.layout.head_line_main_custom, this, true);
        mBtn_tryAgain = findViewById(R.id.btn_mansetTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.pager = findViewById(R.id.pager);
        this.pagerAdapter = new HeadlinePagerAdapter((Activity) getContext(), this.mResponse);
        this.pagerAdapter.setCustomPageName(customPageName);
        this.pagerAdapter.setTitleVisible(isTitleVisible);
        this.pagerAdapter.setHomepage(isHomepage);
        this.pagerAdapter.setSurmanset(isSurmanset);
        this.pagerAdapter.setSelectedListener(this.mOnSelectedNewsListener);
        getMansetNews(mApiPath);

        pagerAdapter.setOnPagerSizeComplate(this);

        this.pager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    stopTimer();
                    if (pager != null)
                        pager.setOnTouchListener(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        //TODO Hata oluşması durumunda yeniden dene butonu
        mBtn_tryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getMansetNews(mApiPath);
            }
        });
    }

    public void initWithData(ArrayList<Article> mResponse) {
        final LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(customPageName.equals("") ? R.layout.head_line_main : R.layout.head_line_main_custom, this, true);
        mBtn_tryAgain = findViewById(R.id.btn_mansetTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.pager = findViewById(R.id.pager);
        this.pagerAdapter = new HeadlinePagerAdapter((Activity) getContext(), this.mResponse);
        this.pagerAdapter.setSelectedListener(this.mOnSelectedNewsListener);
        this.pagerAdapter.setCustomPageName(customPageName);
        this.pagerAdapter.setTitleVisible(isTitleVisible);
        this.pagerAdapter.setHomepage(isHomepage);
        this.pagerAdapter.setSurmanset(isSurmanset);
        this.mResponse.addAll(mResponse);

        if (pager != null) {
            pager.setAdapter(pagerAdapter);

            if (!customPageName.equals("")) {
                pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        LinePageIndicator indicator = findViewById(R.id.titles);
                        indicator.setSelectedColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
                        if (mResponse.size() > 0) {
                            indicator.setGapWidth(pager.getWidth() / (10 * mResponse.size())); // indicator arasındaki boşluklar hesaplanıyor önce
                            indicator.setLineWidth(((pager.getWidth() - (indicator.getGapWidth() * mResponse.size() + 1)) / mResponse.size())); // boşluğa göre de indicator genişliği hesaplanır
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

    public void setApiPath(String apiPath) {
        mApiPath = apiPath;
    }

    public boolean isAutoSlide() {
        return autoSlide;
    }

    public void setAutoSlide(boolean autoSlide) {
        this.autoSlide = autoSlide;
    }

    public void setCategoryID(String cID) {
        categoryID = cID;
    }

    public void setOnSelectedNewsListener(HeadlinePagerAdapter.onSelectedNewsListener onSelectedNewsListener) {
        mOnSelectedNewsListener = onSelectedNewsListener;
    }

    private void getMansetNews(String apiUrl) {
        mRestInterface.getHeadlineNews(apiUrl, categoryID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MansetSliderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MansetSliderData sliderData) {
                        if (sliderData != null && sliderData.getData() != null && sliderData.getData().getArticles() != null && sliderData.getData().getArticles().getResponse() != null) {
                            if (limitListCount == -1) {
                                mResponse.addAll(sliderData.getData().getArticles().getResponse());
                            } else {
                                mResponse.addAll(sliderData.getData().getArticles().getResponse().subList(0, limitListCount));
                            }

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
                                            indicator.setGapWidth(pager.getWidth() / (10 * mResponse.size())); // indicator arasındaki boşluklar hesaplanıyor önce
                                            indicator.setLineWidth(((pager.getWidth() - (indicator.getGapWidth() * mResponse.size() + 1)) / mResponse.size())); // boşluğa göre de indicator genişliği hesaplanır
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (autoSlide)
            startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (autoSlide)
            stopTimer();
    }

    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mTimer.schedule(new HeadlineSliderTimerTask(), 10000, 10000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    public void moveNext() {
        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    public void movePrevious() {
        pager.setCurrentItem(pager.getCurrentItem() - 1);
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

    private class HeadlineSliderTimerTask extends TimerTask {
        private boolean isNext = true;

        @Override
        public void run() {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pager == null || pagerAdapter == null)
                            return;

                        if (pager.getCurrentItem() < (pagerAdapter.getCount() - 1) && isNext) {
                            moveNext();
                        } else {
                            isNext = false;
                        }

                        if (pager.getCurrentItem() != 0 && !isNext) {
                            movePrevious();
                        } else {
                            isNext = true;
                        }
                    }
                });
            }
        }
    }

    public void setCustomPageName(String customPage) {
        this.customPageName = customPage;
    }

    public void setTitleVisible(boolean titleVisible) {
        isTitleVisible = titleVisible;
    }
    public void setHomepage(boolean isHomepage) {
        this.isHomepage = isHomepage;
    }
    public void setSurmanset(boolean isSurmanset) {
        this.isSurmanset= isSurmanset;
    }
}