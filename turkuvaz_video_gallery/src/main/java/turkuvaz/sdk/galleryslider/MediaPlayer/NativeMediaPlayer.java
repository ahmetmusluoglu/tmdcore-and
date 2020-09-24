package turkuvaz.sdk.galleryslider.MediaPlayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

import com.turquaz.videogallery.R;

import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;

/**
 * Created by Ahmet MUŞLUOĞLU on 6/27/2020.
 */
public class NativeMediaPlayer extends RelativeLayout {

    private VideoView videoView;
    private String mVideoURL;
    private OnFullScreenChange onFullScreenChange;
    private ImageView imgPlay;
    private ImageView imgFullScreen;
    private ImageView imgCloseFullScreen;
    private ImageView imgPause;
    private ImageView imgReplay;
    private ProgressBar prgVideo;
    private RelativeLayout rlPlayerControllers;
    private RelativeLayout rlTouch;
    private SeekBar seekBar;
    private TextView txtDuration;
    private TextView txtTotalDuration;
    private Handler updateHandler = new Handler();


    public NativeMediaPlayer(Context context) {
        super(context);
    }

    public NativeMediaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NativeMediaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NativeMediaPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setVideoURL(String videoURL) {
        this.mVideoURL = videoURL;
    }

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void init() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.native_media_player, this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GlobalMethods.stopAudio(this.getContext());
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        rlTouch = findViewById(R.id.rlTouch);
        seekBar = findViewById(R.id.seekBar);
        txtDuration = findViewById(R.id.txtDuration);
        txtTotalDuration = findViewById(R.id.txtTotalDuration);
        seekBar = findViewById(R.id.seekBar);
        rlPlayerControllers = findViewById(R.id.rlPlayerControllers);
        videoView = findViewById(R.id.nativeMediaPlayer);
        imgPlay = findViewById(R.id.imgPlay);
        imgPause = findViewById(R.id.imgPause);
        imgReplay = findViewById(R.id.imgReplay);
        imgFullScreen = findViewById(R.id.imgFullScreen);
        imgCloseFullScreen = findViewById(R.id.imgCloseFullScreen);
        imgFullScreen.performClick();

        prgVideo = findViewById(R.id.prgVideo);
        MediaController controller = new MediaController(getContext());
        controller.setVisibility(GONE);
        controller.setAnchorView(videoView);

        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(mVideoURL));
        videoView.requestFocus();
        videoView.start();
        clickEvents();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickEvents() {
        videoView.setOnTouchListener((view, motionEvent) -> true);
        rlTouch.setOnClickListener(view -> {
            rlTouch.setVisibility(View.GONE);
            rlPlayerControllers.setVisibility(View.VISIBLE);
        });

        rlPlayerControllers.setOnClickListener(view -> {
            rlTouch.setVisibility(View.VISIBLE);
            rlPlayerControllers.setVisibility(View.GONE);
        });
        videoView.setOnPreparedListener(mediaPlayer -> {
                    try {
                        prgVideo.setVisibility(View.GONE);
                        imgPause.setVisibility(View.VISIBLE);
                        seekBar.setVisibility(View.VISIBLE);
                        seekBar.setProgress(0);
                        seekBar.setMax(videoView.getDuration());
                        txtDuration.setVisibility(VISIBLE);
                        txtTotalDuration.setVisibility(VISIBLE);
                        txtTotalDuration.setText(milliSecondsToTimer((long) videoView.getDuration()));
                        updateHandler.postDelayed(updateVideoTime, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        videoView.setOnCompletionListener(mediaPlayer -> {
            try {
                imgReplay.setVisibility(View.VISIBLE);
                imgPlay.setVisibility(View.GONE);
                imgPause.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imgPause.setOnClickListener(view -> {
            imgPlay.setVisibility(View.VISIBLE);
            imgPause.setVisibility(View.GONE);
            videoView.pause();
        });

        imgPlay.setOnClickListener(view -> {
            imgPlay.setVisibility(View.GONE);
            imgPause.setVisibility(View.VISIBLE);
            videoView.start();
        });

        imgFullScreen.setOnClickListener(view -> {
            try {
                imgFullScreen.setVisibility(GONE);
                imgCloseFullScreen.setVisibility(VISIBLE);
                if (onFullScreenChange != null)
                    onFullScreenChange.onFullScreen(true);
                makeVideoFullScreen();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imgCloseFullScreen.setOnClickListener(view -> {
            try {
                imgFullScreen.setVisibility(VISIBLE);
                imgCloseFullScreen.setVisibility(GONE);
                if (onFullScreenChange != null)
                    onFullScreenChange.onFullScreen(false);
                exitVideoFullScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        imgReplay.setOnClickListener(view -> {
            prgVideo.setVisibility(View.VISIBLE);
            imgReplay.setVisibility(View.GONE);
            videoView.setVideoURI(Uri.parse(mVideoURL));
            videoView.requestFocus();
            videoView.start();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                try {
                    txtDuration.setText(milliSecondsToTimer((long) progress));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    videoView.seekTo(seekBar.getProgress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            try {
                long currentPosition = videoView.getCurrentPosition();
                seekBar.setProgress((int) currentPosition);
                updateHandler.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }

    public void setOnFullScreenChange(OnFullScreenChange onFullScreenChange) {
        this.onFullScreenChange = onFullScreenChange;
    }

    public interface OnFullScreenChange {
        void onFullScreen(boolean isFullScreen);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private RelativeLayout.LayoutParams defaultVideoViewParams;
    private int defaultScreenOrientationMode;

    // play video in fullscreen mode
    private void makeVideoFullScreen() {
        defaultScreenOrientationMode = getResources().getConfiguration().orientation;
        defaultVideoViewParams = (LayoutParams) videoView.getLayoutParams();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoView.postDelayed(() -> {
            int h = GlobalMethods.getScreenHeight();
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, h);
            videoView.setLayoutParams(params);
//            params.setMargins(30, 50, 30, 50);
//            videoView.layout(30, 30, 30, 30);
        }, 300);
    }

    private boolean isRemoveStatusBar = false;

    public void removeStatusBar(boolean isRemoveStatusBar) {
        this.isRemoveStatusBar = isRemoveStatusBar;
    }

    // close fullscreen mode
    private void exitVideoFullScreen() {
        activity.setRequestedOrientation(defaultScreenOrientationMode);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        videoView.postDelayed(() -> {
            videoView.setLayoutParams(defaultVideoViewParams);
//            videoView.layout(10, 10, 10, 10);
        }, 300);
    }

    public int statusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
