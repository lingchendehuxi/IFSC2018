package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleDetailActionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 * 会议日程中ViewPager的采集器
 */
public class MeetingScheduleAdapter extends FragmentStatePagerAdapter {

    /** 标题 **/
    private CharSequence Titles[];
    /**  tab栏 **/
    private int NumbOfTabs;
    //所有session的list集合
    private List<String> mSessionDayList;

    public MeetingScheduleAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, List<String> sessionDayList) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mSessionDayList = sessionDayList;
    }

    @Override
    public Fragment getItem(int position) {
        return MeetingScheduleDetailActionFragment.getSingleScheduleFragmetn(Titles[position].toString(), (ArrayList<String>) mSessionDayList);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence month = Titles[position].subSequence(5,7);
        return Integer.valueOf(month.toString())+"月\n"+Titles[position].subSequence(8,10);
    }


    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
    }
}
