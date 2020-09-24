package turkuvaz.general.turkuvazgeneralwidgets.InformationBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.CitiesSpinnerAdapter;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.CityPlakaModel;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.ExchangeEnums;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums;
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

/**
 * Created by Ahmet MUŞLUOĞLU on 4/9/2020.
 */
public class InformationBar extends RelativeLayout implements AdapterView.OnItemSelectedListener {
    private static final String cityListApiPath = "v1/link/184";
    private static final String statusDataApiPath = "v1/link/185";
    private static final String CURRENT_CITY_CODE = "currentCityCode";
    private static final String CURRENT_CITY_POSITION = "currentCityPosition";
    private RestInterface mRestInterface;
    private LinearLayout infoFrameLayout;
    private RelativeLayout rlMain;
    private HorizontalScrollView horizontalScrollView;
    private View vLine;
    private List<Response> mResponseCity;
    private CityPlakaModel[] plakaModels;
    private Spinner spinnerAllCity;
    private View spinnerView;
    private int spinnerCheck = 0;
    private ExchangeData mAllData;
    private static int expandedIndex;
    private static final int DEFAULT = -1;
    private static final int EXCHANGE_INDEX = 0;
    private static final int PRAYER_INDEX = 3;
    private static final int WEATHER_INDEX = 2;
    private boolean isDark;
    private List<SimpleMenu> subActions;

    public InformationBar(Context context, boolean isDark, List<SimpleMenu> subActions) {
        super(context);
        this.mRestInterface = ApiClient.getClientApi(context).create(RestInterface.class);
        this.isDark = isDark;
        this.subActions = subActions;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.information_bar, this, true);
        infoFrameLayout = findViewById(R.id.infoFrameLayout);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        vLine = findViewById(R.id.vLine);
        rlMain = findViewById(R.id.rlMain);
        expandedIndex = DEFAULT;
//        vLine.setBackgroundColor(Color.parseColor(isDark ? "#FFFFFF" : "#000000"));
        rlMain.setBackgroundColor(Color.parseColor(isDark ? "#000000" : "#FFFFFF"));
        getCityList();
    }

    private void getCityList() {
        mRestInterface.getCityList(cityListApiPath).subscribeOn(Schedulers.io())
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

                    @SuppressLint("InflateParams")
                    @Override
                    public void onComplete() {
                        try {
                            spinnerView = ((LayoutInflater) Objects.requireNonNull(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.information_city_spinner, null, false);
                            spinnerAllCity = spinnerView.findViewById(R.id.spinnerAllCity);
                            spinnerAllCity.setVisibility(VISIBLE);
                            spinnerAllCity.setAdapter(new CitiesSpinnerAdapter(getContext(), plakaModels, isDark));
                            spinnerAllCity.setOnItemSelectedListener(InformationBar.this);
                            spinnerAllCity.setSelection(Preferences.getInt(getContext(), CURRENT_CITY_POSITION, 40));
                            spinnerAllCity.setPopupBackgroundResource(isDark ? R.color.siyah : R.color.beyaz);
                            getStatusData();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getStatusData() {
        int currentCityCode = Preferences.getInt(getContext(), CURRENT_CITY_CODE, 34);

        mRestInterface.getExchangeList(statusDataApiPath, currentCityCode)
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

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete() {
                        try {
                            fillList();
                            rlMain.setVisibility(VISIBLE);
                        } catch (NullPointerException | IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void fillList() {
        infoFrameLayout.removeAllViews();
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                infoFrameLayout.addView(spinnerView);
            } else {
                if (expandedIndex == i) {
                    infoFrameLayout.addView(getItemList(i));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (expandedIndex != DEFAULT) {
                                int x = infoFrameLayout.getChildAt(expandedIndex).getLeft();
                                int y = infoFrameLayout.getChildAt(expandedIndex).getTop();
                                horizontalScrollView.smoothScrollBy(x, y);
                            }
                        }
                    }, 100);
                } else {
                    infoFrameLayout.addView(getItemView(i));
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private View getItemView(int position) {
        View view = ((LayoutInflater) Objects.requireNonNull(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.information_bar_item, null, false);
        ((TextView) view.findViewById(R.id.txtTitle)).setTextColor(Color.parseColor(isDark ? "#FFFFFF" : "#000000"));
        ((TextView) view.findViewById(R.id.txtValue)).setTextColor(Color.parseColor(isDark ? "#FFFFFF" : "#000000"));
        return getSingleItem(position, view);
    }

    @SuppressLint("SetTextI18n")
    private View getSingleItem(int position, View view) {
        switch (position) {
            case EXCHANGE_INDEX:
                final turkuvaz.sdk.models.Models.ExchangeList.Response exchangeItem = getExchangeItem(ExchangeEnums.EURO.getType());
                if (exchangeItem != null) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.euro));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(String.valueOf(exchangeItem.getSell()));
                    ((ImageView) view.findViewById(R.id.imgArrow)).setImageDrawable(Utils.isUpExchangeImage2(getContext(), exchangeItem.getLastClosing(), exchangeItem.getSell()));
                    view.findViewById(R.id.imgArrow).setVisibility(VISIBLE);
                    view.findViewById(R.id.layStatusBar).setVisibility(VISIBLE);
                    view.findViewById(R.id.layStatusBar).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            expandedIndex = expandedIndex == EXCHANGE_INDEX ? DEFAULT : EXCHANGE_INDEX;
                            fillList();
                        }
                    });
                }
                return view;
            case PRAYER_INDEX:
                PrayerEnums currentPrayer = Utils.getCurrentPrayer(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getImsak(),
                        mAllData.getData().getCityDetail().getResponse().getPrayTimes().getGunes(),
                        mAllData.getData().getCityDetail().getResponse().getPrayTimes().getOgle(),
                        mAllData.getData().getCityDetail().getResponse().getPrayTimes().getIkindi(),
                        mAllData.getData().getCityDetail().getResponse().getPrayTimes().getAksam(),
                        mAllData.getData().getCityDetail().getResponse().getPrayTimes().getYatsi());

                view.findViewById(R.id.imgLeftIcon).setBackgroundResource(isDark ? R.drawable.cami : R.drawable.ic_mosque_white);
                view.findViewById(R.id.imgLeftIcon).setVisibility(VISIBLE);
                view.findViewById(R.id.layStatusBar).setVisibility(VISIBLE);
                if (currentPrayer == FAJR) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.imsak));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getImsak())));
                } else if (currentPrayer == SUNSHINE) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.gunes));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getGunes())));
                } else if (currentPrayer == DHUHR) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.ogle));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getOgle())));
                } else if (currentPrayer == ASR) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.ikindi));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getIkindi())));
                } else if (currentPrayer == MAGHRIB) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.aksam));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getAksam())));
                } else if (currentPrayer == ISHA) {
                    ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getString(R.string.yatsi));
                    ((TextView) view.findViewById(R.id.txtValue)).setText(convertDate(String.valueOf(mAllData.getData().getCityDetail().getResponse().getPrayTimes().getYatsi())));
                }
                view.findViewById(R.id.layStatusBar).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandedIndex = expandedIndex == PRAYER_INDEX ? DEFAULT : PRAYER_INDEX;
                        fillList();
                    }
                });
                return view;
            case WEATHER_INDEX:
                view.findViewById(R.id.layStatusBar).setVisibility(VISIBLE);
                ((TextView) view.findViewById(R.id.txtTitle)).setText(getContext().getResources().getString(R.string.bugun_kucuk));
                ((TextView) view.findViewById(R.id.txtValue)).setText(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getMaximum() + "/" + mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getMinimum());
                ImageView imgLeftIcon = view.findViewById(R.id.imgLeftIcon);
                imgLeftIcon.setVisibility(VISIBLE);
                if (isDark) {
                    if (getContext() != null && mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath() != null ||
                            !TextUtils.isEmpty(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath())) {
                        Glide.with(getContext().getApplicationContext()).load(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath()).into((imgLeftIcon));
                    }
                } else {
                    if (getContext() != null && mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath2() != null ||
                            !TextUtils.isEmpty(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath2())) {
                        Glide.with(getContext().getApplicationContext()).load(mAllData.getData().getCityDetail().getResponse().getWeather().get(0).getImagePath2()).into(imgLeftIcon);
                    }
                }
                view.findViewById(R.id.layStatusBar).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expandedIndex = expandedIndex == WEATHER_INDEX ? DEFAULT : WEATHER_INDEX;
                        fillList();
                    }
                });
                return view;
            default:
                return null;
        }

    }

    private RecyclerView getItemList(int position) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        InfoType infoType = position == EXCHANGE_INDEX ? InfoType.EXCHANGE : position == WEATHER_INDEX ? InfoType.WEATHER : InfoType.PRAY_TIMES;
        int spanCount = position == EXCHANGE_INDEX ? 4 : position == WEATHER_INDEX ? 3 : 7;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        InformationAdapter informationAdapter = new InformationAdapter(mAllData.getData(), infoType, isDark, subActions, position);
        informationAdapter.setOnclickListener(new InformationAdapter.OnclickListener() {
            @Override
            public void onclick() {
                expandedIndex = DEFAULT;
                fillList();
            }
        });

        recyclerView.setAdapter(informationAdapter);
        return recyclerView;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (++spinnerCheck > 1 && mResponseCity != null) {
            Preferences.save(getContext(), CURRENT_CITY_POSITION, position);
            Preferences.save(getContext(), CURRENT_CITY_CODE, mResponseCity.get(position).getPlateNo());
            expandedIndex = DEFAULT;
            getStatusData();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
