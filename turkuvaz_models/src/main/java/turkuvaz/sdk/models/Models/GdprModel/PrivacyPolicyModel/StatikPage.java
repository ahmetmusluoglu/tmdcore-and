
package turkuvaz.sdk.models.Models.GdprModel.PrivacyPolicyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatikPage {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Response")
    @Expose
    private Response response;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
