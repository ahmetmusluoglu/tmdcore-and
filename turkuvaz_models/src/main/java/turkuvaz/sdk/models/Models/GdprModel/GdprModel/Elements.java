
package turkuvaz.sdk.models.Models.GdprModel.GdprModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Elements {

    @SerializedName("button1")
    @Expose
    private Button1 button1;
    @SerializedName("button2")
    @Expose
    private Button2 button2;
    @SerializedName("acceptButton")
    @Expose
    private AcceptButton acceptButton;
    @SerializedName("veripolitikasiLink")
    @Expose
    private VeripolitikasiLink veripolitikasiLink;

    public Button1 getButton1() {
        return button1;
    }

    public void setButton1(Button1 button1) {
        this.button1 = button1;
    }

    public Button2 getButton2() {
        return button2;
    }

    public void setButton2(Button2 button2) {
        this.button2 = button2;
    }

    public AcceptButton getAcceptButton() {
        return acceptButton;
    }

    public void setAcceptButton(AcceptButton acceptButton) {
        this.acceptButton = acceptButton;
    }

    public VeripolitikasiLink getVeripolitikasiLink() {
        return veripolitikasiLink;
    }

    public void setVeripolitikasiLink(VeripolitikasiLink veripolitikasiLink) {
        this.veripolitikasiLink = veripolitikasiLink;
    }

}
