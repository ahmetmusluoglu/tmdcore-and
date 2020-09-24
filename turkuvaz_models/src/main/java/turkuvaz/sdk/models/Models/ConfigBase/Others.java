
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Others {

    @SerializedName(value = "deeplink")
    @Expose
    private Deeplink deeplink;

    @SerializedName("gallerySlider")
    @Expose
    private GallerySlider gallerySlider;

    public GallerySlider getGallerySlider() {
        return gallerySlider;
    }

    public Deeplink getDeeplink() {
        return deeplink;
    }

    public class Deeplink extends Active {
    }

    public class GallerySlider extends Active {
        @SerializedName("maxDots")
        @Expose
        private int maxDots;

        public int getMaxDots() {
            return maxDots;
        }

        @SerializedName("isShowArrows")
        @Expose
        private Boolean isShowArrows;

        public Boolean isShowArrows() {
            return isShowArrows;
        }
    }
}
