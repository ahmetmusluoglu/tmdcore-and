package turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Adapters;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.ExchangeEnums;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.ConfigBase.SimpleMenu;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.AppNameTypes;
import turkuvaz.sdk.models.Models.ExchangeList.PrayTimes;
import turkuvaz.sdk.models.Models.ExchangeList.Response;
import turkuvaz.sdk.models.Models.ExchangeList.Weather;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums;

import com.bumptech.glide.Glide;

import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.models.Models.Preferences;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.*;

/**
 * Created by turgay.ulutas on 16/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class StatusAdapter extends RecyclerView.Adapter {
    private int CURRENT_TYPE;
    private List<Response> arrayList;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Weather> weather;
    private PrayTimes prayTimes;
    private RelativeLayout rl_exchangeOpen;
    String url;
    private List<SimpleMenu> subActions;

    public StatusAdapter(List<Response> dummyData, Context activity, List<SimpleMenu> subActions) {
        if (activity == null)
            return;
        CURRENT_TYPE = 1;
        this.arrayList = dummyData;
        this.context = activity;
        this.inflater = LayoutInflater.from(context);
        this.subActions = subActions;
    }

    public StatusAdapter(ArrayList<Weather> weather, Context activity, List<SimpleMenu> subActions) {
        if (activity == null)
            return;
        CURRENT_TYPE = 2;
        this.weather = weather;
        this.context = activity;
        this.inflater = LayoutInflater.from(context);
        this.subActions = subActions;
    }

    public StatusAdapter(PrayTimes prayTimes, Context activity, List<SimpleMenu> subActions) {
        if (activity == null)
            return;
        CURRENT_TYPE = 3;
        this.prayTimes = prayTimes;
        this.context = activity;
        this.inflater = LayoutInflater.from(context);
        this.subActions = subActions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (CURRENT_TYPE == 2)
            return new ViewHolder(inflater.inflate(R.layout.status_bar_item_exc_with_image, parent, false));
        else
            return new ViewHolder(inflater.inflate(R.layout.status_bar_item_exchange, parent, false));
    }

    @Override
    public int getItemCount() {
        if (CURRENT_TYPE == 1)
            return arrayList == null ? 0 : arrayList.size();
        else if (CURRENT_TYPE == 3)
            return 6;
        else if (CURRENT_TYPE == 2)
            return 3;
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final ViewHolder viewHolder = (ViewHolder) holder;

            // widgette namaz vaktine tıklayınca açılan alt menünün arkaplan rengi değiştirildi
            viewHolder.layStatusBar.setBackgroundColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_BACK_OPEN.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.dark_gray_bar)))));
            if (Preferences.getString(context, ApiListEnums.THEME.getType(), "dark").equals("light")) // fikriyat gibi açık temalı uygulamalarda renk gri olacak
                viewHolder.rl_exchangeOpen.setBackground(context.getResources().getDrawable(R.drawable.left_light_gray_line));

            viewHolder.rl_exchangeOpen.setOnClickListener(new View.OnClickListener() { // AHBR-4764
                @Override
                public void onClick(View v) {
                    if (subActions != null) {
                        for (int i = 0; i < subActions.size(); i++) {
                            if (subActions.get(i).getActive() && String.valueOf(CURRENT_TYPE).equals(subActions.get(i).getId())) {
                                GlobalIntent.openExternal(context, subActions.get(i).getExternalUrl());
                                break;
                            }
                        }
                    }
                }
            });

            if (CURRENT_TYPE == 1) {
                viewHolder.mTv_exchangeName.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz2)))));
                viewHolder.mTv_exchangeCount.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz)))));

                final Response currentModel = arrayList.get(position);
                if (currentModel != null && currentModel.getType() != null && currentModel.getSell() != null) {
                    if (currentModel.getType().equals(ExchangeEnums.EURO.getType())) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.euro));
                        viewHolder.mTv_exchangeCount.setTextColor(Utils.isUpExchangeColor(context, currentModel.getLastClosing(), currentModel.getSell(), false));
                        viewHolder.mTv_exchangeCount.setText(String.valueOf(currentModel.getSell()));
                    } else if (currentModel.getType().equals(ExchangeEnums.ALTIN.getType())) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.gold));
                        viewHolder.mTv_exchangeCount.setTextColor(Utils.isUpExchangeColor(context, currentModel.getLastClosing(), currentModel.getSell(), false));
                        viewHolder.mTv_exchangeCount.setText(String.valueOf(currentModel.getSell()));
                    } else if (currentModel.getType().equals(ExchangeEnums.DOLAR.getType())) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.dollar));
                        viewHolder.mTv_exchangeCount.setTextColor(Utils.isUpExchangeColor(context, currentModel.getLastClosing(), currentModel.getSell(), false));
                        viewHolder.mTv_exchangeCount.setText(String.valueOf(currentModel.getSell()));
                    } else if (currentModel.getType().equals(ExchangeEnums.BIST100.getType())) {
                        NumberFormat formatter = new DecimalFormat("#,###");
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.bist));
                        viewHolder.mTv_exchangeCount.setTextColor(Utils.isUpExchangeColor(context, currentModel.getLastClosing(), currentModel.getSell(), true));
                        viewHolder.mTv_exchangeCount.setText(String.valueOf(formatter.format(currentModel.getSell())));
                    } else {
                        viewHolder.mTv_exchangeName.setText(currentModel.getType());
                        viewHolder.mTv_exchangeCount.setTextColor(Utils.isUpExchangeColor(context, currentModel.getLastClosing(), currentModel.getSell(), false));
                        viewHolder.mTv_exchangeCount.setText(String.valueOf(currentModel.getSell()));
                    }
                }


            } else if (CURRENT_TYPE == 3) {
                PrayerEnums currentPrayer = Utils.getCurrentPrayer(prayTimes.getImsak(), prayTimes.getGunes(), prayTimes.getOgle(), prayTimes.getIkindi(), prayTimes.getAksam(), prayTimes.getYatsi());

                for (int i = 0; i < getItemCount(); i++) {
                    // önce döngüye girip renk atasın. çünkü aşağıda vaktin durumuna göre tekrar renk atıyor.
                    viewHolder.mTv_exchangeName.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz2)))));
                    viewHolder.mTv_exchangeCount.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz)))));

                    if (position == 0) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.imsak));
                        viewHolder.mTv_exchangeCount.setText(Utils.dateToString(prayTimes.getImsak()));
                        if (currentPrayer == FAJR) {
                            viewHolder.mTv_exchangeName.setTextColor(context.getResources().getColor(R.color.exchange_up));
                            viewHolder.mTv_exchangeCount.setTextColor(context.getResources().getColor(R.color.exchange_up));
                        }
                    } else if (position == 1) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.gunes));
                        viewHolder.mTv_exchangeCount.setText(Utils.dateToString(prayTimes.getGunes()));
                        if (currentPrayer == SUNSHINE) {
                            viewHolder.mTv_exchangeName.setTextColor(context.getResources().getColor(R.color.exchange_up));
                            viewHolder.mTv_exchangeCount.setTextColor(context.getResources().getColor(R.color.exchange_up));
                        }
                    } else if (position == 2) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.ogle));
                        viewHolder.mTv_exchangeCount.setText(Utils.dateToString(prayTimes.getOgle()));
                        if (currentPrayer == DHUHR) {
                            viewHolder.mTv_exchangeName.setTextColor(context.getResources().getColor(R.color.exchange_up));
                            viewHolder.mTv_exchangeCount.setTextColor(context.getResources().getColor(R.color.exchange_up));
                        }
                    } else if (position == 3) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.ikindi));
                        viewHolder.mTv_exchangeCount.setText(Utils.dateToString(prayTimes.getIkindi()));
                        if (currentPrayer == ASR) {
                            viewHolder.mTv_exchangeName.setTextColor(context.getResources().getColor(R.color.exchange_up));
                            viewHolder.mTv_exchangeCount.setTextColor(context.getResources().getColor(R.color.exchange_up));
                        }
                    } else if (position == 4) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.aksam));
                        viewHolder.mTv_exchangeCount.setText(Utils.dateToString(prayTimes.getAksam()));
                        if (currentPrayer == MAGHRIB) {
                            viewHolder.mTv_exchangeName.setTextColor(context.getResources().getColor(R.color.exchange_up));
                            viewHolder.mTv_exchangeCount.setTextColor(context.getResources().getColor(R.color.exchange_up));
                        }
                    } else if (position == 5) {
                        viewHolder.mTv_exchangeName.setText(context.getString(R.string.yatsi));
                        viewHolder.mTv_exchangeCount.setText(Utils.dateToString(prayTimes.getYatsi()));
                        if (currentPrayer == ISHA) {
                            viewHolder.mTv_exchangeName.setTextColor(context.getResources().getColor(R.color.exchange_up));
                            viewHolder.mTv_exchangeCount.setTextColor(context.getResources().getColor(R.color.exchange_up));
                        }
                    }
                }
            } else if (CURRENT_TYPE == 2) {
                final Weather currentModel = weather.get(position);

                if (currentModel != null && currentModel.getImagePath() != null) {
                    if (Preferences.getString(context, ApiListEnums.THEME.getType(), "dark").equals("dark"))
                        Glide.with(context.getApplicationContext()).load(currentModel.getImagePath()).into(viewHolder.mImg_exchangeImage);
                    else // fikriyat gibi açık temalarda hava durumu iconları beyaz olacak
                        Glide.with(context.getApplicationContext()).load(currentModel.getImagePath2()).into(viewHolder.mImg_exchangeImage);
                }

                if (currentModel != null && currentModel.getDayName() != null) {
                    viewHolder.mTv_exchangeName.setText(currentModel.getDayName());
                }

                if (currentModel != null && currentModel.getMaximum() != null && currentModel.getMinimum() != null) {
                    viewHolder.mTv_exchangeCount.setText(currentModel.getMaximum() + "/" + currentModel.getMinimum());
                }

                viewHolder.mTv_exchangeName.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_TITLE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz2)))));
                viewHolder.mTv_exchangeCount.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_WIDGET_TEXT_VALUE.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz)))));
            }
        } catch (NullPointerException | Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTv_exchangeName, mTv_exchangeCount;
        ImageView mImg_exchangeImage;
        LinearLayout layStatusBar;
        RelativeLayout rl_exchangeOpen;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_exchangeName = itemView.findViewById(R.id.tv_exchangeName);
            mTv_exchangeCount = itemView.findViewById(R.id.tv_exchangeCount);
            mImg_exchangeImage = itemView.findViewById(R.id.img_exchangeImage);
            layStatusBar = itemView.findViewById(R.id.layStatusBar);
            rl_exchangeOpen = itemView.findViewById(R.id.rl_exchangeOpen);
        }
    }
}