package com.shape.app.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.shape.app.Fragment.FragmentFeed.AllFeedFragment;
import com.shape.app.Fragment.FragmentFeed.EventFragment_tab;
import com.shape.app.Fragment.FragmentFeed.KnowledgeHub_tab;
import com.shape.app.Fragment.FragmentFeed.Testimonial_tab;
import com.shape.app.R;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;


public class TestFragment_Tab extends Fragment {
    public TestFragment_Tab() {
        // Required empty public constructor
    }

    private EventFragment_tab fragmentOne;
    private KnowledgeHub_tab fragmentTwo;
    private Testimonial_tab fragmentThree;
    private AllFeedFragment fragmentFour;
    private TabLayout allTabs;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_theme, str_lname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_tab, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_theme = sharedPreferences.getString(THEME_COLOR, "");

        allTabs = (TabLayout) view.findViewById(R.id.tabs);
        bindWidgetsWithAnEvent();
        setupTabLayout();


        try {
            if (!str_theme.equals("")) {
                allTabs.setBackgroundColor(Color.parseColor(str_theme));
            }
        } catch (Exception e) {
            Log.d("tag_color:-", "onCreateView: " + e.toString());
        }

        return view;
    }


    private void setupTabLayout() {
        fragmentOne = new EventFragment_tab();
        fragmentTwo = new KnowledgeHub_tab();
       // fragmentThree = new Testimonial_tab();
        fragmentFour = new AllFeedFragment();
        allTabs.addTab(allTabs.newTab().setText("All"), true);
        allTabs.addTab(allTabs.newTab().setText("Event"));
        allTabs.addTab(allTabs.newTab().setText("Knowledge Hub"));
       // allTabs.addTab(allTabs.newTab().setText("Testimonial"));
    }

    private void bindWidgetsWithAnEvent() {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(fragmentFour);

                break;
            case 1:
                replaceFragment(fragmentOne);

                break;
            case 2:
                replaceFragment(fragmentTwo);

                break;
            case 3:
                replaceFragment(fragmentThree);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
