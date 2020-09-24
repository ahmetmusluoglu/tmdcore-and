package turkuvaz.general.turkuvazgeneralwidgets.GdprDialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GdprDialog extends BottomSheetDialog {


    public GdprDialog(@NonNull final Context context, @NonNull String gdprApiPath, @NonNull String privacyPolicyFullUrl, @NonNull String title, int gdprVersion, int kvkkVersion, final boolean isGdprActive, final boolean isKvkkActive, final boolean isEditMode) {
        super(context);

    }

    public void create() {

    }
}