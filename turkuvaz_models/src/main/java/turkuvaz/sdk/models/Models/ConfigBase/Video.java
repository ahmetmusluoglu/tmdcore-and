package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 6/28/2020.
 */
public class Video {

    @SerializedName(value = "nativeplayer")
    @Expose
    private NativePlayer nativeplayer;

    @SerializedName(value = "prerollads")
    @Expose
    private PreRollAds preRollAds;

    public NativePlayer getNativePlayer() {
        return nativeplayer;
    }

    public PreRollAds getPreRollAds() {
        return preRollAds;
    }

    public class NativePlayer extends Active {

    }

    public class PreRollAds extends Active {
    }
}
