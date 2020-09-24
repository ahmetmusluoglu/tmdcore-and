
package turkuvaz.sdk.models.Models.ExchangeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrayTimes {

    @SerializedName("Imsak")
    @Expose
    private String imsak;
    @SerializedName("Aksam")
    @Expose
    private String aksam;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("Gunes")
    @Expose
    private String gunes;
    @SerializedName("Ogle")
    @Expose
    private String ogle;
    @SerializedName("Ikindi")
    @Expose
    private String ikindi;
    @SerializedName("Yatsi")
    @Expose
    private String yatsi;
    @SerializedName("PrayDate")
    @Expose
    private String prayDate;

    public String getImsak() {
        return imsak;
    }

    public void setImsak(String imsak) {
        this.imsak = imsak;
    }

    public String getAksam() {
        return aksam;
    }

    public void setAksam(String aksam) {
        this.aksam = aksam;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getGunes() {
        return gunes;
    }

    public void setGunes(String gunes) {
        this.gunes = gunes;
    }

    public String getOgle() {
        return ogle;
    }

    public void setOgle(String ogle) {
        this.ogle = ogle;
    }

    public String getIkindi() {
        return ikindi;
    }

    public void setIkindi(String ikindi) {
        this.ikindi = ikindi;
    }

    public String getYatsi() {
        return yatsi;
    }

    public void setYatsi(String yatsi) {
        this.yatsi = yatsi;
    }

    public String getPrayDate() {
        return prayDate;
    }

    public void setPrayDate(String prayDate) {
        this.prayDate = prayDate;
    }

}
