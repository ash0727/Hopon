package com.shape.app.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.shape.app.Adapters.AppointmentAdapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.AppointmentModel;
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


public class AppointmentFragment extends Fragment {
    CardView card_main;


    ArrayList<AppointmentModel> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname,str_theme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        SherfPrefes();
        eventTabModelArrayList = new ArrayList<>();
        recyclerView_event = view.findViewById(R.id.recyclerView_event);
        recyclerView_event.setLayoutManager((new GridLayoutManager(getActivity(), 2)));
        Log.d("3", "onCreateView: " + eventTabModelArrayList.size());
        try {
            if (!str_theme.equals("")) {
                setStatusBarColor(str_theme);
//                home_rel.setBackgroundColor(Color.parseColor(str_theme));
//                img_view.setBackgroundColor(Color.parseColor(str_theme));
            }
        } catch (Exception e) {
            Log.d("tag_color:-", "onCreateView: " + e.toString());
        }
        return view;
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

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.isNetworkAvailable(getActivity())) {
            getEventList();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventList() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.List_APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_1", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        eventTabModelArrayList = new ArrayList<>();

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("Data");
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            AppointmentModel video = new AppointmentModel();

                            JSONObject jsonObject1 = jsonArrayvideo.getJSONObject(i);
                            video.id = jsonObject1.getString("id");
                            video.name = jsonObject1.getString("name");
                            video.image = jsonObject1.getString("image");
                            eventTabModelArrayList.add(video);
                        }
                        recyclerView_event.setAdapter(new AppointmentAdapter(eventTabModelArrayList, (AppCompatActivity) getActivity()));

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                Log.d("parameter_tab", "getParams: " + params);
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

    private void SherfPrefes() {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");


    }
}