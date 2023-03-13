package com.shape.app.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.shape.app.Models.CBC_Model;
import com.shape.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;


public class CBCAdapter extends RecyclerView.Adapter<CBCAdapter.MyViewHolder> {
    ArrayList<CBC_Model> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private CBCAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, CBC_Model item, View view);
    }


    public CBCAdapter(ArrayList<CBC_Model> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CBCAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cbc, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CBCAdapter.MyViewHolder holder, final int position) {
        final CBC_Model cbc_model = conVideoArrayList.get(position);


        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        Date date = null;
        try {
            date = fmt.parse(cbc_model.added_date);
            holder.name.setText(fmtOut.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strpdf = cbc_model.report;
        if (!strpdf.equals("")) {
            holder.pdf_1.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDFDetails.class);
                intent.putExtra("url", cbc_model.report);
                intent.putExtra("from", "4");
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
        TextView name, height, weight, code_st, result, normal_range;
        ImageView share, pdf_1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            pdf_1 = itemView.findViewById(R.id.pdf_1);


        }


    }


}