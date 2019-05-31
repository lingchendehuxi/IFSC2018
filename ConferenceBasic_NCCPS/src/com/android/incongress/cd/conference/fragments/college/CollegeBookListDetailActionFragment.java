package com.android.incongress.cd.conference.fragments.college;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.incongress.cd.conference.adapters.CollegeListDetailActionAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeBookCoveryBean;
import com.android.incongress.cd.conference.beans.CollegeListDetailBean;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.dialog.SingleButtonDialog;
import com.android.incongress.cd.conference.widget.refresh_view.XRefreshView;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Jacky on 2016/2/1.
 */
public class CollegeBookListDetailActionFragment extends BaseFragment implements CollegeListDetailActionAdapter.CollegeOnItemOnclicking {
    private StickyListHeadersListView mStickLVSpeaker;
    private CollegeListDetailActionAdapter mScheduleListAdapter;
    private CollegeBookCoveryBean bookBean;

    private static final String BUNDLE_COLLEGE_MODEL_ID = "collegeModelID";
    //哪一天的标识
    private static final int BOOKTYPE = 100;

    private LinearLayout ll_tips;
    private List<CollegeListDetailBean.ClassArrayBean.SessionArrayBean> listBean = new ArrayList<>();
    private List<CollegeListDetailBean.ClassArrayBean> classBean = new ArrayList<>();

    public static CollegeBookListDetailActionFragment getInstance(CollegeBookCoveryBean bookBean) {
        CollegeBookListDetailActionFragment fragment = new CollegeBookListDetailActionFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_COLLEGE_MODEL_ID, bookBean);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            bookBean = (CollegeBookCoveryBean) getArguments().getSerializable(BUNDLE_COLLEGE_MODEL_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_book_college_list, null);
        mStickLVSpeaker = view.findViewById(R.id.slhlv_sessions);
        ll_tips = view.findViewById(R.id.ll_tips);
        getCollegeBookDetailList();
        return view;
    }


    //获取当年当日可预约的课件
    private void getCollegeBookDetailList() {
        List<CollegeBookCoveryBean.ClassArrayBean> listTempBean = bookBean.getClassArray();
        classBean.clear();
        listBean.clear();
        if (listTempBean != null && listTempBean.size() != 0) {
            for (int i = 0; i < listTempBean.size(); i++) {
                CollegeBookCoveryBean.ClassArrayBean fBean = listTempBean.get(i);
                CollegeListDetailBean.ClassArrayBean Tbean = new CollegeListDetailBean.ClassArrayBean();
                Tbean.setClassId(fBean.getClassesId());
                Tbean.setClassName(fBean.getClassesName());
                List<CollegeBookCoveryBean.ClassArrayBean.SessionArrayBean> listTBean = fBean.getSessionArray();
                for (int j = 0; j < listTBean.size(); j++) {
                    CollegeBookCoveryBean.ClassArrayBean.SessionArrayBean ffBean = listTBean.get(j);
                    CollegeListDetailBean.ClassArrayBean.SessionArrayBean TTbean = new CollegeListDetailBean.ClassArrayBean.SessionArrayBean();
                    TTbean.setClassId(fBean.getClassesId());
                    TTbean.setSessionId(ffBean.getSessionGroupId());
                    TTbean.setSessionName(ffBean.getSessionGroupName());
                    listBean.add(TTbean);
                }
                classBean.add(Tbean);
            }
            if (listBean.size() > 0) {
                ll_tips.setVisibility(View.GONE);
                mScheduleListAdapter = new CollegeListDetailActionAdapter(getActivity(), listBean, classBean, CollegeBookListDetailActionFragment.this, BOOKTYPE);
                mStickLVSpeaker.setAdapter(mScheduleListAdapter);
            } else {
                ll_tips.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemOnclick(String sessionId, int mType, int limit, String limitTime) {
        Intent intent = new Intent(getActivity(), CollegeCourseBookActivity.class);
        intent.putExtra("book_session_id", sessionId);
        intent.putExtra("book_type", mType);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }
}
