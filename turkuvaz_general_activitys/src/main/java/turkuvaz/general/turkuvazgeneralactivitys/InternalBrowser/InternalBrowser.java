package turkuvaz.general.turkuvazgeneralactivitys.InternalBrowser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.GdprDialog.GdprDialog;
import turkuvaz.general.turkuvazgeneralwidgets.RateApp.RateApp;
import turkuvaz.general.turkuvazgeneralwidgets.SuccessOrErrorBar.LoadStatus;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.ConfigBase.RateThisApp;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.models.Models.StaticPages.StaticPageData;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import android.webkit.WebSettings;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InternalBrowser extends BaseGeneralActivity {

    private String url;
    private boolean isDataModel;
    private WebView webView;
    private RestInterface mRestInterface;
    private String contentType;
    private LinearLayout llLoadStatusRoot;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private static final int REQUEST_GET_THE_THUMBNAIL = 4000;
    private final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;
    String mCurrentPhotoPath = "";
    final CharSequence[] items = {"Resim Yükle", "Fotoğraf Çek"};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internal_browser_main);

        this.mRestInterface = ApiClient.getClientApi(InternalBrowser.this).create(RestInterface.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        llLoadStatusRoot = findViewById(R.id.llLoadStatusRoot);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbar arkaplan rengi
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(InternalBrowser.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
        // back butonu rengi
        DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(InternalBrowser.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

        getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(InternalBrowser.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));

        url = getIntent().getStringExtra("url");
        isDataModel = getIntent().getBooleanExtra("isDataModel", false);

        if (TextUtils.isEmpty(url)) {
            finish();
        }

        webView = findViewById(R.id.webView);
//        progressBar = findViewById(R.id.progressBar);
        initWebView();

        if (isDataModel) {
            setWebViewWithData();
        } else {
            webView.loadUrl(url);
            // küçük ilanlar app'inde homeactivity kapanıp doğrudan webview açıldığı için gdpr popup'ı görünmüyordu. browser'da görünecek şekilde ayarlandı

        }

        /** SEND ANALYTICS */
        new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.BROWSER_PAGE.getEventName()).sendEvent();
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
                                String mCssUrl = ApiListEnums.CONTENT_CSS.getApi(InternalBrowser.this) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(InternalBrowser.this);
                                String style = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + mCssUrl + "\">";
                                String mReadyContent = getString(R.string.html_content, style, "app-static", staticPageData.getData().getStatikPage().getResponse().getDescription());
                                webView.loadData(mReadyContent, "text/html; charset=UTF-8", null);
                            }
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

    @SuppressLint("ClickableViewAccessibility")
    private void initWebView() {

        webView.setWebChromeClient(new MyWebChromeClient(this));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                llLoadStatusRoot.setVisibility(View.VISIBLE);
                invalidateOptionsMenu();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + "/") || url.equals(ApplicationsWebUrls.getDomain(getApplicationInfo().packageName))) {
                    finish();
                } else if (url.endsWith("veri-politikasi")) {
                    GlobalIntent.openInternalBrowser(InternalBrowser.this, ApiListEnums.ARTICLE_PRIVACY_POLICY.getApi(InternalBrowser.this));
                } else {
                    webView.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                llLoadStatusRoot.setVisibility(View.GONE);
//                loadStatus.loadStatusSuccess();
                invalidateOptionsMenu();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                llLoadStatusRoot.setVisibility(View.GONE);
//                loadStatus.loadStatusError("sayfa yüklenirken hata oluştu.");
                invalidateOptionsMenu();
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // webview debug için eklendi
//            WebView.setWebContentsDebuggingEnabled(true);
    }

    private WebChromeClient getChromeClient() {
        return new WebChromeClient() {
            //3.0++
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooserImpl(uploadMsg);
            }

            //3.0--
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooserImpl(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            // For Android > 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, WebChromeClient.FileChooserParams fileChooserParams) {
                openFileChooserImplForAndroid5(uploadMsg);
                return true;
            }
        };
    }

    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Dosya Seç"), FILECHOOSER_RESULTCODE);
//        new AlertDialog.Builder(InternalBrowser.this)
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        if (items[which].equals(items[0])) {
//                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                        i.addCategory(Intent.CATEGORY_OPENABLE);
//                        i.setType("image/*");
//                        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
////                        } else if (items[which].equals(items[1])) {
////                            dispatchTakePictureIntent();
////                        }
//                        dialog.dismiss();
//                    }
//                })
//                .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        mUploadMessage = null;
//                        dialog.dismiss();
//                    }
//                })
//                .show();
    }

    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        mUploadMessageForAndroid5 = uploadMsg;

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Resim Seç");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);

//        new AlertDialog.Builder(InternalBrowser.this)
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        if (items[which].equals(items[0])) {
//                            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//                            contentSelectionIntent.setType("image/*");
//
//                            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//                            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//                            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Resim Seç");
//
//                            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
////                        } else if (items[which].equals(items[1])) {
////                            dispatchTakePictureIntent();
////                        }
//                        dialog.dismiss();
//                    }
//                })
//                .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        //important to return new Uri[]{}, when nothing to do. This can slove input file wrok for once.
//                        //InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
//                        mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
//                        mUploadMessageForAndroid5 = null;
//                        dialog.dismiss();
//                    }}).show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(InternalBrowser.this.getPackageManager()) != null) {
//            File file = new File(createImageFile());
            Uri imageUri = null;
            try {
                imageUri = Uri.fromFile(createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //temp sd card file
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_GET_THE_THUMBNAIL);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // Create an image file name
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/kucuk_ilanlar_img/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath(); // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result;

            if (intent == null || resultCode != Activity.RESULT_OK) {
                result = null;
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                mUploadMessageForAndroid5 = null;
                mUploadMessage = null;
            } else {
                result = intent.getData();
            }

            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                if (mUploadMessageForAndroid5 != null)
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        } else if (requestCode == REQUEST_GET_THE_THUMBNAIL) {
            if (resultCode == Activity.RESULT_OK) {
                File file = new File(mCurrentPhotoPath);
                Uri localUri = Uri.fromFile(file);
                Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                sendBroadcast(localIntent);

                Uri result = Uri.fromFile(file);
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                mUploadMessageForAndroid5 = null;
            } else {
                File file = new File(mCurrentPhotoPath);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.internal_browser_menu, menu);
        MenuItem item1 = menu.findItem(R.id.action_back);
        MenuItem item2 = menu.findItem(R.id.action_forward);

        DrawableCompat.setTint(item1.getIcon(), Color.parseColor(Preferences.getString(InternalBrowser.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
        DrawableCompat.setTint(item2.getIcon(), Color.parseColor(Preferences.getString(InternalBrowser.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!webView.canGoBack()) {
            menu.getItem(0).setEnabled(false);
            menu.getItem(0).getIcon().setAlpha(130);
        } else {
            menu.getItem(0).setEnabled(true);
            menu.getItem(0).getIcon().setAlpha(255);
        }

        if (!webView.canGoForward()) {
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).getIcon().setAlpha(130);
        } else {
            menu.getItem(1).setEnabled(true);
            menu.getItem(1).getIcon().setAlpha(255);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if (item.getItemId() == R.id.action_back) {
            back();
        }

        if (item.getItemId() == R.id.action_forward) {
            forward();
        }

        return super.onOptionsItemSelected(item);
    }

    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private void forward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

    // fikriyatta kuran dinlerken çıkıldığında arkaplanda çalmaya devam ediyordu
    @Override
    public void onPause() {
        webView.onPause();
        webView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }


    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
    }

    private void checkGdprKvkk() {
        boolean isGdprActive = Preferences.getBoolean(this, ApiListEnums.GDPR_IS_ACTIVE.getType(), false);
        boolean isKvkkActive = Preferences.getBoolean(this, ApiListEnums.KVKK_IS_ACTIVE.getType(), false);
        String privacyPolicyUrl = Preferences.getString(this, ApiListEnums.PRIVACY_POLICY_URL.getType(), "");
        String privacyPolicyTitle = Preferences.getString(this, ApiListEnums.PRIVACY_POLICY_TITLE.getType(), "");

        if (!isGdprActive && !isKvkkActive) {

        } else {
            if (privacyPolicyUrl != null && !TextUtils.isEmpty(privacyPolicyUrl)) {
                GdprDialog gdprDialog = new GdprDialog(this, "168", privacyPolicyUrl, privacyPolicyTitle, 1, 1, isGdprActive, isKvkkActive, false);
                if (!gdprDialog.isShowing())
                    gdprDialog.create();
            }
        }
    }

    private void checkRateApp() {

    }
}