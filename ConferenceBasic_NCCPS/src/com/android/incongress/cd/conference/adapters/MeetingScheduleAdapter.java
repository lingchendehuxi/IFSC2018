package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Edwin on 15/02/2015.
 * 会议日程中ViewPager的采集器
 */
public class MeetingScheduleAdapter extends FragmentStatePagerAdapter {

    /** 标题 **/
    private CharSequence Titles[];
    //所有session的list集合
    private List<Fragment> fragmentList;

    public MeetingScheduleAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }



    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
