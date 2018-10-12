package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MeetingScheduleListFragmentAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jacky on 2016/1/31.
 */
public class MeetingScheduleListActionFragment extends BaseFragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private MeetingScheduleListFragmentAdapter mPageAdapter;
    private List<String> mSessionDaysList = new ArrayList<>();
    private TextView mTvTips;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_schedule_list, null, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

        /*mTvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f,0f);
                alphaAnimation.setDuration(300);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mTvTips.setVisibility(View.GONE);
                        AppApplication.setSPBooleanValue(Constants.LOOK_SCHEDULE_TIPS, true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mTvTips.startAnimation(alphaAnimation);
            }
        });

        if(AppApplication.getSPBooleanValue(Constants.LOOK_SCHEDULE_TIPS)) {
            mTvTips.setVisibility(View.GONE);
        }*/
        getSessionDays();

        mPageAdapter = new MeetingScheduleListFragmentAdapter(getChildFragmentManager(), mSessionDaysList);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setupWithViewPager(mViewPager);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if(mSessionDaysList.contains(simpleDateFormat.format(date))){
            for (int i = 0;i<mSessionDaysList.size();i++){
                if(simpleDateFormat.format(date).equals(mSessionDaysList.get(i))){
                    try {
                        Field field = mViewPager.getClass().getField("mCurItem");
                        field.setAccessible(true);
                        field.setInt(mViewPager, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
// 通过数据修改
                    mPageAdapter.notifyDataSetChanged();
// 切换到指定页面
                    mViewPager.setCurrentItem(i);
                }
            }
        }
        return view;
    }

    private void getSessionDays() {
        mSessionDaysList.clear();
        List<Session> allSession = ConferenceDbUtils.getAllSession();
        for (int i = 0; i < allSession.size(); i++) {
            Session session = allSession.get(i);
            if (mSessionDaysList.size() > 0) {
                if (!(mSessionDaysList.get(mSessionDaysList.size() - 1).equals(session.getSessionDay()))) {
                    mSessionDaysList.add(session.getSessionDay());
                }
            } else {
                mSessionDaysList.add(session.getSessionDay());
            }
        }
//        if(mSessionDaysList.size()==0){
//            mTvTips.setVisibility(View.VISIBLE);
//        }else {
//            mTvTips.setVisibility(View.GONE);
//        }
    }

    public void setRightListener(View view) {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(getActivity()!= null) {
//                    ((HomeActivity)getActivity()).performBackClick();
//                }
//            }
//        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView view = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
                if (AppApplication.systemLanguage == 1) {
                    view.setImageResource(R.drawable.schedule_switch_cn);
                } else {
                    view.setImageResource(R.drawable.schedule_switch);
                }
                MeetingScheduleViewPageFragment scheduleFragment = new MeetingScheduleViewPageFragment();
                scheduleFragment.setRightListener(view);
                getActivity().startActivity(new Intent(getActivity(),MeetingScheduleDetailActivity.class));
                //action(scheduleFragment, R.string.home_schedule, view, false, false, false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
        MobclickAgent.onPageStart(Constants.FRAGMENT_MEETINGSCHEDULELIST);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MEETINGSCHEDULELIST);
    }
}
