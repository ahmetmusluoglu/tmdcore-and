package turkuvaz.general.turkuvazgeneralactivitys.ColumnistsDetailSlider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;
import java.util.Objects;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.GdprDialog.GdprPreferences;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.Listeners.SpecialWebListener;
import turkuvaz.sdk.global.SpecialWebView;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by Ahmet MUŞLUOĞLU on 4/21/2020.
 */

public class ColumnistPaperAdapter extends PagerAdapter {
    private ArrayList<Article> articles;
    private boolean allArticleBtnIsShow;

    private GradientDrawable shape;
    private int columnistBottomTextColor;
    private int toolbarBackColor;
    private int allArticleColor;
    private int shareArticleColor;
    private int authorNameColor;
    private int newsSpotFontSize;
    private float newsTitleFontSize;
    private int headerHeight;

    ColumnistPaperAdapter(Context context, ArrayList<Article> articles, boolean allArticleBtnIsShow, int headerHeight) {
        this.articles = articles;
        this.allArticleBtnIsShow = allArticleBtnIsShow;
        this.headerHeight = headerHeight;
        this.shape = new GradientDrawable();
        this.shape.setShape(GradientDrawable.RECTANGLE);
        this.shape.setCornerRadii(new float[]{45, 45, 45, 45, 45, 45, 45, 45});
        this.shape.setColor(Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_BACK.getType(), "#FFFFFF")));
        this.shape.setStroke(1, Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_EDGE.getType(), "#FFFFFF")));
        this.columnistBottomTextColor = Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BOTTOM_LINE.getType(), "#FFFFFF"));
        this.toolbarBackColor = Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"));
        this.allArticleColor = Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), "#FFFFFF"));
        this.shareArticleColor = Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), "#FFFFFF"));
        this.authorNameColor = Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"));
        this.newsTitleFontSize = Preferences.getInt(context, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25);
        this.newsSpotFontSize = Preferences.getInt(context, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, final int position) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.columnist_detail_item, viewGroup, false);
        try {
            Context context = view.getContext();
            Article article = getArticle(position);
            shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadii(new float[]{45, 45, 45, 45, 45, 45, 45, 45});
            shape.setColor(Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_BACK.getType(), "#FFFFFF")));
            shape.setStroke(1, Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_EDGE.getType(), "#FFFFFF")));
            columnistBottomTextColor = Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BOTTOM_LINE.getType(), "#FFFFFF"));
            toolbarBackColor = Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"));
            allArticleColor = Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), "#FFFFFF"));
            shareArticleColor = Color.parseColor(Preferences.getString(context, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), "#FFFFFF"));
            authorNameColor = Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF"));
            newsTitleFontSize = Preferences.getInt(context, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25);
            newsSpotFontSize = Preferences.getInt(context, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16);

            RelativeLayout rlBottomLayout = view.findViewById(R.id.rlBottomLayout);
            LinearLayout llShareColumnist = view.findViewById(R.id.llShareColumnist);
            LinearLayout llArchiveList = view.findViewById(R.id.llArchiveList);
            RelativeLayout rlColumnistProfileImage = view.findViewById(R.id.rlColumnistProfileImage);
            RelativeLayout rlColumnistProfile = view.findViewById(R.id.rlColumnistProfile);
            ImageView imgNewsDetailTop = view.findViewById(R.id.imgNewsDetailTop);
            TextView txtColumnistName = view.findViewById(R.id.txtColumnistName);
            TextView txtAllArticle = view.findViewById(R.id.txtAllArticle);
            TextView txtShareArticle = view.findViewById(R.id.txtShareArticle);
            SpecialWebView specialWebView = view.findViewById(R.id.specialWebView);
            ProgressBar prgLoading = view.findViewById(R.id.prgLoading);
            TextView txtNewsTitle = view.findViewById(R.id.txtNewsTitle);
            TextView txtNewsSpot = view.findViewById(R.id.txtNewsSpot);
            TextView txtColumnistPrivacyPolicy = view.findViewById(R.id.txtColumnistPrivacyPolicy);
            TextView txtNewsDate = view.findViewById(R.id.txtNewsDate);
            View vLine = view.findViewById(R.id.vLine);
            FrameLayout frameTopBanner = view.findViewById(R.id.frameTopBanner);
            FrameLayout frameBottomBanner = view.findViewById(R.id.frameBottomBanner);
            NestedScrollView nestedScrollView = view.findViewById(R.id.nestedScrollView);

            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (onScrllListener != null) {
                        float alpha = (float) ((100 - (scrollY * 100 / headerHeight)));
                        onScrllListener.onScrll(scrollY > headerHeight, article.getTitle() != null ? article.getTitle() : "");
                        rlBottomLayout.setVisibility(scrollY > headerHeight ? View.INVISIBLE : View.VISIBLE);

                        if (alpha > 0)
                            rlColumnistProfile.setAlpha((alpha / 100));
                    }
                }
            });

            if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals(""))
                setBannerAds(context, new AdSize(320, 142), ApiListEnums.ADS_GENERAL_320x142.getApi(context), frameTopBanner, prgLoading);
            if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
                setBannerAds(context, new AdSize(300, 250), ApiListEnums.ADS_GENERAL_300x250.getApi(context), frameBottomBanner, prgLoading);

            if (!allArticleBtnIsShow)
                llArchiveList.setVisibility(View.GONE);
            llShareColumnist.setBackground(shape);
            llArchiveList.setBackground(shape);
            llArchiveList.setOnClickListener(view1 -> {
                if (article != null && article.getSource() != null && article.getPrimaryImage() != null && !TextUtils.isEmpty(article.getPrimaryImage()))
                    GlobalIntent.openAuthorsArchive(context, ApiListEnums.COLUMNIST_BY_SOURCE.getApi(context), article.getArticleSourceId(), article.getSource(), article.getPrimaryImage());
            });
            llShareColumnist.setOnClickListener(view12 -> {
                try {
                    if (article != null && article.getTitle() != null && !TextUtils.isEmpty(article.getTitle()) && article.getUrl() != null && !TextUtils.isEmpty(article.getUrl())) {

                        new TurkuvazAnalytic(context).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.ACTION_COLUMNIST_SHARE.getEventName()).sendEvent();

                        String urlWithDomain = ApplicationsWebUrls.getDomain(Objects.requireNonNull(context).getApplicationInfo().packageName) + article.getUrl();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, article.getSource());
                        intent.putExtra(Intent.EXTRA_TEXT, article.getTitle() + "\n" + urlWithDomain);
                        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.yaziyi_paylas)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            txtAllArticle.setTextColor(allArticleColor);
            txtShareArticle.setTextColor(shareArticleColor);
            txtColumnistName.setTextColor(authorNameColor);

            rlColumnistProfile.setBackgroundColor(toolbarBackColor);
            vLine.setBackgroundColor(columnistBottomTextColor);

            txtNewsTitle.setTextSize(newsTitleFontSize);
            txtNewsSpot.setTextSize(newsTitleFontSize - 6);
            txtColumnistPrivacyPolicy.setTextSize(newsSpotFontSize);

            txtColumnistPrivacyPolicy.setOnClickListener(view13 -> GlobalIntent.openInternalBrowser(context, ApiListEnums.ARTICLE_PRIVACY_POLICY.getApi(context)));

            if (Preferences.getString(context, ApiListEnums.THEME.getType(), "dark").equals("light")) {
                assert context != null;
                rlColumnistProfileImage.setBackground(context.getResources().getDrawable(R.drawable.shape_image_back_black));
            }

            txtColumnistName.setText(article.getSource());

            if (article.getPrimaryImage() != null && !TextUtils.isEmpty(article.getPrimaryImage())) {
                assert context != null;
                Glide.with(context).load(article.getPrimaryImage()).into(imgNewsDetailTop);
            }

            txtNewsTitle.setText(article.getTitle());
            txtNewsTitle.setText(article.getTitle());
            if (article.getSpot() != null && !article.getSpot().equals(""))
                txtNewsSpot.setText(article.getSpot());
            else
                txtNewsSpot.setVisibility(View.GONE);

            txtColumnistPrivacyPolicy.setText(Html.fromHtml(context.getString(R.string.html_content_columnist)));

            if (article.getOutputDate() != null && !TextUtils.isEmpty(article.getOutputDate()))
                txtNewsDate.setText(article.getOutputDate().trim());
            else
                txtNewsDate.setText(String.valueOf(Utils.convertDate(article.getCreatedDate(), context)));

            specialWebView.setContentType("app-columnist");
            specialWebView.setArticle(article);
            specialWebView.loadNews();
            specialWebView.setOnSelectedURL(new SpecialWebListener(context, prgLoading, (AllColumnistsActivity) context));

            final ViewTreeObserver observer = rlColumnistProfile.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(
                    () -> {
                        if (headerHeight < 11) {//rastgele bir deger girildi..
                            int actionbar_height = Preferences.getInt(context, ApiListEnums.ACTIONBAR_HEIGHT.getType(), 100);
                            headerHeight = rlColumnistProfile.getMeasuredHeight() - actionbar_height;
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

        viewGroup.addView(view);
        return view;
    }

    private Article getArticle(int position) {
        return articles.get(position);
    }

    private void setBannerAds(final Context context, AdSize adSize, final String adUnitId, FrameLayout frameLayout, ProgressBar progressBar) {
        try {
            if (adUnitId.equals(ApiListEnums.ADS_GENERAL_320x50.getApi(context)))
                frameLayout.setPadding(0, 0, 0, 0);
            else
                frameLayout.setPadding(0, 15, 0, 15);
            PublisherAdRequest request;

            FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mLayoutParams.gravity = Gravity.CENTER;

            final PublisherAdView mPublisherAdView = new PublisherAdView(context);
            mPublisherAdView.setVisibility(View.GONE);
            mPublisherAdView.setAdSizes(adSize);
            mPublisherAdView.setAdUnitId(adUnitId);
            mPublisherAdView.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    if (adUnitId.equals(ApiListEnums.ADS_GENERAL_320x50.getApi(context)))
                        frameLayout.setPadding(0, 0, 0, 0);
                    else
                        frameLayout.setPadding(0, 15, 0, 15);
                    mPublisherAdView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    frameLayout.setPadding(0, 0, 0, 0); // hata varsa padding'leri de kaldır fazla boşluk kalıyordu
                    Log.i("BannerAds", "An error occurred when loading ads");
                    progressBar.setVisibility(View.GONE);

                }
            });

            if (GdprPreferences.getAppHedefleme(context)) {
                request = new PublisherAdRequest.Builder().build();

            } else {
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                request = new PublisherAdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
            }

            mPublisherAdView.loadAd(request);

            frameLayout.addView(mPublisherAdView, mLayoutParams);
        } catch (Exception e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return articles == null ? 0 : articles.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    private OnScrllListener onScrllListener;

    void setScrllListener(OnScrllListener onScrllListener) {
        this.onScrllListener = onScrllListener;
    }

    public interface OnScrllListener {
        void onScrll(boolean isVisible, String title);
    }
}
