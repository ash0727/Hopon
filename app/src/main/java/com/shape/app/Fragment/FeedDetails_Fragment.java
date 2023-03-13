package com.shape.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shape.app.Activity.VideoDetails;
import com.shape.app.Fragment.Comment.Comment_F;
import com.shape.app.Fragment.Share.VideoAction_F;
import com.shape.app.Helper.Constant;
import com.shape.app.Interface.Fragment_Callback;
import com.shape.app.Interface.Fragment_Data_Send;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;


public class FeedDetails_Fragment extends Fragment implements Fragment_Data_Send {

    String name, url, des, str_like, str_comt_count, str_like_count, str_feed_id,feed_type;
    TextView name_name, description, like_count, comment_count;
    ImageView imageView, share, comment, like_btn, icon_playbtn;

    SharedPreferences sharedPreferences;
    String str_userid, str_intent_flag;
    CardView card_image;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_feed_details, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            str_feed_id = bundle.getString("feed_id");
            str_intent_flag = bundle.getString("flag");

            Log.d("tag_user_id", "onCreateView: " + str_intent_flag);
        }


        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        icon_playbtn = view.findViewById(R.id.icon_playbtn);
        card_image = view.findViewById(R.id.image_layout);

        name_name = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.image);
        share = view.findViewById(R.id.share);
        comment = view.findViewById(R.id.comment);
        like_btn = view.findViewById(R.id.like_btn);
        like_count = view.findViewById(R.id.like_count);
        comment_count = view.findViewById(R.id.comment_count);

        Log.d("tag_flag_intent_", "OpenDetails: " + str_intent_flag);

        GetFeedDetails(str_feed_id, str_userid);

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_like.equals("1")) {
                    DislikeMethod("0");
                } else {
                    LikeMethod("1");

                }
            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommentBottomSheet bottomSheet = new CommentBottomSheet();
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                OpenComment();
            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        GetFeedDetails(str_feed_id, str_userid);
    }

    @Override
    public void onDataSent(String yourData) {

    }

    private void LikeMethod(String flag_like) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedLike,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id, total_like;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");
                            total_like = jsonObject.getString("TotalLike");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                Glide.with(getActivity()).load(R.drawable.heart_red).into(like_btn);
                                like_count.setText(total_like);
                                //Toast.makeText(getApplicationContext(), "Like Successfully!", Toast.LENGTH_SHORT).show();

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
                params.put("feed_id", str_feed_id);
                params.put("user_id", str_userid);
                params.put("flag", "1");
                Log.d("params_like", "getParams: " + params);
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

    private void DislikeMethod(String flag_like) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedLike,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id, total_like;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");
                            total_like = jsonObject.getString("TotalLike");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                // Glide.with(context).load(R.drawable.dislike).into(holder.like_btn);
                                like_btn.setImageResource(R.drawable.dislike);
                                like_count.setText(total_like);
                                // Toast.makeText(getApplicationContext(), "DisLike Successfully!", Toast.LENGTH_SHORT).show();

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
                params.put("feed_id", str_feed_id);
                params.put("user_id", str_userid);
                params.put("flag", "0");
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

    private void OpenComment() {

        int comment_counnt = Integer.parseInt(str_comt_count);

        Fragment_Data_Send fragment_data_send = this;

        Comment_F comment_f = new Comment_F(comment_counnt, fragment_data_send);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id", str_feed_id);
        args.putString("user_id", str_userid);
        args.putString("comment_counnt", String.valueOf(comment_counnt));
        args.putString("from", "home");
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame_container, comment_f).commit();

    }

    public boolean isPackageInstalled(String packagename) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Not installed", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    private void GetFeedDetails(String str_feed_id, String str_userid) {
        Log.d("tag_id", "GetFeedDetails: " + str_feed_id + str_userid);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("quick_1_list", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String code = jsonObject.getString("code");
                            if (code.equals("200")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("Data");
                                name = jsonObject1.getString("title");
                                url = jsonObject1.getString("media");
                                feed_type = jsonObject1.getString("feed_type");
                                des = jsonObject1.optString("description");
                                str_like = jsonObject1.optString("user_like");
                                str_like_count = jsonObject1.optString("total_like");
                                str_comt_count = jsonObject1.optString("total_comment");


                                if (str_intent_flag.equals("0")) {
                                    icon_playbtn.setVisibility(View.GONE);
                                } else {
                                    icon_playbtn.setVisibility(View.VISIBLE);
                                    card_image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), VideoDetails.class);
                                            intent.putExtra("url", url);
                                            intent.putExtra("from", "0");//feed
                                            getActivity().startActivity(intent);
                                        }
                                    });

                                }
                                Glide.with(getActivity()).load(Constant.FeedImage + url).into(imageView);
                                name_name.setText(name);
                                description.setText(des);
                                like_count.setText(str_like_count);
                                comment_count.setText(str_comt_count);
                                Log.d("str_like_h", "onResponse: " + str_like);
                                if (str_like.equals("1")) {
                                    Glide.with(getActivity()).load(R.drawable.heart_red).placeholder(R.drawable.heart_red).into(like_btn);
                                } else {
                                    Glide.with(getActivity()).load(R.drawable.dislike).placeholder(R.drawable.dislike).into(like_btn);
                                }

                                share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        final VideoAction_F fragment = new VideoAction_F("", new Fragment_Callback() {
                                            @Override
                                            public void Responce(Bundle bundle) {

                                                String action = bundle.getString("action");

                                                switch (action) {

                                                    case VideoAction_F.NORMAL_SHARE:
                                                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                                                        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                                                        intent.putExtra(Intent.EXTRA_EMAIL, "");
                                                        intent.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + name + "\nFeed Description: " + des+"\nFeed Type: "+feed_type);
                                                        intent.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                            startActivity(intent);
                                                        }
                                                        break;
                                                    case VideoAction_F.WHATSAPP_SHARE:
                                                        Intent shareIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                                                        shareIntent1.setType("text/plain");
                                                        shareIntent1.setPackage("com.whatsapp");
                                                        shareIntent1.putExtra(Intent.EXTRA_SUBJECT, "" + name);
                                                        shareIntent1.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + name + "\nFeed Description: " + des+"\nFeed Type: "+feed_type);
                                                        shareIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(shareIntent1);

                                                        break;
                                                    case VideoAction_F.FACEBOOK_SHARE:

                                                        if (isPackageInstalled("com.facebook.katana")) {
                                                            Intent fb_share = new Intent("com.facebook.feeds.ADD_TO_FEED");
                                                            fb_share.setType("text/plain");
                                                            fb_share.setPackage("com.facebook.katana");
                                                            fb_share.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + name + "\nFeed Description: " + des+"\nFeed Type: "+feed_type);
                                                            fb_share.putExtra("com.facebook.platform.extra.APPLICATION_ID", getActivity().getPackageName());
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
                                                            insta_share.putExtra(Intent.EXTRA_TEXT, "Feed Title: " + name + "\nFeed Description: " + des+"\nFeed Type: "+feed_type);
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
                                        bundle.putString("video_id", name);
                                        bundle.putString("user_id", "-");
                                        fragment.setArguments(bundle);
                                        fragment.show(getActivity().getSupportFragmentManager(), "");

                                    }
                                });

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
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("feed_id", str_feed_id);
                params.put("user_id", str_userid);
                Log.d("params_like", "getParams: " + params);
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