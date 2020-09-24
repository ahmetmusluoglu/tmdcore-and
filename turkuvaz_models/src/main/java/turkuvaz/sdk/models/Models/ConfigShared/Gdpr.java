
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gdpr {

    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("version")
    @Expose
    private Double version;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

}
