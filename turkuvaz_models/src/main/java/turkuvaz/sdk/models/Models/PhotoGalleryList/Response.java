
package turkuvaz.sdk.models.Models.PhotoGalleryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("ShowHeadline")
    @Expose
    private Boolean showHeadline;
    @SerializedName("ServerUrl")
    @Expose
    private Object serverUrl;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("primaryImage")
    @Expose
    private String primaryImage;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("UniqueId")
    @Expose
    private String uniqueId;
    @SerializedName("CategoryId")
    @Expose
    private String categoryId;
    @SerializedName("AlbumMediaCount")
    @Expose
    private Integer albumMediaCount;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("ModifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("OutputDate")
    @Expose
    private String outputDate;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Spot")
    @Expose
    private String spot;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getShowHeadline() {
        return showHeadline;
    }

    public void setShowHeadline(Boolean showHeadline) {
        this.showHeadline = showHeadline;
    }

    public Object getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(Object serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAlbumMediaCount() {
        return albumMediaCount;
    }

    public void setAlbumMediaCount(Integer albumMediaCount) {
        this.albumMediaCount = albumMediaCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOutputDate() {
        return outputDate;
    }

    public void setOutputDate(String outputDate) {
        this.outputDate = outputDate;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

}
