package com.android.incongress.cd.conference.fragments.me;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MessageListAdapter;
import com.android.incongress.cd.conference.adapters.MindInfoAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.widget.MyViewPager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.mtablayout.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class MindBookFragment extends BaseFragment {
    private int mCurrentPage = 0;
    private CommonTabLayout tab_layout;
    private MyViewPager mViewPager;
    private MindInfoAdapter adapter;
    private String[] titles ;
    private ArrayList<CustomTabEntity> mTabEntities2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        View view = inflater.inflate(R.layout.mind_book_fragment, null);
        titles = new String[]{getString(R.string.question_meeting), getString(R.string.bus),getString(R.string.schedule),getString(R.string.live)};
        //titles = new String[]{getString(R.string.schedule)};
        mViewPager = view.findViewById(R.id.viewpager);
        tab_layout = view.findViewById(R.id.tab_layout);
        initView(view);
        tab_layout.setIndicatorWidth(24);
        for(int i = 0;i<titles.length;i++){
            mTabEntities2.add(new TabEntity(titles[i], R.drawable.bottom_home, R.drawable.bottom_home));
        }
        adapter = new MindInfoAdapter(getChildFragmentManager(), titles, titles.length);
        mViewPager.setScrollble(true);
        mViewPager.setAdapter(adapter);

        //tab_layout.setViewPager(mViewPager);
        tab_layout.setTabData(mTabEntities2);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(mCurrentPage);
        return view;
    }
    private void initView(View view){
        LinearLayout title_back_panel = view.findViewById(R.id.title_back_panel);
        title_back_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).performBackClick();
            }
        });
        TextView tv_manager = view.findViewById(R.id.tv_manager);
        tv_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<MindBookFragment.ManagerNewLister> list = ((HomeActivity)getActivity()).getMyBookClick();
                for (int i = 0;i<list.size();i++){
                    list.get(i).managerClick();
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tab_layout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab_layout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
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
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
    public interface ManagerNewLister{
        void managerClick();
    }
}
