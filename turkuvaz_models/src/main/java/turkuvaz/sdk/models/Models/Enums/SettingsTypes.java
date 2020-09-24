package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 03/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public enum SettingsTypes {
    STATIC_PAGE("staticPage"),
    CONTACT_US("contactUsForm"),
    NOTIFICATION_TOGGLE("notificationStatus"),
    NOTIFICATION("notificationPage"),
    NEWS_WEBVIEW_FONT_SIZE("webViewFontSize"),
    NEWS_TITLE_FONT_SIZE("newsTitleFontSize"),
    NEWS_SPOT_FONT_SIZE("newsSpotFontSize"),
    IS_FIRST_SET_FONT("isFirstSetFont"),
    SCREEN_WIDTH("screenWidth"),
    SCREEN_HEIGHT("screenHeight"),
    CURRENT_VERSION("currentVersion"),
    CHANGE_PRAVICY_POLICY_SETTING("changePrivacyPolicy"),
    CHANGE_PRAVICY_POLICY_SETTING2("changePrivacyPolicy2"),
    LIST_ALL_CATEGORIES("listAllCategories"),
    DEFAULT_VIDEO_CATEGORY("defaultVideoCategory"),
    INTRO_PAGES("introPages"),
    DEFAULT_GALLERY_CATEGORY("defaultGalleryCategory");

    private String type;

    SettingsTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
