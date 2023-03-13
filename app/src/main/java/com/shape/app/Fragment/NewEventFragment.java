package com.shape.app.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.shape.app.Fragment.FragmentEvent.AllEventsFragment;
import com.shape.app.Fragment.FragmentEvent.HoponFragment;
import com.shape.app.Fragment.FragmentEvent.InterestedFragment;
import com.shape.app.Fragment.FragmentEvent.SchoolFragment;
import com.shape.app.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.shape.app.Forms.Login.FNAME;
import static com.shape.app.Forms.Login.LNAME;
import static com.shape.app.Forms.Login.SHARED_PREFERENCES_NAME;
import static com.shape.app.Forms.Login.THEME_COLOR;
import static com.shape.app.Forms.Login.USER_ID;


public class NewEventFragment extends Fragment {
    public NewEventFragment() {
        // Required empty public constructor
    }

    private HoponFragment fragmentOne;
    private SchoolFragment fragmentTwo;
    private AllEventsFragment fragmentThree;
    private InterestedFragment fragmentFour;
    private TabLayout allTabs;
    SharedPreferences sharedPreferences;
    String str_userid, str_name, str_fname, str_theme, str_lname;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_event, container, false);
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
        fragmentOne = new HoponFragment();
        fragmentTwo = new SchoolFragment();
        fragmentThree = new AllEventsFragment();
        fragmentFour = new InterestedFragment();
        allTabs.addTab(allTabs.newTab().setText("All"), true);
        allTabs.addTab(allTabs.newTab().setText("Private Event"));
        allTabs.addTab(allTabs.newTab().setText("School"));
        allTabs.addTab(allTabs.newTab().setIcon(R.drawable.ic_baseline_check_circle_outline_24));
        // allTabs.addTab(allTabs.newTab().setIcon(R.drawable.tick_interested));
        Objects.requireNonNull(Objects.requireNonNull(allTabs.getTabAt(3)).getIcon()).setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

    }

    private void bindWidgetsWithAnEvent() {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
                //allTabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

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
                replaceFragment(fragmentThree);
                break;
            case 1:
                replaceFragment(fragmentOne);
                break;
            case 2:
                replaceFragment(fragmentTwo);
                break;
            case 3:
                replaceFragment(fragmentFour);
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