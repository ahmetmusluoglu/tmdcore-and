
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import turkuvaz.sdk.models.Models.Enums.ComponentType;
import turkuvaz.sdk.models.Models.Enums.NewsListType;

import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_BANNER_ADS;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_BTN_NEWS_LIVESTREAM;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_INTERNAL_BROWSER;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_VIEWPAGER;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_COLUMNIST_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_DESCRIPTION;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_GALLERY_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_GRID_NEWS;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_HEADLINE;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_INFORMATION_BAR;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_LAST_MINUTE;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_SLIDE_MENU;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_SNAP_BAR;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_SPECIAL_CONTENT;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_ADVERTORIAL;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_TV_STREAM;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_UNKNOWN;
import static turkuvaz.sdk.models.Models.Enums.ComponentType.COM_VIDEO_SLIDER;
import static turkuvaz.sdk.models.Models.Enums.NewsListType.GRID_1X1_ROUNDED_BOX;
import static turkuvaz.sdk.models.Models.Enums.NewsListType.GRID_1x1_BOX;
import static turkuvaz.sdk.models.Models.Enums.NewsListType.GRID_1x1_LIST;
import static turkuvaz.sdk.models.Models.Enums.NewsListType.GRID_2x2_BOX;
import static turkuvaz.sdk.models.Models.Enums.NewsListType.GRID_UNKNOWN;

public class ComponentModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("apiPath")
    @Expose
    private String apiPath;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("options")
    @Expose
    private Options options;
    @SerializedName("view")
    @Expose
    private String view;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("textSize")
    @Expose
    private String textSize;
    @SerializedName("autoSlide")
    @Expose
    private Boolean autoSlide;
    @SerializedName("dateShow")
    @Expose
    private Boolean dateShow;
    @SerializedName("isTitle")
    @Expose
    private Boolean isTitle;
    @SerializedName("isTitleOverImage")
    @Expose
    private Boolean isTitleOverImage;
    @SerializedName("isCategory")
    @Expose
    private Boolean isCategory;
    @SerializedName("isSurmanset")
    @Expose
    private Boolean isSurmanset;
    @SerializedName("isSurmansetBaslik")
    @Expose
    private Boolean isSurmansetBaslik;
    @SerializedName("isSingleType")
    @Expose
    private Boolean isSingleType;
    @SerializedName("numberOfLine")
    @Expose
    private int numberOfLine;
    @SerializedName("isDarkMode")
    @Expose
    private Boolean isDarkMode;
    @SerializedName("list")
    @Expose
    private String list;
    @SerializedName("selectedIndex")
    @Expose
    private int selectedIndex;
    @SerializedName("isSlider")
    @Expose
    private Boolean isSlider;
    @SerializedName("takeCountHeadline")
    @Expose
    private int takeCountHeadline;
    @SerializedName("takeCountCategory")
    @Expose
    private int takeCountCategory;
    @SerializedName("isSingleCell")
    @Expose
    private Boolean isSingleCell;
    @SerializedName("customPageName")
    @Expose
    private String customPageName;
    @SerializedName("topBarItems")
    @Expose
    private List<TopBarNavigation> topBarItems = null;
    @SerializedName("subActions")
    @Expose
    private List<SimpleMenu> subActions = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getText() {
        return text;
    }

    public String getTextSize() {
        return textSize;
    }

    public Boolean getAutoSlide() {
        return autoSlide;
    }

    public void setAutoSlide(Boolean autoSlide) {
        this.autoSlide = autoSlide;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDateShow() {
        return dateShow;
    }

    public void setDateShow(Boolean dateShow) {
        this.dateShow = dateShow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleShow(Boolean title) {
        isTitle = title;
    }

    public Boolean getTitleShow() {
        return isTitle;
    }

    public Boolean getTitleOverImage() {
        return isTitleOverImage;
    }

    public void setTitleOverImage(Boolean titleOverImage) {
        isTitleOverImage = titleOverImage;
    }

    public Boolean getCategory() {
        return isCategory;
    }

    public void setCategory(Boolean category) {
        isCategory = category;
    }

    public Boolean getSurmanset() {
        return isSurmanset;
    }

    public void setSurmanset(Boolean surmanset) {
        isSurmanset = surmanset;
    }

    public Boolean getSurmansetBaslik() {
        return isSurmansetBaslik;
    }

    public void setSurmansetBaslik(Boolean surmansetBaslik) {
        isSurmansetBaslik = surmansetBaslik;
    }

    public Boolean getSingleType() {
        return isSingleType;
    }

    public void setSingleType(Boolean singleType) {
        isSingleType = singleType;
    }

    public int getNumberOfLine() {
        return numberOfLine;
    }

    public void setNumberOfLine(int numberOfLine) {
        this.numberOfLine = numberOfLine;
    }

    public Boolean isDarkMode() {
        return isDarkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        isDarkMode = darkMode;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Boolean isSlider() {
        return isSlider;
    }

    public int getTakeCountHeadline() {
        return takeCountHeadline;
    }

    public int getTakeCountCategory() {
        return takeCountCategory;
    }

    public Boolean getSingleCell() {
        return isSingleCell;
    }

    public String getCustomPageName() {
        return customPageName;
    }

    public List<TopBarNavigation> getTopBarItems() {
        return topBarItems;
    }

    public List<SimpleMenu> getSubActions() {
        return subActions;
    }

    public ComponentType getComponentType() {
        return name == null ? null :
                name.contains(COM_BANNER_ADS.getType()) ? COM_BANNER_ADS :
                        name.contains(COM_COLUMNIST_SLIDER.getType()) ? COM_COLUMNIST_SLIDER :
                                name.contains(COM_GALLERY_SLIDER.getType()) ? COM_GALLERY_SLIDER :
                                        name.contains(COM_GRID_NEWS.getType()) ? COM_GRID_NEWS :
                                                name.contains(COM_HEADLINE.getType()) ? COM_HEADLINE :
                                                        name.contains(COM_SLIDE_MENU.getType()) ? COM_SLIDE_MENU :
                                                                name.contains(COM_INFORMATION_BAR.getType()) ? COM_INFORMATION_BAR :
                                                                        name.contains(COM_LAST_MINUTE.getType()) ? COM_LAST_MINUTE :
                                                                                name.contains(COM_SNAP_BAR.getType()) ? COM_SNAP_BAR :
                                                                                        name.contains(COM_VIDEO_SLIDER.getType()) ? COM_VIDEO_SLIDER :
                                                                                                name.contains(COM_TV_STREAM.getType()) ? COM_TV_STREAM :
                                                                                                        name.contains(COM_BTN_NEWS_LIVESTREAM.getType()) ? COM_BTN_NEWS_LIVESTREAM :
                                                                                                                name.contains(COM_ADVERTORIAL.getType()) ? COM_ADVERTORIAL :
                                                                                                                        name.contains(COM_DESCRIPTION.getType()) ? COM_DESCRIPTION :
                                                                                                                                name.contains(COM_VIEWPAGER.getType()) ? COM_VIEWPAGER :
                                                                                                                                        name.contains(COM_INTERNAL_BROWSER.getType()) ? COM_INTERNAL_BROWSER :
                                                                                                                                                name.contains(COM_SPECIAL_CONTENT.getType()) ? COM_SPECIAL_CONTENT : COM_UNKNOWN;


    }

    public NewsListType getNewsListType() {
        return view == null ? GRID_2x2_BOX :
                view.contains(GRID_1x1_BOX.getType()) ? GRID_1x1_BOX :
                        view.contains(GRID_1x1_LIST.getType()) ? GRID_1x1_LIST :
                            view.contains(GRID_1X1_ROUNDED_BOX.getType()) ? GRID_1X1_ROUNDED_BOX :
                                view.contains(GRID_2x2_BOX.getType()) ? GRID_2x2_BOX : GRID_UNKNOWN;


    }
}
