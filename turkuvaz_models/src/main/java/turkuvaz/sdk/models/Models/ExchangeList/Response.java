
package turkuvaz.sdk.models.Models.ExchangeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("Min")
    @Expose
    private Double min;
    @SerializedName("Max")
    @Expose
    private Double max;
    @SerializedName("Time")
    @Expose
    private String time;
    @SerializedName("Buy")
    @Expose
    private Double buy;
    @SerializedName("Symbol")
    @Expose
    private String symbol;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Sell")
    @Expose
    private Double sell;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("LastClosing")
    @Expose
    private Double lastClosing;

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLastClosing() {
        return lastClosing;
    }

    public void setLastClosing(Double lastClosing) {
        this.lastClosing = lastClosing;
    }

}
