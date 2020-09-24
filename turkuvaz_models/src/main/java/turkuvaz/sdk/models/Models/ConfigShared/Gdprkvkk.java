
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gdprkvkk {

    @SerializedName("gdpr")
    @Expose
    private Gdpr gdpr;
    @SerializedName("kvkk")
    @Expose
    private Kvkk kvkk;
    @SerializedName("version")
    @Expose
    private Double version;
    @SerializedName("apiUrl")
    @Expose
    private String apiUrl;
    @SerializedName("title")
    @Expose
    private String title;

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

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
