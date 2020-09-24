package turkuvaz.general.turkuvazgeneralwidgets.CategoryTabs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.ConfigBase.LeftMenu;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by turgay.ulutas on 13/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class CategoryAdapter extends RecyclerView.Adapter {
    private ArrayList<LeftMenu> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private onSelectCategoryListener onSelectCategory;
    private int selectedPosition = 0;
    int width;
    private boolean dizilerSekmesiMi;
    private String toolBarTitle;
    private RecyclerView recyclerView;

    public CategoryAdapter(ArrayList<LeftMenu> dummyData, Context context, int selectedPosition, boolean dizilerSekmesiMi, String toolBarTitle) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        this.context = context;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        this.dizilerSekmesiMi = dizilerSekmesiMi;
        this.toolBarTitle = toolBarTitle;

        this.inflater = LayoutInflater.from(context);
        if (selectedPosition != -1)
            this.selectedPosition = selectedPosition;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot;
        if (dizilerSekmesiMi)
            vRoot = inflater.inflate(R.layout.category_item_for_tvseries, parent, false);
        else
            vRoot = inflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final LeftMenu currentModel = arrayList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;

            if (selectedPosition == position) {
                viewHolder.mTv_title.setTypeface(null, Typeface.BOLD);
                viewHolder.mTv_title.setTextColor(Color.WHITE);
                if (Preferences.getString(context, ApiListEnums.THEME.getType(), "dark").equals("light")) { // fikriyat gibi açık temalarda renkler tersi olacak. dark temalarda ise takvim ve aspor gibi renklendirmeler olacak
                    DrawableCompat.setTint(context.getResources().getDrawable(R.drawable.shape_category_selected), Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
                    viewHolder.mTv_title.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
                }
                viewHolder.mTv_title.setBackground(context.getResources().getDrawable(R.drawable.shape_category_selected));
            } else {
                viewHolder.mTv_title.setTypeface(null, Typeface.NORMAL);
                viewHolder.mTv_title.setTextColor(Color.GRAY);
//                if (Preferences.getString(context, ApiListEnums.THEME.getType(), "dark").equals("light"))
//                    DrawableCompat.setTint(context.getResources().getDrawable(R.drawable.shape_category_unselected), Color.parseColor(Preferences.getString(context, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF")));
                viewHolder.mTv_title.setBackground(context.getResources().getDrawable(R.drawable.shape_category_unselected));
            }

            if (currentModel.getTitle() != null && !TextUtils.isEmpty(currentModel.getTitle())) {
                viewHolder.mTv_title.setText(currentModel.getTitle());
                viewHolder.mTv_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelectCategory != null) {
                            if (currentModel.getCid() == null) {
                                selectedPosition = 0;
                                onSelectCategory.onSelectCategory(null, (currentModel.getTitle() != null) ? currentModel.getTitle() : "", String.valueOf(selectedPosition), toolBarTitle);
                            } else {
                                selectedPosition = position;
                                onSelectCategory.onSelectCategory(currentModel.getCid(), currentModel.getTitle(), String.valueOf(selectedPosition), toolBarTitle);
                            }

                            notifyDataSetChanged();

                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (recyclerView != null) {
                                        RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(context);
                                        smoothScroller.setTargetPosition(selectedPosition);
                                        recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                                    }
                                }
                            });
                        }
                    }
                });
            }
            if (dizilerSekmesiMi) // diziler ekranıysa, ekranı önceden 2 ye bölüyorduk şimdi kaç item varsa o kadar sayıya bölüyoruz
                viewHolder.mTv_title.getLayoutParams().width = (width / arrayList.size()) - 30;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_title;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_categoryTabsTitle);
        }
    }

    public interface onSelectCategoryListener {
        void onSelectCategory(String categoryID, String categoryTitle, String selectedPos, String toolBarTitle);
    }

    public void setOnSelectCategory(onSelectCategoryListener onSelectCategory) {
        this.onSelectCategory = onSelectCategory;
    }

    public class CenterSmoothScroller extends LinearSmoothScroller {

        public CenterSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
        }
    }
}