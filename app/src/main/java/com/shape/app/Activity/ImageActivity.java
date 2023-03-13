package com.shape.app.Activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.shape.app.Helper.Constant;
import com.shape.app.R;
import com.jsibbold.zoomage.ZoomageView;

import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;


public class ImageActivity extends AppCompatActivity {
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
    SharedPreferences sharedPreferences;
    String str_userid, sub_id, chapter_id, exam_id;
    String str_theme, from, name, intent_pdf, id, updated_date, created_date, chapter_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        str_theme = sharedPreferences.getString(THEME_COLOR, "");

        ((TextView) findViewById(R.id.toolbr_lbl)).setText("Media");
        findViewById(R.id.imgbck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String imagepath = intent.getStringExtra("url");
        String from = getIntent().getStringExtra("from");

        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            RelativeLayout rl_bg = findViewById(R.id.rl_tool);
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }
        if (from.equals("1")) {
            ImageFull_Url = Constant.VIDEO_PrimaryFitness_media + imagepath;
        } else if (from.equals("2")) {
            ImageFull_Url = Constant.VIDEO_SecondaryFitness_media + imagepath;
        } else if (from.equals("3")) {
            ImageFull_Url = Constant.VIDEO_OverallUnit_media + imagepath;
        }else if (from.equals("4")) {
            ImageFull_Url = Constant.GameSkills_media + imagepath;
        }else if (from.equals("5")) {
            ImageFull_Url = Constant.VIDEO_PrimaryFitness_images + imagepath;
        }else if (from.equals("6")) {
            ImageFull_Url = Constant.SecondaryFitness_img + imagepath;
        }else if (from.equals("7")) {
            ImageFull_Url = Constant.Achievement_images + imagepath;
        }


        Log.d("url_image", "onCreate: " + ImageFull_Url);
        imgView = findViewById(R.id.imgView);

        Glide.with(this).load(ImageFull_Url).into(imgView);

        findViewById(R.id.img_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ImageFull_Url);
                String url = ImageFull_Url;
                String filename;
                if (url.contains("?")) {
                    filename = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
                } else {
                    filename = url.substring(url.lastIndexOf("/") + 1, url.length());
                }
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);

// set title and description
                request.setTitle("Data Download");
                request.setDescription("Android Data download using DownloadManager.");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(filename);
//set the local destination for download file to a path within the application's external files directory
                request.setDestinationInExternalFilesDir(ImageActivity.this,Environment.DIRECTORY_DOWNLOADS, filename);
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
            }
        });
//Environment.getExternalStorageDirectory().getPath() + "MyExternalStorageAppPath"


//        rv= (RecyclerView) findViewById(id.rv);
//        progressDialog=new ProgressDialog(ImageActivity.this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");
//        requestQueue=Singleton.getInstance(ImageActivity.this).getRequestQueue();
//        rv.setLayoutManager(new LinearLayoutManager(ImageActivity.this));
//        String str=SharedPref.getJson(ImageActivity.this,SharedPref.json);
//        String name=str.trim();
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageActivity.this);
//                String mag=SharedPref.getJson(ImageActivity.this,SharedPref.json);
//
//                alertDialogBuilder.setMessage(mag);
//
//                alertDialogBuilder.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//
//
//                            }
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//
//            }
//        });
//      //  txt.setText(name);
//        Questpix(name);
//
////
////        img_result= (ImageView) findViewById(R.id.img_result);
////        image=getIntent().getStringExtra("img");
////        Picasso.with(ImageActivity.this).load(Constant.image+image).into(img_result);
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
