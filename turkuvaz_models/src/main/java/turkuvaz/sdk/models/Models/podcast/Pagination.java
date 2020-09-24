package turkuvaz.sdk.models.Models.podcast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class Pagination {
    @SerializedName("previous_url")
    @Expose
    private Object previousUrl;
    @SerializedName("next_url")
    @Expose
    private String nextUrl;

    public Object getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(Object previousUrl) {
        this.previousUrl = previousUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
