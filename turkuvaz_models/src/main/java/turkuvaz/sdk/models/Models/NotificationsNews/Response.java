
package turkuvaz.sdk.models.Models.NotificationsNews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import turkuvaz.sdk.models.Models.Enums.ExternalTypes;

import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ALBUM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.ARTICLE_SOURCE;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EMPTY;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.HTTPS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.HTTP;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.INTERNAL;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.PHOTO_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ExternalTypes.VIDEO;

public class Response {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Type")
    @Expose
    private Integer type;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ExternalTypes getExternalType() {
        return url == null ? EMPTY :
                url.contains(ALBUM.getType()) ? ALBUM :
                        url.contains(VIDEO.getType()) ? VIDEO :
                                url.contains(NEWS.getType()) ? NEWS :
                                        url.contains(PHOTO_NEWS.getType()) ? PHOTO_NEWS :
                                                url.contains(ARTICLE_SOURCE.getType()) ? ARTICLE_SOURCE :
                                                        url.contains(LIVE_STREAM.getType()) ? LIVE_STREAM :
                                                                url.contains(EXTERNAL.getType()) ? EXTERNAL :
                                                                        url.contains(HTTPS.getType()) ? HTTPS :
                                                                                url.contains(HTTP.getType()) ? HTTP : // a news te http geliyordu
                                                                                    url.contains(INTERNAL.getType()) ? INTERNAL : EMPTY;
    }

}
