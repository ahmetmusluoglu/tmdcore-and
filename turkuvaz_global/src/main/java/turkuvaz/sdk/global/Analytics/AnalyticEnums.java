package turkuvaz.sdk.global.Analytics;

public enum AnalyticEnums {
    EVENT_NAME("event_name"),
    PARAMETER_TITLE("title"),
    PARAMETER_URL("url"),
    PARAMETER_NAME("name"),
    VIEW_PAGE("Page_Views"),
    PARAMETER_NAMEFORURL("nameForUrl");

    private String enums;

    AnalyticEnums(String enums) {
        this.enums = enums;
    }

    public String getEnum() {
        return enums;
    }
}
