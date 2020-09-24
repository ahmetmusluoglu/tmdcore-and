package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 06/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class ApplicationsWebUrls {

    public static String getDomain(String currentApp) {
        String SABAH = ("https://www.sabah.com.tr");

        String mCurrentApp = "";

        switch (currentApp) {
            case "tr.sabah":
                mCurrentApp = SABAH;
                break;
        }
        return mCurrentApp;
    }
}