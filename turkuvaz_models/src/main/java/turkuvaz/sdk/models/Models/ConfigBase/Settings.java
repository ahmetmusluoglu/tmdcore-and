
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {
    @SerializedName("socialMedia")
    @Expose
    private SocialMedia socialMedia;
    @SerializedName("stats")
    @Expose
    private Stats stats;
    @SerializedName("advertisement")
    @Expose
    private Advertisement advertisement;
    @SerializedName("others")
    @Expose
    private Others others;
    @SerializedName("colors")
    @Expose
    private Colors colors;
    @SerializedName("video")
    @Expose
    private Video video;

    public SocialMedia getSocialMedia() {
        return socialMedia;
    }

    public Stats getStats() {
        return stats;
    }

    public Others getOthers() {
        return others;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public Colors getColors() {
        return colors;
    }

    public Video getVideo() {
        return video;
    }
}
