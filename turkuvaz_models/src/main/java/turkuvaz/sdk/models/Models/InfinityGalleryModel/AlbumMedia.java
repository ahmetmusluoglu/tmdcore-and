
package turkuvaz.sdk.models.Models.InfinityGalleryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumMedia {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("IsListImage")
    @Expose
    private Boolean isListImage;
    @SerializedName("SortOrder")
    @Expose
    private Integer sortOrder;
    @SerializedName("Height")
    @Expose
    private Integer height;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("HyperLink")
    @Expose
    private String hyperLink;
    @SerializedName("Width")
    @Expose
    private Integer width;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsListImage() {
        return isListImage;
    }

    public void setIsListImage(Boolean isListImage) {
        this.isListImage = isListImage;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHyperLink() {
        return hyperLink;
    }

    public void setHyperLink(String hyperLink) {
        this.hyperLink = hyperLink;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
