package turkuvaz.general.turkuvazgeneralactivitys.StreamingList.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.sdk.models.Models.StreamingList.DaysModel;


/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class StreamingDaysAdapter extends RecyclerView.Adapter {
    private ArrayList<DaysModel> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectedNewsListener selectedListener;
    private int selectedPosition = 0;

    public StreamingDaysAdapter(ArrayList<DaysModel> dummyData, Context context) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.streaming_days_item, parent, false);
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DaysModel currentModel = arrayList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        if (selectedPosition == position) {
            viewHolder.mLy_streamingDaysRoot.setBackgroundColor(Color.RED);
            viewHolder.mTv_daysName.setTextColor(Color.WHITE);
            viewHolder.mTv_daysNumber.setTextColor(Color.WHITE);
            viewHolder.mTv_monthName.setTextColor(Color.WHITE);

        } else {
            viewHolder.mLy_streamingDaysRoot.setBackgroundResource(R.drawable.stroke_gray_line);
            viewHolder.mTv_daysName.setTextColor(Color.BLACK);
            viewHolder.mTv_daysNumber.setTextColor(Color.BLACK);
            viewHolder.mTv_monthName.setTextColor(Color.BLACK);
        }


        viewHolder.mTv_daysName.setText(currentModel.getDayName());
        viewHolder.mTv_daysNumber.setText(String.valueOf(currentModel.getDayNumber()));
        viewHolder.mTv_monthName.setText(currentModel.getMonthName());
        viewHolder.mLy_streamingDaysRoot.setOnClickListener(v -> {
            if (selectedListener != null) {
                selectedPosition = position;
                selectedListener.onSelect(currentModel, position);
                notifyDataSetChanged();
            }

        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_daysName, mTv_daysNumber, mTv_monthName;
        LinearLayout mLy_streamingDaysRoot;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_daysName = itemView.findViewById(R.id.tv_streamingDaysName);
            mTv_daysNumber = itemView.findViewById(R.id.tv_streamingDaysNumber);
            mTv_monthName = itemView.findViewById(R.id.tv_streamingMonthName);
            mLy_streamingDaysRoot = itemView.findViewById(R.id.ly_streamingDaysRoot);
        }
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onSelectedNewsListener {
        void onSelect(DaysModel selectedNews, int position);
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}