package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.android.incongress.cd.conference.adapters.SegmentSearchAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.widget.MyViewPager;
import com.android.incongress.cd.conference.widget.SegmentedGroup;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by Jacky on 2016/3/7.
 * 日程检索+专家检索
 */
public class NewSearchScheduleActionFragment extends BaseFragment {
    private MyViewPager mViewPager;
    private SegmentSearchAdapter mAdapter;
    private SegmentedGroup mSgTab;
    private RadioButton mRbSchedule,mRbProfessor;
    private View mSearchView;

    private static final int TYPE_SCHEDULE = 1;
    private static final int TYPE_FACULTY = 2;

    /**
     * 默认情况下的搜索类型
     */
    private int mCurrentSearchType = TYPE_SCHEDULE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_search_fragment, null);
        mViewPager = (MyViewPager) view.findViewById(R.id.vp_search);
        mViewPager.setScrollble(true);
        mAdapter = new SegmentSearchAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mRbSchedule.setChecked(true);
                    mCurrentSearchType = TYPE_SCHEDULE;
                } else {
                    mRbProfessor.setChecked(true);
                    mCurrentSearchType = TYPE_FACULTY;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    public void setRightView(View view) {
        mSearchView = view;
        if(mSearchView != null) {
            mSearchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCurrentSearchType == TYPE_SCHEDULE) {
                        View searchView = CommonUtils.initView(getActivity(), R.layout.title_search);
                        SearchScheduleDetailActionFragment fragment = new SearchScheduleDetailActionFragment();
                        //fragment.setCenterVie(searchView);
                        //action(fragment, R.string.enter_blank,null,false,false,searchView, false);
                        action(fragment, R.string.search_result, false, false, false);
                    }else {
                        View searchView = CommonUtils.initView(getActivity(), R.layout.title_search);
                        SearchFacultyDetailFragment fragment = new SearchFacultyDetailFragment();
                        //fragment.setCenterVie(searchView);
                        action(fragment, R.string.search_result, false, false, false);
                        //action(fragment, R.string.enter_blank,null,false,false,searchView, false);
                    }
                }
            });
        }
    }

    public void setCenterView(View view) {
        mSgTab = (SegmentedGroup) view.findViewById(R.id.sg_tab);
        mRbSchedule = (RadioButton) view.findViewById(R.id.rb_schedule);
        mRbProfessor = (RadioButton) view.findViewById(R.id.rb_professor);

        mRbSchedule.setChecked(true);
        mSgTab.setVisibility(View.VISIBLE);

        mRbSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mViewPager.setCurrentItem(0);
                }
            }
        });

        mRbProfessor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mViewPager.setCurrentItem(1);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SEARCHSCHEDULE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SEARCHSCHEDULE);
    }
}
