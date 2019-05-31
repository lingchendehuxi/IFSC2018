package com.android.incongress.cd.conference.fragments;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.CollegeActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.PolyvVideoPlayDetailActivity;
import com.android.incongress.cd.conference.VideoPlayDetailActivity;
import com.android.incongress.cd.conference.adapters.EsmoResourceAdapter;
import com.android.incongress.cd.conference.adapters.ResourceAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.BookCoursePlayBean;
import com.android.incongress.cd.conference.beans.EsmosBean;
import com.android.incongress.cd.conference.beans.ResourceBeans;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.NetWorkUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.refresh_view.XRefreshView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2017/4/13.
 * 资源
 */

public class ResourceFragment extends BaseFragment implements View.OnClickListener {
    private String mCurrentChooseConference, msgString;
    private List<EsmosBean> mEsmoBeans;
    private EsmoResourceAdapter mAdapter;
    private ResourceAdapter mNewsAdapter, mHotAdapter, mEsmoAdapter, mKJAdapter;
    private RecyclerView mKJRecyclerView, mNewestRecyclerView, mHottestRecyclerView, mRecyclerView;
    private ResourceBeans mResourceBean;
    private List<BookCoursePlayBean.VideoArrayBean> mHotBeans = new ArrayList<>();
    private List<ResourceBeans.ResourcesBottomArrayBean> mResourceList = new ArrayList<>();
    private List<ResourceBeans.KjArrayBean> mKjList = new ArrayList<>();
    private LinearLayout mBatch, mRetry, mNotice1, mNotice2, mKJLayout,ll_resource;
    private TextView mNotice_text1, mNotice_text2;
    private LayoutInflater inflater;
    private XRefreshView refreshView;
    private static final String BUNDLE_TYPE = "bundleType";
    private int mEsmoType = 1;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 4;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;
    private static final int TAG_OPEN_CONFERENCE = 0x0001;
    List<BookCoursePlayBean.VideoArrayBean> newListBean = new ArrayList<>();


    public static Fragment newInstance(int esmoType) {
        ResourceFragment instance = new ResourceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE, esmoType);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.fragment_resource, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mNotice1 = view.findViewById(R.id.notice_layout1);
        mNotice2 = view.findViewById(R.id.notice_layout2);
        mKJLayout = view.findViewById(R.id.kj_layout);
        mNotice_text1 = view.findViewById(R.id.notice_text1);
        mNotice_text2 = view.findViewById(R.id.notice_text2);
        mRetry = view.findViewById(R.id.resource_cs);
        mKJRecyclerView = view.findViewById(R.id.kj_recycler);
        mNewestRecyclerView = view.findViewById(R.id.home_recycler);
        mHottestRecyclerView = view.findViewById(R.id.home_recycler1);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mKJRecyclerView.setNestedScrollingEnabled(false);
        mNewestRecyclerView.setNestedScrollingEnabled(false);
        mHottestRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        refreshView = view.findViewById(R.id.custom_view);
        mBatch = view.findViewById(R.id.huan);
        ll_resource = view.findViewById(R.id.ll_resource);
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
                if (!NetWorkUtils.isNetworkConnected(getActivity())) {
                    refreshView.stopRefresh();
                    ToastUtils.showToast(getString(R.string.connect_network));
                    return;
                }
                initGetData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
            }
        });
        initGetData();
        initNotice();
        mBatch.setOnClickListener(this);
        mRetry.setOnClickListener(this);
        mNewsAdapter = new ResourceAdapter(newListBean, getActivity(), 2, getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, 1, "");
        mHotAdapter = new ResourceAdapter(mHotBeans, getActivity(), 2, getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, 2);
        mEsmoAdapter = new ResourceAdapter(mResourceList, getActivity(), 3);
        mKJAdapter = new ResourceAdapter(mKjList, getActivity(), 2, getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2, 3, false);
        mKJRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mKJRecyclerView.setAdapter(mKJAdapter);
        mNewestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewestRecyclerView.setAdapter(mNewsAdapter);
        mHottestRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mHottestRecyclerView.setAdapter(mHotAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mEsmoAdapter);
    }

    private void initNotice() {
        CHYHttpClientUsage.getInstanse().doGetCheckUpdateData(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("state") == 1) {
                        mNotice1.setVisibility(View.VISIBLE);
                        msgString = response.getString("msg");
                        mNotice_text1.setText(msgString);
                        float curTranslationY = mNotice1.getTranslationY();
                        ObjectAnimator animator = ObjectAnimator.ofFloat(mNotice1, "translationY", curTranslationY, mNotice1.getMeasuredHeight(), curTranslationY);
                        animator.setDuration(3000);
                        animator.start();
                        handler.sendEmptyMessageDelayed(2, 1500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initBatch(String dataIds) {
        CHYHttpClientUsage.getInstanse().doGetRefreshData(dataIds, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(getActivity(), null, "正在加载...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), "加载失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                List<BookCoursePlayBean.VideoArrayBean> hotBean = new Gson().fromJson(response.toString(), new TypeToken<List<BookCoursePlayBean.VideoArrayBean>>() {
                }.getType());
                mHotBeans.addAll(hotBean);
                mHotAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initGetData() {
        CHYHttpClientUsage.getInstanse().doGetResourceListNew(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                refreshView.stopRefresh(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                refreshView.stopRefresh(true);
                if (response != null) {
                    List<BookCoursePlayBean.VideoArrayBean> listBean = new Gson().fromJson(JSONCatch.parseJsonarray("dataNewArr", response).toString(), new TypeToken<List<BookCoursePlayBean.VideoArrayBean>>() {
                    }.getType());
                    List<BookCoursePlayBean.VideoArrayBean> hotBean = new Gson().fromJson(JSONCatch.parseJsonarray("dataHotArr", response).toString(), new TypeToken<List<BookCoursePlayBean.VideoArrayBean>>() {
                    }.getType());
                    newListBean.clear();
                    mHotBeans.clear();
                    newListBean.addAll(listBean);
                    mHotBeans.addAll(hotBean);
                    Gson gson = new Gson();
                    mResourceBean = gson.fromJson(response.toString(), ResourceBeans.class);
                    handler.sendEmptyMessage(1);
                } else {
                    mRetry.setVisibility(View.VISIBLE);
                    //refreshView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.huan:
                String dataIds = null;
                for (int i = 0; i < mHotBeans.size(); i++) {
                    if (i == 0) {
                        dataIds = mHotBeans.get(i).getDataId() + "";
                    } else {
                        dataIds = dataIds + "," + mHotBeans.get(i).getDataId();
                    }
                }
                mHotBeans.clear();
                initBatch(dataIds);
                break;
            case R.id.resource_cs:
                initGetData();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //refreshView.setVisibility(View.VISIBLE);
                    mRetry.setVisibility(View.GONE);
                    inflater = LayoutInflater.from(getActivity());
                    mNewestRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    mHottestRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    mResourceList.clear();
                    mResourceList.addAll(mResourceBean.getResourcesBottomArray());
                    if(mResourceList.size()<=0){
                        ll_resource.setVisibility(View.GONE);
                    }else {
                        ll_resource.setVisibility(View.VISIBLE);
                    }
                    if (mResourceBean.getKjArray() != null && mResourceBean.getKjArray().size() > 0) {
                        mKJLayout.setVisibility(View.VISIBLE);
                        if (mResourceBean.getKjArray().size() == 1) {
                            mKJRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                        } else if (mResourceBean.getKjArray().size() == 3) {
                            mKJRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        } else {
                            mKJRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        }
                    } else {
                        mKJLayout.setVisibility(View.GONE);
                    }
                    mKjList = mResourceBean.getKjArray();
                    mNewsAdapter.notifyDataSetChanged();
                    mHotAdapter.notifyDataSetChanged();
                    mEsmoAdapter.notifyDataSetChanged();
                    mKJAdapter.notifyDataSetChanged();
                    mKJAdapter.SetOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ResourceBeans.KjArrayBean bean = mResourceBean.getKjArray().get(position);
                            CollegeActivity.startCitCollegeActivity(getContext(), bean.getTitle(), bean.getDataUrl());
                        }
                    });
                    mNewsAdapter.SetOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            BookCoursePlayBean.VideoArrayBean bean = newListBean.get(position);
                            if (!TextUtils.isEmpty(bean.getVideoUrl())) {
                                if (bean.getVideoType() == 3) {
                                    String[] string = bean.getTitle().split(",");
                                    if (AppApplication.systemLanguage == 1) {
                                        CollegeActivity.startCitCollegeActivity(getActivity(), string[0], bean.getVideoUrl());
                                    } else {
                                        CollegeActivity.startCitCollegeActivity(getActivity(), string[1], bean.getVideoUrl());
                                    }
                                } else if (bean.getVideoType() == 2) {
                                    Intent intent = new Intent(getActivity(), VideoPlayDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("video_play_bean", bean);
                                    intent.putExtras(bundle);
                                    getActivity().startActivity(intent);
                                } else if (bean.getVideoType() == 1) {
                                    Intent intent = new Intent(getActivity(), PolyvVideoPlayDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("video_play_bean", bean);
                                    intent.putExtras(bundle);
                                    getActivity().startActivity(intent);
                                }
                            }
                        }
                    });
                    mHotAdapter.SetOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            BookCoursePlayBean.VideoArrayBean bean = mHotBeans.get(position);
                            if (!TextUtils.isEmpty(bean.getVideoUrl())) {
                                if (bean.getVideoType() == 3) {
                                    String[] string = bean.getTitle().split(",");
                                    if (AppApplication.systemLanguage == 1) {
                                        CollegeActivity.startCitCollegeActivity(getActivity(), string[0], bean.getVideoUrl());
                                    } else {
                                        CollegeActivity.startCitCollegeActivity(getActivity(), string[1], bean.getVideoUrl());
                                    }
                                } else if (bean.getVideoType() == 2) {
                                    Intent intent = new Intent(getActivity(), VideoPlayDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("video_play_bean", bean);
                                    intent.putExtras(bundle);
                                    getActivity().startActivity(intent);
                                } else if (bean.getVideoType() == 1) {
                                    Intent intent = new Intent(getActivity(), PolyvVideoPlayDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("video_play_bean", bean);
                                    intent.putExtras(bundle);
                                    getActivity().startActivity(intent);
                                }
                            }
                        }
                    });
                    mEsmoAdapter.SetOnItemClickListener(new ResourceAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            ResourceBeans.ResourcesBottomArrayBean bean = mResourceBean.getResourcesBottomArray().get(position);
                            if(((HomeActivity) getActivity()).mDynamicHomeFragment!=null){
                                ((HomeActivity) getActivity()).mDynamicHomeFragment.checkUserInfo(bean.getResourceName());
                            }
                        }
                    });
                    //refreshView.setPullRefreshEnable(false);
                    break;
                case 2:
                    mNotice1.setVisibility(View.VISIBLE);
                    mNotice2.setVisibility(View.VISIBLE);
                    mNotice_text2.setText(msgString);
                    handler.sendEmptyMessageDelayed(3, 4000);
                    break;
                case 3:
                    mNotice2.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == TAG_OPEN_CONFERENCE) {
                HomeActivity activity = (HomeActivity) getActivity();
                //activity.mDynamicHomeFragment.setConferenceTitle(mCurrentChooseConference);
                activity.switchContent(activity.mDynamicHomeFragment);
            }
        }
    }

    public void updateConferenceData() {
        //mEsmoBeans = ConferenceDbUtils.getEsmoBeansByType(mEsmoType);

        if (mAdapter != null) {
            mAdapter.updateConferenceState(true, mEsmoBeans);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isBackView) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
        MobclickAgent.onPageStart(Constants.FRAGMENT_RESOURCE);
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
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_RESOURCE);
    }


}
