package turkuvaz.sdk.models.Models.Enums;

public enum ExternalTypes {
    EMPTY(""),
    VIDEO("video://"),
    NEWS("news://"),
    ALBUM("album://"),
    PHOTO_NEWS("fotohaber://"),
    ARTICLE_SOURCE("articlesource://"),
    LIVE_STREAM("livestream://"),
    EXTERNAL("external://"),
    INTERNAL("internal://"),
    HTTPS("https://"),
    HTTP("http://"),
    DEEPLINK("deeplink://"),
    HOMESCREEN("homescreen://"),
    PAGE("page://"),
    IMAGE("image://");
    private String type;

    ExternalTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
