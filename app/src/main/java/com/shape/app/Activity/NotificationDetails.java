package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shape.app.R;

import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class NotificationDetails extends AppCompatActivity {
    ImageButton bt_menu;
    TextView title, date, toolbr_lbl;
    TextView details;
    String str_title, str_msg, str_date;
    SharedPreferences sharedPreferences;
    String str_userid, sub_id, chapter_id, exam_id;
    String PDFUrl, from, str_theme, intent_pdf, id, updated_date, created_date, chapter_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        str_theme  = sharedPreferences.getString(THEME_COLOR, "");

        toolbr_lbl = findViewById(R.id.toolbr_lbl);
        title = findViewById(R.id.title);
        details = findViewById(R.id.title_de);
        date = findViewById(R.id.date_txt);
        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            LinearLayout rl_bg = findViewById(R.id.ll_bg);
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }
        str_msg = getIntent().getStringExtra("msg");
        str_title = getIntent().getStringExtra("details");
        str_date = getIntent().getStringExtra("date");
        bt_menu = findViewById(R.id.bt_menu);
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbr_lbl.setText(str_msg);
        title.setText(str_msg);
        date.setText(str_date);
        details.setText(str_title);
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