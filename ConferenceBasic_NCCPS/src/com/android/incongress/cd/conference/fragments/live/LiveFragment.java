package com.android.incongress.cd.conference.fragments.live;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.adapters.LiveAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.LiveClassBean;
import com.android.incongress.cd.conference.beans.LiveInfoBean;
import com.android.incongress.cd.conference.beans.LiveListInfoBean;
import com.android.incongress.cd.conference.fragments.cit_live.CitLiveActivity;
import com.android.incongress.cd.conference.fragments.cit_live.CitLiveFragment;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.WrapContentLinearLayoutManager;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.popup.ChooseBBPopupWindow;
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
 * 直播列表
 *
 * @author Administrator
 */
public class LiveFragment extends BaseFragment implements LiveAdapter.itemOnclick{

    private XRecyclerView mRecyclerView;

    private LinearLayout mLl_sort, ll_tips;
    private TextView mNetWorkError, mNoDate, tv_black_bg;
    ;

    private boolean IsNetWorkOpen = true;
    private List<LiveInfoBean> allBeans = new ArrayList<>();
    private LiveAdapter mAdapter;


    private static final int MSG_NO_DATA = 2;
    private static final int MSG_TOAST_NO_MORE_DATA = 3;
    private static final int MSG_DONE = 4;
    private int classId = -1;

    private static float ScreenHeightLPercent = 0.35f;
    private static float ScreenHeightHPercent = 0.45f;
    private float fixHeight;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;
    //缓存
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private static final String CACHE_LIVE_INFO_LIST = "live_info_list";

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

    /**
     * 提醒
     */
    private TextView tv_type;
    //此handler应用于监听数据加载
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            int result = message.what;
            if (result == MSG_DONE) {
                mAdapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            } else if (result == MSG_NO_DATA) {
                mRecyclerView.refreshComplete();
            } else if (result == MSG_TOAST_NO_MORE_DATA) {
                Toast.makeText(AppApplication.getContext(), R.string.no_more_data, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.live_fragment, null);

        mRecyclerView = view.findViewById(R.id.xr_dzbb);
        tv_type = view.findViewById(R.id.tv_type);
        mLl_sort = view.findViewById(R.id.ll_sort);
        tv_black_bg = view.findViewById(R.id.tv_black_bg);
        mNetWorkError = view.findViewById(R.id.itv_net_error);
        mNoDate = view.findViewById(R.id.no_bb_data);
        ll_tips = view.findViewById(R.id.ll_tips);
        rl_layout = view.findViewById(R.id.rl_layout);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingMoreEnabled(false);
        mDiskLruCacheUtil = new DiskLruCacheUtil(getActivity(), CACHE_LIVE_INFO_LIST);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                if (!NetWorkUtils.isNetworkConnected(getActivity())) {
                    ToastUtils.showToast(getString(R.string.nowifi));
                    mRecyclerView.refreshComplete();
                } else {
                    getLiveList(true);
                }
            }

            @Override
            public void onLoadMore() {
                //加载更多
            }
        });

        IsNetWorkOpen = NetWorkUtils.isNetworkConnected(getActivity());

        mLl_sort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取壁报类型
                if(!NetWorkUtils.isNetworkConnected(getActivity())){
                   ToastUtils.showToast(getString(R.string.nowifi));
                }else {
                    getMeetClass();
                }

            }
        });
        mAdapter = new LiveAdapter(getActivity(), allBeans,this);
        mRecyclerView.setAdapter(mAdapter);
        loadLocalDate();

        return view;
    }

    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            String stringJson = mDiskLruCacheUtil.getStringCache(CACHE_LIVE_INFO_LIST);
            if (!TextUtils.isEmpty(stringJson)) {
                allBeans.clear();
                LiveListInfoBean bean = new Gson().fromJson(stringJson, new TypeToken<LiveListInfoBean>() {
                }.getType());
                if (bean.getSessionArray().size() > 0) {
                    allBeans.addAll(bean.getSessionArray());
                    mNoDate.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessage(MSG_DONE);
                }
                ToastUtils.showToast(getString(R.string.nowifi));
            }
        } else {
            mRecyclerView.setRefreshing(true);
        }
    }

    /**
     * 根据地址，获取直播列表数据
     */
    private void getLiveList(final boolean isFresh) {
        CHYHttpClientUsage.getInstanse().doGetClassForMeetLiveAddress(classId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (classId == -1) {
                    mDiskLruCacheUtil.put(CACHE_LIVE_INFO_LIST, response.toString());
                }
                LiveListInfoBean bean = new Gson().fromJson(response.toString(), new TypeToken<LiveListInfoBean>() {
                }.getType());
                if (isFresh) {
                    allBeans.clear();
                }
                if (bean.getSessionArray().size() > 0) {
                    allBeans.addAll(bean.getSessionArray());
                    mNoDate.setVisibility(View.GONE);
                    ll_tips.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessage(MSG_DONE);
                } else if (bean.getSessionArray().size() == 0) {
                    if (allBeans.size() == 0) {
                        mRecyclerView.setVisibility(View.GONE);
                        mHandler.sendEmptyMessage(MSG_NO_DATA);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mNoDate.setVisibility(View.GONE);
                        ll_tips.setVisibility(View.GONE);
                        mHandler.sendEmptyMessage(MSG_TOAST_NO_MORE_DATA);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.println("statusCode:" + statusCode + ",responseString:" + responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mRecyclerView != null) {
                    mRecyclerView.refreshComplete();
                    mRecyclerView.loadMoreComplete();
                }
            }
        });
    }

    /**
     * 获取会议地址
     */
    private void getMeetClass() {
        CHYHttpClientUsage.getInstanse().doGetClassForMeetLive(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                newClassList.clear();
                LiveClassBean.ClassArrayBean firstBean = new LiveClassBean.ClassArrayBean();
                firstBean.setClassId(-1);
                firstBean.setClassName("所有会议室#@#all");
                newClassList.add(firstBean);
                LiveClassBean bean = new Gson().fromJson(response.toString(), new TypeToken<LiveClassBean>() {
                }.getType());
                newClassList.addAll(bean.getClassArray());
                if (newClassList.size() == 0) {
                    ToastUtils.showToast("没有数据");
                    return;
                }
                initPopupWindow();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }

    private ChooseBBPopupWindow popupWindow;
    private ListAdapter listAdapter;
    private RelativeLayout rl_layout;
    private ArrayList<LiveClassBean.ClassArrayBean> newClassList = new ArrayList<>();

    //创建popupwindow
    private void initPopupWindow() {
        popupWindow = new ChooseBBPopupWindow(getActivity());
        final ListView listView = popupWindow.getmListView();
        listView.setVerticalScrollBarEnabled(false);
        listAdapter = new ListAdapter(getActivity(), newClassList);
        listView.setAdapter(listAdapter);
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }
        if (DensityUtil.getScreenSize(getActivity())[1] <= 1920) {
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightLPercent;
        } else {
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightHPercent;
        }
        if (totalHeight > fixHeight) {
            totalHeight = (int) fixHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_black_bg.setVisibility(View.GONE);
                lightOn(rl_layout);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                classId = newClassList.get(i).getClassId();
                listAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
                StringUtils.setTextShow(tv_type, newClassList.get(i).getClassName());
                getLiveList(true);
            }
        });
        tv_black_bg.setVisibility(View.VISIBLE);
        lightOff(tv_black_bg);
        //popupWindow.showAsDropDown(view);
        popupWindow.showAsDropDown(mLl_sort, -(int) (DensityUtil.getScreenSize(getActivity())[0] * 0.12), DensityUtil.dip2px(getActivity(), 1f));
    }

    class ListAdapter extends BaseAdapter {
        ArrayList<LiveClassBean.ClassArrayBean> listBeans;
        public Context context;
        public LayoutInflater layoutInflater;

        public ListAdapter(Context context, ArrayList<LiveClassBean.ClassArrayBean> listBeans) {
            this.context = context;
            this.listBeans = listBeans;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return listBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LiveFragment.ListAdapter.MyTimeHold myHold;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.my_centertextview, null);
                myHold = new LiveFragment.ListAdapter.MyTimeHold();
                myHold.tv_time = convertView.findViewById(R.id.tv_time);
                convertView.setTag(myHold);
            } else {
                myHold = (LiveFragment.ListAdapter.MyTimeHold) convertView.getTag();
            }
            StringUtils.setTextShow(myHold.tv_time, listBeans.get(position).getClassName());
            return convertView;
        }

        class MyTimeHold {
            TextView tv_time;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position) {
        if(DateUtil.isStart(allBeans.get(position).getStartTime())){
            //getActivity().startActivity(new Intent(getActivity(), CitLiveFragment.class));
            //CitLiveActivity.getInstance(getActivity(),allBeans.get(position).getLiveUrl());
            CollegeActivity.startCitCollegeActivity(getActivity(), getString(R.string.live), allBeans.get(position).getLiveUrl());
        }else {
            action(LiveListInfoFragment.getInstance(allBeans.get(position).getClassId()), R.string.live, false, false, false);
        }
    }
}
