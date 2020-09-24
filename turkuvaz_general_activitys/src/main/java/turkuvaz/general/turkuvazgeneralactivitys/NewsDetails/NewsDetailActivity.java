package turkuvaz.general.turkuvazgeneralactivitys.NewsDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdSize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.ShareBar.ShareBar;
import turkuvaz.general.turkuvazgeneralwidgets.SuccessOrErrorBar.LoadStatus;
import turkuvaz.sdk.galleryslider.ListGallery.VideoListDetailsActivity;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.global.SpecialWebView;
import turkuvaz.sdk.global.VideoEnabledWebChromeClient;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.models.Models.SingleNews.SingleNewsModel;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class NewsDetailActivity extends BaseGeneralActivity implements SpecialWebListener.onStartPopupVideoListener, View.OnTouchListener {

    private SpecialWebView mWeb_details;
    private ImageView mImg_newsTop;
    private TextView mTv_title;
    private TextView mTv_spot;
    private TextView mTv_date;
    private ShareBar mShareBar;
    private LoadStatus loadStatus;

    private VideoEnabledWebChromeClient webChromeClient;

    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SparkPlayerViewPopup sparkPlayerViewPopup = null;
    private static final int MAX_CLICK_DISTANCE = 15;
    private static final int MAX_CLICK_DURATION = 1000;

    private long pressStartTime;
    private int pressedX;
    private int pressedY;
    int offsetX, offsetY;
    int actionBarHeight = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_single_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            // toolbar text color
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(NewsDetailActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));

            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(NewsDetailActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(NewsDetailActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        loadStatus = findViewById(R.id.load_newsSingle);
        mWeb_details = findViewById(R.id.web_details);
        mWeb_details.setContentType("app-news");
        mImg_newsTop = findViewById(R.id.img_newsDetailSingle);
        mTv_title = findViewById(R.id.tv_newsTitle);
        mTv_spot = findViewById(R.id.tv_newsSpot);
        mTv_date = findViewById(R.id.tv_newsDate);
        mShareBar = findViewById(R.id.shareBar_singleNews);
        ImageView mImg_toolbarIcon = findViewById(R.id.img_newsDetailSingleToolbar);


        mTv_title.setTextSize(Preferences.getInt(NewsDetailActivity.this, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25));
        mTv_spot.setTextSize(Preferences.getInt(NewsDetailActivity.this, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));
        mImg_toolbarIcon.setImageResource(IconTypes.getIcon(this.getApplicationInfo().packageName));

        Bundle bundle = getIntent().getExtras(); // bildirimden gelinirse, haberid ile API'den haber alınır.
        if (bundle != null && bundle.containsKey("apiPath") && bundle.containsKey("newsID")) {
            getNews(bundle.getString("apiPath"), bundle.getString("newsID"));
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_goHome) {
            GlobalIntent.openForceHome(NewsDetailActivity.this, false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news_details, menu);
        MenuItem item = menu.findItem(R.id.action_goHome);
        DrawableCompat.setTint(item.getIcon(), Color.parseColor(Preferences.getString(NewsDetailActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"))); // anasayfa iconu

        return true;
    }


    private void getNews(String apiPath, String newsID) {
        RestInterface mRestInterface = ApiClient.getClientApi(NewsDetailActivity.this).create(RestInterface.class);
        mRestInterface.getSingleNews(apiPath, newsID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleNewsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SingleNewsModel singleNewsModel) {
                        try {
                            if (singleNewsModel != null && singleNewsModel.getData() != null && singleNewsModel.getData().getArticle().getResponse() != null) {
                                Article article = singleNewsModel.getData().getArticle().getResponse().get(0);
                                if (article == null)
                                    return;

                                // haber detay için yapılan iş, bildirim ekranındaki haber detay için de yapıldı
                                String newsSource = article.getSource() != null && !article.getSource().isEmpty()
                                        ? "<br/>" + getResources().getString(R.string.source_name)
                                        + "<font color=\"#C40016\"> " + article.getSource() + "</font>" : "";

                                mWeb_details.setArticle(article);
                                mTv_title.setText(article.getTitle());
                                mTv_spot.setText(article.getSpot());
                                if (article.getOutputDate() != null && !TextUtils.isEmpty(article.getOutputDate())) // TAKVIM-5561
                                    mTv_date.setText(String.valueOf(String.valueOf(article.getOutputDate().trim())));
                                else if (article.getModifiedDate() != null && !TextUtils.isEmpty(article.getModifiedDate()))
                                    mTv_date.setText(Html.fromHtml(getResources().getString(R.string.son_guncelleme, convertDate(article.getModifiedDate()), newsSource)));

                                if (article.getTitle() != null && article.getUrl() != null) {
                                    mShareBar.setSocialMediaClickListener(new ShareBar.shareNewsClickListener() {
                                        @Override
                                        public void onClickFacebook() {
                                            GlobalIntent.shareFacebook(NewsDetailActivity.this, article.getTitle() + "\n" + ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + article.getUrl());
                                        }

                                        @Override
                                        public void onClickTwitter() {
                                            GlobalIntent.shareTwitter(NewsDetailActivity.this, article.getTitle() + "\n" + ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + article.getUrl());
                                        }

                                        @Override
                                        public void onClickWhatsApp() {
                                            GlobalIntent.shareWhatApp(NewsDetailActivity.this, article.getTitle() + "\n" + ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + article.getUrl());
                                        }

                                        @Override
                                        public void onClickGeneral() {
                                            GlobalIntent.shareGeneral(NewsDetailActivity.this, ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + article.getUrl(), article.getTitle());
                                        }

                                        @Override
                                        public void onClickTextChanger() {

                                        }
                                    });
                                }
                                if (article.getSecondaryImage() != null && !TextUtils.isEmpty(article.getSecondaryImage())) {
                                    Glide.with(getApplicationContext()).asBitmap().load(article.getSecondaryImage()).listener(
                                            new RequestListener<Bitmap>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {

                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                                    runOnUiThread(() -> {
                                                        try {
                                                            int width = bitmap.getWidth();
                                                            int height = bitmap.getHeight();
                                                            mImg_newsTop.getLayoutParams().height = Preferences.getInt(NewsDetailActivity.this, SettingsTypes.SCREEN_WIDTH.getType(), -1) * height / width;
                                                        } catch (Exception r) {

                                                        }
                                                        mImg_newsTop.setImageBitmap(bitmap);
                                                    });
                                                    return false;
                                                }
                                            }
                                    ).submit();

                                    mImg_newsTop.setOnClickListener(v -> GlobalIntent.openImage(NewsDetailActivity.this, article.getSecondaryImage()));
                                } else {
                                    mImg_newsTop.setImageResource(ErrorImageType.getIcon(getApplicationContext().getApplicationInfo().packageName));
                                }

                                mShareBar.setVisibility(View.VISIBLE);
                                View nonVideoLayout = findViewById(R.id.nonVideoLayout);
                                ViewGroup videoLayout = findViewById(R.id.videoLayout);
                                webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, mWeb_details);
                                mWeb_details.setWebChromeClient(webChromeClient);
                                mWeb_details.loadNews();

                                SpecialWebListener specialWebListener = new SpecialWebListener(NewsDetailActivity.this, null, NewsDetailActivity.this);
                                mWeb_details.setOnSelectedURL(specialWebListener);

                                loadStatus.loadStatusSuccess();

                                /** SEND ANALYTICS */
                                if (article.getTitle() != null && article.getUrl() != null && !TextUtils.isEmpty(article.getTitle()) && !TextUtils.isEmpty(article.getUrl()))
                                    new TurkuvazAnalytic(NewsDetailActivity.this).setIsPageView(true).setLoggable(true)
                                            .addParameter(AnalyticsEvents.TITLE.getEventName(), article.getTitle())
                                            .addParameter(AnalyticsEvents.URL.getEventName(), article.getUrl())
                                            .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName()).sendEvent();

                                /** SET ADS */
                                String adsZone = GlobalMethods.getCategoryZone(NewsDetailActivity.this, article.getCategoryId(), AdsType.ADS_300x250);
                                BannerAds bannerAds = new BannerAds(NewsDetailActivity.this, new AdSize(300, 250), adsZone);
                                ((FrameLayout) findViewById(R.id.frame_newsBottomBanner)).addView(bannerAds);

                                String adsTopZone = GlobalMethods.getCategoryZone(NewsDetailActivity.this, article.getCategoryId(), AdsType.ADS_320x142);//zoom toplantısındaki istek üzerine mastedhead reklam eklendi...
                                BannerAds bannerAdsTop = new BannerAds(NewsDetailActivity.this, new AdSize(320, 142), adsTopZone);
                                ((FrameLayout) findViewById(R.id.frame_newsTopBanner)).addView(bannerAdsTop);

                                String adsSticky = GlobalMethods.getCategoryZone(NewsDetailActivity.this, article.getCategoryId(), AdsType.ADS_320x50);
                                try {
                                    if (Preferences.getBoolean(NewsDetailActivity.this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(NewsDetailActivity.this).equals("")) {
                                        BannerAds bannerSticky = new BannerAds(NewsDetailActivity.this, new AdSize(320, 50), adsSticky);
                                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                        bannerSticky.setLayoutParams(layoutParams);
                                        runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLaySingleMode)).addView(bannerSticky));
                                    }

                                    if (Preferences.getBoolean(NewsDetailActivity.this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(NewsDetailActivity.this).equals(""))
                                        ((FrameLayout) findViewById(R.id.frame_newsBottomBanner)).setPadding(((FrameLayout) findViewById(R.id.frame_newsBottomBanner)).getPaddingLeft(), ((FrameLayout) findViewById(R.id.frame_newsBottomBanner)).getPaddingTop(), ((FrameLayout) findViewById(R.id.frame_newsBottomBanner)).getPaddingRight(), (int) (65 * getResources().getDisplayMetrics().density));
                                } catch (Exception e) {
                                    e.printStackTrace();
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
                        if (loadStatus != null)
                            loadStatus.loadStatusError("");
                        if (e != null)
                            GlobalMethods.onReportErrorToFirebase(NewsDetailActivity.this, e, "NewsDetailActivity-News");

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    String convertDate(String irregularDate) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM, yyyy HH:mm", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }

    @Override
    public void onBackPressed() {
        if (mWeb_details != null && webChromeClient != null && mWeb_details.isVideoFullscreen()) {
            webChromeClient.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void clickToolbarLogo(View view) {
        GlobalIntent.openForceHome(this, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onPopupStart(String videoUrl) {

        try {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }

            TypedValue tv = new TypedValue();
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            if (resourceId > 0) {
                actionBarHeight += getResources().getDimensionPixelSize(resourceId);
            }

            if (GlobalMethods.isActiveNativePlayer()) {
                LayoutInflater layoutInflater = LayoutInflater.from(NewsDetailActivity.this);
                popupView = layoutInflater.inflate(R.layout.popup_nativ_player, null);
                popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                NativeMediaPlayer nativeMediaPlayer = popupView.findViewById(R.id.nativeMediaPlayerPopup);
                popupView.findViewById(R.id.btn_sparkPopupClose).setOnClickListener(view -> popupWindow.dismiss());
                nativeMediaPlayer.setActivity(NewsDetailActivity.this);

                nativeMediaPlayer.setVideoURL(videoUrl);
                nativeMediaPlayer.init();

                nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                    try {
                        if (isFullScreen) {
                            NewsDetailActivity.this.getSupportActionBar().hide();
                            popupView.findViewById(R.id.frameToolbar).setVisibility(View.GONE);
                            popupView.setBackgroundColor(Color.WHITE);
                        } else {
                            NewsDetailActivity.this.getSupportActionBar().show();
                            popupView.findViewById(R.id.frameToolbar).setVisibility(View.VISIBLE);
                            popupView.setBackgroundColor(Color.TRANSPARENT);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                popupView.setOnTouchListener((view, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            pressStartTime = System.currentTimeMillis();
                            pressedX = (int) event.getX();
                            pressedY = (int) event.getY();
                            break;
                        }

                        case MotionEvent.ACTION_UP: {
                            long pressDuration = System.currentTimeMillis() - pressStartTime;
                            if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(NewsDetailActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
                                // Click Action
                            }
                            break;
                        }

                        case MotionEvent.ACTION_MOVE:
                            offsetX = (int) event.getRawX() - pressedX;
                            offsetY = (int) event.getRawY() - pressedY;
                            if (offsetY > actionBarHeight)
                                popupWindow.update(offsetX, offsetY, -1, -1, true);
                            break;
                    }
                    return true;
                });
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(NewsDetailActivity.this);
                popupView = layoutInflater.inflate(R.layout.popup, null);
                popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                sparkPlayerViewPopup = popupView.findViewById(R.id.sparkplayer_popup);
                sparkPlayerViewPopup.setVideoURL(videoUrl);
                sparkPlayerViewPopup.setOnClosePopupListener(() -> popupWindow.dismiss());
                sparkPlayerViewPopup.init();

                ((ViewGroup) sparkPlayerViewPopup.getChildAt(0)).getChildAt(0).setOnTouchListener(this);
                popupWindow.setOnDismissListener(() -> {
                    sparkPlayerViewPopup.closeSpark();
                    popupWindow = null;
                    popupView = null;
                    sparkPlayerViewPopup = null;
                });
            }

            if (popupWindow != null)
                popupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, actionBarHeight);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                pressStartTime = System.currentTimeMillis();
                pressedX = (int) event.getX();
                pressedY = (int) event.getY();
                break;
            }

            case MotionEvent.ACTION_UP: {
                long pressDuration = System.currentTimeMillis() - pressStartTime;
                if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(NewsDetailActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
                    // Click Action
                }
                break;
            }

            case MotionEvent.ACTION_MOVE:
                offsetX = (int) event.getRawX() - pressedX;
                offsetY = (int) event.getRawY() - pressedY;
                if (offsetY > actionBarHeight)
                    popupWindow.update(offsetX, offsetY, -1, -1, true);
                break;
        }
        return true;
    }


    @Override
    protected void onPause() {
        if (popupView != null) {
            if (sparkPlayerViewPopup != null)
                sparkPlayerViewPopup.closeSpark();
            popupWindow.dismiss();
            popupWindow = null;
            popupView = null;
            sparkPlayerViewPopup = null;
        }
        super.onPause();
    }
}