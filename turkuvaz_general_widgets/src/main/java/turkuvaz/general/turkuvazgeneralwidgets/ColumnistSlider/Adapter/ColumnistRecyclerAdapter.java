package turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;

/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class ColumnistRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectedNewsListener selectedListener;
    private boolean dateShow = true;


    public ColumnistRecyclerAdapter(ArrayList<Article> dummyData, Context context, boolean dateShow) {
        if (context == null)
            return;

        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dateShow = dateShow;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.columnist_slider_item, parent, false);
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

        if (context != null && currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage()))
            Glide.with(context.getApplicationContext()).load(currentModel.getPrimaryImage()).into(viewHolder.mImg_authorImage1);
        else
            viewHolder.mImg_authorImage1.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));

        if (!dateShow) // fikriyat home ekranındaki yazarlar cardview'ında tarih görünmemesi istendi. o sebeple parametrik yapıldı
            viewHolder.mTv_authorDate1.setVisibility(View.GONE);

        if (currentModel.getSource() != null)
            viewHolder.mTv_authorName1.setText(currentModel.getSource());

        if (currentModel.getTitle() != null)
            viewHolder.mTv_authorDesc1.setText(currentModel.getTitle());

        if (currentModel.getOutputDate() != null && !TextUtils.isEmpty(currentModel.getOutputDate()))
            viewHolder.mTv_authorDate1.setText(String.valueOf(currentModel.getOutputDate().trim()));
        else if (currentModel.getCreatedDate() != null)
            viewHolder.mTv_authorDate1.setText(convertDate(currentModel.getCreatedDate()));


        viewHolder.mLy_columnistItemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedListener.onSelect(arrayList, position, ClickViewType.COLUMNIST_SLIDER);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg_authorImage1;
        TextView mTv_authorName1, mTv_authorDesc1, mTv_authorDate1;
        LinearLayout mLy_columnistItemRoot;

        ViewHolder(View itemView) {
            super(itemView);
            mImg_authorImage1 = itemView.findViewById(R.id.img_authorImage1);
            mTv_authorName1 = itemView.findViewById(R.id.txtTiele);
            mTv_authorDesc1 = itemView.findViewById(R.id.tv_authorDescription1);
            mTv_authorDate1 = itemView.findViewById(R.id.tv_authorDate1);
            mLy_columnistItemRoot = itemView.findViewById(R.id.ly_columnistItemRoot);
        }
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    String convertDate(String irregularDate) {
        String cleanDate = " ";

        if (irregularDate == null)
            return cleanDate;

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date newDate = format.parse(irregularDate);

            format = new SimpleDateFormat("dd MMMM, yyyy", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }
}