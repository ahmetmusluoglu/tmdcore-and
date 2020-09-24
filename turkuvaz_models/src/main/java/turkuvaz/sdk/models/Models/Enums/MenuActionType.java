package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 03/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public enum MenuActionType {
    HOME_PAGE("homepage"),
    CATEGORY_NEWS("listArticlesByCategory"),
    CATEGORY_NEWS_SPORT("categoryNewsSport"),
    COLUMNIST_LIST("listColumnistsBySource"),
    LIVE_STREAM("liveStream"),
    CATEGORY_PHOTO_GALLERY("listGalleriesByCategory"),
    CATEGORY_VIDEO("listVideosByCategory"),
    CATEGORY_PROGRAMS("listProgramsByCategory"),
    CATEGORY_PROGRAMS_SINGLE("listPrograms"),
    CONTACT_US("contactUsForm"),
    STATIC_PAGE("staticPage"),
    INTERNAL_BROWSER("apiUrl"),
    EXTERNAL("external"),
    HOME_SCREEN("homeScreen"),
    STATIC_VERI_POLITIKASI ("staticVeriPolitikasi"),
    STATIC_KUNYE("staticKunye"),
    STATIC_GIZLILIK_BILDIRIMI ("staticGizlilikBildirimi"),
    STREAMING("streaming"),
    TODAY_TAKVIM("todayTakvim"),
    LIST_TV_SERIES("listTvSeries"),
    CUSTOM_TV_SERIES("customTvSeries"),
    CATEGORY_TODAY("categoryToday"),
    PODCAST("podcast"),
    SUB_PODCAST("subPodcast"),
    ANOTHER_APP("anotherApp"),
    SETTINGS_NOTIFICATION("notificationPage"),
    READ_CONFIG("readConfig");
    private String type;

    MenuActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
