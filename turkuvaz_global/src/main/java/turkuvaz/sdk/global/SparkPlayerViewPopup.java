package turkuvaz.sdk.global;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.comscore.streaming.AdvertisementMetadata;
import com.comscore.streaming.AdvertisementType;
import com.comscore.streaming.ContentMetadata;
import com.comscore.streaming.ContentType;
import com.comscore.streaming.StreamingAnalytics;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.spark.player.PlayItem;
import com.spark.player.SparkPlayer;

import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.TiakChannels;

/**
 * Created by turgay.ulutas on 07/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class SparkPlayerViewPopup extends RelativeLayout implements SparkPlayer.EventListener {

    private SparkPlayer m_spark_player = null;

    private String AD_TAG;
    private String mVideoURL;
    private onClosePopupListener onClosePopupListener;


    public SparkPlayerViewPopup(Context context) {
        super(context);
    }

    public SparkPlayerViewPopup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SparkPlayerViewPopup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SparkPlayerViewPopup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.spark_player_view_popup, this, true);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        AD_TAG = ApiListEnums.ADS_PREROLL.getApi(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GlobalMethods.stopAudio(this.getContext());
        }
        m_spark_player = findViewById(R.id.spark_playerVideoDetail);

        m_spark_player.addListener(SparkPlayerViewPopup.this);
        m_spark_player.vr_mode(false);
        m_spark_player.setPlayWhenReady(true);
        m_spark_player.set_controls_visibility(true);



        if (mVideoURL.contains("ercdn.net")) {
            new VideoToken().getVideoToken(mVideoURL, getContext()).setVideoComplated(videoUrl -> m_spark_player.queue(new PlayItem(AD_TAG, videoUrl, "")));
        } else {
            m_spark_player.queue(new PlayItem(AD_TAG, mVideoURL, ""));
        }

        ImageButton mFullScreenButton = m_spark_player.findViewById(R.id.spark_fullscreen_button_mine);
        mFullScreenButton.setOnClickListener(v -> (m_spark_player.findViewById(R.id.spark_fullscreen_button)).performClick());

        ImageButton mBtn_sparkPopupClose = findViewById(R.id.btn_sparkPopupClose);
        mBtn_sparkPopupClose.setOnClickListener(v -> onClosePopupListener.onClose());

    }

    public void setVideoURL(String videoURL) {
        this.mVideoURL = videoURL;
    }

    @Override
    public void onFullscreenChanged(boolean is_fullscreen) {

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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (Util.SDK_INT > 23)
            start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (Util.SDK_INT <= 23)
            stop();
    }

    private void stop() {

    }

    private void start() {
        if (m_spark_player != null) {
            m_spark_player.onResume();
            m_spark_player.setPlayWhenReady(true);
        }
    }

    public void closeSpark() {
        stop();
        if (m_spark_player != null) {
            m_spark_player.removeListener(this);
            m_spark_player.uninit();
        }
    }

    public interface onClosePopupListener {
        void onClose();
    }

    public void setOnClosePopupListener(SparkPlayerViewPopup.onClosePopupListener onClosePopupListener) {
        this.onClosePopupListener = onClosePopupListener;
    }
}