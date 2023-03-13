package com.shape.app.Activity;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.shape.app.Forms.Login;
import com.shape.app.Helper.Constant;
import com.shape.app.Helper.Functions;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {
    String str_userid, str_name, str_fname, str_theme;
    SharedPreferences sharedPreferences;
    EditText etdsubject, edtdetails;
    TextInputLayout tl_subject, tl_details;
    int limit_count = 5;
WebView webViewTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        ((TextView) findViewById(R.id.toolbr_lbl)).setText("Contact Us");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout linearLayout = findViewById(R.id.ll_bg);
            linearLayout.setBackgroundColor(Color.parseColor(str_theme));
        }

        webViewTerms = findViewById(R.id.webViewTerms);

        str_fname = getIntent().getStringExtra("flag");
        findViewById(R.id.bt_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getAppData("");
        etdsubject = findViewById(R.id.et_subject);
        edtdetails = findViewById(R.id.et_describe);
        tl_details = findViewById(R.id.tl_details);
        tl_subject = findViewById(R.id.tl_subject);

      /*  onTextWatcher(etdsubject);
        onTextWatcher(edtdetails);*/
        etdsubject.addTextChangedListener(contactusWatcher);
        edtdetails.addTextChangedListener(contactusWatcher);

        findViewById(R.id.txt_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.requestEditText(etdsubject);
                etdsubject.setText("");
                edtdetails.setText("");
            }
        });
        findViewById(R.id.txtsubmit).setEnabled(false);

        findViewById(R.id.txtsubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Functions.getStringFromEdit(etdsubject).equals("")) {
                    Functions.requestEditText(etdsubject);
                    setErrorOnInputLayout(R.id.tl_subject, "Enter the subject");


                } else if (Functions.getStringFromEdit(edtdetails).equals("")) {
                    Functions.requestEditText(edtdetails);
                    setErrorOnInputLayout(R.id.tl_details, "Enter the Details");

                } else {
                    SendMail(etdsubject.getText().toString(),edtdetails.getText().toString());

                }
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

    private void setErrorOnInputLayout(int id, String string) {
        ((TextInputLayout) findViewById(id)).setError(string);
        ((TextInputLayout) findViewById(id)).setErrorEnabled(true);
    }

    private void buttonEnableDisable(View button, TextView textView, boolean isEnable) {

        button.setEnabled(isEnable);

        textView.setTextColor(isEnable ?
                Functions.getColor(getApplicationContext(), R.color.white) :
                Functions.getColor(getApplicationContext(), R.color.disable_txt_color));

    }

    private TextWatcher contactusWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str_subj = etdsubject.getText().toString().trim();
            String str_details = edtdetails.getText().toString().trim();

            findViewById(R.id.txtsubmit).setEnabled(!str_subj.isEmpty() && !str_details.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void SendMail(final String subject, final String details) {

        final ProgressDialog progressDialog = new ProgressDialog(ContactUs.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.AddContactUs,
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
                                Functions.requestEditText(etdsubject);
                                etdsubject.setText("");
                                edtdetails.setText("");
                               // Toast.makeText(getApplicationContext(), "Data Send Successfully!", Toast.LENGTH_SHORT).show();

                                Functions.showAlertDialog((ContactUs.this) , "Thanks", "Our team will connect with you shortly.", true,
                                        "Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }, "", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        }, "", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });

                            } else {
                                Toast.makeText(ContactUs.this, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ContactUs.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("subject", subject);//password
                params.put("details", details);
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
    private void getAppData(String flag_i) {
        final ProgressDialog progressDialog = new ProgressDialog(ContactUs.this);
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

                                webViewTerms.loadDataWithBaseURL(null, contact_us, "text/html", "UTF-8", null);

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
                Toast.makeText(ContactUs.this, "Something went wrong", Toast.LENGTH_LONG).show();

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

}