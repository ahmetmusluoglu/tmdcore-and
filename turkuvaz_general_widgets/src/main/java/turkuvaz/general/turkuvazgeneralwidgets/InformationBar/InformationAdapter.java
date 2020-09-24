package turkuvaz.general.turkuvazgeneralwidgets.InformationBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.ExchangeEnums;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums;
import turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.Utils;
import turkuvaz.sdk.global.GlobalIntent;
import turkuvaz.sdk.models.Models.ConfigBase.SimpleMenu;
import turkuvaz.sdk.models.Models.ExchangeList.Data;
import turkuvaz.sdk.models.Models.ExchangeList.PrayTimes;
import turkuvaz.sdk.models.Models.ExchangeList.Response;
import turkuvaz.sdk.models.Models.ExchangeList.Weather;

import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.ASR;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.DHUHR;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.FAJR;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.ISHA;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.MAGHRIB;
import static turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils.PrayerEnums.SUNSHINE;

/**
 * Created by Ahmet MUŞLUOĞLU on 4/9/2020.
 */
public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewHolder> {
    private Data data;
    private InfoType infoType;
    private Context context;
    private boolean isDark;
    private List<SimpleMenu> subActions;
    private int selectedPos;

    InformationAdapter(Data data, InfoType infoType, boolean isDark, List<SimpleMenu> subActions, int position) {
        this.infoType = infoType;
        this.data = data;
        this.isDark = isDark;
        this.subActions = subActions;
        this.selectedPos = position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_bar_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
        try {
            viewHolder.txtTitle.setTextColor(Color.parseColor(isDark ? "#FFFFFF" : "#000000"));
            viewHolder.txtValue.setTextColor(Color.parseColor(isDark ? "#FFFFFF" : "#000000"));

            viewHolder.vLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("","");
                }
            });

            viewHolder.layStatusBar.setVisibility(View.VISIBLE);
            if (infoType == InfoType.EXCHANGE) {
                viewHolder.imgArrow.setVisibility(View.VISIBLE);
                final Response currentModel = getResponseItem(position);
                if (currentModel != null && currentModel.getType() != null && currentModel.getSell() != null) {
                    if (currentModel.getType().equals(ExchangeEnums.EURO.getType())) {
                        viewHolder.txtTitle.setText(context.getString(R.string.euro));
                        viewHolder.txtValue.setText(String.valueOf(currentModel.getSell()));
                        viewHolder.imgArrow.setImageDrawable(Utils.isUpExchangeImage2(context, currentModel.getLastClosing(), currentModel.getSell()));
                    } else if (currentModel.getType().equals(ExchangeEnums.ALTIN.getType())) {
                        viewHolder.txtTitle.setText(context.getString(R.string.gold));
                        viewHolder.txtValue.setText(String.valueOf(currentModel.getSell()));
                        viewHolder.imgArrow.setImageDrawable(Utils.isUpExchangeImage2(context, currentModel.getLastClosing(), currentModel.getSell()));
                    } else if (currentModel.getType().equals(ExchangeEnums.DOLAR.getType())) {
                        viewHolder.txtTitle.setText(context.getString(R.string.dollar));
                        viewHolder.txtValue.setText(String.valueOf(currentModel.getSell()));
                        viewHolder.imgArrow.setImageDrawable(Utils.isUpExchangeImage2(context, currentModel.getLastClosing(), currentModel.getSell()));
                    } else if (currentModel.getType().equals(ExchangeEnums.BIST100.getType())) {
                        NumberFormat formatter = new DecimalFormat("#,###");
                        viewHolder.txtTitle.setText(context.getString(R.string.bist));
                        viewHolder.txtValue.setText(String.valueOf(formatter.format(currentModel.getSell())));
                        viewHolder.imgArrow.setImageDrawable(Utils.isUpExchangeImage2(context, currentModel.getLastClosing(), currentModel.getSell()));
                    } else {
                        viewHolder.txtTitle.setText(currentModel.getType());
                        viewHolder.txtValue.setText(String.valueOf(currentModel.getSell()));
                        viewHolder.imgArrow.setImageDrawable(Utils.isUpExchangeImage2(context, currentModel.getLastClosing(), currentModel.getSell()));
                    }
                }
            } else if (infoType == InfoType.PRAY_TIMES) {
                PrayTimes prayTimes = getPrayTimes();
                PrayerEnums currentPrayer = Utils.getCurrentPrayer(prayTimes.getImsak(), prayTimes.getGunes(), prayTimes.getOgle(), prayTimes.getIkindi(), prayTimes.getAksam(), prayTimes.getYatsi());
                viewHolder.vLine.setVisibility(View.INVISIBLE);
                if (position == 0) {
                    viewHolder.imgLeftIcon.setBackgroundResource(isDark ? R.drawable.cami : R.drawable.ic_mosque_white);
                    viewHolder.imgLeftIcon.setVisibility(View.VISIBLE);
                    viewHolder.rlContainer.setVisibility(View.GONE);
                } else if (position == 1) {
                    viewHolder.txtTitle.setText(context.getString(R.string.imsak));
                    viewHolder.txtValue.setText(Utils.dateToString(prayTimes.getImsak()));
                    if (currentPrayer == FAJR) {
                        viewHolder.vLine.setVisibility(View.VISIBLE);
                    }
                } else if (position == 2) {
                    viewHolder.txtTitle.setText(context.getString(R.string.gunes));
                    viewHolder.txtValue.setText(Utils.dateToString(prayTimes.getGunes()));
                    if (currentPrayer == SUNSHINE) {
                        viewHolder.vLine.setVisibility(View.VISIBLE);
                    }
                } else if (position == 3) {
                    viewHolder.txtTitle.setText(context.getString(R.string.ogle));
                    viewHolder.txtValue.setText(Utils.dateToString(prayTimes.getOgle()));
                    if (currentPrayer == DHUHR) {
                        viewHolder.vLine.setVisibility(View.VISIBLE);
                    }
                } else if (position == 4) {
                    viewHolder.txtTitle.setText(context.getString(R.string.ikindi));
                    viewHolder.txtValue.setText(Utils.dateToString(prayTimes.getIkindi()));
                    if (currentPrayer == ASR) {
                        viewHolder.vLine.setVisibility(View.VISIBLE);
                    }
                } else if (position == 5) {
                    viewHolder.txtTitle.setText(context.getString(R.string.aksam));
                    viewHolder.txtValue.setText(Utils.dateToString(prayTimes.getAksam()));
                    if (currentPrayer == MAGHRIB) {
                        viewHolder.vLine.setVisibility(View.VISIBLE);
                    }
                } else if (position == 6) {
                    viewHolder.txtTitle.setText(context.getString(R.string.yatsi));
                    viewHolder.txtValue.setText(Utils.dateToString(prayTimes.getYatsi()));
                    if (currentPrayer == ISHA) {
                        viewHolder.vLine.setVisibility(View.VISIBLE);
                    }
                }
            } else if (infoType == InfoType.WEATHER) {
                final Weather currentModel = getWeatherItem(position);
                viewHolder.imgLeftIcon.setVisibility(View.VISIBLE);
                Glide.with(context.getApplicationContext()).load(isDark ? currentModel.getImagePath() : currentModel.getImagePath2()).into(viewHolder.imgLeftIcon);
                viewHolder.txtTitle.setText(position == 0 ? "Bugün" : position == 1 ? "Yarın" : currentModel.getDayName() != null ? currentModel.getDayName() : "---");
                viewHolder.txtValue.setText(currentModel.getMaximum() != null && currentModel.getMinimum() != null ? currentModel.getMaximum() + "/" + currentModel.getMinimum() : "---");
            }

            viewHolder.layStatusBar.setOnClickListener(new View.OnClickListener() { // AHBR-4764
                @Override
                public void onClick(View view) {
                    if (subActions != null) {
                        for (int i = 0; i < subActions.size(); i++) {
                            if (subActions.get(i).getActive() && String.valueOf(selectedPos).equals(subActions.get(i).getId())) {
                                GlobalIntent.openExternal(context, subActions.get(i).getExternalUrl());
                                break;
                            }
                        }
                    }
                }
            });
        } catch (NullPointerException | Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            switch (infoType) {
                case EXCHANGE:
                    return data.getPiyasaList() != null ? data.getPiyasaList().getResponse().size() : 0;
                case WEATHER:
                    return data.getCityDetail() != null ? data.getCityDetail().getResponse().getWeather().size() : 0;
                case PRAY_TIMES:
                    return data.getCityDetail() != null ? 7 : 0;
            }
        }
        return 0;
    }

    private Response getResponseItem(int position) {
        return data.getPiyasaList().getResponse().get(position);
    }

    private Weather getWeatherItem(int position) {
        return data.getCityDetail().getResponse().getWeather().get(position);
    }

    private PrayTimes getPrayTimes() {
        return data.getCityDetail().getResponse().getPrayTimes();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtValue;
        ImageView imgLeftIcon, imgArrow;
        RelativeLayout layStatusBar;
        RelativeLayout rlContainer;
        View vLine;

        MyViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtValue = itemView.findViewById(R.id.txtValue);
            imgLeftIcon = itemView.findViewById(R.id.imgLeftIcon);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            vLine = itemView.findViewById(R.id.vLine);
            layStatusBar = itemView.findViewById(R.id.layStatusBar);
            rlContainer = itemView.findViewById(R.id.rlContainer);
        }
    }

    private OnclickListener onclickListener;

    void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    public interface OnclickListener {
        void onclick();
    }
}
