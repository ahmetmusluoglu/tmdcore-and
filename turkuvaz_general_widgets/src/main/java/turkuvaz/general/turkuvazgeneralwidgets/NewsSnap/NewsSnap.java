package turkuvaz.general.turkuvazgeneralwidgets.NewsSnap;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.NewsSnap.Adapter.NewsSnapAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.NewsSnap.Utils.MultiSnapRecyclerView;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.SnapNews.SnapNewsData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

/**
 * Created by turgay.ulutas on 21/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class NewsSnap extends FrameLayout {

    private RestInterface mRestInterface;
    private NewsSnapAdapter mAdapter;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private MultiSnapRecyclerView mRec_newsSnap;
    private Button mBtn_tryAgain;
    private LinearLayoutManager mLayoutManager;
    private NewsSnapAdapter.onSelectedNewsListener onSelectedNewsListener;
    private String mApiUrl;
    private ImageButton mBtn_next, mBtn_previous;
    private boolean isTitleVisible = false;
    private boolean isSingle = false;
    private boolean isTitleOverImage = true;
    private int textSize = 0;

    public NewsSnap(Context context) {
        super(context);
    }

    public NewsSnap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsSnap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NewsSnap(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnSelectedNewsListener(NewsSnapAdapter.onSelectedNewsListener onSelectedNewsListener) {
        this.onSelectedNewsListener = onSelectedNewsListener;
    }

    public void setApiUrl(String apiUrl) {
        this.mApiUrl = apiUrl;
    }

    public void setSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.news_sanp_main, this, true);

        this.mRec_newsSnap = findViewById(R.id.rec_snapNewsList);
        this.mBtn_tryAgain = findViewById(R.id.btn_snapNewsTryAgain);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        this.mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        this.mRec_newsSnap.setLayoutManager(this.mLayoutManager);
        this.mAdapter = new NewsSnapAdapter(this.mResponse, getContext());
        this.mAdapter.setSelectedListener(this.onSelectedNewsListener);
        this.mAdapter.setSingle(isSingle);
        this.mAdapter.setTitleShow(isTitleVisible);
        this.mAdapter.setTitleOverImage(isTitleOverImage);
        this.mAdapter.setTextSize(textSize);
        this.mBtn_next = findViewById(R.id.btn_nextSnap);
        this.mBtn_previous = findViewById(R.id.btn_previousSnap);
        if (isSingle)
            mBtn_next.setVisibility(INVISIBLE);

        mBtn_next.setOnClickListener(v -> {
            try {
                mRec_newsSnap.smoothScrollToPosition(mLayoutManager.findLastCompletelyVisibleItemPosition() + (isSingle ? 1 : 2));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mBtn_previous.setOnClickListener(v -> {
            try {
                if (mLayoutManager.findFirstVisibleItemPosition() >= 2)
                    mRec_newsSnap.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition() - (isSingle ? 1 : 2));
                else
                    mRec_newsSnap.smoothScrollToPosition(mLayoutManager.findFirstVisibleItemPosition() - 1);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mRec_newsSnap.setOnSnapListener(position -> {
            try {
                if (mBtn_previous == null || mBtn_next == null)
                    return;

                if (position == 0 || isSingle)
                    mBtn_previous.setVisibility(INVISIBLE);
                else
                    mBtn_previous.setVisibility(VISIBLE);

                if (position >= (mResponse.size() - 1) || isSingle) {
                    mBtn_next.setVisibility(INVISIBLE);
                } else {
                    mBtn_next.setVisibility(VISIBLE);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        //TODO Hata oluşması durumunda yeniden dene butonu
        mBtn_tryAgain.setOnClickListener(v -> getSnapNews(mApiUrl));

        //TODO Son dakika haberleri alınıyor..
        getSnapNews(mApiUrl);
    }

    private void getSnapNews(String apiUrl) {
        mRestInterface.getSnapNews(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SnapNewsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SnapNewsData snapNewsData) {
                        try {
                            if (snapNewsData != null && snapNewsData.getData() != null && snapNewsData.getData().getArticles() != null && snapNewsData.getData().getArticles().getResponse() != null) {
                                mBtn_tryAgain.setVisibility(GONE);
                                mResponse.addAll(snapNewsData.getData().getArticles().getResponse());
                            } else {
                                mBtn_tryAgain.setVisibility(VISIBLE);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mBtn_tryAgain.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        if (mAdapter == null || mRec_newsSnap == null)
                            return;
                        if (!isSingle)
                            mBtn_next.setVisibility(VISIBLE);
                        mRec_newsSnap.setAdapter(mAdapter);
                    }
                });
    }

    public void setTitleVisible(boolean titleVisible) {
        isTitleVisible = titleVisible;
    }

    public void setTitleOverImage(boolean titleOverImage) {
        isTitleOverImage = titleOverImage;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
