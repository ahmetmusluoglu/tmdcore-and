package turkuvaz.general.turkuvazgeneralactivitys.InfinityGalleryWithSlider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.GalleryAdapter;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.InfinityActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.PushDialog.PushDialog;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.EventBus.PushDialogEvent;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.ConfigBase.LeftMenu;
import turkuvaz.sdk.models.Models.ConfigBase.Settings;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 6/1/2020.
 */
public class GalleryActivityWithSlider extends Activity {

    GalleryPagerAdapter galleryPagerAdapter;
    ShowGalleryModel mShowGalleryModel;
    private RelativeLayout rlNext;
    private RelativeLayout rlPrev;
    ArrayList<Article> allNews;
    public static int activePosition;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_with_slider);

        try {
            mShowGalleryModel = getIntent().getParcelableExtra("showAllGalleryModels");
            int currPosition = getIntent().getExtras().getInt("currPosition", 0);

            Gson gson = new Gson();
            final ArrayList<Article> allNews = gson.fromJson(getIntent().getExtras().getString("allNews"), new TypeToken<ArrayList<Article>>() {}.getType());

            setToolbar();

            // Setup ViewPager with indicator
            ViewPager pager = findViewById(R.id.pager);
            rlNext = findViewById(R.id.rlNext);
            rlPrev = findViewById(R.id.rlPrev);

            if (allNews != null && allNews.size() > 0) {
                new TurkuvazAnalytic(GalleryActivityWithSlider.this).setIsPageView(true).setLoggable(true)
                        .setEventName(AnalyticsEvents.GALLERY_DETAILS.getEventName())
                        .addParameter(AnalyticsEvents.TITLE.getEventName(), allNews.get(currPosition).getTitle())
                        .addParameter(AnalyticsEvents.URL.getEventName(), allNews.get(currPosition).getUrl())
                        .sendEvent();
            }
            GalleryActivityWithSlider.activePosition = currPosition;
            InfinityActivity.mPageIndex = 1;
            galleryPagerAdapter = new GalleryPagerAdapter(GalleryActivityWithSlider.this, mShowGalleryModel);
            pager.setAdapter(galleryPagerAdapter);
            pager.setCurrentItem(currPosition);

            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    new TurkuvazAnalytic(GalleryActivityWithSlider.this).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.GALLERY_DETAILS.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), allNews.get(position).getTitle())
                            .addParameter(AnalyticsEvents.URL.getEventName(), allNews.get(position).getUrl())
                            .sendEvent();
                    GalleryActivityWithSlider.activePosition = position;
                    InfinityActivity.mPageIndex = 1;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            ScrollingPagerIndicator pagerIndicator = findViewById(R.id.pager_indicator);
            pagerIndicator.attachToPager(pager);

            String appSettingsJson = Preferences.getString(getApplicationContext(), ApiListEnums.CATEGORY_SETTINGS.getType(), "");
            Settings appSettings = new Gson().fromJson(appSettingsJson, Settings.class);
            if (appSettings != null) {
                int maxDots = appSettings.getOthers().getGallerySlider() != null ? appSettings.getOthers().getGallerySlider().getMaxDots() : 5;
                if (maxDots % 2 == 0) {
                    maxDots += 1;
                }
                pagerIndicator.setVisibleDotCount(maxDots);//tek olması lazım...

                if (appSettings.getOthers().getGallerySlider() != null && appSettings.getOthers().getGallerySlider().isShowArrows()) {//sağ-sol oklar gösterilsin/gizlensin
                    arrowVisibility(currPosition);
                    rlPrev.setOnClickListener(view1 -> pager.setCurrentItem(pager.getCurrentItem() - 1, true));
                    rlNext.setOnClickListener(view1 -> pager.setCurrentItem(pager.getCurrentItem() + 1, true));

                    pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            arrowVisibility(position);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void arrowVisibility(int position) {
        rlNext.setVisibility(position == galleryPagerAdapter.getCount() - 1 ? View.GONE : View.VISIBLE);
        rlPrev.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }

    private void setToolbar() {
        ((ImageView) findViewById(R.id.img_infinityAppIcon)).setImageResource(mShowGalleryModel.getLogoID());
        ((FrameLayout) findViewById(R.id.frame_infinityToolbar)).setBackgroundColor(Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
        ((ImageView) findViewById(R.id.imageViewGalleryBack)).setImageResource(R.drawable.ic_arrow_back_24dp);
        ((ImageView) findViewById(R.id.imageViewGalleryHome)).setImageResource(R.drawable.ic_home_black_24dp);

        // back ve share iconları, activity içinde verilmiş. toolbardakiler kullanılmadığı için bunlara tekrar renk verildi
        DrawableCompat.setTint(((ImageView) findViewById(R.id.imageViewGalleryBack)).getDrawable(), Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
        DrawableCompat.setTint(((ImageView) findViewById(R.id.imageViewGalleryHome)).getDrawable(), Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));

        DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
        DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_home_black_24dp), Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));

        try {
            if (getActionBar() != null) {
                getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
                // back butonu rengi
                DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(GalleryActivityWithSlider.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
                getActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jumpToTop(View view) {
        GlobalIntent.openForceHome(this, false);
        finish();
    }

    public void clickToolbarLogo(View view) {
        GlobalIntent.openForceHome(this, true);
    }

    public void finishGallery(View view) {
        finish();
        overridePendingTransition(0, 0);
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
                    Utils.setOpenedPush(GalleryActivityWithSlider.this, pushModel.getPid());
                    /** NOTIFICATION CONTROL */
                    if (pushModel != null) {
                        switch (pushModel.getTypestr()) {
                            case NEWS:
                                GlobalIntent.openNewsWithID(GalleryActivityWithSlider.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivityWithSlider.this), pushModel.getRefid());
                                break;
                            case VIDEO:
                                GlobalIntent.openVideoDetailsWithID(GalleryActivityWithSlider.this, pushModel.getRefid(), "AD_TAG");
                                break;
                            case AUTHOR:
                                GlobalIntent.openColumnistWithID(GalleryActivityWithSlider.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryActivityWithSlider.this), pushModel.getRefid());
                                break;
                            case BROWSER:
                                GlobalIntent.openInternalBrowser(GalleryActivityWithSlider.this, pushModel.getU());
                                break;
                            case GALLERY:
                                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryActivityWithSlider.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryActivityWithSlider.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(GalleryActivityWithSlider.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(GalleryActivityWithSlider.this, showGalleryModel, ExternalTypes.ALBUM);
                                break;
                            case PHOTO_NEWS:
                                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryActivityWithSlider.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryActivityWithSlider.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(GalleryActivityWithSlider.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(GalleryActivityWithSlider.this, showGalleryModel2, ExternalTypes.PHOTO_NEWS);
                                break;
                            case LIVE_STREAM:
                                GlobalIntent.openLiveStreamWithURL(GalleryActivityWithSlider.this, "Canlı Yayın", pushModel.getExcurl(), ApiListEnums.ADS_PREROLL.getApi(GalleryActivityWithSlider.this));
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

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        try {
            if (galleryPagerAdapter != null) {
                if (galleryPagerAdapter.getPopupView() != null) {
                    if (galleryPagerAdapter.getSparkPlayerViewPopup() != null) {
                        galleryPagerAdapter.getSparkPlayerViewPopup().closeSpark();
                    }
                    if (galleryPagerAdapter != null && galleryPagerAdapter.getPopupWindow() != null) {
                        galleryPagerAdapter.getPopupWindow().dismiss();
                        galleryPagerAdapter.setPopupWindow();
                    }
                    assert galleryPagerAdapter != null;
                    galleryPagerAdapter.setPopupView();
                    galleryPagerAdapter.setSparkPlayerViewPopup();
                }
            }
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
