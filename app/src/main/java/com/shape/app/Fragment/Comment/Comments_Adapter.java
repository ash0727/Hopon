package com.shape.app.Fragment.Comment;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.GENDER;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.USER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shape.app.Helper.Constant;
import com.shape.app.R;

import java.util.ArrayList;


public class Comments_Adapter extends RecyclerView.Adapter<Comments_Adapter.CustomViewHolder > {

    public Context context;
    private Comments_Adapter.OnItemClickListener listener;
    private ArrayList<Comment_Get_Set> dataList;
    SharedPreferences sharedPreferences;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Comment_Get_Set item, View view);
    }

    public Comments_Adapter(Context context, ArrayList<Comment_Get_Set> dataList, Comments_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }
    public Comments_Adapter(Context context, ArrayList<Comment_Get_Set> dataList) {
        this.context = context;
        this.dataList = dataList;

    }

    @Override
    public Comments_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Comments_Adapter.CustomViewHolder viewHolder = new Comments_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }


    @Override
    public void onBindViewHolder(final Comments_Adapter.CustomViewHolder holder, final int i) {
        final Comment_Get_Set item= dataList.get(i);


        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        holder.username.setText(item.first_name+" "+item.last_name);

        Log.d("CHECK_MAKE", "onBindViewHolder: "+item.gender);
        if (item.gender.equals("2")) {
            Glide.with(context).load(Constant.profile_pic + item.profile_pic).placeholder(R.drawable.ic_female).into(holder.user_pic);
        } else {
            Glide.with(context).load(Constant.profile_pic + item.profile_pic).placeholder(R.drawable.man).into(holder.user_pic);
        }

        holder.message.setText(item.comments);


        holder.bind(i,item,listener);

   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username,message;
        ImageView user_pic,imgdeletecoment;


        public CustomViewHolder(View view) {
            super(view);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            imgdeletecoment=view.findViewById(R.id.imgdeletecoment);
            message=view.findViewById(R.id.message);

        }

        public void bind(final int postion, final Comment_Get_Set item, final Comments_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });

            imgdeletecoment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    listener.onItemClick(postion,item,view);

                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    listener.onItemClick(postion,item,view);

                }
            });

            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    listener.onItemClick(postion,item,view);

                }
            });


        }


    }





}