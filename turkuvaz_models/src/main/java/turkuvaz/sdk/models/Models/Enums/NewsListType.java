package turkuvaz.sdk.models.Models.Enums;

public enum NewsListType {
    GRID_2x2_BOX("grid_2x2_box"),
    GRID_1x1_LIST("grid_1x1_list"),
    GRID_1x1_BOX("grid_1x1_box"),
    INFO_1x1_BOX("info_1x1_list"),
    GRID_1X1_ROUNDED_BOX("grid_1x1_rounded_box"),
    GRID_UNKNOWN("");

    private String type;

    NewsListType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}