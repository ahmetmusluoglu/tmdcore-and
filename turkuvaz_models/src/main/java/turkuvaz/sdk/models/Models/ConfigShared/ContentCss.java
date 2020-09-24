
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentCss {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("versiyon")
    @Expose
    private Integer versiyon;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVersiyon() {
        return versiyon;
    }

    public void setVersiyon(Integer versiyon) {
        this.versiyon = versiyon;
    }

}
