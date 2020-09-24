package turkuvaz.sdk.galleryslider.SliderGallery.MansetView;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.turquaz.videogallery.R;


import turkuvaz.general.turkuvazgeneralwidgets.TurkuvazTextView.TTextView;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Preferences;

import java.util.ArrayList;


public class VideoGalleryPagerAdapter extends PagerAdapter {
    private ArrayList<Article> mResponse;
    private onSelectedNewsListener selectedListener;
    private Activity mContext;
    private onPagerSizeComplate onPagerSizeComplate;
    private Integer Width;
    private boolean titleVisible;
    private String customPageName = "";

    public VideoGalleryPagerAdapter(Activity context, ArrayList<Article> response, boolean titleVisible) {
        mResponse = response;
        mContext = context;
        this.titleVisible = titleVisible;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(collection.getContext()).inflate(R.layout.item_gallery_slider, collection, false);

        try {
            try {
                final Article currentResponse = mResponse.get(position);
                final ImageView mImg_mansetImage = layout.findViewById(R.id.img_mansetImage);
                final ImageView imgVideoIcon = layout.findViewById(R.id.imgVideoIcon);
                TextView mTv_title = layout.findViewById(R.id.tv_galleryTitle);

                if (!customPageName.equals("")) {
                    ((TTextView) layout.findViewById(R.id.tv_CategoryName)).setTextColor(Color.parseColor(Preferences.getString(mContext, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
                    ((TTextView) layout.findViewById(R.id.tv_CategoryName)).setText(currentResponse.getCategoryName());
                    ((TTextView) layout.findViewById(R.id.tv_CategoryName)).setVisibility(View.VISIBLE);
                    ((TTextView) layout.findViewById(R.id.tv_galleryTitle)).setTextSize(17);
                    mTv_title.setGravity(Gravity.CENTER);
                    imgVideoIcon.setImageResource(R.drawable.ic_play_sabah_spor); // bu buton özelleştirilebilir. var olandan ya da url den alınabilir
                }

                if (!titleVisible) // fikriyatta videoslider'da başlık istenmediği için yapıldı
                    mTv_title.setVisibility(View.GONE);

                if (currentResponse != null && currentResponse.getTitleShort() != null && !TextUtils.isEmpty(currentResponse.getTitleShort()))
                    mTv_title.setText(currentResponse.getTitleShort());

                if (mContext != null && currentResponse != null && currentResponse.getPrimaryImage() != null && !TextUtils.isEmpty(currentResponse.getPrimaryImage())) {
                    Glide.with(mContext.getApplicationContext()).asBitmap().load(currentResponse.getPrimaryImage()).listener(
                            new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                    mContext.runOnUiThread(() -> {
                                        try {
                                            int width = bitmap.getWidth();
                                            int height = bitmap.getHeight();

                                            mImg_mansetImage.getLayoutParams().height = Preferences.getInt(mContext, SettingsTypes.SCREEN_WIDTH.getType(),-1) * height / width;
                                            if (onPagerSizeComplate != null)
                                                onPagerSizeComplate.pagerSize(Preferences.getInt(mContext, SettingsTypes.SCREEN_WIDTH.getType(),-1) * height / width);
                                        } catch (Exception r) {
                                            Log.d("","");
                                        }
                                        imgVideoIcon.setVisibility(View.VISIBLE);
                                        mImg_mansetImage.setImageBitmap(bitmap);
                                    });
                                    return false;
                                }
                            }
                    ).submit();
                }else {
                    mImg_mansetImage.setImageResource(ErrorImageType.getIcon(mContext.getApplicationInfo().packageName));
//                    if (!customPageName.equals("")) // imgVideoIcon.setVisibility(View.VISIBLE); enable edilmeli eğer resim yokken de video ikonu gösterilecekse
//                        imgVideoIcon.setImageResource(R.drawable.ic_play_sabah_spor);
                }

                mImg_mansetImage.setOnClickListener(v -> {
                    if (selectedListener != null)
                        selectedListener.onSelect(mResponse, position, ClickViewType.VIDEO_GALLERY_SLIDER);
                });
                collection.addView(layout);
                return layout;

            } catch (IndexOutOfBoundsException e) {
                return layout;
            }
        } catch (NullPointerException e) {
            return layout;
        }
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

    public void setOnPagerSizeComplate(onPagerSizeComplate onPagerSizeComplate) {
        this.onPagerSizeComplate = onPagerSizeComplate;
    }

    public void setCustomPageName(String customPageName) {
        this.customPageName = customPageName;
    }
}
