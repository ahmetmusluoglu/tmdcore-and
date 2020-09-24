package turkuvaz.general.turkuvazgeneralactivitys.AuthorArchive;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdSize;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralactivitys.ColumnistDetail.ColumnistDetailActivity;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.AllAuthors.AllAuthorsData;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.AuthorsArchive.AuthorsArchiveData;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class AuthorsArchive extends BaseGeneralActivity implements AuthorsArchiveAdapter.OnLoadMoreListener {
    private RestInterface mRestInterface;
    private String mApiUrl, mAuthorID, mAuthorName, mAuthorImageURL;
    private ArrayList<Article> mResponse = new ArrayList<>();
    private int mPageIndex = 1;
    private AuthorsArchiveAdapter mAdapter;
    private RecyclerView mRec_archiveList;
    private CircleImageView mImg_tAuthorArchivePhoto;
    private TextView mTv_authorName;
    private ProgressBar mPb_authorArchiveLoading;
    private RelativeLayout layAuthorProfil, layColumnistProfilImage;
    private View lineColumnist;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase,""));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_archive_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey("apiUrl") || !bundle.containsKey("authorID") || !bundle.containsKey("authorName") || !bundle.containsKey("authorImage")) {
            Toast.makeText(this, "Bir hata oluştu.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            mApiUrl = bundle.getString("apiUrl");
            mAuthorID = bundle.getString("authorID");
            mAuthorName = bundle.getString("authorName");
            mAuthorImageURL = bundle.getString("authorImage");
            /** SEND ANALYTICS */
            new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.COLUMNIST_ARCHIVE.getEventName()).sendEvent();
        }

        this.mRestInterface = ApiClient.getClientApi(AuthorsArchive.this).create(RestInterface.class);
        mRec_archiveList = findViewById(R.id.rec_authorsArchiveList);
        mRec_archiveList.setLayoutManager(new LinearLayoutManager(this));
        mImg_tAuthorArchivePhoto = findViewById(R.id.img_columnistAuthorArchivePhoto);
        mTv_authorName = findViewById(R.id.tv_columnistAuthorArchiveName);
        mPb_authorArchiveLoading = findViewById(R.id.pb_authorArchiveLoading);
        layAuthorProfil = findViewById(R.id.layAuthorProfil);
        layColumnistProfilImage = findViewById(R.id.layColumnistProfilImage);
        lineColumnist = findViewById(R.id.lineColumnist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // yazar kartı açıldığında default olarak uygulama adını da yazıyordu arkaplanda
            layAuthorProfil.setBackgroundColor(Color.parseColor(Preferences.getString(AuthorsArchive.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(AuthorsArchive.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        try {
            if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                BannerAds bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                bannerAds.setLayoutParams(layoutParams);
                runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLayAuthorArchive)).addView(bannerAds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Preferences.getString(AuthorsArchive.this, ApiListEnums.THEME.getType(), "dark").equals("light")) // açık temada burası siyah olacak. programatic olarak değiştirilemedi.
            layColumnistProfilImage.setBackground(getResources().getDrawable(R.drawable.shape_image_back_black));

        lineColumnist.setBackgroundColor(Color.parseColor(Preferences.getString(AuthorsArchive.this, ApiListEnums.COLUMNIST_BOTTOM_LINE.getType(), "#FFFFFF")));

        initCollapsingToolbar();

        if (mAuthorImageURL != null && !TextUtils.isEmpty(mAuthorImageURL)) {
            Glide.with(this).load(mAuthorImageURL).into(mImg_tAuthorArchivePhoto);
        }else {
            mImg_tAuthorArchivePhoto.setImageResource(R.drawable.columnist_empty);
        }
       /* RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.columnist_empty);
        requestOptions.error(R.drawable.columnist_empty);

        Glide.with(getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mAuthorImageURL).into(mImg_tAuthorArchivePhoto);*/

//        if (Preferences.getString(AuthorsArchive.this, ApiListEnums.THEME.getType(), "dark").equals("light")) // açık temada burası siyah olacak. programatic olarak değiştirilemedi.
//            mImg_tAuthorArchivePhoto.setBackground(getResources().getDrawable(R.drawable.shape_image_back_black));

        mTv_authorName.setText(mAuthorName);
        mTv_authorName.setTextColor(Color.parseColor(Preferences.getString(AuthorsArchive.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));

        getNewsList(mPageIndex);
    }

    private void getNewsList(final int pageIndex) {
        mRestInterface.getAuthorsArchive(mApiUrl, mAuthorID, pageIndex == 1 ? null : pageIndex).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthorsArchiveData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPb_authorArchiveLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(AuthorsArchiveData newsListData) {
                        try {
                            if (pageIndex == 1) {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getColumnists() != null && newsListData.getData().getColumnists().getResponse() != null) {
                                    mResponse.addAll(newsListData.getData().getColumnists().getResponse());

                                    // spinner a tıklandığında arşiv açılacağı için artık burada primaryımage kontrolü yapılacak
                                    if (mResponse != null && mResponse.size() > 0 && (mResponse.get(0).getPrimaryImage() == null || mResponse.get(0).getPrimaryImage().equals("")))
                                        primaryImageAl(mResponse);

                                    mAdapter = new AuthorsArchiveAdapter(mResponse, AuthorsArchive.this);
                                    mAdapter.setOnLoadMoreListener(AuthorsArchive.this);
//                                    mAdapter.setSelectedListener((selectedNews, position, clickViewType) -> GlobalIntent.openColumnistWithArticle(AuthorsArchive.this, selectedNews.get(position), false));
                                    mAdapter.setSelectedListener((selectedNews, position, clickViewType) -> GlobalIntent.openColumnistWithArticle(AuthorsArchive.this, selectedNews.get(position), false));
                                    mRec_archiveList.setAdapter(mAdapter);

                                    if (Preferences.getBoolean(AuthorsArchive.this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(AuthorsArchive.this).equals(""))
                                        mRec_archiveList.setPadding(mRec_archiveList.getPaddingLeft(), mRec_archiveList.getPaddingTop(), mRec_archiveList.getPaddingRight(), (int) (50 * getResources().getDisplayMetrics().density));
                                }
                            } else {
                                if (newsListData != null && newsListData.getData() != null && newsListData.getData().getColumnists() != null && newsListData.getData().getColumnists().getResponse() != null) {
                                    ArrayList<Article> mPageList = newsListData.getData().getColumnists().getResponse();
                                    mAdapter.notifyDataSet(mPageList, mPageList.size());
                                }
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (e != null)
                            GlobalMethods.onReportErrorToFirebase(AuthorsArchive.this, e, "AuthorsArchive-NewsList");

                    }

                    @Override
                    public void onComplete() {
                        mPb_authorArchiveLoading.setVisibility(View.GONE);
                    }
                });
    }

    public void primaryImageAl(ArrayList<Article> mResponse) {
        ApiClient.getClientApi(AuthorsArchive.this).create(RestInterface.class).getAllAuthors(ApiListEnums.ALL_AUTHORS.getApi(AuthorsArchive.this)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllAuthorsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllAuthorsData allAuthorsData) {
                        try {
                            if (mResponse.get(1) != null && mResponse.get(1).getArticleSourceId() != null) {
                                for (int i = 0; i < allAuthorsData.getData().getAuthors().getResponse().size(); i++) {
                                    if (allAuthorsData.getData().getAuthors().getResponse().get(i).getId().equals(mResponse.get(1).getArticleSourceId())) {
                                        for (int k = 0; k < mResponse.size(); k++) {
                                            if (mResponse.get(k) != null && allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage() != null && !allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage().equals(""))
                                                mResponse.get(k).setPrimaryImage(allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage());
                                        }
                                        if (allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage() != null && !allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage().equals(""))
                                            Glide.with(AuthorsArchive.this.getApplicationContext()).load(allAuthorsData.getData().getAuthors().getResponse().get(i).getPrimaryImage()).into(mImg_tAuthorArchivePhoto);
//                                        else
//                                            mImg_tAuthorArchivePhoto.setImageResource(ErrorImageType.getIcon(getApplicationContext().getApplicationInfo().packageName));
                                        break; // bulunca devam etmesin
                                    }
                                }
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

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        collapsingToolbar.setContentScrimColor(Color.parseColor(Preferences.getString(AuthorsArchive.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))); // collapse arkaplan rengi
        collapsingToolbar.setCollapsedTitleTextAppearance(Preferences.getString(AuthorsArchive.this, ApiListEnums.THEME.getType(), "dark").equals("dark") ? R.style.TextAppearance_ColumnistCollapseTitleDark : R.style.TextAppearance_ColumnistCollapseTitleLight);
        appBarLayout.setBackgroundColor(Color.parseColor(Preferences.getString(AuthorsArchive.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mAuthorName != null)
                        collapsingToolbar.setTitle(mAuthorName + " - Tüm Yazıları");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        mPageIndex++;
        getNewsList(mPageIndex);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}