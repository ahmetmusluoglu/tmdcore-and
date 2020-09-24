
package turkuvaz.sdk.models.Models.AuthorsArchive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("columnists")
    @Expose
    private Columnists columnists;

    public Columnists getColumnists() {
        return columnists;
    }

    public void setColumnists(Columnists columnists) {
        this.columnists = columnists;
    }

}
