package turkuvaz.sdk.galleryslider.ListGallery;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.turquaz.videogallery.R;

import java.util.ArrayList;

import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

public class RelatedVideosAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;

    public RelatedVideosAdapter(ArrayList<Article> dummyData, Context context) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.item_related_videos, parent, false);
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Article currentModel = arrayList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        if (currentModel == null){
            viewHolder.mRl_relatedContentRoot.setVisibility(View.GONE);
            return;
        }

        if (currentModel.getTitleShort() != null)
            viewHolder.mTv_title.setText(Html.fromHtml(currentModel.getTitleShort()));
        viewHolder.mImg_videoThumb.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
        if (currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage()))
            Glide.with(context.getApplicationContext()).load(currentModel.getPrimaryImage()).into(viewHolder.mImg_videoThumb);
        else
            viewHolder.mImg_videoThumb.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));

        viewHolder.mRl_relatedContentRoot.setOnClickListener(v -> GlobalIntent.openVideoDetailPage(context, arrayList, position, "testAds"));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TODO Create object HERE
        TextView mTv_title;
        ImageView mImg_videoThumb;
        RelativeLayout mRl_relatedContentRoot;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_relatedVideoTitle);
            mImg_videoThumb = itemView.findViewById(R.id.img_relatedVideoPhoto);
            mRl_relatedContentRoot = itemView.findViewById(R.id.rl_relatedContentRoot);
        }
    }
}