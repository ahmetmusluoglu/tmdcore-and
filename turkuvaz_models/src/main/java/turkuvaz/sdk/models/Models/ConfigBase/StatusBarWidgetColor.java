package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusBarWidgetColor {
    @SerializedName("closeBackground")
    @Expose
    private String closeBackground;
    @SerializedName("openBackground")
    @Expose
    private String openBackground;
    @SerializedName("widgetTitle")
    @Expose
    private String widgetTitle;
    @SerializedName("widgetValue")
    @Expose
    private String widgetValue;

    public String getCloseBackground() {
        return closeBackground;
    }

    public String getOpenBackground() {
        return openBackground;
    }

    public String getWidgetTitle() {
        return widgetTitle;
    }

    public String getWidgetValue() {
        return widgetValue;
    }
}