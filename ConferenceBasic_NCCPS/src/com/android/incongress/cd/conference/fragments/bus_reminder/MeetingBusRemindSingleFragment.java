package com.android.incongress.cd.conference.fragments.bus_reminder;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.adapters.MeetingBusRemindAdapater;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BusRemindBean;
import com.android.incongress.cd.conference.model.BusInfo;
import com.android.incongress.cd.conference.utils.AlarmUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MeetingBusRemindSingleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private  LinearLayoutManager mLayoutManager;

    private BusInfo.DateArrayBean busInfo;
    private List<BusInfo.DateArrayBean.BusArrayBean> mBusArrayList;
    private MeetingBusRemindAdapater mAdapter;
    private static final String ARG_POSITION = "position";


    public static MeetingBusRemindSingleFragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_POSITION,position);
        MeetingBusRemindSingleFragment fragment = new MeetingBusRemindSingleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null)
            mPosition = bundle.getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_bus_remind, container, false);

        mSwipeRefreshWidget = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.lisr);

        busInfo = new BusInfo.DateArrayBean();
        mBusArrayList = new ArrayList<>();
        mAdapter = new MeetingBusRemindAdapater(getActivity(), mBusArrayList);

        mRecyclerView.setAdapter(mAdapter);

        initData();
        /**
         * 设置刷新图标的颜色
         */
        mSwipeRefreshWidget.setColorSchemeResources(R.color.alpha_theme_color, R.color.alpha_theme_color, R.color.alpha_theme_color, R.color.alpha_theme_color);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new MeetingBusRemindAdapater.BusRemindClickListener() {
            @Override
            public void onStartRemindListener(View view, final BusInfo.DateArrayBean.BusArrayBean busArrayBean, final int position) {
                alertDialog(busArrayBean,position,busArrayBean.isStartNotify(),0);
            }

            @Override
            public void onBackRemindListener(View view, BusInfo.DateArrayBean.BusArrayBean busArrayBean, int position) {
                alertDialog(busArrayBean, position, busArrayBean.isEndNotify(),1);
            }
        });
        return view;
    }

    /**
     * 弹出提示
     */
    private void alertDialog(final BusInfo.DateArrayBean.BusArrayBean busArrayBean, final int position, final boolean notify, final int isStartOrBack) {
        String notifyMessage;
        String s = busArrayBean.getBusDate() +" "+ busArrayBean.getBusTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            Date date = sdf.parse(s);
            if(System.currentTimeMillis() > date.getTime()) {
                showDialog("抱歉，您添加的班车提醒时间已过！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, false);

            }else {

                if(!notify) {
                    notifyMessage = "确定添加闹钟吗？ \n为保障您正常使用班车提醒功能，请勿关闭本程序后台运行";
                }else {
                    notifyMessage = "确定删除闹钟吗？ \n为保障您正常使用班车提醒功能，请勿关闭本程序后台运行";
                }

                showDialog(notifyMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //设置闹钟
                        BusRemindBean busRemindBean = new BusRemindBean();
                        busRemindBean.setBackTime(busArrayBean.getBackTime());
                        busRemindBean.setBusDate(busArrayBean.getBusDate());
                        busRemindBean.setBusTime(busArrayBean.getBusTime());
                        busRemindBean.setBusFrom(busArrayBean.getBusFrom());
                        busRemindBean.setBusInfoId(busArrayBean.getBusInfoId());
                        busRemindBean.setBusTo((busArrayBean.getBusTo()));
                        busRemindBean.setIsVip(busArrayBean.getIsVip());
                        busRemindBean.setNotify(!notify);
                        busRemindBean.setIsStartOrBack(isStartOrBack);

                        if(!notify) {
                            AlarmUtils.addAlarm(getActivity(), busRemindBean);
                        }else {
                            AlarmUtils.deleteAlarm(getActivity(), busRemindBean);
                        }

                        if(isStartOrBack == 0) {
                            mBusArrayList.get(position).setStartNotify(!notify);
                        }else {
                            mBusArrayList.get(position).setEndNotify(!notify);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, false);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshWidget.setRefreshing(false);
        initData();
    }
    private void initData() {
        CHYHttpClientUsage.getInstanse().doGetBusInfo(AppApplication.conId,new JsonHttpResponseHandler(Constants.ENCODING_GBK){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                    try {
                        Gson gson = new Gson();
                        BusInfo busInfo = gson.fromJson(response.toString(),BusInfo.class);

                        if(busInfo != null && busInfo.getState() == 1) {
                            //日程存在
                            mBusArrayList.clear();
                            BusInfo.DateArrayBean dateArrayBean = busInfo.getDateArray().get(mPosition);
                            List<BusInfo.DateArrayBean.BusArrayBean> busArray = dateArrayBean.getBusArray();
                            mBusArrayList.addAll(busArray);
                            mAdapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

        });
    }

}
