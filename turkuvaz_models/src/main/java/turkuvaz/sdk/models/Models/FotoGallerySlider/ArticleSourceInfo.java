package turkuvaz.sdk.models.Models.FotoGallerySlider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 6/24/2020.
 */
public class ArticleSourceInfo {

    @SerializedName("Id")
    @Expose
    private String id = null;

    @SerializedName("Source")
    @Expose
    private String source = null;

    @SerializedName("Type")
    @Expose
    private String type = null;

    @SerializedName("primaryImage")
    @Expose
    private String primaryImage = null;

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }
}
