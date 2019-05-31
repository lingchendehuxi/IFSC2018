package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Map;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> mFragments;
    private float mBaseElevation;
    private List<Map> datasource = new ArrayList<>();
    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;
        datasource.addAll(ConferenceDbUtils.getAllMaps());
        for(int i = 0; i<datasource.size(); i++){
            Map bean = datasource.get(i);
            String mapName = "";
            if(bean.getMapRemark().contains("#@#")){
                if (AppApplication.systemLanguage == 1) {
                    mapName = bean.getMapRemark().split("#@#")[0];
                }else{
                    mapName = bean.getMapRemark().split("#@#")[1];
                }
            }else{
                mapName = bean.getMapRemark();
            }
            addCardFragment(CardFragment.getInstance(bean.getMapUrl(),mapName,i));
        }
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        mFragments.add(fragment);
    }

}
