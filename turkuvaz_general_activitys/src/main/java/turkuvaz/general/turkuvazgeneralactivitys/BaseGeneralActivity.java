package turkuvaz.general.turkuvazgeneralactivitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralactivitys.NotificationsNews.NotificationsNewsActivity;
import turkuvaz.general.turkuvazgeneralwidgets.PushDialog.PushDialog;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.global.EventBus.PushDialogEvent;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsAppRef;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.ExternalTypes;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class BaseGeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            // toolbar arkaplan rengi
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(Preferences.getString(BaseGeneralActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), "#FFFFFF"))));
            // back butonu rengi
            DrawableCompat.setTint(getResources().getDrawable(R.drawable.ic_arrow_back_24dp), Color.parseColor(Preferences.getString(BaseGeneralActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);

            // toolbar text color
            getSupportActionBar().setTitle(Html.fromHtml("<font color=" + Color.parseColor(Preferences.getString(BaseGeneralActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), "#FFFFFF")) + ">" + "" + "</font>"));
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
                    Utils.setOpenedPush(BaseGeneralActivity.this, pushModel.getPid());
                    /** NOTIFICATION CONTROL */
                    if (pushModel != null) {
                        switch (pushModel.getTypestr()) {
                            case NEWS:
                                GlobalIntent.openNewsWithID(BaseGeneralActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(BaseGeneralActivity.this), pushModel.getRefid());
                                break;
                            case VIDEO:
                                GlobalIntent.openVideoDetailsWithID(BaseGeneralActivity.this, pushModel.getRefid(), "AD_TAG");
                                break;
                            case AUTHOR:
                                GlobalIntent.openColumnistWithID(BaseGeneralActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(BaseGeneralActivity.this), pushModel.getRefid());
                                break;
                            case BROWSER:
                                GlobalIntent.openInternalBrowser(BaseGeneralActivity.this, pushModel.getU());
                                break;
                            case GALLERY:
                                ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(BaseGeneralActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(BaseGeneralActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(BaseGeneralActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(BaseGeneralActivity.this, showGalleryModel, ExternalTypes.ALBUM);
                                break;
                            case PHOTO_NEWS:
                                ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(BaseGeneralActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(BaseGeneralActivity.this), pushModel.getRefid(), ApiListEnums.DETAILS_VIDEO.getApi(BaseGeneralActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                                GlobalIntent.openInfinityGallery(BaseGeneralActivity.this, showGalleryModel2, ExternalTypes.PHOTO_NEWS);
                                break;
                            case LIVE_STREAM:
                                GlobalIntent.openLiveStreamWithURL(BaseGeneralActivity.this, "Canlı Yayın", pushModel.getExcurl(), ApiListEnums.ADS_PREROLL.getApi(BaseGeneralActivity.this));
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

    private void setOpenedPush(int pushID) {
        RestInterface mRestInterfacePush = ApiClient.getClientAPNS().create(RestInterface.class);
        mRestInterfacePush.setOpenedPush(
                ApplicationsAppRef.getAppRef(getApplicationInfo().packageName),
                getApplicationInfo().packageName,
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID),
                ApiListEnums.DEVICE_PUSH_TOKEN.getApi(this),
                pushID,
                false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("PUSH OPENED", s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}