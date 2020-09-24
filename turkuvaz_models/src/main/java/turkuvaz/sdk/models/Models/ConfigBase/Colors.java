
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Colors {

    @SerializedName(value = "leftMenuColor")
    @Expose
    private LeftMenuColor leftMenuColor;
    @SerializedName(value = "toolbarColor")
    @Expose
    private ToolbarColor toolbarColor;
    @SerializedName(value = "socialBarColor")
    @Expose
    private SocialbarColor socialBarColor;
    @SerializedName(value = "columnistColor")
    @Expose
    private ColumnistColor columnistColor;
    @SerializedName(value = "lastMinuteColor")
    @Expose
    private LastMinuteColor lastMinuteColor;
    @SerializedName(value = "tvStreamColor")
    @Expose
    private TvStreamColor tvStreamColor;
    @SerializedName(value = "statusBarCityColor")
    @Expose
    private StatusBarCityColor statusBarCityColor;
    @SerializedName(value = "statusBarWidgetColor")
    @Expose
    private StatusBarWidgetColor statusBarWidgetColor;
    @SerializedName(value = "bottomBarColor")
    @Expose
    private BottomBarColor bottomBarColor;

    public LeftMenuColor getLeftMenuColor() {
        return leftMenuColor;
    }

    public ToolbarColor getToolbarColor() {
        return toolbarColor;
    }

    public SocialbarColor getSocialbarColor() {
        return socialBarColor;
    }

    public ColumnistColor getColumnistColor() {
        return columnistColor;
    }

    public LastMinuteColor getLastMinuteColor() {
        return lastMinuteColor;
    }

    public TvStreamColor getTvStreamColor() {
        return tvStreamColor;
    }

    public StatusBarCityColor getStatusBarCityColor() {
        return statusBarCityColor;
    }

    public StatusBarWidgetColor getStatusBarWidgetColor() {
        return statusBarWidgetColor;
    }

    public BottomBarColor getBottomBarColor() {
        return bottomBarColor;
    }
}
