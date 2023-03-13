package com.shape.app.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shape.app.Activity.ImageListActivity;
import com.shape.app.Activity.PDFDetails;
import com.shape.app.Models.AchievementModel;
import com.shape.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.Date;


public class Achievement_Adapter extends RecyclerView.Adapter<Achievement_Adapter.MyViewHolder> {
    ArrayList<AchievementModel> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private Achievement_Adapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, AchievementModel item, View view);
    }


    public Achievement_Adapter(ArrayList<AchievementModel> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Achievement_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Achievement_Adapter.MyViewHolder holder, final int position) {
        final AchievementModel achievementModel = conVideoArrayList.get(position);

        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        holder.remark.setText(achievementModel.remark);
        holder.event_name.setText(achievementModel.event_name);
        holder.event_date.setText("" + achievementModel.event_date);
        holder.sports.setText(achievementModel.sports);
        holder.level.setText(achievementModel.level);
        holder.achievement.setText(achievementModel.achievement);
        holder.location.setText(achievementModel.location);
        holder.count.setText("" + (position + 1));
        Date date = null;
        try {
            date = fmt.parse(achievementModel.event_date);
            holder.event_date.setText("Date\n" + fmtOut.format(date));
            Log.d("tag_adapter_date", "onBindViewHolder: " + new_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strpdf = achievementModel.certificate;
        if (!strpdf.equals("")) {
            holder.certificate.setVisibility(View.VISIBLE);
        }
        ArrayList<String> achievementModel_vilistdata = new ArrayList<String>();

        JSONArray ache_array = achievementModel.images;
        for (int i = 0; i < ache_array.length(); i++) {
            try {
                achievementModel_vilistdata.add(ache_array.getString(i));
                Log.d("ach_vilistdata", "Responce: " + ache_array.getString(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("achievemenl_vilistdata", "Responce: " + achievementModel_vilistdata);

        holder.image_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ImageListActivity.class);
                intent.putExtra("from", "1");//
                intent.putExtra("data", "Achievement");//
                intent.putStringArrayListExtra("array", achievementModel_vilistdata);//
                context.startActivity(intent);
            }
        });
        holder.certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDFDetails.class);
                intent.putExtra("url", achievementModel.certificate);
                intent.putExtra("from", "11");
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
        TextView remark, event_date, level, event_name, location, count, sports, achievement, result, normal_range;
        RelativeLayout certificate, image_ac;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image_ac = itemView.findViewById(R.id.image_ac);
            event_date = itemView.findViewById(R.id.event_date);
            remark = itemView.findViewById(R.id.remark);
            event_name = itemView.findViewById(R.id.event_name);
            sports = itemView.findViewById(R.id.sports);
            level = itemView.findViewById(R.id.level);
            achievement = itemView.findViewById(R.id.achievement);
            certificate = itemView.findViewById(R.id.certificate);
            count = itemView.findViewById(R.id.count);
            location = itemView.findViewById(R.id.location);

        }


    }

    void Showdialog(String behaviour_observation) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.demo_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Certificate / Images");
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView txt = (TextView) promptsView
                .findViewById(R.id.textView1);


        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("Certificate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alertDialogBuilder.setCancelable(false).setPositiveButton("Images", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

}