
package turkuvaz.sdk.models.Models.SingleNews;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Response")
    @Expose
    private List<turkuvaz.sdk.models.Models.GlobalModels.Article> response = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<turkuvaz.sdk.models.Models.GlobalModels.Article> getResponse() {
        return response;
    }

    public void setResponse(List<turkuvaz.sdk.models.Models.GlobalModels.Article> response) {
        this.response = response;
    }

}
