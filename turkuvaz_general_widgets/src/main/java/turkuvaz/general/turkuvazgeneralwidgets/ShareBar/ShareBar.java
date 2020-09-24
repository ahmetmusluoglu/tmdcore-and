package turkuvaz.general.turkuvazgeneralwidgets.ShareBar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;

public class ShareBar extends LinearLayout {

    public shareNewsClickListener shareNewsClickListener;

    public ShareBar(Context context) {
        super(context);
        init(context);
    }

    public ShareBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShareBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShareBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

    }

    public interface shareNewsClickListener {
        void onClickFacebook();

        void onClickTwitter();

        void onClickWhatsApp();

        void onClickGeneral();

        void onClickTextChanger();
    }

    public void setSocialMediaClickListener(shareNewsClickListener shareNewsClickListener) {
    }
}