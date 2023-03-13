package com.shape.app.Activity.PerformanceCriteria;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.shape.app.Helper.Constant;
import com.shape.app.Models.PermissionModel;
import com.shape.app.Models.RelaxModel;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

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

public class RelaxedAwarenessActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String str_userid, intent_grade, str_theme, str_code, str_fname, str_lname;
    ImageView tactics_media_p, team_player_media_p, advance_skill_media_p, report, tactics_media_v, advance_skill_media_v, team_player_media_v;
    TextView name, st_code, coping_strategies, anxiety_control, activation, energizing, overall_grade, grade3, body_language;
    ImageView profile_image;
    String str_pic;
    LinearLayout ll_pdf;
    String str_class_id, str_school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relaxed_awareness);


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
            findViewById(R.id.ll_bg).setBackgroundColor(Color.parseColor(str_theme));
            findViewById(R.id.rl_bg).setBackgroundColor(Color.parseColor(str_theme));
        }
        profile_image = findViewById(R.id.profile_image);
        //Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        overall_grade = findViewById(R.id.overall_grade);
        ll_pdf = findViewById(R.id.ll_pdf);
        coping_strategies = findViewById(R.id.coping_strategies);
        anxiety_control = findViewById(R.id.anxiety_control);
        activation = findViewById(R.id.activation);
        energizing = findViewById(R.id.energizing);
        body_language = findViewById(R.id.body_language);
        report = findViewById(R.id.report);
        grade3 = findViewById(R.id.grade);

        if (intent_grade == null || intent_grade.equals("")) {
            overall_grade.setText("-");
        } else {
            overall_grade.setText(intent_grade + "\nGrade");
            grade3.setText(intent_grade + "\nGrade");

        }

        name = findViewById(R.id.name);
        st_code = findViewById(R.id.st_code);
        name.setText(str_fname + " " + str_lname);
        st_code.setText("(" + str_code + ")");
        if (Constant.isNetworkAvailable(getApplicationContext())) {
            GetData();
            GetPermission();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }


    }

    public void GetPermission() {
        final ProgressDialog progressDialog = new ProgressDialog(RelaxedAwarenessActivity.this);
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
                        PermissionModel permissionModel = new PermissionModel();
                        permissionModel.id = OverallPerformance.getString("id");
                        permissionModel.school_id = OverallPerformance.getString("school_id");
                        permissionModel.class_id = OverallPerformance.getString("class_id");
                        permissionModel.mental_skill = OverallPerformance.getString("mental_skill");

                        permissionModel.coping_strategies = OverallPerformance.getString("coping_strategies");
                        permissionModel.anxiety_control = OverallPerformance.getString("anxiety_control");
                        permissionModel.activation = OverallPerformance.getString("activation");
                        permissionModel.energizing = OverallPerformance.getString("energizing");
                        permissionModel.body_language = OverallPerformance.getString("body_language");


                        if (permissionModel.coping_strategies.equals("0")) {
                            findViewById(R.id.ll_coping_strategies).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_coping_strategies).setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.anxiety_control.equals("0")) {
                            findViewById(R.id.ll_anxiety).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_anxiety).setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.activation.equals("0")) {
                            findViewById(R.id.ll_activation).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_activation).setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.energizing.equals("0")) {
                            findViewById(R.id.ll_enegrey).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_enegrey).setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.body_language.equals("0")) {
                            findViewById(R.id.ll_body_lang).setVisibility(View.GONE);
                        } else {
                            findViewById(R.id.ll_body_lang).setVisibility(View.VISIBLE);
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
        RequestQueue requestQueue = Volley.newRequestQueue(RelaxedAwarenessActivity.this);
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(RelaxedAwarenessActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.RelaxedAwareness, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RelaxedAwarenessReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        if (jsonObject.has("RelaxedAwarenessReport") && !jsonObject.isNull("RelaxedAwarenessReport")) {

                            JSONObject BMIReport = jsonObject.getJSONObject("RelaxedAwarenessReport");

                            RelaxModel bmi_model = new RelaxModel();
                            bmi_model.id = BMIReport.getString("id");
                            bmi_model.first_name = BMIReport.getString("first_name");
                            bmi_model.coping_strategies = BMIReport.getString("coping_strategies");
                            bmi_model.anxiety_control = BMIReport.getString("anxiety_control");
                            bmi_model.activation = BMIReport.getString("activation");
                            bmi_model.energizing = BMIReport.getString("energizing");
                            bmi_model.body_language = BMIReport.getString("body_language");
                            bmi_model.overall_grade = BMIReport.getString("overall_grade");
                            bmi_model.report = BMIReport.getString("report");

                            coping_strategies.setText(bmi_model.coping_strategies + "\nGrade");
                            body_language.setText(bmi_model.body_language + "\nGrade");
                            anxiety_control.setText(bmi_model.anxiety_control + "\nGrade");
                            activation.setText(bmi_model.activation + "\nGrade");
                            energizing.setText(bmi_model.energizing + "\nGrade");
                            String strpdf2 = bmi_model.report;

                            if (!strpdf2.equals("")) {
                                report.setVisibility(View.VISIBLE);
                                ll_pdf.setVisibility(View.VISIBLE);
                            }
                            report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", bmi_model.report);
                                    intent.putExtra("from", "10");
                                    startActivity(intent);

                                }
                            });

                        } else {
                            Log.d("null", "onResponse: ");
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
                params.put("term", "term2");
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

}