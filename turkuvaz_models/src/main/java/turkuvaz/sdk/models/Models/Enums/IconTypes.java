package turkuvaz.sdk.models.Models.Enums;

import android.content.Context;

import androidx.annotation.DrawableRes;

import turkuvaz.sdk.models.R;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class IconTypes {
    public static @DrawableRes int SABAH = R.drawable.no_image_sabah;
    public static @DrawableRes int TAKVIM = R.drawable.takvim_logo_text_transparent;

    public static @DrawableRes
    int getIcon(String currentApp) {
        @DrawableRes int mCurrentApp = 0;
        switch (currentApp) {
            case "tr.sabah":
                mCurrentApp = SABAH;
                break;
        }
        return mCurrentApp;
    }

    public static void setIcon(Context context, String currentApp, String title, int drawable) {
        switch (currentApp) {
            case "tr.sabah":
                if (title.toLowerCase().equals("spor"))
                    SABAH = drawable;
                break;
        }
    }

    public static void resetIcons(){ // sabah spor gibi custom sayfalardan geri gelip home sayfaya ait ekran açıldığında logonun orijinal haline dönmesi için
        SABAH = R.drawable.no_image_sabah;
    }
}