
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import turkuvaz.sdk.models.Models.Enums.MenuActionType;

import static turkuvaz.sdk.models.Models.Enums.MenuActionType.ANOTHER_APP;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CATEGORY_NEWS;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CATEGORY_PHOTO_GALLERY;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CATEGORY_PROGRAMS;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CATEGORY_PROGRAMS_SINGLE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CATEGORY_TODAY;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CATEGORY_VIDEO;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.COLUMNIST_LIST;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CONTACT_US;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.CUSTOM_TV_SERIES;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.EXTERNAL;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.HOME_PAGE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.INTERNAL_BROWSER;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.LIST_TV_SERIES;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.PODCAST;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.READ_CONFIG;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.SETTINGS_NOTIFICATION;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_GIZLILIK_BILDIRIMI;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_KUNYE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_PAGE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_VERI_POLITIKASI;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STREAMING;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.SUB_PODCAST;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.TODAY_TAKVIM;

public class SimpleMenu {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("apiUrl")
    @Expose
    private String apiUrl;
    @SerializedName("imgPath")
    @Expose
    private String imgPath;
    @SerializedName("ExternalUrl")
    @Expose
    private String externalUrl;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
