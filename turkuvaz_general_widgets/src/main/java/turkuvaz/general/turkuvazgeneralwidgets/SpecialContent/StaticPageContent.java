package turkuvaz.general.turkuvazgeneralwidgets.SpecialContent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.StaticPages.StaticPageData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class StaticPageContent extends FrameLayout {

    private WebView webView;
    private String url;
    private boolean isDataModel;
    private RestInterface mRestInterface;
    private ProgressBar progressLoading;

    public StaticPageContent(Context context) {
        super(context);
    }

    public StaticPageContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticPageContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.special_content_view, this, true);

        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        webView = findViewById(R.id.web_specialContent);
        boolean isMinikaApp = getContext().getPackageName().contains("minikatv");
        findViewById(R.id.imgAppLogo).setVisibility(isMinikaApp ? VISIBLE : GONE);
        progressLoading = findViewById(R.id.progressLoading);

        initWebView();
        if (isDataModel) {
            setWebViewWithData();
        } else {
            webView.loadUrl(url);
        }

        /** SEND ANALYTICS */
        new TurkuvazAnalytic(getContext()).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.BROWSER_PAGE.getEventName()).sendEvent();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void seDataModel(boolean isDataModel) {
        this.isDataModel = isDataModel;
    }

    private void setWebViewWithData() {
        mRestInterface.getStaticPages(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StaticPageData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(StaticPageData staticPageData) {
                        try {
                            if (staticPageData.getMeta().getStatusCode() == 200 && staticPageData.getData() != null) {
                                String mCssUrl = ApiListEnums.CONTENT_CSS.getApi(getContext()) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(getContext());
                                String style = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + mCssUrl + "\">";
                                String mReadyContent = getContext().getString(R.string.html_content, style, "app-static", staticPageData.getData().getStatikPage().getResponse().getDescription());
                                webView.loadData(mReadyContent, "text/html; charset=UTF-8", null);
                            }
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

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(getContext()));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressLoading.setVisibility(VISIBLE);
                webView.setVisibility(GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith("veri-politikasi")) {
                    GlobalIntent.openInternalBrowser(getContext(), ApiListEnums.ARTICLE_PRIVACY_POLICY.getApi(getContext()));
                } else {
                    webView.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressLoading.setVisibility(View.GONE);
                webView.setVisibility(VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressLoading.setVisibility(View.GONE);
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setGeolocationEnabled(false);
        webView.getSettings().setNeedInitialFocus(false);
        webView.getSettings().setSaveFormData(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}