
package turkuvaz.sdk.models.Models.GdprModel.GdprModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Button1 {

    @SerializedName("defaultValue")
    @Expose
    private Boolean defaultValue;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("text")
    @Expose
    private String text;

    public Boolean getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
