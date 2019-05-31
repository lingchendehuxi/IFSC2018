package com.android.incongress.cd.conference.fragments.meet_register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoadDataActivity;
import com.android.incongress.cd.conference.adapters.CollegeListDetailActionAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeBookCoveryBean;
import com.android.incongress.cd.conference.beans.CollegeListDetailBean;
import com.android.incongress.cd.conference.beans.HomeMeetBean;
import com.android.incongress.cd.conference.fragments.NewHomeCurrentFragment;
import com.android.incongress.cd.conference.fragments.college.CollegeCourseBookActivity;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Jacky on 2016/2/1.
 */
public class HotMeetFragment extends BaseFragment {
    private StickyListHeadersListView mStickLVSpeaker;
    private HotMeetListDetailAdapter mScheduleListAdapter;
    private List<HomeMeetBean> listMeet;

    private static final String HOT_MEET_BEAN_LIST = "hot_meet_list";

    private LinearLayout ll_tips;

    public static HotMeetFragment getInstance(List<HomeMeetBean> listMeet) {
        HotMeetFragment fragment = new HotMeetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HOT_MEET_BEAN_LIST, (Serializable) listMeet);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listMeet = (ArrayList<HomeMeetBean>) getArguments().getSerializable(HOT_MEET_BEAN_LIST);
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
        dataHandle();
        return view;
    }


    //处理数据
    private void dataHandle() {
        if (listMeet == null || listMeet.size() <= 0) {
            return;
        }
        Collections.sort(listMeet, new Comparator<HomeMeetBean>() {
            /**
             *
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(HomeMeetBean lhs, HomeMeetBean rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getConferencesStartDay());
                Date date2 = DateUtil.stringToDate(rhs.getConferencesStartDay());
                // 对日期字段进行升序，如果欲降序可采用after方法
                if (date1.after(date2)) {
                    return 1;
                }
                return -1;
            }
        });
        Date time = Calendar.getInstance().getTime();
        //用于比较是否是当月
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String currentData = formatter.format(time);//yyyy-MM
        //用于比较是否是已经开始
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
        String currentBegin = formatter2.format(time);//yyyy-MM-dd
        for (int i = 0; i < listMeet.size(); i++) {
            HomeMeetBean bean = listMeet.get(i);
            bean.setMonthString(DateUtil.getCurrentMonth(bean.getConferencesStartDay()));
            if (bean.getConferencesStartDay().equals(currentBegin)) {
                bean.setBegin(true);
                bean.setCurrentMonth(true);
            } else if (bean.getConferencesStartDay().contains(currentData)) {
                bean.setBegin(false);
                bean.setCurrentMonth(true);
            } else {
                bean.setBegin(false);
                bean.setCurrentMonth(false);
            }
        }
        if (listMeet.size() > 0) {
            ll_tips.setVisibility(View.GONE);
            mScheduleListAdapter = new HotMeetListDetailAdapter(getActivity(), listMeet);
            mStickLVSpeaker.setOnItemClickListener(new OnPlanItemClick());
            mStickLVSpeaker.setAdapter(mScheduleListAdapter);
        } else {
            ll_tips.setVisibility(View.VISIBLE);
        }
    }

    private class OnPlanItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HomeMeetBean homeMeetBean = (HomeMeetBean) parent.getAdapter().getItem(
                    position);
            if (SharePreferenceUtils.getDataBoolean(String.valueOf(homeMeetBean.getTotalConId()), false)) {
                ((HomeActivity) getActivity()).performBackClick();
                HomeActivity.activity.mNavigationBar.selectTab(2);
            } else {
                if (homeMeetBean.getState() == 0) {
                    ToastUtils.showToast("大会暂未开始，敬请期待");
                } else {
                    checkUpdate(homeMeetBean.getTotalConId(), homeMeetBean.getConId(), homeMeetBean.getFromWhere());
                }
            }
        }
    }

    /**
     * 查看是否有新的安装包
     */
    private final int backCode = 10001;

    private void checkUpdate(final int totalConId, final int conId, final String fromWhere) {
        int mDbVersion = SharePreferenceUtils.getDataInt(Constants.PREFERENCE_DB_VERSION + totalConId, 0);
        //检查更新数据
        CHYHttpClientUsage.getInstanse().doGetInitData(conId, mDbVersion, AppApplication.instance().getVersionName(), totalConId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray array = JSONCatch.parseJsonarray("version", response);
                if (array != null && array.length() > 0) {
                    Intent intent = new Intent(getActivity(), LoadDataActivity.class);
                    intent.putExtra("totalConId", totalConId);
                    intent.putExtra("fromWhere", fromWhere);
                    intent.putExtra("conId", conId);
                    startActivityForResult(intent, backCode);
                } else {
                    action(NewHomeCurrentFragment.getInstance(totalConId, conId, fromWhere), null);
                }
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == backCode) {
            action(NewHomeCurrentFragment.getInstance(data.getIntExtra("totalConId", 0), data.getIntExtra("conId", 0), data.getStringExtra("fromWhere")), null);
        }
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
