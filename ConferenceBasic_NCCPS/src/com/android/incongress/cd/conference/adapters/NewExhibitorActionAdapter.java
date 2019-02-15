package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.incongress.cd.conference.beans.ExhibitorTitleBean;
import com.android.incongress.cd.conference.fragments.exhibitor.NewExhibitorListFragment;
import com.android.incongress.cd.conference.fragments.me.MyMindBookListFragment;
import com.android.incongress.cd.conference.model.Exhibitor;

import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 */
public class NewExhibitorActionAdapter extends FragmentStatePagerAdapter {
    private List<ExhibitorTitleBean.MenuBean> list;
    private int NumbOfTabs;

    public NewExhibitorActionAdapter(FragmentManager fm, List<ExhibitorTitleBean.MenuBean> list, int mNumbOfTabsumb) {
        super(fm);
        this.list = list;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        return NewExhibitorListFragment.getInstance(list.get(position).getMenuName(),list.get(position).getIndex());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getMenuName();
    }


    @Override
    public int getCount() {
        return NumbOfTabs;
    }

}
