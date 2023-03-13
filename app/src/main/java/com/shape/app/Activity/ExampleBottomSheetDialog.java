package com.shape.app.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.shape.app.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_lyt,
                container, false);

        ImageView button1 = (ImageView) v.findViewById(R.id.btn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWhatsapp("Hey i am using hopon");
            }
        });

        ImageView deep = (ImageView) v.findViewById(R.id.btn2);
        deep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/426253597411506"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/")));
                }
            }
        });

        ImageView nn = (ImageView) v.findViewById(R.id.btn3);
        nn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageInstalled("com.instagram.android")) {
                    Intent shareIntent = new Intent("com.instagram.share.ADD_TO_FEED");
                    shareIntent.setType("video/*");
                    shareIntent.setPackage("com.instagram.android");

                    Activity activity = getActivity();
                    if (activity.getPackageManager().resolveActivity(shareIntent, 0) != null) {
                        activity.startActivityForResult(shareIntent, 0);
                    }
                }

            }
        });

        ImageView button2 = v.findViewById(R.id.btn4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "admin@email.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hopon");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        return v;
    }
    private void sendWhatsapp(String message){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }
    public boolean isPackageInstalled(String packagename) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Not installed", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

}