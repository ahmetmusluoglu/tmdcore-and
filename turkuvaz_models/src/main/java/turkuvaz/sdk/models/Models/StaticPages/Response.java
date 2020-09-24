
package turkuvaz.sdk.models.Models.StaticPages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("TitleShort")
    @Expose
    private String titleShort;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("SpotShort")
    @Expose
    private String spotShort;
    @SerializedName("Spot")
    @Expose
    private String spot;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleShort() {
        return titleShort;
    }

    public void setTitleShort(String titleShort) {
        this.titleShort = titleShort;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpotShort() {
        return spotShort;
    }

    public void setSpotShort(String spotShort) {
        this.spotShort = spotShort;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

}
