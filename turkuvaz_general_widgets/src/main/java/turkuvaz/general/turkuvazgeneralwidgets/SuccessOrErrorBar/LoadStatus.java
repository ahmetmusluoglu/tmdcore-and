package turkuvaz.general.turkuvazgeneralwidgets.SuccessOrErrorBar;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.general.turkuvazgeneralwidgets.TurkuvazTextView.TTextView;

/**
 * Created by turgay.ulutas on 07/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */
public class LoadStatus extends RelativeLayout {

    private onLoadTryAgainListener onLoadTryAgain;

    public LoadStatus(Context context) {
        super(context);
        init(context);
    }

    public LoadStatus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadStatus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadStatus(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.load_status_main, this, true);
    }

    public void loadStatusSuccess() {
        findViewById(R.id.ly_loadStatusRoot).setVisibility(GONE);
    }

    public void loadStatusError(String errorMessage) {
        TTextView mTv_loadStatus = findViewById(R.id.tv_loadStatus);
        ImageView mImg_loadStatus = findViewById(R.id.img_loadStatus);
        ProgressBar mPb_loadStatus = findViewById(R.id.pb_loadStatus);

        mPb_loadStatus.setVisibility(GONE);
        mImg_loadStatus.setVisibility(VISIBLE);
        mTv_loadStatus.setText(getContext().getResources().getString(R.string.sayfa_hatasi));

        findViewById(R.id.ly_loadStatusRoot).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoadTryAgain != null) {
                    onLoadTryAgain.tryAgain();
                    resetLoadPanel();
                }
            }
        });
    }

    public void resetLoadPanel() {
        TTextView mTv_loadStatus = findViewById(R.id.tv_loadStatus);
        mTv_loadStatus.setVisibility(VISIBLE);
        findViewById(R.id.pb_loadStatus).setVisibility(VISIBLE);
        findViewById(R.id.ly_loadStatusRoot).setVisibility(VISIBLE);

        findViewById(R.id.img_loadStatus).setVisibility(GONE);
        findViewById(R.id.ly_loadStatusRoot).setOnClickListener(null);
    }

    public interface onLoadTryAgainListener {
        void tryAgain();
    }

    public void setOnLoadTryAgain(onLoadTryAgainListener onLoadTryAgain) {
        this.onLoadTryAgain = onLoadTryAgain;
    }
}
