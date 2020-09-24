package turkuvaz.general.turkuvazgeneralwidgets.NewsList.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdSize;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.Ads.BannerAds;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.NewsListType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;

public class NewsListAdapter extends RecyclerView.Adapter {
    private ArrayList<Article> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private OnLoadMoreListener listener;
    private int loadMoreSize = -1;
    private int screenSize;
    private onSelectedNewsListener selectedListener;
    private boolean isShowCategory;
    private final byte TYPE_ADS_300x250 = 0;
    private final byte TYPE_2x2_NEWS = 1;
    private NewsListType newsListType;
    private String categoryID = "";
    boolean isShowTitle = true;
    boolean isTitleOverImage = false;
    boolean isSurmanset = false;
    boolean isHomepage;
    boolean isShowBanner = true;

    @LayoutRes
    private int layoutType = R.layout.news_list_item_grid_2x2;

    public NewsListAdapter(ArrayList<Article> dummyData, Context context) {
        if (context == null)
            return;
        this.screenSize = Preferences.getInt(context, SettingsTypes.SCREEN_WIDTH.getType(), -1);
        this.arrayList = dummyData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /** NEWS LIST TYPE DESIGN CONTROL */
        if (newsListType == NewsListType.GRID_2x2_BOX) {
            layoutType = R.layout.news_list_item_grid_2x2;
        } else if (newsListType == NewsListType.GRID_1x1_LIST) {
            layoutType = R.layout.news_list_item_grid_1x1_list;
        } else if (newsListType == NewsListType.GRID_1x1_BOX) {
            layoutType = R.layout.news_list_item_grid_1x1_box;
        } else if (newsListType == NewsListType.GRID_1X1_ROUNDED_BOX) {
            layoutType = R.layout.grid_1x1_rounded_box;
        }

        View vRoot = inflater.inflate(layoutType, parent, false);
        switch (viewType) {
            case TYPE_ADS_300x250:
                vRoot = inflater.inflate(R.layout.news_list_ads_item, parent, false);
                break;
            case TYPE_2x2_NEWS:
                vRoot = inflater.inflate(layoutType, parent, false);
                break;
        }
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (arrayList.get(position) == null && position != 0) ? TYPE_ADS_300x250 : TYPE_2x2_NEWS; // arrayList.get(position) == null ise orada reklam vardır
    }

    public void setShowBanner(boolean isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final Article currentModel = arrayList.get(position);
            final ViewHolder viewHolder = (ViewHolder) holder;
            final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);
            if (currentModel != null && currentModel.getCategoryId() != null)
                categoryID = currentModel.getCategoryId();

            if (CURRENT_ITEM_TYPE == TYPE_ADS_300x250) {
                if (viewHolder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
                    ((StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams()).setFullSpan(true);

                if (viewHolder.mFrame_ads != null && viewHolder.mFrame_ads.getChildCount() == 0) {
                    BannerAds bannerAds = new BannerAds(context, new AdSize(300, 250), GlobalMethods.getCategoryZone(context, categoryID, AdsType.ADS_300x250));
                    viewHolder.mFrame_ads.addView(bannerAds);
                }

                if (Preferences.getBoolean(context, ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), false) && !ApiListEnums.ADS_GENERAL_320x50.getApi(context).equals(""))
                    viewHolder.mFrame_ads.setPadding(viewHolder.mFrame_ads.getPaddingLeft(), viewHolder.mFrame_ads.getPaddingTop(), viewHolder.mFrame_ads.getPaddingRight(), (int) (50 * context.getResources().getDisplayMetrics().density));
            } else {
                viewHolder.mImg_newsImage.setImageDrawable(null);
                if (isShowCategory && currentModel.getTitle() != null) {
                    if (!isTitleOverImage) {
                        viewHolder.mTv_title.setVisibility(currentModel.getCategoryName() != null ? View.VISIBLE : View.GONE); // CagetoryName boş geldiği durumlar oluyordu
                        viewHolder.mTv_title.setText(currentModel.getCategoryName());
                    } else
                        viewHolder.mTv_title.setVisibility(View.GONE);
                } else
                    viewHolder.mTv_title.setVisibility(View.GONE);

                if (currentModel.getTitleShort() != null) {
                    viewHolder.mTv_description.setText(currentModel.getTitleShort());
                    if (viewHolder.mTv_descriptionShadow != null)
                        viewHolder.mTv_descriptionShadow.setText(currentModel.getTitleShort());
                }

                boolean isShowing;
                if (isSurmanset && isShowTitle) {
                    isShowing = isHomepage
                            ? (currentModel.getSurmansetBaslik() != null && currentModel.getSurmansetBaslik())
                            : (currentModel.isSurmansetBaslikKategori() != null && currentModel.isSurmansetBaslikKategori());

                } else {
                    isShowing = isShowTitle;
                }

                if (!isTitleOverImage) {
                    viewHolder.mTv_description.setVisibility(isShowing ? View.VISIBLE : View.GONE);
                    if (viewHolder.mTv_descriptionShadow != null)
                        viewHolder.mTv_descriptionShadow.setVisibility(View.GONE);
                } else {
                    viewHolder.mTv_description.setVisibility(View.GONE);
                    if (viewHolder.mTv_descriptionShadow != null)
                        viewHolder.mTv_descriptionShadow.setVisibility(View.VISIBLE);
                }
                String imagePath = "empty";
                if (currentModel.getPrimaryImage() != null && !TextUtils.isEmpty(currentModel.getPrimaryImage())) {
                    imagePath = currentModel.getPrimaryImage();
                }

                if (currentModel.getArticleSourceType() != null && currentModel.getArticleSourceType().equals("ADVERTORIAL")) {
                    String data = "";
                    if (currentModel.getUsedMethod()) {
                        String scriptStr = currentModel.getListCounterScript().toString();
                        scriptStr = scriptStr.replace("\r\n", "");
                        scriptStr = scriptStr.replace("\r", "");
                        scriptStr = scriptStr.replace("\n", "");
                        scriptStr = scriptStr.replace("\t", "");
                        data = "<!DOCTYPE html><html><head><title></title></head><body>" + scriptStr + "</body></html>";

                        viewHolder.webViewAdvertorial.setWebChromeClient(new WebChromeClient());
                        viewHolder.webViewAdvertorial.setWebViewClient(new WebViewClient());
                        viewHolder.webViewAdvertorial.clearCache(true);
                        viewHolder.webViewAdvertorial.clearHistory();
                        viewHolder.webViewAdvertorial.getSettings().setJavaScriptEnabled(true);
                        viewHolder.webViewAdvertorial.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                        viewHolder.webViewAdvertorial.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
                    }
                    Log.i("<*>DK", "advertorialWebView : " + data);
                }
                viewHolder.mImg_newsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
                if (!imagePath.equals("empty")) {
                    Glide.with(context.getApplicationContext()).asBitmap().load(imagePath).listener(
                            new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                    ((Activity) context).runOnUiThread(() -> {
                                        try {
                                            int width = bitmap.getWidth();
                                            int height = bitmap.getHeight();

                                            if (newsListType == NewsListType.GRID_2x2_BOX) {
                                                viewHolder.mImg_newsImage.getLayoutParams().height = (screenSize / 2) * height / width;
                                            } else if (newsListType == NewsListType.GRID_1x1_LIST) {
                                                viewHolder.mImg_newsImage.getLayoutParams().height = (int) (screenSize / 2.7f) * height / width;
                                            } else if (newsListType == NewsListType.GRID_1x1_BOX || newsListType == NewsListType.GRID_1X1_ROUNDED_BOX) {
                                                viewHolder.mImg_newsImage.getLayoutParams().height = (screenSize) * height / width;
                                            }
                                            if (currentModel.getVideoTypeId() != null)
                                                viewHolder.imgVideoIcon.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            Log.i("TAG", "run: " + e.getMessage());
                                            e.printStackTrace();
                                        }
                                        viewHolder.mImg_newsImage.setImageBitmap(bitmap);
                                    });
                                    return false;
                                }
                            }
                    ).submit();
                } /*else {
                    viewHolder.mImg_newsImage.setImageResource(ErrorImageType.getIcon(context.getApplicationInfo().packageName));
                }*/

                viewHolder.mCard_root.setOnClickListener(v -> {
                    if (selectedListener != null)
                        selectedListener.onSelect(arrayList, position, ClickViewType.NEWS_INFINITY_LIST);
                });
            }

            //TODO Pagination yapısı için yapılan kontroller
            if (position >= getItemCount() - 1 && listener != null && loadMoreSize != getItemCount() - 1) {
                loadMoreSize = getItemCount() - 1;
                listener.onLoadMore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_title, mTv_description, mTv_descriptionShadow;
        ImageView mImg_newsImage;
        ImageView imgVideoIcon;
        CardView mCard_root;
        FrameLayout mFrame_ads;
        WebView webViewAdvertorial;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_newsTitle);
            mTv_description = itemView.findViewById(R.id.tv_newsDescription);
            mTv_descriptionShadow = itemView.findViewById(R.id.tv_newsDescription2);
            mImg_newsImage = itemView.findViewById(R.id.img_newsImage);
            mCard_root = itemView.findViewById(R.id.card_newsRoot);
            mFrame_ads = itemView.findViewById(R.id.frame_newsListAds);
            imgVideoIcon = itemView.findViewById(R.id.imgVideoIcon);
            webViewAdvertorial = itemView.findViewById(R.id.webViewAdvertorial);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.listener = listener;
    }

    public void notifyDataSet(ArrayList<Article> list, int count) {
        if (!ApiListEnums.ADS_GENERAL_300x250.getApi(context).equals("")) { // 300x250 api'den gelmiş olması gerek. gelmediyse null kayıt atma
            if (arrayList.get(arrayList.size() - 1) != null && isShowBanner) // son item reklam ise alt satırda tekrar reklam ekleme
                list.add(0, null);
            list.add(list.size(), null);
        }
        arrayList.addAll(list);
        notifyItemRangeInserted(getItemCount(), count);
    }

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public void setShowCategory(boolean showCategory) {
        isShowCategory = showCategory;
    }

    public void setNewsListType(NewsListType newsListType) {
        this.newsListType = newsListType;
    }

    public void setShowTitle(boolean isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public void setTitleOverImage(boolean titleOverImage) {
        isTitleOverImage = titleOverImage;
    }

    public void setHomepage(boolean isHomepage) {
        this.isHomepage = isHomepage;
    }

    public void setSurmanset(boolean isSurmanset) {
        this.isSurmanset = isSurmanset;
    }

}