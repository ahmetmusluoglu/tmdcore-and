package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public enum AnalyticsParameters {
    CATEGORY("category_type"),
    TITLE("title"),
    ID("id"),
    URL("url");


    private String parameters;

    AnalyticsParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParametersName() {
        return parameters;
    }

}