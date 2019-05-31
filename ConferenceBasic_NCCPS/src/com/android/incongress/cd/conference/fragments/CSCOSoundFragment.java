package com.android.incongress.cd.conference.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.adapters.CSCOSoundAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ScenicXiuBean;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Admin on 2017/7/26.
 */

public class CSCOSoundFragment extends BaseFragment implements XRecyclerView.LoadingListener ,CSCOSoundAdapter.OnItemClickListener{
    private String mLastDataId = "-1";
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;
    @BindView(R.id.recycler_view)
    XRecyclerView mRecyclerView;
    @BindView(R.id.ll_tips)
    LinearLayout ll_tips;
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private List<ScenicXiuBean> mScenic;
    private static String DYNAMIC_CACHE = "dynamic_cache";
    private CSCOSoundAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.fragment_csco_sound, null, false);
        ButterKnife.bind(this, view);
        initView();
        getData(true, mLastDataId);

        return view;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mDiskLruCacheUtil = new DiskLruCacheUtil(getActivity(), DYNAMIC_CACHE);
        mScenic = new ArrayList<>();
        mAdapter = new CSCOSoundAdapter(mScenic,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
        mAdapter.SetOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        getData(true,"-1");
    }

    @Override
    public void onLoadMore() {
        getData(false,String.valueOf(mScenic.get(mScenic.size()-1).getSceneShowId()));
    }

    private void getData(final boolean isRefresh, final String lastSceneShowId) {
        CHYHttpClientUsage.getInstanse().doGetSceneShowZs(lastSceneShowId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (isRefresh) {
                    mScenic.clear();
                }
                List<ScenicXiuBean> listBean = new Gson().fromJson(JSONCatch.parseJsonarray("sceneShowArray", response).toString(), new TypeToken<List<ScenicXiuBean>>() {
                }.getType());
                if (isRefresh) {
                    if (listBean.size() == 0) {
                        ll_tips.setVisibility(View.VISIBLE);
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else {
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                    mRecyclerView.refreshComplete();
                } else {
                    ll_tips.setVisibility(View.GONE);
                    if (listBean.size() == 0) {
                        ToastUtils.showToast(getString(R.string.no_more_date));
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else {
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                    mRecyclerView.loadMoreComplete();
                }
                mScenic.addAll(listBean);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isBackView) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_DYNAMICHOME_SCENICXIU);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_DYNAMICHOME_SCENICXIU);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(mScenic.get(position).getTitle()),mScenic.get(position).getHtmlUrl());
    }
}
