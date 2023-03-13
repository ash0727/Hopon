package com.shape.app.Activity.PerformanceCriteria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.shape.app.Activity.ImageListActivity;
import com.shape.app.Activity.PDFDetails;
import com.shape.app.Activity.VideoDetailsList;
import com.shape.app.Adapters.HeartRateAdapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.HeartRateModel;
import com.shape.app.Models.PermissionModel;
import com.shape.app.Models.PrimaryModel;
import com.shape.app.Models.SecondaryModel;
import com.shape.app.R;

import org.json.JSONArray;
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

public class FitnessActivity extends AppCompatActivity {
    //  ProgressBar max_heart_rate, rest_heart_rate, imdb_heart_rate;
    SharedPreferences sharedPreferences;
    String str_userid, str_class_id, str_school_id, intent_grade, str_theme, str_code, str_fname, str_lname, str_pic;
    TextView name, heart_rate_g, st_code, overall_grade, grade_1, grade_2, grade_3, cardiovascular_ability_grade, muscular_ability_grade, agility, flexibility_grdae, balance, power, speed, coordination, reaction;
    ImageView pdf_1, pdf_2, video_prime,     pdf_3, balance_img, balance_vid, img_card, img_mus, vid_mus, flex_vid, flex_img, coo_img, coo_vid, age_img, age_vid;
    ImageView speed_vid, sped_img, power_vid, power_img, reaction_vid, reaction_img;
    ArrayList<HeartRateModel> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    JSONArray cardiovascular_ability_, muscular_ability_, cardiovascular_ability_vi, muscular_ability_vid, flexibilityability_img, flexibilityability_vid;
    JSONArray balance_img_, balance_vid_array, coordination_img_, coordination_vid_, agility_video_, agility_img_, speed_img_, speed_video_, power_img_, power_vid_, reaction_img_, reaction_vid_;
    TextView max_heart_rate_t, rest_heart_rate_t, imdb_heart_rate_t, SPO2_t;
    ImageView new_pdf, profile_image;
    TextView txtnotfound;
    LinearLayout ll_pdf_2, ll_pdf_1;
    PermissionModel permissionModel;
    CardView card_heart_rate, card_primary, card_secondary;
    LinearLayout ll_flexibility, ll_cardio, ll_muscular_ab;
    LinearLayout ll_body_balance, ll_agility, ll_speed, ll_rxn_time, ll_power, ll_coordination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
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
        initTool();
        intent_grade = getIntent().getStringExtra("grade");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout linearLayout = findViewById(R.id.ll_bg);
            RelativeLayout rl_bg = findViewById(R.id.rl_bg);
            linearLayout.setBackgroundColor(Color.parseColor(str_theme));
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }

        ll_flexibility = findViewById(R.id.ll_flexibility);
        ll_cardio = findViewById(R.id.ll_cardio);
        ll_muscular_ab = findViewById(R.id.ll_muscular_ab);

        ll_body_balance = findViewById(R.id.ll_body_balance);
        ll_agility = findViewById(R.id.ll_agility);
        ll_speed = findViewById(R.id.ll_speed);
        ll_rxn_time = findViewById(R.id.ll_rxn_time);
        ll_power = findViewById(R.id.ll_power);
        ll_coordination = findViewById(R.id.ll_coordination);

        ll_pdf_1 = findViewById(R.id.ll_pdf_1);
        ll_pdf_2 = findViewById(R.id.ll_pdf_2);
        overall_grade = findViewById(R.id.overall_grade);
        name = findViewById(R.id.name);
        st_code = findViewById(R.id.st_code);
        name.setText(str_fname + " " + str_lname);
        st_code.setText("(" + str_code + ")");

        eventTabModelArrayList = new ArrayList<>();
        recyclerView_event = findViewById(R.id.recyclerView_event);
        recyclerView_event.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        Log.d("3", "onCreateView: " + eventTabModelArrayList.size());

        cardiovascular_ability_grade = findViewById(R.id.cardiovascular_ability_grade);
        muscular_ability_grade = findViewById(R.id.muscular_ability_grade);
        flexibility_grdae = findViewById(R.id.flexibility_grade);
        profile_image = findViewById(R.id.profile_image);
     //   Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        card_heart_rate = findViewById(R.id.card_heart_rate);
        card_primary = findViewById(R.id.card_primary);
        card_secondary = findViewById(R.id.card_secondary);
        balance = findViewById(R.id.balance);
        coordination = findViewById(R.id.coordination);
        agility = findViewById(R.id.agility);
        speed = findViewById(R.id.speed);
        reaction = findViewById(R.id.reaction);
        power = findViewById(R.id.power);

        grade_1 = findViewById(R.id.grade_1);
        grade_2 = findViewById(R.id.grade_2);
        grade_3 = findViewById(R.id.grade_3);

        pdf_1 = findViewById(R.id.pdf_1);
        pdf_2 = findViewById(R.id.pdf_2);
        pdf_3 = findViewById(R.id.pdf_3);

        max_heart_rate_t = findViewById(R.id.max_heart_rate_t);
        rest_heart_rate_t = findViewById(R.id.rest_heart_rate_t);
        imdb_heart_rate_t = findViewById(R.id.imdb_heart_rate_t);
        SPO2_t = findViewById(R.id.SPO2_t);
        new_pdf = findViewById(R.id.new_pdf);

        heart_rate_g = findViewById(R.id.heart_rate_g);
        video_prime = findViewById(R.id.video_card);
        img_card = findViewById(R.id.img_card);
        balance_img = findViewById(R.id.balance_img);
        balance_vid = findViewById(R.id.balance_vid);
        img_mus = findViewById(R.id.img_mus);
        vid_mus = findViewById(R.id.vid_mus);
        flex_vid = findViewById(R.id.flex_vid);
        flex_img = findViewById(R.id.flex_img);
        coo_img = findViewById(R.id.coo_img);
        coo_vid = findViewById(R.id.coo_vid);
        age_img = findViewById(R.id.age_img);
        age_vid = findViewById(R.id.age_vid);
        sped_img = findViewById(R.id.spped_img);
        speed_vid = findViewById(R.id.speed_vid);
        power_vid = findViewById(R.id.power_vid);
        power_img = findViewById(R.id.power_img);
        reaction_vid = findViewById(R.id.reaction_vid);
        reaction_img = findViewById(R.id.reaction_img);
        Log.d("data", "onCreate: " + intent_grade);
        if (intent_grade == null || intent_grade.equals("")) {
            overall_grade.setText("-");
        } else {
            overall_grade.setText(intent_grade + "\nGrade");
        }

//        primary_rv = findViewById(R.id.primary_rv);
//        // primary_rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//        primary_rv.setLayoutManager(new GridLayoutManager(this, 2));
        if (Constant.isNetworkAvailable(getApplicationContext())) {
            GetData();
            GetPermission();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
        pdf_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportDailog();
            }
        });

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

    private void initTool() {
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //code otp
    //icon
    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(FitnessActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Primary_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_conp", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String heart_rate_analysis_grade = jsonObject.getString("heart_rate_analysis_grade");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("HeartRateAnlysis");

                        if (jsonArrayvideo != null) {
                            for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                HeartRateModel heartRateModel = new HeartRateModel();
                                JSONObject HeartRateAnlysis = jsonArrayvideo.getJSONObject(i);

                                heartRateModel.id = HeartRateAnlysis.getString("id");
                                heartRateModel.first_name = HeartRateAnlysis.getString("first_name");
                                heartRateModel.last_name = HeartRateAnlysis.getString("last_name");
                                heartRateModel.maximum_heart_rate = HeartRateAnlysis.getString("maximum_heart_rate");
                                heartRateModel.resting_heart_rate = HeartRateAnlysis.getString("resting_heart_rate");
                                heartRateModel.immediate_heart_rate = HeartRateAnlysis.getString("immediate_heart_rate");
                                heartRateModel.heart_rate_analysis = HeartRateAnlysis.getString("heart_rate_analysis");
                                heartRateModel.SPO2 = HeartRateAnlysis.getString("SPO2");
                                heartRateModel.report = HeartRateAnlysis.getString("report");
                                heartRateModel.added_date = HeartRateAnlysis.getString("added_date");

                                eventTabModelArrayList.add(heartRateModel);

                            }
                            recyclerView_event.setAdapter(new HeartRateAdapter(eventTabModelArrayList, (AppCompatActivity) FitnessActivity.this));

                          /*  max_heart_rate.setProgress(Integer.parseInt(heartRateModel.maximum_heart_rate));
                            rest_heart_rate.setProgress(Integer.parseInt(heartRateModel.resting_heart_rate));
                            imdb_heart_rate.setProgress(Integer.parseInt(heartRateModel.immediate_heart_rate));
                            */
                            heart_rate_g.setText(heart_rate_analysis_grade + "\nGrade");
                        }
                        /******************************LatestHeartRateAnlysis******************************/
                        if (jsonObject.has("LatestHeartRateAnlysis") && !jsonObject.isNull("LatestHeartRateAnlysis")) {

                            JSONObject LatestHeartRateAnlysis = jsonObject.getJSONObject("LatestHeartRateAnlysis");

                            HeartRateModel primaryModel = new HeartRateModel();
                            primaryModel.id = LatestHeartRateAnlysis.getString("id");
                            primaryModel.first_name = LatestHeartRateAnlysis.getString("first_name");
                            primaryModel.last_name = LatestHeartRateAnlysis.getString("last_name");
                            primaryModel.report = LatestHeartRateAnlysis.getString("report");
                            primaryModel.maximum_heart_rate = LatestHeartRateAnlysis.getString("maximum_heart_rate");
                            primaryModel.resting_heart_rate = LatestHeartRateAnlysis.getString("resting_heart_rate");
                            primaryModel.immediate_heart_rate = LatestHeartRateAnlysis.getString("immediate_heart_rate");
                            primaryModel.heart_rate_analysis = LatestHeartRateAnlysis.getString("heart_rate_analysis");
                            primaryModel.SPO2 = LatestHeartRateAnlysis.getString("SPO2");


                            SPO2_t.setText(primaryModel.SPO2 + "%");
                            imdb_heart_rate_t.setText(primaryModel.immediate_heart_rate + " (BPM)");
                            rest_heart_rate_t.setText(primaryModel.resting_heart_rate + " (BPM)");
                            max_heart_rate_t.setText(primaryModel.maximum_heart_rate + " (BPM)");

                            String strpdf2 = primaryModel.report;

                            if (!strpdf2.equals("")) {
                                 new_pdf.setVisibility(View.VISIBLE);
                            }
                            new_pdf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", primaryModel.report);
                                    intent.putExtra("from", "1");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d("null", "onResponse: ");
                        }
                        /******************************Primary******************************/
                        if (jsonObject.has("PrimaryFitnessComponents") && !jsonObject.isNull("PrimaryFitnessComponents")) {

                            JSONObject PrimaryFitnessComponents = jsonObject.getJSONObject("PrimaryFitnessComponents");

                            PrimaryModel primaryModel = new PrimaryModel();
                            primaryModel.id = PrimaryFitnessComponents.getString("id");
                            primaryModel.first_name = PrimaryFitnessComponents.getString("first_name");
                            primaryModel.last_name = PrimaryFitnessComponents.getString("last_name");
                            primaryModel.cardiovascular_ability = PrimaryFitnessComponents.getString("cardiovascular_ability");
                            primaryModel.muscular_ability = PrimaryFitnessComponents.getString("muscular_ability");
                            primaryModel.flexibility = PrimaryFitnessComponents.getString("flexibility");
                            primaryModel.grade = PrimaryFitnessComponents.getString("grade");
                            primaryModel.report = PrimaryFitnessComponents.getString("report");
                            primaryModel.flexibility_video = PrimaryFitnessComponents.getString("flexibility_video");

                            ArrayList<String> cardiovascularlistdata = new ArrayList<String>();
                            ArrayList<String> muscular_abilitylistdata = new ArrayList<String>();
                            ArrayList<String> muscular_ability_vidlistdata = new ArrayList<String>();
                            ArrayList<String> cardiovascular_ability_vilistdata = new ArrayList<String>();

                            try {
                                cardiovascular_ability_grade.setText(primaryModel.cardiovascular_ability + "\nGrade");
                                cardiovascular_ability_ = PrimaryFitnessComponents.optJSONArray("cardiovascular_ability_img");
                                if (cardiovascular_ability_ != null) {
                                    img_card.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < cardiovascular_ability_.length(); i++) {
                                        cardiovascularlistdata.add(cardiovascular_ability_.getString(i));
                                        Log.d("side_1", "Responce: " + cardiovascular_ability_.getString(i));
                                    }
                                    img_card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                            intent.putExtra("url", primaryModel.media);
                                            intent.putExtra("from", "1");//
                                            intent.putExtra("data", "PrimaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", cardiovascularlistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    img_card.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(img_card);
                                    img_card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageToast();
                                        }
                                    });

                                }


                                /******************************cardiovascular_ability_vi******************************/
                                cardiovascular_ability_vi = PrimaryFitnessComponents.optJSONArray("cardiovascular_ability_video");
                                if (cardiovascular_ability_vi != null) {
                                    video_prime.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < cardiovascular_ability_vi.length(); i++) {
                                        cardiovascular_ability_vilistdata.add(cardiovascular_ability_vi.getString(i));
                                    }
                                    video_prime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                            intent.putExtra("url", primaryModel.media);
                                            intent.putExtra("from", "2");//
                                            intent.putExtra("data", "PrimaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", cardiovascular_ability_vilistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    video_prime.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(video_prime);
                                    video_prime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            VideoToast();
                                        }
                                    });

                                }

                                /******************************muscular_ability_img******************************/
                                muscular_ability_grade.setText(primaryModel.muscular_ability + "\nGrade");

                                muscular_ability_ = PrimaryFitnessComponents.optJSONArray("muscular_ability_img");
                                if (muscular_ability_ != null) {
                                    img_mus.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < muscular_ability_.length(); i++) {
                                        muscular_abilitylistdata.add(muscular_ability_.getString(i));
                                        Log.d("smuscular_ability__1", "Responce: " + muscular_ability_.getString(i));
                                    }

                                    img_mus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                            intent.putExtra("url", primaryModel.media);
                                            intent.putExtra("from", "1");//
                                            intent.putExtra("data", "PrimaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", muscular_abilitylistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    img_mus.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(img_mus);
                                    img_mus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageToast();
                                        }
                                    });

                                }
                                /******************************muscular_ability_vid******************************/

                                muscular_ability_vid = PrimaryFitnessComponents.optJSONArray("muscular_ability_video");
                                if (muscular_ability_vid != null) {
                                    vid_mus.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < muscular_ability_vid.length(); i++) {
                                        muscular_ability_vidlistdata.add(muscular_ability_vid.getString(i));
                                        Log.d("smuscular_ability__1", "Responce: " + muscular_ability_vid.getString(i));
                                    }
                                    vid_mus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                            intent.putExtra("url", primaryModel.media);
                                            intent.putExtra("from", "1");//
                                            intent.putExtra("data", "PrimaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", muscular_ability_vidlistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    vid_mus.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(vid_mus);
                                    vid_mus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            VideoToast();
                                        }
                                    });

                                }
                                /******************************flexibility_ability_img******************************/
                                ArrayList<String> flexibilityabilityimglistdata = new ArrayList<String>();

                                flexibilityability_img = PrimaryFitnessComponents.optJSONArray("flexibility_ability_img");
                                if (flexibilityability_img != null) {
                                    flex_img.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < flexibilityability_img.length(); i++) {
                                        flexibilityabilityimglistdata.add(flexibilityability_img.getString(i));
                                        Log.d("smuscular_ability__1", "Responce: " + flexibilityability_img.getString(i));
                                    }
                                    flex_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                            intent.putExtra("url", primaryModel.media);
                                            intent.putExtra("from", "1");//
                                            intent.putExtra("data", "PrimaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", flexibilityabilityimglistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    flex_img.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(flex_img);
                                    flex_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageToast();
                                        }
                                    });


                                }
                                /******************************flexibility_ability_img******************************/
                                ArrayList<String> flexibilityvideolistdata = new ArrayList<String>();

                                flexibilityability_vid = PrimaryFitnessComponents.optJSONArray("flexibility_video");
                                if (flexibilityability_vid != null) {
                                    flex_vid.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < flexibilityability_vid.length(); i++) {
                                        flexibilityvideolistdata.add(flexibilityability_vid.getString(i));
                                        Log.d("smuscular_ability__1", "Responce: " + flexibilityability_vid.getString(i));
                                    }
                                    flex_vid.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                            intent.putExtra("url", primaryModel.media);
                                            intent.putExtra("from", "1");//
                                            intent.putExtra("data", "PrimaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", flexibilityvideolistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    flex_vid.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(flex_vid);
                                    flex_vid.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            VideoToast();
                                        }
                                    });


                                }
                                /************************************************************/


                            } catch (Exception e) {
                                Log.e("Exception", "PRIMARY: " + e);
                            }




                            /*if (primaryModel.type.equals("0")) {
                                video_prime.setVisibility(View.GONE);
                                img_card.setVisibility(View.GONE);
                            }*/


                            flexibility_grdae.setText(primaryModel.flexibility + "\nGrade");

                            grade_2.setText(primaryModel.grade + "\nGrade");
                            String strpdf2 = primaryModel.report;
                            if (!strpdf2.equals("")) {
                                pdf_2.setVisibility(View.VISIBLE);
                                ll_pdf_2.setVisibility(View.VISIBLE);
                            }
                            pdf_2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", primaryModel.report);
                                    intent.putExtra("from", "2");

                                    startActivity(intent);

                                }
                            });
                             /* String[] myArray = primaryModel.muscular_ability_img.split(",");
                            System.out.println("Contents of the array ::"+ Arrays.toString(myArray));
                            List <String> myList = Arrays.asList(myArray);
                            Log.d("muscular_ability_img", "Responce: " + myList);*/
                        } else {
                            Log.d("null", "onResponse: ");
                        }
                        /******************************Secondary******************************/
                        if (jsonObject.has("SecondaryFitnessComponents") && !jsonObject.isNull("SecondaryFitnessComponents")) {

                            JSONObject SecondaryFitnessComponents = jsonObject.getJSONObject("SecondaryFitnessComponents");

                            SecondaryModel secondaryModel = new SecondaryModel();
                            secondaryModel.id = SecondaryFitnessComponents.getString("id");
                            secondaryModel.first_name = SecondaryFitnessComponents.getString("first_name");
                            secondaryModel.last_name = SecondaryFitnessComponents.getString("last_name");
                            secondaryModel.coordination = SecondaryFitnessComponents.getString("coordination");
                            secondaryModel.agility = SecondaryFitnessComponents.getString("agility");
                            secondaryModel.balance = SecondaryFitnessComponents.getString("balance");
                            secondaryModel.speed = SecondaryFitnessComponents.getString("speed");
                            secondaryModel.reaction = SecondaryFitnessComponents.getString("reaction");
                            secondaryModel.power = SecondaryFitnessComponents.getString("power");
                            secondaryModel.grade = SecondaryFitnessComponents.getString("grade");
                            secondaryModel.report = SecondaryFitnessComponents.getString("report");

                            balance.setText(secondaryModel.balance + "\nGrade");
                            coordination.setText(secondaryModel.coordination + "\nGrade");
                            agility.setText(secondaryModel.agility + "\nGrade");
                            speed.setText(secondaryModel.speed + "\nGrade");
                            reaction.setText(secondaryModel.reaction + "\nGrade");
                            power.setText(secondaryModel.power + "\nGrade");

                            ArrayList<String> balance_img_listdata = new ArrayList<String>();
                            ArrayList<String> balance_vid_vilistdata = new ArrayList<String>();
                            ArrayList<String> coordination_img_listdata = new ArrayList<String>();
                            ArrayList<String> coordination_vid_listdata = new ArrayList<String>();

                            ArrayList<String> agility_img__listdata = new ArrayList<String>();
                            ArrayList<String> agility_vid_listdata = new ArrayList<String>();

                            ArrayList<String> reaction_img_listdata = new ArrayList<String>();
                            ArrayList<String> reaction_vid_listdata = new ArrayList<String>();

                            ArrayList<String> speed_img_listdata = new ArrayList<String>();
                            ArrayList<String> speed_vid_listdata = new ArrayList<String>();

                            ArrayList<String> power_img_listdata = new ArrayList<String>();
                            ArrayList<String> power_vid_listdata = new ArrayList<String>();

                            try {
                                balance_img_ = SecondaryFitnessComponents.optJSONArray("balance_img");
                                if (balance_img_ != null) {
                                    balance_img.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < balance_img_.length(); i++) {
                                        balance_img_listdata.add(balance_img_.getString(i));
                                        Log.d("side_1", "Responce: " + balance_img_.getString(i));
                                    }
                                    balance_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                            intent.putExtra("data", "SecondaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", balance_img_listdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    balance_img.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(balance_img);

                                    balance_img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ImageToast();
                                        }
                                    });

                                }


                                /******************************balance_video******************************/
                                balance_vid_array = SecondaryFitnessComponents.optJSONArray("balance_video");
                                if (balance_vid_array != null) {
                                    balance_vid.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < balance_vid_array.length(); i++) {
                                        balance_vid_vilistdata.add(balance_vid_array.getString(i));
                                        Log.d("smuscular_ability__2", "Responce: " + balance_vid_array.getString(i));
                                    }
                                    balance_vid.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                            intent.putExtra("from", "2");//
                                            intent.putExtra("data", "SecondaryFitnessComponents");//
                                            intent.putStringArrayListExtra("array", balance_vid_vilistdata);//
                                            startActivity(intent);
                                        }
                                    });

                                } else {
                                    balance_vid.setVisibility(View.GONE);
                                    Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(balance_vid);
                                    balance_vid.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            VideoToast();
                                        }
                                    });

                                }

                                /******************************coordination_img******************************/
                                try {
                                    coordination_img_ = SecondaryFitnessComponents.optJSONArray("coordination_img");

                                    if (coordination_img_ != null) {
                                        coo_img.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < coordination_img_.length(); i++) {
                                            coordination_img_listdata.add(coordination_img_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + coordination_img_.getString(i));
                                        }
                                        coo_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", coordination_img_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        coo_img.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(coo_img);
                                        coo_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ImageToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("coordination_img_", "onResponse: " + e.toString());
                                }

                                /******************************coordination_video******************************/

                                try {
                                    coordination_vid_ = SecondaryFitnessComponents.optJSONArray("coordination_video");
                                    if (coordination_vid_ != null) {
                                        coo_vid.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < coordination_vid_.length(); i++) {
                                            coordination_vid_listdata.add(coordination_vid_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + coordination_vid_.getString(i));
                                        }
                                        coo_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", coordination_vid_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        coo_vid.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(coo_vid);
                                        coo_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                VideoToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("coordination_vid_", "onResponse: " + e.toString());
                                }
                                /******************************agility_img_******************************/

                                try {

                                    agility_img_ = SecondaryFitnessComponents.optJSONArray("agility_img");
                                    if (agility_img_ != null) {
                                        age_img.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < agility_img_.length(); i++) {
                                            agility_img__listdata.add(agility_img_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + agility_img_.getString(i));
                                        }
                                        age_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", agility_img__listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        age_img.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(age_img);
                                        age_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ImageToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("age_img", "onResponse: " + e.toString());
                                }
                                /******************************agility_video******************************/
                                try {


                                    agility_video_ = SecondaryFitnessComponents.optJSONArray("agility_video");
                                    if (agility_video_ != null) {
                                        age_vid.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < agility_video_.length(); i++) {
                                            agility_vid_listdata.add(agility_video_.getString(i));
                                            Log.d("smuscular_ability__v1", "Responce: " + agility_video_.getString(i));
                                        }
                                        age_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", agility_vid_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        age_vid.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(age_vid);
                                        age_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                VideoToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("age_vid", "onResponse: " + e.toString());
                                }
                                /******************************speed_img******************************/
                                try {
                                    speed_img_ = SecondaryFitnessComponents.optJSONArray("speed_img");

                                    if (speed_img_ != null) {
                                        sped_img.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < speed_img_.length(); i++) {
                                            speed_img_listdata.add(speed_img_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + speed_img_.getString(i));
                                        }
                                        sped_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", speed_img_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        sped_img.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(sped_img);

                                        sped_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ImageToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("sped_img", "onResponse: " + e.toString());
                                }
                                /******************************speed_video******************************/
                                try {
                                    speed_video_ = SecondaryFitnessComponents.optJSONArray("speed_video");
                                    if (speed_video_ != null) {
                                        speed_vid.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < speed_video_.length(); i++) {
                                            speed_vid_listdata.add(speed_video_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + speed_video_.getString(i));
                                        }
                                        speed_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", speed_vid_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        speed_vid.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(speed_vid);
                                        speed_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                VideoToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("speed_vid", "onResponse: " + e.toString());
                                }
                                /******************************reaction_img******************************/
                                try {
                                    reaction_img_ = SecondaryFitnessComponents.optJSONArray("reaction_img");

                                    if (reaction_img_ != null) {
                                        reaction_img.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < reaction_img_.length(); i++) {
                                            reaction_img_listdata.add(reaction_img_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + reaction_img_.getString(i));
                                        }
                                        reaction_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", reaction_img_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        reaction_img.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(reaction_img);
                                        reaction_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ImageToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("reaction_img", "onResponse: " + e.toString());
                                }


                                /******************************reaction_video******************************/
                                try {

                                    reaction_vid_ = SecondaryFitnessComponents.optJSONArray("reaction_video");
                                    if (reaction_vid_ != null) {
                                        reaction_vid.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < reaction_vid_.length(); i++) {
                                            reaction_vid_listdata.add(reaction_vid_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + reaction_vid_.getString(i));
                                        }
                                        reaction_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", reaction_vid_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        reaction_vid.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(reaction_vid);
                                        reaction_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                VideoToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("reaction_vic", "onResponse: " + e.toString());
                                }
                                /******************************power_img******************************/
                                try {
                                    power_img_ = SecondaryFitnessComponents.optJSONArray("power_img");

                                    if (power_img_ != null) {
                                        power_img.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < power_img_.length(); i++) {
                                            power_img_listdata.add(power_img_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + power_img_.getString(i));
                                        }
                                        power_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", power_img_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        power_img.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_image_24).into(power_img);
                                        power_img.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ImageToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("power_img", "onResponse: " + e.toString());
                                }

                                /******************************power_video******************************/
                                try {
                                    power_vid_ = SecondaryFitnessComponents.optJSONArray("power_video");
                                    if (power_vid_ != null) {
                                        power_vid.setVisibility(View.VISIBLE);
                                        for (int i = 0; i < power_vid_.length(); i++) {
                                            power_vid_listdata.add(power_vid_.getString(i));
                                            Log.d("smuscular_ability__1", "Responce: " + power_vid_.getString(i));
                                        }
                                        power_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getApplicationContext(), VideoDetailsList.class);
                                                intent.putExtra("from", "1");//
                                                intent.putExtra("data", "SecondaryFitnessComponents");//
                                                intent.putStringArrayListExtra("array", power_vid_listdata);//
                                                startActivity(intent);
                                            }
                                        });

                                    } else {
                                        power_vid.setVisibility(View.GONE);
                                        Glide.with(FitnessActivity.this).load(R.drawable.ic_grey_videocam_24).into(power_vid);
                                        power_vid.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                VideoToast();
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.e("power_vid", "onResponse: " + e.toString());
                                }


                                /************************************************************/

                            } catch (Exception e) {
                                Log.e("Exception", "SECONDARY: " + e);
                            }


                            String strpdf3 = secondaryModel.report;
                            if (!strpdf3.equals("")) {
                                pdf_3.setVisibility(View.VISIBLE);
                                ll_pdf_1.setVisibility(View.VISIBLE);
                            }
                            grade_3.setText(secondaryModel.grade + "\nGrade");
                            pdf_3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", secondaryModel.report);
                                    intent.putExtra("from", "3");

                                    startActivity(intent);

                                }
                            });

                        }
                    } else {
                        // Toast.makeText(FitnessActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void VideoToast() {
        Toast.makeText(FitnessActivity.this, "Media Not Available", Toast.LENGTH_SHORT).show();

    }

    private void ImageToast() {
        Toast.makeText(FitnessActivity.this, "Media Not Available", Toast.LENGTH_SHORT).show();

    }

    public void showReportDailog() {
        // custom dialog
        final Dialog dialog = new Dialog(FitnessActivity.this,
                android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_gift);
        dialog.setTitle("Title...");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ((ImageView) dialog.findViewById(R.id.imgclosetop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtnotfound = dialog.findViewById(R.id.txtnotfound);
        TextView txttitle = dialog.findViewById(R.id.txttitle);

        txttitle.setText("Report");

        RecyclerView recyclerView_gifts = dialog.findViewById(R.id.recylerview_gifts);
        //  recyclerView_gifts.setLayoutManager(new LinearLayoutManager(FitnessActivity.this));
        recyclerView_gifts.setLayoutManager(new GridLayoutManager(FitnessActivity.this, 2));

        UsersRedeemList(recyclerView_gifts, dialog);

        dialog.show();
    }

    private void UsersRedeemList(final RecyclerView recyclerView, final Dialog dialog) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<HeartRateModel> redeemModelArrayList = new ArrayList();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Primary_List,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        // progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Log.d("response", "onResponse: " + response);

                            if (code.equalsIgnoreCase("200")) {
                                JSONArray jsonArrayvideo = jsonObject.getJSONArray("HeartRateAnlysis");

                                if (jsonArrayvideo != null) {
                                    for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                        HeartRateModel heartRateModel = new HeartRateModel();
                                        JSONObject HeartRateAnlysis = jsonArrayvideo.getJSONObject(i);

                                        heartRateModel.id = HeartRateAnlysis.getString("id");
                                        heartRateModel.first_name = HeartRateAnlysis.getString("first_name");
                                        heartRateModel.last_name = HeartRateAnlysis.getString("last_name");
                                        heartRateModel.maximum_heart_rate = HeartRateAnlysis.getString("maximum_heart_rate");
                                        heartRateModel.resting_heart_rate = HeartRateAnlysis.getString("resting_heart_rate");
                                        heartRateModel.immediate_heart_rate = HeartRateAnlysis.getString("immediate_heart_rate");
                                        heartRateModel.heart_rate_analysis = HeartRateAnlysis.getString("heart_rate_analysis");
                                        heartRateModel.SPO2 = HeartRateAnlysis.getString("SPO2");
                                        heartRateModel.report = HeartRateAnlysis.getString("report");
                                        heartRateModel.added_date = HeartRateAnlysis.getString("added_date");
                                        redeemModelArrayList.add(heartRateModel);

                                    }
                                    HeartRateAdapter userWinnerAdapter = new HeartRateAdapter(redeemModelArrayList, (AppCompatActivity) FitnessActivity.this);
                                    recyclerView.setAdapter(userWinnerAdapter);
                                }


                            } else {
                                if (jsonObject.has("message")) {

                                    Toast.makeText(FitnessActivity.this, message,
                                            Toast.LENGTH_LONG).show();
                                    txtnotfound.setVisibility(View.VISIBLE);

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtnotfound.setVisibility(View.VISIBLE);

                        }

                        rlt_progress.setVisibility(View.GONE);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                Toast.makeText(FitnessActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                txtnotfound.setVisibility(View.VISIBLE);
                rlt_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Constant.Token);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }

    public void GetPermission() {
        final ProgressDialog progressDialog = new ProgressDialog(FitnessActivity.this);
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
                        permissionModel.primary_fitness = OverallPerformance.getString("primary_fitness");
                        permissionModel.secondary_fitness = OverallPerformance.getString("secondary_fitness");
                        permissionModel.bmi_report = OverallPerformance.getString("bmi_report");
                        permissionModel.cbc_level = OverallPerformance.getString("cbc_level");
                        permissionModel.musculoskeletal_report = OverallPerformance.getString("musculoskeletal_report");
                        permissionModel.physical_education = OverallPerformance.getString("physical_education");
                        permissionModel.game_skill = OverallPerformance.getString("game_skill");
                        permissionModel.mental_skill = OverallPerformance.getString("mental_skill");
                        permissionModel.relaxed_awareness = OverallPerformance.getString("relaxed_awareness");
                        permissionModel.achievement = OverallPerformance.getString("achievement");
                        permissionModel.overall_performance = OverallPerformance.getString("overall_performance");
                        // permissionModel.health_performance = OverallPerformance.getString("health_perfomance");
                        permissionModel.fitness = OverallPerformance.getString("fitness");

                        permissionModel.cardiovascular_ability = OverallPerformance.getString("cardiovascular_ability");
                        permissionModel.muscular_ability = OverallPerformance.getString("muscular_ability");
                        permissionModel.flexibility = OverallPerformance.getString("flexibility");

                        permissionModel.body_balance = OverallPerformance.getString("body_balance");
                        permissionModel.agility = OverallPerformance.getString("agility");
                        permissionModel.speed = OverallPerformance.getString("speed");
                        permissionModel.reaction_time = OverallPerformance.getString("reaction_time");
                        permissionModel.power = OverallPerformance.getString("power");
                        permissionModel.coordination = OverallPerformance.getString("coordination");


                        if (permissionModel.heart_rate.equals("0")) {
                            card_heart_rate.setVisibility(View.GONE);
                        } else {
                            card_heart_rate.setVisibility(View.VISIBLE);
                        }

                        /*******************************Primary****************************/
                        if (permissionModel.primary_fitness.equals("0")) {
                            card_primary.setVisibility(View.GONE);
                        } else {
                            card_primary.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.cardiovascular_ability.equals("0")) {
                            ll_cardio.setVisibility(View.GONE);
                        } else {
                            ll_cardio.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.muscular_ability.equals("0")) {
                            ll_muscular_ab.setVisibility(View.GONE);
                        } else {
                            ll_muscular_ab.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.flexibility.equals("0")) {
                            ll_flexibility.setVisibility(View.GONE);
                        } else {
                            ll_flexibility.setVisibility(View.VISIBLE);
                        }
                        /*******************************secondary_fitness****************************/
                        if (permissionModel.secondary_fitness.equals("0")) {
                            card_secondary.setVisibility(View.GONE);
                        } else {
                            card_secondary.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.body_balance.equals("0")) {
                            ll_body_balance.setVisibility(View.GONE);
                        } else {
                            ll_body_balance.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.agility.equals("0")) {
                            ll_agility.setVisibility(View.GONE);
                        } else {
                            ll_agility.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.speed.equals("0")) {
                            ll_speed.setVisibility(View.GONE);
                        } else {
                            ll_speed.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.reaction_time.equals("0")) {
                            ll_rxn_time.setVisibility(View.GONE);
                        } else {
                            ll_rxn_time.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.power.equals("0")) {
                            ll_power.setVisibility(View.GONE);
                        } else {
                            ll_power.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.coordination.equals("0")) {
                            ll_coordination.setVisibility(View.GONE);
                        } else {
                            ll_coordination.setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(FitnessActivity.this);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }


}