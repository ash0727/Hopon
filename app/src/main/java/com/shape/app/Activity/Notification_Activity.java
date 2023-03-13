package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shape.app.Adapters.Achievement_Adapter;
import com.shape.app.Adapters.Notification_Adapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.AchievementModel;
import com.shape.app.Models.Notification_Model;
import com.shape.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class Notification_Activity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String str_userid, str_intent_flag, str_theme;
    LinearLayout ll_bg;
    CardView card_main;


    ArrayList<Notification_Model> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_note;
    LottieAnimationView animationView;
    ImageButton bt_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        ll_bg = findViewById(R.id.ll_bg);
        bt_menu = findViewById(R.id.bt_menu);
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            if (!str_theme.equals("")) {
                setStatusBarColor(str_theme);
                ll_bg.setBackgroundColor(Color.parseColor(str_theme));
            }
        } catch (Exception e) {
            Log.d("tag_color:-", "onCreateView: " + e.toString());
        }
        recyclerView_note = findViewById(R.id.rv_recyclerview);
        recyclerView_note.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        Log.d("3", "onCreateView: " + eventTabModelArrayList.size());

        if (Constant.isNetworkAvailable(getApplicationContext())) {
            GetData();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }


    }

    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(Notification_Activity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.NotificationListt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BMIReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        progressDialog.dismiss();


                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("Data");

                        if (jsonArrayvideo != null) {
                            for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                Notification_Model bmi_model = new Notification_Model();
                                JSONObject BMIReport = jsonArrayvideo.getJSONObject(i);

                                bmi_model.id = BMIReport.getString("id");
                                bmi_model.msg = BMIReport.getString("msg");
                                bmi_model.detail = BMIReport.getString("detail");
                                bmi_model.added_date = BMIReport.getString("added_date");

                                eventTabModelArrayList.add(bmi_model);
                            }
                            recyclerView_note.setAdapter(new Notification_Adapter(eventTabModelArrayList, Notification_Activity.this));


                        } else {
                            Log.d("null", "onResponse: ");
                            findViewById(R.id.no_slot).setVisibility(View.VISIBLE);
                        }


                    } else {
                        progressDialog.dismiss();
                        findViewById(R.id.no_slot).setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    findViewById(R.id.no_slot).setVisibility(View.VISIBLE);
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                findViewById(R.id.no_slot).setVisibility(View.VISIBLE);
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

}