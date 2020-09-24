package turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.GlobalMethods.AllFlavors;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.AppNameTypes;
import turkuvaz.sdk.models.Models.Preferences;


/**
 * Created by turgay.ulutas on 16/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class IconSpinnerAdapter extends BaseAdapter {
    final private Context context;
    final private ItemSpinnable[] spinnerEntries;

    public <E extends ItemSpinnable> IconSpinnerAdapter(
            Context context,
            E[] spinnerEntries) {
        this.context = context;
        this.spinnerEntries = spinnerEntries;
    }

    @Override
    public int getCount() {
        return spinnerEntries.length;
    }

    @Override
    public Object getItem(int position) {
        return spinnerEntries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        return getCustomView(pos, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        ItemSpinnable item = spinnerEntries[position];

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup spinnerView = (ViewGroup) inflater.inflate(R.layout.status_bar_icon_spinner_item, parent, false);

        TextView title = spinnerView.findViewById(R.id.title);
        title.setText(item.getCityName());

        title.setTextColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_CITY_TEXT.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.beyaz)))));
        title.setBackgroundColor(Color.parseColor(Preferences.getString(context, ApiListEnums.STATUS_BAR_CITY_BACK.getType(), "#" + Integer.toHexString(context.getResources().getColor(R.color.siyah)))));

        return spinnerView;
    }
}