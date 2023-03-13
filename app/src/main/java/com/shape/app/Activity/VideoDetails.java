package com.shape.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

public class VideoDetails extends AppCompatActivity {
    ImageView btFullScreen;
    boolean flag = false;
    PlayerView playerView;
    ProgressBar progressBar;
    SimpleExoPlayer simpleExoPlayer;
    private FrameLayout frameLayoutMain;
    Boolean isScreenLandscape = false;
    //    String name, url, des,str_like,str_comt_count,str_like_count,str_feed_id;
//    TextView name_name, description, like_count, btn_comment, comment_count;
//    ImageView imageView, share, comment, like_btn;
    private static final int UI_ANIMATION_DELAY = 300;

    SharedPreferences sharedPreferences;
    String str_userid, url, from;
    Uri videoUrl;

    private final Handler mHideHandler = new Handler();
    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private static final int playerHeight = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        //  IntentData();
        url = getIntent().getStringExtra("url");
        from = getIntent().getStringExtra("from");

        playerView = (PlayerView) findViewById(R.id.player_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btFullScreen = (ImageView) playerView.findViewById(R.id.bt_fullscreen);
        frameLayoutMain = findViewById(R.id.frame_layout_main);
        //((TextView) findViewById(R.id.toolbr_lbl)).setText(name);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

//        name_name = findViewById(R.id.title);
//        description = findViewById(R.id.description);
//        share = findViewById(R.id.share);
//        comment = findViewById(R.id.comment);
//        like_btn = findViewById(R.id.like_btn);
//        like_count = findViewById(R.id.like_count);
//        comment_count = findViewById(R.id.comment_count);
//
//        like_count.setText(str_like_count);
//        comment_count.setText(str_comt_count);
//        if (str_like.equals("1")) {
//            Glide.with(getApplicationContext()).load(R.drawable.heart_red).placeholder(R.drawable.heart_red).into(like_btn);
//        } else {
//            Glide.with(getApplicationContext()).load(R.drawable.dislike).placeholder(R.drawable.dislike).into(like_btn);
//        }
//        name_name.setText(name);
//        description.setText(des);
//
//        like_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (str_like.equals("1")) {
//                    DislikeMethod("0");
//                } else {
//                    LikeMethod("1");
//
//                }
//            }
//        });
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "Hey i am using hopon app";//url or content
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share To"));
//            }
//        });
//        comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommentBottomSheet bottomSheet = new CommentBottomSheet();
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
//            }
//        });
        getWindow().setFlags(1024, 1024);
        Log.d("videourk", "onCreate: " + Constant.FeedImage + url);
        Log.d("videourkfrom", "onCreate: " + from);

        if (from.equals("0")) {
            videoUrl = Uri.parse(Constant.FeedImage + url);
        } else if (from.equals("1")) {
            videoUrl = Uri.parse(Constant.Event_img + url);
        } else if (from.equals("2")) {
            videoUrl = Uri.parse(Constant.VIDEO_PrimaryFitness_media + url);
        } else if (from.equals("3")) {
            videoUrl = Uri.parse(Constant.VIDEO_SecondaryFitness_media + url);
        } else if (from.equals("4")) {
            videoUrl = Uri.parse(Constant.VIDEO_OverallUnit_media + url);
        }else if (from.equals("5")) {
            videoUrl = Uri.parse(Constant.GameSkills_media + url);
        }else if (from.equals("6")) {
            videoUrl = Uri.parse(Constant.VIDEO_PrimaryFitness_video + url);
        }else if (from.equals("7")) {
            videoUrl = Uri.parse(Constant.SecondaryFitness_vid + url);
        }
        Log.d("video_With_url", "onCreate: " + videoUrl);

        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(VideoDetails.this, trackSelector, loadControl);

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
                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                int orientation = display.getOrientation();

                if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//                    playerView.setLayoutParams(
//                            new PlayerView.LayoutParams(
//                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
//                                    PlayerView.LayoutParams.MATCH_PARENT,
//                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                                    ScreenUtils.convertDIPToPixels(VideoDetails.this, playerHeight)));


                    // frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    btFullScreen.setImageResource(R.drawable.exo_controls_fullscreen_enter);
                    isScreenLandscape = false;
                    FullScreencall();

                    hide();
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    FullScreencall();

                    playerView.setLayoutParams(
                            new PlayerView.LayoutParams(
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
                                    PlayerView.LayoutParams.MATCH_PARENT,
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
                                    PlayerView.LayoutParams.MATCH_PARENT));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    btFullScreen.setImageResource(R.drawable.exo_controls_fullscreen_exit);
                    isScreenLandscape = true;
                    hide();

                }


            }
        });


    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
      /*  llParentContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);*/
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
    }

    private void hide() {
        // Hide UI first
       /* ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
*/
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, UI_ANIMATION_DELAY);
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

    /* private void IntentData() {
         name = getIntent().getStringExtra("name");
         url = getIntent().getStringExtra("url");
         des = getIntent().getStringExtra("des");
         str_like = getIntent().getStringExtra("like");
         str_like_count = getIntent().getStringExtra("like_count");
         str_comt_count = getIntent().getStringExtra("comment_count");
         str_feed_id = getIntent().getStringExtra("feed_id");
     }
     private void LikeMethod(String flag_like) {
         final ProgressDialog progressDialog = new ProgressDialog(VideoDetails.this);
         progressDialog.setCancelable(false);
         progressDialog.setMessage("Loading..");
         progressDialog.show();

         StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedLike,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         String code, message, id, user_id;

                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             code = jsonObject.getString("code");
                             message = jsonObject.getString("message");

                             Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                             if (code.equals("200")) {

                                 Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                 Glide.with(getApplicationContext()).load(R.drawable.heart_red).into(like_btn);

                                // Toast.makeText(getApplicationContext(), "Like Successfully!", Toast.LENGTH_SHORT).show();

                             } else {
                                 Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                             }


                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                         progressDialog.dismiss();

                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 progressDialog.dismiss();
                 Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
             }
         }) {
             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<String, String>();
                 params.put("feed_id", str_userid);
                 params.put("user_id", str_userid);
                 params.put("flag", "1");
                 Log.d("params_like", "getParams: " + params);
                 return params;
             }

             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("token", Constant.Token);
                 return headers;
             }
         };

         Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


     }

     private void DislikeMethod(String flag_like) {
         final ProgressDialog progressDialog = new ProgressDialog(VideoDetails.this);
         progressDialog.setCancelable(false);
         progressDialog.setMessage("Loading..");
         progressDialog.show();

         StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedLike,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         String code, message, id, user_id;

                         try {
                             JSONObject jsonObject = new JSONObject(response);
                             code = jsonObject.getString("code");
                             message = jsonObject.getString("message");

                             Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                             if (code.equals("200")) {

                                 Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                 // Glide.with(context).load(R.drawable.dislike).into(holder.like_btn);
                                 like_btn.setImageResource(R.drawable.dislike);

                                 //Toast.makeText(getApplicationContext(), "DisLike Successfully!", Toast.LENGTH_SHORT).show();

                             } else {
                                 Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                             }


                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                         progressDialog.dismiss();

                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 progressDialog.dismiss();
                 Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
             }
         }) {
             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<String, String>();
                 params.put("feed_id", str_feed_id);
                 params.put("user_id", str_userid);
                 params.put("flag", "0");
                 Log.d("params_dis_like", "getParams: " + params);
                 return params;
             }

             @Override
             public Map<String, String> getHeaders() throws AuthFailureError {
                 HashMap<String, String> headers = new HashMap<String, String>();
                 headers.put("token", Constant.Token);
                 return headers;
             }
         };

         Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
     }*/
    public void FullScreencall() {


        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }


    }

    public void hideStatusBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }
}