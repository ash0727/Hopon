package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shape.app.Helper.Constant;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.shape.app.Forms.Login.FATHER_NAME;
import static com.shape.app.Forms.Login.FATHER_PHONE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.PHONENO;
import static com.shape.app.Forms.Login.PROFILE_PIC;
import static com.shape.app.Forms.Login.SCHOOL_ID;
import static com.shape.app.Forms.Login.SCHOOL_LOGO;
import static com.shape.app.Forms.Login.SCHOOL_NAME;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class OtpScreen extends AppCompatActivity {
    Button btn_verify;
    EditText et_otp;
    TextView src_phone, tvreotp;
    String str_userphone, str_otpid, str_userid;
    String str_otp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);

        str_userphone = getIntent().getStringExtra("user_phone");
        str_otpid = getIntent().getStringExtra("str_otpid");
        // str_userid = getIntent().getStringExtra("user_id");

        initToolbar();
        onClick();

    }

    private void onClick() {
        tvreotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(OtpScreen.this, "Send!", Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.btn_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_otp = et_otp.getText().toString().trim();
                if (Constant.isNetworkAvailable(OtpScreen.this)) {
                    if (str_otp.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter the valid OTP", Toast.LENGTH_SHORT).show();

                    } else {

                        sentUserOtp(str_otpid, str_otp, str_userid, str_userphone);
                    }
                } else {
                    Toast.makeText(OtpScreen.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void sentUserOtp(final String str_otpid, final String str_otp, final String str_userid, final String str_mobile_no) {

        final ProgressDialog progressDialog = new ProgressDialog(OtpScreen.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.USER_CONFIRMOTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("student_details");
                                Log.d("resp", "onResponse: " + jsonObject1 + "===" + code);


                            } else {
                                Toast.makeText(OtpScreen.this, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(OtpScreen.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_no", str_mobile_no);
                params.put("otp", str_otp);
                params.put("otp_id", str_otpid);
                Log.d("params", "getParams: " + params);
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


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_verify = findViewById(R.id.btn_verify);
        tvreotp = findViewById(R.id.tvreotp);
        src_phone = findViewById(R.id.src_phone);
        src_phone.setText("Enter the OTP Sent To +91" + str_userphone);
        et_otp = findViewById(R.id.et_otp);


    }

    private void SetDataToSherf(String str_user_id, String phone_no, String school_id, String student_code, String first_name, String last_name, String father_mobile, String father_name, String theme_color, String school_name, String logo, String profile_pic) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(USER_ID, str_user_id);
        editor.putString(PHONENO, phone_no);

        editor.putString(PROFILE_PIC, profile_pic);
        editor.putString(SCHOOL_ID, school_id);
        editor.putString(STUDENT_ID, student_code);
        editor.putString(FNAME, first_name);
        editor.putString(LNAME, last_name);
        editor.putString(FATHER_NAME, father_name);
        editor.putString(FATHER_PHONE, father_mobile);
        editor.putString(THEME_COLOR, theme_color);
        editor.putString(SCHOOL_NAME, school_name);
        editor.putString(SCHOOL_LOGO, logo);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}