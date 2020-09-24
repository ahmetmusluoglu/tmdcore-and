
package turkuvaz.sdk.models.Models.PhotoGalleryList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("galleries")
    @Expose
    private Galleries galleries;

    public Galleries getGalleries() {
        return galleries;
    }

    public void setGalleries(Galleries galleries) {
        this.galleries = galleries;
    }

}
