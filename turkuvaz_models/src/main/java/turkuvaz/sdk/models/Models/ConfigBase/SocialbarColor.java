package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialbarColor {
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("text")
    @Expose
    private String text;

    public String getBackground() {
        return background;
    }

    public String getText() {
        return text;
    }
}