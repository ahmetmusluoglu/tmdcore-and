
package turkuvaz.sdk.models.Models.ConfigBase;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionControl {

    @SerializedName("isForce")
    @Expose
    private Boolean isForce;
    @SerializedName("currentVersionCode")
    @Expose
    private Integer currentVersionCode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("whatsnew")
    @Expose
    private ArrayList<Whatsnew> whatsnew = null;

    public Boolean getIsForce() {
        return isForce;
    }

    public void setIsForce(Boolean isForce) {
        this.isForce = isForce;
    }

    public Integer getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(Integer currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Whatsnew> getWhatsnew() {
        return whatsnew;
    }

    public void setWhatsnew(ArrayList<Whatsnew> whatsnew) {
        this.whatsnew = whatsnew;
    }

}
