package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Active {
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public Boolean isActive() {
        return isActive;
    }
}