package turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.ads.AdSize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.PushDialog.PushDialog;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.EventBus.PushDialogEvent;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.AlbumMedia;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;

public class GalleryActivity extends AppCompatActivity implements View.OnTouchListener {

    InfinityActivity infinityActivity;
    ShowGalleryModel mShowGalleryModel;

    private static final int MAX_CLICK_DISTANCE = 15;
    private static final int MAX_CLICK_DURATION = 1000;

    private long pressStartTime;
    private int pressedX;
    private int pressedY;
    int offsetX, offsetY;
    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SparkPlayerViewPopup sparkPlayerViewPopup = null;
    int actionBarHeight = 0;
    private ExternalTypes externalTypes = ExternalTypes.ALBUM;

    private String title, spot, url = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            // toolbar text color
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(GalleryActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));
            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(GalleryActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(GalleryActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        ImageView mImg_toolbarIcon = findViewById(R.id.img_imageDetailListToolbar);
        mImg_toolbarIcon.setImageResource(IconTypes.getIcon(this.getApplicationInfo().packageName));

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("title"))
            title = getIntent().getExtras().getString("title", null);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("spot"))
            spot = getIntent().getExtras().getString("spot", null);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("url"))
            url = getIntent().getExtras().getString("url", null);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("externalType"))
            externalTypes = ExternalTypes.PHOTO_NEWS;

        try {
            if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                BannerAds bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bannerAds.setLayoutParams(layoutParams);
                runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLayGalleryDetail)).addView(bannerAds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mShowGalleryModel = getIntent().getParcelableExtra("showGalleryModel");

        ((ImageView) findViewById(R.id.img_infinityAppIcon)).setImageResource(mShowGalleryModel.getLogoID());

        InfinityActivity.mPageIndex = 1;

        infinityActivity = new InfinityActivity(this);
        infinityActivity.setShowGalleryModel(mShowGalleryModel);
        infinityActivity.setTitle(title);
        infinityActivity.setSpot(spot);
        infinityActivity.setUrl(url);

        infinityActivity.setOnSelectedURLListener(new InfinityActivity.OnSelectedURLListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onVideo(String VideoURL, String VideoCategory, String VideoTitle) {

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
                        LayoutInflater layoutInflater = LayoutInflater.from(GalleryActivity.this);
                        popupView = layoutInflater.inflate(R.layout.popup_nativ_player, null);
                        popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        NativeMediaPlayer nativeMediaPlayer = popupView.findViewById(R.id.nativeMediaPlayerPopup);
                        popupView.findViewById(R.id.btn_sparkPopupClose).setOnClickListener(view -> popupWindow.dismiss());
                        nativeMediaPlayer.setActivity(GalleryActivity.this);

                        nativeMediaPlayer.setVideoURL(VideoURL);
                        nativeMediaPlayer.init();

                        nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                            try {
                                if (isFullScreen) {

                                    GalleryActivity.this.getActionBar().hide();
                                    popupView.findViewById(R.id.frameToolbar).setVisibility(View.GONE);
                                    popupView.setBackgroundColor(Color.WHITE);
                                } else {
                                    GalleryActivity.this.getActionBar().show();
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
                                    if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(GalleryActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
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
                        LayoutInflater layoutInflater = LayoutInflater.from(GalleryActivity.this);
                        popupView = layoutInflater.inflate(R.layout.popup, null);
                        popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        sparkPlayerViewPopup = popupView.findViewById(R.id.sparkplayer_popup);
                        sparkPlayerViewPopup.setVideoURL(VideoURL);
                        sparkPlayerViewPopup.setOnClosePopupListener(() -> popupWindow.dismiss());
                        sparkPlayerViewPopup.init();

                        ((ViewGroup) sparkPlayerViewPopup.getChildAt(0)).getChildAt(0).setOnTouchListener(GalleryActivity.this);
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
            public void onNews(String NewsID) {
                GlobalIntent.openNewsWithID(GalleryActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivity.this), NewsID);
            }

            @Override
            public void onAlbum(String AlbumID) {
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryActivity.this), ApplicationsWebUrls.getDomain(GalleryActivity.this.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryActivity.this), AlbumID, ApiListEnums.DETAILS_VIDEO.getApi(GalleryActivity.this), IconTypes.getIcon(GalleryActivity.this.getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(GalleryActivity.this, showGalleryModel, ExternalTypes.ALBUM);
            }

            @Override
            public void onPhotoNews(String PhotoNewsID) {
                GlobalIntent.openNewsWithID(GalleryActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivity.this), PhotoNewsID);
            }

            @Override
            public void onArticle(String ArticleSourceID) {
                GlobalIntent.openColumnistWithID(GalleryActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivity.this), ArticleSourceID);
            }

            @Override
            public void onExternal(String ExternalURL) {
                GlobalIntent.openExternalBrowser(GalleryActivity.this, ExternalURL);
            }

            @Override
            public void onInternal(String InternalURL) {
                GlobalIntent.openInternalBrowser(GalleryActivity.this, InternalURL);
            }

            @Override
            public void onOpenGallery(String galleryTitle, String galleryURL) {

                if (externalTypes == ExternalTypes.PHOTO_NEWS) {
                    if (title == null || url == null) {

                        title = galleryTitle;
                        url = galleryURL;

                        /** SEND ANALYTICS */
                        new TurkuvazAnalytic(GalleryActivity.this).setIsPageView(true).setLoggable(true)
                                .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
                                .addParameter(AnalyticsEvents.TITLE.getEventName(), galleryTitle)
                                .addParameter(AnalyticsEvents.URL.getEventName(), galleryURL)
                                .sendEvent();
                    } else {
                        /** SEND ANALYTICS */
                        new TurkuvazAnalytic(GalleryActivity.this).setIsPageView(true).setLoggable(true)
                                .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
                                .addParameter(AnalyticsEvents.TITLE.getEventName(), title)
                                .addParameter(AnalyticsEvents.URL.getEventName(), url)
                                .sendEvent();
                    }
                } else {
                    /** SEND ANALYTICS */
                    new TurkuvazAnalytic(GalleryActivity.this).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.GALLERY_DETAILS.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), galleryTitle)
                            .addParameter(AnalyticsEvents.URL.getEventName(), galleryURL)
                            .sendEvent();
                }
            }

            @Override
            public void onCurrentIndex(AlbumMedia albumMedia) {
                if (externalTypes == ExternalTypes.PHOTO_NEWS) {
                    /** SEND ANALYTICS */
                    new TurkuvazAnalytic(GalleryActivity.this).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), title)
                            .addParameter(AnalyticsEvents.URL.getEventName(), url + "/" + albumMedia.getSortOrder())
                            .sendEvent();
                } else {
                    /** SEND ANALYTICS */
                    new TurkuvazAnalytic(GalleryActivity.this).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.GALLERY_INDEX_VIEW.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), String.valueOf(albumMedia.getSortOrder()))
                            .addParameter(AnalyticsEvents.SHORT_ORDER.getEventName(), String.valueOf(albumMedia.getSortOrder()))
                            .sendEvent();
                }

            }
        });
        infinityActivity.init();

        ((FrameLayout) findViewById(R.id.frame_GalleryList)).addView(infinityActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PushDialogEvent event) {
        try {
            PushDialog pushDialog = new PushDialog(this);
            pushDialog.setPushModel(event.getPushModel());
            pushDialog.setOnPushDialog(new PushDialog.onPushDialog() {
                @Override
                public void closeDialog() {

                }

                @Override
                public void openNews(PushModel pushModel) {
                    if (pushDialog != null && pushDialog.isShowing())
                        pushDialog.closeDialog();
                    Utils.setOpenedPush(GalleryActivity.this, pushModel.getPid());
                    /** NOTIFICATION CONTROL */
                    if (pushModel != null) {
                        switch (pushModel.getTypestr()) {
                            case NEWS:
                                GlobalIntent.openNewsWithID(GalleryActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivity.this), pushModel.getRefid());
                                break;
                            case VIDEO:
                                GlobalIntent.openVideoDetailsWithID(GalleryActivity.this, pushModel.getRefid(), "AD_TAG");
                                break;
                            case AUTHOR:
                                GlobalIntent.openColumnistWithID(GalleryActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivity.this), pushModel.getRefid());
                                break;
                            case BROWSER:
                                GlobalIntent.openInternalBrowser(GalleryActivity.this, pushModel.getU());
                                break;
                            case GALLERY:
                                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(GalleryActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(GalleryActivity.this, showGalleryModel, ExternalTypes.ALBUM);
                                break;
                            case PHOTO_NEWS:
                                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(GalleryActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(GalleryActivity.this, showGalleryModel2, ExternalTypes.PHOTO_NEWS);
                                break;
                            case LIVE_STREAM:
                                GlobalIntent.openLiveStreamWithURL(GalleryActivity.this, "Canlı Yayın", pushModel.getExcurl(), ApiListEnums.ADS_PREROLL.getApi(GalleryActivity.this));
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

    public void jumpToTop(View view) {
        GlobalIntent.openForceHome(this, true);
        finish();
    }

    public void shareGallery() {
        if (infinityActivity != null)
            infinityActivity.shareGallery();
    }

    public void finishGallery() {
        finish();
        overridePendingTransition(0, 0);
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
                if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(GalleryActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
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
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        if (popupView != null) {
            if (sparkPlayerViewPopup != null) {
                sparkPlayerViewPopup.closeSpark();
            }
            popupWindow.dismiss();
            popupWindow = null;
            popupView = null;
            sparkPlayerViewPopup = null;
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishGallery();
        } else if (item.getItemId() == R.id.action_share) {
            shareGallery();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        DrawableCompat.setTint(item.getIcon(), Color.parseColor(Preferences.getString(GalleryActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"))); // anasayfa iconu
        return true;
    }

    public void clickToolbarLogo(View view) {
        GlobalIntent.openForceHome(this, true);
    }
}
