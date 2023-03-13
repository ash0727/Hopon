package com.shape.app.Adapters;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shape.app.Activity.PDFDetails;
import com.shape.app.Models.HeartRateModel;
import com.shape.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

import android.content.Intent;

import java.text.ParseException;
import java.util.Date;

public class HeartRateAdapter extends RecyclerView.Adapter<HeartRateAdapter.MyViewHolder> {
    ArrayList<HeartRateModel> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private HeartRateAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, HeartRateModel item, View view);
    }


    public HeartRateAdapter(ArrayList<HeartRateModel> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HeartRateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_heart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HeartRateAdapter.MyViewHolder holder, final int position) {
        final HeartRateModel classModel = conVideoArrayList.get(position);


        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        Date date = null;
        try {
            date = fmt.parse(classModel.added_date);
            holder.name.setText(fmtOut.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.max_heart_rate_t.setText(classModel.maximum_heart_rate + " Bpm");
        holder.rest_heart_rate_t.setText(classModel.resting_heart_rate + " Bpm");
        holder.imdb_heart_rate_t.setText(classModel.immediate_heart_rate + " Bpm");
        holder.SPO2_t.setText(classModel.SPO2 + "%");

        //Glide.with(context).load(Constant.Appointment_img + classModel.getImage()).placeholder(R.drawable.img_no).into(holder.image);

        String strpdf1 = classModel.report;

        if (!strpdf1.equals("")) {
            holder.pdf_1.setVisibility(View.VISIBLE);
        }
        holder.pdf_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDFDetails.class);
                intent.putExtra("url", classModel.report);
                intent.putExtra("from", "1");
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView max_heart_rate_t, rest_heart_rate_t, imdb_heart_rate_t, SPO2_t,name;
        ImageView share, pdf_1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            max_heart_rate_t = itemView.findViewById(R.id.max_heart_rate_t);
            rest_heart_rate_t = itemView.findViewById(R.id.rest_heart_rate_t);
            imdb_heart_rate_t = itemView.findViewById(R.id.imdb_heart_rate_t);
            SPO2_t = itemView.findViewById(R.id.SPO2_t);
            pdf_1 = itemView.findViewById(R.id.pdf_1);
            name = itemView.findViewById(R.id.name);


        }


    }


}