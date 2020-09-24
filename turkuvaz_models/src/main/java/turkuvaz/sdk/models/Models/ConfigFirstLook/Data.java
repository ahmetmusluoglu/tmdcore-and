
package turkuvaz.sdk.models.Models.ConfigFirstLook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    // artık 2 olan url'den alacak parametreleri ve configurl'i de başına ekleyecek
    @SerializedName("androidConfigUrl2")
    @Expose
    private String androidConfigUrl;
    @SerializedName("iosConfigUrl2")
    @Expose
    private String iosConfigUrl;
    @SerializedName("sharedConfigUrl2")
    @Expose
    private String sharedConfigUrl;

    public String getAndroidConfigUrl() {
        return androidConfigUrl;
    }

    public void setAndroidConfigUrl(String androidConfigUrl) {
        this.androidConfigUrl = androidConfigUrl;
    }

    public String getIosConfigUrl() {
        return iosConfigUrl;
    }

    public void setIosConfigUrl(String iosConfigUrl) {
        this.iosConfigUrl = iosConfigUrl;
    }

    public String getSharedConfigUrl() {
        return sharedConfigUrl;
    }

    public void setSharedConfigUrl(String sharedConfigUrl) {
        this.sharedConfigUrl = sharedConfigUrl;
    }

}
