package turkuvaz.general.turkuvazgeneralwidgets.HeadlineSliderWithTitle;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSliderWithTitle.Adapter.HeadlineSliderTitleAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.GeneralUtils.LoopViewPager;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 14/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class HeadlineSliderWithTitle extends RelativeLayout implements HeadlineSliderTitleAdapter.onPagerSizeComplate {

    private RestInterface mRestInterface;
    private HeadlineSliderTitleAdapter pagerAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private LoopViewPager pager;
    private HeadlineSliderTitleAdapter.onSelectedNewsListener mOnSelectedNewsListener;
    private Button mBtn_tryAgain;
    private Activity mActivity;
    private Integer Width;


    public HeadlineSliderWithTitle(Activity context) {
        super(context);
        mActivity = context;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
    }

    public HeadlineSliderWithTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadlineSliderWithTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HeadlineSliderWithTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(ArrayList<Article> sliderWithArticles) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.head_slider_with_title_main, this, true);
        mBtn_tryAgain = findViewById(R.id.btn_mansetTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.pager = findViewById(R.id.pager_fotoGallery);

        this.pagerAdapter = new HeadlineSliderTitleAdapter((Activity) getContext(), this.mResponse);
        pagerAdapter.setOnPagerSizeComplate(this);
        this.pagerAdapter.setSelectedListener(this.mOnSelectedNewsListener);

        mResponse.addAll(sliderWithArticles);
        pager.setAdapter(pagerAdapter);
        CirclePageIndicator indicator = findViewById(R.id.titles);
        indicator.setSnap(true);
        indicator.setViewPager(pager);
    }

    public void setOnSelectedNewsListener(HeadlineSliderTitleAdapter.onSelectedNewsListener onSelectedNewsListener) {
        mOnSelectedNewsListener = onSelectedNewsListener;
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