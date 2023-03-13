package com.shape.app.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shape.app.Activity.PerformanceCriteria.HealthActivity;
import com.shape.app.Models.BMI_Model;
import com.shape.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

public class BMI_Adapter extends RecyclerView.Adapter<BMI_Adapter.MyViewHolder> {
    ArrayList<BMI_Model> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private BMI_Adapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, BMI_Model item, View view);
    }


    public BMI_Adapter(ArrayList<BMI_Model> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BMI_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bmi, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BMI_Adapter.MyViewHolder holder, final int position) {
        final BMI_Model classModel = conVideoArrayList.get(position);


        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");



        if (classModel.report_date.equals(null))
        {
            holder.age.setText("-");
        }
        else
            {
            Date date = null;
            try {
                date = fmt.parse(classModel.report_date);
                holder.age.setText(fmtOut.format(date));
                new_date = fmtOut.format(date);
                Log.d("tag_adapter_date", "onBindViewHolder: " + new_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.age.setText(new_date);
        }
        holder.btn_name.setText(new_date);
        holder.height.setText(classModel.height);
        holder.weight.setText(classModel.weight);
        holder.result.setText(classModel.result);
        holder.normal_range.setText(classModel.normal_range);
        Log.d("tag_app", "onBindViewHolder: " + classModel.report_date);

        //Glide.with(context).load(Constant.Appointment_img + classModel.getImage()).placeholder(R.drawable.img_no).into(holder.image);
        holder.btn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HealthActivity) context).onClickCalled(classModel, position,"1");
            }
        });

    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView btn_name, grade_1, grade_2, grade_3, age, name, height, weight, code_st, result, normal_range;
        ImageView share, image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            height = itemView.findViewById(R.id.height);
            weight = itemView.findViewById(R.id.weight);
            result = itemView.findViewById(R.id.bmi_result);
            normal_range = itemView.findViewById(R.id.normal_range);
            age = itemView.findViewById(R.id.age);
            btn_name = itemView.findViewById(R.id.btn_name);


        }


    }


}