package turkuvaz.sdk.global.FirebasePush;

public enum PushType {

    NEWS("HaberDetay"),
    GALLERY("GaleriDetay"),
    PHOTO_NEWS("FotohaberDetay"),
    AUTHOR("YazarDetay"),
    BROWSER("WebView"),
    LIVE_STREAM("CanliYayin"),
    VIDEO("VideoDetay"),
    UNDEFINED("");
    private String type;

    PushType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}

