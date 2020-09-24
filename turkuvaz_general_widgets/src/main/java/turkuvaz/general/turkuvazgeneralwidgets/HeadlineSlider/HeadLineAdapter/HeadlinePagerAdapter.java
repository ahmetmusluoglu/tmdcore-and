package turkuvaz.general.turkuvazgeneralwidgets.HeadlineSlider.HeadLineAdapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Preferences;

public class HeadlinePagerAdapter extends PagerAdapter {
    private ArrayList<Article> mResponse;
    private onSelectedNewsListener selectedListener;
    private Activity mContext;
    private onPagerSizeComplate onPagerSizeComplate;
    private boolean isTitleVisible = false;
    private boolean isSurmanset = false;
    private boolean isHomepage = false;
    private String customPageName = "";

    public HeadlinePagerAdapter(Activity context, ArrayList<Article> response) {
        mResponse = response;
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(collection.getContext()).inflate(R.layout.head_line_item, collection, false);
        if (mResponse == null || mResponse.size() == 0) return layout;
        final Article currentResponse = mResponse.get(position);
        final ImageView mImg_mansetImage = layout.findViewById(R.id.img_mansetImage);
        final ImageView imgVideoIcon = layout.findViewById(R.id.imgVideoIcon);
        final TextView mTv_headlineTitle = layout.findViewById(R.id.tv_headlineTitle);

        if (!customPageName.equals("")) // custom sayfaların manşetlerinde, indicator ile title arasında boşluk olmalı.
            mTv_headlineTitle.setPadding(mTv_headlineTitle.getPaddingLeft(), mTv_headlineTitle.getPaddingTop(), mTv_headlineTitle.getPaddingRight(), 100);

        try {
            boolean isShowTitle;
            if (isSurmanset && isTitleVisible) {
                isShowTitle = isHomepage
                        ? (currentResponse.getSurmansetBaslik() != null && currentResponse.getSurmansetBaslik())
                        : (currentResponse.isSurmansetBaslikKategori() != null && currentResponse.isSurmansetBaslikKategori());
            } else {
                isShowTitle = isTitleVisible;
            }
            mTv_headlineTitle.setVisibility(isShowTitle ? View.VISIBLE : View.GONE);
            mTv_headlineTitle.setText(isTitleVisible && currentResponse.getTitleShort() != null && !TextUtils.isEmpty(currentResponse.getTitle()) ? currentResponse.getTitleShort() : "");

            if (mContext != null && currentResponse != null && currentResponse.getPrimaryImage() != null && !TextUtils.isEmpty(currentResponse.getPrimaryImage())) {
                Glide.with(mContext.getApplicationContext()).asBitmap().load(currentResponse.getPrimaryImage()).listener(
                        new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                mContext.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            int width = bitmap.getWidth();
                                            int height = bitmap.getHeight();

                                            mImg_mansetImage.getLayoutParams().height = Preferences.getInt(mContext, SettingsTypes.SCREEN_WIDTH.getType(), -1) * height / width;
                                            if (onPagerSizeComplate != null)
                                                onPagerSizeComplate.pagerSize(Preferences.getInt(mContext, SettingsTypes.SCREEN_WIDTH.getType(), -1) * height / width);
                                        } catch (Exception r) {
                                            r.printStackTrace();
                                        }
                                        mImg_mansetImage.setImageBitmap(bitmap);
                                        if (currentResponse.getVideoTypeId() != null)//for video-play icon...
                                            imgVideoIcon.setVisibility(View.VISIBLE);
                                    }
                                });
                                return false;
                            }
                        }
                ).submit();
            }else {
                mImg_mansetImage.setImageResource(ErrorImageType.getIcon(mContext.getApplicationInfo().packageName));
            }


            mImg_mansetImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedListener != null)
                        selectedListener.onSelect(mResponse, position, ClickViewType.HEADLINE_SLIDER);
                }
            });
            collection.addView(layout);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mResponse == null ? 0 : mResponse.size();
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

    public interface onSelectedNewsListener {
        void onSelect(ArrayList<Article> selectedNews, int position, ClickViewType clickViewType);
    }

    public void setSelectedListener(onSelectedNewsListener _selectedListener) {
        selectedListener = _selectedListener;
    }

    public interface onPagerSizeComplate {
        void pagerSize(int pagerSize);
    }

    public void setOnPagerSizeComplate(HeadlinePagerAdapter.onPagerSizeComplate onPagerSizeComplate) {
        this.onPagerSizeComplate = onPagerSizeComplate;
    }

    public void setCustomPageName(String customPageName) {
        this.customPageName = customPageName;
    }

    public void setTitleVisible(boolean isTitleVisible) {
        this.isTitleVisible = isTitleVisible;
    }

    public void setSurmanset(boolean isSurmanset) {
        this.isSurmanset = isSurmanset;
    }

    public void setHomepage(boolean isHomepage) {
        this.isHomepage = isHomepage;
    }
}
