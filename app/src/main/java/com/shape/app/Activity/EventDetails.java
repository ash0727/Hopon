package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.material.appbar.AppBarLayout;
import com.shape.app.Fragment.Share.VideoAction_F;
import com.shape.app.Helper.Constant;
import com.shape.app.Interface.Fragment_Callback;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class EventDetails extends AppCompatActivity {
    String name, url, des, strlocation, strdate, str_flag, str_eventid, str_intent_flag, str_intent_valid;
    TextView name_name, description, location, txt_date, id_tx_inter;
    ImageView imageView, icon_playbtn;
    private static final int UI_ANIMATION_DELAY = 300;
    AppBarLayout toolbar;
    SharedPreferences sharedPreferences;
    String str_userid, str_theme;
    private final Handler mHideHandler = new Handler();
    private boolean mVisible;
    private static final int playerHeight = 250;
    LinearLayout layout_video;
    CardView card_image;
    ImageView btFullScreen;
    boolean flag = false;
    PlayerView playerView;
    ProgressBar progressBar;
    SimpleExoPlayer simpleExoPlayer;
    ImageView share;
    private FrameLayout frameLayoutMain;
    Boolean isScreenLandscape = false;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fmt_month_year = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
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
        str_flag = getIntent().getStringExtra("flag");
        str_eventid = getIntent().getStringExtra("id");
        str_intent_flag = getIntent().getStringExtra("in_flag");
        str_intent_valid = getIntent().getStringExtra("valid");
        ((TextView) findViewById(R.id.toolbr_lbl)).setText(name);


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout linearLayout = findViewById(R.id.ll_bg);
            linearLayout.setBackgroundColor(Color.parseColor(str_theme));

        }
        icon_playbtn = findViewById(R.id.icon_playbtn);
        playerView = (PlayerView) findViewById(R.id.player_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btFullScreen = (ImageView) playerView.findViewById(R.id.bt_fullscreen);
        frameLayoutMain = findViewById(R.id.frame_layout_main);
        toolbar = findViewById(R.id.toolbar);
        share = findViewById(R.id.share);

        layout_video = findViewById(R.id.video_layout);
        card_image = findViewById(R.id.image_layout);

        name_name = findViewById(R.id.title);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.image);
        location = findViewById(R.id.location);
        txt_date = findViewById(R.id.date);
        id_tx_inter = findViewById(R.id.id_tx_inter);
        Log.d("str_intent_valid", "onCreate: " + str_intent_valid);


        Date date_date = null;
        try {
            date_date = fmt_month_year.parse(strdate);
            txt_date.setText(fmtOut.format(date_date));
            Log.d("tag_adapter_date", "onBindViewHolder: " + date_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("date_new", "onCreate: " + strdate);

        Glide.with(getApplicationContext()).load(Constant.Event_img + url).placeholder(R.drawable.img_no).into(imageView);
        name_name.setText(name);
        description.setText(des);
        location.setText(strlocation);
        if (str_flag.equals("1")) {
            id_tx_inter.setBackgroundResource(R.color.bg_yellow);
        } else {
            id_tx_inter.setBackgroundResource(R.color.grey_20);
        }
        if (str_intent_flag.equals("0")) {
            icon_playbtn.setVisibility(View.GONE);

        } else {
            icon_playbtn.setVisibility(View.VISIBLE);
            //card_image.setVisibility(View.GONE);
            card_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("tag_video_ur;", "onClick: " + url);
                    Intent intent = new Intent(getApplicationContext(), VideoDetails.class);
                    intent.putExtra("url", url);
                    intent.putExtra("from", "1");//Event
                    startActivity(intent);
                }
            });

        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final VideoAction_F fragment = new VideoAction_F("", new Fragment_Callback() {
                    @Override
                    public void Responce(Bundle bundle) {

                        String action = bundle.getString("action");

                        switch (action) {

                            case VideoAction_F.NORMAL_SHARE:
                                Intent intent = new Intent(Intent.ACTION_SENDTO);
                                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                intent.putExtra(Intent.EXTRA_EMAIL, "");
                                intent.putExtra(Intent.EXTRA_TEXT, "Event Name:" + name + "\nEvent Date:" + strdate + "\nEvent Description:" + des + "\nEvent Location:" + strlocation);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                                break;
                            case VideoAction_F.WHATSAPP_SHARE:
                                Intent shareIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                                shareIntent1.setType("text/plain");
                                shareIntent1.setPackage("com.whatsapp");
                                shareIntent1.putExtra(Intent.EXTRA_TEXT, "Event Name:" + name + "\nEvent Date:" + strdate + "\nEvent Description:" + des + "\nEvent Location:" + strlocation);
                                shareIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(shareIntent1);

                                break;
                            case VideoAction_F.FACEBOOK_SHARE:

                                if (isPackageInstalled("com.facebook.katana")) {
                                    Intent fb_share = new Intent("com.facebook.feeds.ADD_TO_FEED");
                                    fb_share.setType("text/plain");
                                    fb_share.setPackage("com.facebook.katana");
                                    fb_share.putExtra(Intent.EXTRA_TEXT, "Event Name:" + name + "\nEvent Date:" + strdate + "\nEvent Description:" + des + "\nEvent Location:" + strlocation);
                                    fb_share.putExtra("com.facebook.platform.extra.APPLICATION_ID", getPackageName());
                                    Activity activity = EventDetails.this;
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
                                    insta_share.putExtra(Intent.EXTRA_TEXT, "Event Name:" + name + "\nEvent Date:" + strdate + "\nEvent Description:" + des + "\nEvent Location:" + strlocation);
                                    Activity activity = EventDetails.this;
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

        id_tx_inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_intent_valid.equals("Valid")) {
                    id_tx_inter.setEnabled(false);
                    if (str_flag.equals("1")) {
                        DislikeMethod("0");
                    } else {
                        LikeMethod("1");
                    }
                } else {
                    id_tx_inter.setEnabled(true);
                }

            }
        });
        getWindow().setFlags(1024, 1024);
        Log.d("videourk", "onCreate: " + Constant.Event_img + url);
        Uri videoUrl = Uri.parse(Constant.Event_img + url);
        LoadControl loadControl = new DefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(EventDetails.this, trackSelector, loadControl);

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
/*
                Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                int orientation = display.getOrientation();

                if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                    playerView.setLayoutParams(
                            new PlayerView.LayoutParams(
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT
                                    PlayerView.LayoutParams.MATCH_PARENT,
                                    // or ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ScreenUtils.convertDIPToPixels(EventDetails.this, playerHeight)));


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

                }*/
                Log.d("tag_video_ur;", "onClick: " + url);
                Intent intent = new Intent(getApplicationContext(), VideoDetails.class);
                intent.putExtra("url", url);
                intent.putExtra("from", "1");//Event
                startActivity(intent);
            }
        });

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
        final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EventInterested,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id, TotalLike;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===");
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                // Toast.makeText(getApplicationContext(), "Like Successfully!", Toast.LENGTH_SHORT).show();
                                id_tx_inter.setBackgroundResource(R.color.bg_yellow);

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
                params.put("event_id", str_eventid);
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
        final ProgressDialog progressDialog = new ProgressDialog(EventDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EventInterested,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===");
                                id_tx_inter.setBackgroundResource(R.color.grey_20);

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
                params.put("event_id", str_eventid);
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


    public void setStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            int statusBarColor = Color.parseColor(color);
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
        /*
        * getSupportActionBar().setBackgroundDrawable(
        new ColorDrawable(Color.parseColor("#AA3939")));
        *
        * */
    }


}