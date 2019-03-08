package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.NewExhibitorListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorListInfoBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.RecycleViewDivider;
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
public class NewExhibitorListFragment extends BaseFragment implements XRecyclerView.LoadingListener, NewExhibitorListAdapter.OnRecyclerViewItemClickListener {
    private List<ExhibitorListInfoBean.ArrayBean> mExhibitorBeans;
    private NewExhibitorListAdapter mAdapter;

    protected XRecyclerView mRecyclerView;
    private LinearLayout ll_tips;
    private static final String BUNDLE_TIME = "order_title";
    private static final String BUNDLE_TYPE = "order_type";
    private TextView tv_tips;
    private String mTitle;
    private int type;
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private static String EXHIBITORINFO = "exhibitor_info";

    public NewExhibitorListFragment() {
    }

    public static final NewExhibitorListFragment getInstance(String title, int type) {
        NewExhibitorListFragment fragment = new NewExhibitorListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, title);
        bundle.putInt(BUNDLE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTitle = getArguments().getString(BUNDLE_TIME);
            type = getArguments().getInt(BUNDLE_TYPE);
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
        if (type == 3) {
            mDiskLruCacheUtil = new DiskLruCacheUtil(getActivity(), EXHIBITORINFO);
            mExhibitorBeans = new ArrayList<>();
            mAdapter = new NewExhibitorListAdapter(getActivity(), mExhibitorBeans);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 18), getResources().getColor(R.color.white)) {
            });
        }
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshing(true);
        return view;
    }

    @Override
    public void onLoadMore() {
        if (type == 3) {
            if (mExhibitorBeans != null && mExhibitorBeans.size() != 0) {
                getExhibitorList(mExhibitorBeans.get(mExhibitorBeans.size() - 1).getExhibitorsId() + "", 0);
            }
        }

    }

    @Override
    public void onRefresh() {
        //刷新
        if (type == 3) {
            getExhibitorList("-1", 1);
        } else if (mTitle.equals(getString(R.string.question_meeting))) {
        } else {
            ll_tips.setVisibility(View.VISIBLE);
            mRecyclerView.refreshComplete();
        }
    }


    //获取展商信息
    private void getExhibitorList(String lastId, final int currentPage) {
        CHYHttpClientUsage.getInstanse().doGetExhibitorListInfo(lastId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (JSONCatch.parseInt("state", response) == 1) {
                    mDiskLruCacheUtil.put(EXHIBITORINFO, response.toString());
                    Gson gson = new Gson();
                    if (currentPage == 1) {
                        mExhibitorBeans.clear();
                    }
                    ExhibitorListInfoBean bean = gson.fromJson(response.toString(), new TypeToken<ExhibitorListInfoBean>() {
                    }.getType());
                    if (currentPage == 1) {
                        if (bean.getArray().size() == 0) {
                            ll_tips.setVisibility(View.VISIBLE);
                            mRecyclerView.setLoadingMoreEnabled(false);
                        } else {
                            mRecyclerView.setLoadingMoreEnabled(true);
                        }
                        mRecyclerView.refreshComplete();
                    } else {
                        ll_tips.setVisibility(View.GONE);
                        if (bean.getArray().size() == 0) {
                            ToastUtils.showToast(getString(R.string.no_more_date));
                            mRecyclerView.setLoadingMoreEnabled(false);
                        } else {
                            mRecyclerView.setLoadingMoreEnabled(true);
                        }
                        mRecyclerView.loadMoreComplete();
                    }
                    mExhibitorBeans.addAll(bean.getArray());
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (currentPage == 1) {
                        mRecyclerView.refreshComplete();
                        if (mExhibitorBeans == null || mExhibitorBeans.size() == 0) {
                            //mTvNoTips.setVisibility(View.VISIBLE);
                            ll_tips.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mRecyclerView.loadMoreComplete();
                    }
                    ToastUtils.showToast(getString(R.string.no_more_date));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (type== 3) {
            action(NewExhibitorDetailActionFragment.getInstanceFragment(mExhibitorBeans.get(position).getExhibitorsId()), mExhibitorBeans.get(position).getName(), false, false, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_EXHIBITORS);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_EXHIBITORS);
    }

}
