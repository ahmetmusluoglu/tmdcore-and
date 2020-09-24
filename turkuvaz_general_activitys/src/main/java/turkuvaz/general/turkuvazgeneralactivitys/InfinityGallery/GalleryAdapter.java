package turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdSize;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGalleryWithSlider.GalleryActivityWithSlider;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.TurkuvazTextView.TTextView;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.GetVideoModel.VideoModel;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.Utils.TurquazBannerAds;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.AlbumMedia;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.Response;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GalleryAdapter extends RecyclerView.Adapter {
    private final byte TYPE_IMAGE = 1;
    private final byte TYPE_TITLE = 2;
    private int loadMoreSize = -1;
    private List<AlbumMedia> albumMediaList = new ArrayList<>();
    private Response mResponse;
    private Context context;
    private LayoutInflater inflater;
    private OnLoadMoreListener listener;
    private InfinityActivity.OnSelectedURLListener onSelectedURLListener;
    private String mAdsCode, mDomain, mVideoApi;
    private RestInterface mRestInterface;
    private ShowGalleryModel galleryModel;
    private int albumMediaCount;

    GalleryAdapter(List<Response> dummyData, Context activity, String adUnitId, String domain, String getVideoApi, ShowGalleryModel galleryModel, InfinityActivity.OnSelectedURLListener onSelectedURLListener, int albumMediaCount) {
        if (activity == null)
            return;
        mRestInterface = ApiClient.getClientApi(activity).create(RestInterface.class);
        this.onSelectedURLListener = onSelectedURLListener;
        this.mDomain = domain;
        this.albumMediaCount = albumMediaCount;
        this.mResponse = dummyData.get(0);
        this.albumMediaList.addAll(dummyData.get(0).getAlbumMedias());
        this.galleryModel = galleryModel;
        this.context = activity;
        this.mAdsCode = adUnitId;
        this.mVideoApi = getVideoApi;
        this.inflater = LayoutInflater.from(this.context);

        //TODO Istatistik için galerinin bilgilerinin yollanması
        if (mResponse != null && onSelectedURLListener != null) {
            if (mResponse.getTitle() != null && mResponse.getUrl() != null && (galleryModel.getGalleryIds() == null || mResponse.getId().toString().equals(galleryModel.getGalleryIds().get(GalleryActivityWithSlider.activePosition)))) // galeri ilk açıldığında öncesi ve sonrasındaki galerinin bilgilerini de event atıyordu, kontrol koyuldu
                onSelectedURLListener.onOpenGallery(mResponse.getTitle(), mResponse.getUrl());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.item_infinity_image, parent, false);
        switch (viewType) {
            case TYPE_IMAGE:
                vRoot = inflater.inflate(R.layout.item_infinity_image, parent, false);
                break;
            case TYPE_TITLE:
                vRoot = inflater.inflate(R.layout.item_infinity_title, parent, false);
                break;
        }
        return new GalleryAdapter.ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return albumMediaList == null ? 0 : albumMediaList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_TITLE;
        else
            return TYPE_IMAGE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final AlbumMedia currentGallery = albumMediaList.get(position);
        final ViewHolder mHolder = (GalleryAdapter.ViewHolder) holder;
        final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);


        //TODO Galerinin ilk item'ı. Response içinden gelen datalar burada kullanılıyor.
        if (CURRENT_ITEM_TYPE == TYPE_TITLE) {

            if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals("")) {
                BannerAds bannerAds = new BannerAds(context, new AdSize(320, 142), ApiListEnums.ADS_GENERAL_320x142.getApi(context));
                mHolder.mTopAds.addView(bannerAds);
            }

            if (mHolder.mTv_title != null) {
                mHolder.mTv_title.setTextSize(Preferences.getInt(context, SettingsTypes.NEWS_TITLE_FONT_SIZE.getType(), 25));
            }
            if (mHolder.mWeb_titleDescription != null) {
                mHolder.mWeb_titleDescription.getSettings().setDefaultFontSize(Preferences.getInt(context, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));
            }

            if (mHolder.mWeb_description != null) {
                mHolder.mWeb_description.getSettings().setDefaultFontSize(Preferences.getInt(context, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));
            }

            if (mResponse.getTitle() != null && !TextUtils.isEmpty(mResponse.getTitle()))
                mHolder.mTv_title.setText(Html.fromHtml(mResponse.getTitle()));

            if (mResponse.getDescription() != null && !TextUtils.isEmpty(mResponse.getDescription()) && !mResponse.getSpot().equals(mResponse.getDescription())) {
                mHolder.mWeb_titleDescription.loadData("<div class=\"app-cover app-gallery app-gallery-description\">" + mResponse.getDescription() + "</div>", "text/html; charset=UTF-8", null); // TAKVIM-5638 ve TAKVIM-5753
                mHolder.mWeb_titleDescription.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        checkSelectedURL(url);
                        return true;
                    }
                });
            }

            if (currentGallery.getDescription() != null && !TextUtils.isEmpty(currentGallery.getDescription())) {
                String style = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ApiListEnums.CONTENT_CSS.getApi(context) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(context) + "\">";
                String mReadyContent = context.getString(R.string.html_content, style, "app-gallery", currentGallery.getDescription());

                mHolder.mWeb_description.loadData(currentGallery.getDescription(), "text/html; charset=UTF-8", null);
                mHolder.mWeb_description.loadDataWithBaseURL(null, mReadyContent, "text/html", "UTF-8", null);
                mHolder.mWeb_description.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        checkSelectedURL(url);
                        return true;
                    }
                });
            }

            // FKR-811 - ilk resmin başlığı
            if (mResponse.isShowInlineTitle() != null && mResponse.isShowInlineTitle() && currentGallery.getTitle() != null && !currentGallery.getTitle().equals(""))
                mHolder.mTv_imageTitle.setText(currentGallery.getTitle());
            else
                mHolder.mTv_imageTitle.setVisibility(View.GONE); //title boş gelirse visible kaldırılacak boşluk kalmasın

            // FKR-811 işine ek olarak tarih de eklendi. Galerilerde tarih gösterilmiyordu
            if (mResponse.getOutputDate() != null && !TextUtils.isEmpty(mResponse.getOutputDate()))
                mHolder.mTv_Date.setText(mResponse.getOutputDate().trim());
            else if (mResponse.getModifiedDate() != null && !TextUtils.isEmpty(mResponse.getModifiedDate()))
                mHolder.mTv_Date.setText(convertDate(mResponse.getModifiedDate()));

            if (currentGallery.getSortOrder() != null && mResponse.getAlbumMediaCount() != null)
                mHolder.mTv_counter.setText(String.valueOf(currentGallery.getSortOrder() + "/" + mResponse.getAlbumMediaCount()));

            if (context != null && currentGallery.getImage() != null && !TextUtils.isEmpty(currentGallery.getImage()))
                Glide.with(context.getApplicationContext()).load(currentGallery.getImage()).into(mHolder.mImg_photo);
            else
                mHolder.mImg_photo.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));

            //TODO Galeri içindeki diğer itemler bu şekilde basılıyor. Her 5 itemda bir reklam alınına reklam basılıyor.
        } else if (CURRENT_ITEM_TYPE == TYPE_IMAGE) {
            if (mHolder.mWeb_description != null)
                mHolder.mWeb_description.getSettings().setDefaultFontSize(Preferences.getInt(context, SettingsTypes.NEWS_SPOT_FONT_SIZE.getType(), 16));

            /** SET ADS */
            if (position % (albumMediaList.size() - 1) == 0) { // api'den pagination kaç geliyorsa o kadarda bir reklam gösterilecek
                mHolder.mBanner.loadAds(context, AdSize.MEDIUM_RECTANGLE, GlobalMethods.getCategoryZone(context, mResponse.getCategoryId(), AdsType.ADS_300x250));

            if (Preferences.getBoolean(context, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(context).equals(""))
                mHolder.mBanner.setPadding(mHolder.mBanner.getPaddingLeft(), mHolder.mBanner.getPaddingTop(), mHolder.mBanner.getPaddingRight(), (int) (105 * context.getResources().getDisplayMetrics().density));
        }
            if (currentGallery.getDescription() != null && !TextUtils.isEmpty(currentGallery.getDescription())) {
                String style = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + ApiListEnums.CONTENT_CSS.getApi(context) + "?" + ApiListEnums.CONTENT_CSS_VERSION.getApi(context) + "\">";
                String mReadyContent = context.getString(R.string.html_content, style, "app-gallery", currentGallery.getDescription());

                mHolder.mWeb_description.loadData(currentGallery.getDescription(), "text/html; charset=UTF-8", null);
                mHolder.mWeb_description.loadDataWithBaseURL(null, mReadyContent, "text/html", "UTF-8", null);
                mHolder.mWeb_description.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        checkSelectedURL(url);
                        return true;
                    }
                });
            }

            // FKR-811 - resimlerin başlıkları
            if (mResponse.isShowInlineTitle() != null && mResponse.isShowInlineTitle() && currentGallery.getTitle() != null && !currentGallery.getTitle().equals(""))
                mHolder.mTv_imageTitle.setText(currentGallery.getTitle());
            else
                mHolder.mTv_imageTitle.setVisibility(View.GONE);

            if (currentGallery.getSortOrder() != null && mResponse.getAlbumMediaCount() != null)
                mHolder.mTv_counter.setText(String.valueOf(currentGallery.getSortOrder() + "/" + mResponse.getAlbumMediaCount()));
            mHolder.mImg_photo.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
            if (context != null && currentGallery.getImage() != null && !TextUtils.isEmpty(currentGallery.getImage()))
                Glide.with(context.getApplicationContext()).load(currentGallery.getImage()).into(mHolder.mImg_photo);
            else
                mHolder.mImg_photo.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
        }

        //TODO Seçilen Image'ın detay sayfasında açılması - Paylaşma için detayların yollanması
        if (mHolder.mImg_photo != null)
            mHolder.mImg_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mResponse.getUrl() == null || mDomain == null)
                        return;

                    String mShareUrl = mResponse.getUrl();
                    if (!mResponse.getUrl().contains("http") && !mResponse.getUrl().contains(mDomain)) {
                        if (currentGallery.getSortOrder() != null)
                            mShareUrl = mDomain + mResponse.getUrl() + "/" + Integer.toString(currentGallery.getSortOrder());
                    }

                    Intent intent = new Intent(context, GalleryDetailsActivity.class);
                    intent.putExtra("imagePath", currentGallery.getImage());
                    intent.putExtra("imageTitle", currentGallery.getTitle());
                    intent.putExtra("sharePath", mShareUrl);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    context.startActivity(intent);
                }
            });

        if (mHolder.mImg_infinityShare != null) {
            mHolder.mImg_infinityShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentGallery.getSortOrder() != null && currentGallery.getTitle() != null)
                        shareImage(currentGallery.getTitle(), Integer.toString(currentGallery.getSortOrder()));
                }
            });
        }

        //TODO Pagination yapısı için yapılan kontroller
        if (position >= getItemCount() - 1 && listener != null && loadMoreSize != getItemCount() - 1) {
            loadMoreSize = getItemCount() - 1;
            if (getItemCount() < albumMediaCount) {
                if (galleryModel.getGalleryIds() != null)
                    galleryModel.setGalleryID(galleryModel.getGalleryIds().get(GalleryActivityWithSlider.activePosition));
                listener.onLoadMore();

            } else
                InfinityActivity.mPageIndex = 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_counter, mTv_Date;
        TTextView mTv_title, mTv_imageTitle;
        WebView mWeb_titleDescription, mWeb_description;
        ImageView mImg_photo, mImg_infinityShare;
        TurquazBannerAds mBanner;
        FrameLayout mTopAds;

        ViewHolder(View itemView) {
            super(itemView);
            mTopAds = itemView.findViewById(R.id.ads_infinityTopAds);
            mBanner = itemView.findViewById(R.id.ads_infinityBanner);
            mTv_title = itemView.findViewById(R.id.tv_infinityTitle);
            mTv_counter = itemView.findViewById(R.id.tv_infinityCounter);
            mTv_Date = itemView.findViewById(R.id.tv_infinityDate);
            mTv_imageTitle = itemView.findViewById(R.id.tv_infinityImageTitle);
            mImg_photo = itemView.findViewById(R.id.img_infinityPhoto);
            mImg_infinityShare = itemView.findViewById(R.id.img_infinityShare);
            mWeb_titleDescription = itemView.findViewById(R.id.web_infinityTitleDesc);
            mWeb_description = itemView.findViewById(R.id.web_infinityDescription);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener, int pageIndex) {
        this.listener = listener;
    }

    void notifyDataSet(List<AlbumMedia> list, int count) {
        albumMediaList.addAll(list);
        notifyItemRangeInserted(getItemCount(), count);
    }

    private String convertDate(String irregularDate) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM, EEEE", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }

    private void shareImage(String imageTitle, String index) {
        if (mResponse.getUrl() == null || mDomain == null)
            return;

        new TurkuvazAnalytic(context).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.ACTION_GALLERY_IMAGE_SHARE.getEventName()).sendEvent();

        String mShareUrl = mResponse.getUrl();
        if (!mResponse.getUrl().contains("http") && !mResponse.getUrl().contains(mDomain)) {
            mShareUrl = mDomain + mResponse.getUrl() + "/" + index;
        }
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, imageTitle + " " + mShareUrl);
        context.startActivity(Intent.createChooser(share, context.getResources().getString(R.string.paylas)));
    }

    private void getVideoURL(String videoID) {
        if (context == null)
            return;

        final ProgressDialog progressdialog = new ProgressDialog(context);
        progressdialog.setMessage("Video yükleniyor..");

        mRestInterface.getVideoURL(mVideoApi, videoID.toLowerCase()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoModel response) {
                        if (response != null) {
                            if (response.getMeta().getStatusCode() == 200) {
                                if (response.getData().getListVideoHomeAndDetailPage() != null) {
                                    if (response.getData().getListVideoHomeAndDetailPage().getResponse().size() > 0) {
                                        String title = response.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getTitle();
                                        String category = response.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getCategoryName();
                                        String VideoSmilUrl = response.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoSmilUrl();
                                        String VideoMobileUrl = response.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoMobileUrl();
                                        String VideoUrl = response.getData().getListVideoHomeAndDetailPage().getResponse().get(0).getVideoUrl();

                                        if (VideoSmilUrl != null && !TextUtils.isEmpty(VideoSmilUrl) && onSelectedURLListener != null) {
                                            onSelectedURLListener.onVideo(VideoSmilUrl, category, title);
                                        } else if (VideoMobileUrl != null && !TextUtils.isEmpty(VideoMobileUrl)) {
                                            onSelectedURLListener.onVideo(VideoMobileUrl, category, title);
                                        } else {
                                            onSelectedURLListener.onVideo(VideoUrl, category, title);
                                        }

                                        if (progressdialog.isShowing())
                                            progressdialog.dismiss();
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (progressdialog.isShowing())
                            progressdialog.dismiss();
                        Toast.makeText(context, "Video yüklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //TODO Tıklanılan URL'in kontrol edilmesi ve listenerlara gerekli dataların aktarımı
    private void checkSelectedURL(String selectedURL) {
        if (selectedURL.contains(ExternalTypes.VIDEO.getType())) {
            getVideoURL(selectedURL.replace(ExternalTypes.VIDEO.getType(), ""));

        } else if (selectedURL.contains(ExternalTypes.NEWS.getType())) {
            onSelectedURLListener.onNews(selectedURL.replace(ExternalTypes.NEWS.getType(), ""));

        } else if (selectedURL.contains(ExternalTypes.ALBUM.getType())) {
            onSelectedURLListener.onAlbum(selectedURL.replace(ExternalTypes.ALBUM.getType(), ""));

        } else if (selectedURL.contains(ExternalTypes.PHOTO_NEWS.getType())) {
            onSelectedURLListener.onPhotoNews(selectedURL.replace(ExternalTypes.PHOTO_NEWS.getType(), ""));

        } else if (selectedURL.contains(ExternalTypes.ARTICLE_SOURCE.getType())) {
            onSelectedURLListener.onArticle(selectedURL.replace(ExternalTypes.ARTICLE_SOURCE.getType(), ""));

        } else if (selectedURL.contains(ExternalTypes.LIVE_STREAM.getType())) {


        } else if (selectedURL.contains(ExternalTypes.EXTERNAL.getType())) {
            onSelectedURLListener.onExternal(selectedURL.replace(ExternalTypes.EXTERNAL.getType(), ""));

        } else if (selectedURL.contains(ExternalTypes.INTERNAL.getType())) {
            onSelectedURLListener.onInternal(selectedURL.replace(ExternalTypes.INTERNAL.getType(), ""));
        }
    }
}