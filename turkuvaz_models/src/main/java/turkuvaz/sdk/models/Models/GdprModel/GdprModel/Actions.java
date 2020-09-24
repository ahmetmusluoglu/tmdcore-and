
package turkuvaz.sdk.models.Models.GdprModel.GdprModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Actions {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("elements")
    @Expose
    private Elements elements;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
        this.elements = elements;
    }

}
