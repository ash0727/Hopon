package com.shape.app.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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
import com.shape.app.Activity.VideoDetails;
import com.shape.app.Helper.Constant;
import com.shape.app.Models.VideoMOdel;
import com.shape.app.R;

import java.util.ArrayList;

public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewAdapter.MyViewHolder> {
    ArrayList<VideoMOdel> conVideoArrayList;
    Context context;

    String ImageFull_Url, str_name, str_fname, data;
    private VideoViewAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int positon, VideoMOdel item, View view);
    }


    public VideoViewAdapter(ArrayList<VideoMOdel> conVideoArrayList, AppCompatActivity context, String data) {
        this.conVideoArrayList = conVideoArrayList;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public VideoViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image, parent, false);
        return new VideoViewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewAdapter.MyViewHolder holder, final int position) {
        final VideoMOdel VideoMOdel = conVideoArrayList.get(position);
        Log.d("VideoViewAdapter:=", "onBindViewHolder: " + VideoMOdel.name);
        Log.d("VideoViewAdapter:=", "onBindViewHolder: " + data);

        if (data.equals("PrimaryFitnessComponents")) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoDetails.class);
                    intent.putExtra("url", VideoMOdel.name);
                    intent.putExtra("from", "6");
                    context.startActivity(intent);

                }
            });

            Glide.with(context).load(Constant.VIDEO_PrimaryFitness_video + VideoMOdel.name).placeholder(R.drawable.img_no).into(holder.imageView);
        } else if (data.equals("SecondaryFitnessComponents")) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoDetails.class);
                    intent.putExtra("url", VideoMOdel.name);
                    intent.putExtra("from", "7");
                    context.startActivity(intent);

                }
            });

            Glide.with(context).load(Constant.SecondaryFitness_vid + VideoMOdel.name).placeholder(R.drawable.img_no).into(holder.imageView);

        }

        holder.img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.equals("SecondaryFitnessComponents")){
                    ImageFull_Url= Constant.SecondaryFitness_vid + VideoMOdel.name;
                }else{
                    ImageFull_Url= Constant.VIDEO_PrimaryFitness_video + VideoMOdel.name;
                }
                Log.d("Data_from_url", "onClick: "+ImageFull_Url);
                Uri uri = Uri.parse(ImageFull_Url);
                String url = ImageFull_Url;
                String filename;
                if (url.contains("?")) {
                    filename = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
                } else {
                    filename = url.substring(url.lastIndexOf("/") + 1, url.length());
                }
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);

// set title and description
                request.setTitle("Data Download");
                request.setDescription("Android Data download using DownloadManager.");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(filename);
//set the local destination for download file to a path within the application's external files directory
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                request.setMimeType("*/*");
                downloadManager.enqueue(request);
            }
        });
        //Glide.with(context).load(Constant.Appointment_img + classModel.getImage()).placeholder(R.drawable.img_no).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return conVideoArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView remark, textView, level, event_name, age, name, sports, achievement, result, normal_range;
        ImageView img_download, imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            img_download = itemView.findViewById(R.id.img_download);


        }


    }


}