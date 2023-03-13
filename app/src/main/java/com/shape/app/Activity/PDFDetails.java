package com.shape.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shape.app.Helper.Constant;
import com.shape.app.R;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;

public class PDFDetails extends AppCompatActivity implements DownloadFile.Listener {
    SharedPreferences sharedPreferences;
    String str_userid, sub_id, chapter_id, exam_id;
    String PDFUrl, from, name, intent_pdf, id, updated_date, created_date, chapter_name;
    ProgressDialog progressDialog;
    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;

    PDFPagerAdapter adapter;
    ProgressDialog pDialog;
    String intent_grade, str_theme, str_code, str_fname, str_lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_details);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_code = sharedPreferences.getString(STUDENT_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        if (!str_theme.equals("")) {
            setStatusBarColor(str_theme);
            RelativeLayout rl_bg = findViewById(R.id.rl_tool);
            rl_bg.setBackgroundColor(Color.parseColor(str_theme));
        }
        intent_pdf = getIntent().getStringExtra("url");
        from = getIntent().getStringExtra("from");

        ((TextView) findViewById(R.id.toolbr_lbl)).setText("Report");
        findViewById(R.id.imgbck).setOnClickListener(view -> onBackPressed());

        root = findViewById(R.id.remote_pdf_root);
        pDialog = new ProgressDialog(PDFDetails.this);
        pDialog.setTitle("Please Wait");
        pDialog.setMessage("Loading...");

        if (intent_pdf.equals("")) {
            Toast.makeText(getApplicationContext(), "PDF Not Found!", Toast.LENGTH_SHORT).show();
        }

        if (from.equals("1")) {
            PDFUrl = Constant.PDF_Heart_report + intent_pdf;
        } else if (from.equals("2")) {
            PDFUrl = Constant.PDF_PrimaryFitness_report + intent_pdf;
        } else if (from.equals("3")) {
            PDFUrl = Constant.PDF_SecondaryFitness_report + intent_pdf;
        } else if (from.equals("4")) {
            PDFUrl = Constant.PDF_CBCLevel_report + intent_pdf;
        } else if (from.equals("5")) {
            PDFUrl = Constant.PDF_Muscuskelotal_report + intent_pdf;

        } else if (from.equals("6")) {
            String intent_data = getIntent().getStringExtra("data");
            if (intent_data.equals("1")) {
                ((TextView) findViewById(R.id.toolbr_lbl)).setText("Curriculum");
                PDFUrl = Constant.PDF_Physicaleducation + intent_pdf;
            } else if (intent_data.equals("3")) {
                ((TextView) findViewById(R.id.toolbr_lbl)).setText("Report");
                PDFUrl = Constant.PDF_Physicaleducation + intent_pdf;
            } else {
                ((TextView) findViewById(R.id.toolbr_lbl)).setText("Unit Plans");
                PDFUrl = Constant.PDF_Physicaleducation + intent_pdf;
            }


        } else if (from.equals("7")) {
            PDFUrl = Constant.PDF_OverallUnit_report + intent_pdf;

        } else if (from.equals("8")) {
            PDFUrl = Constant.GameSkills_report + intent_pdf;

        } else if (from.equals("9")) {
            PDFUrl = Constant.MentalSkills_report + intent_pdf;

        } else if (from.equals("10")) {
            PDFUrl = Constant.RelaxedAwareness_report + intent_pdf;

        } else if (from.equals("11")) {
            ((TextView) findViewById(R.id.toolbr_lbl)).setText("Certificate");
            PDFUrl = Constant.Achievement_certificate + intent_pdf;
        } else if (from.equals("12")) {
            PDFUrl = Constant.OverallPerformance_report + intent_pdf;

        } else if (from.equals("14")) {
            ((TextView) findViewById(R.id.toolbr_lbl)).setText("Reflection");
            PDFUrl = Constant.OverallPerformance_reflection + intent_pdf;
        }

        Log.d("intent_data", "onCreate: " + intent_pdf);
        Log.d("intent_url", "onCreate: " + PDFUrl);


        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        // String url = "https://megaplay.sgp1.digitaloceanspaces.com/questpix/PDF/e398be57386952ca2bbcbf58ae4c1f1c.pdf";
        remotePDFViewPager = new RemotePDFViewPager(ctx, PDFUrl, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
        pDialog.show();
        Log.d("pdf_", "onCreate: " + intent_pdf);



       /* sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        if (Constant.isNetworkAvailable(getApplicationContext())) {
            ContentList();
        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }*/
        // new ExamChapterDetails.RetrievePDFStream().execute(intent_pdf);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.close();
        }
    }


    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
        pDialog.dismiss();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        pDialog.dismiss();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        long MEGABYTE = 1024L * 1024L;
        long b = progress / MEGABYTE;
        long l = total / MEGABYTE;
        Log.d("_tag_progress_mb", "onProgressUpdate: progress:" + b);
        Log.d("_tag_progress_mb_1", "onProgressUpdate: progress:" + l);

        int firstDigit = Integer.parseInt(Integer.toString(total).substring(0, 3));
        int firstDigit_two = Integer.parseInt(Integer.toString(progress).substring(0, 3));
        Log.d("firstDigit", "onProgressUpdate: progress:" + firstDigit + "==" + firstDigit_two);

        if (firstDigit == firstDigit_two) {
            pDialog.dismiss();
        }

        Log.d("_tag_progress", "onProgressUpdate: progress:" + progress + "total:" + total);
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