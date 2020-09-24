package turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider.Adapter.ColumnistPagerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider.Adapter.ColumnistRecyclerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.GeneralUtils.LoopViewPager;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.ColumnistSlider.ColumnistData;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 14/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class ColumnistSlider extends RelativeLayout {

    private RestInterface mRestInterface;
    private ColumnistPagerAdapter pagerAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private LoopViewPager pager;
    private ColumnistRecyclerAdapter.onSelectedNewsListener mOnSelectedNewsListener;
    private Button mBtn_tryAgain;
    private String mApiPath;
    private boolean dateShow = true;
    private Context context;
    private int rowCount = 2; //Default row count

    public static ColumnistSlider create() {
        ColumnistSlider mansetSlider = null;
        return mansetSlider;
    }

    public ColumnistSlider(Context context) {
        super(context);
        this.context = context;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
    }

    public ColumnistSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ColumnistSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColumnistSlider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.columnist_slider_main, this, true);
        mBtn_tryAgain = findViewById(R.id.btn_mansetTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.pager = findViewById(R.id.pager);
        this.pagerAdapter = new ColumnistPagerAdapter(this.mResponse, dateShow, rowCount);
        this.pagerAdapter.setSelectedListener(this.mOnSelectedNewsListener);

        getAuthorsList(mApiPath);

        //TODO Hata oluşması durumunda yeniden dene butonu
        mBtn_tryAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getAuthorsList(mApiPath);
            }
        });
    }

    public boolean isDateShow() {
        return dateShow;
    }

    public void setDateShow(boolean dateShow) {
        this.dateShow = dateShow;
    }

    public void setApiPath(String apiPath) {
        mApiPath = apiPath;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setOnSelectedNewsListener(ColumnistRecyclerAdapter.onSelectedNewsListener onSelectedNewsListener) {
        mOnSelectedNewsListener = onSelectedNewsListener;
    }

    private void getAuthorsList(String apiUrl) {
        mRestInterface.getColumnistSlider(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ColumnistData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ColumnistData sliderData) {
                        if (sliderData != null && sliderData.getData() != null && sliderData.getData().getColumnists() != null && sliderData.getData().getColumnists().getResponse() != null) {
                            mResponse.addAll(sliderData.getData().getColumnists().getResponse());
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}