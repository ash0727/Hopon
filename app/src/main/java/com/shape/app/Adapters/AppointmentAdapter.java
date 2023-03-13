package com.shape.app.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.shape.app.Activity.AppointmentDetails;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.AppointmentModel;
import com.shape.app.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {
        ArrayList<AppointmentModel> conVideoArrayList;
        Context context;
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd-MMM-yyyy");
        String new_date;
        SharedPreferences sharedPreferences;
        String str_userid, str_name, str_fname, str_lname;
        private AppointmentAdapter.OnItemClickListener listener;

        public interface OnItemClickListener {
            void onItemClick(int positon, AppointmentModel item, View view);
        }


        public AppointmentAdapter(ArrayList<AppointmentModel> conVideoArrayList, AppCompatActivity context) {
            this.conVideoArrayList = conVideoArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AppointmentAdapter.MyViewHolder holder, final int position) {
            final AppointmentModel classModel = conVideoArrayList.get(position);


            sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            str_userid = sharedPreferences.getString(USER_ID, "");

            holder.name.setText(classModel.getName());
            Log.d("tag_app", "onBindViewHolder: " + classModel.getName());

            Glide.with(context).load(Constant.Appointment_img + classModel.getImage()).placeholder(R.drawable.img_no).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, AppointmentDetails.class);
                    intent.putExtra("name", conVideoArrayList.get(position).getName());
                    intent.putExtra("id", conVideoArrayList.get(position).getId());
                    intent.putExtra("image", Constant.Appointment_img + classModel.getImage());
                    context.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return conVideoArrayList.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, description, date, location, id_tx_inter;
            ImageView share, image;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.name);
                image = itemView.findViewById(R.id.image);


            }


        }


    }
