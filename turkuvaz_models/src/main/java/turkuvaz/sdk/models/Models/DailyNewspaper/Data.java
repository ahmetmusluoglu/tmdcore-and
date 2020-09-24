
package turkuvaz.sdk.models.Models.DailyNewspaper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("listegazete")
    @Expose
    private Listegazete listegazete;

    public Listegazete getListegazete() {
        return listegazete;
    }

    public void setListegazete(Listegazete listegazete) {
        this.listegazete = listegazete;
    }

}
