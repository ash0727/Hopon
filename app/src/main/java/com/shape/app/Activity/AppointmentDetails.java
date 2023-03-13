package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shape.app.Helper.Constant;
import com.shape.app.Helper.ProjectUtils;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class AppointmentDetails extends AppCompatActivity {

    EditText et_name, et_number, et_date, et_time, et_comment;
    String str_name, str_no, str_date, str_time, str_comment, str_name_intent;
    Button submit;
    LinearLayout ll_bg,ll_bg_new;
    TextView txt_1;
    ImageView icon_time, icon_date, bt_menu;

    SharedPreferences sharedPreferences;
    String str_userid, str_intent_flag, str_theme,str_intent_img;
    private ProjectUtils.CustomTimePickerDialog dialog;
    private Calendar myCalendar = Calendar.getInstance();

    private Calendar refCalender = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    public Date dob_timeStamp;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");

        str_name_intent = getIntent().getStringExtra("name");
        str_intent_flag = getIntent().getStringExtra("id");
        str_intent_img = getIntent().getStringExtra("image");

        image = findViewById(R.id.image);
        txt_1 = findViewById(R.id.txt_1);
        bt_menu = findViewById(R.id.bt_menu);
        txt_1.setText(str_name_intent);
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
        et_comment = findViewById(R.id.et_comment);
        submit = findViewById(R.id.submit);
        icon_time = findViewById(R.id.icon_time);
        icon_date = findViewById(R.id.icon_date);
        ll_bg = findViewById(R.id.ll_bg);
        ll_bg_new = findViewById(R.id.ll_bg_new);

        Glide.with(getApplicationContext()).load(str_intent_img).placeholder(R.drawable.img_no).into(image);

        try {
            if (!str_theme.equals("")) {
                setStatusBarColor(str_theme);
                submit.setBackgroundColor(Color.parseColor(str_theme));
                ll_bg.setBackgroundColor(Color.parseColor(str_theme));
                ll_bg_new.setBackgroundColor(Color.parseColor(str_theme));
                icon_date.setBackgroundColor(Color.parseColor(str_theme));
                icon_time.setBackgroundColor(Color.parseColor(str_theme));

            }
        } catch (Exception e) {
            Log.d("tag_color:-", "onCreateView: " + e.toString());
        }

        icon_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProjectUtils.CustomTimePickerDialog(AppointmentDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf1 = new SimpleDateFormat("kk:mm:ss");//24 hour user kk ; 12 hour use hh
                        et_time.setText(sdf1.format(myCalendar.getTime()));
                    }
                },
                        myCalendar.getTime().getHours(), myCalendar.getTime().getMinutes(), true);
                dialog.show();
            }
        });
        icon_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDOB();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Constant.isNetworkAvailable(getApplicationContext())) {
                    str_name = et_name.getText().toString();
                    str_no = et_number.getText().toString();
                    str_date = et_date.getText().toString();
                    str_time = et_time.getText().toString();
                    str_comment = et_comment.getText().toString();
                    if (str_name.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter the valid Name", Toast.LENGTH_SHORT).show();
                    } else if (getLenthFromEdit(et_number) != 10) {
                        Toast.makeText(getApplicationContext(), "Please Enter the valid Phone Number", Toast.LENGTH_SHORT).show();
                    }  else if (str_comment.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter the valid Comment", Toast.LENGTH_SHORT).show();
                    } else {
                        SubmitUserData(str_name, str_no, str_time, str_date, str_comment);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
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
    public static float getLenthFromEdit(EditText editText) {

        return editText.getText().toString().trim().length();
    }
    private void SubmitUserData(String str_name, String str_no, String str_time, String str_date, String str_comment) {
        final ProgressDialog progressDialog = new ProgressDialog(AppointmentDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_APPOINTMENT,
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

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);

//                                Intent intent = new Intent(getBaseContext(), OtpScreen.class);
//                                intent.putExtra("user_phone", str_phone);
//                                intent.putExtra("str_otpid", otp_id);
//                                Log.d("str_userphone", "onResponse: " + str_phone);
                                finish();
//                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Appointment Booked Successfully!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(AppointmentDetails.this, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AppointmentDetails.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                params.put("type", str_intent_flag);
                params.put("name", str_name);
                params.put("phone_no", str_no);
                params.put("date", "");
                params.put("time", "");
                params.put("comment", str_comment);
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

    public void openDatePickerDOB() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(AppointmentDetails.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                calendar.set(Calendar.YEAR, y);
                calendar.set(Calendar.MONTH, m);
                calendar.set(Calendar.DAY_OF_MONTH, d);

                String myFormat = "yyyy-MM-dd"; //In which you need put here dd-MMM-yyyy= 15-Feb-2021
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                et_date.setText(sdf.format(calendar.getTime()));
                dob_timeStamp = calendar.getTime();
                Log.d("tag_dob", "onDateSet: " + dob_timeStamp);


            }


        }, year, monthOfYear, dayOfMonth);
        calendar.set(year, monthOfYear, dayOfMonth);
        long value = calendar.getTimeInMillis();
        datePickerDialog.setTitle(getResources().getString(R.string.select_dob));

        datePickerDialog.getDatePicker().setMinDate(value);//SetMax For Past Date
        datePickerDialog.show();
    }

}