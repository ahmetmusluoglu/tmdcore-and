package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rules {

    @SerializedName("onclick")
    @Expose
    private Integer onclick;
    @SerializedName("onday")
    @Expose
    private Integer onday;

    public Integer getOnclick() {
        return onclick;
    }

    public void setOnclick(Integer onclick) {
        this.onclick = onclick;
    }

    public Integer getOnday() {
        return onday;
    }

    public void setOnday(Integer onday) {
        this.onday = onday;
    }

}