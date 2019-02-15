package com.android.incongress.cd.conference.fragments.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.PolyvVideoPlayDetailActivity;
import com.android.incongress.cd.conference.VideoPlayDetailActivity;
import com.android.incongress.cd.conference.adapters.CollegeListFragmentAdapter;
import com.android.incongress.cd.conference.adapters.CollegeViewGridViewAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeTitleListBean;
import com.android.incongress.cd.conference.beans.CollegeVideoBean;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.TimeUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.SpaceItemDecoration;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Jacky on 2016/2/1.
 */
public class CollegeVideoListFragment extends BaseFragment implements XRecyclerView.LoadingListener ,CollegeViewGridViewAdapter.VideoItemClick{
    private XRecyclerView xRecyclerView;
    private CollegeViewGridViewAdapter mVideoAdapter;
    private String mCurrentMeetingDay = "";
    private String mSearchString = "";
    private List<FastOnLineBean.VideoArrayBean> videoList;
    private static final String BUNDLE_MEETING_DAY = "meetingDay";
    private static final String BUNDLE_SEARCH_STRING = "searchString";
    private LinearLayout ll_book_course,ll_tips;
    private String mLastId;
    //是否有更多数据： 1：代表还有 0：代表已经没有了
    private String mIsMore;
    //缓存
    private CacheManager cacheManager;
    private static final String CACHE_COLLEGE_VIDEO = "college_video_list";


    public static CollegeVideoListFragment getInstance(String meetingDay,String searchString ) {
        CollegeVideoListFragment fragment = new CollegeVideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MEETING_DAY, meetingDay);
        bundle.putString(BUNDLE_SEARCH_STRING, searchString);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCurrentMeetingDay = getArguments().getString(BUNDLE_MEETING_DAY);
            mSearchString = getArguments().getString(BUNDLE_SEARCH_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        View view = inflater.inflate(R.layout.fragment_video_list, null);
        xRecyclerView = view.findViewById(R.id.no_gv_list);
        ll_book_course = view.findViewById(R.id.ll_book_course);
        ll_tips = view.findViewById(R.id.ll_tips);
        cacheManager = CacheManager.getInstance().open(CACHE_COLLEGE_VIDEO, 1);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        xRecyclerView.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.app_normal_margin);
        xRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        videoList = new ArrayList<>();
        mVideoAdapter = new CollegeViewGridViewAdapter(videoList, getActivity(),this);
        xRecyclerView.setAdapter(mVideoAdapter);
        xRecyclerView.setLoadingListener(this);
        loadLocalDate();
        initView();
        return view;
    }

    private void initView() {
        ll_book_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action(CollegeListDetailFragment.getInstance("book","",100), R.string.meeting_video_reservation, false, false, false);
                StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
            }
        });
    }
    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            String json = cacheManager.getString(CACHE_COLLEGE_VIDEO);
            if (!TextUtils.isEmpty(json)) {
                FastOnLineBean bean = new Gson().fromJson(json, new TypeToken<FastOnLineBean>() {
                }.getType());
                refreshViewState();
                videoList.addAll(bean.getVideoArray());
                mVideoAdapter.notifyDataSetChanged();
                ToastUtils.showToast(getString(R.string.connect_network));
            }
        }else {
            xRecyclerView.setRefreshing(true);
        }
    }

    private void refreshViewState() {
        ll_tips.setVisibility(View.GONE);
        ll_book_course.setVisibility(View.VISIBLE);
    }

    /**
     * 获取火速上线的列表
     * @param lastId
     */
    private void getVideoData(final String lastId) {
        CHYHttpClientUsage.getInstanse().doGetFastOnLine(mCurrentMeetingDay, lastId, "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                FastOnLineBean bean = new Gson().fromJson(response.toString(), new TypeToken<FastOnLineBean>() {
                }.getType());
                refreshViewState();
                if (bean.getVideoArray().size() != 0) {
                    //刷新进来
                    if ("-1".equals(lastId)) {
                        cacheManager.saveString(CACHE_COLLEGE_VIDEO,response.toString());
                        videoList.clear();
                        xRecyclerView.refreshComplete();
                        xRecyclerView.setLoadingMoreEnabled(true);
                        //加载更多进来
                    } else {
                        xRecyclerView.loadMoreComplete();
                        if ("0".equals(mIsMore)) {
                            ToastUtils.showShorToast(getString(R.string.incongress_send_no_more_data));
                            xRecyclerView.setLoadingMoreEnabled(false);
                            return;
                        }
                    }
                    videoList.addAll(bean.getVideoArray());
                    ll_tips.setVisibility(View.GONE);
                    mVideoAdapter.notifyDataSetChanged();
                    mLastId = String.valueOf(bean.getVideoArray().get(bean.getVideoArray().size() - 1).getDataId());
                    mIsMore = bean.getIsNextPage();
                } else {
                    if ("0".equals(bean.getIsNextPage())) {
                        xRecyclerView.loadMoreComplete();
                        xRecyclerView.refreshComplete();
                        ToastUtils.showShorToast(getString(R.string.incongress_send_no_more_data));
                        xRecyclerView.setLoadingMoreEnabled(false);
                        return;
                    }
                    ll_book_course.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.VISIBLE);
                    return;
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showShorToast("获取信息失败，请联系管理员");
            }
        });
    }

    /**
     * 获取搜索的数据
     * @param lastId
     */
    private void getSearchVideoData(final String lastId) {
        CHYHttpClientUsage.getInstanse().doGetSearchCollegeTitle(mSearchString, lastId,new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                FastOnLineBean bean = new Gson().fromJson(response.toString(), new TypeToken<FastOnLineBean>() {
                }.getType());
                refreshViewState();
                if (bean.getVideoArray().size() != 0) {
                    //刷新进来
                    if ("-1".equals(lastId)) {
                        videoList.clear();
                        xRecyclerView.refreshComplete();
                        xRecyclerView.setLoadingMoreEnabled(true);
                        //加载更多进来
                    } else {
                        xRecyclerView.loadMoreComplete();
                        if ("0".equals(mIsMore)) {
                            ToastUtils.showShorToast(getString(R.string.incongress_send_no_more_data));
                            xRecyclerView.setLoadingMoreEnabled(false);
                            return;
                        }
                    }
                    videoList.addAll(bean.getVideoArray());
                    ll_tips.setVisibility(View.GONE);
                    mVideoAdapter.notifyDataSetChanged();
                    mLastId = String.valueOf(bean.getVideoArray().get(bean.getVideoArray().size() - 1).getDataId());
                    mIsMore = bean.getIsNextPage();
                } else {
                    if ("0".equals(bean.getIsNextPage())&&videoList.size()!=0) {
                        xRecyclerView.loadMoreComplete();
                        ToastUtils.showShorToast(getString(R.string.incongress_send_no_more_data));
                        xRecyclerView.setLoadingMoreEnabled(false);
                        return;
                    }
                    xRecyclerView.refreshComplete();
                    xRecyclerView.loadMoreComplete();
                    ll_book_course.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.VISIBLE);
                    return;
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
    public void onRefresh() {
        if(!NetWorkUtils.isNetworkConnected(getActivity())){
            xRecyclerView.refreshComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        if(TextUtils.isEmpty(mSearchString)){
            getVideoData("-1");
        }else {
            getSearchVideoData("-1");
        }
    }

    @Override
    public void onLoadMore() {
        if(!NetWorkUtils.isNetworkConnected(getActivity())){
            xRecyclerView.loadMoreComplete();
            ToastUtils.showToast(getString(R.string.connect_network));
            return;
        }
        if(TextUtils.isEmpty(mSearchString)){
            getVideoData(mLastId);
        }else {
            getSearchVideoData(mLastId);
        }
    }

    @Override
    public void itemClick(int position) {
        if(videoList.get(position).getVideoType()== 3){
            String[] string = videoList.get(position).getTitle().split(",");
            if(AppApplication.systemLanguage == 1){
                CollegeActivity.startCitCollegeActivity(getActivity(), string[0], videoList.get(position).getVideoUrl());
            }else {
                CollegeActivity.startCitCollegeActivity(getActivity(), string[1], videoList.get(position).getVideoUrl());
            }
        }else if(videoList.get(position).getVideoType() == 2){
            Intent intent = new Intent(getActivity(),VideoPlayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.VIDEO_DETIAL_BEAN,videoList.get(position));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }else if(videoList.get(position).getVideoType() == 1){
            Intent intent = new Intent(getActivity(),PolyvVideoPlayDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.VIDEO_DETIAL_BEAN,videoList.get(position));
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
    }
}
