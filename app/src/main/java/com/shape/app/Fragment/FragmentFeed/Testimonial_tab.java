package com.shape.app.Fragment.FragmentFeed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shape.app.Adapters.FeedTabAdapter;
import com.shape.app.Fragment.Comment.Comment_F;
import com.shape.app.Fragment.FeedDetails_Fragment;
import com.shape.app.Fragment.Share.VideoAction_F;
import com.shape.app.Helper.Constant;
import com.shape.app.Interface.Fragment_Callback;
import com.shape.app.Interface.Fragment_Data_Send;
import com.shape.app.Models.EventTabModel;
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
import static com.shape.app.Forms.Login.USER_ID;


public class Testimonial_tab extends Fragment implements Fragment_Data_Send {
    public static Fragment newInstance() {
        Testimonial_tab fragment = new Testimonial_tab();
        return fragment;

    }

    ArrayList<EventTabModel> eventTabModelArrayList = new ArrayList<>();
    RecyclerView recyclerView_event;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    FeedTabAdapter adapter;
    ArrayList<EventTabModel> data_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testimonial_tab, container, false);
        SherfPrefes();
        eventTabModelArrayList = new ArrayList<>();
        recyclerView_event = view.findViewById(R.id.recyclerView_event);
        recyclerView_event.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Log.d("list_size_eve", "onCreateView: " + eventTabModelArrayList.size());


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.isNetworkAvailable(getActivity())) {
            getEventList();
           // Toast.makeText(getActivity(), "all 4_resume", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }


    private void getEventList() {
        data_list = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FEED_LIST_TAB, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("quick_1", "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        eventTabModelArrayList = new ArrayList<>();

                        JSONArray jsonArrayvideo = jsonObject.getJSONArray("List");
                        for (int i = 0; i < jsonArrayvideo.length(); i++) {
                            JSONObject jsonObject1 = jsonArrayvideo.getJSONObject(i);
                            EventTabModel video = new EventTabModel();
                            video.id = jsonObject1.getString("id");
                            video.title = jsonObject1.getString("title");
                            video.media_type = jsonObject1.getString("media_type");
                            video.media = jsonObject1.getString("media");
                            video.updated_date = jsonObject1.getString("updated_date");
                            video.feed_type = jsonObject1.optString("feed_type");
                            video.description = jsonObject1.optString("description");
                            video.added_date = jsonObject1.optString("added_date");
                            video.updated_date = jsonObject1.optString("updated_date");
                            video.like = jsonObject1.optString("like");
                            video.total_like = jsonObject1.optString("total_like");
                            video.total_comment = jsonObject1.optString("total_comment");
                            eventTabModelArrayList.add(video);
                        }
                       // recyclerView_event.setAdapter(new FeedTabAdapter(eventTabModelArrayList, (AppCompatActivity) getContext()));
                        if (!eventTabModelArrayList.isEmpty()) {
                            data_list.addAll(eventTabModelArrayList);
                            Set_Adapter();
                        }
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
                params.put("feed_type", "Testimonial");
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

    }

    public void Set_Adapter() {

        adapter = new FeedTabAdapter(getActivity(), data_list, new FeedTabAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positon, EventTabModel item, View view) {
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
                                        intent.putExtra(Intent.EXTRA_EMAIL, "admin@gmail.com");
                                        intent.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + item.getTitle() + "\nFeed Description: " + item.getDescription()+"\nFeed Type: "+item.getFeed_type());
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
                                        shareIntent1.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + item.getTitle() + "\nFeed Description: " + item.getDescription()+"\nFeed Type: "+item.getFeed_type());
                                        shareIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        getActivity().startActivity(Intent.createChooser(shareIntent1, getString(R.string.share)));

                                        break;
                                    case VideoAction_F.FACEBOOK_SHARE:

                                        if (isPackageInstalled("com.facebook.katana")) {
                                            Intent fb_share = new Intent("com.facebook.feeds.ADD_TO_FEED");
                                            fb_share.setType("text/plain");
                                            fb_share.setPackage("com.facebook.katana");
                                            fb_share.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + item.getTitle() + "\nFeed Description: " + item.getDescription()+"\nFeed Type: "+item.getFeed_type());
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
                                            insta_share.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + item.getTitle() + "\nFeed Description: " + item.getDescription()+"\nFeed Type: "+item.getFeed_type());
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
                        OpenComment(item);
                        break;
                    case R.id.ll_card:
                        onPause();
                        OpenDetails(item);
                        break;


                }
            }

        });

        //adapter.setHasStableIds(true);
        recyclerView_event.setAdapter(adapter);

    }
    private void OpenDetails(EventTabModel item) {
        FeedDetails_Fragment comment_f = new FeedDetails_Fragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
      //  transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("name", item.getTitle());
        args.putString("url", item.getMedia());
        args.putString("des", item.getDescription());
        args.putString("like", item.getLike());
        args.putString("like_count", item.getTotal_like());
        args.putString("comment_count", item.getTotal_comment());
        args.putString("feed_id", item.getId());
        args.putString("flag", item.getMedia_type());//0-image 1-video
        Log.d("tag_flag_intent", "OpenDetails: " + item.getMedia_type());
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_container, comment_f).commit();


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

    private void OpenComment(EventTabModel item) {

        int comment_counnt = Integer.parseInt(item.total_comment);

        Fragment_Data_Send fragment_data_send = this;

        Comment_F comment_f = new Comment_F(comment_counnt, fragment_data_send);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id", item.id);
        args.putString("user_id", str_userid);
        args.putString("comment_counnt", String.valueOf(comment_counnt));
        args.putString("from", "home");
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_container, comment_f).commit();

    }

    @Override
    public void onDataSent(String yourData) {

    }
}