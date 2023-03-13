package com.shape.app.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class Functions {
    public static void showAlertDialog(final Activity context, String title, String message, boolean isCancelable,
                                       String firstButtonText, DialogInterface.OnClickListener firstButtonListener
            , String secondButtonText, DialogInterface.OnClickListener secondButtonListener
            , String thirdButtonText, DialogInterface.OnClickListener thirdButtonListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title != null) {
            builder.setTitle(title);
        }

        if (message != null) {
            builder.setMessage(message);
        }

        builder.setCancelable(isCancelable);

        if (firstButtonText != null) {
            builder.setPositiveButton(firstButtonText, firstButtonListener);
        }

        if (secondButtonText != null) {
            builder.setNegativeButton(secondButtonText, secondButtonListener);
        }

        if (thirdButtonText != null) {
            builder.setNeutralButton(thirdButtonText, thirdButtonListener);
        }

        AlertDialog alert = builder.create();
        alert.show();

    }

    public static int getColor(Context context, int icons) {

        return context.getResources().getColor(icons);
    }

    public static String getStringFromTextview(TextView textView) {

        return textView.getText().toString().trim();
    }

    public static String getStringFromEdit(EditText editText) {

        return editText.getText().toString().trim();
    }

    public static void requestEditText(EditText editText) {
        editText.requestFocus();
    }

    public static final void focusOnView(ScrollView view, EditText editText) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.scrollTo(0, editText.getBottom());
            }
        });
    }

}
