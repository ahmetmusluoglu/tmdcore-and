package turkuvaz.general.turkuvazgeneralactivitys.ColumnistDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdSize;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.AuthorArchive.AuthorsArchive;
import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.sdk.galleryslider.MediaPlayer.NativeMediaPlayer;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.global.SparkPlayerViewPopup;
import turkuvaz.sdk.global.SpecialWebView;
import turkuvaz.sdk.models.Models.AllAuthors.AllAuthorsData;
import turkuvaz.sdk.models.Models.ColumnistSlider.ColumnistData;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class ColumnistDetailActivity extends BaseGeneralActivity implements SpecialWebListener.onStartPopupVideoListener, View.OnTouchListener {
    ProgressBar mPb_loadingArticle;
    SpecialWebView mWeb_details;
    NestedScrollView nestedScrollView;
    ImageView mImg_newsTop;
    TextView mTv_title, mTv_spot;
    TextView mTv_authorName;
    TextView mTv_privacyPolicy;
    TextView mTv_date;
    TextView mTv_AllArticle, mTv_ShareArticle;
    RelativeLayout layColumnistProfil;
    RelativeLayout layColumnistProfilImage;
    private Article mArticle;
    private View lineColumnist;

    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SparkPlayerViewPopup sparkPlayerViewPopup = null;
    private static final int MAX_CLICK_DISTANCE = 15;
    private static final int MAX_CLICK_DURATION = 1000;

    private long pressStartTime;
    private int pressedX;
    private int pressedY;
    int offsetX, offsetY;
    int actionBarHeight = 0;
    private boolean allArticleBtnIsShow;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.columnist_detail_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        mWeb_details = findViewById(R.id.web_details);
        mPb_loadingArticle = findViewById(R.id.progressBar);
        mImg_newsTop = findViewById(R.id.img_newsDetailTop);
        mTv_title = findViewById(R.id.tv_newsTitle);
        mTv_spot = findViewById(R.id.tv_newsSpot);
        mTv_date = findViewById(R.id.tv_newsDate);
        mTv_privacyPolicy = findViewById(R.id.tv_columnistPrivacyPolicy);
        mTv_authorName = findViewById(R.id.tv_columnistAuthorName);
        layColumnistProfil = findViewById(R.id.layColumnistProfil);
        layColumnistProfilImage = findViewById(R.id.layColumnistProfilImage);
        mTv_AllArticle = findViewById(R.id.mTv_AllArticle);
        mTv_ShareArticle = findViewById(R.id.mTv_ShareArticle);
        lineColumnist = findViewById(R.id.lineColumnist);

        LinearLayout mLy_shareColumnist = findViewById(R.id.ly_profileShare);
        LinearLayout mLy_archiveList = findViewById(R.id.ly_archiveList);

//        mLy_archiveList.getBackground().setColorFilter(Color.parseColor("#343434"), PorterDuff.Mode.SRC_ATOP);

        // yazıyı paylaş ve tüm yazılar butonlarının arkaplanı xml'den geliyordu. Kod tarafında dinamik oluşturuldu
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[]{45, 45, 45, 45, 45, 45, 45, 45});
        shape.setColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_BUTTON_BACK.getType(), "#FFFFFF")));
        shape.setStroke(1, Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_BUTTON_EDGE.getType(), "#FFFFFF")));
        mLy_shareColumnist.setBackground(shape);
        mLy_archiveList.setBackground(shape);

        // yazar profil resmi çerçevesi için
//        GradientDrawable shapeProfil = new GradientDrawable();
//        shapeProfil.setShape(GradientDrawable.RECTANGLE);
//        shapeProfil.setCornerRadii(new float[] { 50, 50, 50, 50, 50, 50, 50, 50 });
//        shapeProfil.setColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_PROFIL_FRAME.getType(), "#FFFFFF")));
//        shapeProfil.setSize(100,100);
//        layColumnistProfilImage.setBackground(shapeProfil);

        if (Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.THEME.getType(), "dark").equals("light")) // açık temada burası siyah olacak. programatic olarak değiştirilemedi. padding verilemediği için
            layColumnistProfilImage.setBackground(getResources().getDrawable(R.drawable.shape_image_back_black));

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.columnist_empty);
//        requestOptions.error(R.drawable.columnist_empty);
//        Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load("").into(mImg_newsTop);

        // yazıyı paylaş ve tüm yazılar butonlarının text rengi
        mTv_AllArticle.setTextColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), "#FFFFFF")));
        mTv_ShareArticle.setTextColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), "#FFFFFF")));

        lineColumnist.setBackgroundColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_BOTTOM_LINE.getType(), "#FFFFFF")));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);  // yazar kartı açıldığında default olarak uygulama adını da yazıyordu arkaplanda
            layColumnistProfil.setBackgroundColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        try {
            if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                BannerAds bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bannerAds.setLayoutParams(layoutParams);
                runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLayAuthorList)).addView(bannerAds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTv_authorName.setTextColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));

        mLy_archiveList.setOnClickListener(this::openArchiveList);
        mLy_shareColumnist.setOnClickListener(this::setShareButton);

        mWeb_details.setContentType("app-columnist");

        mTv_privacyPolicy.setText(Html.fromHtml(getString(R.string.html_content_columnist)));
        mTv_privacyPolicy.setTextSize(Preferences.getInt(ColumnistDetailActivity.this, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));

        if (mTv_title != null)
            mTv_title.setTextSize(Preferences.getInt(ColumnistDetailActivity.this, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25));

        if (mTv_spot != null)
            mTv_spot.setTextSize(Preferences.getInt(ColumnistDetailActivity.this, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25) - 6);

        if (mWeb_details != null)
            mWeb_details.getSettings().setDefaultFontSize(Preferences.getInt(ColumnistDetailActivity.this, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("selectedNews")) {
            mArticle = new Gson().fromJson(bundle.getString("selectedNews"), Article.class);
//            mArticle = bundle.getParcelable("selectedNews");

            allArticleBtnIsShow = bundle.getBoolean("allArticleBtnIsShow");
            if (!allArticleBtnIsShow) // daha önce tüm yazılarını listeleyerek yazar kartına girildiyse, artık tüm yazıları butonuna gerek kalmayacak. Alaattin abi isteğiyle yapıldı
                mLy_archiveList.setVisibility(View.GONE);

            if (mArticle == null)
                return;

            /** SEND ANALYTICS */
            new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.COLUMNIST_DETAILS.getEventName()).sendEvent();

            /** SET ADS */
            if (!ApiListEnums.ADS_GENERAL_320x142.getApi(this).equals("")) {
                BannerAds topAds = new BannerAds(this, new AdSize(320, 142), ApiListEnums.ADS_GENERAL_320x142.getApi(this));
                ((FrameLayout) findViewById(R.id.frame_columnistTopBanner)).addView(topAds);
            }

            /** SET ADS */
            if (!ApiListEnums.ADS_GENERAL_300x250.getApi(this).equals("")) {
                BannerAds bannerAds = new BannerAds(this, new AdSize(300, 250), ApiListEnums.ADS_GENERAL_300x250.getApi(this));
                ((FrameLayout) findViewById(R.id.frame_columnistBottomBanner)).addView(bannerAds);

                if (Preferences.getBoolean(ColumnistDetailActivity.this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(ColumnistDetailActivity.this).equals(""))
                    ((FrameLayout) findViewById(R.id.frame_columnistBottomBanner)).setPadding(bannerAds.getPaddingLeft(), bannerAds.getPaddingTop(), bannerAds.getPaddingRight(), (int) (50 * getResources().getDisplayMetrics().density));
            }

            if (mArticle.getSource() != null)
                mTv_authorName.setText(mArticle.getSource());

            mTv_title.setText(mArticle.getTitle());
            if (mArticle.getSpot() != null && !mArticle.getSpot().equals("")) // FOTOMAC-8587
                mTv_spot.setText(mArticle.getSpot());
            else
                mTv_spot.setVisibility(View.GONE);
            if (mArticle.getOutputDate() != null && !TextUtils.isEmpty(mArticle.getOutputDate())) // TAKVIM-5561
                mTv_date.setText(String.valueOf(mArticle.getOutputDate().trim()));
            else
                mTv_date.setText(String.valueOf(convertDate(mArticle.getCreatedDate())));

            if (mArticle.getExternal() == null || mArticle.getExternal().equals("")) // manşette yazar varsa, tıklandığında external boş geliyor. bunları ve description u almak için alttaki else'ye girmeli. eğer varsa olanı almalı. diğer şartlarda external dolu geliyor.
                makaleAc();
            else {
                // article id ile api'ye istek atılıyor ve yazının detayı getiriliyor ve bu detaydan description alınıyor.
                ApiClient.getClientApi(ColumnistDetailActivity.this).create(RestInterface.class).getArticleById(ApiListEnums.DETAILS_COLUMNIST.getApi(this), mArticle.getExternal().replace("articlesource://", "")).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ColumnistData>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(ColumnistData authorsModel) {
                                try {
                                    if (authorsModel != null) {
                                        // headline'dan tıklanıp da eksik olan alanlar set ediliyor
                                        mArticle.setDescription(authorsModel.getData().getColumnists().getResponse().get(0).getDescription());
                                        mArticle.setArticleSourceId(authorsModel.getData().getColumnists().getResponse().get(0).getArticleSourceId());
                                        mArticle.setSource(authorsModel.getData().getColumnists().getResponse().get(0).getSource());
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e != null)
                                    GlobalMethods.onReportErrorToFirebase(ColumnistDetailActivity.this, e, "ColumnistDetailActivity-ArticleById");
//                            onStatusListener.onError(e.getMessage());
//                                Toast.makeText(ColumnistDetailActivity.this, "Yazı yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
//                        if (onStatusListener != null)
//                            onStatusListener.onSuccess();
                                makaleAc();
                            }
                        });
            }
        }
    }

    public void setShareButton(View view) {
        try {
            if (mArticle != null && mArticle.getTitle() != null && !TextUtils.isEmpty(mArticle.getTitle()) && mArticle.getUrl() != null && !TextUtils.isEmpty(mArticle.getUrl())) {

                new TurkuvazAnalytic(this).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.ACTION_COLUMNIST_SHARE.getEventName()).sendEvent();

                String urlWithDomain = ApplicationsWebUrls.getDomain(getApplicationInfo().packageName) + mArticle.getUrl();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, mArticle.getSource());
                intent.putExtra(Intent.EXTRA_TEXT, mArticle.getTitle() + "\n" + urlWithDomain);
                startActivity(Intent.createChooser(intent, "Yazıyı Paylaş"));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makaleAc() {
        // her şekilde primaryImage'yi almak için tüm yazarlardan çeksin (sadece articlesource'ler için)
        if (mArticle.getPrimaryImage() != null && !TextUtils.isEmpty(mArticle.getPrimaryImage()))
            Glide.with(ColumnistDetailActivity.this.getApplicationContext()).load(mArticle.getPrimaryImage()).into(mImg_newsTop);
//        } else {
        ApiClient.getClientApi(ColumnistDetailActivity.this).create(RestInterface.class).getAllAuthors(ApiListEnums.ALL_AUTHORS.getApi(ColumnistDetailActivity.this)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllAuthorsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @SuppressLint("CheckResult")
                    @Override
                    public void onNext(AllAuthorsData allAuthorsData) {
                        try {
                            if (mArticle != null && mArticle.getArticleSourceId() != null) {
                                for (int i = 0; i < allAuthorsData.getData().getAuthors().getResponse().size(); i++) {
                                    if (allAuthorsData.getData().getAuthors().getResponse().get(i).getId().equals(mArticle.getArticleSourceId())) {
                                        mArticle.setPrimaryImage(allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage());
                                        break; // bulunca devam etmesin
                                    }
                                }

                                   /* if (mArticle.getPrimaryImage() != null && !TextUtils.isEmpty(mArticle.getPrimaryImage())) {
                                        Glide.with(ColumnistDetailActivity.this.getApplicationContext()).load(mArticle.getPrimaryImage()).into(mImg_newsTop);
                                    }*/
                            } // articlesourceid boş gelse de resmi göstersin.
                            if (mArticle != null && mArticle.getPrimaryImage() != null && !TextUtils.isEmpty(mArticle.getPrimaryImage()))
                                Glide.with(ColumnistDetailActivity.this.getApplicationContext()).load(mArticle.getPrimaryImage()).into(mImg_newsTop);

//                            RequestOptions requestOptions = new RequestOptions();
//                            requestOptions.placeholder(R.drawable.columnist_empty);
//                            requestOptions.error(R.drawable.columnist_empty);
//                            Glide.with(getApplicationContext()).setDefaultRequestOptions(requestOptions).load(mArticle.getPrimaryImage()).into(mImg_newsTop);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null)
                            GlobalMethods.onReportErrorToFirebase(ColumnistDetailActivity.this, e, ColumnistDetailActivity.this.getClass().getSimpleName());

                    }

                    @Override
                    public void onComplete() {
                        mWeb_details.setArticle(mArticle);

                        initCollapsingToolbar();

                        mWeb_details.loadNews();
                        SpecialWebListener specialWebListener = new SpecialWebListener(ColumnistDetailActivity.this, mPb_loadingArticle, ColumnistDetailActivity.this);
                        mWeb_details.setOnSelectedURL(specialWebListener);
//                        mWeb_details.setNestedScrollingEnabled(true);
                    }
                });
//            }


//        }
    }

    public void openArchiveList(View view) {
        if (mArticle != null && /*mArticle.getArticleSourceId() != null &&*/ mArticle.getSource() != null && mArticle.getPrimaryImage() != null && !TextUtils.isEmpty(mArticle.getPrimaryImage()))
            GlobalIntent.openAuthorsArchive(ColumnistDetailActivity.this, ApiListEnums.COLUMNIST_BY_SOURCE.getApi(this), mArticle.getArticleSourceId(), mArticle.getSource(), mArticle.getPrimaryImage());
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        collapsingToolbar.setContentScrimColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
        collapsingToolbar.setCollapsedTitleTextAppearance(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.THEME.getType(), "dark").equals("dark") ? R.style.TextAppearance_ColumnistCollapseTitleDark : R.style.TextAppearance_ColumnistCollapseTitleLight);
        appBarLayout.setBackgroundColor(Color.parseColor(Preferences.getString(ColumnistDetailActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mArticle != null && mArticle.getSource() != null && mArticle.getTitle() != null)
                        collapsingToolbar.setTitle(mArticle.getSource() + " - " + mArticle.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    String convertDate(String irregularDate) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM, yyyy", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cleanDate;
    }

    public void openPrivacyPolicy(View view) {
        GlobalIntent.openInternalBrowser(this, ApiListEnums.ARTICLE_PRIVACY_POLICY.getApi(this));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onPopupStart(String videoUrl) {
        try {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }

            TypedValue tv = new TypedValue();
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
            if (resourceId > 0) {
                actionBarHeight += getResources().getDimensionPixelSize(resourceId);
            }

            if (GlobalMethods.isActiveNativePlayer()) {
                LayoutInflater layoutInflater = LayoutInflater.from(ColumnistDetailActivity.this);
                popupView = layoutInflater.inflate(R.layout.popup_nativ_player, null);
                popupWindow = new PopupWindow(popupView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                NativeMediaPlayer nativeMediaPlayer = popupView.findViewById(R.id.nativeMediaPlayerPopup);
                popupView.findViewById(R.id.btn_sparkPopupClose).setOnClickListener(view -> popupWindow.dismiss());
                nativeMediaPlayer.setActivity(ColumnistDetailActivity.this);

                nativeMediaPlayer.setVideoURL(videoUrl);
                nativeMediaPlayer.init();

                nativeMediaPlayer.setOnFullScreenChange(isFullScreen -> {
                    try {
                        if (isFullScreen) {
                            ColumnistDetailActivity.this.getSupportActionBar().hide();
                            popupView.findViewById(R.id.frameToolbar).setVisibility(View.GONE);
                            popupView.setBackgroundColor(Color.WHITE);
                        } else {
                            ColumnistDetailActivity.this.getSupportActionBar().show();
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
                            if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(ColumnistDetailActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
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
                LayoutInflater layoutInflater = LayoutInflater.from(ColumnistDetailActivity.this);
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

            if (popupWindow != null)
                popupWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, actionBarHeight);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (pressDuration < MAX_CLICK_DURATION && GlobalMethods.distance(ColumnistDetailActivity.this, pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
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

    @Override
    protected void onPause() {
        if (popupView != null) {
            if (sparkPlayerViewPopup != null)
                sparkPlayerViewPopup.closeSpark();
            popupWindow.dismiss();
            popupWindow = null;
            popupView = null;
            sparkPlayerViewPopup = null;
        }
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}