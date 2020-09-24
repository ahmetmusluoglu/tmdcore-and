package turkuvaz.sdk.models.Models.podcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class PodcastData {
    @SerializedName("list")
    @Expose
    private PodCast podCast;

    public PodCast getPodCast() {
        return podCast;
    }

    public void setPodCast(PodCast podCast) {
        this.podCast = podCast;
    }
}
