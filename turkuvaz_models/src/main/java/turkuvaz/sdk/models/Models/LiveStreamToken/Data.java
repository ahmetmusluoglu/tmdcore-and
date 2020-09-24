package turkuvaz.sdk.models.Models.LiveStreamToken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("Success")
    @Expose
    private Boolean success;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("AlternateUrl")
    @Expose
    private String alternateUrl;
    @SerializedName("Time")
    @Expose
    private Integer time;
    @SerializedName("IP")
    @Expose
    private String iP;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlternateUrl() {
        return alternateUrl;
    }

    public void setAlternateUrl(String alternateUrl) {
        this.alternateUrl = alternateUrl;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getIP() {
        return iP;
    }

    public void setIP(String iP) {
        this.iP = iP;
    }

}