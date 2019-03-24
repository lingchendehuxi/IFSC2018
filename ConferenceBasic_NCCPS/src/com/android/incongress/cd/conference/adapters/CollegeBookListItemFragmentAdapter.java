package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.beans.CollegeBookCoveryBean;
import com.android.incongress.cd.conference.fragments.college.CollegeBookListDetailActionFragment;
import com.android.incongress.cd.conference.fragments.college.CollegeListDetailActionFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class CollegeBookListItemFragmentAdapter extends FragmentPagerAdapter {
    private int mPageSize;
    private List<String> mTitles;
    private int mDataType;
    private List<CollegeBookCoveryBean> collegeList;

    public CollegeBookListItemFragmentAdapter(FragmentManager fm, List<String> titles, int mDataType, List<CollegeBookCoveryBean> collegeList) {
        super(fm);
        this.mTitles = titles;
        this.mPageSize = titles.size();
        this.mDataType = mDataType;
        this.collegeList = collegeList;
    }

    @Override
    public Fragment getItem(int position) {
        return CollegeBookListDetailActionFragment.getInstance(collegeList.get(position));
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