package turkuvaz.general.turkuvazgeneralactivitys.NewsDetails.NewsListMode;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdSize;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.sdk.galleryslider.ListGallery.VideoListDetailsActivity;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.AdsZones.AdsDatum;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;

public class NewsDetailListActivity extends BaseGeneralActivity {

    private ArrayList<Article> articles = new ArrayList<>();
    private NewsListModeAdapter newsListModeAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase,""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list_mode);

        RecyclerView mRec_newsListMode = findViewById(R.id.rec_newsListMode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRec_newsListMode.setLayoutManager(mLayoutManager);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            // toolbar text color
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(NewsDetailListActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));
            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(NewsDetailListActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(NewsDetailListActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        try {
            if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                BannerAds bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bannerAds.setLayoutParams(layoutParams);
                runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLayNewsList)).addView(bannerAds));
            }

            if (bundle.containsKey("selectedNews")) {
                articles = new Gson().fromJson(bundle.getString("selectedNews"), new TypeToken<ArrayList<Article>>() {
                }.getType());
//                articles = bundle.getParcelableArrayList("selectedNews");
            }

            ImageView mImg_toolbarIcon = findViewById(R.id.img_newsDetailListToolbar);
            mImg_toolbarIcon.setImageResource(IconTypes.getIcon(this.getApplicationInfo().packageName));
            newsListModeAdapter = new NewsListModeAdapter(articles, this, (ViewGroup) findViewById(R.id.videoLayout));
            mRec_newsListMode.setAdapter(newsListModeAdapter);

            MiddleItemFinder.MiddleItemCallback callback = middleElement -> {
                /** SEND ANALYTICS */
                if (articles.get(middleElement).getTitle() != null && articles.get(middleElement).getUrl() != null && !TextUtils.isEmpty(articles.get(middleElement).getTitle()) && !TextUtils.isEmpty(articles.get(middleElement).getUrl()))
                    new TurkuvazAnalytic(NewsDetailListActivity.this).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), articles.get(middleElement).getTitle())
                            .addParameter(AnalyticsEvents.URL.getEventName(), articles.get(middleElement).getUrl())
                            .sendEvent();
            };

            mRec_newsListMode.addOnScrollListener(new MiddleItemFinder(this, mLayoutManager, callback));

            /** SEND ANALYTICS */
//            new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName()).sendEvent();
            if (articles.get(0).getTitle() != null && articles.get(0).getUrl() != null && !TextUtils.isEmpty(articles.get(0).getTitle()) && !TextUtils.isEmpty(articles.get(0).getUrl()))
                new TurkuvazAnalytic(NewsDetailListActivity.this).setIsPageView(true).setLoggable(true)
                        .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
                        .addParameter(AnalyticsEvents.TITLE.getEventName(), articles.get(0).getTitle())
                        .addParameter(AnalyticsEvents.URL.getEventName(), articles.get(0).getUrl())
                        .sendEvent();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_goHome) {
            GlobalIntent.openForceHome(NewsDetailListActivity.this, false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news_details, menu);
        MenuItem item = menu.findItem(R.id.action_goHome);
        DrawableCompat.setTint(item.getIcon(), Color.parseColor(Preferences.getString(NewsDetailListActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"))); // anasayfa iconu
        return true;
    }

    @Override
    public void onBackPressed() {
        if (newsListModeAdapter != null && newsListModeAdapter.isFullScreenVideo()) {
            newsListModeAdapter.disableFullScreenVideo();
        } else {
            super.onBackPressed();
        }
    }

    public void clickToolbarLogo(View view) {
        GlobalIntent.openForceHome(this, true);
    }
}