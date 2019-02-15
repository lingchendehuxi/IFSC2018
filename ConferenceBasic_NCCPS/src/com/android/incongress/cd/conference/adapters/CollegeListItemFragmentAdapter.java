package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.fragments.college.CollegeListDetailActionFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListDetailActionFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class CollegeListItemFragmentAdapter extends FragmentPagerAdapter {
    private int mPageSize;
    private List<String> mTitles;
    private int mDataType;
    private String stringModelId;

    public CollegeListItemFragmentAdapter(FragmentManager fm, List<String> titles,int mDataType,String stringModelId) {
        super(fm);
        this.mTitles = titles;
        this.mPageSize = titles.size();
        this.mDataType = mDataType;
        this.stringModelId = stringModelId;
    }

    @Override
    public Fragment getItem(int position) {
        return CollegeListDetailActionFragment.getInstance(mTitles.get(position),mDataType,stringModelId,String.valueOf(position));
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