
package turkuvaz.sdk.models.Models.ConfigBase;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import turkuvaz.sdk.models.Models.Intro.PageList;

public class Data {

    @SerializedName("apiAdvertorialUrl")
    @Expose
    private String apiAdvertorialUrl;
    @SerializedName("apiContentUrls")
    @Expose
    private ApiContentUrls apiContentUrls;
    @SerializedName("menu")
    @Expose
    private Menu menu;
    @SerializedName("homeScreen")
    @Expose
    private ArrayList<ComponentModel> homeScreen = null;
    @SerializedName("categoryGallery")
    @Expose
    private ArrayList<ComponentModel> categoryGallery = null;
    @SerializedName("categoryVideo")
    @Expose
    private ArrayList<ComponentModel> categoryVideo = null;
    @SerializedName("categoryNews")
    @Expose
    private ArrayList<ComponentModel> categoryNews = null;
    @SerializedName("categoryNewsSport")
    @Expose
    private ArrayList<ComponentModel> categoryNewsSport = null;
    @SerializedName("versionControl")
    @Expose
    private VersionControl versionControl;
    @SerializedName("rateThisApp")
    @Expose
    private RateThisApp rateThisApp;
    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("categoryToday")
    @Expose
    private ArrayList<ComponentModel> categoryToday;
    @SerializedName("introPages")
    @Expose
    private PageList pageList;
    @SerializedName("widgetTools")
    @Expose
    private ComponentModel widgetTools = null;
    @SerializedName("staticKunye")
    @Expose
    private ArrayList<ComponentModel> staticKunye = null;
    @SerializedName("staticVeriPolitikasi")
    @Expose
    private ArrayList<ComponentModel> staticVeriPolitikasi = null;
    @SerializedName("staticGizlilikBildirimi")
    @Expose
    private ArrayList<ComponentModel> staticGizlilikBildirimi = null;

    public String getApiAdvertorialUrl() {
        return apiAdvertorialUrl;
    }

    public void setApiAdvertorialUrl(String apiAdvertorialUrl) {
        this.apiAdvertorialUrl = apiAdvertorialUrl;
    }

    public ApiContentUrls getApiContentUrls() {
        return apiContentUrls;
    }

    public void setApiContentUrls(ApiContentUrls apiContentUrls) {
        this.apiContentUrls = apiContentUrls;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public ArrayList<ComponentModel> getHomeScreen() {
        return homeScreen;
    }

    public void setHomeScreen(ArrayList<ComponentModel> homeScreen) {
        this.homeScreen = homeScreen;
    }

    public VersionControl getVersionControl() {
        return versionControl;
    }

    public void setVersionControl(VersionControl versionControl) {
        this.versionControl = versionControl;
    }

    public RateThisApp getRateThisApp() {
        return rateThisApp;
    }

    public void setRateThisApp(RateThisApp rateThisApp) {
        this.rateThisApp = rateThisApp;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ArrayList<ComponentModel> getCategoryGallery() {
        return categoryGallery;
    }

    public void setCategoryGallery(ArrayList<ComponentModel> categoryGallery) {
        this.categoryGallery = categoryGallery;
    }

    public ArrayList<ComponentModel> getCategoryVideo() {
        return categoryVideo;
    }

    public void setCategoryVideo(ArrayList<ComponentModel> categoryVideo) {
        this.categoryVideo = categoryVideo;
    }

    public ArrayList<ComponentModel> getCategoryNews() {
        return categoryNews;
    }

    public void setCategoryNews(ArrayList<ComponentModel> categoryNews) {
        this.categoryNews = categoryNews;
    }

    public ArrayList<ComponentModel> getCategoryToday() {
        return categoryToday;
    }

    public void setCategoryToday(ArrayList<ComponentModel> categoryToday) {
        this.categoryToday = categoryToday;
    }

    public PageList getPageList() {
        return pageList;
    }

    public void setPageList(PageList pageList) {
        this.pageList = pageList;
    }

    public ComponentModel getWidgetTools() {
        return widgetTools;
    }

    public void setWidgetTools(ComponentModel widgetTools) {
        this.widgetTools = widgetTools;
    }

    public ArrayList<ComponentModel> getStaticKunye() {
        return staticKunye;
    }

    public ArrayList<ComponentModel> getStaticVeriPolitikasi() {
        return staticVeriPolitikasi;
    }

    public ArrayList<ComponentModel> getStaticGizlilikBildirimi() {
        return staticGizlilikBildirimi;
    }

    public ArrayList<ComponentModel> getCategoryNewsSport() {
        return categoryNewsSport;
    }
}
