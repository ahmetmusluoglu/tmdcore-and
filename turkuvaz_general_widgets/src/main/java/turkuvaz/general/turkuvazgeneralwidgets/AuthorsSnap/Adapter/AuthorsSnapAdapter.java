package turkuvaz.general.turkuvazgeneralwidgets.AuthorsSnap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

public class AuthorsSnapAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectedNewsListener selectedListener;


    public AuthorsSnapAdapter(ArrayList<Article> dummyData, Context context) {
        if (context == null)
            return;

        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.authors_snap_item, parent, false);
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

        if (currentModel.getSource() != null)
            viewHolder.mTv_authorName1.setText(currentModel.getSource());

        if (currentModel.getTitle() != null)
            viewHolder.tv_authorsSnapTitle.setText(currentModel.getTitle());

        if(currentModel.getDescription() != null)
            viewHolder.tv_authorsSnapContent.setText(Html.fromHtml(currentModel.getDescription()));

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
        TextView mTv_authorName1, tv_authorsSnapTitle, mTv_authorDate1, tv_authorsSnapContent;
        CardView mLy_columnistItemRoot;

        ViewHolder(View itemView) {
            super(itemView);
            mImg_authorImage1 = itemView.findViewById(R.id.img_authorImage1);
            mTv_authorName1 = itemView.findViewById(R.id.txtTiele);
            tv_authorsSnapTitle = itemView.findViewById(R.id.tv_authorsSnapTitle);
            tv_authorsSnapContent = itemView.findViewById(R.id.tv_authorsSnapContent);
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

            format = new SimpleDateFormat("dd MMMM yyyy, EEEE", new Locale("tr"));
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }
}