package turkuvaz.sdk.global;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.spark.player.PlayItem;
import com.spark.player.SparkPlayer;

import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;


public class SparkPlayerPopup extends AppCompatActivity implements SparkPlayer.EventListener {
    public String videoUrlAddress;
    public String TITLE;
    private SparkPlayer m_spark_player = null;
    private boolean m_playing = true;
    private ImageButton mFullScreenButton;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spark_player_popup);

        videoUrlAddress = getIntent().getStringExtra("videoUrlAddress");
        TITLE = getIntent().getStringExtra("TITLE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GlobalMethods.stopAudio(this);
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        m_spark_player = findViewById(R.id.float_player);
        m_spark_player.addListener(this);


        if (videoUrlAddress.contains("ercdn.net")) {
            new VideoToken().getVideoToken(videoUrlAddress, SparkPlayerPopup.this).setVideoComplated(videoUrl -> {
                videoUrlAddress = videoUrl;
                init();
            });
        } else {
            init();
        }

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
        m_spark_player.removeListener(this);
        if (m_spark_player != null) m_spark_player.uninit();
        super.onDestroy();
    }

    public void init() {
        m_spark_player.vr_mode(false);
        m_spark_player.queue(new PlayItem("", videoUrlAddress, ""));
    }

    @Override
    public void onFullscreenChanged(boolean b) {

    }

    @Override
    public void onNewVideo(String s) {

    }

    @Override
    public void onTimeUpdate(int i) {

    }

    @Override
    public void onAdStart() {
    }

    @Override
    public void onAdEnd() {
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        if (reason == 0) {
            m_spark_player.seekTo(getIntent().getLongExtra("videoDuration", 0));
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
    }

}
