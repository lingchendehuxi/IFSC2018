package com.android.incongress.cd.conference.fragments.meeting_guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Map;
import com.android.incongress.cd.conference.widget.ScrollControlViewpager;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingGuideRoomMapViewPage extends BaseFragment {
    private ViewPagerAdapter ViewPagerAdapter;
    private List<Map> datasource = new ArrayList<>();
    private List<MeetingGuideRoomMapFragment> mFragments;
    private List<String> listString;
    private int currentPosition;
    @BindView(R.id.vp_pager)
    ScrollControlViewpager vp_pager;
    @BindView(R.id.title_back_panel)
    LinearLayout title_back_panel;
    @BindView(R.id.title_text)
    TextView title_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_guide_viewpage, null);
        ButterKnife.bind(this,view);
        currentPosition = getArguments().getInt("currentPosition");
        listString = new ArrayList<>();
        mFragments = new ArrayList<>();
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
            listString.add(mapName);
            MeetingGuideRoomMapFragment fragment = new MeetingGuideRoomMapFragment();
            fragment.setFilePath(AppApplication.instance().getSDPath() + Constants.FILESDIR+bean.getMapUrl());
            mFragments.add(fragment);
        }
        ViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragments);
        vp_pager.setAdapter(ViewPagerAdapter);
        vp_pager.setOffscreenPageLimit(1);
        vp_pager.setCurrentItem(currentPosition);
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                title_text.setText(listString.get(position));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }
    @OnClick(R.id.title_back_panel)
    void onBackClick(){
        performback();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGMAPINFO);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGMAPINFO);
    }
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<MeetingGuideRoomMapFragment> mFragments;
        public ViewPagerAdapter(FragmentManager fm, List<MeetingGuideRoomMapFragment> fragments){
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}
