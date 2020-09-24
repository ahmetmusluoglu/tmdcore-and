
package turkuvaz.sdk.models.Models.AllAuthors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("primaryImage")
    @Expose
    private String primaryImage;
    @SerializedName("FacebookUrl")
    @Expose
    private Object facebookUrl;
    @SerializedName("TwitterWidgetId")
    @Expose
    private Object twitterWidgetId;
    @SerializedName("TwitterUrl")
    @Expose
    private Object twitterUrl;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("EMail")
    @Expose
    private Object eMail;
    @SerializedName("SourceForUrl")
    @Expose
    private String sourceForUrl;

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Object getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Object getTwitterWidgetId() {
        return twitterWidgetId;
    }

    public void setTwitterWidgetId(Object twitterWidgetId) {
        this.twitterWidgetId = twitterWidgetId;
    }

    public Object getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(Object twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getEMail() {
        return eMail;
    }

    public void setEMail(Object eMail) {
        this.eMail = eMail;
    }

    public String getSourceForUrl() {
        return sourceForUrl;
    }

    public void setSourceForUrl(String sourceForUrl) {
        this.sourceForUrl = sourceForUrl;
    }

}
