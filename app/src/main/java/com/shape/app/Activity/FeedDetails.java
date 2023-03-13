package com.shape.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import com.shape.app.Fragment.Comment.Comment_F;
import com.shape.app.Fragment.Share.VideoAction_F;
import com.shape.app.Helper.Constant;
import com.shape.app.Helper.ScreenUtils;
import com.shape.app.Interface.Fragment_Callback;
import com.shape.app.Interface.Fragment_Data_Send;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

public class FeedDetails extends AppCompatActivity implements Fragment_Data_Send {
    String name, url, des, str_like, str_comt_count, str_like_count, str_feed_id;
    TextView name_name, description, like_count, comment_count;
    ImageView imageView, share, comment, like_btn;

    SharedPreferences sharedPreferences;
    String str_userid, str_intent_flag;


    LinearLayout layout_video;
    CardView card_image;

    private static final int UI_ANIMATION_DELAY = 300;


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
    ImageView btFullScreen;
    boolean flag = false;
    PlayerView playerView;
    ProgressBar progressBar;
    SimpleExoPlayer simpleExoPlayer;
    private FrameLayout frameLayoutMain;
    Boolean isScreenLandscape = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
        findViewById(R.id.bt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
        des = getIntent().getStringExtra("des");
        str_like = getIntent().getStringExtra("like");
        str_like_count = getIntent().getStringExtra("like_count");
        str_comt_count = getIntent().getStringExtra("comment_count");
        str_feed_id = getIntent().getStringExtra("feed_id");
        str_intent_flag = getIntent().getStringExtra("flag");

        ((TextView) findViewById(R.id.toolbr_lbl)).setText(name);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        playerView = (PlayerView) findViewById(R.id.player_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btFullScreen = (ImageView) playerView.findViewById(R.id.bt_fullscreen);
        frameLayoutMain = findViewById(R.id.frame_layout_main);

        layout_video = findViewById(R.id.video_layout);
        card_image = findViewById(R.id.image_layout);


        name_name = findViewById(R.id.title);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.image);
        share = findViewById(R.id.share);
        comment = findViewById(R.id.comment);
        like_btn = findViewById(R.id.like_btn);
        like_count = findViewById(R.id.like_count);
        comment_count = findViewById(R.id.comment_count);

        if (str_intent_flag.equals("0")) {
            layout_video.setVisibility(View.GONE);
        } else {
            layout_video.setVisibility(View.VISIBLE);
            card_image.setVisibility(View.GONE);

        }
        Glide.with(getApplicationContext()).load(Constant.FeedImage + url).into(imageView);
        name_name.setText(name);
        description.setText(des);
        like_count.setText(str_like_count);
        comment_count.setText(str_comt_count);
        if (str_like.equals("1")) {
            Glide.with(getApplicationContext()).load(R.drawable.heart_red).placeholder(R.drawable.heart_red).into(like_btn);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.dislike).placeholder(R.drawable.dislike).into(like_btn);
        }
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_like.equals("1")) {
                    DislikeMethod("0");
                } else {
                    LikeMethod("1");

                }
            }
        });

        getWindow().setFlags(1024, 1024);
        Log.d("videourk", "onCreate: " + Constant.FeedImage + url);
        Uri videoUrl = Uri.parse(Constant.FeedImage + url);
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(FeedDetails.this, trackSelector, loadControl);

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


                    playerView.setLayoutParams(
                            new PlayerView.LayoutParams(
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
                                    PlayerView.LayoutParams.MATCH_PARENT,
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ScreenUtils.convertDIPToPixels(FeedDetails.this, playerHeight)));


                    frameLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* *//* Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey i am using hopon app";//url or content
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share To"));*//*
                ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");*/


                final VideoAction_F fragment = new VideoAction_F("", new Fragment_Callback() {
                    @Override
                    public void Responce(Bundle bundle) {

                        String action = bundle.getString("action");

                        switch (action) {

                            case VideoAction_F.NORMAL_SHARE:
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                intent.putExtra(Intent.EXTRA_EMAIL, "");
                                intent.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + name + "\nFeed Description: " + des+"\nFeed Type: "+str_feed_id);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                                break;
                            case VideoAction_F.WHATSAPP_SHARE:
                                Intent shareIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent1.setType("text/plain");
                                shareIntent1.setPackage("com.whatsapp");
                                shareIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hopon");
                                shareIntent1.putExtra(android.content.Intent.EXTRA_TITLE, "Hopon");
                                shareIntent1.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                shareIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(Intent.createChooser(shareIntent1, getString(R.string.share)));

                                break;
                            case VideoAction_F.FACEBOOK_SHARE:

                                if (isPackageInstalled("com.facebook.katana")) {
                                    Intent fb_share = new Intent("com.facebook.feeds.ADD_TO_FEED");
                                    fb_share.setType("text/plain");
                                    fb_share.setPackage("com.facebook.katana");
                                    fb_share.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                    fb_share.putExtra("com.facebook.platform.extra.APPLICATION_ID", getPackageName());
                                    Activity activity = FeedDetails.this;
                                    if (activity.getPackageManager().resolveActivity(fb_share, 0) != null) {
                                        activity.startActivityForResult(fb_share, 0);
                                    }

                                }
                                break;

                            case VideoAction_F.INSTRAGRAM_SHARE:
                                if (isPackageInstalled("com.instagram.android")) {
                                    Intent insta_share = new Intent("com.instagram.share.ADD_TO_FEED");
                                    insta_share.setType("text/plain");
                                    insta_share.setPackage("com.instagram.android");
                                    insta_share.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                    Activity activity = FeedDetails.this;
                                    if (activity.getPackageManager().resolveActivity(insta_share, 0) != null) {
                                        activity.startActivityForResult(insta_share, 0);
                                    }
                                }
                                break;

                        }

                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("video_id", name);
                bundle.putString("user_id", "-");
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "");

            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommentBottomSheet bottomSheet = new CommentBottomSheet();
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                OpenComment();
            }
        });


    }

    private void OpenComment() {

        int comment_counnt = Integer.parseInt(str_comt_count);

        Fragment_Data_Send fragment_data_send = this;

        Comment_F comment_f = new Comment_F(comment_counnt, fragment_data_send);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id", str_feed_id);
        args.putString("user_id", str_userid);
        args.putString("comment_counnt", String.valueOf(comment_counnt));
        args.putString("from", "home");
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_container, comment_f).commit();

    }

    public boolean isPackageInstalled(String packagename) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Not installed", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    private void LikeMethod(String flag_like) {
        final ProgressDialog progressDialog = new ProgressDialog(FeedDetails.this);
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

                                //Toast.makeText(getApplicationContext(), "Like Successfully!", Toast.LENGTH_SHORT).show();

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
        final ProgressDialog progressDialog = new ProgressDialog(FeedDetails.this);
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

                                // Toast.makeText(getApplicationContext(), "DisLike Successfully!", Toast.LENGTH_SHORT).show();

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

    private void IntentData() {
        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
        des = getIntent().getStringExtra("des");
        str_like = getIntent().getStringExtra("like");
        str_like_count = getIntent().getStringExtra("like_count");
        str_comt_count = getIntent().getStringExtra("comment_count");
        str_feed_id = getIntent().getStringExtra("feed_id");
    }

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

    @Override
    public void onDataSent(String yourData) {

    }
}