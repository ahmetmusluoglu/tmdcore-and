package turkuvaz.sdk.galleryslider.ListGallery;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.turquaz.videogallery.R;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.VideoGalleryList.VideoGalleryListData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 22/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class VideoListGallery extends RecyclerView implements VideoGalleryListAdapter.OnLoadMoreListener, VideoGalleryListAdapter.onLoadTopBanner {
    private RestInterface mRestInterface;
    private StaggeredGridLayoutManager mLayoutManager;
    private VideoGalleryListAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private int mPageIndex = 1;
    private String mApiUrl, mCategoryID, mApiUrlHeadline="";
    private VideoGalleryListAdapter.onSelectedNewsListener onSelectedNewsListener;
    private onStatusListener onStatusListener;
    private boolean isTouch;
    private int spanCount = 2;
    private String customPageName = "";

    public VideoListGallery(@NonNull Context context) {
        super(context);
    }

    public VideoListGallery(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoListGallery(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setApiPath(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void setApiPathHeadline(String mApiUrlHeadline) {
        this.mApiUrlHeadline = mApiUrlHeadline;
    }

    public void setmCategoryID(String categoryID) {
        this.mCategoryID = categoryID;
    }

    public void setOnSelectedNewsListener(VideoGalleryListAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public void init() {
        this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));
        this.setPadding(5, 0, 5, 0);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        this.setLayoutManager(mLayoutManager);
        setNestedScrollingEnabled(true);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        getVideoList(mPageIndex);
    }

    private void getVideoList(final int pageIndex) {
        mRestInterface.getVideoGalleryList(mApiUrl, mCategoryID, pageIndex == 1 ? null : pageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoGalleryListData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoGalleryListData newsListData) {
                        try {
                            if (pageIndex == 1) {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getVideos() != null && newsListData.getData().getVideos().getResponse() != null) {
                                    mResponse.addAll(newsListData.getData().getVideos().getResponse());
                                    if (getContext() instanceof Activity) {
                                        mAdapter = new VideoGalleryListAdapter(mResponse, getContext(), mApiUrlHeadline, customPageName);
                                        mAdapter.setOnLoadMoreListener(VideoListGallery.this);
                                        mAdapter.setOnLoadTopBanner(VideoListGallery.this);
                                        mAdapter.setSelectedListener(onSelectedNewsListener);
                                        mAdapter.setmCategoryIDHeadline(mCategoryID);
                                        setAdapter(mAdapter);
                                    }

                                }
                            } else {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getVideos() != null && newsListData.getData().getVideos().getResponse() != null) {
                                    ArrayList<Article> mPageList = newsListData.getData().getVideos().getResponse();
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
        onStatusListener.onStartLoad();
        new Handler().postDelayed(() -> {
            mPageIndex++;
            getVideoList(mPageIndex);
        }, 500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (!isTouch)
            isTouch = true;
        return super.onTouchEvent(e);
    }

    @Override
    public void onLoadedBanner() {
        if (!isTouch)
            smoothScrollToPosition(0);
    }

    public interface onStatusListener {
        void onSuccess();

        void onError(String errorMessage);

        void onStartLoad();
    }

    public void setOnStatusListener(onStatusListener onStatusListener) {
        this.onStatusListener = onStatusListener;
    }

    public void setCustomPageName(String customPageName) {
        this.customPageName = customPageName;
    }
}
