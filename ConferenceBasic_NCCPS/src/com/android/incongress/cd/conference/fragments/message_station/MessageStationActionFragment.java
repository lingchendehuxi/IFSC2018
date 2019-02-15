package com.android.incongress.cd.conference.fragments.message_station;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.adapters.MessageListAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jacky on 2016/1/29.
 */
public class MessageStationActionFragment extends BaseFragment {

    private int mCurrentPage = 0;
    private SlidingTabLayout tab_layout;
    private ViewPager mViewPager;
    private MessageListAdapter adapter;
    private String[] titles;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        View view = inflater.inflate(R.layout.fragment_message_station, null, false);
        titles = new String[]{getString(R.string.system_notify), getString(R.string.person_notify)};
        mViewPager = view.findViewById(R.id.viewpager);
        tab_layout = view.findViewById(R.id.tab_layout);
        tab_layout.setIndicatorWidth(50);
        adapter = new MessageListAdapter(getFragmentManager(), titles, titles.length);
        mViewPager.setAdapter(adapter);
        tab_layout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(mCurrentPage);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_MESSAGESTATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MESSAGESTATION);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

}
