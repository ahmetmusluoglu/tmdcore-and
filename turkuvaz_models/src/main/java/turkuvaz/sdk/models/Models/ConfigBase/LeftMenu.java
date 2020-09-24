
package turkuvaz.sdk.models.Models.ConfigBase;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.INTERNAL_BROWSER;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.READ_CONFIG;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_GIZLILIK_BILDIRIMI;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_KUNYE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_PAGE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STATIC_VERI_POLITIKASI;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.SUB_PODCAST;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.LIST_TV_SERIES;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.LIVE_STREAM;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.PODCAST;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.SETTINGS_NOTIFICATION;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.STREAMING;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.HOME_PAGE;
import static turkuvaz.sdk.models.Models.Enums.MenuActionType.TODAY_TAKVIM;

public class LeftMenu {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("apiUrl")
    @Expose
    private String apiUrl;
    @SerializedName("menuIcon")
    @Expose
    private String menuIcon;
    @SerializedName("imgPath")
    @Expose
    private String imgPath;
    @SerializedName("ExternalUrl")
    @Expose
    private String externalUrl;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("isOpenDefault")
    @Expose
    private Boolean isOpenDefault;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("subLinks")
    @Expose
    private List<LeftMenu> subLinks = null;
    @SerializedName("subTabs")
    @Expose
    private List<LeftMenu> subTabs = null;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName(value = "cid", alternate = {"bundle"})
    @Expose
    private String cid;
    @SerializedName("subType")
    @Expose
    private String subType;
    @SerializedName("isIcon")
    @Expose
    private Boolean isIcon;
    @SerializedName("isCategoryBar")
    @Expose
    private Boolean isCategoryBar;
    @SerializedName("isTitleShow")
    @Expose
    private Boolean isTitleShow;
    @SerializedName("typeCategory")
    @Expose
    private String typeCategory;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getIsOpenDefault() {
        return isOpenDefault;
    }

    public void setIsOpenDefault(Boolean isOpenDefault) {
        this.isOpenDefault = isOpenDefault;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<LeftMenu> getSubTabs() {
        return subTabs;
    }

    public void setSubTabs(List<LeftMenu> subTabs) {
        this.subTabs = subTabs;
    }

    public List<LeftMenu> getSubLinks() {
        return subLinks;
    }

    public void setSubLinks(List<LeftMenu> subLinks) {
        this.subLinks = subLinks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
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

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Boolean isIcon() {
        return isIcon;
    }

    public void setIcon(Boolean icon) {
        isIcon = icon;
    }

    public Boolean isCategoryBar() {
        return isCategoryBar;
    }

    public void setCategoryBar(Boolean categoryBar) {
        isCategoryBar = categoryBar;
    }

    public Boolean isTitleShow() {
        return isTitleShow;
    }

    public void setTitleShow(Boolean titleShow) {
        isTitleShow = titleShow;
    }

    public String getTypeCategory() {
        return typeCategory;
    }

    public MenuActionType getMenuActionType() {
        return type == null ? null :
                type.contains(CATEGORY_NEWS.getType()) ? CATEGORY_NEWS :
                        type.contains(COLUMNIST_LIST.getType()) ? COLUMNIST_LIST :
                                type.contains(EXTERNAL.getType()) ? EXTERNAL :
                                        type.contains(LIVE_STREAM.getType()) ? LIVE_STREAM :
                                                type.contains(STREAMING.getType()) ? STREAMING :
                                                        type.contains(CATEGORY_PHOTO_GALLERY.getType()) ? CATEGORY_PHOTO_GALLERY :
                                                                type.contains(CATEGORY_VIDEO.getType()) ? CATEGORY_VIDEO :
                                                                        type.contains(CONTACT_US.getType()) ? CONTACT_US :
                                                                                type.contains(STATIC_PAGE.getType()) ? STATIC_PAGE :
                                                                                        type.contains(LIST_TV_SERIES.getType()) ? LIST_TV_SERIES :
                                                                                                type.contains(CUSTOM_TV_SERIES.getType()) ? CUSTOM_TV_SERIES :
                                                                                                        type.contains(CATEGORY_PROGRAMS.getType()) ? CATEGORY_PROGRAMS :
                                                                                                                type.contains(CATEGORY_PROGRAMS_SINGLE.getType()) ? CATEGORY_PROGRAMS_SINGLE :
                                                                                                                        type.contains(CONTACT_US.getType()) ? CONTACT_US :
                                                                                                                                type.contains(INTERNAL_BROWSER.getType()) ? INTERNAL_BROWSER :
                                                                                                                                        type.contains(HOME_PAGE.getType()) ? HOME_PAGE :
                                                                                                                                                type.contains(TODAY_TAKVIM.getType()) ? TODAY_TAKVIM :
                                                                                                                                                        type.contains(CATEGORY_TODAY.getType()) ? CATEGORY_TODAY :
                                                                                                                                                                type.contains(PODCAST.getType()) ? PODCAST :
                                                                                                                                                                        type.contains(SUB_PODCAST.getType()) ? SUB_PODCAST :
                                                                                                                                                                                type.contains(ANOTHER_APP.getType()) ? ANOTHER_APP :
                                                                                                                                                                                        type.contains(READ_CONFIG.getType()) ? READ_CONFIG :
                                                                                                                                                                                                type.contains(STATIC_KUNYE.getType()) ? STATIC_KUNYE :
                                                                                                                                                                                                        type.contains(STATIC_VERI_POLITIKASI.getType()) ? STATIC_VERI_POLITIKASI :
                                                                                                                                                                                                                type.contains(STATIC_GIZLILIK_BILDIRIMI.getType()) ? STATIC_GIZLILIK_BILDIRIMI :
                                                                                                                                                                                                                        type.contains(SUB_PODCAST.getType()) ? SUB_PODCAST :
                                                                                                                                                                                                                                type.contains(ANOTHER_APP.getType()) ? ANOTHER_APP :
                                                                                                                                                                                                                                        type.contains(SETTINGS_NOTIFICATION.getType()) ? SETTINGS_NOTIFICATION : null;
    }
}
