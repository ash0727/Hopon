package com.shape.app.Fragment.Comment;

import androidx.fragment.app.Fragment;

import com.shape.app.Interface.OnBackPressListener;

public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImplimentation(this).onBackPressed();
    }
}