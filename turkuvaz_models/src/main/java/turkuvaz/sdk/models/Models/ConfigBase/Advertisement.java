
package turkuvaz.sdk.models.Models.ConfigBase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Advertisement {

    @SerializedName("advertorial")
    @Expose
    private Advertorial advertorial;
    @SerializedName("stickyAd")
    @Expose
    private StickyAd stickyAd;
    @SerializedName("interstitialAd")
    @Expose
    private InterstitialAd interstitialAd;

    public Advertorial getAdvertorial() {
        return advertorial;
    }

    public StickyAd getStickyAd() {
        return stickyAd;
    }

    public InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }

    public class Advertorial {
        @SerializedName("apiUrl")
        @Expose
        private String apiUrl;

        public String getApiUrl() {
            return apiUrl;
        }
    }

    public class InterstitialAd extends Active {
        @SerializedName("minutes")
        @Expose
        private int minutes;

        public int getMinutes() {
            return minutes;
        }
    }

    public class StickyAd extends Active {
    }

}
