package com.android.incongress.cd.conference.fragments.college;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.CollegeBookListItemFragmentAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeBookCoveryBean;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.widget.MyViewPager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/31.
 */
public class CollegeBookListDetailFragment extends BaseFragment {
    private MyViewPager mViewPager;
    private LinearLayout mTabLayout;
    private CollegeBookListItemFragmentAdapter mPageAdapter;
    private List<String> mSessionDaysList = new ArrayList<>();
    private List<String> mSessionDaysDetailList = new ArrayList<>();
    private static String BUNDLE_CACHE_DATA = "collegeCacheData";
    private ArrayList<CollegeBookCoveryBean> dataList;
    //缓存
    private ArrayList<TextView> listText;

    public static CollegeBookListDetailFragment getInstance(List<CollegeBookCoveryBean> dataList) {
        CollegeBookListDetailFragment fragment = new CollegeBookListDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_CACHE_DATA, (ArrayList<CollegeBookCoveryBean>)dataList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_college_list_detail, null, false);
        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.ll_table);
        dataList = (ArrayList<CollegeBookCoveryBean>) getArguments().getSerializable(BUNDLE_CACHE_DATA);
        getCollegeBookDayList();
        return view;
    }

    private void getSessionDays() {
        if (listText == null) {
            listText = new ArrayList<>();
        } else {
            listText.clear();
        }
        int h_size = DensityUtil.getScreenSize(getActivity())[0];
        for (int i = 0; i < mSessionDaysList.size(); i++) {
            final TextView textView = new TextView(getContext());
            FrameLayout.LayoutParams params;
            if (mSessionDaysList.size() >= 4) {
                params = new FrameLayout.LayoutParams(h_size / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                params = new FrameLayout.LayoutParams(h_size / mSessionDaysList.size(), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setText(mSessionDaysList.get(i));
            textView.setTextSize(14);
            textView.setTag(i);
            textView.setLayoutParams(params);
            listText.add(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < listText.size(); i++) {
                        listText.get(i).setTextColor(getResources().getColor(R.color.black_login_text));
                    }
                    listText.get((int) view.getTag()).setTextColor(getResources().getColor(R.color.theme_color));
                    mViewPager.setCurrentItem((int) view.getTag());
                }
            });
            mTabLayout.addView(textView);
        }
        if (listText.size() > 0) {
            listText.get(0).performClick();
        }
    }

    //获取当年可预约的课件信息
    //测试数据 290
    private void getCollegeBookDayList() {
        if (dataList!=null&&dataList.size() != 0) {
            mSessionDaysList.clear();
            mSessionDaysDetailList.clear();
            for (int i = 0; i < dataList.size(); i++) {
                if (AppApplication.systemLanguage == 1) {
                    mSessionDaysList.add(DateUtil.getFormatMonthAndDayChinese(dataList.get(i).getDate()));
                } else {
                    mSessionDaysList.add(DateUtil.getFormatMonthAndDayEnglish(dataList.get(i).getDate()));
                }
                mSessionDaysDetailList.add(dataList.get(i).getDate());

                if (mSessionDaysList.size() > 0) {
                    mPageAdapter = new CollegeBookListItemFragmentAdapter(getChildFragmentManager(), mSessionDaysDetailList, 100, dataList);
                    mViewPager.setScrollble(false);
                    mViewPager.setAdapter(mPageAdapter);
                    mViewPager.setCurrentItem(0);
                    mTabLayout.removeAllViews();
                    getSessionDays();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
        MobclickAgent.onPageStart(Constants.FRAGMENT_COLLEGETEXTLIST);
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().findViewById(R.id.title_back_panel).setVisibility(View.VISIBLE);
        MobclickAgent.onPageEnd(Constants.FRAGMENT_COLLEGETEXTLIST);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}