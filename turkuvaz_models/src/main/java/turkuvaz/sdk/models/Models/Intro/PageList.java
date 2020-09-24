package turkuvaz.sdk.models.Models.Intro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmet MUŞLUOĞLU on 3/13/2020.
 */
public class PageList {

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("pages")
    @Expose
    private List<Page> pages = null;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
