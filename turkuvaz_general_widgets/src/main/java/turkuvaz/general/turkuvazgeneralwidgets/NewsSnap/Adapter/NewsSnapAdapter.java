package turkuvaz.general.turkuvazgeneralwidgets.NewsSnap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class NewsSnapAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectedNewsListener selectedListener;
    private boolean isTitleShow = false;
    private boolean isTitleOverImage = true;
    private int textSize = 0;
    private static int widht = 0;
    AllFlavors app;
    RecyclerView mRecyclerView;
    CardView cardview;
    private boolean isSingle = false;

    public NewsSnapAdapter(ArrayList<Article> dummyData, Context context) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        widht = GlobalMethods.getScreenWidth();
        app = GlobalMethods.getCurrentFlavor(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot; // api tarafında parametrik yapıldı. (isTitleOverImage)
        if (isSingle)
            vRoot = inflater.inflate(R.layout.news_single_snap_item, parent, false); // başlığın resmin üstünde olma görünümü
        else if (isTitleOverImage)
            vRoot = inflater.inflate(R.layout.news_snap_item, parent, false); // başlığın resmin üstünde olma görünümü
        else
            vRoot = inflater.inflate(R.layout.news_snap_item_alternative, parent, false); // başlığın resmin altında olma durumu
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final Article currentModel = arrayList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage())) {
                Glide.with(context.getApplicationContext()).load(currentModel.getPrimaryImage()).error(ErrorImageType.getIcon(context.getApplicationInfo().packageName)).into(viewHolder.mImg_snapNewsImage);
            } else if (context != null) {
                viewHolder.mImg_snapNewsImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                viewHolder.mImg_snapNewsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
            }
            if (widht < 10)
                widht = GlobalMethods.getScreenWidth();
            int currWidth = isSingle ? widht * 4 / 5 : (widht / 2) - 32;
            if (viewHolder.rlSnap != null) {
                ViewGroup.LayoutParams params = viewHolder.rlSnap.getLayoutParams();
                params.width = currWidth;
                viewHolder.rlSnap.setLayoutParams(params);
            }
            if (viewHolder.rlSnap2 != null) { // başlığın resmin altında olduğu durum için
                ViewGroup.LayoutParams params = viewHolder.rlSnap2.getLayoutParams();
                params.width = currWidth;
                viewHolder.rlSnap2.setLayoutParams(params);
            }

            if (textSize > 0) // eğer apiden değer verildiyse set et. verilmediyse default olan size alınacak
                viewHolder.mTv_snapNewsTitle.setTextSize(textSize);

            if (isTitleShow && currentModel.getTitleShort() != null && !TextUtils.isEmpty(currentModel.getTitle())) {
                viewHolder.mTv_snapNewsTitle.setText(currentModel.getTitleShort());
                viewHolder.mTv_snapNewsTitle.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTv_snapNewsTitle.setVisibility(View.GONE);
            }

            viewHolder.mImg_snapNewsImage.setOnClickListener(v -> {
                if (selectedListener != null)
                    selectedListener.onSelect(arrayList, position, ClickViewType.NEWS_SNAP);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg_snapNewsImage;
        TextView mTv_snapNewsTitle;
        RelativeLayout rlSnap;
        LinearLayout rlSnap2; // a haberde başlık resmin altında olsun istendi. 2 farklı layout tek layoutta birleştirilemediği için ayrı yapıldı

        ViewHolder(View itemView) {
            super(itemView);
            mImg_snapNewsImage = itemView.findViewById(R.id.img_snapNewsImage);
            mTv_snapNewsTitle = itemView.findViewById(R.id.tv_snapNewsTitle);
            rlSnap = itemView.findViewById(R.id.rlSnap);
            rlSnap2 = itemView.findViewById(R.id.rlSnap2);
            cardview = itemView.findViewById(R.id.card_newsRoot);
        }
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setTitleShow(boolean titleShow) {
        isTitleShow = titleShow;
    }

    public void setSingle(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public void setTitleOverImage(boolean titleOverImage) {
        isTitleOverImage = titleOverImage;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}