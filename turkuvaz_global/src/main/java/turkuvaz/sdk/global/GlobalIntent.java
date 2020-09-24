package turkuvaz.sdk.global;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.GetVideoModel.VideoModel;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.models.Models.SingleNews.SingleNewsModel;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ARTICLE_SOURCE;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.DEEPLINK;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EMPTY;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.HOMESCREEN;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.INTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PAGE;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.VIDEO;


/**
 * Created by turgay.ulutas on 23/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class GlobalIntent {


    private static final String HOME_PAGE = "turkuvaz.general.apps.base.CoreHomeActivity";
    private static final String CUSTOM_HOME_PAGE = "turkuvaz.general.apps.base.CoreCustomActivity";

    /**
     * GALLERY
     */
    private static final String GALLERY_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.GalleryActivity";
    private static final String GALLERY_CLASS_SLIDER = "turkuvaz.general.turkuvazgeneralactivitys.InfinityGalleryWithSlider.GalleryActivityWithSlider";

    /**
     * STREAMING
     */
    private static final String STREAMING_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.StreamingList.StreamingList";

    /**
     * NEWS
     */
    private static final String NEWS_DETAILS_LIST_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.NewsDetails.NewsListMode.NewsDetailListActivity";
    private static final String NEWS_DETAILS_SINGLE_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.NewsDetails.NewsDetailActivity";
    private static final String LAST_MINUTE_ACTIVITY = "turkuvaz.general.turkuvazgeneralactivitys.LastMinute.LastMinuteActivity";
    private static final String NEWS_LIST_WITH_CATEGORY_CLASS = "turkuvaz.general.apps.global_activities.CategoryNewsActivity";
    //    private static final String NEWS_LIST_WITH_CATEGORY_CLASS = "turkuvaz.general.apps.GlobalActivity.CategoryNewsActivity";

    /**
     * VIDEO
     */
    private static final String SPARK_PLAYER_FULLSCREEN_CLASS = "turkuvaz.sdk.global.SparkPlayerAcitivty";
    private static final String SPARK_PLAYER_POPUP_CLASS = "turkuvaz.sdk.global.SparkPlayerPopup";
    private static final String SPARK_PLAYER_DETAIL_CLASS = "turkuvaz.sdk.galleryslider.ListGallery.VideoListDetailsActivity";
    private static final String VIDEO_LIST_ACTIVITY_CLASS = "turkuvaz.general.apps.global_activities.VideoListActivity";
    private static final String CUSTOM_TV_SERIES_ACTIVITY_CLASS = "turkuvaz.general.apps.global_activities.CustomTvSeriesActivity";
    private static final String TV_SERIES_LIST_ACTIVITY_CLASS = "turkuvaz.general.apps.global_activities.TvSeriesListActivity";
    private static final String PROGRAMMES_LIST_ACTIVITY_CLASS = "turkuvaz.general.apps.global_activities.ProgrammesListActivity";
//    private static final String VIDEO_LIST_ACTIVITY_CLASS = "turkuvaz.general.apps.GlobalActivity.VideoListActivity";
//    private static final String TV_SERIES_LIST_ACTIVITY_CLASS = "com.turkuvaz.takvim.AllActivity.BottomMenu.TvSeriesListActivity";

    /**
     * BROWSER
     */
    private static final String INTERNAL_BROWSER_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.InternalBrowser.InternalBrowser";

    private static final String SETTINGS_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.Settings.SettingsActivity";

    /**
     * IMAGE
     */
    private static final String SINGLE_IMAGE__CLASS = "turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.GalleryDetailsActivity";
    //    private static final String NEWS_PAPER_BROWSER = "turkuvaz.general.turkuvazgeneralactivitys.NewsPaperBrowser.SingleNewsPaperPage";
    private static final String NEWS_PAPER_BROWSER = "turkuvaz.general.turkuvazgeneralactivitys.NewsPaperBrowser.NewsPaperBrowser";
//    private static final String PHOTO_GALLERY_LIST_ACTIVITY_CLASS = "turkuvaz.general.apps.GlobalActivity.PhotoGalleryListActivity";

    /**
     * COLUMNIST - AUTHOR
     */
    private static final String ALL_COLUMNIST_DETAILS_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.ColumnistsDetailSlider.AllColumnistsActivity";
    //    private static final String ALL_COLUMNIST_DETAILS_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.ColumnistsDetailSlider.ColumnistsActivity";
    private static final String COLUMNIST_DETAILS_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.ColumnistDetail.ColumnistDetailActivity";
    private static final String AUTHOR_LIST_ACTIVITY_CLASS = "turkuvaz.general.apps.global_activities.AuthorListActivity";
    private static final String AUTHOR_ARCHIVE_LIST_ACTIVITY_CLASS = "turkuvaz.general.turkuvazgeneralactivitys.AuthorArchive.AuthorsArchive";

    public static void openExternalBrowser(Context context, String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            context.startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void openInternalBrowser(Context context, String url) {
        try {
            Class<?> c = Class.forName(INTERNAL_BROWSER_CLASS);
            Intent intent = new Intent(context, c).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (url != null && !TextUtils.isEmpty(url) && url.contains("v1/link/")) {
                intent.putExtra("url", url).putExtra("isDataModel", true);
            } else {
                intent.putExtra("url", url).putExtra("isDataModel", false);
            }

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openNewsWithID(final Context context, String apiPath, String newsID) {
        if (context == null || apiPath == null || newsID == null)
            return;
        try {
            Class<?> c = Class.forName(NEWS_DETAILS_SINGLE_CLASS);
            Intent intent = new Intent(context, c)
                    .putExtra("apiPath", apiPath)
                    .putExtra("newsID", newsID)

                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }

    }

    public static void openColumnistWithID(final Context context, String apiPath, String newsID) {
        if (context == null || apiPath == null || newsID == null)
            return;

        RestInterface mRestInterface = ApiClient.getClientApi(context).create(RestInterface.class);
        mRestInterface.getSingleNews(apiPath, newsID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleNewsModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SingleNewsModel singleNewsModel) {
                        try {
                            if (singleNewsModel != null && singleNewsModel.getData() != null)
//                                openColumnistWithArticle(context, singleNewsModel.getData().getArticle().getResponse().get(0));
                                openColumnistWithArticle(context, singleNewsModel.getData().getArticle().getResponse().get(0), true);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void openImage(final Context context, String imageUrl) {
        try {
            Class<?> c = Class.forName(SINGLE_IMAGE__CLASS);
            Intent intent = new Intent(context, c).putExtra("imagePath", imageUrl).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            /** SEND ANALYTICS */
            new TurkuvazAnalytic(context).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.IMAGE_DETAILS.getEventName()).sendEvent();
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }


    //    public static void openColumnistWithArticle(final Context context, Article article) {
    public static void openColumnistWithArticle(final Context context, Article article, boolean allArticleBtnIsShow) {
        try {
            Class<?> c = Class.forName(COLUMNIST_DETAILS_CLASS);
//            Intent intent = new Intent(context, c).putExtra("selectedNews", article).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent intent = new Intent(context, c).putExtra("selectedNews", new Gson().toJson(article)).putExtra("allArticleBtnIsShow", allArticleBtnIsShow).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openAllColumnistWithArticle(final Context context, ArrayList<Article> articles, int position, boolean allArticleBtnIsShow) {
        try {
            boolean isSlider = Preferences.getBoolean(context, ApiListEnums.IS_SLIDER.getType(), false);
            if (isSlider) {
                Class<?> c = Class.forName(ALL_COLUMNIST_DETAILS_CLASS);
                Intent intent = new Intent(context, c)
                        .putExtra("allColumnists", new Gson().toJson(articles))
                        .putExtra("allArticleBtnIsShow", allArticleBtnIsShow)
                        .putExtra("position", position)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                openColumnistWithArticle(context, articles.get(position), allArticleBtnIsShow);
            }
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openInfinityGalleryWithSlider(final Context context, ShowGalleryModel showGalleryModel, ExternalTypes externalTypes, int currPosition, ArrayList<Article> selectedNews) {
        try {
            Class<?> c = Class.forName(GALLERY_CLASS_SLIDER);
            Intent intent = new Intent(context, c).putExtra("showAllGalleryModels", showGalleryModel).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (externalTypes == PHOTO_NEWS)
                intent.putExtra("externalType", PHOTO_NEWS);
            intent.putExtra("currPosition", currPosition);
            intent.putExtra("allNews", new Gson().toJson(selectedNews));
            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openInfinityGallery(final Context context, ShowGalleryModel showGalleryModel, ExternalTypes externalTypes) {
        try {
            Class<?> c = Class.forName(GALLERY_CLASS);
            Intent intent = new Intent(context, c).putExtra("showGalleryModel", showGalleryModel).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (externalTypes == PHOTO_NEWS)
                intent.putExtra("externalType", PHOTO_NEWS);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openInfinityGalleryFotoHaber(final Context context, ShowGalleryModel showGalleryModel, String title, String spot, String url) {
        try {
            Class<?> c = Class.forName(GALLERY_CLASS);
            Intent intent = new Intent(context, c).putExtra("showGalleryModel", showGalleryModel)
                    .putExtra("title", title)
                    .putExtra("spot", spot)
                    .putExtra("url", url)
                    .putExtra("externalType", PHOTO_NEWS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openNewsDetailsList(final Context context, ArrayList<Article> selectedNews, int position) {
        try {
            ArrayList<Article> articlesCleared = new ArrayList<>(selectedNews.subList(position, selectedNews.size()));
            ArrayList<Article> finalArticles = new ArrayList<>(articlesCleared.size() > 10 ? articlesCleared.subList(0, 10) : articlesCleared);
            Class<?> c = Class.forName(NEWS_DETAILS_LIST_CLASS);
            Intent intent = new Intent(context, c).putExtra("selectedNews", new Gson().toJson(finalArticles)).putExtra("selectedNewsPosition", position).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public static void openVideoWithURL(final Context context, String title, String videoURL, String adUnitTag) {

        try {
            Class<?> c = Class.forName(SPARK_PLAYER_FULLSCREEN_CLASS);
            Intent intent = new Intent(context, c)
                    .putExtra("videoUrlAddress", videoURL)
                    .putExtra("AD_TAG", adUnitTag)
                    .putExtra("TITLE", title)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }


    public static void openVideoPopupWithURL(final Context context, String title, String videoURL, String adUnitTag) {

        try {
            Class<?> c = Class.forName(SPARK_PLAYER_POPUP_CLASS);
            Intent intent = new Intent(context, c)
                    .putExtra("videoUrlAddress", videoURL)
                    .putExtra("AD_TAG", adUnitTag)
                    .putExtra("TITLE", title)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openVideoWithID(final Context context, String videoID, String mVideoApi, final String adUnitTag, boolean isPopup) {
        RestInterface mRestInterface = ApiClient.getClientApi(context).create(RestInterface.class);
        mRestInterface.getVideoURL(mVideoApi, videoID.toLowerCase()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoModel leftMenu) {
                        try {
                            if (leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().size() > 0) {
                                String title = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getTitle();
                                String category = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getCategoryName();
                                String VideoSmilUrl = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoSmilUrl();
                                String VideoMobileUrl = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoMobileUrl();
                                String VideoUrl = leftMenu.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoUrl();

                                if (VideoSmilUrl != null && !TextUtils.isEmpty(VideoSmilUrl)) {
                                    if (isPopup)
                                        openVideoPopupWithURL(context, title, VideoSmilUrl, adUnitTag);
                                    else
                                        openVideoWithURL(context, title, VideoSmilUrl, adUnitTag);
                                } else if (VideoMobileUrl != null && !TextUtils.isEmpty(VideoMobileUrl)) {
                                    if (isPopup)
                                        openVideoPopupWithURL(context, title, VideoMobileUrl, adUnitTag);
                                    else
                                        openVideoWithURL(context, title, VideoMobileUrl, adUnitTag);
                                } else {
                                    if (isPopup)
                                        openVideoPopupWithURL(context, title, VideoUrl, adUnitTag);
                                    else
                                        openVideoWithURL(context, title, VideoUrl, adUnitTag);
                                }
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Video yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static void openVideoDetailsWithID(final Context context, final String videoID, final String adUnitTag) {
        try {
            Class<?> c = Class.forName(SPARK_PLAYER_DETAIL_CLASS);
            Intent intent = new Intent(context, c)
                    .putExtra("videoID", videoID)
                    .putExtra("adUnitTag", adUnitTag)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openLiveStreamWithURL(final Context context, String title, String liveStream, String adUnitTag) {
        try {
            Class<?> c = Class.forName(SPARK_PLAYER_FULLSCREEN_CLASS);
            Intent intent = new Intent(context, c)
                    .putExtra("videoUrlAddress", liveStream)
                    .putExtra("AD_TAG", adUnitTag)
                    .putExtra("TITLE", title)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openAuthorsArchive(final Context context, String apiUrl, String authorID, String authorName, String authorImage) {
        try {
            Class<?> c = Class.forName(AUTHOR_ARCHIVE_LIST_ACTIVITY_CLASS);
            Intent intent = new Intent(context, c)
                    .putExtra("apiUrl", apiUrl)
                    .putExtra("authorID", authorID)
                    .putExtra("authorName", authorName)
                    .putExtra("authorImage", authorImage)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    private static void openCustomActivity(Context context, String pageName) {
        try {
            if (pageName.equals("settings")) {
                Class<?> c = Class.forName(SETTINGS_CLASS);
                Intent intent = new Intent(context, c).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openVideoDetailPage(final Context context, ArrayList<Article> articles, int position, String adUnitTag) {
        try {

            TinyDB tinydb = new TinyDB(context);
            Gson gson = new Gson();
            ArrayList<String> gsonString = new ArrayList<>();
            for (int i = 0; i < articles.size(); i++) // gson ile arraylist, json a çevrildi. Sonra tinydb ye atıldı. doğrudan gson'a atılarak gsondan parse edince ilk obje null olduğu için diğer ekranda hata veriyor
                gsonString.add(gson.toJson(articles.get(i)));
            tinydb.putListString("arrayListVideo", gsonString);
            Class<?> c = Class.forName(SPARK_PLAYER_DETAIL_CLASS);
            Intent intent = new Intent(context, c)
//                    .putExtra("videoList", articles)
//                    .putExtra("videoList", gsonString) // hata vermemesi için gönderildi.
                    .putExtra("position", position)
                    .putExtra("adUnitTag", adUnitTag)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openLastMinute(final Context context, String apiPath) {
        try {
            Class<?> c = Class.forName(LAST_MINUTE_ACTIVITY);
            Intent intent = new Intent(context, c)
                    .putExtra("apiPath", apiPath)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (ClassNotFoundException i) {
            i.printStackTrace();
        } catch (ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void openForceHome(final Context context, boolean isClickedMainLogo) {
        try {
            Class<?> c;
            if (Preferences.getBoolean(context, "isOpenCustomPage", false) && isClickedMainLogo) { // custom sayfada main logoya basarsa, custom sayfanın anasayfasına gidecek. sqğ üstteki butona basarsa corehome'a gidecek
                c = Class.forName(CUSTOM_HOME_PAGE); //istatistik atılmadı çünkü custompage oncreate'de zaten atılıyor
//                new TurkuvazAnalytic(context).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.HOME_PAGE.getEventName() + "_" + Preferences.getString(context, "customPageTitle", "")).sendEvent();
            } else {
                c = Class.forName(HOME_PAGE);
//                new TurkuvazAnalytic(context).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.HOME_PAGE.getEventName()).sendEvent();
            }
//            GlobalMethods.setIsOpenedPush(false); // anasayfaya gittiyse push kapanmış olur
            Intent intent = new Intent(context, c)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            context.startActivity(intent);
            ((Activity) context).finish();
        } catch (ClassNotFoundException | ActivityNotFoundException i) {
            i.printStackTrace();
        }
    }

    public static void shareTwitter(Context context, String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = context.getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            context.startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            context.startActivity(i);
            Toast.makeText(context, context.getResources().getString(R.string.twitter_yuklu_degil), Toast.LENGTH_LONG).show();
        }
    }

    public static void shareFacebook(Context context, String message) {
        boolean appInstalled = false;
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                appInstalled = true;
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                context.startActivity(shareIntent);
                break;
            }
        }
        if (!appInstalled) {
            Toast.makeText(context, context.getResources().getString(R.string.facebook_yuklu_degil), Toast.LENGTH_LONG).show();
        }
    }

    public static void shareWhatApp(Context context, String message) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            context.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, context.getResources().getString(R.string.whatsapp_yuklu_degil), Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareGeneral(Context context, String message, String title) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(i, context.getResources().getString(R.string.haberi_paylas)));
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("urlEncode", "UTF-8 should always be supported", e);
            return "";
        }
    }

    private static void openAnotherApp(Context context, String packageName) {
    }

    public static void openExternal(Context context, String externalURL) {
        Log.i("TAG", "openExternal: " + externalURL);
        ExternalTypes selectedNewsExternalType = getExternalType(externalURL);
        switch (selectedNewsExternalType) {
            case HOMESCREEN:
                openForceHome(context, false);
                break;
            case EMPTY:
                break;
            case ALBUM:
                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(context), ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(context), externalURL.replace(ALBUM.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(context), IconTypes.getIcon(context.getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(context, showGalleryModel, NEWS);
                break;
            case NEWS:
                GlobalIntent.openNewsWithID(context, ApiListEnums.DETAILS_ARTICLE.getApi(context), externalURL.replace(NEWS.getType(), ""));
                break;
            case VIDEO:
                GlobalIntent.openVideoWithID(context, externalURL.replace(VIDEO.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(context), "AD_TAG", false);
                break;
            case EXTERNAL:
                GlobalIntent.openExternalBrowser(context, externalURL.replace(EXTERNAL.getType(), ""));
                break;
            case INTERNAL:
                GlobalIntent.openInternalBrowser(context, externalURL.replace(INTERNAL.getType(), ""));
                break;
            case PHOTO_NEWS:
                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(context), ApplicationsWebUrls.getDomain(context.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(context), externalURL.replace(PHOTO_NEWS.getType(), ""), ApiListEnums.DETAILS_VIDEO.getApi(context), IconTypes.getIcon(context.getApplicationInfo().packageName));
                GlobalIntent.openInfinityGallery(context, showGalleryModel2, PHOTO_NEWS);
                break;
            case LIVE_STREAM:
                GlobalIntent.openLiveStreamWithURL(context, context.getResources().getString(R.string.canli_yayin), externalURL.replace(LIVE_STREAM.getType(), ""), ApiListEnums.ADS_PREROLL.getApi(context));
                break;
            case ARTICLE_SOURCE:
                GlobalIntent.openColumnistWithID(context, ApiListEnums.DETAILS_ARTICLE.getApi(context), externalURL.replace(ARTICLE_SOURCE.getType(), ""));
                break;
            case DEEPLINK:
                GlobalIntent.openAnotherApp(context, externalURL.replace(DEEPLINK.getType(), ""));
                break;
            case PAGE:
                GlobalIntent.openCustomActivity(context, externalURL.replace(PAGE.getType(), ""));
                break;
        }
    }

    public static ExternalTypes getExternalType(String externalURL) {
        return externalURL == null ? EMPTY :
                externalURL.contains(ALBUM.getType()) ? ALBUM :
                        externalURL.contains(VIDEO.getType()) ? VIDEO :
                                externalURL.contains(NEWS.getType()) ? NEWS :
                                        externalURL.contains(PHOTO_NEWS.getType()) ? PHOTO_NEWS :
                                                externalURL.contains(ARTICLE_SOURCE.getType()) ? ARTICLE_SOURCE :
                                                        externalURL.contains(LIVE_STREAM.getType()) ? LIVE_STREAM :
                                                                externalURL.contains(EXTERNAL.getType()) ? EXTERNAL :
                                                                        externalURL.contains(DEEPLINK.getType()) ? DEEPLINK :
                                                                                externalURL.contains(HOMESCREEN.getType()) ? HOMESCREEN :
                                                                                        externalURL.contains(PAGE.getType()) ? PAGE :
                                                                                                externalURL.contains(INTERNAL.getType()) ? INTERNAL : EMPTY;
    }
}