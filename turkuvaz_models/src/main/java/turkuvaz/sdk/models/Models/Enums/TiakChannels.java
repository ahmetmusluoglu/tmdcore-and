package turkuvaz.sdk.models.Models.Enums;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class TiakChannels {
    public static String getChannelId(String currentApp) {
        String SABAH = "2222";//Rastgele verildi bu deger...

        String mCurrentApp = "";

        switch (currentApp) {
            case "tr.sabah":
                mCurrentApp = SABAH;
                break;
        }
        return mCurrentApp;
    }
}