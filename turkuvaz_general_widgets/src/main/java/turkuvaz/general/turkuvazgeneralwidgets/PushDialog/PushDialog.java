package turkuvaz.general.turkuvazgeneralwidgets.PushDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.general.turkuvazgeneralwidgets.TurkuvazTextView.TTextView;
import turkuvaz.sdk.global.FirebasePush.PushModel;

/**
 * Created by turgay.ulutas on 03/07/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class PushDialog extends Dialog {
    private onPushDialog onPushDialog;
    private PushModel pushModel;
    private Activity mActivity;

    public PushDialog(@NonNull Context context) {
        super(context);
        mActivity = (Activity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public PushDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected PushDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void setPushModel(PushModel pushModel) {
        this.pushModel = pushModel;
    }

    public void showDialog() {

    }

    public interface onPushDialog {
        void closeDialog();
        void openNews(PushModel pushModel);
    }

    public void closeDialog() {
        if (mActivity != null && !mActivity.isFinishing() && isShowing()) {
            cancel();
            dismiss();
        }
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
        if (onPushDialog != null)
            onPushDialog.closeDialog();
    }

    public void setOnPushDialog(onPushDialog onPushDialog) {
        this.onPushDialog = onPushDialog;
    }
}