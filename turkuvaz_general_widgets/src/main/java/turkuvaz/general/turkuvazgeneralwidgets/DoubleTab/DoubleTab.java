package turkuvaz.general.turkuvazgeneralwidgets.DoubleTab;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DoubleTab extends LinearLayout {

    String mLiveStreamUrl = "None";

    public DoubleTab(Context context) {
        super(context);
    }

    public DoubleTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DoubleTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DoubleTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {

    }

    public DoubleTab setLiveStreamUrl(String mLiveStreamUrl) {
        return this;
    }
}
