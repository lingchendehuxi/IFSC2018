package com.android.incongress.cd.conference.fragments.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.MessageStationAdapter;
import com.android.incongress.cd.conference.adapters.MindBookListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeListDetailBean;
import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.beans.MyOrderCourse;
import com.android.incongress.cd.conference.fragments.DynamicHomeFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/15.
 * 预约提醒的子fragment
 */
public class MyMindBookListFragment extends BaseFragment implements MindBookFragment.ManagerNewLister, XRecyclerView.LoadingListener, MindBookListAdapter.DeleteItemOnClick {
    private List<MyOrderCourse.ArrayBean> mOrderBeans;
    private MindBookListAdapter mAdapter;

    protected XRecyclerView mRecyclerView;
    private LinearLayout ll_tips;
    private int mCurrentPage = 1;
    private static final String BUNDLE_TIME = "order_title";
    private List<Meeting> mMeetingBeans = new ArrayList<>();
    private TextView tv_tips;
    private String mTitle;


    public MyMindBookListFragment() {
    }

    public static final MyMindBookListFragment getInstance(String title) {
        MyMindBookListFragment fragment = new MyMindBookListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTitle = getArguments().getString(BUNDLE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_item, null);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        ll_tips = view.findViewById(R.id.ll_tips);
        tv_tips = view.findViewById(R.id.tv_tips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        ((HomeActivity) getActivity()).setMyBookClick(this);
        mOrderBeans = new ArrayList<>();
        mAdapter = new MindBookListAdapter(this, getActivity(), mOrderBeans);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setRefreshing(true);
        return view;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        //刷新
        if (mTitle.equals(getString(R.string.schedule))) {
            getMyOrderCourse();
        } else if (mTitle.equals(getString(R.string.question_meeting))) {
            getMyDaySchedule();
        } else {
            ll_tips.setVisibility(View.VISIBLE);
            mRecyclerView.refreshComplete();
        }
    }

    //获取我的日程
    private void getMyDaySchedule() {
        mOrderBeans.clear();
        tv_tips.setText(getString(R.string.my_schedule_tips));
        mMeetingBeans = ConferenceDbUtils.getMeetingsByAttention("1");
        for (int i = 0; i < mMeetingBeans.size(); i++) {
            Meeting meeting = mMeetingBeans.get(i);
            MyOrderCourse.ArrayBean arrayBean = new MyOrderCourse.ArrayBean();
            arrayBean.setType(1);
            arrayBean.setTime(meeting.getMeetingDay() + "  " + meeting.getStartTime() + " - " + meeting.getEndTime());
            arrayBean.setTopic(meeting.getTopic());
            mOrderBeans.add(arrayBean);
        }
        if (mOrderBeans.size() == 0) {
            ll_tips.setVisibility(View.VISIBLE);
        } else {
            ll_tips.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.refreshComplete();
    }
    //取消我的提醒日程
    private void CancelMySchedule(int position){
        mMeetingBeans.clear();
        mMeetingBeans = ConferenceDbUtils.getMeetingsByAttention("1");
        mMeetingBeans.get(position).setAttention(0);
        if(mMeetingBeans.get(position).save()){
            mOrderBeans.remove(position);
            mAdapter.notifyDataSetChanged();
            ll_tips.setVisibility(View.GONE);
            ToastUtils.showToast("删除成功");
            if(mOrderBeans.size() == 0){
                ll_tips.setVisibility(View.VISIBLE);
            }
        }else {
            ToastUtils.showToast("删除失败，请重试");
        }

    }

    //获取我的课件预约
    private void getMyOrderCourse() {
        CHYHttpClientUsage.getInstanse().doGetMYOrderCourse(Constants.getConId(), SharePreferenceUtils.getUser(Constants.USER_ID), new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mOrderBeans.clear();
                MyOrderCourse bean = new Gson().fromJson(response.toString(), new TypeToken<MyOrderCourse>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    mOrderBeans.addAll(bean.getArray());
                    for (int i = 0; i < mOrderBeans.size(); i++) {
                        mOrderBeans.get(i).setType(3);
                    }
                    mAdapter.notifyDataSetChanged();
                    ll_tips.setVisibility(View.GONE);
                }
                if (mOrderBeans.size() == 0) {
                    ll_tips.setVisibility(View.VISIBLE);
                }
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }

    //删除我的课件预约
    private void deleteMyOrderCourse(String topic, final int position) {
        CHYHttpClientUsage.getInstanse().doDeleteMYOrderCourse(topic, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyOrderCourse bean = new Gson().fromJson(response.toString(), new TypeToken<MyOrderCourse>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    mOrderBeans.remove(position);
                    mAdapter.notifyDataSetChanged();
                    ll_tips.setVisibility(View.GONE);
                    ToastUtils.showToast("删除成功");
                }
                if (mOrderBeans.size() == 0) {
                    ll_tips.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }

    @Override
    public void deleteItem(int position) {
        if (mTitle.equals(getString(R.string.schedule))) {
            deleteMyOrderCourse(mOrderBeans.get(position).getTopic(), position);
        }else if(mTitle.equals(getString(R.string.question_meeting))){
            CancelMySchedule(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.MY_ORDER_COURSER);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.MY_ORDER_COURSER);
    }

    @Override
    public void managerClick() {
        for (int i = 0; i < mOrderBeans.size(); i++) {
            mOrderBeans.get(i).setShow(!mOrderBeans.get(i).isShow());
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity) getActivity()).cleanListerList();
    }
}
