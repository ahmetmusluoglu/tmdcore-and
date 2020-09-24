
package turkuvaz.sdk.models.Models.ExchangeList;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response_ {

    @SerializedName("PrayTimes")
    @Expose
    private PrayTimes prayTimes;
    @SerializedName("City")
    @Expose
    private City city;
    @SerializedName("Weather")
    @Expose
    private ArrayList<Weather> weather = null;

    public PrayTimes getPrayTimes() {
        return prayTimes;
    }

    public void setPrayTimes(PrayTimes prayTimes) {
        this.prayTimes = prayTimes;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

}
