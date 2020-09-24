package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColumnistColor {
    @SerializedName("buttonBackground")
    @Expose
    private String buttonBackground;
    @SerializedName("buttonText")
    @Expose
    private String buttonText;
    @SerializedName("buttonEdge")
    @Expose
    private String buttonEdge;
    @SerializedName("profilFrame")
    @Expose
    private String profilFrame;
    @SerializedName("bottomLine")
    @Expose
    private String bottomLine;

    public String getButtonBackground() {
        return buttonBackground;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getButtonEdge() {
        return buttonEdge;
    }

    public String getProfilFrame() {
        return profilFrame;
    }

    public String getBottomLine() {
        return bottomLine;
    }
}