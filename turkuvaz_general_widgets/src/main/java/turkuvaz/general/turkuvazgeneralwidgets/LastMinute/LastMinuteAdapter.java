package turkuvaz.general.turkuvazgeneralwidgets.LastMinute;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Preferences;

import java.util.ArrayList;


/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class LastMinuteAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectedNewsListener selectedListener;
    private onChangePosition onChangePosition;
    private String text;

    public LastMinuteAdapter(ArrayList<Article> dummyData, Context context, String mText) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.text = mText;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.last_minute_item, parent, false);
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

        if (onChangePosition != null)
            onChangePosition.onCurrentPosition(holder.getAdapterPosition());

        // son dakika barında sağdaki layout içindeki metnin rengi belirlendi
        viewHolder.mTv_title.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.LAST_MINUTE_TEXT_RIGHT.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz)))));

        if (text != null && text.equals("TitleShort")) { // titleshort ayarlıysa, titleshort gelecek. fakat eğer boş gelirse yine title alınacak boş görünmemesi için TAKVIM-5569
            if (currentModel.getTitleShort() != null && !TextUtils.isEmpty(currentModel.getTitleShort()))
                viewHolder.mTv_title.setText(currentModel.getTitleShort());
            else if (currentModel.getTitle() != null && !TextUtils.isEmpty(currentModel.getTitle()))
                viewHolder.mTv_title.setText(currentModel.getTitle());
        } else if ( text != null &&text.equals("Title")) {
            if (currentModel.getTitle() != null && !TextUtils.isEmpty(currentModel.getTitle()))
                viewHolder.mTv_title.setText(currentModel.getTitle());
            else if (currentModel.getTitleShort() != null && !TextUtils.isEmpty(currentModel.getTitleShort()))
                viewHolder.mTv_title.setText(currentModel.getTitleShort());
        } else { // bu alanın olmadığı uygulamalarda boş geleceği için titleshort alınsın
            if (currentModel.getTitleShort() != null && !TextUtils.isEmpty(currentModel.getTitleShort()))
                viewHolder.mTv_title.setText(currentModel.getTitleShort());
            else if (currentModel.getTitle() != null && !TextUtils.isEmpty(currentModel.getTitle()))
                viewHolder.mTv_title.setText(currentModel.getTitle());
        }

        viewHolder.mTv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedListener != null)
                    selectedListener.onSelect(arrayList, position, ClickViewType.LAST_MINUTE);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_title;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_itemSonDakika);

        }
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public interface onChangePosition {
        void onCurrentPosition(int position);
    }

    public void setOnChangePosition(LastMinuteAdapter.onChangePosition onChangePosition) {
        this.onChangePosition = onChangePosition;
    }
}