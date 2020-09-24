package turkuvaz.general.apps.global_activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralactivitys.BaseGeneralActivity;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.AllAuthors.AllAuthors;
import turkuvaz.general.turkuvazgeneralwidgets.AuthorList.AuthorList;

import turkuvaz.general.apps.R;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.AllAuthors.Response;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Preferences;

public class AuthorListActivity extends BaseGeneralActivity {
    AllAuthors allAuthors;
    boolean yazarSecimiYapildi = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase,""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey("apiPath")) {
            finish();
        } else {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(Html.fromHtml("<font color="+Color.parseColor(Preferences.getString(AuthorListActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"))+">" + bundle.getString("toolbarTitle", "") + "</font>"));

            LinearLayout mFrame_authorsList = findViewById(R.id.frame_authorListActivity);

            AuthorList authorList = new AuthorList(this);
            authorList.setApiPath(bundle.getString("apiPath"));
//            authorList.setOnSelectedNewsListener((selectedNews, position, returnClickViewType) -> GlobalIntent.openColumnistWithArticle(AuthorListActivity.this, selectedNews.get(position), (authorList.getAuthorsID() != null && !authorList.getAuthorsID().equals("")) ? false : true));
            authorList.setOnSelectedNewsListener((selectedNews, position, returnClickViewType) -> GlobalIntent.openAllColumnistWithArticle(AuthorListActivity.this, selectedNews,position, (authorList.getAuthorsID() != null && !authorList.getAuthorsID().equals("")) ? false : true));
            authorList.setPagination(false);
            authorList.setTumYazilariEkraniAc(false);
            authorList.init();

            allAuthors = new AllAuthors(this);
            allAuthors.setApiUrl(ApiListEnums.ALL_AUTHORS.getApi(this));
            allAuthors.init();
            allAuthors.setOnSpinnerItemClickListener((position, itemAtPosition, authorsID) -> {
                authorList.setmPageIndex(1);
                if (!authorsID.equals("None")) { // yazar seçtiyse api link değişsin. tümünü göster yapıldıysa eski apiyi alsın yani all_columnist
                    ArrayList<Response> allAuthorList = allAuthors.getData();
                    if (allAuthorList.size() > 0 && allAuthorList.get(position) != null)
                        GlobalIntent.openAuthorsArchive(this, ApiListEnums.COLUMNIST_BY_SOURCE.getApi(this), allAuthorList.get(position).getId(), allAuthorList.get(position).getSource(), allAuthorList.get(position).getPrimaryImage());
                    else
                        Toast.makeText(this, "Yazara Ait Yazı Bulunamadı", Toast.LENGTH_SHORT).show();
                    yazarSecimiYapildi = true;
                    authorList.setTumYazilariEkraniAc(true);
                    authorList.setPagination(true);
//                    authorList.setApiPath(ApiListEnums.COLUMNIST_BY_SOURCE.getApi(this)); // home'daki yazarlar widgeti ile sol menüdeki yazarlar widget'i eşitlendiği için spinner ile de tüm yazıları sayfasındaki apiler eşitlendi
//                    authorList.setAuthorsID(authorsID);
                }
                else {
                    authorList.setTumYazilariEkraniAc(false);
                    authorList.setPagination(false);
                    authorList.setApiPath(ApiListEnums.COLUMNIST.getApi(this)); // home'daki yazarlar widgeti ile sol menüdeki yazarlar widget'i eşitlendiği için spinner ile de tüm yazıları sayfasındaki apiler eşitlendi
                    authorList.setAuthorsID(null);
                }
            });

            if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals(""))
                authorList.setPadding(authorList.getPaddingLeft(), authorList.getPaddingTop(), authorList.getPaddingRight(), (int) (50 * getResources().getDisplayMetrics().density));

            mFrame_authorsList.addView(allAuthors);
            mFrame_authorsList.addView(authorList);

            try {
                if (Preferences.getBoolean(this, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(this).equals("")) {
                    BannerAds bannerAds = new BannerAds(this, new AdSize(320, 50), ApiListEnums.ADS_GENERAL_320x50.getApi(this));
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    bannerAds.setLayoutParams(layoutParams);
                    runOnUiThread(() -> ((RelativeLayout) findViewById(R.id.relLayAuthorList1)).addView(bannerAds));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /** SEND ANALYTICS */
            new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.COLUMNIST_PAGE.getEventName()).sendEvent();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // spinner'dan yazar seçince doğrudan tüm yazıları ekranına gitmesi için eklendi
    @Override
    protected void onResume() {
        super.onResume();
//        if (yazarSecimiYapildi && allAuthors != null)
//            allAuthors.setSelection(0); // tüm yazarlar seçili olacak geri gelindiğinde
    }
}