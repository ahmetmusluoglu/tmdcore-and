
package turkuvaz.sdk.models.Models.ConfigShared;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LivestreamUrls {

    @SerializedName("atv")
    @Expose
    private String atv;
    @SerializedName("ahaber")
    @Expose
    private String ahaber;
    @SerializedName("aspor")
    @Expose
    private String aspor;
    @SerializedName("a2")
    @Expose
    private String a2;
    @SerializedName("anews")
    @Expose
    private String anews;
    @SerializedName("minikatvcocuk")
    @Expose
    private String minikatvcocuk;
    @SerializedName("minikatvgo")
    @Expose
    private String minikatvgo;

    public String getAtv() {
        return atv;
    }

    public void setAtv(String atv) {
        this.atv = atv;
    }

    public String getAhaber() {
        return ahaber;
    }

    public void setAhaber(String ahaber) {
        this.ahaber = ahaber;
    }

    public String getAspor() {
        return aspor;
    }

    public void setAspor(String aspor) {
        this.aspor = aspor;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getAnews() {
        return anews;
    }

    public void setAnews(String anews) {
        this.anews = anews;
    }

    public String getMinikago() {
        return minikatvgo;
    }

    public String getMinikacocuk() {
        return minikatvcocuk;
    }
}
