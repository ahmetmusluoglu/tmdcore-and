
package turkuvaz.sdk.models.Models.GdprModel.GdprModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Kvkk {

    @SerializedName("contentTitle")
    @Expose
    private String contentTitle;
    @SerializedName("contentText")
    @Expose
    private String contentText;
    @SerializedName("actions")
    @Expose
    private Actions actions;
    @SerializedName("countryList")
    @Expose
    private List<String> countryList = null;
    @SerializedName("responseTimeOut")
    @Expose
    private Integer responseTimeOut;

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public List<String> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<String> countryList) {
        this.countryList = countryList;
    }

    public Integer getResponseTimeOut() {
        return responseTimeOut;
    }

    public void setResponseTimeOut(Integer responseTimeOut) {
        this.responseTimeOut = responseTimeOut;
    }

}
