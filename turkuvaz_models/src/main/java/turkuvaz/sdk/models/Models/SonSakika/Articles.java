
package turkuvaz.sdk.models.Models.SonSakika;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import turkuvaz.sdk.models.Models.GlobalModels.Article;

public class Articles {

    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Response")
    @Expose
    private ArrayList<Article> response = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ArrayList<Article> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Article> response) {
        this.response = response;
    }

}
