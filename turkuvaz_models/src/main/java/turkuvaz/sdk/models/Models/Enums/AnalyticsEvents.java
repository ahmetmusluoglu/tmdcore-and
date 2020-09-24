package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public enum AnalyticsEvents {
    SPLASH_PAGE("Splash_Page"),
    HOME_PAGE("Home_Page"),
    NOTIFICATION_PAGE("Notification_Page"),
    BROWSER_PAGE("Browser_Page"),
    SETTINGS_PAGE("Settings_Page"),
    CONTACT_US("Contact_Us"),
    DAILY_TAKVIM("Daily_Takvim"),
    LIVE_STREAM("Live_Stream"),

    NEWS_DETAILS("News_Details"),
    NEWS_CATEGORY("News_Category"),
    NEWS_SPORT_CATEGORY("News_Sport_Category"),

    COLUMNIST_PAGE("Columnist_Page"),
    COLUMNIST_DETAILS("Columnist_Details"),
    COLUMNIST_ARCHIVE("Columnist_Archive"),

    LAST_MINUTE("Last_Minute"),

    TV_SERIES("Tv_Series"),
    TV_SERIES_ACTUAL("Tv_Series_Actual"),
    TV_SERIES_CLASSIC("Tv_Series_Classic"),
    TV_SERIES_CLASSIC_FRAGMAN("Tv_Series_Classic_Fragman"),
    TV_SERIES_ACTUAL_FRAGMAN("Tv_Series_Actual_Fragman"),

    GALLERY_DETAILS("Gallery_Detail"),
    GALLERY_INDEX_VIEW("Gallery_Index_View"),
    GALLERY_CATEGORY("Gallery_Category"),
    GALLERY_CATEGORY_SABAH_SPOR("Gallery_Category_Sabah_Spor"),
    STATIC_KUNYE("static_kunye"),
    STATIC_VERI_POLITIKASI("static_veri_politikasi"),
    STATIC_GIZLILIK_BILDIRIMI("static_gizlilik_bildirimi"),
    SHORT_ORDER("shortOrder"),
    TITLE("title"),
    URL("url"),

    IMAGE_DETAILS("Image_Details"),

    RATE_APP("Rate_App"),
    RATE_APP_OK("Rate_App_Ok"),
    RATE_APP_LATER("Rate_App_Later"),
    RATE_APP_NEVER("Rate_App_Never"),
    UPDATE_APP("Update_App"),
    UPDATE_APP_OK("Update_App_Ok"),
    UPDATE_APP_LATER("Update_App_Later"),

    VIDEO_DETAILS("Video_Detail"),
    VIDEO_CATEGORY("Video_Category"),
    PROGRAM_CATEGORY("Program_Category"),
    PROGRAM_CATEGORY_SINGLE("Program_Category_Single"),

    PROGRAM_GUIDE("Program_Guide"),

    PRIVACY_POLICY_SETTINGS("Privacy_Policy_Settings"),
    GDPR_KVKK_DATA_POLICY("Data_Policy_Show"),

    ACTION_INSTAGRAM("Action_Instagram"),
    ACTION_TWITTER("Action_Twitter"),
    ACTION_FACEBOOK("Action_Facebook"),
    ACTION_YOUTUBE("Action_Youtube"),
    ACTION_NEWS_SHARE("Action_News_Share"),
    ACTION_GALLERY_SHARE("Action_Gallery_Share"),
    ACTION_GALLERY_IMAGE_SHARE("Action_Gallery_Image_Share"),
    ACTION_IMAGE_SHARE("Action_Image_Share"),
    ACTION_VIDEO_SHARE("Action_Video_Share"),
    ACTION_COLUMNIST_SHARE("Action_Columnist_Share");


    private String eventName;

    AnalyticsEvents(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

}