package turkuvaz.general.turkuvazgeneralwidgets.NewsListAndSlider;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSliderWithTitle.Adapter.HeadlineSliderTitleAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.HeadlineSliderWithTitle.HeadlineSliderWithTitle;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.AnimationUtils.AnimationUtil;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;

public class NewsListWithSliderAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private ArrayList<Article> headlines;
    private Context context;
    private LayoutInflater inflater;
    private OnLoadMoreListener listener;
    private int loadMoreSize = -1;
    private int screenSize;
    private onSelectedNewsListener selectedListener;
    private onHeadlineSelectedNewsListener onHeadlineSelectedNewsListener;
    int previousPosition = 0;
    private onLoadTopBanner onLoadTopBanner;
    private final byte TYPE_SLIDER = 0;
    private final byte TYPE_2x2_NEWS = 1;
    private final byte TYPE_ADS_300x250 = 2;
    private final byte TYPE_ADS_320x142 = 3;


    public NewsListWithSliderAdapter(ArrayList<Article> articles, ArrayList<Article> headlines, Context context) {
        if (context == null)
            return;
        this.screenSize = Preferences.getInt(context, SettingsTypes.SCREEN_WIDTH.getType(), -1);
        this.arrayList = articles;
        this.context = context;
        this.headlines = headlines;
        this.inflater = LayoutInflater.from(context);
        if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals("")) // eğer reklam api linkleri boş gelirse, reklamlar için boş bir item eklemesin yoksa ilk yazıyı uçuruyor
            this.arrayList.add(0, null);
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
            this.arrayList.add(arrayList.size(), null);
        for (int i = 0; i < arrayList.size(); i++) {
            if (i == 1)  // slider için eklendi
                arrayList.add(i, null);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View vRoot = inflater.inflate(R.layout.news_list_item_grid_2x2, parent, false);
        switch (viewType) {
            case TYPE_SLIDER:
                vRoot = inflater.inflate(R.layout.news_slider_empty_item, parent, false);
                break;
            case TYPE_2x2_NEWS:
                vRoot = inflater.inflate(R.layout.news_list_item_grid_2x2, parent, false);
                break;
            case TYPE_ADS_320x142:
                vRoot = inflater.inflate(R.layout.news_list_ads_item, parent, false);
                break;
            case TYPE_ADS_300x250:
                vRoot = inflater.inflate(R.layout.news_list_ads_item, parent, false);
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
        if (ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals("") && ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
            return TYPE_2x2_NEWS;
        else if (ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals(""))
            return position == 0 ? TYPE_2x2_NEWS : (arrayList.get(position) == null) ? TYPE_ADS_300x250 : TYPE_2x2_NEWS;
        else if (ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
            return position == 0 ? TYPE_ADS_320x142 : TYPE_2x2_NEWS;
        else if (position == 1)
            return TYPE_SLIDER;
        else
            return position == 0 ? TYPE_ADS_320x142 : (arrayList.get(position) == null) ? TYPE_ADS_300x250 : TYPE_2x2_NEWS;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final Article currentModel = arrayList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);


            if (CURRENT_ITEM_TYPE == TYPE_ADS_320x142) {
                if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
                    ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);
                if (viewHolder.mFrame_ads != null && viewHolder.mFrame_ads.getChildCount() == 0) {
                    /** SET ADS */
                    BannerAds bannerAds = new BannerAds(context, new AdSize(320, 142), GlobalMethods.getCategoryZone(context, currentModel.getCategoryId(), AdsType.ADS_320x142));
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
                    BannerAds bannerAds = new BannerAds(context, new AdSize(300, 250), GlobalMethods.getCategoryZone(context,currentModel.getCategoryId(), AdsType.ADS_300x250));
                    viewHolder.mFrame_ads.addView(bannerAds);
                }
            } else if (CURRENT_ITEM_TYPE == TYPE_SLIDER) {
                if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
                    ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);
                HeadlineSliderWithTitle headlineSliderWithTitle = new HeadlineSliderWithTitle((Activity) context);
                headlineSliderWithTitle.setOnSelectedNewsListener(new HeadlineSliderTitleAdapter.onSelectedNewsListener() {
                    @Override
                    public void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType) {
                        onHeadlineSelectedNewsListener.onSelect(selectedNews, position, clickViewType);
                    }
                });
                headlineSliderWithTitle.init(headlines);

                if (viewHolder.mLy_sliderRoot.getChildCount() == 0)
                    viewHolder.mLy_sliderRoot.addView(headlineSliderWithTitle);

            } else if (CURRENT_ITEM_TYPE == TYPE_2x2_NEWS) {

                viewHolder.mImg_newsImage.setImageDrawable(null);

                viewHolder.mTv_title.setVisibility(View.GONE);

                if (currentModel.getTitleShort() != null)
                    viewHolder.mTv_description.setText(currentModel.getTitleShort());

                if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage()))
                    Glide.with(context.getApplicationContext()).asBitmap().load(currentModel.getPrimaryImage()).listener(
                            new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                int width = bitmap.getWidth();
                                                int height = bitmap.getHeight();
                                                viewHolder.mImg_newsImage.getLayoutParams().height = (screenSize / 2) * height / width;
                                            } catch (Exception r) {

                                            }
                                            viewHolder.mImg_newsImage.setImageBitmap(bitmap);
                                        }
                                    });
                                    return false;
                                }
                            }
                    ).submit();
                else
                    viewHolder.mImg_newsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));


                viewHolder.mCard_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedListener != null)
                            selectedListener.onSelect(arrayList, position, ClickViewType.NEWS_INFINITY_LIST);
                    }
                });
            }


            /** SCROLL ITEM ANIMATION */
            if (position > previousPosition && position > 6) {
                AnimationUtil.animate(holder);
            }
            previousPosition = position;

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
        TextView mTv_title, mTv_description;
        ImageView mImg_newsImage;
        CardView mCard_root;
        LinearLayout mLy_sliderRoot;
        FrameLayout mFrame_ads;

        ViewHolder(View itemView) {
            super(itemView);
            mFrame_ads = itemView.findViewById(R.id.frame_newsListAds);
            mTv_title = itemView.findViewById(R.id.tv_newsTitle);
            mTv_description = itemView.findViewById(R.id.tv_newsDescription);
            mImg_newsImage = itemView.findViewById(R.id.img_newsImage);
            mCard_root = itemView.findViewById(R.id.card_newsRoot);
            mLy_sliderRoot = itemView.findViewById(R.id.ly_rootSliderItem);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public void notifyDataSet(ArrayList<Article> list, int count) {
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
            list.add(list.size(), null);
        arrayList.addAll(list);
        notifyItemRangeInserted(getItemCount(), count);
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public interface onHeadlineSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public void setOnHeadlineSelectedNewsListener(NewsListWithSliderAdapter.onHeadlineSelectedNewsListener onHeadlineSelectedNewsListener) {
        this.onHeadlineSelectedNewsListener = onHeadlineSelectedNewsListener;
    }

    public interface onLoadTopBanner {
        void onLoadedBanner();
    }

    public void setOnLoadTopBanner(onLoadTopBanner onLoadTopBanner) {
        this.onLoadTopBanner = onLoadTopBanner;
    }
}