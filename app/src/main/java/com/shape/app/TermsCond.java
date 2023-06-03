package com.shape.app;

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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shape.app.Activity.About_Us;
import com.shape.app.Activity.MainActivity;
import com.shape.app.Activity.WelcomeScreen;
import com.shape.app.Forms.Login;
import com.shape.app.Fragment.HomeFragment;
import com.shape.app.Helper.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TermsCond extends AppCompatActivity {
    Button btn;
    WebView webViewTerms;
    CheckBox checkBox;
    TextView textView;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    Button btnterms;
    String str_userid, str_name, str_fname, str_theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_cond2);
        textView = findViewById(R.id.textterms);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
//                    textView.setText("Please agree Terms and Condition");
                    Toast.makeText(TermsCond.this, "Please agree Terms and Condition", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.bt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        checkBox = findViewById(R.id.checkboxterms);

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout linearLayout = findViewById(R.id.ll_bg);
            linearLayout.setBackgroundColor(Color.parseColor(str_theme));
        }


        str_fname = getIntent().getStringExtra("flag");
        findViewById(R.id.ll_view).setVisibility(View.VISIBLE);
        btnterms=findViewById(R.id.btnterms);
        webViewTerms = findViewById(R.id.webViewTerms);

        if (str_fname.equals("0")) {
            ((TextView) findViewById(R.id.toolbr_lbl)).setText("About Us");
            getAppData("0");
        } else if (str_fname.equals("1")) {
            ((TextView) findViewById(R.id.toolbr_lbl)).setText("Contact Us");
            getAppData("1");
        } else if ((str_fname.equals("2"))) {
            webViewTerms.setVisibility(View.VISIBLE);
            findViewById(R.id.ll_view).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.toolbr_lbl)).setText("Terms and Conditions");
            getAppData("2");
        }

    }

    private void getAppData(String flag_i) {
        final ProgressDialog progressDialog = new ProgressDialog(TermsCond.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.About_Us,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            if (code.equals("200")) {
                                JSONObject jsonArrayvideo = jsonObject.getJSONObject("Data");
                                Log.d("jsonArrayvideo", "onResponse: " + jsonArrayvideo);

                                String id = jsonArrayvideo.getString("id");
                                String contact_us = jsonArrayvideo.getString("contact_us");
                                String about_us = jsonArrayvideo.getString("about_us");
                                String terms = jsonArrayvideo.getString("terms");


                                if (flag_i.equals("0")) {
                                    webViewTerms.loadDataWithBaseURL(null, about_us, "text/html", "UTF-8", null);

                                } else if (flag_i.equals("1")) {
                                    webViewTerms.loadDataWithBaseURL(null, contact_us, "text/html", "UTF-8", null);
                                } else if ((flag_i.equals("2"))) {
                                    webViewTerms.loadDataWithBaseURL(null, terms, "text/html", "UTF-8", null);

                                }
                            } else {
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TermsCond.this, "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
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

    private String JustifyText(String bodyHTML) {
        return "<html>" + "<body style='text-align: justify;'>" + bodyHTML + "</body></html>";
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


