package turkuvaz.sdk.models.Models.Enums;

public enum AppNameTypes { // sadece deeplink için kullanılacak
    SABAH("sabah");

    private String type;

    AppNameTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
