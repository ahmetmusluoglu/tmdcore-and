
package turkuvaz.sdk.models.Models.Series;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("categories")
    @Expose
    private Series categories;

    public Series getCategories() {
        return categories;
    }

    public void setCategories(Series series) {
        this.categories = series;
    }

}
