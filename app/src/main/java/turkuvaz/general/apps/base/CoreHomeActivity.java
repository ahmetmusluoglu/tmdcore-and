package turkuvaz.general.apps.base;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;

import com.google.android.gms.ads.AdSize;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import turkuvaz.general.apps.R;
import turkuvaz.general.apps.utils.Utils;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.AuthorsSnap.Adapter.AuthorsSnapAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider.Adapter.ColumnistRecyclerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider.ColumnistSlider;
import turkuvaz.general.turkuvazgeneralwidgets.ColumnistSliderSingle.SingleColumnistSlider;
import turkuvaz.general.turkuvazgeneralwidgets.ColumnistSliderSingle.SingleColumnistSliderAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.DoubleTab.DoubleTab;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider.HeadLineAdapter.HeadlinePagerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider.HeadLineSlider;
import turkuvaz.general.turkuvazgeneralwidgets.InformationBar.InformationBar;
import turkuvaz.general.turkuvazgeneralwidgets.LastMinute.LastMinute;
import turkuvaz.general.turkuvazgeneralwidgets.LastMinute.LastMinuteAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.NewsList.Adapter.NewsListAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.NewsList.NewsList;
import turkuvaz.general.turkuvazgeneralwidgets.NewsSnap.Adapter.NewsSnapAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.NewsSnap.NewsSnap;
import turkuvaz.general.turkuvazgeneralwidgets.OnlyVerticalSwipeRefreshLayout;
import turkuvaz.general.turkuvazgeneralwidgets.SpecialContent.SpecialContent;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.StatusBar;
import turkuvaz.general.turkuvazgeneralwidgets.TvStream.TvStreamSlider;
import turkuvaz.sdk.galleryslider.ListGallery.VideoGalleryListAdapter;
import turkuvaz.sdk.galleryslider.SliderGallery.MansetView.VideoGalleryPagerAdapter;
import turkuvaz.sdk.galleryslider.SliderGallery.VideoGallerySlider;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.ConfigBase.ComponentModel;
import turkuvaz.sdk.models.Models.ConfigBase.Settings;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.Enums.NewsListType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazfotogallerymodel.ListGallery.PhotoGalleryListAdapter;
import turkuvaz.sdk.turquazfotogallerymodel.SliderGallery.FotoGallerySlider;
import turkuvaz.sdk.turquazfotogallerymodel.SliderGallery.PhotoGalleryView.FotoGalleryPagerAdapter;

import static turkuvaz.sdk.models.Models.Enums.ClickViewType.COLUMNIST_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ClickViewType.PHOTO_GALLERY_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ClickViewType.PHOTO_WITH_DESCRIPTION;
import static turkuvaz.sdk.models.Models.Enums.ClickViewType.VIDEO_GALLERY_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ClickViewType.VIDEO_WITH_DESCRIPTION;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.INTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.VIDEO;

/**
 * Created by Ahmet MUŞLUOĞLU on 1/23/2020.
 */
public class CoreHomeActivity extends CoreBaseActivity implements
        NewsListAdapter.onSelectedNewsListener, VideoGalleryPagerAdapter.onSelectedNewsListener,
        ColumnistRecyclerAdapter.onSelectedNewsListener, NewsSnapAdapter.onSelectedNewsListener,
        HeadlinePagerAdapter.onSelectedNewsListener, FotoGalleryPagerAdapter.onSelectedNewsListener,
        LastMinuteAdapter.onSelectedNewsListener, AuthorsSnapAdapter.onSelectedNewsListener,
        PhotoGalleryListAdapter.onSelectedNewsListener, VideoGalleryListAdapter.onSelectedNewsListener,
        SingleColumnistSliderAdapter.onSelectedNewsListener {

    private OnlyVerticalSwipeRefreshLayout swipeContainer;

    private LinearLayout llHomeAllContent;
    private NestedScrollView nestedScrollView;
    private RelativeLayout relLayHomePage;
    private ProgressBar prgLoading;
    private BannerAds stickyads;
    private ImageButton btnScrollToTop;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configurationViews();
        controlDeepLink();
        startHomeActivity(true);

        GlobalMethods.setInitialInterstitialAd(getApplicationContext());
    }

    private void configurationViews() {
        nestedScrollView = findViewById(R.id.nested_homeFragment);
        relLayHomePage = findViewById(R.id.relLayHomePage);
        llHomeAllContent = findViewById(R.id.llHomeAllContent);
        prgLoading = findViewById(R.id.prgLoading);

        btnScrollToTop = findViewById(R.id.btnScrollToTop);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setProgressViewOffset(false, 0, Utils.dpToPx(65)); // refresh butonu topbarin uzerinde gorunuyordu, biraz asagiya kaydirildi

        swipeContainer.setEnabled(false);
        swipeContainer.setOnRefreshListener(() -> {
            llHomeAllContent.removeAllViews();
            swipeContainer.setRefreshing(true);
            startHomeActivity(true);
        });

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = nestedScrollView.getScrollY();
            int screenHeight = GlobalMethods.getScreenHeight();
            btnScrollToTop.setVisibility((scrollY > screenHeight && navigationView.getSelectedItemId() == R.id.navigation_home) ? View.VISIBLE : View.GONE);
            btnScrollToTop.bringToFront();
        });
    }

    protected void startHomeActivity(boolean sendHomeAnalytics) {
            changeContent(0);
            checkRateApp(); // CHECK RATE APP

        if (sendHomeAnalytics)
            new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.HOME_PAGE.getEventName()).sendEvent();

        try {
            /** String Olarak sharedPreferences'a kaydedilmiş datatyı ArrayList formatına dönüştürüp kullanacağız*/
            String homeComponentJson = Preferences.getString(this, ApiListEnums.HOME_SCREEN_COMPONENT_JSON.getType(), "");
            ArrayList<ComponentModel> homeScreenComponents = fromJson(homeComponentJson, new TypeToken<ArrayList<ComponentModel>>() {
            }.getType());

            for (int i = 0; i < homeScreenComponents.size(); i++) {
                if (!homeScreenComponents.get(i).getActive()) continue;

                View currentView = null;
                switch (homeScreenComponents.get(i).getComponentType()) {
                    case COM_BANNER_ADS:
                        BannerAds bannerAds = null;
                        if (homeScreenComponents.get(i).getSize().equals("320x142"))
                            bannerAds = new BannerAds(this, new AdSize(320, 142), ApiListEnums.ADS_HOMEPAGE_320x142.getApi(this));
                        else if (homeScreenComponents.get(i).getSize().equals("300x250"))
                            bannerAds = new BannerAds(this, new AdSize(300, 250), ApiListEnums.ADS_HOMEPAGE_300x250.getApi(this));
                        else if (homeScreenComponents.get(i).getSize().equals("320x50"))
                            bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_HOMEPAGE_320x50.getApi(this));
                        currentView = bannerAds;
                        break;

                    case COM_HEADLINE:
                        HeadLineSlider headLineSlider = new HeadLineSlider(CoreHomeActivity.this);
                        headLineSlider.setApiPath(homeScreenComponents.get(i).getApiPath());
                        headLineSlider.setOnSelectedNewsListener(this);
                        headLineSlider.setAutoSlide(homeScreenComponents.get(i).getAutoSlide() != null ? homeScreenComponents.get(i).getAutoSlide() : false);
                        headLineSlider.setTitleVisible(homeScreenComponents.get(i).getTitleShow() != null ? homeScreenComponents.get(i).getTitleShow() : false);
                        headLineSlider.setCustomPageName(homeScreenComponents.get(i).getCustomPageName() != null ? homeScreenComponents.get(i).getCustomPageName() : "");
                        headLineSlider.setHomepage(true);
                        headLineSlider.setSurmanset(homeScreenComponents.get(i).getSurmanset() != null ? homeScreenComponents.get(i).getSurmanset() : false);
                        headLineSlider.init();
                        currentView = headLineSlider;
                        break;

                    case COM_SNAP_BAR:
                        NewsSnap newsSnap = new NewsSnap(CoreHomeActivity.this);
                        newsSnap.setApiUrl(homeScreenComponents.get(i).getApiPath());
                        newsSnap.setOnSelectedNewsListener(this);
                        newsSnap.setSingle(homeScreenComponents.get(i).getSingleCell() != null && homeScreenComponents.get(i).getSingleCell());
                        newsSnap.setTitleVisible(homeScreenComponents.get(i).getTitleShow() != null ? homeScreenComponents.get(i).getTitleShow() : false);
                        newsSnap.setTextSize(homeScreenComponents.get(i).getTextSize() != null ? Integer.valueOf(homeScreenComponents.get(i).getTextSize()) : 0);
                        newsSnap.setTitleOverImage(homeScreenComponents.get(i).getTitleOverImage() != null ? homeScreenComponents.get(i).getTitleOverImage() : true);
                        newsSnap.init();
                        currentView = newsSnap;
                        break;

                    case COM_BTN_NEWS_LIVESTREAM:
                        DoubleTab doubleTab = new DoubleTab(CoreHomeActivity.this);
                        doubleTab.setLiveStreamUrl(homeScreenComponents.get(i).getApiPath());
                        doubleTab.init();
                        currentView = doubleTab;
                        break;

                    case COM_GRID_NEWS:
                        NewsList newsList = new NewsList(CoreHomeActivity.this);
                        newsList.setApiPath(homeScreenComponents.get(i).getApiPath());
                        newsList.setOnSelectedNewsListener(this);
                        newsList.setNewsListType(homeScreenComponents.get(i).getNewsListType());
                        newsList.setHomepage(true);
                        if (homeScreenComponents.get(i).getCategory() != null)
                            newsList.setCategoryShow(homeScreenComponents.get(i).getCategory());
                        newsList.setTitleOverImage(homeScreenComponents.get(i).getTitleOverImage() != null ? homeScreenComponents.get(i).getTitleOverImage() : false);
                        newsList.setShowTitle(homeScreenComponents.get(i).getTitleShow() != null ? homeScreenComponents.get(i).getTitleShow() : true);
                        newsList.setSurmanset(homeScreenComponents.get(i).getSurmanset() != null ? homeScreenComponents.get(i).getSurmanset() : false);
                        newsList.setOnLoadMoreMode(false);
                        newsList.setOnStatusListener(new NewsList.onStatusListener() {
                            @Override
                            public void onStartLoad() {
                                prgLoading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onSuccess() {
                                prgLoading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });

                        newsList.init((homeScreenComponents.size() - 1) == i);

                        if (nestedScrollView != null) {
                            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                                        if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                                            newsList.onLoadMoreManually();
                                        }
                                    });
                        }

                        currentView = newsList;
                        break;

                    case COM_ADVERTORIAL:
                        NewsList advertorial = new NewsList(CoreHomeActivity.this);
                        advertorial.setApiPath(homeScreenComponents.get(i).getApiPath());
                        advertorial.setCategoryShow(homeScreenComponents.get(i).getCategory() != null && homeScreenComponents.get(i).getCategory());
                        advertorial.setOnSelectedNewsListener(this);
                        advertorial.setNewsListType(homeScreenComponents.get(i).getNewsListType());
                        advertorial.setOnLoadMoreMode(false);
                        advertorial.setAdv(true);
                        advertorial.setOnStatusListener(new NewsList.onStatusListener() {
                            @Override
                            public void onStartLoad() {
                                prgLoading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onSuccess() {
                                prgLoading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });

                        advertorial.setOnLoadMoreMode(false);
                        advertorial.init((homeScreenComponents.size() - 1) == i);

                        if (nestedScrollView != null) {
                            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                                        if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                                            advertorial.onLoadMoreManually();
                                        }
                                    });
                        }

                        currentView = advertorial;
                        break;

                    case COM_LAST_MINUTE:
                        LastMinute lastMinute = new LastMinute(CoreHomeActivity.this);
                        lastMinute.setApiPath(homeScreenComponents.get(i).getApiPath());
                        lastMinute.setOnSelectedNewsListener(this);
                        lastMinute.init();
                        currentView = lastMinute;
                        break;

                    case COM_VIDEO_SLIDER:
                        VideoGallerySlider videoSlider = new VideoGallerySlider(CoreHomeActivity.this);
                        videoSlider.setApiPath(homeScreenComponents.get(i).getApiPath());
                        videoSlider.setSliderSize(229, 440);
                        videoSlider.setTitleVisible(homeScreenComponents.get(i).getTitleShow() != null ? homeScreenComponents.get(i).getTitleShow() : true);
                        videoSlider.setCustomPageName(homeScreenComponents.get(i).getCustomPageName() != null ? homeScreenComponents.get(i).getCustomPageName() : "");
                        videoSlider.setAutoSlide(homeScreenComponents.get(i).getAutoSlide() != null ? homeScreenComponents.get(i).getAutoSlide() : false);
                        videoSlider.setOnSelectedNewsListener(this);
                        videoSlider.init();
                        currentView = videoSlider;
                        break;

                    case COM_GALLERY_SLIDER:
                        FotoGallerySlider gallerySlider = new FotoGallerySlider(CoreHomeActivity.this);
                        gallerySlider.setApiPath(homeScreenComponents.get(i).getApiPath());
                        gallerySlider.setAutoSlide(homeScreenComponents.get(i).getAutoSlide() != null ? homeScreenComponents.get(i).getAutoSlide() : false);
                        gallerySlider.setOnSelectedNewsListener(this);
                        gallerySlider.init();
                        currentView = gallerySlider;
                        break;

                    case COM_INFORMATION_BAR:
                        if (homeScreenComponents.get(i).getView() != null && homeScreenComponents.get(i).getView().equals(NewsListType.INFO_1x1_BOX.getType())) {
                            boolean isDark = homeScreenComponents.get(i).isDarkMode() != null && homeScreenComponents.get(i).isDarkMode();
                            currentView = new InformationBar(CoreHomeActivity.this, isDark, homeScreenComponents.get(i).getSubActions());
                        } else {
                            String list = homeScreenComponents.get(i).getList() != null ? homeScreenComponents.get(i).getList() : "";
                            int selectedIndex = homeScreenComponents.get(i).getSelectedIndex() >= 0 ? homeScreenComponents.get(i).getSelectedIndex() : -1;
                            currentView = new StatusBar(CoreHomeActivity.this, "v1/link/184", "v1/link/185", list, selectedIndex, homeScreenComponents.get(i).getSubActions());
                        }
                        break;
                    case COM_COLUMNIST_SLIDER:
                        boolean isSlider = homeScreenComponents.get(i).isSlider() != null && homeScreenComponents.get(i).isSlider();
                        Preferences.save(getApplicationContext(), ApiListEnums.IS_SLIDER.getType(), isSlider);
                        if (homeScreenComponents.get(i).getSingleType() != null && homeScreenComponents.get(i).getSingleType()) {
                            SingleColumnistSlider singleColumnistSlider = new SingleColumnistSlider(CoreHomeActivity.this);
                            singleColumnistSlider.setApiPath(homeScreenComponents.get(i).getApiPath());
                            singleColumnistSlider.setOnSelectedNewsListener(this);
                            singleColumnistSlider.init();
                            currentView = singleColumnistSlider;
                        } else {
                            ColumnistSlider columnistSlider = new ColumnistSlider(CoreHomeActivity.this);
                            columnistSlider.setDateShow(homeScreenComponents.get(i).getDateShow() != null ? homeScreenComponents.get(i).getDateShow() : true);
                            columnistSlider.setApiPath(homeScreenComponents.get(i).getApiPath());
                            columnistSlider.setRowCount(homeScreenComponents.get(i).getNumberOfLine() > 0 ? homeScreenComponents.get(i).getNumberOfLine() : 2);
                            columnistSlider.setOnSelectedNewsListener(this);
                            columnistSlider.init();
                            currentView = columnistSlider;
                        }
                        break;
                    case COM_SPECIAL_CONTENT:
                        SpecialContent specialContent = new SpecialContent(CoreHomeActivity.this);
                        specialContent.setData(homeScreenComponents.get(i).getApiPath());
                        specialContent.init();
                        currentView = specialContent;
                        break;
                    case COM_INTERNAL_BROWSER: // Küçük ilanlar app'i için yapıldı. sadece 1 webview'dan oluşuyor. next-back butonları olacağı için special yapılmadı
                        finish();
                        GlobalIntent.openInternalBrowser(CoreHomeActivity.this, homeScreenComponents.get(i).getApiPath());
                        break;
                    case COM_TV_STREAM:
                        TvStreamSlider tvStreamSlider = new TvStreamSlider(CoreHomeActivity.this);
                        tvStreamSlider.setApiPath(homeScreenComponents.get(i).getApiPath());
                        tvStreamSlider.init();
                        currentView = tvStreamSlider;
                        break;
                    case COM_UNKNOWN:
                        Log.e("COMPONENT", "COM_UNKNOWN");
                        break;
                }

                if (currentView != null) {
                    View finalCurrentView = currentView;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(() -> llHomeAllContent.addView(finalCurrentView));
                        }
                    }, 400);
                }
            }

            try { // bottom bar varsa sticky gösterilmeyecek. sadece anasayfada sticky olmayacak bu durumda. is_active açıksa diğer ekranlarda gösterim devam eder
                if (!CoreApp.getSettings().isShowBottomNavigationView() && Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                    if (stickyads != null) // sayfayı swipe edince üst üste stickyleri ekliyordu, önce çıkarıldı sonra tekrar eklendi
                        runOnUiThread(() -> relLayHomePage.removeView(stickyads));
                    stickyads = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                    stickyads.setPaddingView(btnScrollToTop); // hata olduğunda padding işlemleri yapabilmek için verildi. Reklam gelmediğinde scroll butonu yukarıda kalıyordu. Bannerads içindeki listener'larda düştüğünde kontrol edilmesi sağlandı
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ABOVE, R.id.rlBottomNavigationView);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    stickyads.setLayoutParams(layoutParams);
                    runOnUiThread(() -> relLayHomePage.addView(stickyads));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Log.i("TAG", "onFinishOOO: ");
                    if (navigationView.getSelectedItemId() == R.id.navigation_home) {
                        swipeContainer.setEnabled(true); // yazarlar ekranında da kaydırma yapılabiliyordu engellendi
                    }
                    swipeContainer.setRefreshing(false);
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void controlDeepLink() {

    }

    private void checkRateApp() {

    }

    @Override
    public void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType) {
        CoreApp.getInstance().getPageOverlayAds().show();

        if (clickViewType == PHOTO_GALLERY_SLIDER || clickViewType == PHOTO_WITH_DESCRIPTION) {
            String appSettingsJson = Preferences.getString(getApplicationContext(), ApiListEnums.CATEGORY_SETTINGS.getType(), "");
            Settings appSettings = new Gson().fromJson(appSettingsJson, Settings.class);
            boolean isSliderActive = appSettings != null && appSettings.getOthers().getGallerySlider() != null && appSettings.getOthers().getGallerySlider().isActive();
            if (isSliderActive && navigationView.getSelectedItemId() == R.id.navigation_photo_gallery) {
                String currId = selectedNews.get(position).getId();
                ArrayList<String> newsIds = new ArrayList<>();
                for (int i = 0; i < selectedNews.size(); i++) {
                    if (selectedNews.get(i) != null) {
                        String s = selectedNews.get(i).getId();
                        newsIds.add(s);
                    }
                }
                int curPos = 0;
                for (String id : newsIds) {
                    if (id.equals(currId))
                        break;
                    curPos++;
                }
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), newsIds, ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                GlobalIntent.openInfinityGalleryWithSlider(CoreHomeActivity.this, showGalleryModel, ALBUM, curPos, selectedNews);
            } else {
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), selectedNews.get(position).getId(), ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(CoreHomeActivity.this, showGalleryModel, ALBUM);
            }
            return;
        }

        if (clickViewType == COLUMNIST_SLIDER) {
//            GlobalIntent.openColumnistWithArticle(CoreHomeActivity.this, selectedNews.get(position), true);
            GlobalIntent.openAllColumnistWithArticle(CoreHomeActivity.this, selectedNews, position, true);// for all columnists slider...
//            EventBus.getDefault().postSticky(new AllColumnists(selectedNews,true,position));
            return;
        }

        if (clickViewType == VIDEO_GALLERY_SLIDER) {
            if (selectedNews.get(position).getVideoSmilUrl() != null || selectedNews.get(position).getVideoUrl() != null || selectedNews.get(position).getVideoMobileUrl() != null) {
                GlobalIntent.openVideoDetailPage(CoreHomeActivity.this, selectedNews, position, "AD_TAG");
            }
            return;
        }
        if (clickViewType == VIDEO_WITH_DESCRIPTION) {
            GlobalIntent.openVideoDetailPage(CoreHomeActivity.this, selectedNews, position, "testAds");
            return;
        }

        String selectedNewsExternal = selectedNews.get(position).getExternal();
        ExternalTypes selectedNewsExternalType = selectedNews.get(position).getExternalType();


        switch (selectedNewsExternalType) {
            case EMPTY:
                GlobalIntent.openNewsDetailsList(CoreHomeActivity.this, selectedNews, position);
                break;
            case ALBUM:
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), selectedNewsExternal.replace(ALBUM.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(CoreHomeActivity.this, showGalleryModel, ALBUM);
                break;
            case NEWS:
                GlobalIntent.openNewsWithID(CoreHomeActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(this), selectedNewsExternal.replace(NEWS.getType(), ""));
                break;
            case VIDEO:
                GlobalIntent.openVideoDetailsWithID(CoreHomeActivity.this, selectedNewsExternal.replace(VIDEO.getType(), ""), "AD_TAG");
                break;
            case EXTERNAL:
                GlobalIntent.openExternalBrowser(CoreHomeActivity.this, selectedNewsExternal.replace(EXTERNAL.getType(), ""));
                break;
            case INTERNAL:
                GlobalIntent.openInternalBrowser(CoreHomeActivity.this, selectedNewsExternal.replace(INTERNAL.getType(), ""));
                break;
            case PHOTO_NEWS:
                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), selectedNewsExternal.replace(PHOTO_NEWS.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                GlobalIntent.openInfinityGalleryFotoHaber(CoreHomeActivity.this,
                        showGalleryModel2,
                        selectedNews.get(position).getTitle(),
                        selectedNews.get(position).getSpot(),
                        selectedNews.get(position).getUrl());
                break;
            case LIVE_STREAM:
                GlobalIntent.openLiveStreamWithURL(CoreHomeActivity.this, "Canlı Yayın", selectedNewsExternal.replace(LIVE_STREAM.getType(), ""), ApiListEnums.ADS_PREROLL.getApi(CoreHomeActivity.this));
                break;
            case ARTICLE_SOURCE:
                GlobalIntent.openColumnistWithArticle(CoreHomeActivity.this, selectedNews.get(position), true);
//                GlobalIntent.openAllColumnistWithArticle(CoreHomeActivity.this, selectedNews, position, true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private ArrayList<ComponentModel> fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    public void openLiveStream(View view) {
    }

    private void changeContent(int key) {

    }

    public void scrollToTop(View v) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nestedScrollView.smoothScrollTo(0, 0);
                timer.cancel();
            }
        }, 0, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
