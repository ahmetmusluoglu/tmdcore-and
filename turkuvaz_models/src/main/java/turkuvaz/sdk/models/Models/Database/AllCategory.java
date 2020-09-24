package turkuvaz.sdk.models.Models.Database;

/**
 * Created by turgay.ulutas on 12/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class AllCategory {
    public static final String TABLE_NAME = "allCategory";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME_FOR_URL = "nameForUrl";


    private String id;
    private String data;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_NAME_FOR_URL + " TEXT"
                    + ")";

    public AllCategory() {
    }

    public AllCategory(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setId(String id) {
        this.id = id;
    }

}