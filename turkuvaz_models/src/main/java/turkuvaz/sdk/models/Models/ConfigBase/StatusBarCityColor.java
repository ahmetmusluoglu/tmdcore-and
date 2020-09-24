package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusBarCityColor {
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getBackground() {
        return background;
    }

    public String getText() {
        return text;
    }

    public String getIcon() {
        return icon;
    }
}