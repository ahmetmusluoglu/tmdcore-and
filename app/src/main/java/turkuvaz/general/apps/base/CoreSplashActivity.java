package turkuvaz.general.apps.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.apps.BuildConfig;
import turkuvaz.general.apps.R;
import turkuvaz.general.turkuvazgeneralwidgets.ErrorDialog.ErrorDialog;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.Database.DatabaseHelper;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.MyContextWrapper;
import turkuvaz.sdk.models.Models.AdsZones.AdsDatum;
import turkuvaz.sdk.models.Models.ConfigBase.BaseConfigData;
import turkuvaz.sdk.models.Models.ConfigBase.Colors;
import turkuvaz.sdk.models.Models.ConfigBase.Data;
import turkuvaz.sdk.models.Models.ConfigBase.LeftMenu;
import turkuvaz.sdk.models.Models.ConfigBase.RateThisApp;
import turkuvaz.sdk.models.Models.ConfigBase.Settings;
import turkuvaz.sdk.models.Models.ConfigBase.SettingsMenu;
import turkuvaz.sdk.models.Models.ConfigBase.ToolbarMenu;
import turkuvaz.sdk.models.Models.ConfigFirstLook.FirstLookData;
import turkuvaz.sdk.models.Models.ConfigShared.SharedConfigData;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsWebUrls;
import turkuvaz.sdk.models.Models.Enums.IconTypes;
import turkuvaz.sdk.models.Models.Enums.MenuActionType;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.InfinityGalleryModel.ShowGalleryModel;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;

/**
 * Created by Ahmet MUŞLUOĞLU on 1/23/2020.
 */
public class CoreSplashActivity extends AppCompatActivity {
    private String TAG = "CoreSplashActivity";
    private RestInterface mRestInterface;
    private ArrayList<LeftMenu> mVideoCategoryList = new ArrayList<>();
    private ArrayList<LeftMenu> mGalleryCategoryList = new ArrayList<>();
    private ArrayList<LeftMenu> mProgramsCategoryList = new ArrayList<>();
    private Gson gson = new Gson();
    private ErrorDialog errorDialog;

    private Context getInstance() {
        return this;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_activity_splash);
        getHMSToken();
        setInitialValues();
        /**Baslangic degeri 0 verildi. Hangi uygulama calisacaksa initApp()'te ID degeri verilecek*/
        initApp();
    }

    /**
     * Default degerler bunlar...
     */
    public void setInitialValues() {
        /** SET APP COLOR SETTINGS */
    }

    public void initApp() {
        this.mRestInterface = ApiClient.getClientApi(CoreSplashActivity.this).create(RestInterface.class);
        this.errorDialog = new ErrorDialog(getInstance());

        Preferences.save(this, "token", FirebaseInstanceId.getInstance().getToken()); // settings'de token alırken kullanılacak.

        ((ProgressBar) findViewById(R.id.pb_splashLoading)).getIndeterminateDrawable().setColorFilter(Color.parseColor(Preferences.getString(this, ApiListEnums.SPLASH_TEXT.getType(), "#FFFFFF")), PorterDuff.Mode.SRC_IN);
        ((TextView) findViewById(R.id.tv_splashLoadingInfo)).setTextColor(Color.parseColor(Preferences.getString(this, ApiListEnums.SPLASH_TEXT.getType(), "#FFFFFF")));
        ((TextView) findViewById(R.id.tv_splashLoadingInfo)).setText(getResources().getString(R.string.yukleniyor));

        ImageView imgSplash = findViewById(R.id.imgSplash);
        ImageView imgSplash2 = findViewById(R.id.imgSplash2);
        RelativeLayout rlTheme2 = findViewById(R.id.rlTheme2);
        if (CoreApp.getSettings().isSplashOnlyImage()) {
            imgSplash.setImageResource(CoreApp.getSettings().getSplashImageResId());
            rlTheme2.setVisibility(View.GONE);
        } else {
            imgSplash2.setImageResource(CoreApp.getSettings().getSplashImageResId());
            rlTheme2.setVisibility(View.VISIBLE);
            rlTheme2.setBackgroundColor(Color.parseColor(Preferences.getString(this, ApiListEnums.SPLASH_BACK.getType(), "#FFFFFF")));
        }

//        appIsOpen();

        Preferences.save(this, "isOpenCustomPage", false);
//        Preferences.removeCustomKeys(CoreSplashActivity.this); // custom sayfalardaki renkler sıfırlanıyor. Normal sayfalar ile karışmaması için

        /** START GET ALL CONFIG */
        getFirstLookConfig();

        /** NOTIFICATION REGISTER */
        GlobalMethods.getPushToken(this);

        /** SET SCREEN WIDTH */
        GlobalMethods.getScreenWidth(this);
    }

    private void appIsOpen() {
       /* if (getIntent().getData() != null && getIntent().getData().toString().contains(BuildConfig.DEEP_LINK + "://open")) {
            Log.i(TAG, "appIsOpen: BuildConfig.DEEP_LINK-1");
            if (Preferences.getBoolean(getApplicationContext(), "appIsOpen")) {
                try {
                    Intent startIntent = new Intent(CoreSplashActivity.this, CoreHomeActivity.class);
//                    takvim://open?id=aaf5c727-4398-4f7b-8032-37eeff33a9f7&type=articlesource
                    String[] dizi = getIntent().getData().toString().split("/");
//                    startActivity(startIntent.putExtra("id", "3a89ab39-b336-4310-ba30-cfa72f39b4b6").putExtra("type", "articlesource"));
                    startActivity(startIntent.putExtra("id", dizi[dizi.length - 1]).putExtra("type", dizi[dizi.length - 2]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/

        /*if (Preferences.getBoolean(getApplicationContext(), "appIsOpen")) {
            if (getIntent().getData() != null && (getIntent().getData().toString().contains(BuildConfig.DEEP_LINK + "://open") || getIntent().getData().toString().contains(BuildConfig.DEEP_LINK))) {
                Bundle bundle = new Bundle();
                bundle.putString("deepLink", getIntent().getData().toString());
                controlDeepLink2(bundle);
            }
        }*/
    }

    public void controlDeepLink2(Bundle bundle) {
        if (bundle != null && bundle.containsKey("deepLink")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.getDefault());

                String[] dizi = bundle.getString("deepLink").replace(BuildConfig.DEEP_LINK + "://open?", "").split("&");
                Log.i("TAG", "isUpdateReady: BuildConfig.DEEP_LINK-3");
                String id = dizi[dizi.length - 2].replace("id=", "");
                String type = dizi[dizi.length - 1].replace("type=", "");
                if (!id.equals("") && !type.equals("")) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("deep_link_yuklu").child(sdf.format(new Date())).child("id").setValue(id); // *** mid olarak gönderdiğimiz değere bakılacak. idOrj değeri firebase'e boş atarsa, tüm mid'leri id'ye çevirelim sorun olmaz paket adını almaz yani ***
                    mDatabase.child("deep_link_yuklu").child(sdf.format(new Date())).child("type").setValue(type);

                    switch (type) {
                        case "news":
                            GlobalIntent.openNewsWithID(this, ApiListEnums.DETAILS_ARTICLE.getApi(this), id);
                            break;
                        case "video":
                            GlobalIntent.openVideoDetailsWithID(CoreSplashActivity.this, id, "AD_TAG");
                            break;
                        case "articlesource":
                            GlobalIntent.openColumnistWithID(this, ApiListEnums.DETAILS_ARTICLE.getApi(this), id);
                            break;
                        case "gallery":
                            ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(this), id, ApiListEnums.DETAILS_VIDEO.getApi(this), IconTypes.getIcon(getApplicationInfo().packageName));
                            GlobalIntent.openInfinityGallery(this, showGalleryModel, ALBUM);
                            break;
                        case "fotohaber":
                            ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(CoreSplashActivity.this), ApplicationsWebUrls.getDomain(getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(CoreSplashActivity.this), id, ApiListEnums.DETAILS_VIDEO.getApi(CoreSplashActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                            GlobalIntent.openInfinityGallery(CoreSplashActivity.this, showGalleryModel2, PHOTO_NEWS);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /** DEEP LINK CONTROL */
        String settingsJson = Preferences.getString(CoreSplashActivity.this, ApiListEnums.CATEGORY_SETTINGS.getType(), "");
        Settings settings = new Gson().fromJson(settingsJson, Settings.class);
        boolean isActiveDeeplink = settings != null && settings.getOthers() != null && settings.getOthers().getDeeplink() != null && settings.getOthers().getDeeplink().isActive();
        String deepLinkId = Preferences.getString(CoreSplashActivity.this, "deepLinkId", "");
        String deepLinkType = Preferences.getString(CoreSplashActivity.this, "deepLinkType", "");
        String deepLinkDefault = Preferences.getString(CoreSplashActivity.this, "deepLinkDefault", "");

        Log.i("TAG", "controlDeepLink: " + deepLinkId);
        Log.i("TAG", "controlDeepLink: " + deepLinkType);
        Log.i("TAG", "controlDeepLink: " + deepLinkDefault);
        if (!deepLinkId.equals("") && !deepLinkType.equals("") && isActiveDeeplink) {
            Preferences.save(CoreSplashActivity.this, "deepLinkId", ""); // 1 defa çalışınca sıfırlanacak ve artık buraya düşmeyecek
            Preferences.save(CoreSplashActivity.this, "deepLinkType", "");

            String tarih = new SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.getDefault()).format(new Date());

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("deep_link").child(tarih).child("id").setValue(deepLinkId);
            mDatabase.child("deep_link").child(tarih).child("type").setValue(deepLinkType);
            mDatabase.child("deep_link").child(tarih).child("default").setValue(deepLinkDefault);

            switch (deepLinkType) {
                case "news":
                    GlobalIntent.openNewsWithID(CoreSplashActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(CoreSplashActivity.this), deepLinkId);
                    break;
                case "video":
                    GlobalIntent.openVideoDetailsWithID(CoreSplashActivity.this, deepLinkId, "AD_TAG");
                    break;
                case "articlesource":
                    GlobalIntent.openColumnistWithID(CoreSplashActivity.this, ApiListEnums.DETAILS_ARTICLE.getApi(CoreSplashActivity.this), deepLinkId);
                    break;
                case "gallery":
                    ShowGalleryModel showGalleryModel = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(CoreSplashActivity.this), ApplicationsWebUrls.getDomain(CoreSplashActivity.this.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(CoreSplashActivity.this), deepLinkId, ApiListEnums.DETAILS_VIDEO.getApi(CoreSplashActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                    GlobalIntent.openInfinityGallery(CoreSplashActivity.this, showGalleryModel, ALBUM);
                    break;
                case "fotohaber":
                    ShowGalleryModel showGalleryModel2 = new ShowGalleryModel(ApiListEnums.ADS_GENERAL_300x250.getApi(CoreSplashActivity.this), ApplicationsWebUrls.getDomain(CoreSplashActivity.this.getApplicationInfo().packageName), ApiListEnums.DETAILS_GALLERY.getApi(CoreSplashActivity.this), deepLinkId, ApiListEnums.DETAILS_VIDEO.getApi(CoreSplashActivity.this), IconTypes.getIcon(getApplicationInfo().packageName));
                    GlobalIntent.openInfinityGallery(CoreSplashActivity.this, showGalleryModel2, PHOTO_NEWS);
                    break;
                default:
                    break;
            }
        } else {
            Preferences.save(CoreSplashActivity.this, "deepLinkId", ""); // 1 defa çalışınca sıfırlanacak ve artık buraya düşmeyecek
            Preferences.save(CoreSplashActivity.this, "deepLinkType", "");
        }
    }

    private void getFirstLookConfig() {
        Log.i(TAG, "getFirstLookConfig-ID : " + CoreApp.getSettings().getConfigId());
        mRestInterface.getFirstLookConfig(CoreApp.getSettings().getConfigId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FirstLookData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FirstLookData firstLookData) {
                        try {
                            if (firstLookData != null && firstLookData.getMeta() != null)
                                if (firstLookData.getMeta().getStatusCode() == 200) {
                                    Preferences.save(getInstance(), ApiListEnums.SHARED_CONFIG_URL.getType(), Preferences.getString(CoreSplashActivity.this, "connectedurl", "https://api.tmgrup.com.tr") + firstLookData.getData().getSharedConfigUrl());
                                    Preferences.save(getInstance(), ApiListEnums.ANDROID_CONFIG_URL.getType(), Preferences.getString(CoreSplashActivity.this, "connectedurl", "https://api.tmgrup.com.tr") + firstLookData.getData().getAndroidConfigUrl());
                                    Log.i(TAG, "onNext-URL :  " + Preferences.getString(CoreSplashActivity.this, "connectedurl", "https://api.tmgrup.com.tr") + firstLookData.getData().getAndroidConfigUrl());
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && errorDialog != null && !errorDialog.isShowing() && !isFinishing())
                            errorDialog.setThrowable(e).setSubTitle(getResources().getString(R.string.hata_olustu)).setErrorFromClass("CoreSplashActivity * getFirstLookConfigTakvim").init();
                    }

                    @Override
                    public void onComplete() {
                        getSharedConfigUrl();
                    }
                });
    }

    private void getSharedConfigUrl() {
        String apiPath = Preferences.getString(getInstance(), ApiListEnums.SHARED_CONFIG_URL.getType(), "");
        mRestInterface.getSharedConfig(apiPath).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SharedConfigData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SharedConfigData sharedConfigData) {
                        try {
                            if (sharedConfigData != null && sharedConfigData.getMeta() != null)
                                if (sharedConfigData.getMeta().getStatusCode() == 200) {
                                    /** SET GDPR - KVKK  */
                                    setGDPRandKVKK(sharedConfigData);

                                    /** SET CONTENT CSS */
                                    setCSS(sharedConfigData);

                                    /** SET GOOGLE DFP URL */
                                    Preferences.save(getInstance(), ApiListEnums.GOOGLE_DFP_URL.getType(), sharedConfigData.getData().getApiGoogleDfpUrl());

                                    /** SET LIVESTREAM URL */
                                    Preferences.save(getInstance(), ApiListEnums.LIVE_STREAM_URL.getType(), sharedConfigData.getData().getLiveStreamUrl() != null ? sharedConfigData.getData().getLiveStreamUrl() : "");
                                    Preferences.save(getInstance(), ApiListEnums.LIVE_STREAMS.getType(), sharedConfigData.getData().getLivestreamUrls() != null ? new Gson().toJson(sharedConfigData.getData().getLivestreamUrls()) : "");
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && errorDialog != null && !errorDialog.isShowing() && !isFinishing())
                            errorDialog.setThrowable(e).setSubTitle("Hata oluştu.").setErrorFromClass("CoreSplashActivity * getSharedConfigUrl").init();
                    }

                    @Override
                    public void onComplete() {
                        getAndroidConfigUrl();
                        getAdsZones();
                    }
                });
    }

    private void getAndroidConfigUrl() {
        String apiPath = Preferences.getString(this, ApiListEnums.ANDROID_CONFIG_URL.getType(), "");
        mRestInterface.getAndroidConfig(apiPath).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseConfigData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseConfigData baseConfigData) {
                        try {
                            if (baseConfigData != null && baseConfigData.getData() != null && baseConfigData.getMeta().getStatusCode() == 200) {
                                /** CHECK VERSION */

                                /** SET RATE APP */
                                setRateApp(baseConfigData.getData().getRateThisApp());

                                /** SET OPTION MENU */
                                setSettingsMenu(baseConfigData.getData().getMenu().getSettingsMenu());

                                /** SET LEFT MENU */
                                setLeftMenu(baseConfigData.getData().getMenu().getLeft());

                                /** SET CATEGORY MENU */
                                setCategoryList(baseConfigData.getData().getMenu().getLeft());

                                /** SET TOOLBAR MENU */
                                setToolbarMenu(baseConfigData.getData().getMenu().getToolBarMenu());

                                /** SET COMPONENT SCREENS */
                                setComponentScreens(baseConfigData.getData());

                                /** SET ALL API PATH */
                                setAllApiPath(baseConfigData);

                                /** SET SETTINGS */
                                setSettings(baseConfigData.getData().getSettings());

                                /** SET COLORS */
                                setAppColorSettings(baseConfigData.getData().getSettings().getColors());

                                goToHomeActivity();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && errorDialog != null && !errorDialog.isShowing() && !isFinishing())
                            errorDialog.setThrowable(e).setSubTitle("Hata oluştu.").setErrorFromClass("CoreSplashActivity * getAndroidConfigUrl").init();
                    }

                    @Override
                    public void onComplete() {
                        /** SEND ANALYTICS */ // settings içindeki parametreye ihtiyaç olduğu için config alındıktan sonra gönderildi
                        new TurkuvazAnalytic(CoreSplashActivity.this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.SPLASH_PAGE.getEventName()).sendEvent();
                    }
                });
    }

    private void getAdsZones() {
        mRestInterface.getAdsZones("test/sabah-android-banners.json").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<AdsDatum>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<AdsDatum> adsDatum) {
                        setAdsZones(adsDatum);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && errorDialog != null && !errorDialog.isShowing() && !isFinishing())
                            errorDialog.setThrowable(e).setSubTitle("Hata oluştu.").setErrorFromClass("CoreSplashActivity * getAdsZones").init();
                    }

                    @Override
                    public void onComplete() {
                        GlobalMethods.setInitialInterstitialAd(getApplicationContext());
                    }
                });
    }

    private void goToHomeActivity() {
        try {
            Intent startIntent = new Intent(getInstance(), CoreHomeActivity.class);
            startActivity(startIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCategoryList(ArrayList<LeftMenu> leftMenu) {
        try {
            /** Video ve Photo Gallery için categorileri alıyoruz - Database'e kaydedip diğer ekranlarda kullanacağız */
            for (int x = 0; x < leftMenu.size(); x++) {
                if (leftMenu.get(x).getSubLinks() == null)
                    continue;
                for (int i = 0; i < leftMenu.get(x).getSubLinks().size(); i++) {
                    if (leftMenu.get(x).getSubLinks().get(i).getType().equals(MenuActionType.CATEGORY_VIDEO.getType())) {
                        mVideoCategoryList.add(leftMenu.get(x).getSubLinks().get(i));
                        if (i == 0)
                            Preferences.save(getInstance(), SettingsTypes.DEFAULT_VIDEO_CATEGORY.getType(), leftMenu.get(x).getSubLinks().get(i).getTitle());
                    }

                    if (leftMenu.get(x).getSubLinks().get(i).getType().equals(MenuActionType.CATEGORY_PHOTO_GALLERY.getType())) {
                        mGalleryCategoryList.add(leftMenu.get(x).getSubLinks().get(i));
                        if (i == 0)
                            Preferences.save(getInstance(), SettingsTypes.DEFAULT_GALLERY_CATEGORY.getType(), leftMenu.get(x).getSubLinks().get(i).getTitle());
                    }

                    if (leftMenu.get(x).getSubLinks().get(i).getType().equals(MenuActionType.CATEGORY_PROGRAMS.getType())) {
                        mProgramsCategoryList.add(leftMenu.get(x).getSubLinks().get(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(getInstance());
        databaseHelper.clearTable();
        databaseHelper.setPhotoGalleryCategory(gson.toJson(mGalleryCategoryList));
        databaseHelper.setVideoGalleryCategory(gson.toJson(mVideoCategoryList));
        databaseHelper.setProgramsCategory(gson.toJson(mProgramsCategoryList));
    }

    private void setAdsZones(ArrayList<AdsDatum> adsZones) {
        Preferences.save(getInstance(), ApiListEnums.ADS_ALL_ZONE.getType(), gson.toJson(adsZones));
        try {
            for (int i = 0; i < adsZones.size(); i++) {
                if (adsZones.get(i).getSection().equals("AnaSayfa")) {
                    for (int j = 0; j < adsZones.get(i).getZones().size(); j++) {
                        switch (adsZones.get(i).getZones().get(j).getZone()) {
                            case "app_android_320x142":
                                Preferences.save(getInstance(), ApiListEnums.ADS_HOMEPAGE_320x142.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_300x250":
                                Preferences.save(getInstance(), ApiListEnums.ADS_HOMEPAGE_300x250.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_320x50":
                                Preferences.save(getInstance(), ApiListEnums.ADS_HOMEPAGE_320x50.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_Preroll_Bolum":
                                Preferences.save(getInstance(), ApiListEnums.ADS_PREROLL.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_pageoverlay":
                                Preferences.save(getInstance(), ApiListEnums.ADS_PAGE_OVERLAY.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                        }
                    }
                } else if (adsZones.get(i).getSection().equals("DigerSayfalar")) {
                    for (int j = 0; j < adsZones.get(i).getZones().size(); j++) {
                        switch (adsZones.get(i).getZones().get(j).getZone()) {
                            case "app_android_320x142":
                                Preferences.save(getInstance(), ApiListEnums.ADS_GENERAL_320x142.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_300x250":
                                Preferences.save(getInstance(), ApiListEnums.ADS_GENERAL_300x250.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_320x50":
                                Preferences.save(getInstance(), ApiListEnums.ADS_GENERAL_320x50.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                            case "app_android_pageoverlay":
                                Log.i(TAG, "setAdsZones: ppp");
                                Preferences.save(getInstance(), ApiListEnums.ADS_PAGE_OVERLAY_OTHERS.getType(), adsZones.get(i).getZones().get(j).getValue());
                                break;
                        }
                    }
                }
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private PushModel isComeFromNotification() {
        return null;
    }

    private void setSettingsMenu(ArrayList<SettingsMenu> settingsMenus) {
        String jsonData = gson.toJson(settingsMenus);
        Preferences.save(getInstance(), ApiListEnums.SETTINGS_MENU_JSON.getType(), jsonData);
    }

    private void setLeftMenu(ArrayList<LeftMenu> leftMenu) {
    }

    private void setToolbarMenu(ArrayList<ToolbarMenu> toolbarMenu) {
    }

    private void setGDPRandKVKK(SharedConfigData sharedConfigData) {
    }

    private void setCSS(SharedConfigData sharedConfigData) {
        Preferences.save(getInstance(), ApiListEnums.CONTENT_CSS.getType(), sharedConfigData.getData().getContentCss().getUrl());
        Preferences.save(getInstance(), ApiListEnums.CONTENT_CSS_VERSION.getType(), String.valueOf(sharedConfigData.getData().getContentCss().getVersiyon()));
    }

    @SuppressLint("ResourceType")
    private void setAppColorSettings(Colors colorsJson) {
        Preferences.save(CoreSplashActivity.this, ApiListEnums.TOOLBAR_BACK.getType(), colorsJson == null ? getResources().getString(R.color.toolbar_back) : colorsJson.getToolbarColor().getBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.TOOLBAR_ICON.getType(), colorsJson == null ? getResources().getString(R.color.toolbar_icon) : colorsJson.getToolbarColor().getText());

        // SOL MENU - menü arkaplan ve title ve icon renkleri. Genelde title ve icon renkleri aynı olacak.
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_BACK.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_back) : colorsJson.getLeftMenuColor().getListBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_TOP_BACK.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_top_back) : colorsJson.getLeftMenuColor().getTopBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_TOP_ICON.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_top_icon) : colorsJson.getLeftMenuColor().getTopText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_LIST_BACK.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_list_back) : colorsJson.getLeftMenuColor().getGroupBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_LIST_ICON.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_list_icon) : colorsJson.getLeftMenuColor().getGroupText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_LIST_DETAIL_BACK.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_list_detail_back) : colorsJson.getLeftMenuColor().getChildBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_LIST_DETAIL_ICON.getType(), colorsJson == null ? getResources().getString(R.color.left_menu_list_detail_icon) : colorsJson.getLeftMenuColor().getChildText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_DIVIDER.getType(), colorsJson == null ? getResources().getString(R.color.menuDividerColor) : colorsJson.getLeftMenuColor().getDivider());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LEFT_MENU_CHILD_DIVIDER.getType(), colorsJson == null ? getResources().getString(R.color.subMenuDividerColor) : colorsJson.getLeftMenuColor().getChildDivider());

        // SON DAKIKA BARI - SOL VE SAĞ LAYOUT'LARIN ARKAPLAN VE TEXT RENKLERİ
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LAST_MINUTE_BACK_LEFT.getType(), colorsJson == null ? getResources().getString(R.color.last_minute_back_left) : colorsJson.getLastMinuteColor().getLeftBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LAST_MINUTE_TEXT_LEFT.getType(), colorsJson == null ? getResources().getString(R.color.last_minute_text_left) : colorsJson.getLastMinuteColor().getLeftText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LAST_MINUTE_BACK_RIGHT.getType(), colorsJson == null ? getResources().getString(R.color.last_minute_back_right) : colorsJson.getLastMinuteColor().getRightBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.LAST_MINUTE_TEXT_RIGHT.getType(), colorsJson == null ? getResources().getString(R.color.last_minute_text_right) : colorsJson.getLastMinuteColor().getRightText());

        // STATUS BAR - CITY
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_CITY_BACK.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_city_back) : colorsJson.getStatusBarCityColor().getBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_CITY_TEXT.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_city_text) : colorsJson.getStatusBarCityColor().getText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_ICON.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_icon) : colorsJson.getStatusBarCityColor().getIcon()); // konum değiştir ve lokasyon butonu

        // STATUS BAR - WIDGET
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_WIDGET_BACK_CLOSE.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_back_close) : colorsJson.getStatusBarWidgetColor().getCloseBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_WIDGET_BACK_OPEN.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_back_open) : colorsJson.getStatusBarWidgetColor().getOpenBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_widget_title) : colorsJson.getStatusBarWidgetColor().getWidgetTitle());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), colorsJson == null ? getResources().getString(R.color.status_bar_widget_value) : colorsJson.getStatusBarWidgetColor().getWidgetValue());

        // BOTTOM BAR
        Preferences.save(CoreSplashActivity.this, ApiListEnums.BOTTOM_BAR_BACK.getType(), colorsJson == null ? getResources().getString(R.color.bottom_bar_back) : colorsJson.getBottomBarColor().getBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.BOTTOM_BAR_SELECTED.getType(), colorsJson == null ? getResources().getString(R.color.bottom_bar_selected) : colorsJson.getBottomBarColor().getSelected());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.BOTTOM_BAR_UNSELECTED.getType(), colorsJson == null ? getResources().getString(R.color.bottom_bar_unselected) : colorsJson.getBottomBarColor().getUnSelected());

        // SOCIAL BAR
        Preferences.save(CoreSplashActivity.this, ApiListEnums.SOCIAL_BAR_BACK.getType(), colorsJson == null ? getResources().getString(R.color.social_bar_back) : colorsJson.getSocialbarColor().getBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.SOCIAL_BAR_ICON.getType(), colorsJson == null ? getResources().getString(R.color.social_bar_icon) : colorsJson.getSocialbarColor().getText());

        // COLUMNIST
        Preferences.save(CoreSplashActivity.this, ApiListEnums.COLUMNIST_BUTTON_BACK.getType(), colorsJson == null ? getResources().getString(R.color.columnist_button_back) : colorsJson.getColumnistColor().getButtonBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.COLUMNIST_BUTTON_TEXT.getType(), colorsJson == null ? getResources().getString(R.color.columnist_button_text) : colorsJson.getColumnistColor().getButtonText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.COLUMNIST_BUTTON_EDGE.getType(), colorsJson == null ? getResources().getString(R.color.columnist_button_edge) : colorsJson.getColumnistColor().getButtonEdge());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.COLUMNIST_PROFIL_FRAME.getType(), colorsJson == null ? getResources().getString(R.color.columnist_profil_frame) : colorsJson.getColumnistColor().getProfilFrame());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.COLUMNIST_BOTTOM_LINE.getType(), colorsJson == null ? getResources().getString(R.color.columnist_bottom_line) : colorsJson.getColumnistColor().getBottomLine());

        // TV STREAM - anasayfadaki yayın akışı widgeti
        Preferences.save(CoreSplashActivity.this, ApiListEnums.TV_STREAM_BACK.getType(), colorsJson == null ? getResources().getString(R.color.tv_stream_back) : colorsJson.getTvStreamColor().getBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.TV_STREAM_TEXT.getType(), colorsJson == null ? getResources().getString(R.color.tv_stream_text) : colorsJson.getTvStreamColor().getText());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.TV_STREAM_BUTTON_BACK.getType(), colorsJson == null ? getResources().getString(R.color.tv_stream_button_back) : colorsJson.getTvStreamColor().getButtonBackground());
        Preferences.save(CoreSplashActivity.this, ApiListEnums.TV_STREAM_BUTTON_TEXT.getType(), colorsJson == null ? getResources().getString(R.color.tv_stream_button_text) : colorsJson.getTvStreamColor().getButtonText());
    }

    private void setRateApp(RateThisApp rateApp) {
    }

    private void setComponentScreens(Data componentScreens) {
        Preferences.save(getInstance(), ApiListEnums.HOME_SCREEN_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getHomeScreen()));
        Preferences.save(getInstance(), ApiListEnums.CATEGORY_GALLERY_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getCategoryGallery()));
        Preferences.save(getInstance(), ApiListEnums.CATEGORY_VIDEO_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getCategoryVideo()));
        Preferences.save(getInstance(), ApiListEnums.CATEGORY_NEWS_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getCategoryNews()));
        Preferences.save(getInstance(), ApiListEnums.CATEGORY_NEWS_SPORT_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getCategoryNewsSport()));
        Preferences.save(getInstance(), ApiListEnums.CATEGORY_TODAY_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getCategoryToday()));
        Preferences.save(getInstance(), ApiListEnums.WIDGET_TOOLS_COMPONENT_JSON.getType(), gson.toJson(componentScreens.getWidgetTools()));
        Preferences.save(getInstance(), ApiListEnums.STATIC_KUNYE_JSON.getType(), gson.toJson(componentScreens.getStaticKunye()));
        Preferences.save(getInstance(), ApiListEnums.STATIC_VERI_POLITIKASI_JSON.getType(), gson.toJson(componentScreens.getStaticVeriPolitikasi()));
        Preferences.save(getInstance(), ApiListEnums.STATIC_GIZLILIK_BILDIRIMI_JSON.getType(), gson.toJson(componentScreens.getStaticGizlilikBildirimi()));
    }

    private void setSettings(Settings settingsJson) {
        if (settingsJson != null) { // settings de yeni yapıya geçildiği için canlıya çıkmamış uygulamalarda hata vermemesi için kontrol koyuldu. tüm uygulamalar canlıya çıkınca burası kaldırılabilir
            Preferences.save(getInstance(), ApiListEnums.CATEGORY_SETTINGS.getType(), gson.toJson(settingsJson));
            Preferences.save(getInstance(), ApiListEnums.SETTINGS_STICKY_IS_ACTIVE.getType(), (settingsJson.getAdvertisement() != null && settingsJson.getAdvertisement().getStickyAd() != null) ? settingsJson.getAdvertisement().getStickyAd().isActive() : false);
            Preferences.save(getInstance(), ApiListEnums.SETTINGS_NATIVE_PLAYER_IS_ACTIVE.getType(), (settingsJson.getVideo() != null && settingsJson.getVideo().getNativePlayer() != null) ? settingsJson.getVideo().getNativePlayer().isActive() : false);
            Preferences.save(getInstance(), ApiListEnums.SETTINGS_PRE_ROLL_ADS_IS_ACTIVE.getType(), (settingsJson.getVideo() != null && settingsJson.getVideo().getPreRollAds() != null) ? settingsJson.getVideo().getPreRollAds().isActive() : true);

            /** SET STATS */
            if (settingsJson.getStats() != null) {
                Preferences.save(getInstance(), ApiListEnums.SETTINGS_GEMIUS_IS_ACTIVE.getType(), settingsJson.getStats().getGemius().isActive());
                Preferences.save(getInstance(), ApiListEnums.SETTINGS_TIAK_IS_ACTIVE.getType(), settingsJson.getStats().getTiak().isActive());
                Preferences.save(getInstance(), ApiListEnums.SETTINGS_FIREBASE_IS_ACTIVE.getType(), settingsJson.getStats().getFirebase().isActive());
                Preferences.save(getInstance(), ApiListEnums.SETTINGS_GEMIUS_KEY.getType(), settingsJson.getStats().getGemius().getKey() != null ? settingsJson.getStats().getGemius().getKey() : "");
            }

            /** SET SOCIAL MEDIA */
            if (settingsJson.getSocialMedia() != null) {
                Preferences.save(getInstance(), ApiListEnums.INSTAGRAM_URL.getType(), settingsJson.getSocialMedia().getInstagram());
                Preferences.save(getInstance(), ApiListEnums.FACEBOOK_URL.getType(), settingsJson.getSocialMedia().getFacebook());
                Preferences.save(getInstance(), ApiListEnums.TWITTER_URL.getType(), settingsJson.getSocialMedia().getTwitter());
                Preferences.save(getInstance(), ApiListEnums.YOUTUBE_URL.getType(), settingsJson.getSocialMedia().getYoutube());
                Preferences.save(getInstance(), ApiListEnums.YOUTUBE_USER_URL.getType(), settingsJson.getSocialMedia().getYoutubeUser() != null ? settingsJson.getSocialMedia().getYoutubeUser() : "");
            }
        }
    }

    private void setAllApiPath(BaseConfigData baseConfigData) {
        Preferences.save(getInstance(), ApiListEnums.ARTICLES_HEADLINE.getType(), baseConfigData.getData().getApiContentUrls().getArticlesHeadlines());
        Preferences.save(getInstance(), ApiListEnums.ARTICLES_SNAP_NEWS.getType(), baseConfigData.getData().getApiContentUrls().getArticlesSnapbar());
        Preferences.save(getInstance(), ApiListEnums.COLUMNIST.getType(), baseConfigData.getData().getApiContentUrls().getColumnists());
        Preferences.save(getInstance(), ApiListEnums.VIDEO_SLIDER.getType(), baseConfigData.getData().getApiContentUrls().getVideoSlider());
        Preferences.save(getInstance(), ApiListEnums.ARTICLES_HEADLINE_PART.getType(), baseConfigData.getData().getApiContentUrls().getArticlesGridNews());
        Preferences.save(getInstance(), ApiListEnums.GALLERY_SLIDER.getType(), baseConfigData.getData().getApiContentUrls().getGallerySlider());
        Preferences.save(getInstance(), ApiListEnums.ARTICLES_INFINITY_NEWS.getType(), baseConfigData.getData().getApiContentUrls().getArticlesInfiniytList());
        Preferences.save(getInstance(), ApiListEnums.DETAILS_GALLERY.getType(), baseConfigData.getData().getApiContentUrls().getDetailsGallery());
        Preferences.save(getInstance(), ApiListEnums.DETAILS_VIDEO.getType(), baseConfigData.getData().getApiContentUrls().getDetailsVideo());
        Preferences.save(getInstance(), ApiListEnums.DETAILS_ARTICLE.getType(), baseConfigData.getData().getApiContentUrls().getDetailsArticle());
        Preferences.save(getInstance(), ApiListEnums.DETAILS_COLUMNIST.getType(), baseConfigData.getData().getApiContentUrls().getDetailsColumnist());
        Preferences.save(getInstance(), ApiListEnums.ALL_CATEGORIES.getType(), baseConfigData.getData().getApiContentUrls().getListAllCategories());
        Preferences.save(getInstance(), ApiListEnums.ALL_AUTHORS.getType(), baseConfigData.getData().getApiContentUrls().getListAllAuthors());
        Preferences.save(getInstance(), ApiListEnums.ARTICLE_BY_CATEGORY.getType(), baseConfigData.getData().getApiContentUrls().getListArticlesByCategory());
        Preferences.save(getInstance(), ApiListEnums.VIDEO_BY_CATEGORY.getType(), baseConfigData.getData().getApiContentUrls().getListVideosByCategory());
        Preferences.save(getInstance(), ApiListEnums.LIST_PROGRAMS.getType(), baseConfigData.getData().getApiContentUrls().getListPrograms());
        Preferences.save(getInstance(), ApiListEnums.LIST_PROGRAMS_BY_CATEGORY.getType(), baseConfigData.getData().getApiContentUrls().getListProgramsByCategory());
        Preferences.save(getInstance(), ApiListEnums.GALLERIES_BY_CATEGORY.getType(), baseConfigData.getData().getApiContentUrls().getListGalleriesByCategory());
        Preferences.save(getInstance(), ApiListEnums.LIST_NOTIFICATION.getType(), baseConfigData.getData().getApiContentUrls().getListNotifications());
        Preferences.save(getInstance(), ApiListEnums.COLUMNIST_BY_SOURCE.getType(), baseConfigData.getData().getApiContentUrls().getListColumnistsBySource());
        Preferences.save(getInstance(), ApiListEnums.ARTICLE_KUNYE.getType(), baseConfigData.getData().getApiContentUrls().getStaticArticleKunye());
        Preferences.save(getInstance(), ApiListEnums.ARTICLE_PRIVACY_POLICY.getType(), baseConfigData.getData().getApiContentUrls().getStaticArticleGizlilik());
        Preferences.save(getInstance(), ApiListEnums.LIST_STREAMING.getType(), baseConfigData.getData().getApiContentUrls().getListStreaming());

        // A haber diziler - Farklı tasarım
        Preferences.save(getInstance(), ApiListEnums.CUSTOM_TV_SERIES_BOLUM.getType(), baseConfigData.getData().getApiContentUrls().getListCustomTvSeriesBolum());
        Preferences.save(getInstance(), ApiListEnums.CUSTOM_TV_SERIES_FRAGMAN.getType(), baseConfigData.getData().getApiContentUrls().getListCustomTvSeriesFragman());
        Preferences.save(getInstance(), ApiListEnums.CUSTOM_TV_SERIES_OZEL.getType(), baseConfigData.getData().getApiContentUrls().getListCustomTvSeriesOzel());

        /**Takvim icin gerekli kisimlar*/
        Preferences.save(getInstance(), ApiListEnums.ARTICLES_BY_DAILY_TAKVIM.getType(), baseConfigData.getData().getApiContentUrls().getListArticlesDailyTakvim());
        Preferences.save(getInstance(), ApiListEnums.ARTICLES_LAST_MINUTE.getType(), baseConfigData.getData().getApiContentUrls().getArticlesLastMinute());
        Preferences.save(getInstance(), ApiListEnums.DAILY_NEWS_PAPER.getType(), baseConfigData.getData().getApiContentUrls().getDailyNewsPaper());
        Preferences.save(getInstance(), ApiListEnums.ACTUAL_TV_SERIES.getType(), baseConfigData.getData().getApiContentUrls().getListTvSeries());
        Preferences.save(getInstance(), ApiListEnums.CLASSIC_TV_SERIES.getType(), baseConfigData.getData().getApiContentUrls().getListClassicTvSeries());
        Preferences.save(getInstance(), ApiListEnums.ACTUAL_TV_SERIES_FRAGMAN.getType(), baseConfigData.getData().getApiContentUrls().getListTvSeriesFragman());
        Preferences.save(getInstance(), ApiListEnums.CLASSIC_TV_SERIES_FRAGMAN.getType(), baseConfigData.getData().getApiContentUrls().getListClassicTvSeriesFragman());

        GlobalMethods.setAllCategory(this);
    }

    private void getHMSToken() {

    }
}