
package turkuvaz.sdk.models.Models.InfinityGalleryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("gallery")
    @Expose
    private GalleryDetailsById galleryDetailsById;

    public GalleryDetailsById getGalleryDetailsById() {
        return galleryDetailsById;
    }

    public void setGalleryDetailsById(GalleryDetailsById galleryDetailsById) {
        this.galleryDetailsById = galleryDetailsById;
    }

}
