package turkuvaz.sdk.galleryslider.ListGallery;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.comscore.Analytics;
import com.google.android.gms.ads.AdSize;
import com.google.gson.Gson;
import com.turquaz.videogallery.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.PushDialog.PushDialog;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.general.turkuvazgeneralwidgets.SuccessOrErrorBar.LoadStatus;
import turkuvaz.general.turkuvazgeneralwidgets.TurkuvazTextView.TTextView;
import turkuvaz.sdk.galleryslider.LockableScrollView;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.galleryslider.SparkPlayerView.SparkPlayerView;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.EventBus.PushDialogEvent;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.global.SpecialWebView;
import turkuvaz.sdk.global.TinyDB;
import turkuvaz.sdk.global.VideoToken;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.AnalyticsParameters;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GetVideoModel.VideoModel;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class VideoListDetailsActivity extends AppCompatActivity {

    FrameLayout mFrame_videoContent;
    String mAdUnit;
    ArrayList<Article> mRelatedList;
    TTextView mTv_title, mTv_spot;
    TextView mTv_Date;
    SparkPlayerView sparkPlayerView;
    SpecialWebView mWeb_detailContent;
    RecyclerView mRec_relatedVideos;
    int position = 0;
    RelatedVideosAdapter mAdapter;
    LoadStatus mLoadStatus;
    VideoModel mCurrentVideoModel;
    public static NativeMediaPlayer nativeMediaPlayer;
    boolean isActiveNativePlayer = true;
    private LockableScrollView nestedScrollView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list_details);
        mLoadStatus = findViewById(R.id.ls_videoListDetails);
        nestedScrollView = findViewById(R.id.sv_videoListDetails);

        mRec_relatedVideos = findViewById(R.id.rec_videoDetailRelated);
        mRec_relatedVideos.setLayoutManager(new LinearLayoutManager(this));
        mRec_relatedVideos.setNestedScrollingEnabled(false);

        nestedScrollView.setEnabled(true);
        nestedScrollView.setScrollingEnabled(true);

        isActiveNativePlayer = GlobalMethods.isActiveNativePlayer();
        Log.i("TAG", "onCreate: ApiListEnums.ADS_PREROLL" + isActiveNativePlayer);
        Log.i("TAG", "onCreate: ApiListEnums.ADS_PREROLL" + ApiListEnums.ADS_PREROLL.getApi(getApplicationContext()));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(VideoListDetailsActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(VideoListDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

            // toolbar text color
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(VideoListDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));
        }

        try {
            if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                BannerAds bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bannerAds.setLayoutParams(layoutParams);
                runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLayVideoDetail)).addView(bannerAds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("adUnitTag") && bundle.containsKey("position")) { // videoList parametresi kaldırıldı artık gönderilmeyecek
//            mRelatedList = bundle.getParcelableArrayList("videoList"); // arraylistin boyutu 1100 den fazlaysa bundle a set ederken sorun oluyordu. Gson ile put get yapınca ilk item null olduğu için sorun oldu
//            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
//            mRelatedList = gson.fromJson("videoList", new TypeToken<ArrayList<Article>>() {}.getType());
            try {
                mRelatedList = new ArrayList<>();
                TinyDB tinydb = new TinyDB(this);
                Gson gson = new Gson();
                for (int i = 0; i < tinydb.getListString("arrayListVideo").size(); i++)
                    mRelatedList.add(gson.fromJson(tinydb.getListString("arrayListVideo").get(i), Article.class));
                mAdUnit = bundle.getString("adUnitTag");
                position = bundle.getInt("position");
                startVideo();
            } catch (Exception e) {
                Toast.makeText(VideoListDetailsActivity.this, "Video yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } else if (bundle != null && bundle.containsKey("videoID") && bundle.containsKey("adUnitTag")) {
            mAdUnit = bundle.getString("adUnitTag");
            getSingleVideo(bundle.getString("videoID"));
        } else {
            Toast.makeText(VideoListDetailsActivity.this, "Video yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PushDialogEvent event) {
        try {
            final PushDialog pushDialog = new PushDialog(this);
            pushDialog.setPushModel(event.getPushModel());
            pushDialog.setOnPushDialog(new PushDialog.onPushDialog() {
                @Override
                public void closeDialog() {

                }

                @Override
                public void openNews(PushModel pushModel) {
                    if (pushDialog != null && pushDialog.isShowing())
                        pushDialog.closeDialog();
                    Utils.setOpenedPush(VideoListDetailsActivity.this, pushModel.getPid());
                    /** NOTIFICATION CONTROL */
                    if (pushModel != null) {
                        switch (pushModel.getTypestr()) {
                            case NEWS:
                                GlobalIntent.openNewsWithID(VideoListDetailsActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(VideoListDetailsActivity.this), pushModel.getRefid());
                                break;
                            case VIDEO:
                                GlobalIntent.openVideoDetailsWithID(VideoListDetailsActivity.this, pushModel.getRefid(), "AD_TAG");
                                break;
                            case AUTHOR:
                                GlobalIntent.openColumnistWithID(VideoListDetailsActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(VideoListDetailsActivity.this), pushModel.getRefid());
                                break;
                            case BROWSER:
                                GlobalIntent.openInternalBrowser(VideoListDetailsActivity.this, pushModel.getU());
                                break;
                            case GALLERY:
                                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(VideoListDetailsActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(VideoListDetailsActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(VideoListDetailsActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(VideoListDetailsActivity.this, showGalleryModel, ExternalTypes.ALBUM);
                                break;
                            case PHOTO_NEWS:
                                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(VideoListDetailsActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(VideoListDetailsActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(VideoListDetailsActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(VideoListDetailsActivity.this, showGalleryModel2, ExternalTypes.PHOTO_NEWS);
                                break;
                            case LIVE_STREAM:
                                GlobalIntent.openLiveStreamWithURL(VideoListDetailsActivity.this, "Canlı Yayın", pushModel.getExcurl(), ApiListEnums.ADS_PREROLL.getApi(VideoListDetailsActivity.this));
                                break;
                            case UNDEFINED:
                                break;
                        }
                    }
                }
            });

            if (!this.isFinishing())
                pushDialog.showDialog();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startVideo() {
        if (mRelatedList != null && mRelatedList.size() > 0) {
            try {
                mLoadStatus.setOnLoadTryAgain(new LoadStatus.onLoadTryAgainListener() {
                    @Override
                    public void tryAgain() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mRelatedList.size() > position)
                                        openVideoWithID(mRelatedList.get(position).getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);
                    }
                });

                try {
                    if (mRelatedList.size() > position)
                        openVideoWithID(mRelatedList.get(position).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mFrame_videoContent = findViewById(R.id.frame_videoContent);
                mTv_title = findViewById(R.id.tv_videoDetailTitle);
                mTv_Date = findViewById(R.id.tv_videoDetailDate);
                mWeb_detailContent = findViewById(R.id.web_videoDetailContent);
                mWeb_detailContent.setContentType("app-video");
                mTv_spot = findViewById(R.id.tv_videoDetailSpot);
                mRelatedList.remove(position);
                mAdapter = new RelatedVideosAdapter(mRelatedList, this);

                mTv_title.setTextSize(Preferences.getInt(VideoListDetailsActivity.this, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25));
                mTv_spot.setTextSize(Preferences.getInt(VideoListDetailsActivity.this, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));


            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void openVideoWithID(final String videoID) {
        RestInterface mRestInterface = ApiClient.getClientApi(VideoListDetailsActivity.this).create(RestInterface.class);
        mRestInterface.getVideoURL(ApiListEnums.DETAILS_VIDEO.getApi(this), videoID.toLowerCase()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        if (isActiveNativePlayer) {
                            nativeMediaPlayer = new NativeMediaPlayer(VideoListDetailsActivity.this);
                            nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                                try {
                                    if (isFullScreen) {
                                        ((FrameLayout) findViewById(R.id.frame_videoDetailsTopBanner)).setVisibility(View.GONE);
                                        Objects.requireNonNull(getSupportActionBar()).hide();
                                        int scrollTo = ((View) mFrame_videoContent.getParent().getParent()).getTop() + mFrame_videoContent.getTop();
                                        nestedScrollView.smoothScrollTo(0, scrollTo);
                                        new Handler().postDelayed(() -> {
                                            nestedScrollView.setEnabled(false);
                                            nestedScrollView.setScrollingEnabled(false);
                                        }, 600);
                                    } else {
                                        ((FrameLayout) findViewById(R.id.frame_videoDetailsTopBanner)).setVisibility(View.VISIBLE);
                                        Objects.requireNonNull(getSupportActionBar()).show();
                                        nestedScrollView.setEnabled(true);
                                        nestedScrollView.setScrollingEnabled(true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            sparkPlayerView = new SparkPlayerView(VideoListDetailsActivity.this);
                            sparkPlayerView.setOnFullScreenChange(isFullScreen -> {
                                if (!isFullScreen)
                                    findViewById(R.id.sv_videoListDetails).scrollTo(0, 0);
                            });
                        }
                    }

                    @Override
                    public void onNext(VideoModel videoModel) {
                        try {
                            Article videoArticle = videoModel.getData().getListVideoHomeAndDetailPage().getResponse().get(0);
                            mTv_title.setText(videoArticle.getTitle());
                            mTv_spot.setText(videoArticle.getSpot());

                            if (videoArticle.getOutputDate() != null && !TextUtils.isEmpty(videoArticle.getOutputDate()))
                                mTv_Date.setText(videoArticle.getOutputDate().trim());
                            else if (videoArticle.getModifiedDate() != null && !TextUtils.isEmpty(videoArticle.getModifiedDate()))
                                mTv_Date.setText(convertDate(videoArticle.getModifiedDate()));
                            else
                                mTv_Date.setVisibility(View.GONE);

                            mWeb_detailContent.setArticle(videoArticle);
                            mWeb_detailContent.loadNews();
                            mWeb_detailContent.setOnSelectedURL(new SpecialWebListener(VideoListDetailsActivity.this, null, null));

                            if (videoModel.getData().getListVideoHomeAndDetailPage().getResponse().size() > 0) {
                                mCurrentVideoModel = videoModel;
                                String title = videoArticle.getTitle();
                                String category = videoArticle.getCategoryName();
                                String categoryID = videoArticle.getCategoryId();
                                String VideoSmilUrl = videoArticle.getVideoSmilUrl();
                                String VideoMobileUrl = videoArticle.getVideoMobileUrl();
                                String VideoUrl = videoArticle.getVideoUrl();

                                if (isActiveNativePlayer && nativeMediaPlayer == null)
                                    return;

                                if (!isActiveNativePlayer && sparkPlayerView == null)
                                    return;
                                if (!isActiveNativePlayer)
                                    sparkPlayerView.setArticle(videoArticle);

                                if (VideoSmilUrl != null && !TextUtils.isEmpty(VideoSmilUrl)) {
                                    if (VideoSmilUrl.contains("atv-vod.ercdn.net")) {
                                        new VideoToken().getVideoToken(VideoSmilUrl, VideoListDetailsActivity.this).setVideoComplated(new VideoToken.getVideoComplated() {
                                            @Override
                                            public void onSuccess(String videoUrl) {
//                                                sparkPlayerView.setVideoURL(videoUrl);
//                                                sparkPlayerView.view();
                                                playVideo(videoUrl);
                                            }
                                        });
                                    } else {
//                                        sparkPlayerView.setVideoURL(VideoSmilUrl);
//                                        sparkPlayerView.view();
                                        playVideo(VideoSmilUrl);
                                    }

                                } else if (VideoMobileUrl != null && !TextUtils.isEmpty(VideoMobileUrl)) {
                                    if (VideoMobileUrl.contains("atv-vod.ercdn.net")) {
                                        new VideoToken().getVideoToken(VideoMobileUrl, VideoListDetailsActivity.this).setVideoComplated(new VideoToken.getVideoComplated() {
                                            @Override
                                            public void onSuccess(String videoUrl) {
//                                                sparkPlayerView.setVideoURL(videoUrl);
//                                                sparkPlayerView.view();
                                                playVideo(videoUrl);
                                            }

                                        });
                                    } else {
//                                        sparkPlayerView.setVideoURL(VideoMobileUrl);
//                                        sparkPlayerView.view();
                                        playVideo(VideoMobileUrl);
                                    }

                                } else {
                                    if (VideoUrl.contains("atv-vod.ercdn.net")) {
                                        new VideoToken().getVideoToken(VideoUrl, VideoListDetailsActivity.this).setVideoComplated(new VideoToken.getVideoComplated() {
                                            @Override
                                            public void onSuccess(String videoUrl) {
//                                                sparkPlayerView.setVideoURL(videoUrl);
//                                                sparkPlayerView.view();
                                                playVideo(videoUrl);
                                            }

                                        });
                                    } else {
//                                        sparkPlayerView.setVideoURL(VideoUrl);
//                                        sparkPlayerView.view();
                                        playVideo(VideoUrl);
                                    }

                                }

                                /** SEND ANALYTICS */
                                new TurkuvazAnalytic(VideoListDetailsActivity.this).setIsPageView(true).setLoggable(true)
                                        .setEventName(AnalyticsEvents.VIDEO_DETAILS.getEventName())
                                        .addParameter(AnalyticsParameters.CATEGORY.getParametersName(), category)
                                        .addParameter(AnalyticsParameters.TITLE.getParametersName(), title)
                                        .sendEvent();

                                /** SET ADS */
                                BannerAds bannerAdsTop = new BannerAds(VideoListDetailsActivity.this, new AdSize(320, 142), GlobalMethods.getCategoryZone(VideoListDetailsActivity.this, categoryID, AdsType.ADS_320x142));

                                /** SET ADS */
                                BannerAds bannerAdsBottom = new BannerAds(VideoListDetailsActivity.this, new AdSize(300, 250), GlobalMethods.getCategoryZone(VideoListDetailsActivity.this, categoryID, AdsType.ADS_300x250));

                                FrameLayout frameBottomBanner = (FrameLayout) findViewById(R.id.frame_videoDetailsBottomBanner);
                                FrameLayout frameTopBanner = (FrameLayout) findViewById(R.id.frame_videoDetailsTopBanner);

                                frameTopBanner.addView(bannerAdsTop);
                                frameBottomBanner.addView(bannerAdsBottom);

                                if (Preferences.getBoolean(VideoListDetailsActivity.this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(VideoListDetailsActivity.this).equals(""))
                                    frameBottomBanner.setPadding(frameBottomBanner.getPaddingLeft(), frameBottomBanner.getPaddingTop(), frameBottomBanner.getPaddingRight(), (int) (65 * getResources().getDisplayMetrics().density));

//                                sparkPlayerView.setArticle(videoArticle);

                                if (isActiveNativePlayer) {
                                    mFrame_videoContent.addView(nativeMediaPlayer);
                                } else {
                                    mFrame_videoContent.addView(sparkPlayerView);
                                }
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadStatus.loadStatusError("");
                    }

                    @Override
                    public void onComplete() {
                        mRec_relatedVideos.setAdapter(mAdapter);
                        mLoadStatus.loadStatusSuccess();
                    }
                });
    }

    private void playVideo(String videoUrl) {
        if (isActiveNativePlayer) {
            nativeMediaPlayer.setVideoURL(videoUrl);
            nativeMediaPlayer.setActivity(this);
            nativeMediaPlayer.init();
        } else {
            sparkPlayerView.setVideoURL(videoUrl);
            sparkPlayerView.init();
        }
    }

    private String convertDate(String irregularDate) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM, EEEE", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (isActiveNativePlayer) {
                if (mFrame_videoContent != null && nativeMediaPlayer != null) {
                    mFrame_videoContent.removeAllViews();
                }
            } else {
                if (mFrame_videoContent != null && sparkPlayerView != null) {
                    sparkPlayerView.closeSpark();
                    mFrame_videoContent.removeAllViews();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_share) {
            try {
                new TurkuvazAnalytic(this).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.ACTION_VIDEO_SHARE.getEventName()).sendEvent();
                GlobalIntent.shareGeneral(VideoListDetailsActivity.this, ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + mCurrentVideoModel.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getUrl(), mCurrentVideoModel.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getTitle());
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.video_details_page, menu);
        MenuItem item = menu.findItem(R.id.menu_share);
        // bu iconlar ve arkaplan rengi parametrik olacak
        DrawableCompat.setTint(item.getIcon(), Color.parseColor(Preferences.getString(VideoListDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"))); // paylaş iconu
        return true;
    }

    public void getSingleVideo(String videoID) {
        RestInterface mRestInterface = ApiClient.getClientApi(VideoListDetailsActivity.this).create(RestInterface.class);
        mRestInterface.getVideoURL(ApiListEnums.DETAILS_VIDEO.getApi(VideoListDetailsActivity.this), videoID.toLowerCase()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoModel videoModel) {
                        try {
                            if (videoModel.getData().getListVideoHomeAndDetailPage().getResponse().size() > 0) {

                                ArrayList<Article> articles = new ArrayList<>();
                                articles.add(videoModel.getData().getListVideoHomeAndDetailPage().getResponse().get(0));
                                mRelatedList = articles;
                                position = 0;
                                startVideo();
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VideoListDetailsActivity.this, "Video yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Analytics.notifyUxActive();
        Analytics.notifyEnterForeground();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Analytics.notifyUxInactive();
        Analytics.notifyExitForeground();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}