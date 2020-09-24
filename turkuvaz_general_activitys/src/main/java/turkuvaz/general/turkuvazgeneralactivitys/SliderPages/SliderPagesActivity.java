package turkuvaz.general.turkuvazgeneralactivitys.SliderPages;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.InfinityActivity;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGalleryWithSlider.GalleryActivityWithSlider;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.ConfigBase.Settings;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 9/22/2020.
 */
public class SliderPagesActivity extends Activity {

    ShowGalleryModel mShowGalleryModel;
    private RelativeLayout rlNext;
    private RelativeLayout rlPrev;
    private ArrayList<Article> allNews;
    public static int activePosition;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_with_slider);

        try {
            mShowGalleryModel = getIntent().getParcelableExtra("showAllGalleryModels");
            int currPosition = getIntent().getExtras().getInt("currPosition", 0);

            Gson gson = new Gson();
            final ArrayList<Article> allNews = gson.fromJson(getIntent().getExtras().getString("allNews"), new TypeToken<ArrayList<Article>>() {}.getType());

            ViewPager pager = findViewById(R.id.pager);
            rlNext = findViewById(R.id.rlNext);
            rlPrev = findViewById(R.id.rlPrev);

            if (allNews != null && allNews.size() > 0) {
                new TurkuvazAnalytic(SliderPagesActivity.this).setIsPageView(true).setLoggable(true)
                        .setEventName(AnalyticsEvents.GALLERY_DETAILS.getEventName())
                        .addParameter(AnalyticsEvents.TITLE.getEventName(), allNews.get(currPosition).getTitle())
                        .addParameter(AnalyticsEvents.URL.getEventName(), allNews.get(currPosition).getUrl())
                        .sendEvent();
            }


            GalleryActivityWithSlider.activePosition = currPosition;
            InfinityActivity.mPageIndex = 1;
//            galleryPagerAdapter = new GalleryPagerAdapter(SliderPagesActivity.this, mShowGalleryModel);
//            pager.setAdapter(galleryPagerAdapter);
            pager.setCurrentItem(currPosition);

            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    new TurkuvazAnalytic(SliderPagesActivity.this).setIsPageView(true).setLoggable(true)
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

        }
    }



    private void arrowVisibility(int position) {
//        rlNext.setVisibility(position == galleryPagerAdapter.getCount() - 1 ? View.GONE : View.VISIBLE);
        rlPrev.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
    }
}
