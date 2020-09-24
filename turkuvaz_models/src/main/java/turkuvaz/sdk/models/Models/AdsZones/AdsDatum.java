
package turkuvaz.sdk.models.Models.AdsZones;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsDatum {

    @SerializedName("Section")
    @Expose
    private String section;
    @SerializedName("Zones")
    @Expose
    private ArrayList<Zone> zones = null;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }

}
