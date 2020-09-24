package turkuvaz.general.turkuvazgeneralwidgets.ErrorDialog;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ErrorDialog extends BottomSheetDialog {


    public ErrorDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @SuppressLint("SetTextI18n")
    public ErrorDialog init() {

        return this;
    }

    public ErrorDialog setSubTitle(String subTitle) {
        return this;
    }

    public ErrorDialog setErrorFromClass(String errorFromClass) {
        return this;
    }


    public ErrorDialog setThrowable(Throwable throwable) {
        return this;
    }

}