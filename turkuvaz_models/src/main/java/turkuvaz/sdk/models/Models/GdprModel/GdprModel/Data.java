
package turkuvaz.sdk.models.Models.GdprModel.GdprModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("ipCheckUrl")
    @Expose
    private String ipCheckUrl;
    @SerializedName("gdpr")
    @Expose
    private Gdpr gdpr;
    @SerializedName("kvkk")
    @Expose
    private Kvkk kvkk;

    public String getIpCheckUrl() {
        return ipCheckUrl;
    }

    public void setIpCheckUrl(String ipCheckUrl) {
        this.ipCheckUrl = ipCheckUrl;
    }

    public Gdpr getGdpr() {
        return gdpr;
    }

    public void setGdpr(Gdpr gdpr) {
        this.gdpr = gdpr;
    }

    public Kvkk getKvkk() {
        return kvkk;
    }

    public void setKvkk(Kvkk kvkk) {
        this.kvkk = kvkk;
    }

}
