package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 06/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class GemiusIdentifier {

    public static String getGemiusIdentifier(String currentApp) {
        String SABAH = ("ba1KWAbEK9VERKSY7ZHP5sUG.BkNo1geDaNbVcgEJwD.t7");

        String mCurrentApp = "";

        switch (currentApp) {
            case "tr.sabah":
                mCurrentApp = SABAH;
                break;
        }
        return mCurrentApp;
    }
}