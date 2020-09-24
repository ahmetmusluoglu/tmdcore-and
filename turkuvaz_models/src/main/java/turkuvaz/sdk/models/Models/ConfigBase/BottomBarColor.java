package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BottomBarColor {
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("selected")
    @Expose
    private String selected;
    @SerializedName("unSelected")
    @Expose
    private String unSelected;

    public String getBackground() {
        return background;
    }

    public String getSelected() {
        return selected;
    }

    public String getUnSelected() {
        return unSelected;
    }
}