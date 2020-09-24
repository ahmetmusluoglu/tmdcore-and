
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppColorSettings {

    @SerializedName("toolbarBack")
    @Expose
    private String toolbarBack;
    @SerializedName("toolbarIcon")
    @Expose
    private String toolbarIcon;
    @SerializedName("leftMenuBack")
    @Expose
    private String leftMenuBack;
    @SerializedName("leftMenuIcon")
    @Expose
    private String leftMenuIcon;
    @SerializedName("leftMenuDetailBack")
    @Expose
    private String leftMenuDetailBack;
    @SerializedName("leftMenuDetailIcon")
    @Expose
    private String leftMenuDetailIcon;
    @SerializedName("otherColor")
    @Expose
    private String otherColor;

    public String getToolbarBack() {
        return toolbarBack;
    }

    public void setToolbarBack(String toolbarBack) {
        this.toolbarBack = toolbarBack;
    }

    public String getToolbarIcon() {
        return toolbarIcon;
    }

    public void setToolbarIcon(String toolbarIcon) {
        this.toolbarIcon = toolbarIcon;
    }

    public String getLeftMenuBack() {
        return leftMenuBack;
    }

    public void setLeftMenuBack(String leftMenuBack) {
        this.leftMenuBack = leftMenuBack;
    }

    public String getLeftMenuIcon() {
        return leftMenuIcon;
    }

    public void setLeftMenuIcon(String leftMenuIcon) {
        this.leftMenuIcon = leftMenuIcon;
    }

    public String getLeftMenuDetailBack() {
        return leftMenuDetailBack;
    }

    public void setLeftMenuDetailBack(String leftMenuDetailBack) {
        this.leftMenuDetailBack = leftMenuDetailBack;
    }

    public String getLeftMenuDetailIcon() {
        return leftMenuDetailIcon;
    }

    public void setLeftMenuDetailIcon(String leftMenuDetailIcon) {
        this.leftMenuDetailIcon = leftMenuDetailIcon;
    }

    public String getOtherColor() {
        return otherColor;
    }

    public void setOtherColor(String otherColor) {
        this.otherColor = otherColor;
    }
}
