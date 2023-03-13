package com.shape.app.Fragment.Comment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shape.app.Helper.Constant;
import com.shape.app.Interface.Fragment_Data_Send;
import com.shape.app.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;


public class Comment_F extends RootFragment {

    View view;
    Context context;
    SharedPreferences sharedPreferences;
    String str_userid;
    RecyclerView recyclerView;

    Comments_Adapter adapter;

    ArrayList<Comment_Get_Set> data_list;

    String video_id;
    String user_id;
    String from;

    EditText message_edit;
    ImageButton send_btn;
    ProgressBar send_progress;

    TextView comment_count_txt;

    FrameLayout comment_screen;

    public static int comment_count = 0;

    public Comment_F() {

    }

    Fragment_Data_Send fragment_data_send;

    @SuppressLint("ValidFragment")
    public Comment_F(int count, Fragment_Data_Send fragment_data_send) {
        comment_count = count;
        this.fragment_data_send = fragment_data_send;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comment, container, false);
        context = getContext();
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        comment_screen = view.findViewById(R.id.comment_screen);
        comment_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });

        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            video_id = bundle.getString("video_id");
            user_id = bundle.getString("user_id");
            from = bundle.getString("from");
            Log.d("tag_user_id", "onCreateView: " + video_id);
        }


        comment_count_txt = view.findViewById(R.id.comment_count);

        recyclerView = view.findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);


        data_list = new ArrayList<>();
        adapter = new Comments_Adapter(context, data_list, new Comments_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, Comment_Get_Set item, View view) {
                switch (view.getId()) {
                    case R.id.imgdeletecoment:
                        //deletecomment(item.comment_id);
                        break;
                    case R.id.user_pic:
                        // OpenProfile(item , false);

                        break;

                    case R.id.username:
                        // OpenProfile(item , false);

                        break;
                }

            }
        });

        recyclerView.setAdapter(adapter);


        message_edit = view.findViewById(R.id.message_edit);


        send_progress = view.findViewById(R.id.send_progress);
        send_btn = view.findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = message_edit.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    Send_Comments(video_id, message);
                    message_edit.setText(null);
                    send_progress.setVisibility(View.VISIBLE);
                    send_btn.setVisibility(View.GONE);

                }

            }
        });


        Get_All_Comments(video_id);


        return view;
    }


    @Override
    public void onDetach() {
        hideSoftKeyboard(getActivity());

        super.onDetach();
    }

    // this funtion will get all the comments against post
    public void Get_All_Comments(String video_id) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LIST_FEED_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_1", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        data_list.clear();
                        //  eventTabModelArrayList = new ArrayList<>();
                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("listing");
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            Comment_Get_Set video = new Comment_Get_Set();

                            JSONObject jsonObject1 = jsonArrayvideo.getJSONObject(i);
                            video.id = jsonObject1.getString("id");
                            video.user_id = jsonObject1.getString("user_id");
                            video.feed_id = jsonObject1.getString("feed_id");
                            video.comments = jsonObject1.getString("comment");
                            video.added_date = jsonObject1.getString("added_date");
                            video.first_name = jsonObject1.getString("first_name");
                            video.father_name = jsonObject1.getString("father_name");
                            video.last_name = jsonObject1.getString("last_name");
                            video.profile_pic = jsonObject1.getString("profile_pic");
                            video.gender = jsonObject1.getString("gender");
                            // eventTabModelArrayList.add(video);
                            data_list.add(video);
                        }
                        adapter.notifyDataSetChanged();

                        comment_count_txt.setText(data_list.size() + " comments");

                        //  recyclerView_event.setAdapter(new Comments_Adapter((AppCompatActivity) getActivity(),eventTabModelArrayList));

                    } else {
                        progressDialog.dismiss();
                       // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                params.put("feed_id", video_id);
                params.put("user_id", str_userid);
                Log.d("parameter_tab_feed", "getParams: " + params);
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


    // this function will call an api to upload your comment
    public void Send_Comments(final String feed_id, final String message) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_FEED_COMMENT,
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

                                send_progress.setVisibility(View.GONE);
                                send_btn.setVisibility(View.VISIBLE);
                                getActivity().onBackPressed();
                                //SendOTP(str_phone);
                                comment_count++;

                                comment_count_txt.setText(comment_count + " comments");

                                if (fragment_data_send != null)
                                    fragment_data_send.onDataSent("" + comment_count);

                            } else {
                                Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                            }

                            adapter.notifyDataSetChanged();

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
                params.put("feed_id", feed_id);
                params.put("user_id", str_userid);
                params.put("comment", message);
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
