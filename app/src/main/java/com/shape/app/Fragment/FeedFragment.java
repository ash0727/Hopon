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
import com.shape.app.Fragment.FragmentFeed.EventFragment_tab;
import com.shape.app.Fragment.FragmentFeed.KnowledgeHub_tab;
import com.shape.app.Fragment.FragmentFeed.Testimonial_tab;
import com.shape.app.R;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {
    public FeedFragment() {
        // Required empty public constructor
    }

    LinearLayout ll_card;


    private ViewPager view_pager;
    private TabLayout tab_layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        view_pager = (ViewPager) view.findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(EventFragment_tab.newInstance(), "Events");
        adapter.addFragment(KnowledgeHub_tab.newInstance(), "Knowledge Hub");
       // adapter.addFragment(Testimonial_tab.newInstance(), "Testimonial");
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