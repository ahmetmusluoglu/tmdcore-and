package turkuvaz.sdk.models.Models.podcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class PodCast {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Response")
    @Expose
    private ArrayList<PodcastResponse> podcastResponse = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ArrayList<PodcastResponse> getResponse() {
        return podcastResponse;
    }

    public void setResponse(ArrayList<PodcastResponse> podcastResponse) {
        this.podcastResponse = podcastResponse;
    }
}
