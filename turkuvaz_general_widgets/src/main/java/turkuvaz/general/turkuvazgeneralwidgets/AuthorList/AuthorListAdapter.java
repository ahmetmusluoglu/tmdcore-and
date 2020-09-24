package turkuvaz.general.turkuvazgeneralwidgets.AuthorList;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdSize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

public class AuthorListAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectedNewsListener selectedListener;

    private final byte TYPE_AUTHORS = 0;
    private final byte TYPE_ADS_300x250 = 1;
    private final byte TYPE_ADS_320x142 = 2;
    private onLoadTopBanner onLoadTopBanner;

    private ArrayList<Article> arrayListTemp;
    private OnLoadMoreListener listener;
    private int loadMoreSize = -1;
    private boolean pagination;

    public AuthorListAdapter(ArrayList<Article> dummyData, Context context, boolean pagination) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.pagination = pagination;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals(""))
            this.arrayList.add(0, null);
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
            this.arrayList.add(arrayList.size(), null);
    }

    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.author_list_item, parent, false);
        switch (viewType) {
            case TYPE_AUTHORS:
                vRoot = inflater.inflate(R.layout.author_list_item, parent, false);
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
        return (arrayList.get(position) == null && position != 0) ? TYPE_ADS_300x250 : (arrayList.get(position) == null && position == 0) ? TYPE_ADS_320x142 : TYPE_AUTHORS;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final Article currentModel = arrayList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);


            if (CURRENT_ITEM_TYPE == TYPE_ADS_320x142) {
                if (viewHolder.mFrame_ads != null && viewHolder.mFrame_ads.getChildCount() == 0) {
                    /** SET ADS */
                    BannerAds bannerAds = new BannerAds(context, new AdSize(320, 142), ApiListEnums.ADS_GENERAL_320x142.getApi(context));
                    viewHolder.mFrame_ads.addView(bannerAds);
                }
            } else if (CURRENT_ITEM_TYPE == TYPE_ADS_300x250) {
                if (viewHolder.mFrame_ads != null && viewHolder.mFrame_ads.getChildCount() == 0) {
                    /** SET ADS */
                    BannerAds bannerAds = new BannerAds(context, new AdSize(300, 250), ApiListEnums.ADS_GENERAL_300x250.getApi(context));
                    viewHolder.mFrame_ads.addView(bannerAds);
                }
            } else if (CURRENT_ITEM_TYPE == TYPE_AUTHORS) {
                if (currentModel.getSource() != null)
                    viewHolder.mTv_name.setText(currentModel.getSource());

                if (currentModel.getOutputDate() != null && !TextUtils.isEmpty(currentModel.getOutputDate())) // TAKVIM-5561
                    viewHolder.mTv_lastDate.setText(currentModel.getOutputDate().trim());
                else if (currentModel.getCreatedDate() != null && !TextUtils.isEmpty(currentModel.getCreatedDate()))
                    viewHolder.mTv_lastDate.setText(convertDate(currentModel.getCreatedDate()));

                if (currentModel.getTitle() != null)
                    viewHolder.mTv_lastArticle.setText(currentModel.getTitle());

                if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage()))
                    Glide.with(context.getApplicationContext()).load(currentModel.getPrimaryImage()).into(viewHolder.mImg_newsImage);
                else
                    viewHolder.mImg_newsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));

                viewHolder.mCard_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try { // bu blok, Ahmet ile konuşularak tekrar düzenlendi. Reklamın olmadığı durumlarda -1. item'ı alıyordu.
                            if (selectedListener != null) { // ilk ve son değer reklam eklenmis, onları kaldırmak icin...
                                int pos = 0;
                                Article currArticle = arrayList.get(position);
                                ArrayList<Article> articles = new ArrayList<>();
                                for (Article article : arrayList) {
                                    if (article != null) {
                                        if (article.getArticleId().equals(currArticle.getArticleId())) {
                                            pos = articles.size();
                                        }
                                        articles.add(article);
                                    }
                                }
                                selectedListener.onSelect(articles, pos, ClickViewType.NEWS_INFINITY_LIST);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (pagination) {
                if (position >= getItemCount() - 1 && listener != null && loadMoreSize != getItemCount() - 1) { // spinner'dan yazarın 10 yazısı geliyordu ve aşağı kaymıyordu. tüm yazıları butonu gibi yapıldı
                    loadMoreSize = getItemCount() - 1;
                    listener.onLoadMore();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TODO Create object HERE
        TextView mTv_name, mTv_lastArticle, mTv_lastDate;
        ImageView mImg_newsImage;
        RelativeLayout mCard_root;
        FrameLayout mFrame_ads;

        ViewHolder(View itemView) {
            super(itemView);
            mFrame_ads = itemView.findViewById(R.id.frame_newsListAds);
            mTv_name = itemView.findViewById(R.id.tv_authorPersonName);
            mTv_lastArticle = itemView.findViewById(R.id.tv_authorPersonLast);
            mTv_lastDate = itemView.findViewById(R.id.tv_authorLastDate);
            mImg_newsImage = itemView.findViewById(R.id.img_authorPersonImage);
            mCard_root = itemView.findViewById(R.id.card_authorsRoot);
        }
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    void notifyDataSet(ArrayList<Article> list, int count) {
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals(""))
            list.add(list.size(), null);
        arrayList.addAll(list);
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

    public interface onLoadTopBanner {
        void onLoadedBanner();
    }

    public void setOnLoadTopBanner(onLoadTopBanner onLoadTopBanner) {
        this.onLoadTopBanner = onLoadTopBanner;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }
}