package com.shape.app.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.shape.app.Activity.EventDetails;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.FeedModel;
import com.shape.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    ArrayList<FeedModel> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fmt_month_year = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private EventAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, FeedModel item, View view);
    }


    public EventAdapter(Context context, ArrayList<FeedModel> dataList, EventAdapter.OnItemClickListener listener) {
        this.context = context;
        this.conVideoArrayList = dataList;
        this.listener = listener;

    }

    public EventAdapter(ArrayList<FeedModel> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed_lyt, parent, false);
        return new EventAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, final int position) {
        final FeedModel classModel = conVideoArrayList.get(position);

        holder.bind(position, classModel, listener);


        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");

        holder.name.setText(classModel.getTitle());
        holder.description.setText(classModel.getDescription());
        //holder.date.setText(classModel.getAdded_date());
        holder.location.setText(classModel.getLocation());
        Date date = null;
        try {
            date = fmt_month_year.parse(classModel.month_year);
            holder.date.setText(fmtOut.format(date));
            new_date = fmtOut.format(date);
            Log.d("tag_adapter_date", "onBindViewHolder: " + new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (classModel.getInterested().equals("1")) {
            holder.id_tx_inter.setBackgroundResource(R.color.bg_yellow);
        } else {
            holder.id_tx_inter.setBackgroundResource(R.color.grey_20);
        }

      /*  holder.id_tx_inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classModel.getInterested().equals("1")) {
                    DislikeMethod("0", classModel, holder);
                } else {
                    LikeMethod("1", classModel, holder);

                }
            }
        });*/

        Glide.with(context).load(Constant.Event_img + classModel.getMedia()).placeholder(R.drawable.img_no).into(holder.image);
        if (conVideoArrayList.get(position).getMedia_type().equals("0")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, EventDetails.class);
                    intent.putExtra("name", conVideoArrayList.get(position).getTitle());
                    intent.putExtra("url", conVideoArrayList.get(position).getMedia());
                    intent.putExtra("des", conVideoArrayList.get(position).getDescription());
                    intent.putExtra("id", conVideoArrayList.get(position).getId());
                    intent.putExtra("location", conVideoArrayList.get(position).getLocation());
                    intent.putExtra("date", conVideoArrayList.get(position).month_year);
                    intent.putExtra("in_flag", "0");
                    intent.putExtra("flag", conVideoArrayList.get(position).getInterested());
                    intent.putExtra("valid", conVideoArrayList.get(position).Validity);
                    Log.d("tag_adapter_date_1", "onBindViewHolder: " + new_date);

                    context.startActivity(intent);
                }
            });

        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EventDetails.class);
                    intent.putExtra("name", conVideoArrayList.get(position).getTitle());
                    intent.putExtra("url", conVideoArrayList.get(position).getMedia());
                    intent.putExtra("des", conVideoArrayList.get(position).getDescription());
                    intent.putExtra("location", conVideoArrayList.get(position).getLocation());
                    intent.putExtra("flag", conVideoArrayList.get(position).getInterested());
                    intent.putExtra("id", conVideoArrayList.get(position).getId());
                    intent.putExtra("in_flag", "1");
                    intent.putExtra("date", conVideoArrayList.get(position).month_year);
                    Log.d("tag_adapter_date_2", "onBindViewHolder: " + new_date);

                    context.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, date, location, id_tx_inter;
        ImageView share, image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.image);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            id_tx_inter = itemView.findViewById(R.id.id_tx_inter);
            share = itemView.findViewById(R.id.share);

        }

        public void bind(int position, FeedModel classModel, EventAdapter.OnItemClickListener listener) {
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, classModel, v);
                }
            });
            id_tx_inter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (classModel.Validity.equals("Valid")) {
                        listener.onItemClick(position, classModel, v);
                    } else {
                        id_tx_inter.setEnabled(true);
                    }
                }
            });

        }

    }

    private void LikeMethod(String flag_like, FeedModel classModel, EventAdapter.MyViewHolder holder) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EventInterested,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String code, message, id, TotalLike;

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            code = jsonObject.getString("code");
                            message = jsonObject.getString("message");

                            Log.d("resp", "onResponse: " + jsonObject + "===");
                            if (code.equals("200")) {

                                Log.d("resp_", "onResponse: " + jsonObject + "===" + code);
                                //  Toast.makeText(context, "Like Successfully!", Toast.LENGTH_SHORT).show();
                                holder.id_tx_inter.setBackgroundResource(R.color.bg_yellow);

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
                params.put("event_id", classModel.getId());
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

    private void DislikeMethod(String flag_like, FeedModel classModel, EventAdapter.MyViewHolder holder) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                                holder.id_tx_inter.setBackgroundResource(R.color.grey_20);

                                // Toast.makeText(context, "DisLike Successfully!", Toast.LENGTH_SHORT).show();

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
                params.put("event_id", classModel.getId());
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

}
