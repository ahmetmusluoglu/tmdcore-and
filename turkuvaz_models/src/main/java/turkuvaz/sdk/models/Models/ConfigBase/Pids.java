
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pids {

    @SerializedName("phone")
    @Expose
    private Integer phone;
    @SerializedName("tablet")
    @Expose
    private Integer tablet;

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getTablet() {
        return tablet;
    }

    public void setTablet(Integer tablet) {
        this.tablet = tablet;
    }

}
