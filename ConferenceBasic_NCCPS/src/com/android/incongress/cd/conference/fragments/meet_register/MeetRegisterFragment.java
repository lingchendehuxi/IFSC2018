package com.android.incongress.cd.conference.fragments.meet_register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.adapters.MeetRegisterListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeetRegisterBean;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by 13008 on 2019/4/12.
 */

public class MeetRegisterFragment extends BaseFragment implements MeetRegisterListAdapter.MeetRegisterOnClick{
    @BindView(R.id.recyclerview)
    XRecyclerView recyclerView;
    @BindView(R.id.ll_tips)
    LinearLayout ll_tips;
    @BindView(R.id.ll_chose_meet)
    LinearLayout ll_chose_meet;
    @BindView(R.id.tv_tips)
    TextView tv_tips;
    @BindView(R.id.bt_next)
    Button bt_next;
    private MeetRegisterListAdapter listAdapter;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    public static String CHOSE_TYPE = "chose_type";
    private int mType;
    private List<MeetRegisterBean> listBean = new ArrayList<>();
    private int currentPositon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_register_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    public static MeetRegisterFragment getInstance(int type){
        MeetRegisterFragment fragment = new MeetRegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CHOSE_TYPE,type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }

    private void initView() {
        mType = getArguments().getInt(CHOSE_TYPE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
        bt_next.setEnabled(false);
        bt_next.setClickable(false);
        bt_next.getBackground().setAlpha(104);
        switch (mType){
            case 1:
                tv_tips.setText(getString(R.string.meet_register_tip));
                getMeetRegisterList();
                break;
            case 2:
                tv_tips.setText(getString(R.string.meet_contribute_tip));
                getMeetContributeList();
                break;
            case 3:
                tv_tips.setText(getString(R.string.meet_hotel_tip));
                getMeetHotelList();
                break;
        }
    }

    /**
     * 获取参会注册列表
     */
    private void getMeetRegisterList() {
        CHYHttpClientUsage.getInstanse().doGetRegisterMeetList(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                listBean.clear();
                List<MeetRegisterBean> list = new Gson().fromJson(JSONCatch.parseJsonarray("conArray", response).toString(), new TypeToken<List<MeetRegisterBean>>() {
                }.getType());
                listBean.addAll(list);
                if (listBean != null && listBean.size() > 0) {
                    ll_tips.setVisibility(View.GONE);
                    ll_chose_meet.setVisibility(View.VISIBLE);
                    listAdapter = new MeetRegisterListAdapter(getActivity(), listBean,MeetRegisterFragment.this);
                    recyclerView.setAdapter(listAdapter);
                }else {
                    ll_chose_meet.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }
    /**
     * 获取论文投稿列表
     */
    private void getMeetContributeList() {
        CHYHttpClientUsage.getInstanse().doGetContributeMeetList(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                listBean.clear();
                List<MeetRegisterBean> list = new Gson().fromJson(JSONCatch.parseJsonarray("conArray", response).toString(), new TypeToken<List<MeetRegisterBean>>() {
                }.getType());
                listBean.addAll(list);
                if (listBean != null && listBean.size() > 0) {
                    ll_tips.setVisibility(View.GONE);
                    ll_chose_meet.setVisibility(View.VISIBLE);
                    listAdapter = new MeetRegisterListAdapter(getActivity(), listBean,MeetRegisterFragment.this);
                    recyclerView.setAdapter(listAdapter);
                }else {
                    ll_chose_meet.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }
    /**
     * 获取酒店预订列表
     */
    private void getMeetHotelList() {
        CHYHttpClientUsage.getInstanse().doGetHotelMeetList(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                listBean.clear();
                List<MeetRegisterBean> list = new Gson().fromJson(JSONCatch.parseJsonarray("conArray", response).toString(), new TypeToken<List<MeetRegisterBean>>() {
                }.getType());
                listBean.addAll(list);
                if (listBean != null && listBean.size() > 0) {
                    ll_tips.setVisibility(View.GONE);
                    ll_chose_meet.setVisibility(View.VISIBLE);
                    listAdapter = new MeetRegisterListAdapter(getActivity(), listBean,MeetRegisterFragment.this);
                    recyclerView.setAdapter(listAdapter);
                }else {
                    ll_chose_meet.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onItemOnclick(int position) {
        currentPositon = position;
        bt_next.setEnabled(true);
        bt_next.setClickable(true);
        bt_next.getBackground().setAlpha(255);
        for (int i = 0; i < listBean.size(); i++) {
            listBean.get(i).setChecked(false);
        }
        listBean.get(position).setChecked(true);
        listAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.bt_next)
    void onClick(){
        if(listBean.size()<=0){
            ToastUtils.showToast("暂无数据");
            return;
        }
        MeetRegisterBean bean = listBean.get(currentPositon);
        switch (mType){
            case 1:
                CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.meet_courseware), Constants.MEET_ORDER_URI+"@userId="+ AppApplication.userId+"@userType="+AppApplication.userType+"@fromWhere="+bean.getFromWhere()+"@conId="+bean.getConId());
                break;
            case 2:
                CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.paper_contribute), Constants.CONTRIBUTE_URI+"@userId="+ AppApplication.userId+"@userType="+AppApplication.userType+"@fromWhere="+bean.getFromWhere()+"@conId="+bean.getConId());
                break;
            case 3:
                CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.hotel_reservation), Constants.HOTEL_ORDER_URI+"@userId="+ AppApplication.userId+"@userType="+AppApplication.userType+"@fromWhere="+bean.getFromWhere()+"@conId="+bean.getConId());
                break;
        }
    }
}
