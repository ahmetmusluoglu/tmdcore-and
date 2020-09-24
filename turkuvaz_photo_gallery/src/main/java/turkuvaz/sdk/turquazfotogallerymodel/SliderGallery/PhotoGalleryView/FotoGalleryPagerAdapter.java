package turkuvaz.sdk.turquazfotogallerymodel.SliderGallery.PhotoGalleryView;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Display;
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
import com.turquaz.turquazfotogallerymodel.R;

import java.util.ArrayList;

import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.GlobalModels.Article;
import turkuvaz.sdk.models.Models.Enums.ClickViewType;
import turkuvaz.sdk.models.Models.Preferences;

/**
 * @author Nikita Olifer
 */
public class FotoGalleryPagerAdapter extends PagerAdapter {
    private ArrayList<Article> mResponse;
    private onSelectedNewsListener selectedListener;

    private Activity mContext;
    private onPagerSizeComplate onPagerSizeComplate;
    private Integer Width;

    public FotoGalleryPagerAdapter(Activity context, ArrayList<Article> response) {
        mResponse = response;
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(collection.getContext()).inflate(R.layout.photo_gallery_slider_item, collection, false);
        try {
            try {
                final Article currentResponse = mResponse.get(position);
                final ImageView mImg_mansetImage = layout.findViewById(R.id.img_mansetImage);
                TextView mTv_title = layout.findViewById(R.id.tv_galleryTitle);

                if (currentResponse != null && currentResponse.getTitle() != null && !TextUtils.isEmpty(currentResponse.getTitle()))
                    mTv_title.setText(currentResponse.getTitle());

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
                                        }
                                    });
                                    return false;
                                }
                            }
                    ).submit();
                } else {
                    mImg_mansetImage.setImageResource(ErrorImageType.getIcon(mContext.getApplicationInfo().packageName));
                }

                mImg_mansetImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedListener != null)
                            selectedListener.onSelect(mResponse, position, ClickViewType.PHOTO_GALLERY_SLIDER);
                    }
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

}
