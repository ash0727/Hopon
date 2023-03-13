package com.shape.app.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.EventTabModel;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;


public class FeedTabAdapter extends RecyclerView.Adapter<FeedTabAdapter.MyViewHolder> {
    ArrayList<EventTabModel> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private FeedTabAdapter.OnItemClickListener listener;



    public interface OnItemClickListener {
        void onItemClick(int positon, EventTabModel item, View view);
    }


    public FeedTabAdapter(Context context, ArrayList<EventTabModel> dataList, FeedTabAdapter.OnItemClickListener listener) {
        this.context = context;
        this.conVideoArrayList = dataList;
        this.listener = listener;

    }

    @NonNull
    @Override
    public FeedTabAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eventtabmodel, parent, false);
        return new FeedTabAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedTabAdapter.MyViewHolder holder, final int position) {
        final EventTabModel classModel = conVideoArrayList.get(position);

        holder.bind(position, classModel, listener);

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        holder.name.setText(conVideoArrayList.get(position).getTitle());
        holder.description.setText(conVideoArrayList.get(position).getDescription());
        holder.comment_count.setText(conVideoArrayList.get(position).getTotal_comment());
        Log.d("tag_comment", "onBindViewHolder: " + conVideoArrayList.get(position).getTotal_comment());
        holder.like_count.setText(conVideoArrayList.get(position).getTotal_like());

        if (classModel.getLike().equals("1")) {
            Glide.with(context).load(R.drawable.heart_red).into(holder.like_btn);
        } else {
            Glide.with(context).load(R.drawable.dislike).into(holder.like_btn);
        }
        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (classModel.getLike().equals("1")) {
                    DislikeMethod("0", classModel, holder);
                } else {
                    LikeMethod("1", classModel, holder);

                }
            }
        });


       /* Date date = null;
        try {
            date = fmt.parse(classModel.added_date);
            holder.dte.setText(fmtOut.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        Glide.with(context).load(Constant.FeedImage + conVideoArrayList.get(position).getMedia()).placeholder(R.drawable.img_no).into(holder.image);

       /* if (conVideoArrayList.get(position).getMedia_type().equals("0")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FeedDetails.class);
                    intent.putExtra("name", conVideoArrayList.get(position).getTitle());
                    intent.putExtra("url", conVideoArrayList.get(position).getMedia());
                    intent.putExtra("des", conVideoArrayList.get(position).getDescription());
                    intent.putExtra("like_count", conVideoArrayList.get(position).getTotal_like());
                    intent.putExtra("comment_count", conVideoArrayList.get(position).getTotal_comment());
                    intent.putExtra("like", conVideoArrayList.get(position).getLike());
                    intent.putExtra("feed_id", conVideoArrayList.get(position).getId());
                    intent.putExtra("flag", "0");//Image;
                    context.startActivity(intent);
                }
            });

        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FeedDetails.class);
                    intent.putExtra("name", conVideoArrayList.get(position).getTitle());
                    intent.putExtra("url", conVideoArrayList.get(position).getMedia());
                    intent.putExtra("des", conVideoArrayList.get(position).getDescription());
                    intent.putExtra("like_count", conVideoArrayList.get(position).getTotal_like());
                    intent.putExtra("comment_count", conVideoArrayList.get(position).getTotal_comment());
                    intent.putExtra("like", conVideoArrayList.get(position).getLike());
                    intent.putExtra("feed_id", conVideoArrayList.get(position).getId());
                    intent.putExtra("flag", "1");//Image;


                    context.startActivity(intent);
                }
            });
        }*/
/*
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey i am using hopon app";//url or content
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hello");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share To"));
            }
        });
*/
/*
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommentBottomSheet bottomSheet = new CommentBottomSheet();
                bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(),
                        "ModalBottomSheet",classModel);
            }
        });
*/


    }

    private void LikeMethod(String flag_like, EventTabModel classModel, MyViewHolder holder) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedLike,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id, TotalLike;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");
                            TotalLike = jsonObject.getString("TotalLike");

                            Log.d("resp", "onResponse: " + jsonObject + "===" + TotalLike);
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                Glide.with(context).load(R.drawable.heart_red).into(holder.like_btn);
                                holder.like_count.setText(TotalLike);
                                // Toast.makeText(context, "Like Successfully!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("feed_id", classModel.getId());
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

        Volley.newRequestQueue(context).add(stringRequest);


    }

    private void DislikeMethod(String flag_like, EventTabModel classModel, MyViewHolder holder) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.FeedLike,response -> {

                    String code, message, id, TotalLike;

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        code = jsonObject.getString("code");
                        message = jsonObject.getString("message");
                        TotalLike = jsonObject.getString("TotalLike");

                        Log.d("resp", "onResponse: " + jsonObject + "===" + code);
                        if (code.equals("200")) {

                            Log.d("resp_", "onResponse: " + jsonObject + "===" + TotalLike);
                            // Glide.with(context).load(R.drawable.dislike).into(holder.like_btn);
                            holder.like_btn.setImageResource(R.drawable.dislike);
                            holder.like_count.setText(TotalLike);
                            // Toast.makeText(context, "DisLike Successfully!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("feed_id", classModel.getId());
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

        Volley.newRequestQueue(context).add(stringRequest);


    }


    public EventTabModel getItem(int position) {
        return conVideoArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, like_count, comment_count;
        ImageView share, image, comment, like_btn;
        LinearLayout ll_card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            share = itemView.findViewById(R.id.share);
            comment = itemView.findViewById(R.id.comment);
            like_btn = itemView.findViewById(R.id.like_btn);
            like_count = itemView.findViewById(R.id.like_count);
            comment_count = itemView.findViewById(R.id.comment_count);
            ll_card = itemView.findViewById(R.id.ll_card);

        }

        public void bind(int position, EventTabModel classModel, OnItemClickListener listener) {
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(position, classModel, v);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(position, classModel, v);
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(position, classModel, v);
                }
            });

        }
    }

}