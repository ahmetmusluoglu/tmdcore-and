package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public enum ComponentType {
    COM_BANNER_ADS("comDfpBannerAds"),
    COM_SLIDE_MENU("comSlideMenu"),
    COM_LAST_MINUTE("comSondakika"),
    COM_HEADLINE("comHeadlines"),
    COM_VIEWPAGER("comViewPager"),
    COM_INFORMATION_BAR("comboxInformationBar"),
    COM_SNAP_BAR("comSnapbar"),
    COM_COLUMNIST_SLIDER("comColumnists"),
    COM_VIDEO_SLIDER("comVideoSlider"),
    COM_GRID_NEWS("comGridNews"),
    COM_DESCRIPTION("comDescription"),
    COM_GALLERY_SLIDER("comGallerySlider"),
    COM_SPECIAL_CONTENT("comSpecialContent"),
    COM_INTERNAL_BROWSER("comInternalBrowser"),
    COM_BTN_NEWS_LIVESTREAM("comBtnNewsLiveStream"),
    COM_ADVERTORIAL("comAdvertorial"),
    COM_TV_STREAM("comTvStream"),
    COM_UNKNOWN("None");

    private String type;

    ComponentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
