
package turkuvaz.sdk.models.Models.StaticPages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("statikPage")
    @Expose
    private StatikPage statikPage;

    public StatikPage getStatikPage() {
        return statikPage;
    }

    public void setStatikPage(StatikPage statikPage) {
        this.statikPage = statikPage;
    }

}
