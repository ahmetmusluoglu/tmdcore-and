package turkuvaz.general.turkuvazgeneralactivitys.NewsDetails.NewsListMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdSize;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import turkuvaz.general.turkuvazgeneralactivitys.FlowLayout;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.NewsList.NewsList;
import turkuvaz.general.turkuvazgeneralwidgets.ShareBar.ShareBar;
import turkuvaz.sdk.galleryslider.ListGallery.VideoListDetailsActivity;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.global.SpecialWebView;
import turkuvaz.sdk.global.VideoEnabledWebChromeClient;
import turkuvaz.sdk.models.Models.ConfigBase.ComponentModel;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.FotoGallerySlider.ArticleSourceInfo;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;

import static turkuvaz.sdk.models.Models.Enums.ClickViewType.COLUMNIST_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.INTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.VIDEO;
import static turkuvaz.sdk.models.Models.Enums.NewsListType.GRID_1x1_LIST;

public class NewsListModeAdapter extends RecyclerView.Adapter implements SpecialWebListener.onStartPopupVideoListener, View.OnTouchListener {
    private List<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private int screenSize;
    private ViewGroup fullScreenVideoLayout;
    private ArrayList<VideoEnabledWebChromeClient> currentWebClient = new ArrayList<>();

    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SparkPlayerViewPopup sparkPlayerViewPopup = null;
    private static final int MAX_CLICK_DISTANCE = 15;
    private static final int MAX_CLICK_DURATION = 1000;

    float scale;
    int dpAsPixels;

    private long pressStartTime;
    private int pressedX;
    private int pressedY;
    int offsetX, offsetY;
    int actionBarHeight = 0;
    private String widgetToolsApiPath;
    private ComponentModel componentModelWidgetTool;

    private RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public NewsListModeAdapter(List<Article> dummyData, Context context, ViewGroup fullScreenVideoLayout) {
        if (context == null)
            return;

        /*for (int i = 0; i < dummyData.size(); i++) {
            if (dummyData.get(i) == null)
                dummyData.remove(i);
        }*/

        for (Iterator<Article> iterator = dummyData.iterator(); iterator.hasNext(); ) {
            Article art = iterator.next();
            if (art == null) {
                iterator.remove();
            }
        }

        try {
            String widgetToolsJson = Preferences.getString(context, ApiListEnums.WIDGET_TOOLS_COMPONENT_JSON.getType(), "");
            componentModelWidgetTool = new Gson().fromJson(widgetToolsJson, ComponentModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.screenSize = Preferences.getInt(context, SettingsTypes.SCREEN_WIDTH.getType(), -1);
        this.arrayList = dummyData;
        this.fullScreenVideoLayout = fullScreenVideoLayout;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.item_news_list_mode, parent, false);

        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Article currentModel = arrayList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals("")) {
            BannerAds topAds = new BannerAds(context, new AdSize(320, 142), ApiListEnums.ADS_GENERAL_320x142.getApi(context));
            viewHolder.mFrame_TopAds.addView(topAds);
        } else {
            scale = context.getResources().getDisplayMetrics().density;
            dpAsPixels = (int) (12 * scale + 0.5f); // padding = 12 olacak şekilde
            viewHolder.mTv_title.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);
        }

        viewHolder.mTv_title.setTextSize(Preferences.getInt(context, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25));
        viewHolder.mTv_description.setTextSize(Preferences.getInt(context, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));

        if (arrayList.get(position) != null && arrayList.get(position).getTitle() != null && arrayList.get(position).getUrl() != null) {
            viewHolder.mShareBar.setSocialMediaClickListener(new ShareBar.shareNewsClickListener() {
                @Override
                public void onClickFacebook() {
                    GlobalIntent.shareFacebook(context, arrayList.get(position).getTitle() + "\n" + ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName) + arrayList.get(position).getUrl());
                }

                @Override
                public void onClickTwitter() {
                    GlobalIntent.shareTwitter(context, arrayList.get(position).getTitle() + "\n" + ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName) + arrayList.get(position).getUrl());
                }

                @Override
                public void onClickWhatsApp() {
                    GlobalIntent.shareWhatApp(context, arrayList.get(position).getTitle() + "\n" + ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName) + arrayList.get(position).getUrl());
                }

                @Override
                public void onClickGeneral() {
                    GlobalIntent.shareGeneral(context, ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName) + arrayList.get(position).getUrl(), arrayList.get(position).getTitle());
                }

                @Override
                public void onClickTextChanger() {

                }
            });
        }

        if (currentModel.getTitle() != null)
            viewHolder.mTv_title.setText(currentModel.getTitle());

        if (currentModel.getTitleShort() != null)
            viewHolder.mTv_description.setText(currentModel.getSpot());

        String newsSource = currentModel.getSource() != null && !currentModel.getSource().isEmpty()
                ? "<br/>" + context.getResources().getString(R.string.source_name)
                + "<font color=\"#C40016\"> " + currentModel.getSource() + "</font>" : "";
//        if (currentModel.getOutputDate() != null && !TextUtils.isEmpty(currentModel.getOutputDate()))
//            viewHolder.mTv_newsDate.setText(String.valueOf(currentModel.getOutputDate().trim()));
//        else if (currentModel.getModifiedDate() != null && !TextUtils.isEmpty(currentModel.getModifiedDate()))
//            viewHolder.mTv_newsDate.setText(String.valueOf(context.getResources().getString(R.string.son_guncelleme) + convertDate(currentModel.getModifiedDate())));
//            viewHolder.mTv_newsDate.setText(Html.fromHtml(context.getResources().getString(R.string.son_guncelleme, convertDate(currentModel.getModifiedDate()), newsSource)));


        if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage())) {
            Glide.with(context.getApplicationContext()).asBitmap().load(currentModel.getPrimaryImage()).listener(
                    new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                            ((Activity) context).runOnUiThread(() -> {
                                try {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    viewHolder.mImg_newsImage.getLayoutParams().height = screenSize * height / width;
                                } catch (Exception r) {

                                }
                                viewHolder.mImg_newsImage.setImageBitmap(bitmap);
                            });

                            return false;
                        }
                    }
            ).submit();

            viewHolder.mImg_newsImage.setOnClickListener(v -> GlobalIntent.openImage(context, currentModel.getPrimaryImage()));
        } else {
            viewHolder.mImg_newsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
            viewHolder.mImg_newsImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        viewHolder.mNewsWebView.setContentType("app-news");
        viewHolder.mNewsWebView.setArticle(arrayList.get(position));
        viewHolder.mNewsWebView.loadNews();
        SpecialWebListener specialWebListener = new SpecialWebListener(context, null, this);
        viewHolder.mNewsWebView.setOnSelectedURL(specialWebListener);

        /** SET ADS */
        BannerAds bannerAds = new BannerAds(context, new AdSize(300, 250), GlobalMethods.getCategoryZone(context, currentModel.getCategoryId(), AdsType.ADS_300x250));
        viewHolder.mFrame_bottomAds.addView(bannerAds);

        if (Preferences.getBoolean(context, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(context).equals(""))
            viewHolder.mFrame_bottomAds.setPadding(viewHolder.mFrame_bottomAds.getPaddingLeft(), viewHolder.mFrame_bottomAds.getPaddingTop(), viewHolder.mFrame_bottomAds.getPaddingRight(), (int) (65 * context.getResources().getDisplayMetrics().density));

        if (componentModelWidgetTool != null && componentModelWidgetTool.getActive())
            viewHolder.llDetailsListedNews.addView(addRelatedNews(context, currentModel.getArticleId(), viewHolder, currentModel.getCategoryId()));

        VideoEnabledWebChromeClient webChromeClient;
        webChromeClient = new VideoEnabledWebChromeClient(viewHolder.nonVideoLayout, fullScreenVideoLayout, viewHolder.mNewsWebView);
        currentWebClient.add(webChromeClient);
        viewHolder.mNewsWebView.setWebChromeClient(webChromeClient);
        addArticleSourceInfo(viewHolder, currentModel);

    }

    private void addArticleSourceInfo(ViewHolder viewHolder, Article currentModel) {

        /*ArrayList<ArticleSourceInfo> mArticleSourceInfo = new ArrayList<>();
        ArticleSourceInfo articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("MELİH TORUNLAR");
        articleSourceInfo.setPrimaryImage("");
        mArticleSourceInfo.add(articleSourceInfo);

        articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("ZİYA RAMOĞLU");
        articleSourceInfo.setPrimaryImage("https://isbh.tmgrup.com.tr/sbh/2016/10/20/60x60/1476956060771.jpg");
        mArticleSourceInfo.add(articleSourceInfo);

        articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("MEHMET BONCUK");
        articleSourceInfo.setPrimaryImage("");
        mArticleSourceInfo.add(articleSourceInfo);

        articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("SIRRIBERK ARSLAN");
        articleSourceInfo.setPrimaryImage("https://isbh.tmgrup.com.tr/sbh/2017/05/31/60x60/1496236003046.jpg");
        mArticleSourceInfo.add(articleSourceInfo);

        articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("BEKİR COŞKUN");
        articleSourceInfo.setPrimaryImage("");
        mArticleSourceInfo.add(articleSourceInfo);

        articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("YASER ÇAPAROĞLU");
        articleSourceInfo.setPrimaryImage("https://isbh.tmgrup.com.tr/sbh/2016/10/20/60x60/1476961295185.jpg");
        mArticleSourceInfo.add(articleSourceInfo);

        articleSourceInfo = new ArticleSourceInfo();
        articleSourceInfo.setSource("HAYRETTİN YENEL");
        articleSourceInfo.setPrimaryImage("");
        mArticleSourceInfo.add(articleSourceInfo);*/

        List<ArticleSourceInfo> mArticleSourceInfo= currentModel.getArticleSourceInfo();
        if (mArticleSourceInfo != null) {
            viewHolder.llDetailSources.setVisibility(View.VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(context);
            int pos = 0;
            for (ArticleSourceInfo info : mArticleSourceInfo) {
                if (info != null) {
                    View view = inflater.inflate(R.layout.source_item, null);
                    TextView txtSource = view.findViewById(R.id.txtSource);
                    ImageView imgImage = view.findViewById(R.id.imgImage);
                    View vLine = view.findViewById(R.id.vLine);

                    if (info.getPrimaryImage() != null && !info.getPrimaryImage().isEmpty()) {
                        Glide.with(context).load(info.getPrimaryImage()).into(imgImage);
//                        imgImage.setVisibility(View.VISIBLE);
                    } else {
                        imgImage.setVisibility(View.GONE);
//                        vLine.setVisibility(pos++ == 0 ? View.GONE : View.VISIBLE);
                    }

                    if (info.getSource() != null)
                        txtSource.setText(info.getSource().replaceAll(" ", "\n"));
                    view.setLayoutParams(param);
                    viewHolder.frameDetailSources.addView(view);
                }
            }
        } else {
            viewHolder.llDetailSources.setVisibility(View.GONE);
        }
    }

    private NewsList addRelatedNews(Context context, String articleId, final ViewHolder viewHolder, final String categoryId) {
        try {
            widgetToolsApiPath = componentModelWidgetTool.getApiPath();
            if (widgetToolsApiPath != null && !widgetToolsApiPath.isEmpty()) {
                NewsList relatedNews = new NewsList(context);
                relatedNews.setApiPath(widgetToolsApiPath);
                relatedNews.checkContainsNews(true);
                relatedNews.setCategoryID(null);
                relatedNews.setTakeCount(componentModelWidgetTool.getTakeCountHeadline());
                relatedNews.setArticleID(articleId);
                relatedNews.setNewsListType(GRID_1x1_LIST);
                relatedNews.setCategoryShow(false);
                relatedNews.setOnLoadMoreMode(false);
                relatedNews.setOnListener(new NewsList.OnListener() {

                    @Override
                    public void onSuccess(boolean isFirst) {
                        viewHolder.llDetailsListedNews.setVisibility(View.VISIBLE);
                        if (isFirst) {
                            relatedNews.setCategoryID(categoryId);
                            relatedNews.setTakeCount(componentModelWidgetTool.getTakeCountCategory());
                            relatedNews.getNewsList2();
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        viewHolder.llDetailsListedNews.setVisibility(View.GONE);
                    }
                });

                relatedNews.init(false);

                relatedNews.setOnSelectedNewsListener((selectedNews, position, clickViewType) -> {
                    String selectedNewsExternal = selectedNews.get(position).getExternal();
                    ExternalTypes selectedNewsExternalType = selectedNews.get(position).getExternalType();

                    if (clickViewType == COLUMNIST_SLIDER) {
                        GlobalIntent.openAllColumnistWithArticle(context, selectedNews, position, true);
                        return;
                    }
                    switch (selectedNewsExternalType) {
                        case EMPTY:
                            GlobalIntent.openNewsDetailsList(context, selectedNews, position);
                            break;
                        case ALBUM:
                            ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(context), ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(context), selectedNewsExternal.replace(ALBUM.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(context), IconTypes.getIcon(context.getApplicationInfo().packageName));
                            GlobalIntent.openInfinityGallery(context, showGalleryModel, ALBUM);
                            break;
                        case NEWS:
                            GlobalIntent.openNewsWithID(context, ApiListEnums.DETAILS_ARTICLE.getApi(context), selectedNewsExternal.replace(NEWS.getType(), ""));
                            break;
                        case VIDEO:
                            GlobalIntent.openVideoDetailsWithID(context, selectedNewsExternal.replace(VIDEO.getType(), ""), "AD_TAG");
                            break;
                        case EXTERNAL:
                            GlobalIntent.openExternalBrowser(context, selectedNewsExternal.replace(EXTERNAL.getType(), ""));
                            break;
                        case INTERNAL:
                            GlobalIntent.openInternalBrowser(context, selectedNewsExternal.replace(INTERNAL.getType(), ""));
                            break;
                        case PHOTO_NEWS:
                            ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(context), ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(context), selectedNewsExternal.replace(PHOTO_NEWS.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(context), IconTypes.getIcon(context.getApplicationInfo().packageName));
                            GlobalIntent.openInfinityGalleryFotoHaber(context,
                                    showGalleryModel2,
                                    selectedNews.get(position).getTitle(),
                                    selectedNews.get(position).getSpot(),
                                    selectedNews.get(position).getUrl());
                            break;
                        case LIVE_STREAM:
                            GlobalIntent.openLiveStreamWithURL(context, "Canlı Yayın", selectedNewsExternal.replace(LIVE_STREAM.getType(), ""), ApiListEnums.ADS_PREROLL.getApi(context));
                            break;
                        case ARTICLE_SOURCE:
                            GlobalIntent.openAllColumnistWithArticle(context, selectedNews, position, true);
                            break;
                    }
                });
                return relatedNews;
            }
        } catch (
                NullPointerException e) {
            e.printStackTrace();
            viewHolder.llDetailsListedNews.setVisibility(View.GONE);
            return null;
        }
        viewHolder.llDetailsListedNews.setVisibility(View.GONE);
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onPopupStart(String videoUrl) {

        try {
            if (popupWindow != null) {
                popupWindow.dismiss();
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
                nativeMediaPlayer.setVideoURL(videoUrl);
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
                            pressStartTime = System.currentTimeMillis();
                            pressedX = (int) event.getX();
                            pressedY = (int) event.getY();
                            break;
                        }

                        case MotionEvent.ACTION_UP: {
                            long pressDuration = System.currentTimeMillis() - pressStartTime;
                            if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(context, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
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
                LayoutInflater layoutInflater = LayoutInflater.from(context);
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

            TypedValue tv = new TypedValue();
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
            if (resourceId > 0) {
                actionBarHeight += context.getResources().getDimensionPixelSize(resourceId);
            }
            if (popupWindow != null)
                popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, actionBarHeight);
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTv_title, mTv_description, mTv_newsDate;
        private ImageView mImg_newsImage;
        private SpecialWebView mNewsWebView;
        private FrameLayout mFrame_bottomAds, mFrame_TopAds;
        private ShareBar mShareBar;
        private View nonVideoLayout;
        private HtmlTextView txtNewsDetails;
        private LinearLayout llDetailsListedNews;
        private FlowLayout frameDetailSources;
        private LinearLayout llDetailSources;

        ViewHolder(View itemView) {
            super(itemView);
            mFrame_TopAds = itemView.findViewById(R.id.frame_newsTopBanner);
            mTv_title = itemView.findViewById(R.id.tv_newsTitle);
            mTv_description = itemView.findViewById(R.id.tv_newsSpot);
            mTv_newsDate = itemView.findViewById(R.id.tv_newsDate);
            mImg_newsImage = itemView.findViewById(R.id.img_newsDetailTop);
            mNewsWebView = itemView.findViewById(R.id.web_details);
            mFrame_bottomAds = itemView.findViewById(R.id.frame_newsBottomBanner);
            mShareBar = itemView.findViewById(R.id.shareBar_listNews);
            nonVideoLayout = itemView.findViewById(R.id.nonVideoLayout);
            llDetailsListedNews = itemView.findViewById(R.id.llDetailsListedNews);
            txtNewsDetails = itemView.findViewById(R.id.txtNewsDetails);
            frameDetailSources = itemView.findViewById(R.id.frameDetailSources);
            llDetailSources = itemView.findViewById(R.id.llDetailSources);
        }

    }


    public boolean isFullScreenVideo() {
        boolean isFullScreen = false;

        if (currentWebClient != null && currentWebClient.size() > 0) {
            for (int i = 0; i < currentWebClient.size(); i++) {
                if (currentWebClient.get(i).isVideoFullscreen()) {
                    isFullScreen = true;
                }
            }
        }
        return isFullScreen;
    }

    public void disableFullScreenVideo() {
        if (currentWebClient != null && currentWebClient.size() > 0) {
            for (int i = 0; i < currentWebClient.size(); i++) {
                currentWebClient.get(i).onBackPressed();
            }
        }
    }

    private String convertDate(String irregularDate) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM, yyyy HH:mm", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
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
                if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(context, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
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

    private String getDescriptionContent(Article mArticle) {
        if (mArticle == null)
            return "";

        String mCssUrl = ApiListEnums.CONTENT_CSS.getApi(context) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(context);
        String contentType = "app-news";
        String mPhotoNewsDescription = null;

        if (mArticle.getExternal() == null)
            mArticle.setExternal("");
        if (mArticle.getUseDetailPaging() != null && mArticle.getUseDetailPaging() && mArticle.getExternal().equals("")) {
            try {
                StringBuilder sb = new StringBuilder();
                String jsonOutput = mArticle.getDescription();
                Type listType = new TypeToken<List<String>>() {
                }.getType();

                List<String> posts = new Gson().fromJson(jsonOutput, listType);
                if (posts != null) {
                    for (String s : posts) {
                        sb.append(s);
                    }
                }
                mPhotoNewsDescription = sb.toString();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        String style = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + mCssUrl + "\">";

        return context.getString(R.string.text_content, style, contentType,
                (mArticle.getUseDetailPaging() != null && mArticle.getUseDetailPaging() && mArticle.getExternal().equals("")
                        ? mPhotoNewsDescription : mArticle.getDescription()));

    }
}