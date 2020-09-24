
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsMenu {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("apiPath")
    @Expose
    private String apiPath;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("pids")
    @Expose
    private Pids pids;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Pids getPids() {
        return pids;
    }

    public void setPids(Pids pids) {
        this.pids = pids;
    }
}
