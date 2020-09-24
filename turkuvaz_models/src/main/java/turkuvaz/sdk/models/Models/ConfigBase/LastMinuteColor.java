package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastMinuteColor {
    @SerializedName("leftBackground")
    @Expose
    private String leftBackground;
    @SerializedName("leftText")
    @Expose
    private String leftText;
    @SerializedName("rightBackground")
    @Expose
    private String rightBackground;
    @SerializedName("rightText")
    @Expose
    private String rightText;

    public String getLeftBackground() {
        return leftBackground;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getRightBackground() {
        return rightBackground;
    }

    public String getRightText() {
        return rightText;
    }
}