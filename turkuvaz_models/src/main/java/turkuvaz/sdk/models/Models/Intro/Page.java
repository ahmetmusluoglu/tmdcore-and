package turkuvaz.sdk.models.Models.Intro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 3/13/2020.
 */
public class Page {

    @SerializedName("path")
    @Expose
    private String imagePath;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("bgColor")
    @Expose
    private String bgColor;
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
