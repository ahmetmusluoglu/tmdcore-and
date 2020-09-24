package turkuvaz.general.turkuvazgeneralwidgets.StatusBar.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by turgay.ulutas on 16/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class CityPlakaModel implements ItemSpinnable {

    private String cityName;
    private int cityCode;

    public CityPlakaModel(String name, int expression) {
        this.cityName = name;
        this.cityCode = expression;
    }

    @Override
    @NonNull
    public String getCityName() {
        return cityName;
    }

    @Override
    @Nullable
    public int getCityCode() {
        return cityCode;
    }
}