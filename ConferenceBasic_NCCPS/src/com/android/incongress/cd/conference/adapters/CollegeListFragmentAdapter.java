package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.fragments.college.CollegeListDetailFragment;
import com.android.incongress.cd.conference.fragments.college.CollegeVideoListFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class CollegeListFragmentAdapter extends FragmentStatePagerAdapter {
    private int mPageSize;
    private List<String> mTitles;
    private List<String> mSessionIDsList;

    public CollegeListFragmentAdapter(FragmentManager fm, List<String> titles, List<String> mSessionIDsList) {
        super(fm);
        this.mTitles = titles;
        this.mPageSize = titles.size();
        this.mSessionIDsList = mSessionIDsList;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0 && mSessionIDsList.get(0).contains("-")) {
            return CollegeVideoListFragment.getInstance(mSessionIDsList.get(position), "");
        } else {
            return CollegeListDetailFragment.getInstance(mTitles.get(position), mSessionIDsList.get(position), position);
        }
    }

    @Override
    public int getCount() {
        return mPageSize;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

}