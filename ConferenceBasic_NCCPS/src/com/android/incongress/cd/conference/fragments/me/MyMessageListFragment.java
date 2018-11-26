package com.android.incongress.cd.conference.fragments.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.MessageStationAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.fragments.DynamicHomeFragment;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 * 消息站中的子fragment
 */
public class MyMessageListFragment extends BaseFragment {
    private List<MessageBean> mMessageBeans = new ArrayList<>();
    private MessageStationAdapter mAdapter;

    protected XRecyclerView mRecyclerView;
    private TextView mTvNoTips;
    private int mCurrentPage = 1;
    private static final String BUNDLE_TIME = "title";

    private String mTitle;


    public MyMessageListFragment() {
    }

    public static final MyMessageListFragment getInstance(String title) {
        MyMessageListFragment fragment = new MyMessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTitle = getArguments().getString(BUNDLE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_item, null);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        mTvNoTips = (TextView) view.findViewById(R.id.tv_tips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mAdapter = new MessageStationAdapter(getActivity(), mMessageBeans);
        mRecyclerView.setAdapter(mAdapter);
        if(mTitle.equals(getString(R.string.system_notify))){
            getDatas(mCurrentPage = 1);
        }else if(mTitle.equals(getString(R.string.person_notify))){
            getUserDatas();
        }
        mAdapter.setOnItemClickListener(new MessageStationAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, MessageBean bean) {
                if (bean != null && bean.getType() == 2) {
                    String url = bean.getUrl();
                    if(url.contains("https:")) {
                        url = url.replaceFirst("s","");
                    }
                    WebViewContainerActivity.startWebViewContainerActivity(1,getActivity(),url,  bean.getContent());
                }
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                if(mTitle.equals(getString(R.string.system_notify))){
                    getDatas(mCurrentPage = 1);
                }else if(mTitle.equals(getString(R.string.person_notify))){
                    getUserDatas();
                }
            }
            @Override
            public void onLoadMore() {
                //加载更多
                getDatas(++mCurrentPage);
            }
        });

        mRecyclerView.setRefreshing(true);
        return view;
    }
    private void getDatas(final int currentPage) {
        CHYHttpClientUsage.getInstanse().doGetTokenList(Constants.conId + "", Constants.PAGE_SIZE, currentPage + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    LogUtils.println("response:" + response.toString());
                    int state = response.getInt("state");
                    if (state == 1) {
                        Gson gson = new Gson();
                        if (currentPage == 1) {
                            mMessageBeans.clear();
                        }
                        List<MessageBean> tempBeans = gson.fromJson(response.getString("tokenList"), new TypeToken<List<MessageBean>>() {
                        }.getType());
                        mMessageBeans.addAll(tempBeans);
                        mAdapter.notifyDataSetChanged();
                        if (currentPage == 1) {
                            mRecyclerView.refreshComplete();
                        } else {
                            mRecyclerView.loadMoreComplete();
                        }
                        mTvNoTips.setVisibility(View.GONE);
                    } else {
                        if (currentPage == 1) {
                            mRecyclerView.refreshComplete();
                            if (mMessageBeans == null || mMessageBeans.size() == 0) {
                                //mTvNoTips.setVisibility(View.VISIBLE);
                                mTvNoTips.setText(R.string.system_nomessage);
                            }
                        } else {
                            mRecyclerView.loadMoreComplete();
                        }
                        ToastUtils.showShorToast(getString(R.string.no_more_date));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getUserDatas() {
        CHYHttpClientUsage.getInstanse().doGetUserMessage(AppApplication.userId + "", AppApplication.userType + "",new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray commentArray = response.getJSONArray("commentArray");
                    JSONArray questionArray = response.getJSONArray("questionArray");
                    JSONArray answerArray = response.getJSONArray("answerArray");
                    mMessageBeans.clear();
                    if(commentArray.length()>0 || questionArray.length()>0 || answerArray.length()>0){
                        for (int i = 0;i<commentArray.length();i++){
                            MessageBean bean = new MessageBean();
                            JSONObject a = commentArray.getJSONObject(i);
                            bean.setCreateTime(a.getString("commentCreateTime"));
                            bean.setType(1);
                            bean.setContent(a.getString("commentName")+"在您的帖子评论：“"+a.getString("commentContent")+"” - 请前往“播客”查看");
                            mMessageBeans.add(bean);
                        }
                        for (int i = 0;i<questionArray.length();i++){
                            MessageBean bean = new MessageBean();
                            JSONObject a = questionArray.getJSONObject(i);
                            bean.setCreateTime(a.getString("questionCreateTime"));
                            bean.setType(1);
                            bean.setContent(a.getString("questionUserName")+"向您提问：“"+a.getString("questionContent")+"” - 请前往“专家秘书”查看");
                            mMessageBeans.add(bean);
                        }
                        for (int i = 0;i<answerArray.length();i++){
                            MessageBean bean = new MessageBean();
                            JSONObject a = answerArray.getJSONObject(i);
                            bean.setCreateTime(a.getString("answerTimeDate"));
                            bean.setType(1);
                            bean.setContent(a.getString("answerName")+"回答了您的提问：“"+a.getString("answerContent")+"” - 请前往“日程提问”查看");
                            mMessageBeans.add(bean);
                        }
                    }else{
                        //mTvNoTips.setVisibility(View.VISIBLE);
                        mTvNoTips.setText(R.string.personal_nomessage);
                    }
                    mRecyclerView.refreshComplete();
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void updateLookTime() {
        CHYHttpClientUsage.getInstanse().doCreateUserLooked(AppApplication.userId + "", AppApplication.userType + "", AppApplication.TOKEN_IMEI, Constants.LookTimeMsgStation, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                sendMessageStationBroadcast();
            }
        });
    }
    /**
     * 通知更新首页信息
     */
    private void sendMessageStationBroadcast() {
        Intent intent = new Intent();
        intent.setAction(DynamicHomeFragment.INTENT_MESSAGE_STATION);
        if(getActivity() != null)
            getActivity().sendBroadcast(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateLookTime();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MESSAGESTATION);
    }

}
