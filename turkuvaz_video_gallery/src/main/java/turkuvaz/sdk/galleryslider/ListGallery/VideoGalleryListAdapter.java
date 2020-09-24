package turkuvaz.sdk.galleryslider.ListGallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdSize;
import com.turquaz.videogallery.R;

import java.util.ArrayList;
import java.util.List;

import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider.HeadLineAdapter.HeadlinePagerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider.HeadLineSlider;
import turkuvaz.sdk.global.AnimationUtils.AnimationUtil;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Preferences;

public class VideoGalleryListAdapter extends RecyclerView.Adapter{
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private OnLoadMoreListener listener;
    private int loadMoreSize = -1;
    private int screenSize;
    private onSelectedNewsListener selectedListener;
    private int previousPosition = 0;
    private final byte TYPE_2x2_NEWS = 0;
    private final byte TYPE_ADS_300x250 = 1;
    private final byte TYPE_ADS_320x142 = 2;
    private final byte TYPE_HEADLINE = 3;
    private onLoadTopBanner onLoadTopBanner;
    private String categoryID = "", categoryIDHeadline = "", mApiUrlHeadline, customPageName = "";

    public VideoGalleryListAdapter(ArrayList<Article> dummyData, Context context, String mApiUrlHeadline, String customPageName) {
        if (context == null) return;
        this.screenSize = Preferences.getInt(context, SettingsTypes.SCREEN_WIDTH.getType(), -1);
        this.arrayList = dummyData;
        this.context = context;
        this.mApiUrlHeadline = mApiUrlHeadline;
        this.customPageName = customPageName;
        this.inflater = LayoutInflater.from(context);
        if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals(""))
            this.arrayList.add(0, null);
        if (!mApiUrlHeadline.equals(""))
            this.arrayList.add(1, null); // manşet eklenince uçuyordu, manuel eklendi
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.item_video_list, parent, false);
        switch (viewType) {
            case TYPE_ADS_300x250:
                vRoot = inflater.inflate(R.layout.news_list_ads_item, parent, false);
                break;
            case TYPE_ADS_320x142:
                vRoot = inflater.inflate(R.layout.news_list_ads_item, parent, false);
                break;
            case TYPE_HEADLINE:
                vRoot = inflater.inflate(R.layout.news_headline_item, parent, false);
                break;
            case TYPE_2x2_NEWS:
                vRoot = inflater.inflate(R.layout.item_video_list, parent, false);
                break;
        }
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) == null && position == 0)
            return TYPE_ADS_320x142;
        else if (position == 1 && !mApiUrlHeadline.equals(""))
            return TYPE_HEADLINE;
        else if (arrayList.get(position) == null && position != 0)
            return TYPE_ADS_300x250;
        else
            return TYPE_2x2_NEWS;
//        return (arrayList.get(position) == null && position != 0) ? TYPE_ADS_300x250 : (arrayList.get(position) == null && position == 0) ? TYPE_ADS_320x142 : TYPE_2x2_NEWS; // arrayList.get(position) == null ise orada reklam vardır
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try { // headline'da tek sütun olacak. diğerleri 2 sütun olacağı için bu blok yazıldı
            final StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(holder.itemView.getLayoutParams());
            if ((position == 1 && !mApiUrlHeadline.equals(""))) {
                layoutParams.setFullSpan(true);
            } else {
                layoutParams.setFullSpan(false);
            }
            holder.itemView.setLayoutParams(layoutParams);

            final Article currentModel = arrayList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);

            if (currentModel != null && currentModel.getCategoryId() != null)
                categoryID = currentModel.getCategoryId();
            else
                categoryID = null; // 300x250 reklamda currentModel null geliyordu ve alttaki if'te hata veriyordu

            if (CURRENT_ITEM_TYPE == TYPE_ADS_320x142) {
                if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
                    ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);
                if (viewHolder.mFrame_ads != null && viewHolder.mFrame_ads.getChildCount() == 0) {
                    /** SET ADS */
                    BannerAds bannerAds = new BannerAds(context, new AdSize(320, 142), GlobalMethods.getCategoryZone(context, categoryID, AdsType.ADS_320x142));
//                    bannerAds.setPadding(0,10,0,10);
                    bannerAds.setBannerStatusListener(new BannerAds.bannerStatusListener() {
                        @Override
                        public void onLoad() {
                            if (onLoadTopBanner != null)
                                onLoadTopBanner.onLoadedBanner();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    viewHolder.mFrame_ads.addView(bannerAds);
                }
            } else if (CURRENT_ITEM_TYPE == TYPE_ADS_300x250) {
                if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
                    ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);
                if (viewHolder.mFrame_ads != null && viewHolder.mFrame_ads.getChildCount() == 0) {
                    /** SET ADS */
                    BannerAds bannerAds = new BannerAds(context, new AdSize(300, 250), GlobalMethods.getCategoryZone(context, categoryID , AdsType.ADS_300x250));
//                    bannerAds.setPadding(0,15,0,25);
                    viewHolder.mFrame_ads.addView(bannerAds);
                }
            } else if (CURRENT_ITEM_TYPE == TYPE_HEADLINE && !mApiUrlHeadline.equals("")) {
                HeadLineSlider headLineSlider = new HeadLineSlider((Activity) context);
                headLineSlider.setApiPath(mApiUrlHeadline);
                headLineSlider.setOnSelectedNewsListener((selectedNews, position1, clickViewType) -> {
                    if (selectedListener != null)
                        selectedListener.onSelect(selectedNews, position1, ClickViewType.VIDEO_WITH_DESCRIPTION);
                });
//                headLineSlider.setPadding(0,25,0,0);
                headLineSlider.setCustomPageName(customPageName);
                headLineSlider.setTitleVisible(true); // default verildi
                headLineSlider.setCategoryID(categoryIDHeadline);
                headLineSlider.init();
                viewHolder.mFrameHeadline.addView(headLineSlider);
            } else if (CURRENT_ITEM_TYPE == TYPE_2x2_NEWS) {

                viewHolder.mImg_newsImage.setImageDrawable(null);

                if (currentModel.getTitleShort() != null)
                    viewHolder.mTv_description.setText(currentModel.getTitleShort());

                if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage()))
                    Glide.with(context.getApplicationContext())
                            .asBitmap().load(currentModel.getPrimaryImage())
                            .listener(new RequestListener<Bitmap>() {
                                          @Override
                                          public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                              return false;
                                          }

                                          @Override
                                          public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                              ((Activity) context).runOnUiThread(() -> {
                                                  try {
//                                                      int width = bitmap.getWidth();
//                                                      int height = bitmap.getHeight();
//                                                      viewHolder.mImg_newsImage.getLayoutParams().height = (screenSize / 2) * height / width;
                                                      if (currentModel.getVideoTypeId() != null)
                                                          viewHolder.imgVideoIcon.setVisibility(View.VISIBLE);
                                                  } catch (Exception e) {
                                                      e.printStackTrace();
                                                  }
                                                  viewHolder.mImg_newsImage.setImageBitmap(bitmap);
                                              });
                                              return false;
                                          }
                                      }
                            ).submit();
                else
                    viewHolder.mImg_newsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));


                viewHolder.mCard_root.setOnClickListener(v -> {
                    // her videoya tıklandığında sonraki 10 videoyu alsın dedik ama sürekli sabit 10 video dönüyor ilişkili videolarda da. bu sorun oldu. arraylistin boyutu 140 150 den fazlaysa Bundle'a set ederken hata alınıyordu bu sebeple bu düşünüldü
//                        List<Article> yeniArray = arrayList.subList(position, (position + 10) < arrayList.size() ? (position + 12) : (arrayList.size()));
                    if (selectedListener != null)
                        selectedListener.onSelect(arrayList, position, ClickViewType.VIDEO_WITH_DESCRIPTION);
//                        selectedListener.onSelect(yeniArray, 0, ClickViewType.VIDEO_WITH_DESCRIPTION); // ilk item 0 gönderince reklam olan null a denk geliyor ve diğer ekranda hata alınıyor. 1 gönderilebilir.
                });

                /** SCROLL ITEM ANIMATION */
                if (position > previousPosition && position > 6) {
                    AnimationUtil.animate(holder);
                }
                previousPosition = position;
            }

            //TODO Pagination yapısı için yapılan kontroller
            if (position >= getItemCount() - 1 && listener != null && loadMoreSize != getItemCount() - 1) {
                loadMoreSize = getItemCount() - 1;
                listener.onLoadMore();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // TODO Create object HERE
        TextView mTv_description;
        ImageView mImg_newsImage;
        ImageView imgVideoIcon;
        CardView mCard_root;
        FrameLayout mFrame_ads, mFrameHeadline;

        ViewHolder(View itemView) {
            super(itemView);
            mFrameHeadline = itemView.findViewById(R.id.mFrameHeadline);
            mFrame_ads = itemView.findViewById(R.id.frame_newsListAds);
            mTv_description = itemView.findViewById(R.id.tv_newsDescription);
            mImg_newsImage = itemView.findViewById(R.id.img_newsImage);
            imgVideoIcon = itemView.findViewById(R.id.imgVideoIcon);
            mCard_root = itemView.findViewById(R.id.card_newsRoot);
        }
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public void setmCategoryIDHeadline(String categoryID) {
        this.categoryIDHeadline = categoryID;
    }

    void notifyDataSet(ArrayList<Article> list, int count) {
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals("")) {
            if (arrayList.get(arrayList.size() - 1) != null)
                list.add(0, null);
            list.add(list.size(), null);
        }
        arrayList.addAll(list);
        notifyItemRangeInserted(getItemCount(), count);
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onLoadTopBanner {
        void onLoadedBanner();
    }

    public void setOnLoadTopBanner(onLoadTopBanner onLoadTopBanner) {
        this.onLoadTopBanner = onLoadTopBanner;
    }

}