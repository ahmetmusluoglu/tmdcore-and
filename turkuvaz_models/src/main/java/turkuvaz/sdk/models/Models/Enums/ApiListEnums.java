package turkuvaz.sdk.models.Models.Enums;

import android.content.Context;

import turkuvaz.sdk.models.Models.Preferences;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public enum ApiListEnums {
    DEVICE_PUSH_TOKEN("pushToken"),

    ANDROID_CONFIG_URL("androidConfigUrl"),
    SHARED_CONFIG_URL("sharedConfigUrl"),
    GOOGLE_DFP_URL("apiGoogleDfpUrl"),

    PRIVACY_POLICY_TYPE("privacyPolicyType"),
    GDPR_IS_ACTIVE("gdprIsActive"),
    KVKK_IS_ACTIVE("kvkkIsActive"),
    PRIVACY_POLICY_URL("privacyPolicyUrl"),
    PRIVACY_POLICY_TITLE("privacyPolicyTitle"),
    PRIVACY_POLICY_BTN_TEXT("privacyPolicyBtnText"),

    SETTINGS_MENU_JSON("optionMenu"),
    LEFT_MENU_JSON("leftMenu"),
    TOOLBAR_MENU_JSON("toolbarMenu"),
    RATE_THIS_APP("rateThisApp"),

    CUSTOM_SETTINGS_MENU_JSON("custom_optionMenu"),
    CUSTOM_LEFT_MENU_JSON("custom_leftMenu"),
    CUSTOM_TOOLBAR_MENU_JSON("custom_toolbarMenu"),

    HOME_SCREEN_COMPONENT_JSON("homeScreen"),
    CATEGORY_VIDEO_COMPONENT_JSON("categoryVideo"),
    WIDGET_TOOLS_COMPONENT_JSON("widgetTools"),
    CATEGORY_GALLERY_COMPONENT_JSON("categoryGallery"),
    CATEGORY_NEWS_COMPONENT_JSON("categoryNews"),
    CATEGORY_NEWS_SPORT_COMPONENT_JSON("categoryNewsSport"),
    CATEGORY_TODAY_COMPONENT_JSON("categoryToday"),
    STATIC_KUNYE_JSON("staticKunye"),
    STATIC_VERI_POLITIKASI_JSON("staticVeriPolitikasi"),
    STATIC_GIZLILIK_BILDIRIMI_JSON("staticGizlilikBildirimi"),
    STATIC_ABOUT_US_JSON("staticAboutUsJson"),
    STATIC_CONTACT_JSON("staticContactJson"),
    CATEGORY_SETTINGS("settings"),
    INTRO_PAGES_COMPONENT_JSON("introPages"),
    IS_SLIDER("isSlider"),
    ACTIONBAR_HEIGHT("actionBarHeight"),

    CUSTOM_HOME_SCREEN_COMPONENT_JSON("custom_homeScreen"),
    CUSTOM_CATEGORY_VIDEO_COMPONENT_JSON("custom_categoryVideo"),
    CUSTOM_WIDGET_TOOLS_COMPONENT_JSON("custom_widgetTools"),
    CUSTOM_CATEGORY_GALLERY_COMPONENT_JSON("custom_categoryGallery"),
    CUSTOM_CATEGORY_NEWS_COMPONENT_JSON("custom_categoryNews"),
    CUSTOM_CATEGORY_NEWS_SPORT_COMPONENT_JSON("custom_categoryNewsSport"),
    CUSTOM_CATEGORY_TODAY_COMPONENT_JSON("custom_categoryToday"),
    CUSTOM_CATEGORY_SETTINGS("custom_settings"),
    CUSTOM_STATIC_KUNYE_JSON("custom_staticKunye"),
    CUSTOM_STATIC_VERI_POLITIKASI_JSON("custom_staticVeriPolitikasi"),
    CUSTOM_STATIC_GIZLILIK_BILDIRIMI_JSON("custom_staticGizlilikBildirimi"),
    CUSTOM_IS_SLIDER("custom_isSlider"),

    SETTINGS_STICKY_IS_ACTIVE("stickIsAcive"),
    SETTINGS_GEMIUS_IS_ACTIVE("gemiusIsAcive"),
    SETTINGS_TIAK_IS_ACTIVE("tiakIsAcive"),
    SETTINGS_FIREBASE_IS_ACTIVE("firebaseIsAcive"),
    SETTINGS_NATIVE_PLAYER_IS_ACTIVE("native_player"),
    SETTINGS_PRE_ROLL_ADS_IS_ACTIVE("pre_roll_ads"),

    SETTINGS_GEMIUS_KEY("gemiusKey"),

    INSTAGRAM_URL("instagramUrl"),
    FACEBOOK_URL("facebookUrl"),
    TWITTER_URL("twitterUrl"),
    YOUTUBE_URL("youtubeUrl"),
    YOUTUBE_USER_URL("youtubeUserUrl"),

    CUSTOM_INSTAGRAM_URL("custom_instagramUrl"),
    CUSTOM_FACEBOOK_URL("custom_facebookUrl"),
    CUSTOM_TWITTER_URL("custom_twitterUrl"),
    CUSTOM_YOUTUBE_URL("custom_youtubeUrl"),
    CUSTOM_YOUTUBE_USER_URL("custom_youtubeUserUrl"),

    LIVE_STREAM_URL("liveStreamUrl"),
    LIVE_STREAMS("liveStreams"),

    CONTENT_CSS("contentCss"),
    CONTENT_CSS_VERSION("contentCssVersion"),

    // UYGULAMA BAZLI RENKLER
    TOOLBAR_BACK("toolbar_back"),
    TOOLBAR_ICON("toolbar_icon"),
    LEFT_MENU_BACK("left_menu_back"),
    LEFT_MENU_TOP_BACK("left_menu_top_back"),
    LEFT_MENU_TOP_ICON("left_menu_top_icon"),
    LEFT_MENU_LIST_BACK("left_menu_list_back"),
    LEFT_MENU_LIST_ICON("left_menu_list_icon"),
    LEFT_MENU_LIST_DETAIL_BACK("left_menu_list_detail_back"),
    LEFT_MENU_LIST_DETAIL_ICON("left_menu_list_detail_icon"),
    LEFT_MENU_DIVIDER("left_menu_divider"),
    LEFT_MENU_CHILD_DIVIDER("left_menu_child_divider"),
    LAST_MINUTE_BACK_LEFT("last_minute_back_left"),
    LAST_MINUTE_BACK_RIGHT("last_minute_back_right"),
    LAST_MINUTE_TEXT_LEFT("last_minute_text_left"),
    LAST_MINUTE_TEXT_RIGHT("last_minute_text_right"),
    STATUS_BAR_CITY_BACK("status_bar_city_back"),
    STATUS_BAR_CITY_TEXT("status_bar_city_text"),
    STATUS_BAR_ICON("status_bar_icon"),
    STATUS_BAR_WIDGET_BACK_CLOSE("status_bar_widget_back_close"),
    STATUS_BAR_WIDGET_BACK_OPEN("status_bar_widget_back_open"),
    STATUS_BAR_WIDGET_TEXT_TITLE("status_bar_widget_text_title"),
    STATUS_BAR_WIDGET_TEXT_VALUE("status_bar_widget_text_value"),
    BOTTOM_BAR_BACK("bottom_bar_back"),
    BOTTOM_BAR_SELECTED("bottom_bar_selected"),
    BOTTOM_BAR_UNSELECTED("bottom_bar_unselected"),
    SPLASH_BACK("main_back"),
    SPLASH_TEXT("splash_text"),
    SOCIAL_BAR_BACK("social_bar_back"),
    SOCIAL_BAR_ICON("social_bar_icon"),
    COLUMNIST_BUTTON_EDGE("columnist_button_edge"),
    COLUMNIST_BUTTON_BACK("columnist_button_back"),
    COLUMNIST_BUTTON_TEXT("columnist_button_text"),
    COLUMNIST_PROFIL_FRAME("columnist_profil_frame"),
    COLUMNIST_BOTTOM_LINE("columnist_bottom_line"),
    TV_STREAM_BACK("tv_stream_back"),
    TV_STREAM_TEXT("tv_stream_text"),
    TV_STREAM_BUTTON_BACK("tv_stream_button_back"),
    TV_STREAM_BUTTON_TEXT("tv_stream_button_text"),

    // SAYFA BAZLI RENKLER
    CUSTOM_TOOLBAR_BACK("custom_toolbar_back"),
    CUSTOM_TOOLBAR_ICON("custom_toolbar_icon"),
    CUSTOM_LEFT_MENU_BACK("custom_left_menu_back"),
    CUSTOM_LEFT_MENU_TOP_BACK("custom_left_menu_top_back"),
    CUSTOM_LEFT_MENU_TOP_ICON("custom_left_menu_top_icon"),
    CUSTOM_LEFT_MENU_LIST_BACK("custom_left_menu_list_back"),
    CUSTOM_LEFT_MENU_LIST_ICON("custom_left_menu_list_icon"),
    CUSTOM_LEFT_MENU_LIST_DETAIL_BACK("custom_left_menu_list_detail_back"),
    CUSTOM_LEFT_MENU_LIST_DETAIL_ICON("custom_left_menu_list_detail_icon"),
    CUSTOM_LEFT_MENU_DIVIDER("custom_left_menu_divider"),
    CUSTOM_LEFT_MENU_CHILD_DIVIDER("custom_left_menu_child_divider"),
    CUSTOM_STATUS_BAR_CITY_BACK("custom_status_bar_city_back"),
    CUSTOM_STATUS_BAR_CITY_TEXT("custom_status_bar_city_text"),
    CUSTOM_STATUS_BAR_ICON("custom_status_bar_icon"),
    CUSTOM_STATUS_BAR_WIDGET_BACK_CLOSE("custom_status_bar_widget_back_close"),
    CUSTOM_STATUS_BAR_WIDGET_BACK_OPEN("custom_status_bar_widget_back_open"),
    CUSTOM_STATUS_BAR_WIDGET_TEXT_TITLE("custom_status_bar_widget_text_title"),
    CUSTOM_STATUS_BAR_WIDGET_TEXT_VALUE("custom_status_bar_widget_text_value"),
    CUSTOM_BOTTOM_BAR_BACK("custom_bottom_bar_back"),
    CUSTOM_BOTTOM_BAR_SELECTED("custom_bottom_bar_selected"),
    CUSTOM_BOTTOM_BAR_UNSELECTED("custom_bottom_bar_unselected"),
    CUSTOM_SOCIAL_BAR_BACK("custom_social_bar_back"),
    CUSTOM_SOCIAL_BAR_ICON("custom_social_bar_icon"),
    CUSTOM_COLUMNIST_BUTTON_EDGE("custom_columnist_button_edge"),
    CUSTOM_COLUMNIST_BUTTON_BACK("custom_columnist_button_back"),
    CUSTOM_COLUMNIST_BUTTON_TEXT("custom_columnist_button_text"),
    CUSTOM_COLUMNIST_PROFIL_FRAME("custom_columnist_profil_frame"),
    CUSTOM_COLUMNIST_BOTTOM_LINE("custom_columnist_bottom_line"),

    THEME("app_theme"),

    ADS_ALL_ZONE("allAdsZone"),

    ADS_HOMEPAGE_320x142("homepage_320x142"),
    ADS_HOMEPAGE_300x250("homepage_300x250"),
    ADS_HOMEPAGE_320x50("homepage_320x50"),
    ADS_GENERAL_320x142("general_320x142"),
    ADS_GENERAL_300x250("general_300x250"),
    ADS_GENERAL_320x50("general_320x50"),
    ADS_PREROLL("general_preroll"),
    ADS_PAGE_OVERLAY("app_page_overlay"),
    ADS_PAGE_OVERLAY_OTHERS("app_page_overlay"),

    ARTICLES_BY_DAILY_TAKVIM("listArticlesDailyTakvim"),
    ARTICLES_LAST_MINUTE("articlesLastMinute"),
    ARTICLES_HEADLINE("articlesHeadlines"),
    ARTICLES_SNAP_NEWS("articlesSnapbar"),
    COLUMNIST("columnists"),
    VIDEO_SLIDER("videoSlider"),
    ARTICLES_HEADLINE_PART("articlesMansetlerDevam"),
    GALLERY_SLIDER("gallerySlider"),
    ARTICLES_INFINITY_NEWS("articlesInfiniytList"),
    DETAILS_GALLERY("detailsGallery"),
    DETAILS_VIDEO("detailsVideo"),
    DETAILS_ARTICLE("detailsArticle"),
    DETAILS_COLUMNIST("detailsColumnist"),
    ALL_CATEGORIES("listAllCategories"),
    ALL_AUTHORS("listAllAuthors"),
    ARTICLE_BY_CATEGORY("listArtcilesByCategory"),
    VIDEO_BY_CATEGORY("listVideosByCategory"),
    LIST_PROGRAMS("listPrograms"),
    LIST_PROGRAMS_BY_CATEGORY("listProgramsByCategory"),
    GALLERIES_BY_CATEGORY("listGalleriesByCategory"),
    LIST_NOTIFICATION("listNotifications"),
    LIST_STREAMING("listStreaming"),
    COLUMNIST_BY_SOURCE("listColumnistsBySource"),
    DAILY_NEWS_PAPER("listDailyNewsPaper"),
    ACTUAL_TV_SERIES("listActualTvSeries"),
    CLASSIC_TV_SERIES("listClassicTvSeries"),
    ACTUAL_TV_SERIES_FRAGMAN("listActualTvSeriesFragman"),
    CLASSIC_TV_SERIES_FRAGMAN("listClassicTvSeriesFragman"),
    CUSTOM_TV_SERIES_BOLUM("listCustomTvSeriesBolum"),
    CUSTOM_TV_SERIES_FRAGMAN("listCustomTvSeriesFragman"),
    CUSTOM_TV_SERIES_OZEL("listCustomTvSeriesOzel"),
    ARTICLE_KUNYE("staticArticleKunye"),
    ARTICLE_PRIVACY_POLICY("staticArticleGizlilik");

    private String type;

    ApiListEnums(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getApi(Context context) {
        return Preferences.getString(context, type, "");
    }
}