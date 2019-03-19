package com.android.incongress.cd.conference.fragments.live;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.LiveAdapter;
import com.android.incongress.cd.conference.adapters.LiveDetailListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.LiveClassBean;
import com.android.incongress.cd.conference.beans.LiveInfoBean;
import com.android.incongress.cd.conference.beans.LiveListInfoBean;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.LiveForOrderInfo;
import com.android.incongress.cd.conference.model.LiveForOrderInfoBean;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.WrapContentLinearLayoutManager;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.ListViewForFix;
import com.android.incongress.cd.conference.widget.ListViewForScrollView;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.popup.ChooseBBPopupWindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
 * 获取相关直播列表
 *
 * @author Administrator
 */
public class LiveListInfoFragment extends BaseFragment {

    private ListViewForFix mRecyclerView;

    private LinearLayout ll_tips, ll_order;
    private TextView mNoDate, tv_address, tv_title, tv_time, tv_holder, tv_order;

    private List<LiveForOrderInfoBean> lastBeans = new ArrayList<>();
    private LiveDetailListAdapter mAdapter;
    private RelativeLayout rl_layout;
    private LiveForOrderInfoBean firstBean;
    private int classId;

    private static final String LIVEINFOBEAN = "live_info_bean";

    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    public static LiveListInfoFragment getInstance(int classId) {
        LiveListInfoFragment fragment = new LiveListInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LIVEINFOBEAN, classId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isBackView) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        hideShurufa();
        MobclickAgent.onPageStart(Constants.FRAGMENT_LIVE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_LIVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.live_book_fragment, null);
        initView(view);
        classId = getArguments().getInt(LIVEINFOBEAN);
        mAdapter = new LiveDetailListAdapter(getActivity(), lastBeans,this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusable(false);
        getRelativeLive(classId);
        return view;
    }

    private void initView(View view) {
        tv_address = view.findViewById(R.id.tv_address);
        tv_title = view.findViewById(R.id.tv_title);
        tv_time = view.findViewById(R.id.tv_time);
        tv_holder = view.findViewById(R.id.tv_holder);
        ll_order = view.findViewById(R.id.ll_order);
        mRecyclerView = view.findViewById(R.id.xr_live_list);
        mNoDate = view.findViewById(R.id.no_bb_data);
        ll_tips = view.findViewById(R.id.ll_tips);
        rl_layout = view.findViewById(R.id.rl_layout);
        tv_order = view.findViewById(R.id.tv_order);
        ll_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_order.setText(getString(R.string.orderedLive));
                ll_order.setBackground(getResources().getDrawable(R.drawable.booked_course));
                ll_order.setClickable(false);
                saveBean(firstBean);
            }
        });
    }

    /**
     * 获取相关直播的链接
     */
    private void getRelativeLive(int classId) {
        CHYHttpClientUsage.getInstanse().doGetRelativeLive(classId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                List<LiveForOrderInfoBean> list = new Gson().fromJson(JSONCatch.parseString("sessionArray", response), new TypeToken<List<LiveForOrderInfoBean>>() {
                }.getType());
                if (list.size() > 0) {
                    firstBean = list.get(0);
                    StringUtils.setTextShow(tv_address, firstBean.getLiveClassesName());
                    StringUtils.setTextShow(tv_title, firstBean.getSessionGroupName());
                    tv_time.setText(firstBean.getSessionDay() + " " + firstBean.getTime());
                    ArrayList<?> list1 = (ArrayList<?>) firstBean.getZcrArray();
                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                    String jsonString = gson.toJson(list1);
                    tv_holder.setText(StringUtils.getNewString(jsonString));
                    for (int i = 1; i < list.size(); i++) {
                        lastBeans.add(list.get(i));
                        lastBeans.add(list.get(i));
                        lastBeans.add(list.get(i));
                        lastBeans.add(list.get(i));
                        lastBeans.add(list.get(i));
                    }
                    if (lastBeans.size() > 0) {
                        mAdapter.notifyDataSetChanged();
                    }else {
                        ll_tips.setVisibility(View.VISIBLE);
                    }
                    //验证是否已经预约
                    if(ConferenceDbUtils.liveIsOrdered(firstBean.getSessionGroupId())){
                        tv_order.setText("已预约");
                        ll_order.setBackground(getResources().getDrawable(R.drawable.booked_course));
                        ll_order.setClickable(false);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }
    public void saveBean(LiveForOrderInfoBean firstBean){
        if (firstBean != null) {
            replaceBean(firstBean);
            //添加闹钟提醒
            Alert alertbean = new Alert();
            if (!TextUtils.isEmpty(firstBean.getStartTime()) && !TextUtils.isEmpty(firstBean.getTime())) {
                String[] timeString = firstBean.getTime().split("-");
                alertbean.setDate(firstBean.getSessionDay());
                alertbean.setStart(timeString[0]);
                alertbean.setEnd(timeString[1]);
            } else {
                ToastUtils.showToast("直播时间不正确，请联系管理员");
                return;
            }
            alertbean.setEnable(1);
            alertbean.setRelativeid(String.valueOf(firstBean.getSessionGroupId()));
            alertbean.setRepeatdistance("5");
            alertbean.setRepeattimes("0");
            alertbean.setRoom(firstBean.getLiveClassesName());
            alertbean.setIdenId(firstBean.getSessionGroupId());
            alertbean.setTitle(firstBean.getSessionGroupName());
            alertbean.setLiveUrl(firstBean.getLiveUrl());
            alertbean.setType(AlertBean.TYPE_LIVE); //3代表直播

            alertbean.save();
            AlermClock.addClock(alertbean);
            mAdapter.orderLive(firstBean.getSessionGroupId());
        }
    }

    //这里替换保存的实体bean 返回参数 是否保存成功
    private boolean replaceBean(LiveForOrderInfoBean srcBean) {
        LiveForOrderInfo dsrBean = new LiveForOrderInfo();
        dsrBean.setLiveClassesName(srcBean.getLiveClassesName());
        dsrBean.setLiveUrl(srcBean.getLiveClassesName());
        dsrBean.setScrRoles(srcBean.getScrRoles());
        dsrBean.setSessionDay(srcBean.getSessionDay());
        dsrBean.setSessionGroupId(srcBean.getSessionGroupId());
        dsrBean.setSessionGroupName(srcBean.getSessionGroupName());
        dsrBean.setStartTime(srcBean.getStartTime());
        dsrBean.setTime(srcBean.getTime());
        dsrBean.setWeeks(srcBean.getWeeks());
        return dsrBean.save();
    }
}
