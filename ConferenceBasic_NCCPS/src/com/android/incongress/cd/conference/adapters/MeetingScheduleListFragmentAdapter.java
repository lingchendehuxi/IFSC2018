package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListDetailActionFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class MeetingScheduleListFragmentAdapter extends FragmentPagerAdapter {
    private int mPageSize;
    private List<String> mTitles;

    public MeetingScheduleListFragmentAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        this.mTitles = titles;
        this.mPageSize = titles.size();
    }

    @Override
    public Fragment getItem(int position) {
        return MeetingScheduleListDetailActionFragment.getInstance(mTitles.get(position));
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