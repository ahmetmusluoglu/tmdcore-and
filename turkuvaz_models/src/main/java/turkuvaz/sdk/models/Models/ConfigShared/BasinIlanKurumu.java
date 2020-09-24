
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasinIlanKurumu {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("platformIos")
    @Expose
    private Boolean platformIos;
    @SerializedName("platformAndroid")
    @Expose
    private Boolean platformAndroid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPlatformIos() {
        return platformIos;
    }

    public void setPlatformIos(Boolean platformIos) {
        this.platformIos = platformIos;
    }

    public Boolean getPlatformAndroid() {
        return platformAndroid;
    }

    public void setPlatformAndroid(Boolean platformAndroid) {
        this.platformAndroid = platformAndroid;
    }

}
