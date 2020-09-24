package turkuvaz.sdk.global;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;


/**
 * Created by turgay.ulutas on 21/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class SpecialWebView extends WebView {

    Article mArticle;
    String mReadyContent;
    SpecialClient myWebViewClient;
    int WebViewTextSize = 16;
    private String mCssUrl = "";
    private String contentType;
    private VideoEnabledWebChromeClient videoEnabledWebChromeClient;
    private boolean addedJavascriptInterface;

    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void notifyVideoEnd() {
            new Handler(Looper.getMainLooper()).post(() -> {
                if (videoEnabledWebChromeClient != null) {
                    videoEnabledWebChromeClient.onHideCustomView();
                }
            });
        }
    }

    public boolean isVideoFullscreen() {
        return videoEnabledWebChromeClient != null && videoEnabledWebChromeClient.isVideoFullscreen();
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void setWebChromeClient(WebChromeClient client) {
        getSettings().setJavaScriptEnabled(true);
        if (client instanceof VideoEnabledWebChromeClient) {
            this.videoEnabledWebChromeClient = (VideoEnabledWebChromeClient) client;
        }

        super.setWebChromeClient(client);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        addJavascriptInterface();
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        addJavascriptInterface();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    private void addJavascriptInterface() {
        if (!addedJavascriptInterface) {
            addJavascriptInterface(new JavascriptInterface(), "_VideoEnabledWebView");
            addedJavascriptInterface = true;
        }
    }

    public SpecialWebView(Context context) {
        super(context);
        this.mCssUrl = ApiListEnums.CONTENT_CSS.getApi(context) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(context);
        addedJavascriptInterface = false;
        initView();
    }

    public SpecialWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCssUrl = ApiListEnums.CONTENT_CSS.getApi(context) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(context);
        addedJavascriptInterface = false;
        initView();
    }

    public void setOnSelectedURL(SpecialClient.onSelectedURLListener _onSelectedURL) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        myWebViewClient = new SpecialClient();
        myWebViewClient.setArticle(mArticle);
        myWebViewClient.setOnSelectedURLListener(_onSelectedURL);
        this.setWebViewClient(myWebViewClient);
    }

    public void setArticle(Article article) {
        this.mArticle = article;
    }

    public void loadNews() {
        addJavascriptInterface();
        createStyle();
        loadDataWithBaseURL(null, mReadyContent, "text/html", "UTF-8", null);
        mReadyContent = "";
    }

    private void createStyle() {
        if (mArticle == null)
            return;

        String mPhotoNewsDescription = null;

        if (mArticle.getExternal() == null)
            mArticle.setExternal("");
        if (mArticle.getUseDetailPaging() != null && mArticle.getUseDetailPaging() && mArticle.getExternal().equals("")) {
            try {
                StringBuilder sb = new StringBuilder();
                Gson gson = new Gson();
                String jsonOutput = mArticle.getDescription();
                Type listType = new TypeToken<List<String>>() {
                }.getType();

                List<String> posts = gson.fromJson(jsonOutput, listType);
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

        mReadyContent = getContext().getString(R.string.html_content, style, contentType,
                (mArticle.getUseDetailPaging() != null && mArticle.getUseDetailPaging() && mArticle.getExternal().equals("") ? mPhotoNewsDescription : mArticle.getDescription()));

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        this.clearCache(true);
        this.clearHistory();
        this.clearFormData();
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        try {
            WebSettings settings = this.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            settings.setDefaultFontSize(WebViewTextSize);
            settings.setAppCacheEnabled(false);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            settings.setGeolocationEnabled(false);
            settings.setNeedInitialFocus(false);
            settings.setSaveFormData(false);
            settings.setDefaultFontSize(Preferences.getInt(getContext(), SettingsTypes.NEWS_WEBVIEW_FONT_SIZE.getType(), 16));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}