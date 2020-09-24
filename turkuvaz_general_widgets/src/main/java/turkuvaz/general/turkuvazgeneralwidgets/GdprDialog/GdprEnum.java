package turkuvaz.general.turkuvazgeneralwidgets.GdprDialog;

public enum GdprEnum {
    APP_HEDEFLEME("app-istatistik");
    private String type;

    GdprEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}