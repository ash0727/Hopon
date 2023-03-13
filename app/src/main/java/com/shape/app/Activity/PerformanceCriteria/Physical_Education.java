package com.shape.app.Activity.PerformanceCriteria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.shape.app.Adapters.PhysicalEducationAdapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.PhyEduModel;
import com.shape.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.GENDER;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.PROFILE_PIC;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class Physical_Education extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String str_userid, intent_grade, str_theme, str_code, str_fname, str_lname;
    TextView name, st_code, overall_grade, grade, term, term_2, unit, unit_details, unit_plan, grade_1, report_analysis, remark, upper_extremity, spine, lower_extremity;
    ImageView pdf_2, pdf_1,video_prime, curriculum_2, curriculum;
    int check = 0;
    String[] Months = {"Select Term", "Term 1", "Term 2"};
    Spinner spin;
    CardView cardView1, cardView2;
    RecyclerView recyclerView_event, recyclerView_event2;
    ArrayList<PhyEduModel> eventTabModelArrayList = new ArrayList<>();
    ArrayList<PhyEduModel> eventTabModelArrayList2 = new ArrayList<>();
    LinearLayout term_1ll, term_2ll;
    ImageView profile_image;
    String str_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical__education);

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

        intent_grade = getIntent().getStringExtra("grade");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout linearLayout = findViewById(R.id.ll_bg);
            RelativeLayout rl_bg = findViewById(R.id.rl_bg);
            linearLayout.setBackgroundColor(Color.parseColor(str_theme));
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }

        profile_image = findViewById(R.id.profile_image);
       // Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getApplicationContext()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        name = findViewById(R.id.name);
        st_code = findViewById(R.id.st_code);
        name.setText(str_fname + " " + str_lname);
        st_code.setText("("+str_code+")");

        eventTabModelArrayList = new ArrayList<>();
        eventTabModelArrayList2 = new ArrayList<>();
        recyclerView_event = findViewById(R.id.recyclerView_event);
        recyclerView_event2 = findViewById(R.id.recyclerView_event2);
        recyclerView_event.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView_event2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        Log.d("3", "onCreateView: " + eventTabModelArrayList.size());


        term_1ll = findViewById(R.id.term_1ll);
        term_2ll = findViewById(R.id.term_2ll);
        cardView1 = findViewById(R.id.card_1);
        cardView2 = findViewById(R.id.card_2);
        remark = findViewById(R.id.remark);
        grade = findViewById(R.id.grade);
        grade_1 = findViewById(R.id.grade_1);
        term = findViewById(R.id.term);

        term = findViewById(R.id.term);
        term = findViewById(R.id.term);

        term_2 = findViewById(R.id.term_2);
        unit = findViewById(R.id.unit);
        //  unit_details = findViewById(R.id.unit_details);

        overall_grade = findViewById(R.id.overall_grade);
        // unit_plan = findViewById(R.id.unit_plan);
        curriculum = findViewById(R.id.curriculum);
        curriculum_2 = findViewById(R.id.curriculum_2);

        video_prime = findViewById(R.id.video_prime);

        pdf_2 = findViewById(R.id.pdf_2);
        pdf_1 = findViewById(R.id.pdf_1);
        if (intent_grade == null || intent_grade.equals("")) {
            overall_grade.setText("-");
        } else {
            overall_grade.setText(intent_grade + "\nGrade");
        }
        spin = (Spinner) findViewById(R.id.spinner2);
        spin.setSelection(0); // Assuming the default position is 0.
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("tag_check", "onItemSelected: " + id);

                if (id == 0) {
                    cardView1.setVisibility(View.GONE);
                    cardView2.setVisibility(View.GONE);
                } else if (id == 1) {
                    cardView1.setVisibility(View.VISIBLE);
                    cardView2.setVisibility(View.GONE);
                    //GetData();
                } else {
                   // GetDataOverallUnitReport();

                    cardView1.setVisibility(View.GONE);
                    cardView2.setVisibility(View.VISIBLE);
                }
               /* Toast.makeText(getApplicationContext(),
                        Months[position],
                        Toast.LENGTH_LONG)
                        .show();*/


                Log.d("tag_check", "onItemSelected: " + check);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter aa = new ArrayAdapter(Physical_Education.this, android.R.layout.simple_spinner_item, Months);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        if (Constant.isNetworkAvailable(getApplicationContext())) {
            GetData();
            GetDataOverallUnitReport();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }


    }


    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(Physical_Education.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PhysicalEducation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PhysicalEducationReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONObject PhysicalEducation = jsonObject.getJSONObject("PhysicalEducation");

                        term_1ll.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("PhysicalEducationReport");
                        if (jsonArrayvideo != null) {
                            for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                PhyEduModel bmi_model = new PhyEduModel();
                                JSONObject phyedudata = jsonArrayvideo.getJSONObject(i);
                                bmi_model.id = phyedudata.getString("id");
                                bmi_model.student_id = phyedudata.getString("student_id");
                                bmi_model.term = phyedudata.getString("term");
                                bmi_model.curriculum = phyedudata.getString("curriculum");
                                bmi_model.unit_plans = phyedudata.getString("unit_plans");
                                bmi_model.grade = phyedudata.getString("grade");
                                bmi_model.grade_name = phyedudata.getString("grade_name");
                                bmi_model.report = phyedudata.getString("reports");
                                eventTabModelArrayList.add(bmi_model);

                            }
                            recyclerView_event.setAdapter(new PhysicalEducationAdapter(eventTabModelArrayList, (AppCompatActivity) Physical_Education.this));
                            String pdf_data = PhysicalEducation.getString("curriculum");
                            Log.d("PhysicalEducation", "onResponse: " + pdf_data);
                            curriculum.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", pdf_data);
                                    intent.putExtra("from", "6");
                                    intent.putExtra("data", "1");
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d("null", "onResponse: ");
                        }

                    } else {
                      //  Toast.makeText(getApplicationContext(), "No Data Added", Toast.LENGTH_SHORT).show();
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
                params.put("term", "term1");
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

    public void GetDataOverallUnitReport() {
        final ProgressDialog progressDialog = new ProgressDialog(Physical_Education.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PhysicalEducation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PhysicalEducation_2", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONObject PhysicalEducation = jsonObject.getJSONObject("PhysicalEducation");
                        term_2ll.setVisibility(View.VISIBLE);

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("PhysicalEducationReport");
                        if (jsonArrayvideo != null) {
                            for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                PhyEduModel bmi_model = new PhyEduModel();
                                JSONObject phyedudata = jsonArrayvideo.getJSONObject(i);
                                bmi_model.id = phyedudata.getString("id");
                                bmi_model.student_id = phyedudata.getString("student_id");
                                bmi_model.term = phyedudata.getString("term");
                                bmi_model.curriculum = phyedudata.getString("curriculum");
                                bmi_model.unit_plans = phyedudata.getString("unit_plans");
                                bmi_model.grade = phyedudata.getString("grade_name");
                                bmi_model.grade_name = phyedudata.getString("grade_name");
                                bmi_model.report = phyedudata.getString("reports");
                                eventTabModelArrayList2.add(bmi_model);

                            }
                            recyclerView_event2.setAdapter(new PhysicalEducationAdapter(eventTabModelArrayList2, (AppCompatActivity) Physical_Education.this));

                            String pdf_data = PhysicalEducation.getString("curriculum");
                            Log.d("PhysicalEducation", "onResponse: " + pdf_data);
                            curriculum_2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), PDFDetails.class);
                                    intent.putExtra("url", pdf_data);
                                    intent.putExtra("from", "6");
                                    startActivity(intent);
                                }
                            });

                        } else {

                        }

                    } else {
                        term_2ll.setVisibility(View.GONE);
                      //  Toast.makeText(getApplicationContext(), "No Data Added", Toast.LENGTH_SHORT).show();

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