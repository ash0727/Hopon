package com.shape.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shape.app.Adapters.Achievement_Adapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.AchievementModel;
import com.shape.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;


public class AchievementFragment extends Fragment {
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_theme, str_lname;
    CardView card_main;


    ArrayList<AchievementModel> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    LottieAnimationView animationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");
        animationView = view.findViewById(R.id.animationView);
        try {
            if (!str_theme.equals("")) {
                setStatusBarColor(str_theme);
//                home_rel.setBackgroundColor(Color.parseColor(str_theme));
//                img_view.setBackgroundColor(Color.parseColor(str_theme));
            }
        } catch (Exception e) {
            Log.d("tag_color:-", "onCreateView: " + e.toString());
        }
        recyclerView_event = view.findViewById(R.id.recyclerView_ac);
        recyclerView_event.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Log.d("3", "onCreateView: " + eventTabModelArrayList.size());

        if (Constant.isNetworkAvailable(getActivity())) {
            GetData();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void GetData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.Achievement, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BMIReport", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        //ShowFirework();
                        animationView.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animationView.setVisibility(View.GONE);
                            }
                        }, 3000);

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("AchievementReport");

                        if (jsonArrayvideo != null) {
                            for (int i = 0; i < jsonArrayvideo.length(); i++) {
                                AchievementModel bmi_model = new AchievementModel();
                                JSONObject BMIReport = jsonArrayvideo.getJSONObject(i);

                                bmi_model.id = BMIReport.getString("id");
                                bmi_model.event_date = BMIReport.getString("event_date");
                                bmi_model.event_name = BMIReport.getString("event_name");
                                bmi_model.sports = BMIReport.getString("sports");
                                bmi_model.level = BMIReport.getString("level");
                                bmi_model.achievement = BMIReport.getString("achievement");
                                bmi_model.certificate = BMIReport.getString("certificate");
                                bmi_model.remark = BMIReport.getString("remark");
                                bmi_model.location = BMIReport.getString("location");
                                bmi_model.images = BMIReport.getJSONArray("images");
                                eventTabModelArrayList.add(bmi_model);
                            }
                            recyclerView_event.setAdapter(new Achievement_Adapter(eventTabModelArrayList, (AppCompatActivity) getActivity()));


                        } else {
                            Log.d("null", "onResponse: ");
                        }


                    } else {
                        Toast.makeText(getActivity(), "No Achievements Added", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        RetryPolicy retryPolicy = new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);

    }

    private void ShowFirework() {

        final androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater_1 = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater_1.inflate(R.layout.load_dialog, null);
        alertDialogBuilder.setView(view2);
        alertDialogBuilder.setCancelable(true);
        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 5000);

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

}