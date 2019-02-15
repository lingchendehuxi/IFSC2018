package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.fragments.me.MindBookFragment;
import com.android.incongress.cd.conference.fragments.me.MyMessageListFragment;
import com.android.incongress.cd.conference.fragments.me.MyMindBookListFragment;
import com.android.incongress.cd.conference.fragments.my_schedule.MyScheduleDetailActionFragment;

/**
 * Created by Jacky on 2016/1/15.
 */
public class MessageListAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[];
    private int NumbOfTabs;

    public MessageListAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        return MyMessageListFragment.getInstance(Titles[position].toString());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }


    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
