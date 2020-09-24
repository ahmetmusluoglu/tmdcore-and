package turkuvaz.general.turkuvazgeneralwidgets.NewsListAndSlider;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.NewsList.NewsListData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 22/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class NewsListWithSlider extends RelativeLayout implements NewsListWithSliderAdapter.OnLoadMoreListener, NewsListWithSliderAdapter.onLoadTopBanner {
    private RestInterface mRestInterface;
    private StaggeredGridLayoutManager mLayoutManager;
    private NewsListWithSliderAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private int mPageIndex = 1;
    private String mApiUrl, mCategoryID;
    private NewsListWithSliderAdapter.onSelectedNewsListener onSelectedNewsListener;
    private NewsListWithSliderAdapter.onHeadlineSelectedNewsListener onHeadlineSelectedNewsListener;
    private onStatusListener onStatusListener;
    private LinearLayout mLy_rootContent;
    private RecyclerView mRec_listNews;
    private boolean isOnLoadMoreMode = false;
    private ArrayList<Article> mHeadlines = new ArrayList<>();
    private boolean isTouch;


    public NewsListWithSlider(@NonNull Context context) {
        super(context);
    }

    public NewsListWithSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListWithSlider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setApiPath(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void setCategoryID(String categoryID) {
        this.mCategoryID = categoryID;
    }

    public void setOnSelectedNewsListener(NewsListWithSliderAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void setOnHeadlineSelectedNewsListener(NewsListWithSliderAdapter.onHeadlineSelectedNewsListener onHeadlineSelectedNewsListener) {
        this.onHeadlineSelectedNewsListener = onHeadlineSelectedNewsListener;
    }

    public void setOnStatusListener(onStatusListener onStatusListener) {
        this.onStatusListener = onStatusListener;
    }

    public void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.news_list_with_slider, this, true);
        mLy_rootContent = findViewById(R.id.ly_newsListWithSliderRoot);

        mRec_listNews = new RecyclerView(getContext());

        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRec_listNews.setLayoutManager(mLayoutManager);
        mRec_listNews.setNestedScrollingEnabled(false);
        mRec_listNews.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        getNewsList(mPageIndex);
    }

    public ArrayList<Article> getHeadlines() {
        return mHeadlines;
    }

    private void getNewsList(final int pageIndex) {
        mRestInterface.getNewsList(mApiUrl, mCategoryID, pageIndex == 1 ? null : pageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsListData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsListData newsListData) {
                        try {
                            if (pageIndex == 1) {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getArticles() != null && newsListData.getData().getArticles().getResponse() != null) {
                                    mResponse.addAll(newsListData.getData().getArticles().getResponse());
                                    mHeadlines.addAll(newsListData.getData().getHeadlines().getResponse());
                                    if (getContext() instanceof Activity) {
                                        mAdapter = new NewsListWithSliderAdapter(mResponse, mHeadlines, getContext());
                                        mAdapter.setOnLoadMoreListener(NewsListWithSlider.this);
                                        mAdapter.setOnLoadTopBanner(NewsListWithSlider.this);
                                        mAdapter.setSelectedListener(onSelectedNewsListener);
                                        mAdapter.setOnHeadlineSelectedNewsListener(onHeadlineSelectedNewsListener);
                                        mRec_listNews.setAdapter(mAdapter);
                                        mLy_rootContent.addView(mRec_listNews);
                                    }
                                }

                            } else {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getArticles() != null && newsListData.getData().getArticles().getResponse() != null) {
                                    ArrayList<Article> mPageList = newsListData.getData().getArticles().getResponse();
                                    mAdapter.notifyDataSet(mPageList, mPageList.size());
                                }
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e.getMessage() != null && onStatusListener != null)
                            onStatusListener.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (onStatusListener != null)
                            onStatusListener.onSuccess();
                    }
                });
    }

    @Override
    public void onLoadMore() {
        if (isOnLoadMoreMode) {
            onStatusListener.onStartLoad();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPageIndex++;
                    getNewsList(mPageIndex);
                }
            }, 500);
        }
    }

    public void onLoadMoreManually() {
        onStatusListener.onStartLoad();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageIndex++;
                getNewsList(mPageIndex);
            }
        }, 500);
    }

    public void setOnLoadMoreMode(boolean mode) {
        isOnLoadMoreMode = mode;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (!isTouch)
            isTouch = true;
        return super.onTouchEvent(e);
    }

    @Override
    public void onLoadedBanner() {
        if (!isTouch && mRec_listNews != null)
            mRec_listNews.smoothScrollToPosition(0);
    }

    public interface onStatusListener {
        void onStartLoad();

        void onSuccess();

        void onError(String errorMessage);
    }
}