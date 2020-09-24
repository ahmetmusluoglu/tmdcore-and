package turkuvaz.general.turkuvazgeneralwidgets.NewsList;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.NewsList.Adapter.NewsListAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.NewsListType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.NewsList.NewsListData;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 22/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class NewsList extends RecyclerView implements NewsListAdapter.OnLoadMoreListener {
    private RestInterface mRestInterface;
    private NewsListAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private int mPageIndex = 1;
    private String mApiUrl, mCategoryID, articleId;
    private NewsListAdapter.onSelectedNewsListener onSelectedNewsListener;
    private onStatusListener onStatusListener;
    private boolean isOnLoadMoreMode = false;
    private int startToItem = -1;
    private boolean isCategoryShow = true;
    private boolean isHomepage = false;
    private boolean isSurmanset = false;
    private boolean isAdv = false;
    private boolean isShowTitle = true;
    private boolean isTitleOverImage = false;
    private NewsListType newsListType = NewsListType.GRID_2x2_BOX;
    private int takeCount;

    public NewsList(@NonNull Context context) {
        super(context);
    }

    public NewsList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCategoryShow(boolean categoryShow) {
        isCategoryShow = categoryShow;
    }

    public void setApiPath(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void setCategoryID(String categoryID) {
        this.mCategoryID = categoryID;
    }

    public void setTakeCount(int takeCount) {
        this.takeCount = takeCount;
    }

    public void setArticleID(String articleId) {
        this.articleId = articleId;
    }

    public void setOnSelectedNewsListener(NewsListAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void setOnStatusListener(onStatusListener onStatusListener) {
        this.onStatusListener = onStatusListener;
    }

    public void setNewsListType(NewsListType newsListType) {
        this.newsListType = newsListType;
    }

    boolean isCheckContainsNews;

    public void checkContainsNews(boolean isCheckContainsNews) {
        this.isCheckContainsNews = isCheckContainsNews;
    }

    public void init(boolean isLastComponent) {
        this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);

        // LIST TYPE LAYOUT MANAGERS
        if (newsListType == NewsListType.GRID_2x2_BOX) {
            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            this.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            this.setLayoutManager(linearLayoutManager);
        }

        if (isLastComponent && Preferences.getBoolean(getContext(), ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(getContext()).equals(""))
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) (80 * getResources().getDisplayMetrics().density));

        setNestedScrollingEnabled(false);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        if (isCheckContainsNews)
            getNewsList2();
        else
            getNewsList(mPageIndex);
    }

    public void initWithData(ArrayList<Article> mResponse) {
        this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        // LIST TYPE LAYOUT MANAGERS */
        if (newsListType == NewsListType.GRID_2x2_BOX) {
            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            this.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            this.setLayoutManager(linearLayoutManager);
        }

        if (Preferences.getBoolean(getContext(), ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(getContext()).equals(""))
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) (50 * getResources().getDisplayMetrics().density));

        setNestedScrollingEnabled(false);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mAdapter = new NewsListAdapter(mResponse, getContext());
        mAdapter.setOnLoadMoreListener(NewsList.this);
        mAdapter.setSelectedListener(onSelectedNewsListener);
        setAdapter(mAdapter);
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
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getArticles() != null && newsListData.getData().getArticles().getResponse() != null && newsListData.getData().getArticles().getResponse().size()>0) {

                                    if (startToItem == -1) {
                                        mResponse.addAll(newsListData.getData().getArticles().getResponse());
                                    } else {
                                        mResponse.addAll(newsListData.getData().getArticles().getResponse().subList(startToItem, newsListData.getData().getArticles().getResponse().size()));
                                    }
                                    if (getContext() instanceof Activity) {
                                        if (isAdv && mResponse.size() > 0) {//Adv listesinden birden fazla adv gelirse, sadece ilki basılacak...
                                            ArrayList<Article> currResponse = new ArrayList<>();
                                            currResponse.add(mResponse.get(0));
                                            mResponse.clear();
                                            mResponse.addAll(currResponse);
                                        }
                                        mAdapter = new NewsListAdapter(mResponse, getContext());
                                        mAdapter.setOnLoadMoreListener(NewsList.this);
                                        mAdapter.setShowCategory(isCategoryShow);
                                        mAdapter.setNewsListType(newsListType);
                                        mAdapter.setSelectedListener(onSelectedNewsListener);
                                        mAdapter.setHomepage(isHomepage);
                                        mAdapter.setSurmanset(isSurmanset);
                                        mAdapter.setShowTitle(isShowTitle);
                                        mAdapter.setTitleOverImage(isTitleOverImage);
                                        setAdapter(mAdapter);
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

    public void getNewsList2() {
        mRestInterface.getNewsList(mApiUrl, mCategoryID, 1).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsListData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsListData newsListData) {
                        try {
                            if (mCategoryID == null) {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getArticles() != null && newsListData.getData().getArticles().getResponse() != null && newsListData.getData().getArticles().getResponse().size() > 0) {

                                    if (startToItem == -1) {
                                        mResponse.addAll(newsListData.getData().getArticles().getResponse());
                                    } else {
                                        mResponse.addAll(newsListData.getData().getArticles().getResponse().subList(startToItem, newsListData.getData().getArticles().getResponse().size()));
                                    }
                                    if (getContext() instanceof Activity) {
                                        if (isCheckContainsNews && mResponse.size() > 0) {
                                            ArrayList<Article> currResponse = new ArrayList<>();
                                            for (int i = 0; i < mResponse.size(); i++) {
                                                if (!mResponse.get(i).getArticleId().equals(articleId) && currResponse.size() < takeCount) {
                                                    currResponse.add(mResponse.get(i));
                                                }
                                            }
                                            mResponse.clear();
                                            mResponse.addAll(currResponse);
                                        }

                                        mAdapter = new NewsListAdapter(mResponse, getContext());
                                        mAdapter.setOnLoadMoreListener(NewsList.this);
                                        mAdapter.setShowCategory(isCategoryShow);
                                        mAdapter.setNewsListType(newsListType);
                                        mAdapter.setSelectedListener(onSelectedNewsListener);
                                        mAdapter.setHomepage(isHomepage);
                                        mAdapter.setShowBanner(false);
                                        mAdapter.setSurmanset(isSurmanset);
                                        mAdapter.setShowTitle(isShowTitle);
                                        mAdapter.setTitleOverImage(isTitleOverImage);
                                        setAdapter(mAdapter);
                                        if (onListener != null)
                                            onListener.onSuccess(true);
                                    }
                                }

                            } else {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getArticles() != null && newsListData.getData().getArticles().getResponse() != null) {
                                    ArrayList<Article> mPageList = newsListData.getData().getArticles().getResponse();
                                    if (isCheckContainsNews) {
                                        ArrayList<Article> currResponse = new ArrayList<>();
                                        if (mResponse.size() > 0) {
                                            for (Iterator<Article> iterator = mPageList.iterator(); iterator.hasNext(); ) {
                                                Article art = iterator.next();
                                                for (Article article : mResponse) {
                                                    if (art.getArticleId().equals(article.getArticleId()) || art.getArticleId().equals(articleId)) {
                                                        iterator.remove();
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        for (int i = 0; i < mPageList.size(); i++) {
                                            if (!mPageList.get(i).getArticleId().equals(articleId) && currResponse.size() < takeCount) {
                                                currResponse.add(mPageList.get(i));
                                            }
                                        }
                                        mPageList.clear();
                                        mPageList.addAll(currResponse);
                                    }
                                    mAdapter.setShowBanner(false);
                                    mAdapter.notifyDataSet(mPageList, mPageList.size());
                                }
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e.getMessage() != null && onListener != null)
                            onListener.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (onListener != null)
                            onListener.onSuccess(mCategoryID == null);
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

    public interface onStatusListener {
        void onStartLoad();

        void onSuccess();

        void onError(String errorMessage);
    }

    OnListener onListener;

    public interface OnListener {

        void onSuccess(boolean isFirst);

        void onError(String errorMessage);
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    public void setStartToItem(int startToItem) {
        this.startToItem = startToItem;
    }

    public void setHomepage(boolean isHomepage) {
        this.isHomepage = isHomepage;
    }

    public void setShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public void setSurmanset(boolean isSurmanset) {
        this.isSurmanset = isSurmanset;
    }

    public void setTitleOverImage(boolean isTitleOverImage) {
        this.isTitleOverImage = isTitleOverImage;
    }

    public void setAdv(boolean isAdv) {
        this.isAdv = isAdv;
    }
}