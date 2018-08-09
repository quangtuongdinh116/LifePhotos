package com.forabetterlife.dtq.myunsplash.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forabetterlife.dtq.myunsplash.R;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {

    ViewPager mViewPager;
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) root.findViewById(R.id.tabs);

        PagerAdapter mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        mPagerAdapter.addFragment(new AboutAppFragment(), getString(R.string.life_photo_tab_lable));
        mPagerAdapter.addFragment(new AuthorFragment(), getString(R.string.author_tab_lable));

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getActivity().setTitle(getString(R.string.about_title));

        return root;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return fragmentTitleList.get(position);
        }
    }

}
