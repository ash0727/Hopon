package com.shape.app.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * @author cubycode
 * @since 31/07/2018
 * All rights reserved
 */
public class UIUtils {

    public static Context getAppContext() {
        return Configs.getInstance().getApplicationContext();
    }

    public static int getDimension(@DimenRes int resId) {
        return (int) getAppContext().getResources().getDimension(resId);
    }

    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getAppContext(), resId);
    }

    public static String getString(@StringRes int resId) {
        return getAppContext().getString(resId);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            return;
        }
        hideKeyboard(currentFocus);
    }
}
