package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvStreamColor {
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("buttonBackground")
    @Expose
    private String buttonBackground;
    @SerializedName("buttonText")
    @Expose
    private String buttonText;

    public String getBackground() {
        return background;
    }

    public String getText() {
        return text;
    }

    public String getButtonBackground() {
        return buttonBackground;
    }

    public String getButtonText() {
        return buttonText;
    }
}