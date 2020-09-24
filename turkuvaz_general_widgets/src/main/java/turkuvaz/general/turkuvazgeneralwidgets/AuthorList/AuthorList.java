package turkuvaz.general.turkuvazgeneralwidgets.AuthorList;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.AuthorList.AuthorsModel;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;

/**
 * Created by turgay.ulutas on 22/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class AuthorList extends RecyclerView implements AuthorListAdapter.OnLoadMoreListener  {
    private RestInterface mRestInterface;
    private LinearLayoutManager mLayoutManager;
    private AuthorListAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private String mApiUrl;
    private AuthorListAdapter.onSelectedNewsListener onSelectedNewsListener;
    private onStatusListener onStatusListener;
    private String authorID = null;
    private int mPageIndex = 1;
    private boolean pagination = false;
    private boolean tumYazilariEkraniAc = false;

    public AuthorList(@NonNull Context context) {
        super(context);
    }

    public AuthorList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AuthorList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setApiPath(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void setAuthorsID(String authorsID) {
        this.authorID = authorsID;
        this.mResponse.clear();
        this.init();
    }

    public void setTumYazilariEkraniAc(boolean tumYazilariEkraniAc) {
        this.tumYazilariEkraniAc = tumYazilariEkraniAc;
    }

    public int getmPageIndex() {
        return mPageIndex;
    }

    public void setmPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
    }
    public String getAuthorsID() {
        return this.authorID;
    }

    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public void setOnSelectedNewsListener(AuthorListAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void init() {
        this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));
        this.setPadding(5, 0, 5, 0);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.setLayoutManager(mLayoutManager);
        setNestedScrollingEnabled(false);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        getAuthorsList(mPageIndex);
    }

    private void getAuthorsList(final int mPageIndex) {
//        mRestInterface.getAuthorList(mApiUrl, authorID).subscribeOn(Schedulers.io())
        mRestInterface.getAuthorList(mApiUrl, authorID, mPageIndex == 1 ? null : mPageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthorsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (onStatusListener != null)
                            onStatusListener.onStartLoad();
                    }

                    @Override
                    public void onNext(AuthorsModel authorsModel) {
                        try {
                            if (tumYazilariEkraniAc) { // spinner'dan bir yazar seçince, authorlist ekranına gidecek
                                if (authorsModel != null && authorsModel.getData() != null && authorsModel.getData().getColumnists() != null && authorsModel.getData().getColumnists().getResponse() != null) {
                                    mResponse.addAll(authorsModel.getData().getColumnists().getResponse());
                                    if (getContext() instanceof Activity) {
                                        if (authorID != null && mResponse.size() > 0 && mResponse.get(0) != null)
                                            GlobalIntent.openAuthorsArchive(getContext(), ApiListEnums.COLUMNIST_BY_SOURCE.getApi(getContext()), mResponse.get(0).getArticleSourceId(), mResponse.get(0).getSource(), mResponse.get(0).getPrimaryImage());
                                        else
                                            Toast.makeText(getContext(), "Yazara Ait Yazı Bulunamadı", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (mPageIndex == 1) {
                                    if (authorsModel != null && authorsModel.getData() != null && authorsModel.getData().getColumnists() != null && authorsModel.getData().getColumnists().getResponse() != null) {
                                        mResponse.addAll(authorsModel.getData().getColumnists().getResponse());
                                        if (getContext() instanceof Activity) {
                                            mAdapter = new AuthorListAdapter(mResponse, getContext(), isPagination());
                                            mAdapter.setOnLoadMoreListener(AuthorList.this);
                                            mAdapter.setSelectedListener(onSelectedNewsListener);
                                            setAdapter(mAdapter);
                                        }
                                    }
                                } else { // sayfalama çalıştığı zaman
                                    if (authorsModel != null && authorsModel.getData() != null && authorsModel.getData().getColumnists() != null && authorsModel.getData().getColumnists().getResponse() != null) {
                                        ArrayList<Article> mPageList = authorsModel.getData().getColumnists().getResponse();
                                        mAdapter.setPagination(true);
                                        mAdapter.notifyDataSet(mPageList, mPageList.size());
                                    }
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
        mPageIndex++;
        getAuthorsList(mPageIndex);
    }


    public interface onStatusListener {
        void onSuccess();

        void onError(String errorMessage);

        void onStartLoad();

    }

    public void setOnStatusListener(onStatusListener onStatusListener) {
        this.onStatusListener = onStatusListener;
    }

}
