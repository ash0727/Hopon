package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.shape.app.Adapters.ImageViewAdapter;
import com.shape.app.Models.ImageMOdel;
import com.shape.app.R;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class ImageListActivity extends AppCompatActivity {
    String image;
    ImageView img_result;
    Button btn;

    String ImageFull_Url;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    RecyclerView rv;
    // ArrayList<ResultModel> arrayList;
    //ImageView imgView;
    ZoomageView imgView;
    private ListView lv;
    RecyclerView recyclerView_image;
    SharedPreferences sharedPreferences;
    String str_userid, sub_id, chapter_id, exam_id;
    String str_theme, from, name, intent_pdf, id, updated_date, created_date, chapter_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        ((TextView) findViewById(R.id.toolbr_lbl)).setText("Media");
        findViewById(R.id.imgbck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            RelativeLayout rl_bg = findViewById(R.id.rl_tool);
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }
        Intent intent = getIntent();
        ArrayList<String> array = intent.getStringArrayListExtra("array");
        String data = intent.getStringExtra("data");
        Log.d("array_array", "onCreate: " + array);
        ArrayList<ImageMOdel> cardiovascularlistdata = new ArrayList<ImageMOdel>();


        if (data.equals("cardiovascular_ability_img")) {

        } else if (data.equals("muscular_ability_img")) {

        }


        try {
            Log.d("str_server_sub_services", "Responce: " + array);
            Log.d("str_server_service", "Responce: " + array.size());

            if (!array.equals("null")) {
                for (int i = 0; i < array.size(); i++) {
                    ImageMOdel imageMOdel = new ImageMOdel();

                    imageMOdel.name = (array.get(i));
                    Log.d("side_1", "Responce: " + array.get(i));
                    cardiovascularlistdata.add(imageMOdel);

                }
            }

        } catch (Exception e) {
            Log.d("Exception", "Responce: " + cardiovascularlistdata.size());
        }
        Log.d("cardiovascularlistdata", "size: " + cardiovascularlistdata.size());


        recyclerView_image = findViewById(R.id.recyclerView_image);

        recyclerView_image.setLayoutManager((new GridLayoutManager(getApplicationContext(), 2)));
        ImageViewAdapter adapter = (new ImageViewAdapter(cardiovascularlistdata, (AppCompatActivity) ImageListActivity.this,data));
        ;
        recyclerView_image.setAdapter(adapter);


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
