package turkuvaz.sdk.models.Models.MansetSlider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

import java.util.List;

public class Articles {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Response")
    @Expose
    private List<Article> response = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Article> getResponse() {
        return response;
    }

    public void setResponse(List<Article> response) {
        this.response = response;
    }

}
