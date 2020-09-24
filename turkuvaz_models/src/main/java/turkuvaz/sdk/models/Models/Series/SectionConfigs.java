
package turkuvaz.sdk.models.Models.Series;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionConfigs {

    @SerializedName("DoubleClick1")
    @Expose
    private String doubleClick1;
    @SerializedName("DoubleClick2")
    @Expose
    private String doubleClick2;

    public String getDoubleClick1() {
        return doubleClick1;
    }

    public void setDoubleClick1(String doubleClick1) {
        this.doubleClick1 = doubleClick1;
    }

    public String getDoubleClick2() {
        return doubleClick2;
    }

    public void setDoubleClick2(String doubleClick2) {
        this.doubleClick2 = doubleClick2;
    }

}
