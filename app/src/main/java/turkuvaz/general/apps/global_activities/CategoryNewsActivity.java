package turkuvaz.general.apps.global_activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;

import turkuvaz.general.apps.R;
import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralwidgets.NewsListAndSlider.NewsListWithSlider;
import turkuvaz.general.turkuvazgeneralwidgets.NewsListAndSlider.NewsListWithSliderAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.SuccessOrErrorBar.LoadStatus;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.AnalyticsParameters;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Preferences;

import static turkuvaz.sdk.models.Models.Enums.ClickViewType.COLUMNIST_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ClickViewType.PHOTO_GALLERY_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ClickViewType.VIDEO_GALLERY_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.INTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.VIDEO;

public class CategoryNewsActivity extends BaseGeneralActivity implements NewsListWithSliderAdapter.onSelectedNewsListener, NewsListWithSliderAdapter.onHeadlineSelectedNewsListener {

    LoadStatus mLoadStatus;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase,""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news);

        mLoadStatus = findViewById(R.id.ls_categoryNews);

        Bundle bundle = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(CategoryNewsActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#000000"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(CategoryNewsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }


        if (bundle == null || !bundle.containsKey("newsApiPath") || !bundle.containsKey("categoryID") || !bundle.containsKey("toolbarTitle")) {
            finish();
        } else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(Html.fromHtml("<font color="+Color.parseColor(Preferences.getString(CategoryNewsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"))+">" + getResources().getString(R.string.haberler_baslik) + bundle.getString("toolbarTitle", "") + "</font>"));

            getNews(bundle);

            mLoadStatus.setOnLoadTryAgain(() -> {
                new Handler().postDelayed(() -> getNews(bundle), 1000);
            });

            /** SEND ANALYTICS */
            new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true)
                    .setEventName(AnalyticsEvents.NEWS_CATEGORY.getEventName())
                    .addParameter(AnalyticsParameters.CATEGORY.getParametersName(), bundle.getString("toolbarTitle", ""))
                    .sendEvent();
        }
    }

    private void getNews(Bundle bundle) {
        FrameLayout frameLayout = findViewById(R.id.frame_newsListWithCategory);
        NewsListWithSlider newsListWithSlider = new NewsListWithSlider(this);
        newsListWithSlider.setApiPath(bundle.getString("newsApiPath", "none"));
        newsListWithSlider.setCategoryID(bundle.getString("categoryID", "none"));
        newsListWithSlider.setOnSelectedNewsListener(this);
        newsListWithSlider.setOnHeadlineSelectedNewsListener(this);
        newsListWithSlider.setOnStatusListener(new NewsListWithSlider.onStatusListener() {
            @Override
            public void onStartLoad() {
                findViewById(R.id.pb_loadMoreNewsActivity).setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess() {
                mLoadStatus.loadStatusSuccess();
                findViewById(R.id.pb_loadMoreNewsActivity).setVisibility(View.GONE);
            }

            @Override
            public void onError(String errorMessage) {
                mLoadStatus.loadStatusError("");
            }
        });
        newsListWithSlider.init(CategoryNewsActivity.this);
        newsListWithSlider.setOnLoadMoreMode(true);

        frameLayout.addView(newsListWithSlider);
    }

    @Override
    public void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType) {

        if (clickViewType == PHOTO_GALLERY_SLIDER) {
            ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), selectedNews.get(position).getId(), ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
            GlobalIntent.openInfinityGallery(CategoryNewsActivity.this, showGalleryModel, ALBUM);
            return;
        }

        if (clickViewType == COLUMNIST_SLIDER) {
//            GlobalIntent.openColumnistWithArticle(CategoryNewsActivity.this, selectedNews.get(position),true);
            GlobalIntent.openAllColumnistWithArticle(CategoryNewsActivity.this, selectedNews,position,true);
            return;
        }

        if (clickViewType == VIDEO_GALLERY_SLIDER) {
            if (selectedNews.get(position).getVideoSmilUrl() != null || selectedNews.get(position).getVideoUrl() != null || selectedNews.get(position).getVideoMobileUrl() != null) {
                GlobalIntent.openVideoWithURL(CategoryNewsActivity.this, selectedNews.get(position).getTitle(), selectedNews.get(position).getVideoUrl(), "AD_TAG");
            }
            return;
        }


        String selectedNewsExternal = selectedNews.get(position).getExternal();
        ExternalTypes selectedNewsExternalType = selectedNews.get(position).getExternalType();

        //TODO EVENT LIST
        switch (selectedNewsExternalType) {
            case EMPTY:
                GlobalIntent.openNewsDetailsList(CategoryNewsActivity.this, selectedNews, position);
                break;
            case ALBUM:
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), selectedNewsExternal.replace(ALBUM.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(CategoryNewsActivity.this, showGalleryModel, ALBUM);
                break;
            case NEWS:
                GlobalIntent.openNewsWithID(CategoryNewsActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(this), selectedNewsExternal.replace(NEWS.getType(), ""));
                break;
            case VIDEO:
                GlobalIntent.openVideoDetailsWithID(CategoryNewsActivity.this, selectedNewsExternal.replace(VIDEO.getType(), ""), "AD_TAG");
                break;
            case EXTERNAL:
                GlobalIntent.openExternalBrowser(CategoryNewsActivity.this, selectedNewsExternal.replace(EXTERNAL.getType(), ""));
                break;
            case INTERNAL:
                GlobalIntent.openInternalBrowser(CategoryNewsActivity.this, selectedNewsExternal.replace(INTERNAL.getType(), ""));
                break;
            case PHOTO_NEWS:
                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), selectedNewsExternal.replace(PHOTO_NEWS.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                GlobalIntent.openInfinityGalleryFotoHaber(CategoryNewsActivity.this,
                        showGalleryModel2,
                        selectedNews.get(position).getTitle(),
                        selectedNews.get(position).getSpot(),
                        selectedNews.get(position).getUrl());
                break;
            case LIVE_STREAM:
                GlobalIntent.openLiveStreamWithURL(CategoryNewsActivity.this, "Canlı Yayın", selectedNewsExternal.replace(LIVE_STREAM.getType(), ""), ApiListEnums.ADS_PREROLL.getApi(CategoryNewsActivity.this));
                break;
            case ARTICLE_SOURCE:
//                GlobalIntent.openColumnistWithArticle(CategoryNewsActivity.this, selectedNews.get(position),true);
                GlobalIntent.openAllColumnistWithArticle(CategoryNewsActivity.this, selectedNews,position,true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}