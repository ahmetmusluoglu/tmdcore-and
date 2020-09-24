package turkuvaz.sdk.models.Models.podcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/24/2020.
 */
public class Data {
    @SerializedName("list")
    @Expose
    private PodcastList podcastList;

    public PodcastList getPodcastList() {
        return podcastList;
    }

    public void setPodcastList(PodcastList podcastList) {
        this.podcastList = podcastList;
    }
}
