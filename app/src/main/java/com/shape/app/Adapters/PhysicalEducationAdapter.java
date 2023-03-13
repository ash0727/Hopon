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
import com.shape.app.Models.PhyEduModel;
import com.shape.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

public class PhysicalEducationAdapter extends RecyclerView.Adapter<PhysicalEducationAdapter.MyViewHolder> {
    ArrayList<PhyEduModel> conVideoArrayList;
    Context context;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
    String new_date;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_lname;
    private PhysicalEducationAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, PhyEduModel item, View view);
    }


    public PhysicalEducationAdapter(ArrayList<PhyEduModel> conVideoArrayList, AppCompatActivity context) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PhysicalEducationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phy_edu, parent, false);
        return new PhysicalEducationAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhysicalEducationAdapter.MyViewHolder holder, final int position) {
        final PhyEduModel classModel = conVideoArrayList.get(position);


        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");


        holder.name.setText("Unit Plan "+(position + 1 ));
        holder.imdb_heart_rate_t.setText(classModel.grade_name + "\nGrade");


        String strpdf1 = classModel.unit_plans;
        String strpdf2 = classModel.report;

        if (!strpdf2.equals("")) {
            holder.pdf_report.setVisibility(View.VISIBLE);
        }
        holder.pdf_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDFDetails.class);
                intent.putExtra("url", classModel.report);
                intent.putExtra("from", "6");
                intent.putExtra("data", "3");
                context.startActivity(intent);

            }
        });
        if (!strpdf1.equals("")) {
            holder.pdf_1.setVisibility(View.VISIBLE);
        }
        holder.pdf_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PDFDetails.class);
                intent.putExtra("url", classModel.unit_plans);
                intent.putExtra("from", "6");
                intent.putExtra("data", "2");
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, grade, imdb_heart_rate_t, SPO2_t;
        ImageView pdf_report, pdf_1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            imdb_heart_rate_t = itemView.findViewById(R.id.grade);
            pdf_1 = itemView.findViewById(R.id.pdf_1);
            pdf_report = itemView.findViewById(R.id.pdf_report);


        }


    }


}