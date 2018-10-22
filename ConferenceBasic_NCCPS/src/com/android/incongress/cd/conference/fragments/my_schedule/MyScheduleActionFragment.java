package com.android.incongress.cd.conference.fragments.my_schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyScheduleAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.TimeUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 *
 * 我的日程
 */
public class MyScheduleActionFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MyScheduleAdapter mAdapter;

    private List<String> mSessionDaysList = new ArrayList<>();

    public static final String BROADCAST_EDIT = "edit";
    public static final String BROADCAST_OPEN_CLOSE ="mode";

    private boolean mIsEditMode = false;
    private int mCurrentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_schedule, null);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);

        List<Session> allSession = ConferenceDbUtils.getAllSession();
        for (int i = 0; i < allSession.size(); i++) {
            Session session = allSession.get(i);
            if(mSessionDaysList.size()>0) {
                if(!(mSessionDaysList.get(mSessionDaysList.size()-1).equals(session.getSessionDay()))) {
                    mSessionDaysList.add(session.getSessionDay());
                }
            }else {
                mSessionDaysList.add(session.getSessionDay());
            }
        }

        String[] titles = new String[mSessionDaysList.size()];
        for(int i=0; i<mSessionDaysList.size(); i++) {
            titles[i] = mSessionDaysList.get(i);
            if(TimeUtils.getCurrentTimeMD().equals(mSessionDaysList.get(i))){
                mCurrentPage = i;
            }
        }

        mAdapter = new MyScheduleAdapter(getFragmentManager(), titles, titles.length);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mCurrentPage);

        return view;
    }

    public void setRightView(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsEditMode) {
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_EDIT);
                    intent.putExtra(BROADCAST_OPEN_CLOSE, true);
                    getActivity().sendBroadcast(intent);
                    ((TextView)view).setText(R.string.mymeeting_finish);
                    mIsEditMode = true;
                }else {
                    Intent intent = new Intent();
                    intent.setAction(BROADCAST_EDIT);
                    intent.putExtra(BROADCAST_OPEN_CLOSE, false);
                    getActivity().sendBroadcast(intent);
                    ((TextView)view).setText(R.string.mymeeting_manage);
                    mIsEditMode = false;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MYSCHEDULE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MYSCHEDULE);
    }
}
