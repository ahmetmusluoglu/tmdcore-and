package turkuvaz.sdk.global;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ARTICLE_SOURCE;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EMPTY;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.IMAGE;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.INTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.VIDEO;

/**
 * Created by turgay.ulutas on 24/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class SpecialClient extends WebViewClient {

    private onSelectedURLListener onSelectedURL;
    private Article article;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null && !TextUtils.isEmpty(url)) {
            doSomething(url);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.getUrl() != null && !TextUtils.isEmpty(request.getUrl().toString())) {
            String url = request.getUrl().toString();
            doSomething(url);
        }
        return true;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    private void doSomething(String url) {
        switch (checkReturnType(url)) {
            case EMPTY:
                Log.e("EMPTY", "EMPTY");
                break;
            case ALBUM:
                onSelectedURL.onAlbum(url.replace(ALBUM.getType(), ""));
                break;
            case NEWS:
                onSelectedURL.onNews(url.replace(NEWS.getType(), ""));
                break;
            case VIDEO:
                onSelectedURL.onVideo(url.replace(VIDEO.getType(), ""), "None", "None");
                break;
            case EXTERNAL:
                onSelectedURL.onExternal(url.replace(EXTERNAL.getType(), ""));
                break;
            case INTERNAL:
                onSelectedURL.onInternal(url.replace(INTERNAL.getType(), ""));
                break;
            case PHOTO_NEWS:
                onSelectedURL.onPhotoNews(url.replace(PHOTO_NEWS.getType(), ""), article.getTitle(), article.getSpot(), article.getUrl());
                break;
            case LIVE_STREAM:
                onSelectedURL.onLiveStream(url.replace(LIVE_STREAM.getType(), ""));
                break;
            case ARTICLE_SOURCE:
                onSelectedURL.onArticle(url.replace(ARTICLE_SOURCE.getType(), ""));
                break;
            case IMAGE:
                onSelectedURL.onImage(url.replace(IMAGE.getType(), ""));
                break;
        }
    }

    public void setOnSelectedURLListener(onSelectedURLListener _onSelectedURLListener) {
        onSelectedURL = _onSelectedURLListener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
            onSelectedURL.onLoad();
    }

    public interface onSelectedURLListener {
        void onVideo(String videoURL, String videoCategory, String videoTitle);

        void onNews(String newsID);

        void onAlbum(String albumID);

        void onPhotoNews(String photoNewsID, String title, String spot, String url);

        void onArticle(String articleSourceID);

        void onExternal(String externalURL);

        void onInternal(String internalURL);

        void onLoad();

        void onImage(String imageURL);

        void onLiveStream(String liveStreamID);
    }

    private ExternalTypes checkReturnType(String url) {
        return url == null ? EMPTY :
                url.contains(ALBUM.getType()) ? ALBUM :
                        url.contains(VIDEO.getType()) ? VIDEO :
                                url.contains(NEWS.getType()) ? NEWS :
                                        url.contains(PHOTO_NEWS.getType()) ? PHOTO_NEWS :
                                                url.contains(ARTICLE_SOURCE.getType()) ? ARTICLE_SOURCE :
                                                        url.contains(LIVE_STREAM.getType()) ? LIVE_STREAM :
                                                                url.contains(EXTERNAL.getType()) ? EXTERNAL :
                                                                        url.contains(INTERNAL.getType()) ? INTERNAL :
                                                                                url.contains(IMAGE.getType()) ? IMAGE : EMPTY;
    }
}