
package turkuvaz.sdk.models.Models.ExchangeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("Region")
    @Expose
    private String region;
    @SerializedName("StationId")
    @Expose
    private Integer stationId;
    @SerializedName("StationDisplayName")
    @Expose
    private String stationDisplayName;
    @SerializedName("Maximum")
    @Expose
    private String maximum;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("EndString")
    @Expose
    private String endString;
    @SerializedName("Minimum")
    @Expose
    private String minimum;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;
    @SerializedName("ImagePath2") // Fikriyatta hava durumu iconu siyah olacak
    @Expose
    private String imagePath2;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Temperature")
    @Expose
    private String temperature;
    @SerializedName("DayName")
    @Expose
    private String dayName;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStationDisplayName() {
        return stationDisplayName;
    }

    public void setStationDisplayName(String stationDisplayName) {
        this.stationDisplayName = stationDisplayName;
    }

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndString() {
        return endString;
    }

    public void setEndString(String endString) {
        this.endString = endString;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

}
