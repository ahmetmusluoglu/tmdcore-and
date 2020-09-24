package turkuvaz.sdk.galleryslider.SparkPlayerView;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.comscore.Analytics;
import com.turquaz.videogallery.R;

import turkuvaz.sdk.global.MyContextWrapper;

public class FullScreenVideoActivity extends AppCompatActivity implements SparkPlayerView.onFullScreenChange {
    SparkPlayerView sparkPlayerView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_full_screen_video);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("videoURL")) {
            sparkPlayerView = new SparkPlayerView(FullScreenVideoActivity.this);
            sparkPlayerView.setOnFullScreenChange(FullScreenVideoActivity.this);
            sparkPlayerView.setVideoURL(bundle.getString("videoURL"));
            sparkPlayerView.init();

            FrameLayout frameLayout = findViewById(R.id.frame_fullScreenVideo);
            frameLayout.addView(sparkPlayerView);
        } else {
            finish();
        }

    }

    @Override
    public void fullScreen(boolean isFullScreen) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sparkPlayerView != null) {
            sparkPlayerView.closeSpark();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Analytics.notifyUxActive();
        Analytics.notifyEnterForeground();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Analytics.notifyUxInactive();
        Analytics.notifyExitForeground();
    }
}
