package turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import android.text.Html;
import android.text.TextUtils;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.Utils.PinchImageView;
import turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery.Utils.SwipeToDismissTouchListener;
import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.general.turkuvazgeneralwidgets.PushDialog.PushDialog;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.EventBus.PushDialogEvent;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ErrorImageType;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;

public class GalleryDetailsActivity extends AppCompatActivity {

    private PinchImageView mImg_detailImage;
    private String mImagePath, mSharePath, mImageTitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase,""));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infinity_detail_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            // toolbar text color
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(GalleryDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));
            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(GalleryDetailsActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(GalleryDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        }

        mImg_detailImage = findViewById(R.id.img_detailImage);

        try {
            if (getActionBar() != null)
                getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(GalleryDetailsActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(GalleryDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mImg_detailImage.setOnTouchListener(SwipeToDismissTouchListener.createFromView(this, mImg_detailImage, getSwipeToDismissCallback()));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.containsKey("sharePath") || !bundle.containsKey("imageTitle")) {
                if (bundle.containsKey("imagePath")) {
                    mImagePath = getIntent().getStringExtra("imagePath");
                    if (mImagePath != null && !TextUtils.isEmpty(mImagePath)) {
                        Glide.with(getApplicationContext()).asBitmap().load(mImagePath).listener(
                                new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                        Toast.makeText(getApplicationContext(), "Hata oluştu", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                        runOnUiThread(() -> {
                                            try {
                                                mImg_detailImage.setImageBitmap(bitmap);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        return false;
                                    }
                                }
                        ).submit();
                    }else {
                        mImg_detailImage.setImageResource(ErrorImageType.getIcon(getApplicationContext().getApplicationInfo().packageName));
                    }
                    return;
                }
            }

            if (bundle.containsKey("imagePath") && bundle.containsKey("sharePath") && bundle.containsKey("imageTitle")) {
                mImagePath = getIntent().getStringExtra("imagePath");
                mSharePath = getIntent().getStringExtra("sharePath");
                mImageTitle = getIntent().getStringExtra("imageTitle");

                if (mImagePath != null && !TextUtils.isEmpty(mImagePath)) {
                    Glide.with(getApplicationContext()).asBitmap().load(mImagePath).listener(
                            new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                    Toast.makeText(getApplicationContext(), "Hata oluştu", Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(final Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                    runOnUiThread(() -> {
                                        try {
                                            mImg_detailImage.setImageBitmap(bitmap);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    return false;
                                }
                            }
                    ).submit();
                }
            } else {
                mImg_detailImage.setImageResource(ErrorImageType.getIcon(getApplicationContext().getApplicationInfo().packageName));
                Toast.makeText(this, "Fotoğraf açılamıyor. Hata kodu: 1", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Fotoğraf açılamıyor. Hata kodu: 2", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PushDialogEvent event) {
        try {
            PushDialog pushDialog = new PushDialog(this);
            pushDialog.setPushModel(event.getPushModel());
            pushDialog.setOnPushDialog(new PushDialog.onPushDialog() {
                @Override
                public void closeDialog() {

                }

                @Override
                public void openNews(PushModel pushModel) {
                    if (pushDialog != null && pushDialog.isShowing())
                        pushDialog.closeDialog();
                    Utils.setOpenedPush(GalleryDetailsActivity.this, pushModel.getPid());
                    /** NOTIFICATION CONTROL */
                    if (pushModel != null) {
                        switch (pushModel.getTypestr()) {
                            case NEWS:
                                GlobalIntent.openNewsWithID(GalleryDetailsActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryDetailsActivity.this), pushModel.getRefid());
                                break;
                            case VIDEO:
                                GlobalIntent.openVideoDetailsWithID(GalleryDetailsActivity.this, pushModel.getRefid(), "AD_TAG");
                                break;
                            case AUTHOR:
                                GlobalIntent.openColumnistWithID(GalleryDetailsActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(GalleryDetailsActivity.this), pushModel.getRefid());
                                break;
                            case BROWSER:
                                GlobalIntent.openInternalBrowser(GalleryDetailsActivity.this, pushModel.getU());
                                break;
                            case GALLERY:
                                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryDetailsActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryDetailsActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(GalleryDetailsActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(GalleryDetailsActivity.this, showGalleryModel, ExternalTypes.ALBUM);
                                break;
                            case PHOTO_NEWS:
                                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(GalleryDetailsActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(GalleryDetailsActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(GalleryDetailsActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(GalleryDetailsActivity.this, showGalleryModel2, ExternalTypes.PHOTO_NEWS);
                                break;
                            case LIVE_STREAM:
                                GlobalIntent.openLiveStreamWithURL(GalleryDetailsActivity.this, "Canlı Yayın", pushModel.getExcurl(), ApiListEnums.ADS_PREROLL.getApi(GalleryDetailsActivity.this));
                                break;
                            case UNDEFINED:
                                break;
                        }
                    }
                }
            });

            if (!this.isFinishing())
                pushDialog.showDialog();

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishGallery() {
        finish();
        overridePendingTransition(0, 0);
    }

    public void shareImage() {
        new TurkuvazAnalytic(this).setIsPageView(false).setLoggable(true).setEventName(AnalyticsEvents.ACTION_IMAGE_SHARE.getEventName()).sendEvent();

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, mImageTitle + " " + mSharePath);
        startActivity(Intent.createChooser(share, getResources().getString(R.string.paylas)));
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    SwipeToDismissTouchListener.Callback getSwipeToDismissCallback() {
        return new SwipeToDismissTouchListener.Callback() {
            @Override
            public void onDismiss() {
                finish();
                overridePendingTransition(0, R.anim.anim_slide_out);
            }

            @Override
            public void onMove(float translationY) { /* intentionally blank */ }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishGallery();
        } else if (item.getItemId() == R.id.action_share) {
            shareImage();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.containsKey("sharePath") || !bundle.containsKey("imageTitle"))
                item.setVisible(false);
        }
        DrawableCompat.setTint(item.getIcon(), Color.parseColor(Preferences.getString(GalleryDetailsActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
        return true;
    }
}