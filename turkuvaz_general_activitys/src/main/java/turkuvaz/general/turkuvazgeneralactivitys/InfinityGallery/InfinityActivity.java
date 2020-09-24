package turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.AlbumMedia;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.GalleryModel;


import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class InfinityActivity extends RelativeLayout implements GalleryAdapter.OnLoadMoreListener {
    private RestInterface mRestInterface;
    private RecyclerView mRec_infinityGallery;
    private GalleryAdapter mGalleryAdapter;
    private List<turkuvaz.sdk.models.Models.InfinityGalleryModel.Response> mResponse;
    public static int mPageIndex = 1;
    private ShowGalleryModel showGalleryModel;
    private ProgressBar mPb_loadingInfinityGallery;
    private List<AlbumMedia> mAllAlbumList;
    private OnSelectedURLListener onSelectedURLListener;
    private String title, spot, url;
    private int selectedPosition;

    public InfinityActivity(@NonNull Context context) {
        super(context);
    }

    public InfinityActivity(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfinityActivity(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setShowGalleryModel(ShowGalleryModel showGalleryModel) {
        this.showGalleryModel = showGalleryModel;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setOnSelectedURLListener(OnSelectedURLListener onSelectedURLListener) {
        this.onSelectedURLListener = onSelectedURLListener;
    }

    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.infinity_gallery_activity, this, true);

        createClassObject();

        getGalleryList(mPageIndex);
    }


    private void createClassObject() {
        mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRec_infinityGallery = findViewById(R.id.rec_infinityGallery);
        mRec_infinityGallery.setLayoutManager(mLayoutManager);
        mRec_infinityGallery.setHasFixedSize(false);
        mRec_infinityGallery.setItemViewCacheSize(150);
        mRec_infinityGallery.setDrawingCacheEnabled(true);
        mRec_infinityGallery.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mPb_loadingInfinityGallery = findViewById(R.id.pb_loadingInfinityGallery);
        ImageButton jumpToTop = findViewById(R.id.btn_jumpToTop);

        if (Preferences.getBoolean(getContext(), ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(getContext()).equals(""))
            jumpToTop.setPadding(jumpToTop.getPaddingLeft(), jumpToTop.getPaddingTop(), jumpToTop.getPaddingRight(), (int) (60 * getResources().getDisplayMetrics().density));

        if (jumpToTop != null)
            jumpToTop.setOnClickListener(v -> jumpToTop());


        MiddleItemFinder.MiddleItemCallback middleItemCallback = new MiddleItemFinder.MiddleItemCallback() {
            @Override
            public void scrollFinished(int middleElement) {
                if (onSelectedURLListener != null && mAllAlbumList != null) {
                    if (mAllAlbumList.get(middleElement) != null) {
                        try {
                            onSelectedURLListener.onCurrentIndex(mAllAlbumList.get(middleElement));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void jumpToTop(boolean isShow) {

                if (jumpToTop != null)
                    if (isShow)
                        jumpToTop.setVisibility(VISIBLE);
                    else
                        jumpToTop.setVisibility(INVISIBLE);
            }
        };

        mRec_infinityGallery.addOnScrollListener(new MiddleItemFinder(getContext(), mLayoutManager, middleItemCallback));

    }

    private void getGalleryList(final int pageIndex) {
        mRestInterface.getInfinityGallery(showGalleryModel.getApiPath(), showGalleryModel.getGalleryID(), pageIndex == 1 ? null : pageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GalleryModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GalleryModel response) {
                        try {
                            mPb_loadingInfinityGallery.setVisibility(View.GONE);
                            if (response.getData() != null && response.getData().getGalleryDetailsById() != null) {
                                findViewById(R.id.pb_loadMoreGallery).setVisibility(View.GONE);
                                if (pageIndex == 1) {
                                    mResponse = response.getData().getGalleryDetailsById().getResponse();
                                    mAllAlbumList = mResponse.get(0).getAlbumMedias();

                                    //TODO fotohaber ise haber title, spot ve url'i kullanılacak
                                    if (title != null)
                                        mResponse.get(0).setTitle(title);
                                    if (spot != null)
                                        mResponse.get(0).setDescription(spot);
                                    if (url != null)
                                        mResponse.get(0).setUrl(url);

                                    mGalleryAdapter = new GalleryAdapter(mResponse, getContext(), showGalleryModel.getAdsCode(), showGalleryModel.getShareDomain(), showGalleryModel.getGetVideoById_ApiPath(), showGalleryModel, onSelectedURLListener, mResponse.get(0).getAlbumMediaCount());
                                    mGalleryAdapter.setOnLoadMoreListener(InfinityActivity.this, pageIndex);
                                    mRec_infinityGallery.setAdapter(mGalleryAdapter);
                                } else {
                                    List<AlbumMedia> mPageList = response.getData().getGalleryDetailsById().getResponse().get(0).getAlbumMedias();
                                    mAllAlbumList.addAll(mPageList);
                                    mGalleryAdapter.notifyDataSet(mPageList, mPageList.size());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPb_loadingInfinityGallery.setVisibility(View.GONE);
                        onErrorDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void shareGallery() {
        try {
            if (mResponse.get(0).getUrl() == null || showGalleryModel.getShareDomain() == null)
                return;

            new TurkuvazAnalytic(getContext()).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.ACTION_GALLERY_SHARE.getEventName()).sendEvent();

            String mShareUrl = mResponse.get(0).getUrl();
            if (!mResponse.get(0).getUrl().contains("http") && !mResponse.get(0).getUrl().contains(showGalleryModel.getShareDomain())) {
                mShareUrl = showGalleryModel.getShareDomain() + mResponse.get(0).getUrl();
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_TEXT, mResponse.get(0).getTitle() + " " + mShareUrl);
            getContext().startActivity(Intent.createChooser(share, getResources().getString(R.string.paylas)));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onErrorDialog() {
        Toast.makeText(getContext(), getResources().getString(R.string.galeri_hata), Toast.LENGTH_SHORT).show();
        ((Activity) getContext()).finish();
    }

    @Override
    public void onLoadMore() {
        findViewById(R.id.pb_loadMoreGallery).setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageIndex++;
                getGalleryList(mPageIndex);
            }
        }, 1500);

    }

    public interface OnSelectedURLListener {
        void onVideo(String VideoURL, String VideoCategory, String VideoTitle);

        void onNews(String NewsID);

        void onAlbum(String AlbumID);

        void onPhotoNews(String PhotoNewsID);

        void onArticle(String ArticleSourceID);

        void onExternal(String ExternalURL);

        void onInternal(String InternalURL);

        void onOpenGallery(String GalleryTitle, String GalleryURL);

        void onCurrentIndex(AlbumMedia albumMedia);

    }

    public void jumpToTop() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mRec_infinityGallery.smoothScrollToPosition(0);
                timer.cancel();
            }
        }, 0, 100);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}