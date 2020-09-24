package turkuvaz.general.apps.utils;

import android.content.res.Resources;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/19/2020.
 */
public class Utils {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}
