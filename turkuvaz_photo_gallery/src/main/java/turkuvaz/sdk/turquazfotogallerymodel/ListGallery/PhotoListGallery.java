package turkuvaz.sdk.turquazfotogallerymodel.ListGallery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.turquaz.turquazfotogallerymodel.R;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.sdk.models.Models.Enums.NewsListType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.PhotoGalleryList.PhotoGalleryListData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 22/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class PhotoListGallery extends RecyclerView implements PhotoGalleryListAdapter.OnLoadMoreListener, PhotoGalleryListAdapter.onLoadTopBanner {
    private RestInterface mRestInterface;
    private StaggeredGridLayoutManager mLayoutManager;
    private PhotoGalleryListAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private int mPageIndex = 1;
    private String mApiUrl, mCategoryID;
    private PhotoGalleryListAdapter.onSelectedNewsListener onSelectedNewsListener;
    private onStatusListener onStatusListener;
    private boolean isTouch = false;
    private Bundle itemBundle;
    private int spanCount;
    private String customPageName = "";

    public PhotoListGallery(@NonNull Context context) {
        super(context);
    }

    public PhotoListGallery(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoListGallery(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setApiPath(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public void setmCategoryID(String categoryID) {
        this.mCategoryID = categoryID;
    }

    public void setOnSelectedNewsListener(PhotoGalleryListAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void init() {
        this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));
        this.setPadding(5, 0, 5, 0);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.mLayoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);// nav-bar tıklandığında, liste 2x2 mi yoksa 1x1'lik mi olacak
        this.setLayoutManager(mLayoutManager);
//        this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        setNestedScrollingEnabled(false);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        getNewsList(mPageIndex);
    }


    private void getNewsList(final int pageIndex) {
        mRestInterface.getPhotoGalleryList(mApiUrl, mCategoryID, pageIndex == 1 ? null : pageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhotoGalleryListData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PhotoGalleryListData newsListData) {
                        try {
                            if (pageIndex == 1) {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getGalleries() != null && newsListData.getData().getGalleries().getResponse() != null) {
                                    mResponse.addAll(newsListData.getData().getGalleries().getResponse());
                                    if (getContext() instanceof Activity) {
                                        mAdapter = new PhotoGalleryListAdapter(mResponse, getContext(), mCategoryID, itemBundle, customPageName);
                                        mAdapter.setOnLoadMoreListener(PhotoListGallery.this);
                                        mAdapter.setOnLoadTopBanner(PhotoListGallery.this);
                                        mAdapter.setSelectedListener(onSelectedNewsListener);
                                        setAdapter(mAdapter);
                                    }

                                }
                            } else {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getGalleries() != null && newsListData.getData().getGalleries().getResponse() != null) {
                                    ArrayList<Article> mPageList = newsListData.getData().getGalleries().getResponse();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageIndex++;
                getNewsList(mPageIndex);
            }
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

    public void setItemBundle(Bundle itemBundle) {
        this.itemBundle = itemBundle;
    }

    public void setCustomPageName(String customPageName) {
        this.customPageName = customPageName;
    }
}