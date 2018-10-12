package com.android.incongress.cd.conference.fragments.message_station;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.WebViewContainerActivity;
import com.android.incongress.cd.conference.adapters.MessageStationAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MessageBean;
import com.android.incongress.cd.conference.fragments.DynamicHomeFragment;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
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
 * Created by Jacky on 2016/1/29.
 */
public class MessageStationActionFragment extends BaseFragment {
    private List<MessageBean> mMessageBeans = new ArrayList<>();
    private MessageStationAdapter mAdapter;

    protected XRecyclerView mRecyclerView;
    private TextView mTvNoTips,mMessageXT,mMessageGR;
    private int mCurrentPage = 1;
    private ImageView mImgXT,mImgGR,mImgGR_TS;
    private RelativeLayout mLayoutXT,mLayoutGR;
    private boolean type = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_station, null, false);
        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        mTvNoTips = (TextView) view.findViewById(R.id.tv_tips);
        mMessageXT = (TextView) view.findViewById(R.id.massage_xt);
        mMessageGR = (TextView) view.findViewById(R.id.massage_gr);
        mImgXT = (ImageView) view.findViewById(R.id.massage_xt_img);
        mImgGR = (ImageView) view.findViewById(R.id.massage_gr_img);
        mImgGR_TS = (ImageView) view.findViewById(R.id.massage_gr_tz);
        mLayoutXT = (RelativeLayout) view.findViewById(R.id.massage_xt_layout);
        mLayoutGR = (RelativeLayout) view.findViewById(R.id.massage_gr_layout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mAdapter = new MessageStationAdapter(getActivity(), mMessageBeans);

        mRecyclerView.setAdapter(mAdapter);

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
                if(type){
                    getDatas(mCurrentPage = 1);
                }else{
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
        mLayoutXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = true;
                mMessageXT.setTextColor(getResources().getColor(R.color.alpha_theme_color));
                mMessageGR.setTextColor(getResources().getColor(R.color.normal_gray));
                mImgGR.setVisibility(View.GONE);
                mImgXT.setVisibility(View.VISIBLE);
                getDatas(mCurrentPage = 1);
            }
        });
        mLayoutGR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = false;
                mMessageXT.setTextColor(getResources().getColor(R.color.normal_gray));
                mMessageGR.setTextColor(getResources().getColor(R.color.alpha_theme_color));
                mImgGR.setVisibility(View.VISIBLE);
                mImgXT.setVisibility(View.GONE);
                mImgGR_TS.setVisibility(View.GONE);
                SharePreferenceUtils.setSPBooleanValue("messageToken",false);
                getUserDatas();
            }
        });

        return view;
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

    private void getDatas(final int currentPage) {
        CHYHttpClientUsage.getInstanse().doGetTokenList(AppApplication.conId + "", Constants.PAGE_SIZE, currentPage + "", new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
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
    @Override
    public void onResume() {
        super.onResume();
        updateLookTime();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MESSAGESTATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MESSAGESTATION);
    }
}
