package com.android.incongress.cd.conference.fragments.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.PolyvVideoPlayDetailActivity;
import com.android.incongress.cd.conference.VideoPlayDetailActivity;
import com.android.incongress.cd.conference.adapters.CollegeCourseBookAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.BookDetailBean;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
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
 * Created by Jacky on 2016/2/1.
 */
public class CollegeCourseBookFragment extends BaseFragment implements CollegeCourseBookAdapter.OnItemClick, XRecyclerView.LoadingListener {
    private XRecyclerView xRecyclerView;
    private CollegeCourseBookAdapter mCourseAdapter;
    private String mCurrentSessionId;
    private int mType;
    private List<BookDetailBean.MeetingArrayBean> listBook = new ArrayList<>();
    private List<BookCoursePlayBean.VideoArrayBean> listVideo = new ArrayList<>();
    private static final String BUNDLE_BOOK_SESSIONID = "book_session_id";
    private static final String BUNDLE_BOOK_TYPE = "book_type";


    public static CollegeCourseBookFragment getInstance(String sessionId, int mType) {
        CollegeCourseBookFragment fragment = new CollegeCourseBookFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_BOOK_SESSIONID, sessionId);
        bundle.putInt(BUNDLE_BOOK_TYPE, mType);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCurrentSessionId = getArguments().getString(BUNDLE_BOOK_SESSIONID);
            mType = getArguments().getInt(BUNDLE_BOOK_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_college_book_list, null);
        xRecyclerView = view.findViewById(R.id.x_recycler);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mCourseAdapter = new CollegeCourseBookAdapter(listBook, listVideo, getActivity(), mType, CollegeCourseBookFragment.this);
        xRecyclerView.setAdapter(mCourseAdapter);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        loadLocalDate();
        return view;
    }

    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {

            listBook.clear();
            listVideo.clear();
            ToastUtils.showToast(getString(R.string.nowifi));
        } else {
            xRecyclerView.setRefreshing(true);
        }
    }

    @Override
    public void onRefresh() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            xRecyclerView.refreshComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        if (mType == 100) {
            getCollegeBookDetail();
        } else {
            getCollegePlayDetail();
        }
    }

    @Override
    public void onLoadMore() {
    }

    //获取预约的课件信息
    private void getCollegeBookDetail() {
        CHYHttpClientUsage.getInstanse().doGetCollegeBookDetail(mCurrentSessionId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                xRecyclerView.refreshComplete();
                listBook.clear();
                BookDetailBean bean = new Gson().fromJson(response.toString(), new TypeToken<BookDetailBean>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    listBook.addAll(bean.getMeetingArray());
                    mCourseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                xRecyclerView.refreshComplete();
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }

    //获取可以播放的课件信息
    private void getCollegePlayDetail() {
        CHYHttpClientUsage.getInstanse().doGetCollegeVideoDetail(mCurrentSessionId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                xRecyclerView.refreshComplete();
                listVideo.clear();
                BookCoursePlayBean bean = new Gson().fromJson(response.toString(), new TypeToken<BookCoursePlayBean>() {
                }.getType());
                if ("1".equals(bean.getState())) {
                    listVideo.addAll(bean.getVideoArray());
                    mCourseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                xRecyclerView.refreshComplete();
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }

    //预约课件接口
    private void orderCourse(String topic, final int position) {
        CHYHttpClientUsage.getInstanse().doGetOrderCourse(Constants.getConId() + "", topic, SharePreferenceUtils.getUser(Constants.USER_ID), new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if ("1".equals(JSONCatch.parseString("state", response))) {
                    listBook.get(position).setYuyue("1");
                    ToastUtils.showShorToast("预约成功");

                    mCourseAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showShorToast("预约失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }

    //type 100 预约课件 101课件播放
    @Override
    public void onItemClicking(String topic, int type, int position) {
        if (type == 100) {
            if (!AppApplication.isUserLogIn()) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                return;
            }
            orderCourse(topic, position);
        } else {
            if (listVideo.get(position).getVideoType() == 3) {
                String[] string = listVideo.get(position).getTitle().split(",");
                if (AppApplication.systemLanguage == 1) {
                    CollegeActivity.startCitCollegeActivity(getActivity(), string[0], listVideo.get(position).getVideoUrl());
                } else {
                    CollegeActivity.startCitCollegeActivity(getActivity(), string[1], listVideo.get(position).getVideoUrl());
                }
            } else if(listVideo.get(position).getVideoType() == 2){
                Intent intent = new Intent(getActivity(), VideoPlayDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video_play_bean", listVideo.get(position));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }else if(listVideo.get(position).getVideoType() == 1){
                Intent intent = new Intent(getActivity(), PolyvVideoPlayDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video_play_bean", listVideo.get(position));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
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
        //预约的
        if(mType == 100){
            MobclickAgent.onPageStart(Constants.ACTIVITY_COLLEGE_ORDER);
        }else { //播放的
            MobclickAgent.onPageStart(Constants.ACTIVITY_COLLEGE_PLAY);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mType == 100){
            MobclickAgent.onPageEnd(Constants.ACTIVITY_COLLEGE_ORDER);
        }else { //播放的
            MobclickAgent.onPageEnd(Constants.ACTIVITY_COLLEGE_PLAY);
        }
    }
}
