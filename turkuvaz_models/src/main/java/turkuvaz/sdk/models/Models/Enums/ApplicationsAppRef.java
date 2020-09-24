package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 06/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class ApplicationsAppRef {

    public static String getAppRef(String currentApp) {
        String SABAH = ("android-sabah");

        String mCurrentApp = "";

        switch (currentApp) {
            case "tr.sabah":
                mCurrentApp = SABAH;
                break;
        }
        return mCurrentApp;
    }
}