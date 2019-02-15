package com.android.incongress.cd.conference.fragments.question;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyMeetingQuestionsAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/12/22.
 */

public class MeetingQuestionFragment extends BaseFragment implements View.OnClickListener{
    private XRecyclerView mRVQuestion;
    private View emptyView;
    private TextView fawen;
    private MyMeetingQuestion mAllQuestions;
    private MyMeetingQuestionsAdapter mQuestionAdapter;
    private ProgressBar mPbLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_question, container,false);
        mRVQuestion = (XRecyclerView) view.findViewById(R.id.rv_question);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        fawen = (TextView) view.findViewById(R.id.tv_question);
        emptyView = view.findViewById(R.id.tv_tips);
        mRVQuestion.setEmptyView(emptyView);

        fawen.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRVQuestion.setLayoutManager(layoutManager);

        refreshQuestionData();
        mRVQuestion.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshQuestionData( );
            }

            @Override
            public void onLoadMore() {
            }
        });
        mRVQuestion.setLoadingMoreEnabled(false);
        mRVQuestion.setRefreshing(true);
        return view;
    }

    /**
     * 只有下拉刷新
     */
    private void refreshQuestionData() {
        CHYHttpClientUsage.getInstanse().doGetQuestionsBySessionV2( new JsonHttpResponseHandler(Constants.ENCODING_GBK){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("GYW",response.toString());
                try {
                    Gson gson = new Gson();
                    mAllQuestions = gson.fromJson(response.toString(), MyMeetingQuestion.class);
                    mQuestionAdapter = new MyMeetingQuestionsAdapter(getActivity(), mAllQuestions);
                    mRVQuestion.setAdapter(mQuestionAdapter);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRVQuestion.refreshComplete();
                mPbLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_question:
                View view = CommonUtils.initView(getActivity(), R.layout.title_right_image);
                ((ImageView)view).setImageResource(R.drawable.room_down);
                QuestionByMeetingOrPoster questionByMeetingOrPoster = new QuestionByMeetingOrPoster();
                questionByMeetingOrPoster.setRightView(view);
                action(questionByMeetingOrPoster, R.string.make_question, view, false, false, false);
                break;

        }
    }
}
