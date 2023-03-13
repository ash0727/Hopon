package com.shape.app.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.shape.app.Fragment.FragmentEvent.HoponFragment;
import com.shape.app.Fragment.FragmentEvent.SchoolFragment;
import com.shape.app.R;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {

    public static Fragment newInstance() {
        EventFragment fragment = new EventFragment();
        return fragment;

    }

    LinearLayout ll_card;


    private ViewPager view_pager;
    private TabLayout tab_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);
        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        EventFragment.SectionsPagerAdapter adapter = new EventFragment.SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(HoponFragment.newInstance(), "Hopon");
        adapter.addFragment(SchoolFragment.newInstance(), "School");
        viewPager.setAdapter(adapter);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}