package turkuvaz.sdk.models.Models.Enums;

import androidx.annotation.DrawableRes;

import turkuvaz.sdk.models.R;

/**
 * Created by turgay.ulutas on 04/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class ErrorImageType {
    public static @DrawableRes
    int getIcon(String currentApp) {
        @DrawableRes int SABAH = R.drawable.no_image_sabah;

        @DrawableRes int mCurrentApp = 0;

        switch (currentApp) {
            case "tr.sabah":
                mCurrentApp = SABAH;
                break;
        }
        return mCurrentApp;
    }
}