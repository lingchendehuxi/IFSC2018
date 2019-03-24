package com.android.incongress.cd.conference.fragments.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.MeetingBusRemindMyAdapater;
import com.android.incongress.cd.conference.adapters.MindBookListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.BusRemindBean;
import com.android.incongress.cd.conference.beans.MyOrderCourse;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.LiveForOrderInfo;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

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
    private MeetingBusRemindMyAdapater mBusAdapter;

    protected XRecyclerView mRecyclerView;
    private LinearLayout ll_tips;
    private static final String BUNDLE_TIME = "order_title";
    private List<Meeting> mMeetingBeans = new ArrayList<>();
    private List<LiveForOrderInfo> mLiveBeans = new ArrayList<>();
    private List<BusRemindBean> mBusArrayList = new ArrayList<>();
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
        if(!mTitle.equals(getString(R.string.bus))){
            mAdapter = new MindBookListAdapter(this, getActivity(), mOrderBeans);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mBusAdapter = new MeetingBusRemindMyAdapater(getActivity(), mBusArrayList);
            mRecyclerView.setAdapter(mBusAdapter);
        }
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
            if(!NetWorkUtils.isNetworkConnected(getActivity())){
                mRecyclerView.refreshComplete();
                ToastUtils.showToast(getString(R.string.nowifi));
                return;
            }
            getMyOrderCourse();
        } else if (mTitle.equals(getString(R.string.question_meeting))) {
            getMyDaySchedule();
        } else if(mTitle.equals(getString(R.string.live))){
            getMyLive();
        }else if(mTitle.equals(getString(R.string.bus))){
            getMyBus();
            /*ll_tips.setVisibility(View.VISIBLE);
            mRecyclerView.refreshComplete();*/
        }
    }
    //获取我的直播
    private void getMyLive() {
        mOrderBeans.clear();
        mLiveBeans = ConferenceDbUtils.getLivesByOrder();
        for (int i = 0; i < mLiveBeans.size(); i++) {
            LiveForOrderInfo liveBean = mLiveBeans.get(i);
            MyOrderCourse.ArrayBean arrayBean = new MyOrderCourse.ArrayBean();
            arrayBean.setType(4);
            arrayBean.setTime(liveBean.getSessionDay()+" "+liveBean.getTime());
            arrayBean.setTopic(liveBean.getSessionGroupName());
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
    //删除我的预约直播
    private void deleteMyLive(int position) {
        LiveForOrderInfo Beans = ConferenceDbUtils.getLiveBean(mLiveBeans.get(position).getSessionGroupId());
        //添加需要删除的闹钟提醒
        Alert alertbean = new Alert();
        if (!TextUtils.isEmpty(Beans.getStartTime()) && !TextUtils.isEmpty(Beans.getTime())) {
            String[] timeString = Beans.getTime().split("-");
            alertbean.setDate(Beans.getSessionDay());
            alertbean.setStart(timeString[0]);
            alertbean.setEnd(timeString[1]);
        } else {
            ToastUtils.showToast("直播时间不正确，请联系管理员");
            return;
        }
        alertbean.setEnable(1);
        alertbean.setRelativeid(String.valueOf(Beans.getSessionGroupId()));
        alertbean.setRepeatdistance("5");
        alertbean.setRepeattimes("0");
        alertbean.setRoom(Beans.getLiveClassesName());
        alertbean.setIdenId(Beans.getSessionGroupId());
        alertbean.setTitle(Beans.getSessionGroupName());
        alertbean.setLiveUrl(Beans.getLiveUrl());
        alertbean.setType(AlertBean.TYPE_LIVE); //3代表直播
        AlermClock.disableAlert(AppApplication.getContext(),alertbean);
        Beans.delete();
        mOrderBeans.remove(position);
        mAdapter.notifyDataSetChanged();
        ToastUtils.showToast("删除成功");
        ll_tips.setVisibility(View.GONE);
        if(mOrderBeans.size() == 0){
            ll_tips.setVisibility(View.VISIBLE);
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
        Meeting meeting = mMeetingBeans.get(position);
        Alert alertbean = new Alert();
        alertbean.setDate(meeting.getMeetingDay());
        alertbean.setEnable(1);
        alertbean.setEnd(meeting.getEndTime());
        alertbean.setRelativeid(String.valueOf(meeting.getSessionGroupId()));
        alertbean.setRepeatdistance("5");
        alertbean.setRepeattimes("0");
        alertbean.setIdenId(meeting.getMeetingId());
        alertbean.setStart(meeting.getStartTime());
        alertbean.setTitle(meeting.getTopic() + "#@#" + meeting.getTopicEn());
        alertbean.setType(AlertBean.TYPE_MEETING);
        AlermClock.disableAlert(AppApplication.getContext(),alertbean);
        meeting.setAttention(0);
        if(meeting.save()){
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
                ToastUtils.showToast("获取信息失败，请联系管理员");
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
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }
    //获取我的班车
    private void getMyBus() {
        mBusArrayList.clear();
        List<BusRemindBean> tempList = ConferenceDbUtils.getBusByOrder();
        mBusArrayList.addAll(tempList);
        if (mBusArrayList.size() == 0) {
            ll_tips.setVisibility(View.VISIBLE);
        } else {
            ll_tips.setVisibility(View.GONE);
            mBusAdapter.notifyDataSetChanged();
        }
        mRecyclerView.refreshComplete();
    }

    @Override
    public void deleteItem(int position) {
        if (mTitle.equals(getString(R.string.schedule))) {
            deleteMyOrderCourse(mOrderBeans.get(position).getTopic(), position);
        }else if(mTitle.equals(getString(R.string.question_meeting))){
            CancelMySchedule(position);
        }else if(mTitle.equals(getString(R.string.live))){
            deleteMyLive(position);
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
        if(!mTitle.equals(getString(R.string.bus))){
            for (int i = 0; i < mOrderBeans.size(); i++) {
                mOrderBeans.get(i).setShow(!mOrderBeans.get(i).isShow());
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity) getActivity()).cleanListerList();
    }
}
