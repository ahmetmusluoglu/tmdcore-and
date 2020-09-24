package turkuvaz.general.turkuvazgeneralactivitys.AuthorArchive;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import android.widget.FrameLayout;
import com.google.android.gms.ads.AdSize;
import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.AuthorList.AuthorListAdapter;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;

public class AuthorsArchiveAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private OnLoadMoreListener listener;
    private int loadMoreSize = -1;
    private onSelectedNewsListener selectedListener;
    private ArrayList<Article> arrayListTemp;
    private final byte TYPE_AUTHORS = 0;
    private final byte TYPE_ADS_300x250 = 1;
    private final byte TYPE_ADS_320x142 = 2;

    public AuthorsArchiveAdapter(ArrayList<Article> dummyData, Context context) {
        if (context == null)
            return;
        arrayListTemp = new ArrayList<>(dummyData);
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        if (!ApiListEnums.ADS_GENERAL_320x142.getApi(context).equals(""))
            this.arrayList.add(0, null);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.authors_archive_item, parent, false);
        switch (viewType) {
            case TYPE_AUTHORS:
                vRoot = inflater.inflate(R.layout.authors_archive_item, parent, false);
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
    public int getItemViewType(int position) {
        return (arrayList.get(position) == null && position != 0) ? TYPE_ADS_300x250 : (arrayList.get(position) == null && position == 0) ? TYPE_ADS_320x142 : TYPE_AUTHORS;
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Article currentModel = arrayList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);

        /*if (currentModel.getTitle() != null)
            viewHolder.mTv_title.setText(Html.fromHtml(currentModel.getTitle()));

        if (currentModel.getDescription() != null)
            viewHolder.mTv_description.setText(Html.fromHtml(currentModel.getDescription()));

        if (currentModel.getCreatedDate() != null)
            viewHolder.mTv_date.setText(convertDate(currentModel.getCreatedDate()));

//        viewHolder.mTv_dot.setText(Html.fromHtml("&#8226;"));

        viewHolder.mRl_authorsArchiveRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedListener != null)
                    selectedListener.onSelect(arrayList, position, ClickViewType.VIDEO_WITH_DESCRIPTION);
            }
        });*/

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
                bannerAds.setBackgroundResource(R.drawable.bottom_gray_line_archive); // araya reklam koyuyor ama altta boşluk oluyordu. reklama da bir item gibi arkaplan verildi
                viewHolder.mFrame_ads.addView(bannerAds);
            }
        } else if (CURRENT_ITEM_TYPE == TYPE_AUTHORS) {
            if (currentModel.getTitle() != null)
                viewHolder.mTv_title.setText(Html.fromHtml(currentModel.getTitle()));
            if (currentModel.getDescription() != null)
                viewHolder.mTv_description.setText(Html.fromHtml(currentModel.getDescription()));

            if (currentModel.getOutputDate() != null && !TextUtils.isEmpty(currentModel.getOutputDate()))
                viewHolder.mTv_date.setText(String.valueOf(currentModel.getOutputDate().trim()));
            else if (currentModel.getCreatedDate() != null)
                viewHolder.mTv_date.setText(convertDate(currentModel.getCreatedDate()));

//        viewHolder.mTv_dot.setText(Html.fromHtml("&#8226;"));
        viewHolder.mRl_authorsArchiveRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedListener != null)
                    selectedListener.onSelect(arrayList, position, ClickViewType.VIDEO_WITH_DESCRIPTION);
            }
        });
    }

        //TODO Pagination yapısı için yapılan kontroller
        if (position >= getItemCount() - 1 && listener != null && loadMoreSize != getItemCount() - 1) {
            loadMoreSize = getItemCount() - 1;
            listener.onLoadMore();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TODO Create object HERE
        TextView mTv_title, mTv_description, mTv_date;
        RelativeLayout mRl_authorsArchiveRoot;
        FrameLayout mFrame_ads;

        ViewHolder(View itemView) {
            super(itemView);
            mFrame_ads = itemView.findViewById(R.id.frame_newsListAds);
            mTv_title = itemView.findViewById(R.id.tv_authorsArchiveTitle);
            mTv_description = itemView.findViewById(R.id.tv_authorsArchiveDesc);
            mTv_date = itemView.findViewById(R.id.tv_authorsArchiveDate);
            mRl_authorsArchiveRoot = itemView.findViewById(R.id.rl_authorsArchiveRoot);
//            mTv_dot = itemView.findViewById(R.id.dot);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    void notifyDataSet(ArrayList<Article> list, int count) {
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals("")) { // 300x250 api'den gelmiş olması gerek. gelmediyse null kayıt atma
            if (arrayList.get(arrayList.size() - 1) != null) // son item reklam ise alt satırda tekrar reklam ekleme
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

    String convertDate(String irregularDate) {
        String cleanDate = " ";
        if (irregularDate == null)
            return cleanDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);
            format = new SimpleDateFormat("dd MMMM yyyy, EEEE", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cleanDate;
    }
}