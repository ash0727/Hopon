package com.shape.app.Fragment.Share;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shape.app.Interface.Fragment_Callback;
import com.shape.app.Models.EventTabModel;
import com.shape.app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoAction_F extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String NORMAL_SHARE = "NORMAL_SHARE";
    public static final String WHATSAPP_SHARE = "WHATSAPP_SHARE";
    public static final String FACEBOOK_SHARE = "FACEBOOK_SHARE";
    //public static final String FACEBOOK_STORY_SHARE = "FACEBOOK_STORY_SHARE";
    //public static final String YOUTUBE_SHARE = "YOUTUBE_SHARE";
    public static final String INSTRAGRAM_SHARE = "INSTRAGRAM_SHARE";
   // public static final String INSTRAGRAM_STORY_SHARE = "INSTRAGRAM_STORY_SHARE";
    public static final String DELETE_VIDEO = "DELETE_VIDEO";

    View view;
    Context context;
    RecyclerView recyclerView;

    Fragment_Callback fragment_callback;

    String video_id, user_id;

    ProgressBar progressBar;
    EventTabModel item;

    public VideoAction_F() {
    }

    @SuppressLint("ValidFragment")
    public VideoAction_F(String id, Fragment_Callback fragment_callback) {
        video_id = id;
        this.fragment_callback = fragment_callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_action, container, false);
        context = getContext();

        Bundle bundle = getArguments();
        if (bundle != null) {
            item = (EventTabModel) bundle.getSerializable("data");
            video_id = bundle.getString("video_id");
            user_id = bundle.getString("user_id");
        }

        progressBar = view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.instagram).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.facebook).setOnClickListener(this);
        view.findViewById(R.id.whatsApp).setOnClickListener(this);
        view.findViewById(R.id.email).setOnClickListener(this);

        view.findViewById(R.id.cancel).setOnClickListener(this);

//

        return view;
    }


    private void callback(String action) {


            Bundle bundle = new Bundle();
            bundle.putString("action", action);
            dismiss();
            fragment_callback.Responce(bundle);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.email:
                callback(NORMAL_SHARE);
                break;

            case R.id.whatsApp:

                callback(WHATSAPP_SHARE);

                break;

            case R.id.instagram:

                callback(INSTRAGRAM_SHARE);

                break;

//            case R.id.instagram_story:
//
//                callback(INSTRAGRAM_STORY_SHARE);
//
//                break;

            case R.id.facebook:

                callback(FACEBOOK_SHARE);

                break;


        }
    }



}
