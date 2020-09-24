
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToolbarMenu {

    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("ExternalUrl")
    @Expose
    private String externalUrl;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("primaryColor")
    @Expose
    private Boolean primaryColor;
    @SerializedName("type")
    @Expose
    private String type;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Boolean primaryColor) {
        this.primaryColor = primaryColor;
    }
}
