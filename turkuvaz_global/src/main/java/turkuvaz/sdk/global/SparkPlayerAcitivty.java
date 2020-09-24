package turkuvaz.sdk.global;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.comscore.streaming.AdvertisementMetadata;
import com.comscore.streaming.AdvertisementType;
import com.comscore.streaming.ContentMetadata;
import com.comscore.streaming.ContentType;
import com.comscore.streaming.StreamingAnalytics;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.util.Util;
import com.spark.player.PlayItem;
import com.spark.player.SparkPlayer;

import turkuvaz.sdk.global.Analytics.TurkuvazAnalytic;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.AnalyticsEvents;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.TiakChannels;

public class SparkPlayerAcitivty extends AppCompatActivity {
    private Activity activity;
    public String videoUrlAddress;
    public String TITLE;
    private SparkPlayer m_spark_player = null;
    private SampleListener m_listener;
    private boolean m_playing = true;
    private static String AD_TAG;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase,""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spark_player);

        videoUrlAddress = getIntent().getStringExtra("videoUrlAddress");
        TITLE = getIntent().getStringExtra("TITLE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GlobalMethods.stopAudio(this);
        }
        AD_TAG = ApiListEnums.ADS_PREROLL.getApi(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity = this;
        m_spark_player = findViewById(R.id.float_player);
        m_listener = new SampleListener();
        m_spark_player.addListener(m_listener);

        if (videoUrlAddress.contains("ercdn.net")) {
            new turkuvaz.sdk.global.VideoToken().getVideoToken(videoUrlAddress, SparkPlayerAcitivty.this).setVideoComplated(videoUrl -> {
                videoUrlAddress = videoUrl;
                init();
            });
        } else {
            init();
        }

        /** SEND ANALYTICS */
        new TurkuvazAnalytic(this).setIsPageView(true).setLoggable(true).setEventName(AnalyticsEvents.LIVE_STREAM.getEventName()).sendEvent();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23)
            start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23)
            start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23)
            stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23)
            stop();
    }

    private void stop() {
        m_playing = m_spark_player.getPlayWhenReady();
        m_spark_player.setPlayWhenReady(false);
        m_spark_player.onPause();
    }

    private void start() {
        m_spark_player.onResume();
        m_spark_player.setPlayWhenReady(m_playing);
    }

    @Override
    public void onDestroy() {
        stop();
        if (m_spark_player != null) {
            m_spark_player.removeListener(m_listener);
            m_spark_player.uninit();
        }
        super.onDestroy();
    }

    public void init() {
        Log.d("<*>DK videoUrlAddress:", videoUrlAddress);
        m_spark_player.vr_mode(false);
        m_spark_player.queue(new PlayItem(AD_TAG, videoUrlAddress, ""));
    }

    class SampleListener extends SparkPlayer.DefaultEventListener {
        @Override
        public void onPlayerStateChanged(boolean play, int playback_state) {
            Log.d("<*>DK", play ? "play" : "pause");
            String state = playback_state == Player.STATE_IDLE ? "IDLE" : playback_state == Player.STATE_BUFFERING ? "BUFFERING"
                    : playback_state == Player.STATE_READY ? "READY" : playback_state == Player.STATE_ENDED ? "ENDED" : "UNKNOWN";
            Log.d("<*>DK", "state: " + state);
        }

        @Override
        public void onSeekProcessed() {
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.e("<*>DK", "error: ", error);
        }

        @Override
        public void onLoadingChanged(boolean is_loading) {
            Log.d("<*>DK", "loading: " + is_loading);
        }

        @Override
        public void onFullscreenChanged(boolean is_fullscreen) {
            Log.d("<*>DK", "onFullscreenChanged: " + is_fullscreen);
            finish();
        }

        @Override
        public void onAdStart() {
        }

        @Override
        public void onAdEnd() {
        }
    }

    public void finishPlayer(View view) {
        finish();
    }

}
