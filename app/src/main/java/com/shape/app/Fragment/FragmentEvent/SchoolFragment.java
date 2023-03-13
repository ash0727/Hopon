package com.shape.app.Fragment.FragmentEvent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shape.app.Fragment.Share.VideoAction_F;
import com.shape.app.Interface.Fragment_Callback;
import com.shape.app.R;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shape.app.Adapters.FeedTabAdapter;
import com.shape.app.Adapters.EventAdapter;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.FeedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.SCHOOL_ID;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

public class SchoolFragment extends Fragment {
    CardView card_1;
    FeedTabAdapter eventTabAdapter;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    SharedPreferences sharedPreferences;
    String str_userid, str_school_id, str_name, str_fname, str_lname;
    ArrayList<FeedModel> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;

    public static Fragment newInstance() {
        SchoolFragment fragment = new SchoolFragment();
        return fragment;
    }

    EventAdapter adapter;
    ArrayList<FeedModel> data_list;
    int check = 0;
    String[] Months = {"Select Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    RelativeLayout not_item_lyt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school, container, false);
        SherfPrefes();

        not_item_lyt = view.findViewById(R.id.not_item_lyt);
        recyclerView_event = view.findViewById(R.id.recyclerView_event);
        recyclerView_event.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        Spinner spin = (Spinner) view.findViewById(R.id.month);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (check++ >= 1) {
                    Toast.makeText(getActivity(), "" + id + "pos:" + position, Toast.LENGTH_SHORT).show();
                    int month_int = (int) id;
                    int year = Calendar.getInstance().get(Calendar.YEAR);
                    getEventListByMonth(month_int, year);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, Months);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        return view;
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

    private void SherfPrefes() {
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_school_id = sharedPreferences.getString(SCHOOL_ID, "");

    }

    private void getEventListByMonth(int month_int, int year) {
        data_list = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EventFilter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_1", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        eventTabModelArrayList = new ArrayList<>();

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("FilteredData");
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayvideo.getJSONObject(i);
                            FeedModel video = new FeedModel();
                            video.id = jsonObject1.getString("id");
                            video.title = jsonObject1.getString("title");
                            video.media_type = jsonObject1.getString("media_type");
                            video.media = jsonObject1.getString("media");
                            video.updated_date = jsonObject1.getString("updated_date");
                            video.location = jsonObject1.optString("location");
                            video.description = jsonObject1.optString("description");
                            video.Validity = jsonObject1.optString("Validity");
                            video.added_date = jsonObject1.optString("added_date");
                            video.flag = jsonObject1.optString("flag");
                            video.interested = jsonObject1.optString("interested");
                            video.month_year = jsonObject1.optString("month_year");
                            eventTabModelArrayList.add(video);
                        }
                        recyclerView_event.setAdapter(new EventAdapter(eventTabModelArrayList, (AppCompatActivity) getContext()));
                        if (!eventTabModelArrayList.isEmpty()) {
                            data_list.addAll(eventTabModelArrayList);
                            Set_Adapter();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Event Not Found!", Toast.LENGTH_SHORT).show();
                        eventTabModelArrayList.clear();
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
                params.put("month", String.valueOf(month_int));
                params.put("year", String.valueOf(year));
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

    private void getEventList() {
        data_list = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ALL_EVENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_1", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        not_item_lyt.setVisibility(View.GONE);
                        recyclerView_event.setVisibility(View.VISIBLE);
                        eventTabModelArrayList = new ArrayList<>();

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("List");
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayvideo.getJSONObject(i);
                            FeedModel video = new FeedModel();
                            video.id = jsonObject1.getString("id");
                            video.title = jsonObject1.getString("title");
                            video.media_type = jsonObject1.getString("media_type");
                            video.media = jsonObject1.getString("media");
                            video.updated_date = jsonObject1.getString("updated_date");
                            video.location = jsonObject1.optString("location");
                            video.description = jsonObject1.optString("description");
                            video.added_date = jsonObject1.optString("added_date");
                            video.flag = jsonObject1.optString("flag");
                            video.interested = jsonObject1.optString("interested");
                            video.Validity = jsonObject1.optString("Validity");
                            video.month_year = jsonObject1.optString("month_year");
                            eventTabModelArrayList.add(video);
                        }

                        recyclerView_event.setAdapter(new EventAdapter(eventTabModelArrayList, (AppCompatActivity) getContext()));
                        if (!eventTabModelArrayList.isEmpty()) {
                            data_list.addAll(eventTabModelArrayList);
                            Set_Adapter();
                        }
                    } else {
                        progressDialog.dismiss();
                        not_item_lyt.setVisibility(View.VISIBLE);
                        recyclerView_event.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    not_item_lyt.setVisibility(View.VISIBLE);
                    recyclerView_event.setVisibility(View.GONE);

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
                params.put("event_type", "School");
                params.put("user_id", str_userid);
                params.put("school_id", str_school_id);
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

    public boolean isPackageInstalled(String packagename) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Not installed", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public void Set_Adapter() {

        adapter = new EventAdapter(getActivity(), data_list, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positon, FeedModel item, View view) {
                switch (view.getId()) {

                    case R.id.share:
                        onPause();
                        final VideoAction_F fragment = new VideoAction_F(item.title, new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {

                                String action = bundle.getString("action");

                                switch (action) {

                                    case VideoAction_F.NORMAL_SHARE:
                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                        intent.putExtra(Intent.EXTRA_EMAIL, "");
                                        intent.putExtra(Intent.EXTRA_TEXT, "Event Name:" + item.getTitle() + "\nEvent Date:" + item.month_year + "\nEvent Description:" + item.getDescription() + "\nEvent Location:" + item.getLocation());
                                        intent.putExtra(Intent.EXTRA_SUBJECT, "" + item.getTitle());
                                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                            startActivity(intent);
                                        }
                                        break;
                                    case VideoAction_F.WHATSAPP_SHARE:
                                        Intent shareIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                                        shareIntent1.setType("text/plain");
                                        shareIntent1.setPackage("com.whatsapp");
                                        shareIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hopon");
                                        shareIntent1.putExtra(android.content.Intent.EXTRA_TITLE, "Hopon");
                                        shareIntent1.putExtra(Intent.EXTRA_TEXT, "Event Name:" + item.getTitle() + "\nEvent Date:" + item.month_year + "\nEvent Description:" + item.getDescription() + "\nEvent Location:" + item.getLocation());
                                        shareIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        getActivity().startActivity(Intent.createChooser(shareIntent1, getString(R.string.share)));

                                        break;
                                    case VideoAction_F.FACEBOOK_SHARE:

                                        if (isPackageInstalled("com.facebook.katana")) {
                                            Intent fb_share = new Intent("com.facebook.feeds.ADD_TO_FEED");
                                            fb_share.setType("text/plain");
                                            fb_share.setPackage("com.facebook.katana");
                                            fb_share.putExtra(Intent.EXTRA_TEXT, "Event Name:" + item.getTitle() + "\nEvent Date:" + item.month_year + "\nEvent Description:" + item.getDescription() + "\nEvent Location:" + item.getLocation());
                                            fb_share.putExtra("com.facebook.platform.extra.APPLICATION_ID", getContext().getPackageName());
                                            Activity activity = getActivity();
                                            if (activity.getPackageManager().resolveActivity(fb_share, 0) != null) {
                                                activity.startActivityForResult(fb_share, 0);
                                            }

                                        }
                                        break;

                                    case VideoAction_F.INSTRAGRAM_SHARE:
                                        if (isPackageInstalled("com.instagram.android")) {
                                            Intent insta_share = new Intent("com.instagram.share.ADD_TO_FEED");
                                            insta_share.setType("text/plain");
                                            insta_share.setPackage("com.instagram.android");
                                            insta_share.putExtra(Intent.EXTRA_TEXT, "Event Name:" + item.getTitle() + "\nEvent Date:" + item.month_year + "\nEvent Description:" + item.getDescription() + "\nEvent Location:" + item.getLocation());
                                            Activity activity = getActivity();
                                            if (activity.getPackageManager().resolveActivity(insta_share, 0) != null) {
                                                activity.startActivityForResult(insta_share, 0);
                                            }
                                        }
                                        break;


                                }

                            }
                        });
                        Bundle bundle = new Bundle();
                        bundle.putString("video_id", item.title);
                        bundle.putString("user_id", item.description);
                        fragment.setArguments(bundle);
                        fragment.show(getChildFragmentManager(), "");
                        // Toast.makeText(getActivity(), "" + eventTabModelArrayList.get(positon).getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.comment:
                        onPause();
                        break;

                    case R.id.id_tx_inter:
                        onPause();
                        if (item.getInterested().equals("1")) {
                            DislikeMethod("0", item);
                        } else {
                            DislikeMethod("1", item);

                        }
                        break;
                }
            }

        });

        //adapter.setHasStableIds(true);
        recyclerView_event.setAdapter(adapter);

    }

    private void DislikeMethod(String flag_like, FeedModel classModel) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EventInterested,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===");
                                //holder.id_tx_inter.setBackgroundResource(R.color.grey_20);
                                getEventList();
                                // Toast.makeText(getActivity(), "DisLike Successfully!", Toast.LENGTH_SHORT).show();

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
                params.put("event_id", classModel.getId());
                params.put("user_id", str_userid);
                params.put("flag", flag_like);
                Log.d("params_dis_like", "getParams: " + params);
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