package turkuvaz.sdk.models.Models.podcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class PodcastResponse {
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("PodcastUrl")
    @Expose
    private String podcastUrl;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("ShareUrl")
    @Expose
    private String shareUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPodcastUrl() {
        return podcastUrl;
    }

    public void setPodcastUrl(String podcastUrl) {
        this.podcastUrl = podcastUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
