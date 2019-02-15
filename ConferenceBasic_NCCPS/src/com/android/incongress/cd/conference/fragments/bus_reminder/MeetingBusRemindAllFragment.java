package com.android.incongress.cd.conference.fragments.bus_reminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.BusInfo;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by qichen on 2017/5/20.
 */

public class MeetingBusRemindAllFragment extends BaseFragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BusRemindAdapter mAdapter;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.fragment_bus_reminder_all, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.setOffscreenPageLimit(2);
        initData();
        return view;
    }

    private void initData() {
        CHYHttpClientUsage.getInstanse().doGetBusInfo(Constants.getConId(),new JsonHttpResponseHandler(Constants.ENCODING_GBK){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Gson gson = new Gson();
                    BusInfo busInfo = gson.fromJson(response.toString(),BusInfo.class);

                    if(busInfo != null && busInfo.getState() == 1) {
                        String[] titles = new String[busInfo.getDateArray().size()];
                        //日程存在
                        for (int i = 0; i < busInfo.getDateArray().size(); i++) {
                            titles[i] = busInfo.getDateArray().get(i).getBusDate();
                        }
                        mAdapter = new BusRemindAdapter(getChildFragmentManager(), titles,busInfo.getDateArray().size());
                        mViewPager.setAdapter(mAdapter);
                        mTabLayout.setupWithViewPager(mViewPager);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
                        Date date = new Date(System.currentTimeMillis());
                        for (int i = 0;i<titles.length;i++){
                            if(simpleDateFormat.format(date).equals(titles[i])){
                                try {
                                    Field field = mViewPager.getClass().getField("mCurItem");
                                    field.setAccessible(true);
                                    field.setInt(mViewPager, i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
// 通过数据修改
                                mAdapter.notifyDataSetChanged();
// 切换到指定页面
                                mViewPager.setCurrentItem(i);
                            }
                        }
                    }else{
                        Toast.makeText(getActivity(),"暂无班车信息",Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private class BusRemindAdapter extends FragmentStatePagerAdapter {

        /** 标题 **/
        private CharSequence Titles[];
        /**  tab栏 **/
        private int NumbOfTabs;

        public BusRemindAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);
            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;
        }

        @Override
        public Fragment getItem(int position) {
            return MeetingBusRemindSingleFragment.getInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
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

    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
