package com.shape.app.Activity.PerformanceCriteria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shape.app.Activity.ImageActivity;
import com.shape.app.Activity.PDFDetails;
import com.shape.app.Activity.VideoDetails;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.GameSkillModel;
import com.shape.app.Models.HeartRateModel;
import com.shape.app.Models.PermissionModel;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.CLASS_ID;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.GENDER;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.PROFILE_PIC;
import static com.shape.app.Forms.Login.SCHOOL_ID;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class GameActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String str_userid, intent_grade, str_theme, str_code, str_fname, str_lname;
    TextView name, st_code, overall_grade, grade_over, tactics, advance_skill, rules, team_player, attention_to_basics;
    ImageView tactics_media_p, team_player_media_p, advance_skill_media_p, report, tactics_media_v, advance_skill_media_v, team_player_media_v;
    ArrayList<HeartRateModel> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    LinearLayout ll_advance_skill, ll_pdf;
    ImageView profile_image, img_card_1, img_card_2, img_card_3, img_card_4, img_card_5;
    String str_pic, ImageFull_Url;
    PermissionModel permissionModel;
    String str_class_id, str_school_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_code = sharedPreferences.getString(STUDENT_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        str_pic = sharedPreferences.getString(PROFILE_PIC, "");
        str_school_id = sharedPreferences.getString(SCHOOL_ID, "");
        str_class_id = sharedPreferences.getString(CLASS_ID, "");
        intent_grade = getIntent().getStringExtra("grade");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout linearLayout = findViewById(R.id.ll_bg);
            RelativeLayout rl_bg = findViewById(R.id.rl_bg);
            linearLayout.setBackgroundColor(Color.parseColor(str_theme));
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }


        img_card_1 = findViewById(R.id.img_card_3);
        img_card_2 = findViewById(R.id.img_card_4);
        img_card_3 = findViewById(R.id.img_card_5);


        ll_pdf = findViewById(R.id.ll_pdf);
        ll_advance_skill = findViewById(R.id.ll_advance_skill);
        overall_grade = findViewById(R.id.overall_grade);
        grade_over = findViewById(R.id.grade_over);
        name = findViewById(R.id.name);
        st_code = findViewById(R.id.st_code);
        name.setText(str_fname + " " + str_lname);
        st_code.setText("(" + str_code + ")");


        if (intent_grade == null || intent_grade.equals("")) {
            overall_grade.setText("-");
            grade_over.setText("-");

        } else {
            overall_grade.setText(intent_grade + "\nGrade");
            grade_over.setText(intent_grade + "\nGrade");

        }

        attention_to_basics = findViewById(R.id.attention_to_basics);
        rules = findViewById(R.id.rules);
        tactics = findViewById(R.id.tactics);
        advance_skill = findViewById(R.id.advance_skill);
        team_player = findViewById(R.id.team_player);
        profile_image = findViewById(R.id.profile_image);
       // Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        report = findViewById(R.id.report);
        tactics_media_p = findViewById(R.id.tactics_media_p);
        team_player_media_p = findViewById(R.id.team_player_media_p);
        advance_skill_media_p = findViewById(R.id.advance_skill_media_p);

        tactics_media_v = findViewById(R.id.tactics_media_v);
        team_player_media_v = findViewById(R.id.team_player_media_v);
        advance_skill_media_v = findViewById(R.id.advance_skill_media_v);


        if (Constant.isNetworkAvailable(getApplicationContext())) {
            GetData();
            GetPermission();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }


    }

    public void GetPermission() {
        final ProgressDialog progressDialog = new ProgressDialog(GameActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GetPermission, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetPermission", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONObject OverallPerformance = jsonObject.getJSONObject("Data");
                        permissionModel = new PermissionModel();
                        permissionModel.id = OverallPerformance.getString("id");
                        permissionModel.school_id = OverallPerformance.getString("school_id");
                        permissionModel.class_id = OverallPerformance.getString("class_id");
                        permissionModel.heart_rate = OverallPerformance.getString("heart_rate");
                        permissionModel.game_skill = OverallPerformance.getString("game_skill");

                        permissionModel.attention_to_basics = OverallPerformance.getString("attention_to_basics");
                        permissionModel.rules_of_games = OverallPerformance.getString("rules_of_games");
                        permissionModel.team_tactics = OverallPerformance.getString("team_tactics");
                        permissionModel.team_play = OverallPerformance.getString("team_play");
                        permissionModel.advance_skills = OverallPerformance.getString("advance_skills");

                        if (permissionModel.attention_to_basics.equals("0")) {
                            findViewById(R.id.ll_attention_to).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_attention_to).setVisibility(View.VISIBLE);
                        }

                        if (permissionModel.rules_of_games.equals("0")) {
                            findViewById(R.id.ll_rules_game).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_rules_game).setVisibility(View.VISIBLE);
                        }

                        if (permissionModel.team_tactics.equals("0")) {
                            findViewById(R.id.ll_team_tactics).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_team_tactics).setVisibility(View.VISIBLE);
                        }

                        if (permissionModel.team_play.equals("0")) {
                            findViewById(R.id.ll_team_play).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_team_play).setVisibility(View.VISIBLE);
                        }

                        if (permissionModel.advance_skills.equals("0")) {
                            findViewById(R.id.ll_advance_skill).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_advance_skill).setVisibility(View.VISIBLE);
                        }

                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                params.put("class_id", str_class_id);
                params.put("school_id", str_school_id);
                Log.d("params_permission", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(GameActivity.this);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(GameActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GameSkill, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GameSkillReport_resp", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        /******************************GameSkillReport******************************/
                        if (jsonObject.has("GameSkillReport") && !jsonObject.isNull("GameSkillReport")) {

                            JSONObject LatestHeartRateAnlysis = jsonObject.getJSONObject("GameSkillReport");
                            GameSkillModel primaryModel = new GameSkillModel();
                            primaryModel.id = LatestHeartRateAnlysis.getString("id");
                            primaryModel.first_name = LatestHeartRateAnlysis.getString("first_name");
                            primaryModel.tactics = LatestHeartRateAnlysis.getString("tactics");
                            primaryModel.attention_to_basics = LatestHeartRateAnlysis.getString("attention_to_basics");
                            primaryModel.rules = LatestHeartRateAnlysis.getString("rules");

                            primaryModel.tactics_media = LatestHeartRateAnlysis.getString("tactics_media");
                            primaryModel.tactics_video = LatestHeartRateAnlysis.getString("tactics_video");
                            primaryModel.tactics_media_type = LatestHeartRateAnlysis.getString("tactics_media_type");

                            primaryModel.team_player = LatestHeartRateAnlysis.getString("team_player");
                            primaryModel.team_player_media = LatestHeartRateAnlysis.getString("team_player_media");
                            primaryModel.team_player_video = LatestHeartRateAnlysis.getString("team_player_video");
                            primaryModel.team_player_type = LatestHeartRateAnlysis.getString("team_player_type");

                            primaryModel.advance_skill = LatestHeartRateAnlysis.getString("advance_skill");
                            primaryModel.advance_skill_media = LatestHeartRateAnlysis.getString("advance_skill_media");
                            primaryModel.advance_skill_video = LatestHeartRateAnlysis.getString("advance_skill_video");
                            primaryModel.advance_skill_type = LatestHeartRateAnlysis.getString("advance_skill_type");

                            primaryModel.report = LatestHeartRateAnlysis.getString("report");
                            primaryModel.overall_grade = LatestHeartRateAnlysis.getString("overall_grade");
                            primaryModel.behaviour_observation = LatestHeartRateAnlysis.getString("behaviour_observation");


                            attention_to_basics.setText(primaryModel.attention_to_basics + "\nGrade");
                            rules.setText(primaryModel.rules + "\nGrade");

                            if (primaryModel.advance_skill.equals("null")) {
                                advance_skill.setText("-");
                            } else {
                                advance_skill.setText(primaryModel.advance_skill + "\nGrade");

                            }

                            /*tactics*/
                            if (primaryModel.tactics_video.equals("")) {
                                tactics_media_v.setVisibility(View.GONE);
                            } else {
                                tactics_media_v.setVisibility(View.VISIBLE);
                                tactics_media_v.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_videocam_24));
                                tactics_media_v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Showdialog("5", primaryModel.tactics_video);
                                    }
                                });

                            }
                            if (primaryModel.tactics_media.equals("")) {
                                img_card_1.setVisibility(View.GONE);
                            } else {
                                img_card_1.setVisibility(View.VISIBLE);
                                img_card_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                img_card_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                                        intent.putExtra("url", primaryModel.tactics_media);
                                        intent.putExtra("from", "4");//
                                        startActivity(intent);
                                    }
                                });
                            }


                            tactics.setText(primaryModel.tactics + "\nGrade");

                            String strpdf2 = primaryModel.report;

                            if (!strpdf2.equals("")) {
                                report.setVisibility(View.VISIBLE);
                                ll_pdf.setVisibility(View.VISIBLE);
                            }
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", primaryModel.report);
                                    intent.putExtra("from", "8");

                                    startActivity(intent);

                                }
                            });

                            /*team_player*/
                            if (primaryModel.team_player_video.equals("")) {
                                team_player_media_v.setVisibility(View.GONE);
                            } else {
                                team_player_media_v.setVisibility(View.VISIBLE);
                                team_player_media_v.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_videocam_24));
                                team_player_media_v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Showdialog("5", primaryModel.team_player_video);


                                    }
                                });
                            }
                            if (primaryModel.team_player_media.equals("")) {
                                img_card_2.setVisibility(View.GONE);
                            } else {
                                img_card_2.setVisibility(View.VISIBLE);
                                img_card_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                img_card_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                                        intent.putExtra("url", primaryModel.team_player_media);
                                        intent.putExtra("from", "4");//
                                        startActivity(intent);
                                    }
                                });
                            }


                            team_player.setText(primaryModel.team_player + "\nGrade");

                            /*advance_skill*/
                            Log.d("advance_skill_grade", "onResponse: " + primaryModel.advance_skill);
                            Log.d("advance_skill_type", "onResponse: " + primaryModel.advance_skill_type);
                            Log.d("advance_skill_check", "onResponse: " + primaryModel.advance_skill_media);

                            if (!primaryModel.advance_skill.equals("") && primaryModel.advance_skill_type.equals("0")) {
                                ll_advance_skill.setVisibility(View.VISIBLE);
                            }

                            if (primaryModel.advance_skill_video.equals("")) {
                                advance_skill_media_v.setVisibility(View.GONE);

                            } else {
                                advance_skill_media_v.setVisibility(View.VISIBLE);
                                Glide.with(GameActivity.this).load(R.drawable.ic_baseline_videocam_24).into(advance_skill_media_v);
                                advance_skill_media_v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Showdialog("5", primaryModel.advance_skill_video);

                                    }
                                });

                            }
                            if (primaryModel.advance_skill_media.equals("")) {
                                img_card_3.setVisibility(View.GONE);

                            } else {
                                img_card_3.setVisibility(View.VISIBLE);
                                img_card_3.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                img_card_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
                                        intent.putExtra("url", primaryModel.advance_skill_media);
                                        intent.putExtra("from", "4");//
                                        startActivity(intent);
                                    }
                                });

                            }


                        } else {
                            Log.d("null", "onResponse: ");
                        }


                    } else {
                        team_player_media_v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(GameActivity.this, "Media Not Available", Toast.LENGTH_SHORT).show();
                            }
                        });

                        advance_skill_media_v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(GameActivity.this, "Media Not Available", Toast.LENGTH_SHORT).show();
                            }
                        });
                        tactics_media_v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(GameActivity.this, "Media Not Available", Toast.LENGTH_SHORT).show();
                            }
                        });

                        //Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

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

    void Showdialog(String flag, String url) {
        LayoutInflater li = LayoutInflater.from(GameActivity.this);
        View promptsView = li.inflate(R.layout.demo_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
        alertDialogBuilder.setTitle("Play/Download Video");
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView txt = (TextView) promptsView
                .findViewById(R.id.textView1);
        Log.d("check_post", "Showdialog: " + url);
        Log.d("check", "Showdialog: " + flag);

        // set dialog message
        alertDialogBuilder.setCancelable(true).setNegativeButton("Play", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), VideoDetails.class);
                intent.putExtra("url", url);
                intent.putExtra("from", "5");//
                startActivity(intent);
            }
        });

        alertDialogBuilder.setCancelable(true).setPositiveButton("Download", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ImageFull_Url = (Constant.GameSkills_media + url);
                Uri uri = Uri.parse(ImageFull_Url);
                String url = ImageFull_Url;
                String filename;
                if (url.contains("?")) {
                    filename = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
                } else {
                    filename = url.substring(url.lastIndexOf("/") + 1, url.length());
                }
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);

// set title and description
                request.setTitle("Data Download");
                request.setDescription("Android Data download using DownloadManager.");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(filename);
//set the local destination for download file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        alertDialogBuilder.setCancelable(true).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });
    }

}