package com.android.incongress.cd.conference.fragments.college;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.incongress.cd.conference.adapters.CollegeListDetailActionAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.CollegeListDetailBean;
import com.android.incongress.cd.conference.utils.CacheManager;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.utils.cache.DiskLruCacheUtil;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.dialog.SingleButtonDialog;
import com.android.incongress.cd.conference.widget.refresh_view.XRefreshView;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Jacky on 2016/2/1.
 */
public class CollegeListDetailActionFragment extends BaseFragment implements CollegeListDetailActionAdapter.CollegeOnItemOnclicking {
    private StickyListHeadersListView mStickLVSpeaker;
    private CollegeListDetailActionAdapter mScheduleListAdapter;
    private String mCurrentCollegeDay = "", stringModelId;
    private int mCurrentCollegeType;
    private XRefreshView refreshView;

    private static final String BUNDLE_COLLEGE_DAY = "collegeDay";
    private static final String BUNDLE_COLLEGE_TYPE = "collegeType";
    private static final String BUNDLE_COLLEGE_MODEL_ID = "collegeModelID";
    //哪一天的标识
    private static final String BUNDLE_CACHE_ID = "collegeCacheID";
    private static final int BOOKTYPE = 100;
    private static final int VIDEOTYPE = 101;

    private ProgressBar mPbLoading;
    private LinearLayout ll_tips;
    private String cache_dir;
    private List<CollegeListDetailBean.ClassArrayBean.SessionArrayBean> listBean = new ArrayList<>();
    //缓存
    private CacheManager cacheManager, cacheManager2;
    private static final String CACHE_COLLEGE_DATA_DETAIL = "college_data_detail_list";
    private static final String CACHE_COLLEGE_BOOK_DETAIL = "college_book_detail_list";
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private Context context;

    public static CollegeListDetailActionFragment getInstance(String meetingDay, int mDataType, String stringModelId, String cache_dir) {
        CollegeListDetailActionFragment fragment = new CollegeListDetailActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_COLLEGE_DAY, meetingDay);
        bundle.putInt(BUNDLE_COLLEGE_TYPE, mDataType);
        bundle.putString(BUNDLE_COLLEGE_MODEL_ID, stringModelId);
        bundle.putString(BUNDLE_CACHE_ID, cache_dir + "_" + stringModelId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCurrentCollegeDay = getArguments().getString(BUNDLE_COLLEGE_DAY);
            mCurrentCollegeType = getArguments().getInt(BUNDLE_COLLEGE_TYPE);
            cache_dir = getArguments().getString(BUNDLE_CACHE_ID);
            stringModelId = getArguments().getString(BUNDLE_COLLEGE_MODEL_ID);
            cacheManager = CacheManager.getInstance().open(CACHE_COLLEGE_DATA_DETAIL + cache_dir, 1);
            mDiskLruCacheUtil = new DiskLruCacheUtil(context, CACHE_COLLEGE_BOOK_DETAIL + cache_dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_college_list, null);
        context = getContext();
        mStickLVSpeaker = view.findViewById(R.id.slhlv_sessions);
        mPbLoading = view.findViewById(R.id.pb_loading);
        refreshView = view.findViewById(R.id.custom_view);
        ll_tips = view.findViewById(R.id.ll_tips);
        // 设置是否可以下拉刷新
        refreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(false);
        //当下拉刷新被禁用时，调用这个方法并传入false可以不让头部被下拉
        refreshView.setMoveHeadWhenDisablePullRefresh(true);
        // 设置时候可以自动刷新
        refreshView.setAutoRefresh(false);
        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                if (!NetWorkUtils.isNetworkConnected(context)) {
                    refreshView.stopRefresh();
                    ToastUtils.showToast(getString(R.string.connect_network));
                    return;
                }
                if (mCurrentCollegeType == VIDEOTYPE) {
                    getCollegeDayList();
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
            }
        });
        loadLocalDate();

        return view;
    }

    //无网络的时候加载本地数据
    private void loadLocalDate() {
        if (!NetWorkUtils.isNetworkConnected(context)) {
            ll_tips.setVisibility(View.GONE);
            String stringJson = "";
            if (mCurrentCollegeType == VIDEOTYPE) {
                stringJson = cacheManager.getString(CACHE_COLLEGE_DATA_DETAIL + cache_dir);
            }
            if (!TextUtils.isEmpty(stringJson)) {
                listBean.clear();
                CollegeListDetailBean bean = new Gson().fromJson(stringJson, new TypeToken<CollegeListDetailBean>() {
                }.getType());
                for (int i = 0; i < bean.getClassArray().size(); i++) {
                    listBean.addAll(bean.getClassArray().get(i).getSessionArray());
                }
                if (listBean.size() == 0) {
                    ToastUtils.showToast(getString(R.string.connect_network));
                    mPbLoading.setVisibility(View.GONE);
                    return;
                }
                if (mCurrentCollegeType == VIDEOTYPE) {
                    mScheduleListAdapter = new CollegeListDetailActionAdapter(context, listBean, bean.getClassArray(), CollegeListDetailActionFragment.this, VIDEOTYPE);
                }
                mStickLVSpeaker.setAdapter(mScheduleListAdapter);
            }
            mPbLoading.setVisibility(View.GONE);
            ToastUtils.showToast(getString(R.string.connect_network));
        } else {
            if (mCurrentCollegeType == VIDEOTYPE) {
                getCollegeDayList();
            }
        }
    }

    private void getCollegeDayList() {
        CHYHttpClientUsage.getInstanse().doGetCollegeListDetail(mCurrentCollegeDay, stringModelId, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                cacheManager.saveString(CACHE_COLLEGE_DATA_DETAIL + cache_dir, response.toString());
                listBean.clear();
                CollegeListDetailBean bean = new Gson().fromJson(response.toString(), new TypeToken<CollegeListDetailBean>() {
                }.getType());
                if ("1".equals(bean.getState()) && bean.getClassArray() != null) {
                    for (int i = 0; i < bean.getClassArray().size(); i++) {
                        listBean.addAll(bean.getClassArray().get(i).getSessionArray());
                    }
                    if (listBean.size() > 0) {
                        ll_tips.setVisibility(View.GONE);
                        mScheduleListAdapter = new CollegeListDetailActionAdapter(context, listBean, bean.getClassArray(), CollegeListDetailActionFragment.this, VIDEOTYPE);
                        mStickLVSpeaker.setAdapter(mScheduleListAdapter);
                    } else {
                        ll_tips.setVisibility(View.VISIBLE);
                    }
                    mPbLoading.setVisibility(View.GONE);
                    refreshView.stopRefresh(true);
                } else {
                    mPbLoading.setVisibility(View.GONE);
                    refreshView.stopRefresh(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                refreshView.stopRefresh(false);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }
        });
    }

    @Override
    public void onItemOnclick(String sessionId, int mType,int limit,String limitTime) {
        if(limit == 0){
            final SingleButtonDialog dialog =new SingleButtonDialog(getActivity(),R.style.MyDialog);
            dialog.setYesOnclickListener("确定", new SingleButtonDialog.onYesOnclickListener() {
                @Override
                public void onYesOnclick() {
                    dialog.dismiss();
                }
            });
            if(limitTime!=null){
                dialog.setMessage(limitTime);
            }
            dialog.setCancelable(true);
            dialog.show();
        }else {
            Intent intent = new Intent(context, CollegeCourseBookActivity.class);
            intent.putExtra("book_session_id", sessionId);
            intent.putExtra("book_type", mType);
            startActivity(intent);
        }
    }
}
