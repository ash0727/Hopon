package com.shape.app.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shape.app.Activity.ImageListActivity;
import com.shape.app.Activity.NotificationDetails;
import com.shape.app.Activity.PDFDetails;
import com.shape.app.Models.Notification_Model;
import com.shape.app.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.MyViewHolder> {
    ArrayList<Notification_Model> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private Achievement_Adapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, Notification_Model item, View view);
    }


    public Notification_Adapter(ArrayList<Notification_Model> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Notification_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        return new Notification_Adapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Notification_Adapter.MyViewHolder holder, final int position) {
        final Notification_Model achievementModel = conVideoArrayList.get(position);

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        holder.title.setText(achievementModel.msg);
        String s="<body style=\"margin: 0; padding: 0\">\n <head><meta name='viewport' content='target-densityDpi=device-dpi'/></head>";
        holder.title_de.setText(achievementModel.detail);
        Date date = null;
        try {
            date = fmt.parse(achievementModel.added_date);
            holder.date_txt.setText(fmtOut.format(date));
            new_date = fmtOut.format(date);
            Log.d("tag_adapter_date", "onBindViewHolder: " + new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotificationDetails.class);
                intent.putExtra("msg", achievementModel.msg);
                intent.putExtra("details", achievementModel.detail);
                intent.putExtra("date", new_date);
                context.startActivity(intent);

            }
        });
        //Glide.with(context).load(Constant.Appointment_img + classModel.getImage()).placeholder(R.drawable.img_no).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date_txt, title;
        TextView title_de;
        RelativeLayout certificate, image_ac;
        CardView ll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date_txt = itemView.findViewById(R.id.date_txt);
            title_de = itemView.findViewById(R.id.title_de);
            title = itemView.findViewById(R.id.title);
            ll = itemView.findViewById(R.id.ll);


        }


    }


}