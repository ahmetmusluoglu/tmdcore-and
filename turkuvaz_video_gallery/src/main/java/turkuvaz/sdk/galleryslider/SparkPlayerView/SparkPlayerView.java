package turkuvaz.sdk.galleryslider.SparkPlayerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.comscore.streaming.AdvertisementMetadata;
import com.comscore.streaming.AdvertisementType;
import com.comscore.streaming.ContentDeliveryMode;
import com.comscore.streaming.ContentDistributionModel;
import com.comscore.streaming.ContentFeedType;
import com.comscore.streaming.ContentMediaFormat;
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
import com.turquaz.videogallery.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import turkuvaz.sdk.global.Constants;
import turkuvaz.sdk.global.DateUtils;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.TiakChannels;
import turkuvaz.sdk.models.Models.GlobalModels.Article;

/**
 * Created by turgay.ulutas on 07/06/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class SparkPlayerView extends RelativeLayout implements SparkPlayer.EventListener {

    private SparkPlayer m_spark_player = null;

    private String AD_TAG;
    private String mVideoURL;
    private onFullScreenChange onFullScreenChange;
    private Article mArticle;
    private ImageButton mFullScreenButton;

    private StreamingAnalytics sa;
    private static ContentMetadata cm;
    private static AdvertisementMetadata am;
    private String TAG = "<*>DK-SparkPlayerView";

    public SparkPlayerView(Context context) {
        super(context);
    }

    public SparkPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SparkPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SparkPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.spark_player_view, this, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            GlobalMethods.stopAudio(this.getContext());
        }
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        AD_TAG = ApiListEnums.ADS_PREROLL.getApi(getContext());

        m_spark_player = findViewById(R.id.spark_playerVideoDetail);

        m_spark_player.addListener(this);
        m_spark_player.vr_mode(false);
        m_spark_player.setPlayWhenReady(true);
        m_spark_player.set_controls_visibility(true);
        m_spark_player.queue(new PlayItem(AD_TAG, mVideoURL, ""));

        mFullScreenButton = m_spark_player.findViewById(R.id.spark_fullscreen_button_mine);
        mFullScreenButton.setOnClickListener(v -> (m_spark_player.findViewById(R.id.spark_fullscreen_button)).performClick());

        if (GlobalMethods.isNeedTiak()) {
            sa = new StreamingAnalytics();
            sa.createPlaybackSession();
            sa.setMediaPlayerName(Constants.MEDIA_PLAYER_NAME);
            sa.setMediaPlayerVersion(Constants.MEDIA_PLAYER_VERSION);
            sa.setImplementationId(TiakChannels.getChannelId(getContext().getApplicationInfo().packageName));
            sa.setProjectId(TiakChannels.getChannelId(getContext().getApplicationInfo().packageName));
            setMetadata(mArticle);
        }
    }

    public void setOnFullScreenChange(onFullScreenChange onFullScreenChange) {
        this.onFullScreenChange = onFullScreenChange;
    }

    public void setVideoURL(String videoURL) {
        this.mVideoURL = videoURL;
    }

    @Override
    public void onFullscreenChanged(boolean is_fullscreen) {
        onFullScreenChange.fullScreen(is_fullscreen);
        mFullScreenButton.setImageResource(!is_fullscreen ? R.drawable.ic_fullscreen : R.drawable.ic_full_screen_close);
    }

    @Override
    public void onNewVideo(String s) {
        Log.i(TAG, "onNewVideo: ");
    }

    @Override
    public void onTimeUpdate(int i) {
        Log.i(TAG, "onTimeUpdate: ");
    }

    @Override
    public void onAdStart() {
        if (sa != null) {
            am = new AdvertisementMetadata.Builder()
                    .mediaType(AdvertisementType.ON_DEMAND_PRE_ROLL)
                    .relatedContentMetadata(cm)
//                    .uniqueId("")
//                    .length(0)
                    .build();
            sa.setMetadata(am);
            sa.notifyPlay();
        }
        Log.i("TAG_ADS", "onAdStart: ");
    }

    @Override
    public void onAdEnd() {
        if (sa != null) {
            sa.notifyEnd();

        }
        Log.i(TAG, "onAdEnd: ");
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        Log.i(TAG, "onTimelineChanged: ");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.i(TAG, "onTracksChanged: ");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i(TAG, "onLoadingChanged: ");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.i(TAG, "onPlayerStateChanged: " + playWhenReady + " :´: " + playbackState);
        if (playbackState == Player.STATE_READY) {
            if (playWhenReady) {

//                Analytics.notifyViewEvent(GlobalMethods.getTiakData(getContext(), mArticle));
                if (sa != null) {
                    sa.setMetadata(cm);
                    sa.notifyPlay();
                    Log.i(TAG, "onPlayerStateChanged: notifyPlay");
                }
            } else {
                if (sa != null) {
                    sa.notifyPause();
                    Log.i(TAG, "onPlayerStateChanged: notifyPause");
                }
            }
        } else if (playbackState == Player.STATE_ENDED) {
            if (sa != null) sa.notifyEnd();
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        Log.i(TAG, "onRepeatModeChanged: ");
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.i(TAG, "onShuffleModeEnabledChanged: ");
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i(TAG, "onPlayerError: ");
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.i(TAG, "onPositionDiscontinuity: ");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.i(TAG, "onPlaybackParametersChanged: ");
    }

    @Override
    public void onSeekProcessed() {
        if (m_spark_player != null && sa != null) {
            long currPosition = m_spark_player.getCurrentPosition();
            sa.startFromPosition(currPosition);
            sa.notifySeekStart();
        }

//        if (sa != null) sa.notifySeekStart();
        Log.i(TAG, "onSeekProcessedXX" + m_spark_player.getCurrentPosition());
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
        if (m_spark_player != null) {
            m_spark_player.setPlayWhenReady(false);
            m_spark_player.onPause();
            if (sa != null) sa.notifyEnd();
            Log.i(TAG, "stop");
        }
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

    public interface onFullScreenChange {
        void fullScreen(boolean isFullScreen);
    }

    public void setArticle(Article mArticle) {
        this.mArticle = mArticle;
    }

    private void setMetadata(Article article) {
        try {
            //yyyy-MM-dd'T'HH:mm:ss.SSS
            int year = DateUtils.getPartOfDate(article.getCreatedDateReal(), "yyyy");
            int month = DateUtils.getPartOfDate(article.getCreatedDateReal(), "MM");
            int day = DateUtils.getPartOfDate(article.getCreatedDateReal(), "dd");
            int hour = DateUtils.getPartOfDate(article.getCreatedDateReal(), "HH");
            int minutes = DateUtils.getPartOfDate(article.getCreatedDateReal(), "mm");
            long clipLength = article.getVideoDuration() != null && !article.getVideoDuration().isEmpty() ? (Long.parseLong(article.getVideoDuration()) * 1000) : 0;
            cm = new ContentMetadata.Builder()
                    .mediaType(ContentType.LONG_FORM_ON_DEMAND)
                    .uniqueId(article.getId() + "")
                    .length(clipLength) //23m58s in milliseconds
                    .dictionaryClassificationC3("*null")
                    .dictionaryClassificationC4("*null")
                    .dictionaryClassificationC6("*null")
                    .stationTitle(GlobalMethods.getCurrentFlavor(getContext()).name() + "")
                    .stationCode(TiakChannels.getChannelId(getContext().getApplicationInfo().packageName) + "")
                    .publisherName(Constants.PUBLISHER_NAME)
                    .programTitle(URLEncoder.encode(article.getCategoryName() + "", "UTF-8"))
                    .episodeTitle(article.getTitleShort() + "")
                    .mediaFormat(ContentMediaFormat.FULL_CONTENT_GENERIC)
                    .distributionModel(ContentDistributionModel.TV_AND_ONLINE)
                    .episodeId(article.getEpisode() + "")
                    .genreName(article.getBroadcastKind() + "")
                    .dateOfTvAiring(year, month, day)
                    .feedType(ContentFeedType.EAST_HD)
                    .deliveryMode(ContentDeliveryMode.ON_DEMAND)
                    .programId(article.getId() + "")
                    .timeOfTvAiring(hour, minutes)
                    .classifyAsCompleteEpisode(true)
                    .build();
        } catch (UnsupportedEncodingException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}