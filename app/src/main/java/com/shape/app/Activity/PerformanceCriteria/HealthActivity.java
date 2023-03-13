package com.shape.app.Activity.PerformanceCriteria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.shape.app.Activity.PDFDetails;
import com.shape.app.Adapters.BMI_Adapter;
import com.shape.app.Adapters.CBCAdapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.BMI_Model;
import com.shape.app.Models.CBC_Model;
import com.shape.app.Models.Muscuskelotal_Model;
import com.shape.app.Models.PermissionModel;
import com.shape.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class HealthActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String str_userid, intent_grade, str_theme, str_code, str_fname, str_lname, str_school_id, str_class_id;
    TextView overall_grade, grade_1, grade_2, grade_3, name, height, weight, code_st;
    ImageView pdf_1, pdf_2, video_prime, pdf_3, video_prime_1;
    TextView report_analysis, upper_extremity, spine, lower_extremity;
    ArrayList<BMI_Model> eventTabModelArrayList = new ArrayList<>();
    ArrayList<CBC_Model> cbcTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event, recyclerView_cbc;

    TextView recommendation_bmi, open_dial_r, open_dial, open_dial_2, open_dial_5, open_dial_3, open_dial_4, remark, open_dial_6, txtnotfound;
    ImageView profile_image, img_file;
    String str_pic;
    LinearLayout ll_pdf, ll_pdf_2;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");

    TextView bmi_height, bmi_height_n, bmi_weight, bmi_weight_n, bmi_result_, bmi_result_n, remark_analasis, remark_new, rp_date, report_data, your_bmi;
    TextView box_1, box_2, box_3;
    LinearLayout ll_bmi, ll_bar_data, bmi_rec_data;
    RelativeLayout rl_all_data;
    LinearLayout box_4, box_5, box_6;
    TextView your_bmi_2, your_bmi_3;
    boolean clicked = false;
    PermissionModel permissionModel;
    CardView card_bmi, card_cbc, card_musc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
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
        eventTabModelArrayList = new ArrayList<>();
        cbcTabModelArrayList = new ArrayList<>();
        recyclerView_event = findViewById(R.id.recyclerView_event);
        recyclerView_cbc = findViewById(R.id.recyclerView_cbc);
        recyclerView_event.setLayoutManager((new GridLayoutManager(getApplicationContext(), 2)));

        // recyclerView_event.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_cbc.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        Log.d("3", "onCreateView: " + eventTabModelArrayList.size());
        profile_image = findViewById(R.id.profile_image);
        img_file = findViewById(R.id.img_file);
      //  Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        card_bmi = findViewById(R.id.card_bmi);
        card_cbc = findViewById(R.id.card_cbc);
        card_musc = findViewById(R.id.card_muscu);

        bmi_rec_data = findViewById(R.id.bmi_rec_data);
        ll_bar_data = findViewById(R.id.ll_bar_data);
        ll_bmi = findViewById(R.id.ll_bmi);
        rl_all_data = findViewById(R.id.rl_all_data);
        box_1 = findViewById(R.id.box_1);
        box_2 = findViewById(R.id.box_2);
        box_3 = findViewById(R.id.box_3);

        box_4 = findViewById(R.id.box_4);
        box_5 = findViewById(R.id.box_5);
        box_6 = findViewById(R.id.box_6);

        your_bmi_2 = findViewById(R.id.your_bmi_2);
        your_bmi_3 = findViewById(R.id.your_bmi_3);
        your_bmi = findViewById(R.id.your_bmi);
        report_data = findViewById(R.id.report_data);
        rp_date = findViewById(R.id.rp_date);
        bmi_height = findViewById(R.id.bmi_height);
        bmi_height_n = findViewById(R.id.bmi_height_n);
        bmi_weight = findViewById(R.id.bmi_weight);
        bmi_weight_n = findViewById(R.id.bmi_weight_n);
        bmi_result_ = findViewById(R.id.bmi_result_);
        bmi_result_n = findViewById(R.id.bmi_result_n);
        remark_analasis = findViewById(R.id.remark_ana);
        remark_new = findViewById(R.id.remark_new);

        open_dial_r = findViewById(R.id.open_dial_r);
        open_dial_3 = findViewById(R.id.open_dial_3);
        open_dial_4 = findViewById(R.id.open_dial_4);
        open_dial_6 = findViewById(R.id.open_dial_6);
        open_dial_5 = findViewById(R.id.open_dial_5);
        open_dial_2 = findViewById(R.id.open_dial_2);
        open_dial = findViewById(R.id.open_dial);
        remark = findViewById(R.id.remark);
        name = findViewById(R.id.name);
        code_st = findViewById(R.id.code_st);
        recommendation_bmi = findViewById(R.id.recommendation_bmi);
        upper_extremity = findViewById(R.id.upper_extremity);
        spine = findViewById(R.id.spine);
        lower_extremity = findViewById(R.id.lower_extremity);

        overall_grade = findViewById(R.id.overall_grade);
        report_analysis = findViewById(R.id.report_analysis);


        grade_1 = findViewById(R.id.grade_1);
        grade_2 = findViewById(R.id.grade_2);
        grade_3 = findViewById(R.id.grade_3);

        pdf_1 = findViewById(R.id.pdf_1);
        pdf_2 = findViewById(R.id.pdf_2);
        ll_pdf = findViewById(R.id.ll_pdf);
        ll_pdf_2 = findViewById(R.id.ll_pdf_2);

        if (intent_grade == null || intent_grade.equals("")) {
            overall_grade.setText("-");
        } else {
            overall_grade.setText(intent_grade);
        }


        name.setText(str_fname + " " + str_lname);
        code_st.setText("(" + str_code + ")");
        if (Constant.isNetworkAvailable(getApplicationContext())) {
            GetData();
            GetPermission();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }

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

    public void GetPermission() {
        final ProgressDialog progressDialog = new ProgressDialog(HealthActivity.this);
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
                        permissionModel.fitness = OverallPerformance.getString("fitness");

                        /*0- hide 1- show */
                        if (permissionModel.bmi_report.equals("0")) {
                            card_bmi.setVisibility(View.GONE);
                        } else {
                            card_bmi.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.musculoskeletal_report.equals("0")) {
                            card_musc.setVisibility(View.GONE);
                        } else {
                            card_musc.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.cbc_level.equals("0")) {
                            card_cbc.setVisibility(View.GONE);
                        } else {
                            card_cbc.setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(HealthActivity.this);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(HealthActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.HealthReport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BMIReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    img_file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (code.equals("200")) {
                                recyclerView_event.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(HealthActivity.this, "No BMI reports are added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    if (code.equals("200")) {
                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("BMIReport");

                        if (jsonArrayvideo != null) {
                            for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                BMI_Model bmi_model = new BMI_Model();
                                JSONObject BMIReport = jsonArrayvideo.getJSONObject(i);

                                bmi_model.id = BMIReport.getString("id");
                                bmi_model.first_name = BMIReport.getString("first_name");
                                bmi_model.student_id = BMIReport.getString("student_id");
                                bmi_model.age = BMIReport.getString("age");
                                bmi_model.height = BMIReport.getString("height");
                                bmi_model.weight = BMIReport.getString("weight");
                                bmi_model.result = BMIReport.getString("result");
                                bmi_model.normal_range = BMIReport.getString("normal_range");
                                bmi_model.grade = BMIReport.getString("grade");
                                bmi_model.remark = BMIReport.getString("remark");
                                bmi_model.report_date = BMIReport.getString("report_date");
                                eventTabModelArrayList.add(bmi_model);
                            }
                            recyclerView_event.setAdapter(new BMI_Adapter(eventTabModelArrayList, (AppCompatActivity) HealthActivity.this));


                        } else {
                            Log.d("null", "onResponse: ");
                        }

                        /******************************LatestBMIReport******************************/

                        if (jsonObject.has("LatestBMIReport") && !jsonObject.isNull("LatestBMIReport")) {
                            JSONObject LatestBMIReport = jsonObject.getJSONObject("LatestBMIReport");

                            BMI_Model bm_model = new BMI_Model();
                            bm_model.id = LatestBMIReport.getString("id");
                            bm_model.first_name = LatestBMIReport.getString("first_name");
                            bm_model.remark = LatestBMIReport.getString("remark");
                            bm_model.grade = LatestBMIReport.getString("grade");

                            grade_1.setText(bm_model.grade);

                        } else {
                            Log.d("null", "onResponse: ");
                        }
                        /******************************LatestCBCLevel******************************/

                        if (jsonObject.has("LatestCBCLevel") && !jsonObject.isNull("LatestCBCLevel")) {
                            JSONObject CBCLevel = jsonObject.getJSONObject("LatestCBCLevel");

                            CBC_Model cbc_model = new CBC_Model();
                            cbc_model.id = CBCLevel.getString("id");
                            cbc_model.first_name = CBCLevel.getString("first_name");
                            cbc_model.report_analysis = CBCLevel.getString("report_analysis");
                            cbc_model.remark = CBCLevel.getString("remark");
                            cbc_model.grade = CBCLevel.getString("grade");
                            cbc_model.report = CBCLevel.getString("report");


                            if (cbc_model.remark.equals("") || cbc_model.remark.equals("null")) {
                                remark.setVisibility(View.GONE);
                            } else {

                                remark.setText(cbc_model.remark);
                                if (remark.getLineCount() >= 4) {
                                    open_dial_5.setVisibility(View.VISIBLE);
                                    open_dial_5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(cbc_model.remark, "Recommendation");
                                        }
                                    });

                                } else {
                                    open_dial_5.setVisibility(View.GONE);

                                }

                            }

                            if (cbc_model.report_analysis.equals("") || cbc_model.report_analysis.equals("null")) {
                                report_analysis.setVisibility(View.GONE);
                            } else {
                                report_analysis.setText(cbc_model.report_analysis);
                                if (report_analysis.getLineCount() >= 4) {
                                    open_dial_6.setVisibility(View.VISIBLE);
                                    open_dial_6.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(cbc_model.report_analysis, "Report Analysis");
                                        }
                                    });

                                }
                            }

                            grade_2.setText(cbc_model.grade);
                            String strpdf = cbc_model.report;
                            if (!strpdf.equals("")) {
                                pdf_1.setVisibility(View.VISIBLE);
                                ll_pdf_2.setVisibility(View.VISIBLE);
                            }

                            pdf_1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showReportDailog();

                                }
                            });
                        } else {
                            Log.d("null", "onResponse: ");
                        }/******************************CBC******************************/
                        JSONArray jsonArrayv = jsonObject.getJSONArray("CBCLevel");
                        if (jsonArrayv != null) {

                            for (int i = 0; i < jsonArrayv.length(); i++) {
                                JSONObject CBCLevel = jsonArrayv.getJSONObject(i);


                                CBC_Model cbc_model = new CBC_Model();
                                cbc_model.id = CBCLevel.getString("id");
                                cbc_model.first_name = CBCLevel.getString("first_name");
                                cbc_model.report_analysis = CBCLevel.getString("report_analysis");
                                cbc_model.remark = CBCLevel.getString("remark");
                                cbc_model.grade = CBCLevel.getString("grade");
                                cbc_model.report = CBCLevel.getString("report");
                                cbcTabModelArrayList.add(cbc_model);
                            }
                            recyclerView_cbc.setAdapter(new CBCAdapter(cbcTabModelArrayList, (AppCompatActivity) HealthActivity.this));

                        } else {
                            Log.d("null", "onResponse: ");
                        }
                        /******************************MuscuskelotalReport******************************/
                        if (jsonObject.has("MuscuskelotalReport") && !jsonObject.isNull("MuscuskelotalReport")) {

                            JSONObject MuscuskelotalReport = jsonObject.getJSONObject("MuscuskelotalReport");


                            Muscuskelotal_Model secondaryModel = new Muscuskelotal_Model();
                            secondaryModel.id = MuscuskelotalReport.getString("id");
                            secondaryModel.first_name = MuscuskelotalReport.getString("first_name");
                            secondaryModel.upper_extremity = MuscuskelotalReport.getString("upper_extremity");
                            secondaryModel.spine = MuscuskelotalReport.getString("spine");
                            secondaryModel.lower_extremity = MuscuskelotalReport.getString("lower_extremity");

                            secondaryModel.grade = MuscuskelotalReport.getString("grade");
                            secondaryModel.report = MuscuskelotalReport.getString("report");


                            if (secondaryModel.upper_extremity.equals("") || secondaryModel.upper_extremity.equals("null")) {
                                upper_extremity.setVisibility(View.GONE);
                                open_dial_2.setVisibility(View.GONE);
                            } else {
                                upper_extremity.setText(secondaryModel.upper_extremity);
                                if (upper_extremity.getLineCount() >= 4) {
                                    open_dial_2.setVisibility(View.VISIBLE);
                                    open_dial_2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(secondaryModel.upper_extremity, "Upper Extremity");
                                        }
                                    });

                                } else {
                                    open_dial_2.setVisibility(View.GONE);

                                }


                            }
                            if (secondaryModel.spine.equals("") || secondaryModel.spine.equals("null")) {
                                spine.setVisibility(View.GONE);
                                open_dial_4.setVisibility(View.GONE);
                            } else {
                                spine.setText(secondaryModel.spine);
                                if (spine.getLineCount() >= 4) {
                                    open_dial_4.setVisibility(View.VISIBLE);
                                    open_dial_4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(secondaryModel.spine, "Report Analysis");
                                        }
                                    });
                                } else {
                                    open_dial_4.setVisibility(View.GONE);

                                }


                            }
                            if (secondaryModel.lower_extremity.equals("") || secondaryModel.lower_extremity.equals("null")) {
                                lower_extremity.setVisibility(View.GONE);
                                open_dial_3.setVisibility(View.GONE);
                            } else {
                                lower_extremity.setText(secondaryModel.lower_extremity);
                                if (lower_extremity.getLineCount() >= 4) {
                                    open_dial_3.setVisibility(View.VISIBLE);
                                    open_dial_3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(secondaryModel.lower_extremity, "Recommendation");
                                        }
                                    });
                                } else {
                                    open_dial_3.setVisibility(View.GONE);

                                }

                            }

                            String strpdf1 = secondaryModel.report;
                            if (!strpdf1.equals("")) {
                                pdf_2.setVisibility(View.VISIBLE);
                                ll_pdf.setVisibility(View.VISIBLE);
                            }
                            grade_3.setText(secondaryModel.grade);
                            pdf_2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", secondaryModel.report);
                                    intent.putExtra("from", "5");

                                    startActivity(intent);

                                }
                            });

                        } else {
                            Log.d("null", "onResponse: ");
                            // Toast.makeText(HealthActivity.this, "null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //Toast.makeText(HealthActivity.this, message, Toast.LENGTH_SHORT).show();
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

    void Showdialog(String behaviour_observation, String title) {
        LayoutInflater li = LayoutInflater.from(HealthActivity.this);
        View promptsView = li.inflate(R.layout.demo, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HealthActivity.this);
        alertDialogBuilder.setTitle(title);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView txt = (TextView) promptsView
                .findViewById(R.id.textView1);
        txt.setText(behaviour_observation);


        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    public void showReportDailog() {
        // custom dialog
        final Dialog dialog = new Dialog(HealthActivity.this,
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
        recyclerView_gifts.setLayoutManager(new GridLayoutManager(HealthActivity.this, 2));

        UsersRedeemList(recyclerView_gifts, dialog);

        dialog.show();
    }

    private void UsersRedeemList(final RecyclerView recyclerView, final Dialog dialog) {


        final RelativeLayout rlt_progress = dialog.findViewById(R.id.rlt_progress);
        rlt_progress.setVisibility(View.VISIBLE);

        final ArrayList<CBC_Model> redeemModelArrayList = new ArrayList();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.HealthReport,
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
                                JSONArray jsonArrayv = jsonObject.getJSONArray("CBCLevel");
                                if (jsonArrayv != null) {

                                    for (int i = 0; i < jsonArrayv.length(); i++) {
                                        JSONObject CBCLevel = jsonArrayv.getJSONObject(i);


                                        CBC_Model cbc_model = new CBC_Model();
                                        cbc_model.id = CBCLevel.getString("id");
                                        cbc_model.first_name = CBCLevel.getString("first_name");
                                        cbc_model.report_analysis = CBCLevel.getString("report_analysis");
                                        cbc_model.remark = CBCLevel.getString("remark");
                                        cbc_model.grade = CBCLevel.getString("grade");
                                        cbc_model.report = CBCLevel.getString("report");
                                        cbc_model.added_date = CBCLevel.getString("added_date");
                                        redeemModelArrayList.add(cbc_model);
                                    }
                                    CBCAdapter userWinnerAdapter = new CBCAdapter(redeemModelArrayList, (AppCompatActivity) HealthActivity.this);
                                    recyclerView.setAdapter(userWinnerAdapter);
                                } else {
                                    Log.d("null", "onResponse: ");
                                }


                            } else {
                                pdf_1.setVisibility(View.GONE);
                                if (jsonObject.has("message")) {

                                    Toast.makeText(HealthActivity.this, message,
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
                Toast.makeText(HealthActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
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

    public void onClickCalled(BMI_Model classModel, int position, String s) {


        if (Constant.isNetworkAvailable(HealthActivity.this)) {
            report_data.setText("Report:" + (position + 1));
            report_data.setVisibility(View.VISIBLE);
            rl_all_data.setVisibility(View.VISIBLE);
            ll_bar_data.setVisibility(View.VISIBLE);
            bmi_rec_data.setVisibility(View.VISIBLE);
            FetchBMIReport(classModel.id);

        } else {
            Toast.makeText(this, "No Internet Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void FetchBMIReport(String report_id) {
        final ProgressDialog progressDialog = new ProgressDialog(HealthActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FetchBMIReport,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, ResultNormalRange, HeightNormalRange, WeightNormalRange;
/*
*
*FOR HEIGHT/WEIGHT: HeightNormalRange/WeightNormalRange
 0,1,2 ->between (Green)
3-below (Red)
4-above (Red)

 FOR BMI RESULT:
*
0- below
1-in range
2- above
* */
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");
                            Log.d("CHECK_BMI", "onResponse: " + response);
                            

                            ResultNormalRange = jsonObject.getString("ResultNormalRange");
                            HeightNormalRange = jsonObject.getString("HeightNormalRange");
                            WeightNormalRange = jsonObject.getString("WeightNormalRange");
                                /*
                                #HeightNormalRange,#WeightNormalRange
                                0->normal
                                3->under
                                4->over
                                */
                            if (HeightNormalRange.equals("0")) {
                                bmi_height.setTextColor(getResources().getColor(R.color.green));
                            } else if (HeightNormalRange.equals("3")) {
                                bmi_height.setTextColor(getResources().getColor(R.color.bg_red));
                            } else {
                                bmi_height.setTextColor(getResources().getColor(R.color.bg_red));
                            }

                            if (WeightNormalRange.equals("0") ) {
                                bmi_weight.setTextColor(getResources().getColor(R.color.green));
                            } else if (WeightNormalRange.equals("3")) {
                                bmi_weight.setTextColor(getResources().getColor(R.color.bg_red));
                            } else {
                                bmi_weight.setTextColor(getResources().getColor(R.color.bg_red));
                            }

                          /*
                           #ResultNormalRange
                           1- normal
                           0-underweight
                           2-over
                          */
                            if (ResultNormalRange.equals("0")) {
                                box_4.setVisibility(View.VISIBLE);//Arrow Handle Here based
                                box_5.setVisibility(View.INVISIBLE);//Arrow Handle Here based
                                box_6.setVisibility(View.INVISIBLE);//Arrow Handle Here based
                                bmi_result_.setTextColor(getResources().getColor(R.color.bg_red));
                                ll_bmi.setBackgroundColor(getResources().getColor(R.color.bg_red));
                            } else if (ResultNormalRange.equals("1")) {
                                box_5.setVisibility(View.VISIBLE);//Arrow Handle Here based
                                box_4.setVisibility(View.INVISIBLE);//Arrow Handle Here based
                                box_6.setVisibility(View.INVISIBLE);//Arrow Handle Here based
                                bmi_result_.setTextColor(getResources().getColor(R.color.green));
                                ll_bmi.setBackgroundColor(getResources().getColor(R.color.green));
                            } else {
                                box_6.setVisibility(View.VISIBLE);//Arrow Handle Here based
                                box_4.setVisibility(View.INVISIBLE);//Arrow Handle Here based
                                box_5.setVisibility(View.INVISIBLE);//Arrow Handle Here based
                                bmi_result_.setTextColor(getResources().getColor(R.color.bg_red));
                                ll_bmi.setBackgroundColor(getResources().getColor(R.color.bg_red));
                            }

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {
                                JSONObject BMIReport = jsonObject.getJSONObject("BMIReport");
                                JSONObject HeightChart = jsonObject.getJSONObject("HeightChart");
                                JSONObject WeightChart = jsonObject.getJSONObject("WeightChart");
                                JSONObject BMIResultChart = jsonObject.getJSONObject("BMIResultChart");

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                bmi_height.setText(BMIReport.getString("height"));
                                bmi_height_n.setText(BMIReport.getString("height_normal_range"));
                                bmi_weight.setText(BMIReport.getString("weight"));
                                bmi_weight_n.setText(BMIReport.getString("weight_normal_range"));
                                bmi_result_.setText(BMIReport.getString("result"));
                                your_bmi.setText(BMIReport.getString("result"));
                                your_bmi_2.setText(BMIReport.getString("result"));
                                your_bmi_3.setText(BMIReport.getString("result"));
                                bmi_result_n.setText(BMIReport.getString("result_normal_range"));
                                remark_new.setText(BMIReport.getString("remark"));
                                Date date = null;
                                try {
                                    date = fmt.parse(BMIReport.getString("report_date"));
                                    rp_date.setText(fmtOut.format(date));

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                box_2.setText("Normal Range\n" + BMIResultChart.getString("min_avg") + " - " + BMIResultChart.getString("max_avg"));
                                box_1.setText("Need Attention\n < " + BMIResultChart.getString("min_avg"));
                                box_3.setText("Need Attention\n" + ">" + BMIResultChart.getString("max_avg"));

                                String remar_data = BMIReport.getString("analysis");
                                remark_analasis.setText(remar_data);

                                if (remark_analasis.getLineCount() >= 4) {
                                    open_dial_r.setVisibility(View.VISIBLE);
                                    open_dial_r.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(remar_data, "Analysis");
                                        }
                                    });
                                } else {
                                    remark_analasis.setText(remar_data);
                                    open_dial_r.setVisibility(View.GONE);
                                }

                                recommendation_bmi.setText(BMIReport.getString("remark"));
                                String data_rec_bmi = BMIReport.getString("remark");
                                if (recommendation_bmi.getLineCount() >= 4) {
                                    open_dial.setVisibility(View.VISIBLE);
                                    open_dial.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Showdialog(data_rec_bmi, "Recommendation");
                                        }
                                    });
                                } else {
                                    recommendation_bmi.setText(data_rec_bmi);
                                    open_dial.setVisibility(View.GONE);
                                }
                                //remark_analasis.setText(BMIReport.getString("analysis"));

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
                params.put("report_id", report_id);
                params.put("user_id", str_userid);
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

}