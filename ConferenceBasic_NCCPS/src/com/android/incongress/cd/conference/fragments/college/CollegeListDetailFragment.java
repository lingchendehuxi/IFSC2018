package com.android.incongress.cd.conference.fragments.college;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.CollegeListItemFragmentAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeListDay;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.TimeUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.MyViewPager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/31.
 */
public class CollegeListDetailFragment extends BaseFragment {
    private MyViewPager mViewPager;
    private LinearLayout mTabLayout;
    private CollegeListItemFragmentAdapter mPageAdapter;
    private List<String> mSessionDaysList = new ArrayList<>();
    private List<String> mSessionDaysDetailList = new ArrayList<>();
    private String stringYear, stringModelId;
    private int mCurrentPage,cacheId;
    private static String BUNDLE_COLLEGE_YEAR = "collegeYear";
    private static String BUNDLE_COLLEGE_MODEL_ID = "collegeModelID";
    private static String BUNDLE_CACHE_ID = "collegeCacheID";
    //缓存
    private CacheManager cacheManager2;
    private static final String CACHE_COLLEGE_DATA_TITLE = "college_data_title_list";//college_data_title_list
    private static final String CACHE_COLLEGE_BOOK_TITLE = "college_book_title_list";
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private ArrayList<TextView> listText;
    private FragmentManager fragmentManager;

    public static CollegeListDetailFragment getInstance(String stringYear, String stringModelId,int cacheId) {
        CollegeListDetailFragment fragment = new CollegeListDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_COLLEGE_YEAR, stringYear);
        bundle.putString(BUNDLE_COLLEGE_MODEL_ID, stringModelId);
        bundle.putInt(BUNDLE_CACHE_ID, cacheId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_college_list_detail, null, false);
        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.ll_table);
        stringYear = getArguments().getString(BUNDLE_COLLEGE_YEAR);
        stringModelId = getArguments().getString(BUNDLE_COLLEGE_MODEL_ID);
        cacheId = getArguments().getInt(BUNDLE_CACHE_ID);
        fragmentManager = getChildFragmentManager();
        if ("book".equals(stringYear)) {
            cacheManager2 = CacheManager.getInstance().open(CACHE_COLLEGE_BOOK_TITLE+cacheId, 1);
        } else {
            mDiskLruCacheUtil = new DiskLruCacheUtil(getActivity(), CACHE_COLLEGE_DATA_TITLE+cacheId);
        }
        loadLocalDate();
        return view;
    }

    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            String stringJson ;
            if ("book".equals(stringYear)) {
                stringJson = cacheManager2.getString(CACHE_COLLEGE_BOOK_TITLE+cacheId);
            } else {
                stringJson = mDiskLruCacheUtil.getStringCache(CACHE_COLLEGE_DATA_TITLE+cacheId);
            }
            if (!TextUtils.isEmpty(stringJson)) {
                mSessionDaysList.clear();
                mSessionDaysDetailList.clear();
                CollegeListDay bean = new Gson().fromJson(stringJson, new TypeToken<CollegeListDay>() {
                }.getType());
                for (int i = 0; i < bean.getDateArray().size(); i++) {
                    if(AppApplication.systemLanguage == 1){
                        mSessionDaysList.add(DateUtil.getFormatMonthAndDayChinese(bean.getDateArray().get(i).getTimeStart()));
                    }else {
                        mSessionDaysList.add(DateUtil.getFormatMonthAndDayEnglish(bean.getDateArray().get(i).getTimeStart()));
                    }
                    mSessionDaysDetailList.add(bean.getDateArray().get(i).getTimeStart());
                }
                if (mSessionDaysList.size() > 0) {
                    if ("book".equals(stringYear)) {
                        mPageAdapter = new CollegeListItemFragmentAdapter(fragmentManager, mSessionDaysDetailList, 100, stringModelId);
                    } else {
                        mPageAdapter = new CollegeListItemFragmentAdapter(fragmentManager, mSessionDaysDetailList, 101, stringModelId);
                    }
                    mViewPager.setScrollble(false);
                    mViewPager.setAdapter(mPageAdapter);
                    mViewPager.setCurrentItem(0);
                    mTabLayout.removeAllViews();
                    getSessionDays();
                    for (int i = 0; i < mSessionDaysList.size(); i++) {
                        if (TimeUtils.getCurrentTimeMD().equals(mSessionDaysList.get(i))) {
                            mCurrentPage = i;
                        }
                    }
                    mViewPager.setCurrentItem(mCurrentPage, false);
                }
                ToastUtils.showToast(getString(R.string.connect_network));
            }
        } else {
            if ("book".equals(stringYear)) {
                getCollegeBookDayList();
            } else {
                getCollegeList();
            }
        }
    }

    private void getSessionDays() {
        if(listText == null){
            listText = new ArrayList<>();
        }else {
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
    //获取当年可播放的标题
    private void getCollegeList() {
        CHYHttpClientUsage.getInstanse().doGetDayArrayListData(stringModelId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                CollegeListDay bean = new Gson().fromJson(response.toString(), new TypeToken<CollegeListDay>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    mDiskLruCacheUtil.put(CACHE_COLLEGE_DATA_TITLE+cacheId, response.toString());
                    mSessionDaysList.clear();
                    mSessionDaysDetailList.clear();
                    for (int i = 0; i < bean.getDateArray().size(); i++) {
                        if(AppApplication.systemLanguage == 1){
                            mSessionDaysList.add(DateUtil.getFormatMonthAndDayChinese(bean.getDateArray().get(i).getTimeStart()));
                        }else {
                            mSessionDaysList.add(DateUtil.getFormatMonthAndDayEnglish(bean.getDateArray().get(i).getTimeStart()));
                        }
                        mSessionDaysDetailList.add(bean.getDateArray().get(i).getTimeStart());
                    }
                    if (mSessionDaysDetailList.size() > 0) {
                        mPageAdapter = new CollegeListItemFragmentAdapter(fragmentManager, mSessionDaysDetailList, 101, stringModelId);
                        mViewPager.setScrollble(false);
                        mViewPager.setAdapter(mPageAdapter);
                        mViewPager.setCurrentItem(0);
                        mTabLayout.removeAllViews();
                        getSessionDays();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    //获取当年可预约的课件信息
    //测试数据 290
    private void getCollegeBookDayList() {
        CHYHttpClientUsage.getInstanse().doGetCollegeBookDays(Constants.getConId() + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                CollegeListDay bean = new Gson().fromJson(response.toString(), new TypeToken<CollegeListDay>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    cacheManager2.saveString(CACHE_COLLEGE_BOOK_TITLE+cacheId, response.toString());
                    mSessionDaysList.clear();
                    mSessionDaysDetailList.clear();
                    for (int i = 0; i < bean.getDateArray().size(); i++) {
                        if(AppApplication.systemLanguage == 1){
                            mSessionDaysList.add(DateUtil.getFormatMonthAndDayChinese(bean.getDateArray().get(i).getTimeStart()));
                        }else {
                            mSessionDaysList.add(DateUtil.getFormatMonthAndDayEnglish(bean.getDateArray().get(i).getTimeStart()));
                        }
                        mSessionDaysDetailList.add(bean.getDateArray().get(i).getTimeStart());
                    }
                    if (mSessionDaysList.size() > 0) {
                        mPageAdapter = new CollegeListItemFragmentAdapter(fragmentManager, mSessionDaysDetailList, 100, stringModelId);
                        mViewPager.setScrollble(false);
                        mViewPager.setAdapter(mPageAdapter);
                        mViewPager.setCurrentItem(0);
                        mTabLayout.removeAllViews();
                        getSessionDays();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if ("book".equals(stringYear)) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        } else {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
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
            if ("book".equals(stringYear)) {
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
            } else {
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
            }
        }
    }
}