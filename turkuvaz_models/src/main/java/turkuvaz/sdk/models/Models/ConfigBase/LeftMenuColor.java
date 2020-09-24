package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeftMenuColor {
    @SerializedName("listBackground")
    @Expose
    private String listBackground;
    @SerializedName("groupBackground")
    @Expose
    private String groupBackground;
    @SerializedName("groupText")
    @Expose
    private String groupText;
    @SerializedName("childBackground")
    @Expose
    private String childBackground;
    @SerializedName("childText")
    @Expose
    private String childText;
    @SerializedName("topBackground")
    @Expose
    private String topBackground;
    @SerializedName("topText")
    @Expose
    private String topText;
    @SerializedName("divider")
    @Expose
    private String divider;
    @SerializedName("childDivider")
    @Expose
    private String childDivider;

    public String getListBackground() {
        return listBackground;
    }

    public String getGroupBackground() {
        return groupBackground;
    }

    public String getGroupText() {
        return groupText;
    }

    public String getChildBackground() {
        return childBackground;
    }

    public String getChildText() {
        return childText;
    }

    public String getTopBackground() {
        return topBackground;
    }

    public String getTopText() {
        return topText;
    }

    public String getDivider() {
        return divider;
    }

    public String getChildDivider() {
        return childDivider;
    }
}