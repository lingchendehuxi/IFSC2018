package com.android.incongress.cd.conference.fragments.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.adapters.MyMeetingQuestionsAdapter;
import com.android.incongress.cd.conference.adapters.MyPosterQuestionsAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion;
import com.android.incongress.cd.conference.beans.PosterBean;
import com.android.incongress.cd.conference.beans.PosterQuestionBean;
import com.android.incongress.cd.conference.fragments.wall_poster.PosterImageFragment;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/12/22.
 */

public class PosterQuestionFragment extends BaseFragment {
    private XRecyclerView mRecyclerView;
    private View emptyView;
    private int mLastPosterQuestionId = -1;

    private PosterQuestionBean mPosterQuestionBean;
    private List<PosterQuestionBean.SceneShowArrayBean> mQuestions = new ArrayList<>();
    private MyPosterQuestionsAdapter mQuestionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_question, container, false);

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        emptyView = view.findViewById(R.id.tv_tips);
        mRecyclerView.setEmptyView(emptyView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mQuestionAdapter = new MyPosterQuestionsAdapter(getActivity(), mQuestions);
        mRecyclerView.setAdapter(mQuestionAdapter);

        mQuestionAdapter.setPosterClickListener(new MyPosterQuestionsAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(PosterQuestionBean.SceneShowArrayBean bean) {
                CHYHttpClientUsage.getInstanse().doGetPosterByID(AppApplication.conId, bean.getPosterId() +"", AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler(Constants.ENCODING_GBK){
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgressBar("正在获取壁报信息");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressBar();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        LogUtils.println("response:" + response);
                        Gson gson = new Gson();
                        PosterBean posterBean = gson.fromJson(response.toString(), PosterBean.class);

                        if(posterBean.getState() == 1) {
                            DZBBBean dzbb = new DZBBBean(posterBean.getPosterId(),posterBean.getPosterCode(), posterBean.getConField(), posterBean.getTitle(), posterBean.getAuthor(), posterBean.getPosterPicUrl(),0,0,posterBean.getIsJingxuan());
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), PosterImageFragment.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean",dzbb);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }else {
                            ToastUtils.showShorToast("未找到该电子壁报，可能已被删除");
                        }
                    }
                });
            }
        });
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshQuestionData(mLastPosterQuestionId = -1);
            }

            @Override
            public void onLoadMore() {
                refreshQuestionData(mLastPosterQuestionId);
            }
        });

        mRecyclerView.setRefreshing(true);
        return view;
    }

    private void refreshQuestionData(final int lastQuestionId) {
        CHYHttpClientUsage.getInstanse().doGetQuestionByPoster(AppApplication.conId, lastQuestionId, AppApplication.userId, AppApplication.userType, AppApplication.getSystemLanuageCode(), new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.println("meeting question:" + response);
                try {
                    if (response.getInt("state") == 1) {
                        Gson gson = new Gson();
                        mPosterQuestionBean = gson.fromJson(response.toString(), PosterQuestionBean.class);

                        if(lastQuestionId == -1)
                            mQuestions.clear();
                        mQuestions.addAll(mPosterQuestionBean.getSceneShowArray());
                        mQuestionAdapter.notifyDataSetChanged();

                        if (mQuestions.size() > 0) {
                            mLastPosterQuestionId = mQuestions.get(mQuestions.size() - 1).getSceneShowId();
                        }
                    } else {
                        ToastUtils.showShorToast("没有更多数据了哦");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRecyclerView.refreshComplete();
                mRecyclerView.loadMoreComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.println("meeting question:failure");
            }
        });

    }
}
