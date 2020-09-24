
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopBarNavigation {

    @SerializedName(value = "title")
    @Expose
    private String title;
    @SerializedName(value = "type")
    @Expose
    private String type;
    @SerializedName(value = "ExternalUrl")
    @Expose
    private String ExternalUrl;
    @SerializedName(value = "isActive")
    @Expose
    private Boolean isActive;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExternalUrl() {
        return ExternalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        ExternalUrl = externalUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public TopBarNavigation(String title, String type, String externalUrl, Boolean isActive) {
        this.title = title;
        this.type = type;
        ExternalUrl = externalUrl;
        this.isActive = isActive;
    }
}
