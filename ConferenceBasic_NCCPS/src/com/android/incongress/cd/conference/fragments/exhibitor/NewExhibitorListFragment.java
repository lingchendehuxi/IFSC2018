package com.android.incongress.cd.conference.fragments.exhibitor;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.adapters.NewExhibitorListAdapter;
import com.android.incongress.cd.conference.adapters.NewHotListAdapter;
import com.android.incongress.cd.conference.adapters.NewSatelliteListAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ExhibitorListInfoBean;
import com.android.incongress.cd.conference.beans.HotListBean;
import com.android.incongress.cd.conference.beans.SatelliteInfoListBean;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
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
 * 参展商的子界面
 */
public class NewExhibitorListFragment extends BaseFragment implements XRecyclerView.LoadingListener, NewExhibitorListAdapter.OnRecyclerViewItemClickListener, NewHotListAdapter.OnRecyclerViewItemClickListener,NewSatelliteListAdapter.OnRecyclerViewItemClickListener {
    //展商列表
    private List<ExhibitorListInfoBean.ResultBean> mExhibitorBeans;
    private NewExhibitorListAdapter mAdapter;
    //hot列表
    private List<HotListBean.ResultBean> mHotBeans;
    private NewHotListAdapter mHotAdapter;
    //卫星会列表
    private List<SatelliteInfoListBean.ResultBean> mSatelliteBeans;
    private NewSatelliteListAdapter mSatelliteAdapter;

    protected XRecyclerView mRecyclerView;
    private LinearLayout ll_tips;
    private static final String BUNDLE_TIME = "order_title";
    private static final String BUNDLE_TYPE = "order_type";
    private TextView tv_tips;
    private String mTitle;
    private int type;
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private static String EXHIBITORINFO = "exhibitor_info";
    private int pageIndex;
    private String lastIndex;
    private int topNumber;

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
        mDiskLruCacheUtil = new DiskLruCacheUtil(getActivity(), EXHIBITORINFO + type);
        if (type == 3) {
            mExhibitorBeans = new ArrayList<>();
            mAdapter = new NewExhibitorListAdapter(getActivity(), mExhibitorBeans);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 18), getResources().getColor(R.color.white)) {
            });
        } else if (type == 1) {
            mHotBeans = new ArrayList<>();
            mHotAdapter = new NewHotListAdapter(getActivity(), mHotBeans);
            mRecyclerView.setAdapter(mHotAdapter);
            mHotAdapter.setOnItemClickListener(this);
            mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 18), getResources().getColor(R.color.white)) {
            });
            mRecyclerView.setLoadingMoreEnabled(false);
        } else if (type == 2) {
            mHotBeans = new ArrayList<>();
            mHotAdapter = new NewHotListAdapter(getActivity(), mHotBeans);
            mRecyclerView.setAdapter(mHotAdapter);
            mHotAdapter.setOnItemClickListener(this);
            mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 18), getResources().getColor(R.color.white)) {
            });
        }else if(type == 4){
            mSatelliteBeans = new ArrayList<>();
            mSatelliteAdapter = new NewSatelliteListAdapter(getActivity(),mSatelliteBeans);
            mRecyclerView.setAdapter(mSatelliteAdapter);
            mSatelliteAdapter.setOnItemClickListener(this);
            mRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, DensityUtil.dip2px(getActivity(), 18), getResources().getColor(R.color.white)) {
            });
            mRecyclerView.setLoadingMoreEnabled(false);
        }
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setRefreshing(true);
        return view;
    }

    @Override
    public void onLoadMore() {
        if (type == 3) {
            getExhibitorList(false);
        } else if (type == 2) {
            getAcitvityList(String.valueOf(mHotBeans.get(mHotBeans.size() - 1).getId()), false);
        }

    }

    @Override
    public void onRefresh() {
        //刷新
        if (type == 3) {
            //getExhibitorList("-1", 1);
            getExhibitorList(true);
        } else if (type == 1) {
            getHotList("-1");
        } else if (type == 2) {
            pageIndex = 0;
            getAcitvityList("-1", true);
        } else if(type == 4){
            getSatelliteArray("-1");
            /*ll_tips.setVisibility(View.VISIBLE);
            mRecyclerView.refreshComplete();*/
        }
    }


    //获取Hot信息
    private void getHotList(String lastIndex) {
        CHYHttpClientUsage.getInstanse().doGetExhibitorByListInfo("50",type, lastIndex, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mDiskLruCacheUtil.put(EXHIBITORINFO + type, response.toString());
                mHotBeans.clear();
                HotListBean bean = new Gson().fromJson(response.toString(), new TypeToken<HotListBean>() {
                }.getType());
                if (bean.getResult() != null && bean.getResult().size() > 0) {
                    ll_tips.setVisibility(View.GONE);
                    mHotBeans.addAll(bean.getResult());
                    mRecyclerView.refreshComplete();
                } else {
                    ll_tips.setVisibility(View.VISIBLE);
                    mRecyclerView.refreshComplete();
                }
                mHotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    //获取活动信息
    private void getAcitvityList(String lastId, final boolean isRefresh) {
        CHYHttpClientUsage.getInstanse().doGetExhibitorByListInfo(Constants.MAXDATA,type, lastId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mDiskLruCacheUtil.put(EXHIBITORINFO + type, response.toString());
                if (isRefresh) {
                    mHotBeans.clear();
                }
                HotListBean bean = new Gson().fromJson(response.toString(), new TypeToken<HotListBean>() {
                }.getType());
                if (isRefresh) {
                    if (bean.getResult().size() == 0) {
                        ll_tips.setVisibility(View.VISIBLE);
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else {
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                    mRecyclerView.refreshComplete();
                } else {
                    ll_tips.setVisibility(View.GONE);
                    if (bean.getResult().size() == 0) {
                        ToastUtils.showToast(getString(R.string.no_more_date));
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else {
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                    mRecyclerView.loadMoreComplete();
                }
                mHotBeans.addAll(bean.getResult());
                mHotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    //获取展商信息
    private void getExhibitorList(final boolean isRefresh) {
        if(isRefresh){
            lastIndex = "-1";
            pageIndex = 0;
        }else {
            pageIndex+=10;
            lastIndex = String.valueOf(pageIndex);
        }
        CHYHttpClientUsage.getInstanse().doGetExhibitorByListInfo(Constants.MAXDATA,type, lastIndex, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mDiskLruCacheUtil.put(EXHIBITORINFO + type, response.toString());
                if (isRefresh) {
                    mExhibitorBeans.clear();
                }
                ExhibitorListInfoBean bean = new Gson().fromJson(response.toString(), new TypeToken<ExhibitorListInfoBean>() {
                }.getType());
                if (isRefresh) {
                    if (bean.getResult().size() == 0&&bean.getTopArray().size() == 0) {
                        ll_tips.setVisibility(View.VISIBLE);
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else {
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                    mRecyclerView.refreshComplete();
                    for(int i =0;i<bean.getTopArray().size();i++){
                        ExhibitorListInfoBean.ResultBean resultBean = bean.getTopArray().get(i);
                        resultBean.setType(0);
                        mExhibitorBeans.add(resultBean);
                    }
                } else {
                    ll_tips.setVisibility(View.GONE);
                    if (bean.getResult().size() == 0) {
                        ToastUtils.showToast(getString(R.string.no_more_date));
                        mRecyclerView.setLoadingMoreEnabled(false);
                    } else {
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                    mRecyclerView.loadMoreComplete();
                }
                mExhibitorBeans.addAll(bean.getResult());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }
    //获取卫星会信息
    private void getSatelliteArray(String lastIndex){
        CHYHttpClientUsage.getInstanse().doGetExhibitorByListInfo("50",type, lastIndex, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mDiskLruCacheUtil.put(EXHIBITORINFO + type, response.toString());
                mSatelliteBeans.clear();
                SatelliteInfoListBean bean = new Gson().fromJson(response.toString(), new TypeToken<SatelliteInfoListBean>() {
                }.getType());
                if (bean.getResult() != null && bean.getResult().size() > 0) {
                    ll_tips.setVisibility(View.GONE);
                    mSatelliteBeans.addAll(bean.getResult());
                    mRecyclerView.refreshComplete();
                } else {
                    ll_tips.setVisibility(View.VISIBLE);
                    mRecyclerView.refreshComplete();
                }
                mSatelliteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (type == 3) {
            action(NewExhibitorDetailActionFragment.getInstanceFragment(mExhibitorBeans.get(position)), getString(R.string.exhibitor_detail_title), false, false, false);
        }else if(type == 1){
            HotListBean.ResultBean resultBean = mHotBeans.get(position);
            if(resultBean.getType() == 2){
                CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(resultBean.getTitle()), resultBean.getUrl(),1);
            }else {
                if(resultBean.getIslive() == 1){
                    CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(resultBean.getTitle()), resultBean.getLiveUrl(),1);
                }else {
                    CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(resultBean.getTitle()), Constants.NO_LIVE_URL+resultBean.getId(),1);
                }
            }
        }else if(type == 2){
            HotListBean.ResultBean resultBean = mHotBeans.get(position);
            CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(resultBean.getTitle()), resultBean.getUrl(),1);
        }else if(type == 4){
            SatelliteInfoListBean.ResultBean resultBean = mSatelliteBeans.get(position);
            if(resultBean.getIslive() == 1){
                CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(resultBean.getTitle()), resultBean.getLiveUrl(),1);
            }else {
                CollegeActivity.startCitCollegeActivity(getActivity(), StringUtils.getNeedString(resultBean.getTitle()), Constants.NO_LIVE_URL+resultBean.getId(),1);
            }
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
