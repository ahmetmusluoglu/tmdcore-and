package turkuvaz.sdk.models.Models.Enums;

public enum AdsType {
    ADS_320x142("app_android_320x142"),
    ADS_300x250("app_android_300x250"),
    ADS_320x50("app_android_320x50"),
    ADS_PREROLL("app_android_Preroll_Bolum"),
    ADS_PAGE_OVERLAY("app_android_pageoverlay");
    private String type;

    AdsType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}