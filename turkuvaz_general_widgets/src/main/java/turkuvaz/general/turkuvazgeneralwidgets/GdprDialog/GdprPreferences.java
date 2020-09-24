package turkuvaz.general.turkuvazgeneralwidgets.GdprDialog;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class GdprPreferences {

    public static boolean getAppHedefleme(@NonNull Context context) {
        return getPreferences(context).getBoolean(GdprEnum.APP_HEDEFLEME.getType(), false);
    }


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("GDPR_KVKK", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }
}