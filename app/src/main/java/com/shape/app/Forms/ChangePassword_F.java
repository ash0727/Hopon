package com.shape.app.Forms;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shape.app.Activity.WelcomeScreen;
import com.shape.app.Helper.Constant;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword_F extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText et_password_one, et_password_two;
    Button btn_submit_mobile;
    String str_student_code, str_user_id, str_phone, str_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_f);
        btn_submit_mobile = findViewById(R.id.btn_submit_mobile);
        et_password_one = findViewById(R.id.et_student_code);
        et_password_two = findViewById(R.id.et_phone);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_user_id = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
        }


        findViewById(R.id.btn_submit_mobile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isNetworkAvailable(ChangePassword_F.this)) {
                    if (et_password_one.getText().toString().trim().equals("")) {
                        Toast.makeText(ChangePassword_F.this, "Please Enter the valid Password", Toast.LENGTH_SHORT).show();

                    } else if (et_password_two.getText().toString().trim().equals("")) {
                        Toast.makeText(ChangePassword_F.this, "Please Enter the valid Confirm Password ", Toast.LENGTH_SHORT).show();

                    } else if (!et_password_two.getText().toString().trim().equals(et_password_one.getText().toString().trim())) {
                        Toast.makeText(ChangePassword_F.this, "Password Do Not Match", Toast.LENGTH_SHORT).show();

                    } else {
                        str_student_code = et_password_one.getText().toString().trim();
                        str_phone = et_password_two.getText().toString().trim();
                        getUserChangePassword_F(str_phone);
                    }
                } else {
                    Toast.makeText(ChangePassword_F.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getUserChangePassword_F(String str_pass) {

        final ProgressDialog progressDialog = new ProgressDialog(ChangePassword_F.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ChangePassword,
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

                                Intent intent = new Intent(getBaseContext(), WelcomeScreen.class);
                                finish();
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Password Changed Successfully.", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ChangePassword_F.this, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ChangePassword_F.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("password", str_pass);//password
                params.put("user_id", str_user_id);
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