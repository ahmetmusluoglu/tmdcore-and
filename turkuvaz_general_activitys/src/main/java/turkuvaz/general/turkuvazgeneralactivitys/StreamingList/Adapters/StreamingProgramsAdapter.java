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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.sdk.models.Models.StreamingList.DaysModel;
import turkuvaz.sdk.models.Models.StreamingList.Response;


/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class StreamingProgramsAdapter extends RecyclerView.Adapter {
    private ArrayList<Response> arrayList;
    private Context context;
    private LayoutInflater inflater;
    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS", Locale.getDefault());
    Date currentTime;

    public StreamingProgramsAdapter(ArrayList<Response> dummyData, Context context) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        currentTime = Calendar.getInstance().getTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.streaming_programs_item, parent, false);
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Response currentModel = arrayList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mTb_programsTime.setText(timeConverter(currentModel.getStartTime()));
        viewHolder.mTv_programsName.setText(String.valueOf(currentModel.getTitle()).trim());

        try {
            if (currentTime.after(inFormat.parse(currentModel.getStartTime())) && currentTime.before(inFormat.parse(currentModel.getEndTime()))) {
                viewHolder.layStreamProgramLine.setBackgroundColor(Color.LTGRAY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_programsName, mTb_programsTime;
        LinearLayout layStreamProgramLine;

        ViewHolder(View itemView) {
            super(itemView);
            mTb_programsTime = itemView.findViewById(R.id.tv_streamingProgramsTime);
            mTv_programsName = itemView.findViewById(R.id.tv_streamingProgramsName);
            layStreamProgramLine = itemView.findViewById(R.id.layStreamProgramLine);
        }
    }

    private String timeConverter(String stringDate) {
        String time = "--:--";
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
            Date date = inFormat.parse(stringDate);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            time = timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}