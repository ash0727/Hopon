package com.shape.app.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shape.app.Activity.OtpScreen;
import com.shape.app.Activity.WelcomeScreen;
import com.shape.app.Helper.Constant;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText et_student_code, et_phone;
    Button btn_submit_mobile;
    String str_student_code, str_phone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView skip;
    ImageView icon_date;

    public static final String SHARED_PREFERENCES_NAME = "login_portal";
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String PHONENO = "phoneno";
    public static final String PROFILE_PIC = "pic";
    public static final String FNAME = "fname";
    public static final String LNAME = "lname";
    public static final String FATHER_NAME = "father_name";
    public static final String FATHER_PHONE = "father_phone";
    public static final String SCHOOL_ID = "school_id";
    public static final String CLASS_ID = "class_id";
    public static final String GENDER = "gender";
    public static final String STUDENT_ID = "student_id";
    public static final String THEME_COLOR = "theme_color";
    public static final String SCHOOL_NAME = "school_name";
    public static final String SCHOOL_LOGO = "school_logo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_login);

        btn_submit_mobile = findViewById(R.id.btn_submit_mobile);
        et_student_code = findViewById(R.id.et_student_code);
        et_phone = findViewById(R.id.et_phone);
        icon_date = findViewById(R.id.icon_date);

//        setTheme(R.style.AppTheme);

        TextView textView = (TextView) findViewById(R.id.link_con);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + "8425987115");
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);

            }
        });
        TextView link_con_2 = (TextView) findViewById(R.id.link_con_2);
        link_con_2.setClickable(true);
        link_con_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + "8425979115");
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);

            }
        });
        et_phone.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        et_phone.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


     /*   textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='http://www.google.com'> For assistance Contact(Admin@gmail.com) </a>";
        textView.setText(Html.fromHtml(text));*/
        findViewById(R.id.btn_submit_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(Login.this)) {
                    if (et_phone.getText().toString().trim().equals("")) {
                        Toast.makeText(Login.this, "Please Enter the valid Password", Toast.LENGTH_SHORT).show();

                    } else {
                        str_student_code = et_student_code.getText().toString().trim();
                        str_phone = et_phone.getText().toString().trim();
                        getUserLogin(str_phone, str_student_code);
                    }
                } else {
                    Toast.makeText(Login.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void getUserLogin(final String mobile_no, final String student_code) {

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.USER_LOGIN,
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
                                JSONObject jsonObject1 = jsonObject.getJSONObject("student_data");

                                String str_id = jsonObject1.getString("id");
                                String school_id = jsonObject1.getString("school_id");
                                String student_code = jsonObject1.getString("student_code");
                                String first_name = jsonObject1.getString("first_name");
                                String last_name = jsonObject1.getString("last_name");
                                String father_mobile = jsonObject1.getString("father_mobile");
                                String father_name = jsonObject1.getString("father_name");
                                String theme_color = jsonObject1.getString("theme_color");
                                String school_name = jsonObject1.getString("school_name");
                                String logo = jsonObject1.getString("logo");
                                String password = jsonObject1.getString("password");
                                String profile_pic = jsonObject1.getString("profile_pic");
                                String GENDER = jsonObject1.getString("gender");

                                SetDataToSherf(str_id, father_mobile, school_id, student_code, first_name, last_name, father_mobile, father_name, theme_color, school_name, logo, profile_pic,GENDER);

                              /*  if ( jsonObject1.getString("password_reset").equals("0")){
                                    Intent intent = new Intent(getBaseContext(), ChangePassword_F.class);
                                    finish();
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getBaseContext(), WelcomeScreen.class);
                                    finish();
                                    startActivity(intent);
                                }
*/
                                Intent intent = new Intent(getBaseContext(), WelcomeScreen.class);
                                finish();
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(Login.this, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", mobile_no);//password
                params.put("student_code", student_code);
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

    private void SendOTP(final String mobile_no) {

        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.USER_SendOTP,
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
                                String otp_id = jsonObject.getString("otp_id");

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);

                                Intent intent = new Intent(getBaseContext(), OtpScreen.class);
                                intent.putExtra("user_phone", str_phone);
                                intent.putExtra("str_otpid", otp_id);
                                Log.d("str_userphone", "onResponse: " + str_phone);
                                finish();
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "OTP Sent Successfully!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(Login.this, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile_no", mobile_no);
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

    private void SetDataToSherf(String str_user_id, String phone_no, String school_id, String student_code, String first_name, String last_name, String father_mobile, String father_name, String theme_color, String school_name, String logo, String profile_pic, String gender) {
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
        editor.putString(GENDER, gender);
        editor.apply();
    }

/*
    private void SetDataToSherf(String str_user_id, String phone_no) {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(USER_ID, str_user_id);
        editor.putString(PHONENO, phone_no);
        editor.apply();
    }
*/


    public void ShowHidePass(View view) {

        if (view.getId() == R.id.icon_date) {
            if (et_phone.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.hide);
                //Show Password
                et_phone.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.visible);
                //Hide Password
                et_phone.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}