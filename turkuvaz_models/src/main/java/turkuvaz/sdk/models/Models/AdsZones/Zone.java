
package turkuvaz.sdk.models.Models.AdsZones;

import android.os.Build;
import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Zone {

    @SerializedName("Zone")
    @Expose
    private String zone;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("ValueMobile")
    @Expose
    private String valueMobile;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getValue() {
        return replaceHtmlCharacter(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueMobile() {
        return valueMobile;
    }

    public void setValueMobile(String valueMobile) {
        this.valueMobile = valueMobile;
    }

    public String replaceHtmlCharacter(String zoneValue) { // \t\n\r gibi karakterler atılır, baştan ve sondan fark etmiyor
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Html.fromHtml(zoneValue, Html.FROM_HTML_MODE_COMPACT).toString().trim();
            } else
                return Html.fromHtml(zoneValue).toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
