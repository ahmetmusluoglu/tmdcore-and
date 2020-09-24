package turkuvaz.general.turkuvazgeneralwidgets.SpecialContent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import turkuvaz.general.turkuvazgeneralwidgets.R;

/**
 * -1 ise device genişlik * yükseklik
 * else
 * ekran genişliğine oranla
 */

public class SpecialContent extends FrameLayout {

    private String data;
    private WebView mSpecialContent;
    private boolean isIframe;

    public SpecialContent(Context context) {
        super(context);
    }

    public SpecialContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpecialContent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.special_content_view, this, true);
        mSpecialContent = findViewById(R.id.web_specialContent);

        mSpecialContent.clearCache(true);
        mSpecialContent.clearHistory();
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        WebSettings settings = mSpecialContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(false);
        settings.setGeolocationEnabled(false);
        settings.setNeedInitialFocus(false);
        settings.setSaveFormData(false);
        settings.setLoadWithOverviewMode(true);

//        mSpecialContent.setWebViewClient(new WebViewClient() // küçük ilanlar için yazıldı, internalbrowser olacağı için askıya alındı
//        {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url)
//            {
//                return false;
//            }
//        });

        mSpecialContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (isIframe) {
            mSpecialContent.loadData(data, "text/html; charset=UTF-8", null);
        } else {
            mSpecialContent.loadUrl(data);
        }
    }

    public void setData(String data) {
        isIframe = !data.startsWith("http");
        if (data.startsWith("http")) {
            this.isIframe = false;
            this.data = data;
        } else {
            this.isIframe = true;
            this.data = getContext().getString(R.string.html_content, "", "", data);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) { // küçük ilanlar için yazıldı, internalbrowser olacağı için askıya alındı
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (mSpecialContent.canGoBack()) {
//                        mSpecialContent.goBack();
//                    } else {
////                        super.onBackPressed();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onBackPressed() {
//        if (mSpecialContent.canGoBack()) {
//            mSpecialContent.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    //TODO CONFIG AYARLARINA BAKILACAK
    public void setSize(int height, int width) {
        if (width == -1)
            mSpecialContent.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height));
        else
            mSpecialContent.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height));
    }
}