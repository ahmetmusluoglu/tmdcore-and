
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("tiak")
    @Expose
    private Tiak tiak;
    @SerializedName("firebase")
    @Expose
    private Firebase firebase;
    @SerializedName("gemius")
    @Expose
    private Gemius gemius;

    public Tiak getTiak() {
        return tiak;
    }

    public Firebase getFirebase() {
        return firebase;
    }

    public Gemius getGemius() {
        return gemius;
    }

    public class Tiak extends Active {
    }

    public class Firebase extends Active {
    }

    public class Gemius extends Active {
        @SerializedName("key")
        @Expose
        private String key;

        public String getKey() {
            return key;
        }
    }

}
