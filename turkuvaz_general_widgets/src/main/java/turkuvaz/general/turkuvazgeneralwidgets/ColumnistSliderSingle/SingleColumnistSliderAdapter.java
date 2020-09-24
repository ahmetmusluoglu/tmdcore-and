package turkuvaz.general.turkuvazgeneralwidgets.ColumnistSliderSingle;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

/**
 * Created by Ahmet MUŞLUOĞLU on 4/6/2020.
 */
public class SingleColumnistSliderAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private SingleColumnistSliderAdapter.onSelectedNewsListener selectedListener;

    public SingleColumnistSliderAdapter(ArrayList<Article> dummyData, Context context) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.single_columnist_slider_item, parent, false);
        return new SingleColumnistSliderAdapter.ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final Article currentModel = arrayList.get(position);
            final SingleColumnistSliderAdapter.ViewHolder viewHolder = (SingleColumnistSliderAdapter.ViewHolder) holder;

            if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage())) {
                Glide.with(context.getApplicationContext()).load(currentModel.getPrimaryImage()).error(ErrorImageType.getIcon(context.getApplicationInfo().packageName)).into(viewHolder.imgAuthor);
            } else if (context != null) {
                viewHolder.imgAuthor.setScaleType(ImageView.ScaleType.FIT_CENTER);
                viewHolder.imgAuthor.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
            }

            viewHolder.txtTitle.setText(currentModel.getTitleShort());
            viewHolder.txtAuthorName.setText(currentModel.getSource());
            viewHolder.txtTitle.setVisibility(View.VISIBLE);

            viewHolder.llColumnistItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedListener != null)
                        selectedListener.onSelect(arrayList, position, ClickViewType.COLUMNIST_SLIDER);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAuthor;
        TextView txtTitle;
        TextView txtAuthorName;
        LinearLayout llColumnistItemRoot;

        ViewHolder(View itemView) {
            super(itemView);
            imgAuthor = itemView.findViewById(R.id.imgAuthor);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthorName = itemView.findViewById(R.id.txtAuthorName);
            llColumnistItemRoot = itemView.findViewById(R.id.llColumnistItemRoot);

        }
    }

    public void setSelectedListener(SingleColumnistSliderAdapter.onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }
}