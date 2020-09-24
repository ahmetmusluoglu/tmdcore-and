package turkuvaz.general.turkuvazgeneralactivitys.InfinityGalleryWithSlider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.ads.AdSize;

import java.util.Objects;

import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.InfinityActivity;
import turkuvaz.general.turkuvazgeneralactivitys.NewsDetails.NewsListMode.NewsDetailListActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.AlbumMedia;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 6/1/2020.
 */
class GalleryPagerAdapter extends PagerAdapter {

    private InfinityActivity infinityActivity;
    private ExternalTypes externalTypes = ExternalTypes.ALBUM;
    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SparkPlayerViewPopup sparkPlayerViewPopup = null;
    private ShowGalleryModel mShowGalleryModel;
    private int actionBarHeight = 0;
    private int pressedX;
    private int pressedY;
    private int offsetX, offsetY;
    private GalleryActivityWithSlider activity;

    GalleryPagerAdapter(GalleryActivityWithSlider activity, ShowGalleryModel mShowGalleryModel) {
        this.activity = activity;
        this.mShowGalleryModel = mShowGalleryModel;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(collection.getContext()).inflate(R.layout.gallery_pager_adapter, collection, false);

        mShowGalleryModel.setGalleryID(mShowGalleryModel.getGalleryIds().get(position));
        configureToolbar(view, mShowGalleryModel, GalleryActivityWithSlider.activePosition);
        collection.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mShowGalleryModel == null ? 0 : mShowGalleryModel.getGalleryIds().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    private void configureToolbar(ViewGroup view, ShowGalleryModel mShowGalleryModel, int selectedPosition) {
        Context context = view.getContext();

        addBanner(view);
        infinityActivity = new InfinityActivity(context);
        infinityActivity.setSelectedPosition(selectedPosition);
        infinityActivity.setShowGalleryModel(mShowGalleryModel);

        infinityActivity.setOnSelectedURLListener(new InfinityActivity.OnSelectedURLListener() {
            @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
            @Override
            public void onVideo(String VideoURL, String VideoCategory, String VideoTitle) {

                try {
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }

                    TypedValue tv = new TypedValue();
                    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                    if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                        actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
                    }
                    if (resourceId > 0) {
                        actionBarHeight += context.getResources().getDimensionPixelSize(resourceId);
                    }

                    if (GlobalMethods.isActiveNativePlayer()) {
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        popupView = layoutInflater.inflate(R.layout.popup_nativ_player, null);
                        popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        NativeMediaPlayer nativeMediaPlayer = popupView.findViewById(R.id.nativeMediaPlayerPopup);
                        popupView.findViewById(R.id.btn_sparkPopupClose).setOnClickListener(view -> popupWindow.dismiss());
                        nativeMediaPlayer.setActivity((NewsDetailListActivity) context);
                        nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                            if (isFullScreen) {
                                Objects.requireNonNull(((NewsDetailListActivity) context).getSupportActionBar()).hide();
                            } else {
                                Objects.requireNonNull(((NewsDetailListActivity) context).getSupportActionBar()).show();
                            }
                        });
                        nativeMediaPlayer.setVideoURL(VideoURL);
                        nativeMediaPlayer.init();

                        nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                            try {
                                if (isFullScreen) {
                                    Objects.requireNonNull(((NewsDetailListActivity) context).getSupportActionBar()).hide();
                                    popupView.findViewById(R.id.frameToolbar).setVisibility(View.GONE);
                                    popupView.setBackgroundColor(Color.WHITE);
                                } else {
                                    Objects.requireNonNull(((NewsDetailListActivity) context).getSupportActionBar()).show();
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
//                                    pressStartTime = System.currentTimeMillis();
                                    pressedX = (int) event.getX();
                                    pressedY = (int) event.getY();
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
                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        popupView = layoutInflater.inflate(R.layout.popup, null);
                        popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        sparkPlayerViewPopup = popupView.findViewById(R.id.sparkplayer_popup);
                        sparkPlayerViewPopup.setVideoURL(VideoURL);
                        sparkPlayerViewPopup.setOnClosePopupListener(() -> popupWindow.dismiss());
                        sparkPlayerViewPopup.init();


                        popupWindow.setOnDismissListener(() -> {
                            sparkPlayerViewPopup.closeSpark();
                            popupWindow = null;
                            popupView = null;
                            sparkPlayerViewPopup = null;

                        });

                        ((ViewGroup) sparkPlayerViewPopup.getChildAt(0)).getChildAt(0).setOnTouchListener((view1, event) -> {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    pressedX = (int) event.getX();
                                    pressedY = (int) event.getY();
                                    break;

                                case MotionEvent.ACTION_UP:
                                    break;

                                case MotionEvent.ACTION_MOVE:
                                    offsetX = (int) event.getRawX() - pressedX;
                                    offsetY = (int) event.getRawY() - pressedY;
                                    if (offsetY > actionBarHeight)
                                        if (popupWindow != null)
                                            popupWindow.update(offsetX, offsetY, -1, -1, true);
                                    break;
                            }
                            return true;
                        });
                    }

                    if (popupWindow != null)
                        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, actionBarHeight);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNews(String NewsID) {
                GlobalIntent.openNewsWithID(context, ApiListEnums.DETAILS_ARTICLE.getApi(context), NewsID);
            }

            @Override
            public void onAlbum(String AlbumID) {
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(context), ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(context), AlbumID, ApiListEnums.DETAILS_VIDEO.getApi(context), IconTypes.getIcon(context.getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(context, showGalleryModel, ExternalTypes.ALBUM);
            }

            @Override
            public void onPhotoNews(String PhotoNewsID) {
                GlobalIntent.openNewsWithID(context, ApiListEnums.DETAILS_ARTICLE.getApi(context), PhotoNewsID);
            }

            @Override
            public void onArticle(String ArticleSourceID) {
                GlobalIntent.openColumnistWithID(context, ApiListEnums.DETAILS_ARTICLE.getApi(context), ArticleSourceID);
            }

            @Override
            public void onExternal(String ExternalURL) {
                GlobalIntent.openExternalBrowser(context, ExternalURL);
            }

            @Override
            public void onInternal(String InternalURL) {
                GlobalIntent.openInternalBrowser(context, InternalURL);
            }

            @Override
            public void onOpenGallery(String galleryTitle, String galleryURL) {
                /* SEND ANALYTICS */
//                if (externalTypes == ExternalTypes.PHOTO_NEWS) {
//                    new TurkuvazAnalytic(context).setIsPageView(true).setLoggable(true)
//                            .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
//                            .addParameter(AnalyticsEvents.TITLE.getEventName(), galleryTitle)
//                            .addParameter(AnalyticsEvents.URL.getEventName(), galleryURL)
//                            .sendEvent();
//                } else {
//                    /** SEND ANALYTICS */
//                    new TurkuvazAnalytic(context).setIsPageView(true).setLoggable(true)
//                            .setEventName(AnalyticsEvents.GALLERY_DETAILS.getEventName())
//                            .addParameter(AnalyticsEvents.TITLE.getEventName(), galleryTitle)
//                            .addParameter(AnalyticsEvents.URL.getEventName(), galleryURL)
//                            .sendEvent();
//                }
            }

            @Override
            public void onCurrentIndex(AlbumMedia albumMedia) {
                if (externalTypes == ExternalTypes.PHOTO_NEWS) {
                    /* SEND ANALYTICS */
                    new TurkuvazAnalytic(context).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.NEWS_DETAILS.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), String.valueOf(albumMedia.getSortOrder()))
                            .addParameter(AnalyticsEvents.URL.getEventName(), albumMedia.getUrl() + "/" + albumMedia.getSortOrder())
                            .sendEvent();
                } else {
                    /* SEND ANALYTICS */
                    new TurkuvazAnalytic(context).setIsPageView(true).setLoggable(true)
                            .setEventName(AnalyticsEvents.GALLERY_INDEX_VIEW.getEventName())
                            .addParameter(AnalyticsEvents.TITLE.getEventName(), String.valueOf(albumMedia.getSortOrder()))
                            .addParameter(AnalyticsEvents.SHORT_ORDER.getEventName(), String.valueOf(albumMedia.getSortOrder()))
                            .sendEvent();
                }

            }
        });
        infinityActivity.init();
        ((FrameLayout) view.findViewById(R.id.frame_GalleryList)).addView(infinityActivity);
    }

    private void addBanner(ViewGroup view) {
        try {
            if (Preferences.getBoolean(view.getContext(), ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(view.getContext()).equals("")) {
                BannerAds bannerAds = new BannerAds(view.getContext(), new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(view.getContext()));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bannerAds.setLayoutParams(layoutParams);
                activity.runOnUiThread(() -> ((RelativeLayout) view.findViewById(R.id.relLayGalleryDetail)).addView(bannerAds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    PopupWindow getPopupWindow() {
        return popupWindow;
    }

    View getPopupView() {
        return popupView;
    }

    SparkPlayerViewPopup getSparkPlayerViewPopup() {
        return sparkPlayerViewPopup;
    }

    void setPopupWindow() {
        this.popupWindow = null;
    }

    void setPopupView() {
        this.popupView = null;
    }

    void setSparkPlayerViewPopup() {
        this.sparkPlayerViewPopup = null;
    }

    InfinityActivity getInfinityActivity() {
        return infinityActivity;
    }
}
