package turkuvaz.sdk.models.Models.podcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/24/2020.
 */
public class Response {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("primaryImage")
    @Expose
    private String primaryImage;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("secondaryImage")
    @Expose
    private String secondaryImage;
    @SerializedName("Color")
    @Expose
    private Object color;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Spot")
    @Expose
    private Object spot;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecondaryImage() {
        return secondaryImage;
    }

    public void setSecondaryImage(String secondaryImage) {
        this.secondaryImage = secondaryImage;
    }

    public Object getColor() {
        return color;
    }

    public void setColor(Object color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getSpot() {
        return spot;
    }

    public void setSpot(Object spot) {
        this.spot = spot;
    }

}
