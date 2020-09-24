package turkuvaz.general.turkuvazgeneralwidgets.CategoryTabs;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.Database.DatabaseHelper;

import turkuvaz.sdk.models.Models.ConfigBase.LeftMenu;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.MenuActionType;

/**
 * Created by turgay.ulutas on 11/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class CategoryTabs extends RecyclerView {
    MenuActionType mType;
    CategoryAdapter categoryAdapter;
    int selectedCategoryPosition = 0;
    String defaultItemName = "None";
    String toolBarTitle = "";

    public void setCategoryListener(CategoryAdapter.onSelectCategoryListener categoryListener) {
        if (categoryAdapter != null)
            categoryAdapter.setOnSelectCategory(categoryListener);
    }

    public CategoryTabs(@NonNull Context context) {
        super(context);
        init();
    }

    public CategoryTabs(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoryTabs(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public String getToolBarTitle() {
        return toolBarTitle;
    }

    public void setToolBarTitle(String toolBarTitle) {
        this.toolBarTitle = toolBarTitle;
    }

    public void setSelectedCategoryPosition(int selectedCategoryPosition) {
        this.selectedCategoryPosition = selectedCategoryPosition;
    }

    public int getSelectedCategoryPosition() {
        return selectedCategoryPosition;
    }

    public void setActionType(MenuActionType mType) {
        this.mType = mType;
    }

    public String getDefaultItemName() {
        return defaultItemName;
    }

    public void initForCustomTvSeries(String cid, List<LeftMenu> subTabs) {
        try {
            int dpValue = 8; // margin in dips
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d); // margin in pixels

            this.setPadding(margin, 0, 0, 0);
            this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));

            if (mType == null)
                return;

            this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            ArrayList<LeftMenu> data = new ArrayList<>();

            if (subTabs != null) {
                for (int i = 0; i < subTabs.size(); i++) {
                    LeftMenu menu = new LeftMenu();
                    menu.setCid(cid);
                    menu.setApiUrl(subTabs.get(i).getApiUrl());
                    menu.setTitle(subTabs.get(i).getTitle());
                    data.add(menu);
                }
            }

            categoryAdapter = new CategoryAdapter(data, getContext(), selectedCategoryPosition, true, toolBarTitle);
            defaultItemName = data.get(0).getTitle();

            if (getContext() instanceof Activity) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getLayoutManager() != null)
                            getLayoutManager().scrollToPosition(selectedCategoryPosition);
                    }
                });
            }
            setAdapter(categoryAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initForTvSeries(String cid, String urlFrag, String urlBolum, String title) {
        try {
            int dpValue = 8; // margin in dips
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d); // margin in pixels

            this.setPadding(margin, 0, 0, 0);
            this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));

            if (mType == null)
                return;

            this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            ArrayList<LeftMenu> data = new ArrayList<>();
            LeftMenu menu1 = new LeftMenu();
            menu1.setCid(cid);
            menu1.setApiUrl(urlFrag);
            menu1.setTitle("Fragmanlar");
            data.add(menu1);
            LeftMenu menu2 = new LeftMenu();
            menu2.setCid(cid);
            menu2.setApiUrl(urlBolum);
            menu2.setTitle("Bölümler");
            data.add(menu2);
            categoryAdapter = new CategoryAdapter(data, getContext(), selectedCategoryPosition, true, toolBarTitle);

            defaultItemName = data.get(0).getTitle();

            if (getContext() instanceof Activity) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getLayoutManager() != null)
                            getLayoutManager().scrollToPosition(selectedCategoryPosition);
                    }
                });
            }
            setAdapter(categoryAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initForPrograms(String cid, String cidListJson, String baslikListJson, String title) {
        try {
            int dpValue = 8; // margin in dips
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d); // margin in pixels

            this.setPadding(margin, 0, 0, 0);
            this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));

            if (mType == null)
                return;

            this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            ArrayList<String> baslikArr = new Gson().fromJson(baslikListJson, new TypeToken<ArrayList<String>>() {
            }.getType());

            ArrayList<String> cidArr = new Gson().fromJson(cidListJson, new TypeToken<ArrayList<String>>() {
            }.getType());

            ArrayList<LeftMenu> data = new ArrayList<>();
            for (int i = 0; i < baslikArr.size(); i++) {
                LeftMenu menu1 = new LeftMenu();
                menu1.setCid(cidArr.get(i));
                menu1.setApiUrl(ApiListEnums.VIDEO_BY_CATEGORY.getType());
                menu1.setTitle(baslikArr.get(i));
                data.add(menu1);
            }

            categoryAdapter = new CategoryAdapter(data, getContext(), selectedCategoryPosition, false, toolBarTitle);

            defaultItemName = data.get(0).getTitle();

            if (getContext() instanceof Activity) { // ekran ilk açıldığında da seçili sekmeyi oraya getirecek
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (getLayoutManager() != null) {
                                RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(getContext());
                                smoothScroller.setTargetPosition(selectedCategoryPosition);
                                getLayoutManager().startSmoothScroll(smoothScroller);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            setAdapter(categoryAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        try {
            int dpValue = 8; // margin in dips
            float d = getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d); // margin in pixels

            this.setPadding(margin, 0, 0, 0);
            this.setBackgroundColor(getResources().getColor(R.color.news_list_gray));

            if (mType == null)
                return;

            this.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            String mDataStr = "";
            if (mType == MenuActionType.CATEGORY_PHOTO_GALLERY) {
                mDataStr = databaseHelper.getPhotoGalleryCategory();
            } else if (mType == MenuActionType.CATEGORY_VIDEO) {
                mDataStr = databaseHelper.getVideoGalleryCategory();
            } else if (mType == MenuActionType.LIST_TV_SERIES) {
                mDataStr = databaseHelper.getTvSeriesCategory();
            }else if (mType == MenuActionType.CATEGORY_PROGRAMS) {
                mDataStr = databaseHelper.getProgramsCategory();
            }

            Gson gson = new Gson();
            final ArrayList<LeftMenu> data = gson.fromJson(mDataStr, new TypeToken<ArrayList<LeftMenu>>() {
            }.getType());
            categoryAdapter = new CategoryAdapter(data, getContext(), selectedCategoryPosition, false, "");

            if (data != null && data.size()>0)
            defaultItemName = data.get(0).getTitle();

            if (getContext() instanceof Activity) { // ekran ilk açıldığında da seçili sekmeyi oraya getirecek
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (getLayoutManager() != null) {
                                RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(getContext());
                                smoothScroller.setTargetPosition(selectedCategoryPosition);
                                getLayoutManager().startSmoothScroll(smoothScroller);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            setAdapter(categoryAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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