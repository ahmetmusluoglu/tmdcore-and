package turkuvaz.general.turkuvazgeneralwidgets.ColumnistSlider.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

public class ColumnistPagerAdapter extends PagerAdapter {
    private ArrayList<Article> mResponse;
    private ColumnistRecyclerAdapter.onSelectedNewsListener mOnSelectedNewsListener;
    boolean dateShow = true;
    private int rowCount;

    public ColumnistPagerAdapter(ArrayList<Article> response, boolean dateShow, int rowCount) {
        mResponse = response;
        this.dateShow = dateShow;
        this.rowCount = rowCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(collection.getContext()).inflate(R.layout.columnist_slider_list, collection, false);

        try {
            RecyclerView mRec_columnistList = layout.findViewById(R.id.rec_columnistList);
            mRec_columnistList.setLayoutManager(new LinearLayoutManager(collection.getContext()));

            List<Article> list = null;

            try {
                if (mResponse.size() >= (position * rowCount) + rowCount)
                    list = mResponse.subList(position * rowCount, (position * rowCount) + rowCount);
                else {
                    list = mResponse.subList((mResponse.size() - (mResponse.size() % rowCount)), mResponse.size());
                }
            } catch (NullPointerException | IndexOutOfBoundsException | IllegalArgumentException nullPointerException) {
                nullPointerException.printStackTrace();
            }

/*          0 ->   0  1
            1 ->   2  3
            2 ->   4  5
            3 ->   6  7
            4 ->   8      */

            ColumnistRecyclerAdapter adapter = new ColumnistRecyclerAdapter(new ArrayList<>(list), collection.getContext(), dateShow);
            adapter.setSelectedListener(mOnSelectedNewsListener);
            mRec_columnistList.setAdapter(adapter);
            collection.addView(layout);

        } catch (NullPointerException | IndexOutOfBoundsException nullPointerException) {
            nullPointerException.printStackTrace();
        }

        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        if (mResponse == null)
            return 0;

        if (rowCount == 1)
            return mResponse.size();

        if (mResponse.size() % rowCount == 0) {
            return mResponse.size() / rowCount;
        } else {
            return ((mResponse.size() / rowCount) + 1);
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

    public void setSelectedListener(ColumnistRecyclerAdapter.onSelectedNewsListener mOnSelectedNewsListener) {
        this.mOnSelectedNewsListener = mOnSelectedNewsListener;
    }
}