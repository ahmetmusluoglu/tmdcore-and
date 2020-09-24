package turkuvaz.general.turkuvazgeneralwidgets.Programmes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Series.Response;

public class ProgrammesAdapter extends RecyclerView.Adapter {

    @LayoutRes
    private int layoutType = R.layout.news_list_item_grid_2x2;

    public ProgrammesAdapter(ArrayList<Response> dummyData, Context context) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {

    }

    public void notifyDataSet(ArrayList<Response> list, int count) {

        notifyItemRangeInserted(getItemCount(), count);
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Response> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
    }
}