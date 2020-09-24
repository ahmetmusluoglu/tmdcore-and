package turkuvaz.sdk.global.GlobalMethods;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.sdk.global.Database.CategoryDatabaseHelper;
import turkuvaz.sdk.global.ErrorDialog.ErrorSheetDialog;
import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.IntersitialTimeCache;
import turkuvaz.sdk.models.Models.AdsZones.AdsDatum;
import turkuvaz.sdk.models.Models.Enums.AdsType;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.ApplicationsAppRef;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.models.Models.Series.SeriesModel;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class GlobalMethods {
    public static AllFlavors getCurrentFlavor(Context context) {
        AllFlavors currentFlavor = null;
        switch (context.getApplicationInfo().packageName) {
            case "tr.sabah":
                currentFlavor = AllFlavors.SABAH;
                break;
        }
        return currentFlavor;
    }

    /*public static HashMap<String, String> createTiakLabel(Article article, Context context) {
        if (article == null) return null;

        HashMap<String, String> label = new HashMap<>();
        label.put("publisherId", "6331775");
        label.put("ns_st_stc", TiakChannels.getChannelId(context.getApplicationInfo().packageName));    //Channel ID
        label.put("ns_st_ci", "");                                                                      //Video ID
        label.put("ns_st_cl", article.getVideoDuration());                                              //Video Duration
        label.put("ns_st_pu", "TURKUVAZ MEDYA");                                                        //Yayıncı Adı
        label.put("ns_st_pr", article.getCategoryName());                                               //Category Name
        label.put("ns_st_tpr", article.getCategoryId());                                                //Category Id
        label.put("ns_st_ep", article.getTitle());                                                      //Video Title
        label.put("ns_st_sn", String.valueOf(article.getSeason()));                                     //Video Session No
        label.put("ns_st_en", String.valueOf(article.getEpisode()));                                    //Episode No
        label.put("ns_st_ge", String.valueOf(article.getVideoTypeId()));                                //Video Type
        label.put("ns_st_ct", article.getVideoTypeId() == 10 ? "vc12" : "vc11");                        // VideoTypeID == 10 ? vc12 : vc11
        label.put("ns_st_ia", "");                                                                      //0 ==> reklamla ilgili olduğu için şimdilik ertelenebilir
        label.put("ns_st_ddt", article.getCreatedDate());                                               //video.CreatedDate ==> internet yayin tarihi
        label.put("ns_st_tdt", "");                                                                     //==> tv yayin tarihi (internet yayın tarihi şimdilik verilebilir)
        label.put("ns_st_st", "");                                                                      //Channel Name
        label.put("ns_site", "total");
        label.put("cs_proid", "kwp-tr");
        label.put("c3", null);
        label.put("c4", null);
        label.put("c6", null);
        label.put("ns_st_pn", "1");
        label.put("ns_st_tp", "2");
        label.put("ns_st_ce", "1");
        label.put("ns_st_li", "false");                                                                 //isLiveStream
        return label;
    }*/

    /*
     *    c2	    comScore client ID
     *    ns_st_stc	Channel ID
     *    ns_st_tpr Program ID for TIAK matching
     *    ns_st_ci 	Content asset ID
     *    ns_st_tdt	TV Air date
     *    ns_st_tm	Time of initial broadcast
     *    ns_st_cl	Clip Length
     *    ns_st_ct	Classification type
     *    ns_st_pr	Program name
     *    ns_st_ep	Episode title
     *    ns_st_tep	Episode ID
     */

    /*public static HashMap<String, String> getTiakData(Context context, Article article) {
        HashMap<String, String> labels = new LinkedHashMap<>();
        try {
            String ns_st_tdt = DateUtils.format2(article.getCreatedDateReal());
            String ns_st_tm = DateUtils.format3(article.getCreatedDateReal());
            String ns_st_ct = article.getVideoTypeId() == Constants.TEN ? Constants.VC12 : Constants.VC11;
            labels.put("c2", Constants.CLIENT_ID);
            labels.put("ns_st_stc", TiakChannels.getChannelId(context.getApplicationInfo().packageName));
            labels.put("ns_st_tpr", article.getCategoryId());
            labels.put("ns_st_ci", article.getId());
            labels.put("ns_st_tdt", ns_st_tdt);
            labels.put("ns_st_tm", ns_st_tm);
            labels.put("ns_st_cl", getClipLength(article.getVideoDuration()));
            labels.put("ns_st_ct", ns_st_ct);
            labels.put("ns_st_pr", article.getCategoryName());
//        labels.put("ns_st_ep", article.getTitleShort());

            labels.put("ns_st_ep", URLEncoder.encode(article.getTitleShort(), "UTF-8"));

            labels.put("ns_st_tep", article.getEpisode() + "");
//        Log.i("TAG", "getTiakData-article: " + article.toString());
            Log.i("TAG", "getTiakData-labels: " + labels.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return labels;
    }

    private static String getClipLength(String videoDuration) {
        return videoDuration != null && !videoDuration.isEmpty() ? "" + (Integer.parseInt(videoDuration) * 1000) : "0";
    }*/


    public static void getScreenWidth(Activity activity) {
        // TAKVIM-5619. note 8 cihazında manşet ve diğer resimler uzun geliyordu. ekran genişliği pixel cinsinden alındı ve her girişte alınması sağlandı eskiyi görmemesi için
        Preferences.save(activity, SettingsTypes.SCREEN_WIDTH.getType(), getScreenWidth());
        Preferences.save(activity, SettingsTypes.SCREEN_HEIGHT.getType(), getScreenHeight());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void setRegisterPush(Context context) {

        ApiClient.getClientAPNS().create(RestInterface.class).setRegisterPush(
                ApplicationsAppRef.getAppRef(context.getApplicationInfo().packageName),
                context.getApplicationInfo().packageName,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID),
                ApiListEnums.DEVICE_PUSH_TOKEN.getApi(context),
                false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.e("PUSH REGISTER", s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("PUSH REGISTER", "ERROR");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void setOpenedPush(Context context, int pushID) {
        ApiClient.getClientAPNS().create(RestInterface.class).setOpenedPush(
                ApplicationsAppRef.getAppRef(context.getApplicationInfo().packageName),
                context.getApplicationInfo().packageName,
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID),
                ApiListEnums.DEVICE_PUSH_TOKEN.getApi(context),
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
                Log.e("PUSH OPENED", "ERROR");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public static void getPushToken(Activity activity) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(activity, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Preferences.save(activity, ApiListEnums.DEVICE_PUSH_TOKEN.getType(), newToken);
            Log.e("PUSH TOKEN --> ", newToken);
            GlobalMethods.setRegisterPush(activity);
        });
    }

    public static Context setLocale(Context context, String localeStr) {

            localeStr = "en";
        Locale locale = new Locale(localeStr);
        Locale.setDefault(locale);

        Resources res = context.getResources();

        Configuration config = context.getResources().getConfiguration();
//        config.setLocale(locale);
        config.locale = locale;
        context.createConfigurationContext(config);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        res.updateConfiguration(config, res.getDisplayMetrics());
        return context;
    }

    public static void setAllCategory(Context context) {
        String apiPath = Preferences.getString(context, ApiListEnums.ALL_CATEGORIES.getType(), "");
        ApiClient.getClientApi(context).create(RestInterface.class).getAllCategory(apiPath).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SeriesModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SeriesModel seriesModel) {
                        try {
                            CategoryDatabaseHelper categoryDatabaseHelper = new CategoryDatabaseHelper(context);
                            categoryDatabaseHelper.clearTable();
                            for (int i = 0; i < seriesModel.getData().getCategories().getResponse().size(); i++) {
                                categoryDatabaseHelper.insertCategory(
                                        seriesModel.getData().getCategories().getResponse().get(i).getId(),
                                        seriesModel.getData().getCategories().getResponse().get(i).getNameForUrl());
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static String getCategoryZone(Context context, String categoryID, AdsType adsType) {
        String categoryZone = "";

        if (categoryID != null && !TextUtils.isEmpty(categoryID)) {
            String nameForUrl = new CategoryDatabaseHelper(context).getCategoryNameForUrl(categoryID);
            String allZones = Preferences.getString(context, ApiListEnums.ADS_ALL_ZONE.getType(), "");

            ArrayList<AdsDatum> adsZones = new Gson().fromJson(allZones, new TypeToken<ArrayList<AdsDatum>>() {
            }.getType());

            if (adsZones != null && nameForUrl != null) {
                for (int i = 0; i < adsZones.size(); i++) {
                    if (adsZones.get(i).getSection().equals(nameForUrl)) {
                        for (int j = 0; j < adsZones.get(i).getZones().size(); j++) {
                            if ((adsZones.get(i).getZones().get(j).getZone()).equals(adsType.getType())) {
                                if (adsZones.get(i).getZones().get(j) == null)
                                    categoryZone = null;
                                else
                                    categoryZone = adsZones.get(i).getZones().get(j).getValue();
                                break;
                            }
                        }
                    }
                }
            }
        }


        /** CATEGORY ICIN ZONE TANIMLANMIŞ AMA VALUE NULL GELMİŞSE REKLAM BASILMIYOR */
        if (categoryZone == null) {
            categoryZone = "None";
            return categoryZone;
        }

        /** CATEGORY ICIN ZONE TANIMLANMAMIŞSA DEFAULT OLARAK DIGER SAYFALARIN ZONE'UNU KULLANIYORUZ */
        if (categoryZone.equals("")) {
            switch (adsType) {
                case ADS_320x142: {
                    categoryZone = Preferences.getString(context, ApiListEnums.ADS_GENERAL_320x142.getType(), "");
                    break;
                }
                case ADS_300x250: {
                    categoryZone = Preferences.getString(context, ApiListEnums.ADS_GENERAL_300x250.getType(), "");
                    break;
                }
                case ADS_320x50: {
                    categoryZone = Preferences.getString(context, ApiListEnums.ADS_GENERAL_320x50.getType(), "");
                    break;
                }
                case ADS_PAGE_OVERLAY: {
                    categoryZone = Preferences.getString(context, ApiListEnums.ADS_PAGE_OVERLAY.getType(), "");
                    break;
                }
                case ADS_PREROLL: {
                    categoryZone = Preferences.getString(context, ApiListEnums.ADS_PREROLL.getType(), "");
                    break;
                }
            }
        }

        return categoryZone;
    }

    public static float distance(Context context, float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
        return pxToDp(context, distanceInPx);
    }

    public static float pxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    private static String token = "none";

    public static String getToken() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> token = instanceIdResult.getToken());
            Log.d("<*>DK", "token:" + token);
            return token;
        } catch (Exception var1) {
            return "none";
        }
    }


    private static PushModel pushModel = null;

    public static void setPushModel(PushModel _pushModel) {
        pushModel = _pushModel;
    }

    public static void onReportErrorToFirebase(Activity activity, final Throwable error, final String className) {
        if (!activity.isFinishing())
            activity.runOnUiThread(() -> {
                try {
                    ErrorSheetDialog errorSheetDialog = new ErrorSheetDialog(activity, error, className, pushModel);
                    if (!activity.isFinishing())
                        errorSheetDialog.show();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            });
    }

    private static InterstitialAd mInterstitialAd;

    static String TAG = "TAG_InitialInterstitialAd";

    public static void setInitialInterstitialAd(Context context) {
        try {
            mInterstitialAd = new InterstitialAd(context);
            String zoneId = Preferences.getString(context, ApiListEnums.ADS_PAGE_OVERLAY_OTHERS.getType(), "");
            Log.i(TAG, "setInitialInterstitialAd: " + zoneId);
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //Google test id
            mInterstitialAd.setAdUnitId(zoneId);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    Log.i(TAG, "onAdClosed: ");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                        Log.i(TAG, "onAdFailedToLoad: " + i);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    Log.i(TAG, "onAdLeftApplication: ");
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Log.i(TAG, "onAdImpression: ");
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    Log.i(TAG, "onAdClicked: ");
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    Log.i(TAG, "onAdOpened: ");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInterstitialAd(Context context, int minutes) {
        try {
            String connectedUrl = Preferences.getString(context, "connectedurl", "https://api.tmgrup.com.tr");
            boolean isLive = connectedUrl != null && connectedUrl.equals("https://api.tmgrup.com.tr");
            IntersitialTimeCache timeCache = new IntersitialTimeCache(context);
            boolean isNotExpired = timeCache.isTimeExpired(isLive ? minutes : 2);

            Log.i(TAG, "connectedUrl :" + connectedUrl);
            Log.i(TAG, "isLive " + isLive);
            Log.i(TAG, "isExpired " + isNotExpired);

            if (isNotExpired && mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                Log.i(TAG, "mInterstitialAd-showing success...");
                mInterstitialAd.show();
            } else {
                Log.d(TAG, "The interstitial wasn't loaded yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isOpenedPush;

    public static boolean isOpenedPush() {
        return isOpenedPush;
    }

    public static void setIsOpenedPush(boolean isOpenedPush) {
        GlobalMethods.isOpenedPush = isOpenedPush;
    }

    private static boolean isNeedTiak;

    public static boolean isNeedTiak() {
        return isNeedTiak;
    }

    public static void setNeedTiak(boolean needTiak) {
        isNeedTiak = needTiak;
    }

    private static boolean isActiveNativePlayer ;

    public static boolean isActiveNativePlayer() {
        return isActiveNativePlayer;
    }

    public static void setActivePlayer(boolean isActive) {
        isActiveNativePlayer = isActive;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void stopAudio(Context context) {
        Log.i(TAG, "stopAudio:");
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        AudioAttributes mAudioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        AudioFocusRequest mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(mAudioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(i -> Log.i(TAG, "stopAudio: setOnAudioFocusChangeListener " + i)) // Need to implement listener
                .build();
        int focusRequest = mAudioManager.requestAudioFocus(mAudioFocusRequest);

        switch (focusRequest) {
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                Log.i(TAG, "stopAudio: AudioManager.AUDIOFOCUS_REQUEST_FAILED");
                // don’t start playback
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                Log.i(TAG, "stopAudio: AudioManager.AUDIOFOCUS_REQUEST_GRANTED");
                // actually start playback
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.i(TAG, "stopAudio: AudioManager.AUDIOFOCUS_LOSS");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.i(TAG, "stopAudio:  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.i(TAG, "stopAudio: AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                // ... pausing or ducking depends on your app
                break;
        }
    }
}