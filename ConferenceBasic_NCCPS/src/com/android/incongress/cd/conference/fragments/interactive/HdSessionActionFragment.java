package com.android.incongress.cd.conference.fragments.interactive;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.HdSessionAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.HdSessionBean;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.widget.AutoSwipeRefreshLayout;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jacky on 2016/1/28.
 * <p/>
 * 现场互动模块
 */
public class HdSessionActionFragment extends BaseFragment {
    private RecyclerView mRcvSession;
    private List<HdSessionBean> mHdSessionNow = new ArrayList<>();
    private List<HdSessionBean> mHdSessionWill = new ArrayList<>();
    private HdSessionAdapter mAdapter;
    private AutoSwipeRefreshLayout mAsrlSessions;

    private String mEmptyMsg;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int target = msg.what;

            if (target == 1) {
                mAdapter.notifySessionBeans();
            } else {
                ToastUtils.showShorToast(mEmptyMsg);
            }
            mAsrlSessions.setRefreshing(false);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hd_session, null);

        mRcvSession = (RecyclerView) view.findViewById(R.id.rcv_sessions);
        mAsrlSessions = (AutoSwipeRefreshLayout) view.findViewById(R.id.asrl_sessions);
        mAsrlSessions.setColorSchemeResources(R.color.theme_color);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(OrientationHelper.VERTICAL);
        mRcvSession.setLayoutManager(manager);

        mAdapter = new HdSessionAdapter(getActivity(), mHdSessionNow, mHdSessionWill);
        mRcvSession.setAdapter(mAdapter);

        mRcvSession.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .paintProvider(mAdapter)
                .visibilityProvider(mAdapter)
                .marginProvider(mAdapter)
                .build());

        mAsrlSessions.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDatas();
            }
        });
        mAsrlSessions.autoRefresh();

      showGuideInfo();
        return view;
    }

    /**
     * 显示指示页
     */
    private void showGuideInfo() {
        if (!"1".equals(SharePreferenceUtils.getAppString(Constants.GUIDE_INTERACTIVE))) {

            if (getActivity() != null) {
                getActivity().findViewById(R.id.home_guide).setVisibility(View.VISIBLE);
                ((ImageView) getActivity().findViewById(R.id.home_guide)).setImageResource(R.drawable.interactive_guide);
                (getActivity().findViewById(R.id.home_guide)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().findViewById(R.id.home_guide).setVisibility(View.GONE);
                        SharePreferenceUtils.saveAppString(Constants.GUIDE_INTERACTIVE, "1");
                    }
                });
            }
        }
    }

    private void initDatas() {
        CHYHttpClientUsage.getInstanse().doGetHdSession(AppApplication.conId + "", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Gson gson = new Gson();
                        try {
                            int state = response.getInt("state");
                            if (state == 1) {
                                mHdSessionNow.clear();
                                mHdSessionWill.clear();

                                mHdSessionNow.addAll((Collection<? extends HdSessionBean>) gson.fromJson(response.getString("hdNowArray"), new TypeToken<List<HdSessionBean>>() {
                                }.getType()));
                                mHdSessionWill.addAll((Collection<? extends HdSessionBean>) gson.fromJson(response.getString("hdWillArray"), new TypeToken<List<HdSessionBean>>() {
                                }.getType()));
                                mHandler.sendEmptyMessage(1);
                            } else {
                                mEmptyMsg = response.getString("msg");
                                mHandler.sendEmptyMessage(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        mAsrlSessions.setRefreshing(false);

                    }
                }
        );
    }

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewContainerActivity.startWebViewContainerActivity(getActivity(), getActivity().getString(Constants.get_HD_QUESTION_LIST(), AppApplication.conId, AppApplication.getSystemLanuageCode()), getString(R.string.question_list));
            }
        });
    }
}
