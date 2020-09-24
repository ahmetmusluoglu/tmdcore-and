
package turkuvaz.sdk.models.Models.InfinityGalleryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("previous_url")
    @Expose
    private String previousUrl;
    @SerializedName("next_url")
    @Expose
    private Object nextUrl;

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public Object getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(Object nextUrl) {
        this.nextUrl = nextUrl;
    }

}
