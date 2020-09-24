package turkuvaz.sdk.models.Models.Database;

/**
 * Created by turgay.ulutas on 12/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class GalleryCategory {
    public static final String TABLE_NAME = "galleryCategories";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA = "data";


    private String id;
    private String data;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_DATA + " TEXT"
                    + ")";

    public GalleryCategory() {
    }

    public GalleryCategory(String id, String data) {
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