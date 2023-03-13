package com.shape.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shape.app.Activity.PDFDetails;
import com.shape.app.Activity.PerformanceCriteria.FitnessActivity;
import com.shape.app.Activity.PerformanceCriteria.GameActivity;
import com.shape.app.Activity.PerformanceCriteria.HealthActivity;
import com.shape.app.Activity.PerformanceCriteria.MentalSkillActivity;
import com.shape.app.Activity.PerformanceCriteria.Physical_Education;
import com.shape.app.Activity.PerformanceCriteria.RelaxedAwarenessActivity;
import com.shape.app.Activity.Profile;
import com.shape.app.BuildConfig;
import com.shape.app.Helper.Constant;
import com.shape.app.Helper.ExpandableTextView;
import com.shape.app.Models.OverallGrade;
import com.shape.app.Models.PermissionModel;
import com.shape.app.Models.PhyEduModel;
import com.shape.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.CLASS_ID;
import static com.shape.app.Forms.Login.FATHER_NAME;
import static com.shape.app.Forms.Login.FATHER_PHONE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.GENDER;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.PROFILE_PIC;
import static com.shape.app.Forms.Login.SCHOOL_ID;
import static com.shape.app.Forms.Login.SCHOOL_LOGO;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.STUDENT_ID;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;


public class HomeFragment extends Fragment {
    CardView card_main, card_1, card_2, card_3, card_p, card_4, card_5;
    LinearLayout ll_lyt;
    TextView st_code, txt_class_name, behaviour_observation, overall_grade, open_dial, overall_grade_h, overall_grade_p, overall_grade_game, overall_grade_mental, overall_grade_relax;
    SharedPreferences sharedPreferences;
    String str_userid, str_code, str_pic, str_grade_fitness, str_grade_health, str_fname, str_theme, str_lname, str_grade_mental, str_relaxed_grade, str_game_grade, str_physical_grade, str_class_id, str_school_id;
    Button btn_logout;
    ImageView profile_image, menu, img_view, pdf_1, pdf_2;

    SharedPreferences.Editor editor;
    LinearLayout ll_home;
    RelativeLayout home_rel;
    ExpandableTextView report_analysis, upper_extremity, spine, lower_extremity;
    ArrayList<PhyEduModel> cbcTabModelArrayList_1 = new ArrayList<>();
    ArrayList<PhyEduModel> cbcTabModelArrayList_2 = new ArrayList<>();
    PermissionModel permissionModel;
    TextView grade_term1, grade_term2;

    LinearLayout ll_pdf_2, ll_pdf, ll_pdf_ref_2, ll_pdf_ref;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      /*  getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, //prevent Android taking a screenshot
                WindowManager.LayoutParams.FLAG_SECURE);*/
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        str_pic = sharedPreferences.getString(PROFILE_PIC, "");
        str_code = sharedPreferences.getString(STUDENT_ID, "");
        str_school_id = sharedPreferences.getString(SCHOOL_ID, "");
        str_class_id = sharedPreferences.getString(CLASS_ID, "");



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        SendImage(token);
                        //   getUser(token);

                        Log.d("TOKEN_", "onComplete: " + token);
                    }
                });

        Get_User_Token();
        ll_pdf_ref = view.findViewById(R.id.ll_pdf_ref);
        ll_pdf_ref_2 = view.findViewById(R.id.ll_pdf_ref_2);
        ll_pdf_2 = view.findViewById(R.id.ll_pdf_2);
        ll_pdf = view.findViewById(R.id.ll_pdf);
        grade_term1 = view.findViewById(R.id.grade_term1);
        grade_term2 = view.findViewById(R.id.grade_term2);
        pdf_1 = view.findViewById(R.id.pdf_1);
        pdf_2 = view.findViewById(R.id.pdf_2);
        open_dial = view.findViewById(R.id.open_dial);
        card_main = view.findViewById(R.id.card_main);
        txt_class_name = view.findViewById(R.id.txt_class_name);
        card_p = view.findViewById(R.id.card_p);
        card_1 = view.findViewById(R.id.card_1);
        card_2 = view.findViewById(R.id.card_2);
        card_3 = view.findViewById(R.id.card_3);
        card_4 = view.findViewById(R.id.card_4);
        card_5 = view.findViewById(R.id.card_5);
        ll_lyt = view.findViewById(R.id.ll_lyt);
        ll_home = view.findViewById(R.id.ll_home);
        st_code = view.findViewById(R.id.st_code);
        profile_image = view.findViewById(R.id.profile_image);
        behaviour_observation = view.findViewById(R.id.behaviour_observation);
        overall_grade = view.findViewById(R.id.overall_grade);
        overall_grade_h = view.findViewById(R.id.overall_grade_h);
        overall_grade_p = view.findViewById(R.id.overall_grade_p);
        overall_grade_game = view.findViewById(R.id.overall_grade_game);
        overall_grade_mental = view.findViewById(R.id.overall_grade_mental);
        overall_grade_relax = view.findViewById(R.id.overall_grade_relax);
        home_rel = view.findViewById(R.id.home_rel);
        img_view = view.findViewById(R.id.img_view);
        txt_class_name.setText(str_fname + " " + str_lname);
        //  Glide.with(getActivity()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);
        if (sharedPreferences.getString(GENDER, "").equals("2")) {
            Glide.with(getActivity()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.ic_female).into(profile_image);
        } else {
            Glide.with(getActivity()).load(Constant.profile_pic + str_pic).placeholder(R.drawable.man).into(profile_image);

        }
        st_code.setText(str_code);
//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getActivity(), R.drawable.curve_inner);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#00B0FF"));
//        GradientDrawable bgShape = (GradientDrawable)ll_home.getBackground();
//        bgShape.setColor(Color.RED);
        String version = "";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (!str_theme.equals("")) {
                setStatusBarColor(str_theme);
                home_rel.setBackgroundColor(Color.parseColor(str_theme));
                img_view.setBackgroundColor(Color.parseColor(str_theme));
            }
        } catch (Exception e) {
            Log.d("tag_color:-", "onCreateView: " + e.toString());
        }
        if (Constant.isNetworkAvailable(getActivity())) {
            GetData();
            GetDataTerm1();
            GetDataTerm2();
            GetPermission();
            getAppData(version);
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }

        card_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(getActivity(), GraphDetails.class);
                view.getContext().startActivity(intent);*/
            }
        });
        card_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ll_lyt.setVisibility(View.GONE);
               /* Fragment fragment = new HealthFragment();
                replaceFragment(fragment);*/

                Intent intent = new Intent(getActivity(), HealthActivity.class);
                intent.putExtra("grade", str_grade_health);
                view.getContext().startActivity(intent);
            }
        });
        card_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FitnessActivity.class);
                intent.putExtra("grade", str_grade_fitness);
                startActivity(intent);
                /*Fragment fragment = new FitnessFragment();
                replaceFragment(fragment);*/
            }
        });
        card_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MentalSkillActivity.class);
                intent.putExtra("grade", str_grade_mental);
                startActivity(intent);
                /*Fragment fragment = new FitnessFragment();
                replaceFragment(fragment);*/
            }
        });
        card_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Physical_Education.class);
                intent.putExtra("grade", str_physical_grade);
                startActivity(intent);
                /*Fragment fragment = new FitnessFragment();
                replaceFragment(fragment);*/
            }
        });
        card_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RelaxedAwarenessActivity.class);
                intent.putExtra("grade", str_relaxed_grade);
                startActivity(intent);
                /*Fragment fragment = new FitnessFragment();
                replaceFragment(fragment);*/
            }
        });
        card_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra("grade", str_game_grade);
                startActivity(intent);
                //  ll_lyt.setVisibility(View.GONE);
                /*Fragment fragment = new GameFragment();
                replaceFragment(fragment);*/
            }
        });

        return view;
    }

    private void Get_User_Token() {

    }

    @Override
    public void onResume() {
        super.onResume();
        //  getUser();
       /* GetDataTerm1Graph();
        GetDataTerm2Graph();*/
    }

    private void getAppData(String version) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.AppData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response_update", response);
                        progressDialog.dismiss();
                        String term_condition = "", privacy_policy = "", version = "";

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String message = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            String Status = jsonObject.getString("Status");

                            JSONObject jsonObject1 = jsonObject.getJSONObject("Data");

                            String app_version = jsonObject1.getString("app_version");
                            int versionCode = BuildConfig.VERSION_CODE;
                            if (code.equals("200")) {
                                Log.e("update", "onResponse: " + app_version + "==" + version);
                                if (!app_version.equals(String.valueOf(versionCode))) {
                                    ShowUpdate();
                                }

                            } else {
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(requireContext(), "" + response, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("app_version", version);
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

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

    private void ShowUpdate() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.update);
        builder.setTitle("New Update Is Available, Update the app now");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.shape.app")));

              /*  final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try {
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }*/
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    public void setStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
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

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.OverallGrade, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_conp", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    String Behaviour_Observation = jsonObject.getString("Behaviour_Observation");
                    if (!Behaviour_Observation.equals("")) {
                        behaviour_observation.setText(Behaviour_Observation);

                        if (behaviour_observation.getLineCount() >= 4) {
                            open_dial.setVisibility(View.VISIBLE);
                            open_dial.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Showdialog(Behaviour_Observation);
                                }
                            });
                        } else {
                            open_dial.setVisibility(View.GONE);
                        }

                    } else {
                        behaviour_observation.setVisibility(View.GONE);
                        open_dial.setVisibility(View.INVISIBLE);


                    }


                    if (code.equals("200")) {
                        if (jsonObject.has("OverallGrade") && !jsonObject.isNull("OverallGrade")) {
                            JSONObject overallGrade = jsonObject.getJSONObject("OverallGrade");

                            OverallGrade heartRateModel = new OverallGrade();
                            heartRateModel.fitness_grade = overallGrade.getString("fitness_grade");
                            heartRateModel.first_name = overallGrade.getString("first_name");
                            heartRateModel.health_grade = overallGrade.getString("health_grade");
                            heartRateModel.physical_grade = overallGrade.getString("physical_grade");
                            heartRateModel.game_grade = overallGrade.getString("game_grade");
                            heartRateModel.mental_grade = overallGrade.getString("mental_grade");
                            heartRateModel.relaxed_grade = overallGrade.getString("relaxed_grade");
                            heartRateModel.behaviour_observation = overallGrade.getString("behaviour_observation");

                            str_grade_fitness = heartRateModel.fitness_grade;
                            str_grade_health = heartRateModel.health_grade;
                            str_grade_mental = heartRateModel.mental_grade;
                            str_relaxed_grade = heartRateModel.relaxed_grade;
                            str_game_grade = heartRateModel.game_grade;
                            str_physical_grade = heartRateModel.physical_grade;


                            overall_grade_mental.setText(heartRateModel.mental_grade + "\nGrade");
                            overall_grade_relax.setText(heartRateModel.relaxed_grade + "\nGrade");
                            overall_grade_game.setText(heartRateModel.game_grade + "\nGrade");
                            overall_grade_p.setText(heartRateModel.physical_grade + "\nGrade");
                            overall_grade.setText(heartRateModel.fitness_grade + "\nGrade");
                            overall_grade_h.setText(heartRateModel.health_grade);
                        }

                    } else {
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();
      }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    void Showdialog(String behaviour_observation) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.demo, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle("Behaviour Observation");
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView txt = (TextView) promptsView
                .findViewById(R.id.textView1);
        txt.setText(behaviour_observation);


        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    public void GetDataTerm1() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.OverallPerformance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PhysicalEducationReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONObject OverallPerformance = jsonObject.getJSONObject("OverallPerformance");

                        String term = OverallPerformance.getString("term");
                        String pdf_data = OverallPerformance.getString("report");
                        String grade_name = OverallPerformance.getString("grade_name");
                        String reflection_pdf = OverallPerformance.getString("reflection");
                        if (reflection_pdf.equals("") || reflection_pdf.equals("null")) {
                            ll_pdf_ref.setVisibility(View.GONE);
                        } else {
                            ll_pdf_ref.setVisibility(View.VISIBLE);
                        }
                        grade_term1.setText(grade_name + "\nGrade");
                        Log.d("OverallPerformance", "onResponse: " + pdf_data);
                        pdf_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), PDFDetails.class);
                                intent.putExtra("url", pdf_data);
                                intent.putExtra("from", "12");
                                startActivity(intent);
                            }
                        });
                        ll_pdf_ref.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), PDFDetails.class);
                                intent.putExtra("url", reflection_pdf);
                                intent.putExtra("from", "14");
                                startActivity(intent);
                            }
                        });


                    } else {
                        pdf_1.setVisibility(View.GONE);
                        ll_pdf.setVisibility(View.GONE);
                        ll_pdf_ref.setVisibility(View.GONE);
                        //Toast.makeText(HealthActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                params.put("term", "term1");
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetPermission() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.GetPermission, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetPermission", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONObject OverallPerformance = jsonObject.getJSONObject("Data");
                        permissionModel = new PermissionModel();
                        permissionModel.id = OverallPerformance.getString("id");
                        permissionModel.school_id = OverallPerformance.getString("school_id");
                        permissionModel.class_id = OverallPerformance.getString("class_id");
                        permissionModel.heart_rate = OverallPerformance.getString("heart_rate");
                        permissionModel.primary_fitness = OverallPerformance.getString("primary_fitness");
                        permissionModel.secondary_fitness = OverallPerformance.getString("secondary_fitness");
                        permissionModel.bmi_report = OverallPerformance.getString("bmi_report");
                        permissionModel.cbc_level = OverallPerformance.getString("cbc_level");
                        permissionModel.musculoskeletal_report = OverallPerformance.getString("musculoskeletal_report");
                        permissionModel.physical_education = OverallPerformance.getString("physical_education");
                        permissionModel.game_skill = OverallPerformance.getString("game_skill");
                        permissionModel.mental_skill = OverallPerformance.getString("mental_skill");
                        permissionModel.relaxed_awareness = OverallPerformance.getString("relaxed_awareness");
                        permissionModel.achievement = OverallPerformance.getString("achievement");
                        permissionModel.overall_performance = OverallPerformance.getString("overall_performance");
                        permissionModel.health_perfomance = OverallPerformance.getString("health_perfomance");
                        permissionModel.fitness = OverallPerformance.getString("fitness");


                        if (permissionModel.health_perfomance.equals("0")) {
                            card_1.setVisibility(View.GONE);
                        } else {
                            card_1.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.fitness.equals("0")) {
                            card_2.setVisibility(View.GONE);
                        } else {
                            card_2.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.physical_education.equals("0")) {
                            card_p.setVisibility(View.GONE);
                        } else {
                            card_p.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.game_skill.equals("0")) {
                            card_3.setVisibility(View.GONE);
                        } else {
                            card_3.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.mental_skill.equals("0")) {
                            card_4.setVisibility(View.GONE);
                        } else {
                            card_4.setVisibility(View.VISIBLE);
                        }
                        if (permissionModel.relaxed_awareness.equals("0")) {
                            card_5.setVisibility(View.GONE);
                        } else {
                            card_5.setVisibility(View.VISIBLE);
                        }

                    } else {
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), ""+response, Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                params.put("class_id", str_class_id);
                params.put("school_id", str_school_id);
                Log.d("params_permission", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetDataTerm2() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.OverallPerformance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PhysicalEducationReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {
                        JSONObject OverallPerformance = jsonObject.getJSONObject("OverallPerformance");
                        String term = OverallPerformance.getString("term");
                        String pdf_data = OverallPerformance.getString("report");
                        String grade_name = OverallPerformance.getString("grade_name");
                        String reflection_pdf = OverallPerformance.getString("reflection");
                        if (reflection_pdf.equals("") || reflection_pdf.equals("null")) {
                            ll_pdf_ref_2.setVisibility(View.GONE);
                        } else {
                            ll_pdf_ref_2.setVisibility(View.VISIBLE);

                        }
                        grade_term2.setText(grade_name + "\nGrade");
                        Log.d("OverallPerformance", "onResponse: " + pdf_data);
                        pdf_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), PDFDetails.class);
                                intent.putExtra("url", pdf_data);
                                intent.putExtra("from", "12");
                                startActivity(intent);
                            }
                        });
                        ll_pdf_ref_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), PDFDetails.class);
                                intent.putExtra("url", reflection_pdf);
                                intent.putExtra("from", "14");
                                startActivity(intent);
                            }
                        });


                    } else {
                        pdf_2.setVisibility(View.GONE);
                        ll_pdf_2.setVisibility(View.GONE);
                        ll_pdf_ref_2.setVisibility(View.GONE);
                        //Toast.makeText(HealthActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                params.put("term", "term2");
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetDataTerm1Graph() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PhysicalEducationForGraph, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetDataTerm1Graph_1", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {

                        JSONArray jsonArrayv = jsonObject.getJSONArray("OverallPerformance");


                        JSONObject CBCLevel = jsonArrayv.getJSONObject(0);


                        PhyEduModel cbc_model = new PhyEduModel();
                        cbc_model.term = CBCLevel.getString("term");
                        cbc_model.grade = CBCLevel.getString("grade_name");


                    } else {
                        pdf_1.setVisibility(View.GONE);

                        //Toast.makeText(HealthActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                params.put("term", "term1");
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    public void GetDataTerm2Graph() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PhysicalEducationForGraph, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetDataTerm2Graph:", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");

                    if (code.equals("200")) {

                        JSONArray jsonArrayv = jsonObject.getJSONArray("PhysicalEducation");

                        for (int i = 0; i < jsonArrayv.length(); i++) {
                            JSONObject CBCLevel = jsonArrayv.getJSONObject(i);


                            PhyEduModel cbc_model = new PhyEduModel();
                            cbc_model.term = CBCLevel.getString("term");
                            cbc_model.grade = CBCLevel.getString("grade_name");
                            cbcTabModelArrayList_2.add(cbc_model);
                        }
                    } else {
                        pdf_1.setVisibility(View.GONE);

                        //Toast.makeText(HealthActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap();
                params.put("user_id", str_userid);
                params.put("term", "term2");
                Log.d("params", "getParams: " + params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("token", Constant.Token);
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    private void getUser(String token) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.UserProfile,
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
                                JSONObject jsonObject1 = jsonObject.getJSONObject("Data");
                                Log.d("resp", "onResponse: " + jsonObject1 + "===" + code);

                                String str_id = jsonObject1.getString("id");
                                String school_id = jsonObject1.getString("school_id");
                                String student_code = jsonObject1.getString("student_code");
                                String first_name = jsonObject1.getString("first_name");
                                String last_name = jsonObject1.getString("last_name");
                                String father_mobile = jsonObject1.getString("father_mobile");
                                String father_name = jsonObject1.getString("father_name");
                                String theme_color = jsonObject1.getString("theme_color");
                                String logo = jsonObject1.getString("logo");
                                String profile_pic = jsonObject1.getString("profile_pic");
                                String class_id = jsonObject1.getString("class_id");
                                String gender = jsonObject1.getString("gender");

                                SetDataToSherf(str_id, school_id, student_code, first_name, last_name, father_mobile, father_name, theme_color, logo, profile_pic, class_id, gender);


                            } else {
                                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                params.put("fcm", token);
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

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

    private void SetDataToSherf(String str_user_id, String school_id, String student_code, String first_name, String last_name, String father_mobile, String father_name, String theme_color, String logo, String profile_pic, String class_id, String gender) {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(USER_ID, str_user_id);

        editor.putString(PROFILE_PIC, profile_pic);
        editor.putString(SCHOOL_ID, school_id);
        editor.putString(CLASS_ID, class_id);
        editor.putString(STUDENT_ID, student_code);
        editor.putString(FNAME, first_name);
        editor.putString(LNAME, last_name);
        editor.putString(FATHER_NAME, father_name);
        editor.putString(FATHER_PHONE, father_mobile);
        editor.putString(THEME_COLOR, theme_color);
        editor.putString(GENDER, gender);
        editor.putString(SCHOOL_LOGO, logo);
        editor.apply();
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            Spannable wordtoSpan = new SpannableString(spanableText);

            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    LayoutInflater li = LayoutInflater.from(widget.getContext());
                    View promptsView = li.inflate(R.layout.demo, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(widget.getContext());
                    alertDialogBuilder.setTitle("Behaviour Observation");
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final TextView txt = (TextView) promptsView
                            .findViewById(R.id.textView1);
                    txt.setText("data");


                    // set dialog message
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();


                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    private void SendImage(String token) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.UpdateUserProfile,
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

                                //  Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                            } else {
                                // Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_userid);
                params.put("profile_pic", "");
                params.put("fcm", token);
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

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

}