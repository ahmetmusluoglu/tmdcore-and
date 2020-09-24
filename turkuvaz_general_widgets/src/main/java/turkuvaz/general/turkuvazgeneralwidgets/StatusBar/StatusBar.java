package turkuvaz.general.turkuvazgeneralwidgets.StatusBar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Adapters.StatusAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.CityPlakaModel;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.ExchangeEnums;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.IconSpinnerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.TypeEnums;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.models.Models.CityList.CityListModel;
import turkuvaz.sdk.models.Models.CityList.Response;
import turkuvaz.sdk.models.Models.ConfigBase.SimpleMenu;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.ExchangeList.ExchangeData;
import turkuvaz.sdk.models.Models.Preferences;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.ASR;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.DHUHR;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.FAJR;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.ISHA;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.MAGHRIB;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.SUNSHINE;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.TypeEnums.COLLAPSE;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.TypeEnums.EXCHANGE;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.TypeEnums.EXPAND;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.TypeEnums.PRAYER;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.TypeEnums.WEATHER;

/**
 * Created by turgay.ulutas on 15/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class StatusBar extends RelativeLayout implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private ValueAnimator valueAnimator;
    private RelativeLayout mRl_exchangeRoot, mRl_exchange, mRl_prayer, mRl_weather, relLayStatusBarMain;
    private LinearLayout linLayStatusBarMain, linLayPrayer;
    private ImageView img_pin;
    private TextView tv_exchangeTitle, tv_statusDollar, tv_statusPrayerTitle, tv_statusPrayer, tv_statusWeather, tv_statusWeatherTitle, tvKonumDegistir;
    private RestInterface mRestInterface;
    private List<Response> mResponseCity;
    private String mCityListApiPath, mStatusDataApiPath;
    private int spinnerCheck = 0;
    private CityPlakaModel[] plakaModels;
    private Spinner mSp_allCity;
    private ExchangeData mAllData;
    private TypeEnums LAST_STATUS_TYPE = COLLAPSE, LAST_BAR_TYPE;
    private RelativeLayout activeButton = null;
    private Timer mTimer;
    private View lineStatusBar;
    private String list;
    private int selectedIndex = -1;
    private boolean isFirstShowing;
    private RelativeLayout selectedTab;
    private List<SimpleMenu> subActions;

    public StatusBar(Context context, String cityListApiPath, String statusDataApiPath, String list, int selectedIndex, List<SimpleMenu> subActions) {
        super(context);
        this.list = list;
        this.selectedIndex = selectedIndex;
        this.mRestInterface = ApiClient.getClientApi(context).create(RestInterface.class);
        this.mCityListApiPath = cityListApiPath;
        this.mStatusDataApiPath = statusDataApiPath;
        isFirstShowing = true;
        this.subActions = subActions;
        init();
    }

    public StatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatusBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.status_bar_main, this, true);
        mRl_exchange = findViewById(R.id.rl_exchangeOpen);
        mRl_prayer = findViewById(R.id.rl_prayerOpen);
        mRl_weather = findViewById(R.id.rl_weatherOpen);
        mRl_exchangeRoot = findViewById(R.id.rl_exchangeRoot);
        mSp_allCity = findViewById(R.id.sp_statusBarCity);
        relLayStatusBarMain = findViewById(R.id.relLayStatusBarMain);
        linLayStatusBarMain = findViewById(R.id.linLayStatusBarMain);
        linLayPrayer = findViewById(R.id.linLayPrayer);
        tv_exchangeTitle = findViewById(R.id.tv_exchangeTitle);
        tv_statusDollar = findViewById(R.id.tv_statusDollar);
        tv_statusPrayerTitle = findViewById(R.id.tv_statusPrayerTitle);
        tv_statusPrayer = findViewById(R.id.tv_statusPrayer);
        tv_statusWeather = findViewById(R.id.tv_statusWeather);
        tv_statusWeatherTitle = findViewById(R.id.tv_statusWeatherTitle);
        tvKonumDegistir = findViewById(R.id.tvKonumDegistir);
        img_pin = findViewById(R.id.img_pin);
        lineStatusBar = findViewById(R.id.lineStatusBar);
        mRl_exchange.setOnClickListener(this);
        mRl_prayer.setOnClickListener(this);
        mRl_weather.setOnClickListener(this);

        lineStatusBar.setBackgroundColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_BACK_CLOSE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz2)))));

        // anasayfadaki namaz vakitleri widget'indeki konum butonunun ve sağdaki konum değiştir view'ının rengi. Alttai ana widget'in, henüz tıklanmamış yani kapalıyken olan arkaplan renkleri verildi.
        DrawableCompat.setTint(img_pin.getDrawable(), Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_ICON.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz2)))));
        tvKonumDegistir.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_ICON.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz2)))));

        // namaz widget ilk açıldığındaki title ve value değerlerinin renkleri. başlık ve altındaki değerler
        tv_exchangeTitle.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz2)))));
        tv_statusDollar.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz)))));
        tv_statusPrayerTitle.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz2)))));
        tv_statusPrayer.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz)))));
        tv_statusWeatherTitle.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz2)))));
        tv_statusWeather.setTextColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.beyaz)))));

        // namaz widgeti renklendirmeleri yapılıyor. üstteki şehir seçimi ve altındaki büyük widget için ayrı ayrı arkaplan renkleri verildi.
        relLayStatusBarMain.setBackgroundColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_CITY_BACK.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.siyah)))));
        linLayStatusBarMain.setBackgroundColor(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_BACK_CLOSE.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.siyah)))));

        // Widget'taki 3 item'a tıklandığındaki arkaplan renkleri belirlendi. Namaz, Borsa ve Hava durumuna tıklandığında altta başka bir bar açılıyor. Bunula ilgili renkler.
        mRl_exchange.setBackground(setImageButtonState());
        mRl_prayer.setBackground(setImageButtonState());
        mRl_weather.setBackground(setImageButtonState());
        if (list.length() > 0) {
            String[] items = list.split(",");//(EXCHANGE,PRAYER,WEATHER : BORSA,NAMAZVAKTI,HAVADURUMU)
            mRl_exchange.setVisibility(Arrays.asList(items).contains(EXCHANGE.name()) ? VISIBLE : GONE);  //Borsa tab..
            mRl_prayer.setVisibility(Arrays.asList(items).contains(PRAYER.name()) ? VISIBLE : GONE);      //Namaz Vakti tab..
            mRl_weather.setVisibility(Arrays.asList(items).contains(WEATHER.name()) ? VISIBLE : GONE);    //Hava Durumu tab..

            if (selectedIndex >= 0 && selectedIndex < items.length) {
                if (items[selectedIndex].equals(EXCHANGE.name())) { // Hangi tab aktif olacagi secilir...
                    selectedTab = mRl_exchange;
                } else if (items[selectedIndex].equals(PRAYER.name())) {
                    selectedTab = mRl_prayer;
                } else if (items[selectedIndex].equals(WEATHER.name())) {
                    selectedTab = mRl_weather;
                }
            }
        }
        getCityList(mCityListApiPath);
    }

    private StateListDrawable setImageButtonState() { // xml'den yapılan bu işlem, kod tarafına taşındı çünkü renklerin dinamik olması gerekiyordu.
        StateListDrawable states = new StateListDrawable(); // alttaki 2. bar açıkken renkler değişiyordu bu yüzden back_open değeri verildi.
        states.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(Color.parseColor(Preferences.getString(getContext(), ApiListEnums.STATUS_BAR_WIDGET_BACK_OPEN.getType(), "#" + Integer.toHexString(getContext().getResources().getColor(R.color.siyah))))));
        return states;
    }

    public void expandStatusBar(final TypeEnums barType) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mRl_exchangeRoot.removeAllViews();
        if (Preferences.getString(getContext(), ApiListEnums.THEME.getType(), "dark").equals("light")) // fikriyatta açık olan widget tabını kapatınca arkada siyah bir layout da açılıp kapanıyordu
            mRl_exchangeRoot.setBackground(getResources().getDrawable(R.drawable.bottom_white_line));
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(layoutParams);

        if (LAST_STATUS_TYPE == EXPAND) {
            if (barType == PRAYER && LAST_BAR_TYPE == PRAYER) {
                startAnimation(PRAYER, COLLAPSE);
                return;
            }

            if (barType == WEATHER && LAST_BAR_TYPE == WEATHER) {
                startAnimation(WEATHER, COLLAPSE);
                return;
            }

            if (barType == EXCHANGE && LAST_BAR_TYPE == EXCHANGE) {
                startAnimation(EXCHANGE, COLLAPSE);
                return;
            }
        }


        try {
            switch (barType) {
                case EXCHANGE: {
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new StatusAdapter(mAllData.getData().getPiyasaList().getResponse(), getContext(), subActions));
                    mRl_exchangeRoot.addView(recyclerView);
                    startAnimation(EXCHANGE, EXPAND);
                    break;
                }
                case PRAYER: {
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 6, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new StatusAdapter(mAllData.getData().getCityDetail().getResponse().getPrayTimes(), getContext(), subActions));
                    mRl_exchangeRoot.addView(recyclerView);
                    startAnimation(PRAYER, EXPAND);
                    break;
                }
                case WEATHER: {
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new StatusAdapter(mAllData.getData().getCityDetail().getResponse().getWeather(), getContext(), subActions));
                    mRl_exchangeRoot.addView(recyclerView);
                    startAnimation(WEATHER, EXPAND);
                    break;
                }
            }
        } catch (NullPointerException e) {

        }
    }

    private void getCityList(String apiUrl) {
        mRestInterface.getCityList(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CityListModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CityListModel cityListModel) {
                        try {
                            if (cityListModel != null && cityListModel.getData() != null) {
                                mResponseCity = cityListModel.getData().getCityList().getResponse();
                                plakaModels = new CityPlakaModel[mResponseCity.size()];
                                for (int x = 0; x < cityListModel.getData().getCityList().getResponse().size(); x++) {
                                    plakaModels[x] = new CityPlakaModel(mResponseCity.get(x).getName(), mResponseCity.get(x).getPlateNo());
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        try {
                            mSp_allCity.setAdapter(new IconSpinnerAdapter(getContext(), plakaModels));
                            mSp_allCity.setOnItemSelectedListener(StatusBar.this);
                            mSp_allCity.setSelection(Preferences.getInt(getContext(), "currentCityPosition", 40));
                            getStatusData(mStatusDataApiPath);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getStatusData(String apiUrl) {
        int currentCityCode = Preferences.getInt(getContext(), "currentCityCode", 34);

        mRestInterface.getExchangeList(apiUrl, currentCityCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ExchangeData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ExchangeData cityListModel) {
                        mAllData = cityListModel;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        try {
                            /**
                             * EXCHANGE BAR
                             */

                            if (TextUtils.isEmpty(((TextView) findViewById(R.id.tv_exchangeTitle)).getText())) {
                                turkuvaz.sdk.models.Models.ExchangeList.Response exchangeItem = getExchangeItem(ExchangeEnums.ALTIN.getType());
                                if (exchangeItem != null) {
                                    ((TextView) findViewById(R.id.tv_exchangeTitle)).setText(getContext().getString(R.string.gold));
                                    ((TextView) findViewById(R.id.tv_statusDollar)).setText(String.valueOf(exchangeItem.getSell()));
                                    ((ImageView) findViewById(R.id.img_exchangeImage)).setImageDrawable(Utils.isUpExchangeImage(getContext(), exchangeItem.getLastClosing(), exchangeItem.getSell()));
                                }
                            }

                            /**
                             * PRAYER BAR
                             */
                            PrayerEnums currentPrayer = Utils.getCurrentPrayer(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getImsak(),
                                    mAllData.getData().getCityDetail().getResponse().getPrayTimes().getGunes(),
                                    mAllData.getData().getCityDetail().getResponse().getPrayTimes().getOgle(),
                                    mAllData.getData().getCityDetail().getResponse().getPrayTimes().getIkindi(),
                                    mAllData.getData().getCityDetail().getResponse().getPrayTimes().getAksam(),
                                    mAllData.getData().getCityDetail().getResponse().getPrayTimes().getYatsi());

                            if (currentPrayer == FAJR) {
                                ((TextView) findViewById(R.id.tv_statusPrayerTitle)).setText(getContext().getString(R.string.imsak));
                                ((TextView) findViewById(R.id.tv_statusPrayer)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getImsak())));
                            } else if (currentPrayer == SUNSHINE) {
                                ((TextView) findViewById(R.id.tv_statusPrayerTitle)).setText(getContext().getString(R.string.gunes));
                                ((TextView) findViewById(R.id.tv_statusPrayer)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getGunes())));
                            } else if (currentPrayer == DHUHR) {
                                ((TextView) findViewById(R.id.tv_statusPrayerTitle)).setText(getContext().getString(R.string.ogle));
                                ((TextView) findViewById(R.id.tv_statusPrayer)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getOgle())));
                            } else if (currentPrayer == ASR) {
                                ((TextView) findViewById(R.id.tv_statusPrayerTitle)).setText(getContext().getString(R.string.ikindi));
                                ((TextView) findViewById(R.id.tv_statusPrayer)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getIkindi())));
                            } else if (currentPrayer == MAGHRIB) {
                                ((TextView) findViewById(R.id.tv_statusPrayerTitle)).setText(getContext().getString(R.string.aksam));
                                ((TextView) findViewById(R.id.tv_statusPrayer)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getAksam())));
                            } else if (currentPrayer == ISHA) {
                                ((TextView) findViewById(R.id.tv_statusPrayerTitle)).setText(getContext().getString(R.string.yatsi));
                                ((TextView) findViewById(R.id.tv_statusPrayer)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getYatsi())));
                            }


                            /**
                             * WEATHER BAR
                             */
                            ((TextView) findViewById(R.id.tv_statusWeather)).setText(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getMaximum() + "/" + mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getMinimum());

                            if (Preferences.getString(getContext(), ApiListEnums.THEME.getType(), "dark").equals("dark")) {
                                if (getContext() != null && mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath() != null ||
                                        !TextUtils.isEmpty(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath())) {
                                    Glide.with(getContext().getApplicationContext()).load(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath()).into(((ImageView) findViewById(R.id.img_statusWeather)));
                                }
                            } else { // fikriyat gibi açık temalarda hava durumu iconları beyaz olacak
                                if (getContext() != null && mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath2() != null ||
                                        !TextUtils.isEmpty(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath2())) {
                                    Glide.with(getContext().getApplicationContext()).load(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath2()).into(((ImageView) findViewById(R.id.img_statusWeather)));
                                }
                            }

                            if (isFirstShowing && selectedTab != null) {
                                onClick(selectedTab);
                                isFirstShowing = false;
                            }
                        } catch (NullPointerException | IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private String convertDate(String irregularDate) {
        String cleanDate = "NONE.";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss", Locale.getDefault());
            Date newDate = format.parse(irregularDate);

            format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            cleanDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cleanDate;
    }

    private void startAnimation(final TypeEnums barType, final TypeEnums statusType) {
        if (valueAnimator != null && valueAnimator.isRunning())
            return;

        int expandSize = (int) getResources().getDimension(R.dimen.status_bar_expand_height);
        int viewHeight = mRl_exchangeRoot.getHeight();

        if (statusType == EXPAND) {
            valueAnimator = ValueAnimator.ofInt(0, expandSize);
        } else {
            valueAnimator = ValueAnimator.ofInt(viewHeight, 0);
        }

        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRl_exchangeRoot.getLayoutParams().height = (int) animation.getAnimatedValue();
                mRl_exchangeRoot.requestLayout();
            }
        });

        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(1);
        valueAnimator.start();

        LAST_STATUS_TYPE = statusType;
        LAST_BAR_TYPE = barType;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (++spinnerCheck > 1 && mResponseCity != null) {
            Preferences.save(getContext(), "currentCityPosition", position);
            Preferences.save(getContext(), "currentCityCode", mResponseCity.get(position).getPlateNo());
            getStatusData(mStatusDataApiPath);
            if (LAST_STATUS_TYPE == EXPAND) {
                if (activeButton != null)
                    activeButton.setSelected(false);
                startAnimation(LAST_BAR_TYPE, COLLAPSE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        selectButton((RelativeLayout) v);
        int i = v.getId();
        if (i == R.id.rl_exchangeOpen) {
            expandStatusBar(EXCHANGE);
        } else if (i == R.id.rl_prayerOpen) {
            expandStatusBar(PRAYER);
        } else if (i == R.id.rl_weatherOpen) {
            expandStatusBar(WEATHER);
        }
    }

    private void selectButton(RelativeLayout button) {
        if (button == null)
            return;

        if (activeButton == button) {
            activeButton = null;
            button.setSelected(false);
            return;
        }

        if (activeButton != null) {
            activeButton.setSelected(false);
            activeButton = null;
        }

        activeButton = button;
        button.setSelected(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        mTimer.schedule(new statusBarTimerTask(), 5000, 5000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }
    }

    private class statusBarTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (((TextView) findViewById(R.id.tv_exchangeTitle)).getText().equals(getContext().getString(R.string.gold))) {
                                turkuvaz.sdk.models.Models.ExchangeList.Response exchangeItem = getExchangeItem(ExchangeEnums.DOLAR.getType());
                                if (exchangeItem != null) {
                                    ((TextView) findViewById(R.id.tv_exchangeTitle)).setText(getContext().getString(R.string.dollar));
                                    ((TextView) findViewById(R.id.tv_statusDollar)).setText(String.valueOf(exchangeItem.getSell()));
                                    ((ImageView) findViewById(R.id.img_exchangeImage)).setImageDrawable(Utils.isUpExchangeImage(getContext(), exchangeItem.getLastClosing(), exchangeItem.getSell()));
                                }
                            } else if (((TextView) findViewById(R.id.tv_exchangeTitle)).getText().equals(getContext().getString(R.string.dollar))) {
                                turkuvaz.sdk.models.Models.ExchangeList.Response exchangeItem = getExchangeItem(ExchangeEnums.EURO.getType());
                                if (exchangeItem != null) {
                                    ((TextView) findViewById(R.id.tv_exchangeTitle)).setText(getContext().getString(R.string.euro));
                                    ((TextView) findViewById(R.id.tv_statusDollar)).setText(String.valueOf(exchangeItem.getSell()));
                                    ((ImageView) findViewById(R.id.img_exchangeImage)).setImageDrawable(Utils.isUpExchangeImage(getContext(), exchangeItem.getLastClosing(), exchangeItem.getSell()));
                                }
                            } else if (((TextView) findViewById(R.id.tv_exchangeTitle)).getText().equals(getContext().getString(R.string.euro))) {
                                turkuvaz.sdk.models.Models.ExchangeList.Response exchangeItem = getExchangeItem(ExchangeEnums.BIST100.getType());
                                if (exchangeItem != null) {
                                    NumberFormat formatter = new DecimalFormat("#,###");
                                    ((TextView) findViewById(R.id.tv_exchangeTitle)).setText(getContext().getString(R.string.bist));
                                    ((TextView) findViewById(R.id.tv_statusDollar)).setText(String.valueOf(formatter.format(exchangeItem.getSell())));
                                    ((ImageView) findViewById(R.id.img_exchangeImage)).setImageDrawable(Utils.isUpExchangeImage(getContext(), exchangeItem.getLastClosing(), exchangeItem.getSell()));
                                }
                            } else if (((TextView) findViewById(R.id.tv_exchangeTitle)).getText().equals(getContext().getString(R.string.bist))) {
                                turkuvaz.sdk.models.Models.ExchangeList.Response exchangeItem = getExchangeItem(ExchangeEnums.ALTIN.getType());
                                if (exchangeItem != null) {
                                    ((TextView) findViewById(R.id.tv_exchangeTitle)).setText(getContext().getString(R.string.gold));
                                    ((TextView) findViewById(R.id.tv_statusDollar)).setText(String.valueOf(exchangeItem.getSell()));
                                    ((ImageView) findViewById(R.id.img_exchangeImage)).setImageDrawable(Utils.isUpExchangeImage(getContext(), exchangeItem.getLastClosing(), exchangeItem.getSell()));
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private turkuvaz.sdk.models.Models.ExchangeList.Response getExchangeItem(String type) {
        try {
            List<turkuvaz.sdk.models.Models.ExchangeList.Response> exchangeList = mAllData.getData().getPiyasaList().getResponse();
            for (turkuvaz.sdk.models.Models.ExchangeList.Response response : exchangeList) {
                if (response.getType().equals(type))
                    return response;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}