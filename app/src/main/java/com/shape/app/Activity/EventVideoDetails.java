package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.shape.app.Helper.Constant;
import com.shape.app.R;

public class EventVideoDetails extends AppCompatActivity {
    ImageView btFullScreen;
    boolean flag = false;
    PlayerView playerView;
    ProgressBar progressBar;
    SimpleExoPlayer simpleExoPlayer;
    String name, url, des;
    ImageView share;
    private FrameLayout frameLayoutMain;
    Boolean isScreenLandscape = false;

    String strlocation,strdate;
    TextView name_name,description,location,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_video_details);
        findViewById(R.id.bt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
        des = getIntent().getStringExtra("des");
        strlocation = getIntent().getStringExtra("location");
        strdate = getIntent().getStringExtra("date");

        ((TextView) findViewById(R.id.toolbr_lbl)).setText(name);

        playerView = (PlayerView) findViewById(R.id.player_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btFullScreen = (ImageView) playerView.findViewById(R.id.bt_fullscreen);
        frameLayoutMain = findViewById(R.id.frame_layout_main);
        ((TextView) findViewById(R.id.toolbr_lbl)).setText(name);


        name_name =  findViewById(R.id.title);
        share =  findViewById(R.id.share);
        description =  findViewById(R.id.description);
        location =  findViewById(R.id.location);
        date =  findViewById(R.id.date);
        name_name.setText(name);
        description.setText(des);
        location.setText(strlocation);
        date.setText(strdate);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

        getWindow().setFlags(1024, 1024);
        Log.d("videourk", "onCreate: " + Constant.Event_img + url);
        Uri videoUrl = Uri.parse(Constant.Event_img + url);
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(EventVideoDetails.this, trackSelector, loadControl);

        DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl, defaultHttpDataSourceFactory, extractorsFactory, null, null);
        playerView.setPlayer(simpleExoPlayer);
        playerView.setKeepScreenOn(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.addListener(new Player.EventListener() {
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            public void onLoadingChanged(boolean isLoading) {
            }

            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            public void onRepeatModeChanged(int repeatMode) {
            }

            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            public void onPlayerError(ExoPlaybackException error) {
                Log.d("tag_play", "onPlayerError: " + error.toString());
            }

            public void onPositionDiscontinuity(int reason) {
            }

            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            public void onSeekProcessed() {
            }
        });
        btFullScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (flag) {
                    btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    flag = false;
                    Log.d("tag_pos_1", "onClick: " + simpleExoPlayer.getContentPosition() + "dur:" + simpleExoPlayer.getDuration());

                    return;
                }
                btFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                flag = true;
            }
        });


    }


    public void onPause() {
        super.onPause();
        this.simpleExoPlayer.setPlayWhenReady(false);
        this.simpleExoPlayer.getPlaybackState();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        this.simpleExoPlayer.setPlayWhenReady(true);
        this.simpleExoPlayer.getPlaybackState();
    }

}