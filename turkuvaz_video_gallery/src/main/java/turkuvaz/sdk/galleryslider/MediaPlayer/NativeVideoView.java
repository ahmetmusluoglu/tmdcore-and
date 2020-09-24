package turkuvaz.sdk.galleryslider.MediaPlayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

/**
 * Created by Ahmet MUŞLUOĞLU on 6/27/2020.
 */
public class NativeVideoView extends VideoView {

    private int mForceHeight = 0;
    private int mForceWidth = 0;

    public NativeVideoView(Context context) {
        super(context);
    }

    public NativeVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NativeVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("@@@@", "onMeasure");

//        setMeasuredDimension(mForceWidth, mForceHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getHolder().setFixedSize(getMeasuredWidth(), getMeasuredHeight());
    }
}