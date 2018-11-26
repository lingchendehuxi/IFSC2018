package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.incongress.cd.conference.fragments.search_schedule.SegmentScheduleActionFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerSearchFragment;

/**
 * Created by Jacky on 2016/3/8.
 */
public class SegmentSearchAdapter extends FragmentStatePagerAdapter {
    private static final int mMaxCount = 2;

    public SegmentSearchAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new SegmentScheduleActionFragment();
        }else if(position == 1) {
            return SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_HOME);
        }

        return null;
    }

    @Override
    public int getCount() {
        return mMaxCount;
    }
}
