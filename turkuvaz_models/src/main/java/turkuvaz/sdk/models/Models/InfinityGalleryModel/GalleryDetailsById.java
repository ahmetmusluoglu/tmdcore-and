
package turkuvaz.sdk.models.Models.InfinityGalleryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GalleryDetailsById {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Response")
    @Expose
    private java.util.List<Response> response = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public java.util.List<Response> getResponse() {
        return response;
    }

    public void setResponse(java.util.List<Response> response) {
        this.response = response;
    }

}
