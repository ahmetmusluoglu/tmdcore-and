
package turkuvaz.sdk.models.Models.ExchangeList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("piyasaList")
    @Expose
    private PiyasaList piyasaList;
    @SerializedName("cityDetail")
    @Expose
    private CityDetail cityDetail;

    public PiyasaList getPiyasaList() {
        return piyasaList;
    }

    public void setPiyasaList(PiyasaList piyasaList) {
        this.piyasaList = piyasaList;
    }

    public CityDetail getCityDetail() {
        return cityDetail;
    }

    public void setCityDetail(CityDetail cityDetail) {
        this.cityDetail = cityDetail;
    }

}
