package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/12/27.
 */

public class MyMeetingQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private MyMeetingQuestion mAllQuestions;

    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_QUESTION =2;

    public MyMeetingQuestionsAdapter(Context context, MyMeetingQuestion questions) {
        this.mContext = context;
        this.mAllQuestions = questions;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.question_title,parent, false);
            QuestionTitleViewHolder titleViewHolder = new QuestionTitleViewHolder(view);
            return titleViewHolder;
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_meeting,parent, false);
            QuestionViewHolder questionViewHolder = new QuestionViewHolder(view);
            return questionViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == mAllQuestions.getQCount1() + 1) {
            return VIEW_TYPE_TITLE;
        }else {
            return VIEW_TYPE_QUESTION;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == VIEW_TYPE_TITLE) {
            if(position == 0) {
                if(AppApplication.systemLanguage ==1){
                    ((QuestionTitleViewHolder)holder).tvTitle.setText("我的提问");
                }else{
                    ((QuestionTitleViewHolder)holder).tvTitle.setText("My Question");
                }

            }else {
                if(AppApplication.systemLanguage ==1){
                    ((QuestionTitleViewHolder)holder).tvTitle.setText("其他提问");
                }else{
                    ((QuestionTitleViewHolder)holder).tvTitle.setText("Other Question");
                }
            }
        }else {
            if(position <= mAllQuestions.getQCount1()) {
                //我的提问
                MyMeetingQuestion.SceneShowArray1Bean sceneShowArrayBean = mAllQuestions.getSceneShowArray1().get(position - 1);
                fillQuestionContent((QuestionViewHolder) holder,sceneShowArrayBean.getMeetingName(), sceneShowArrayBean.getTimeShow(), sceneShowArrayBean.getAnswerUserName(), sceneShowArrayBean.getContent(), sceneShowArrayBean.getAnswerContent(),
                        sceneShowArrayBean.getIsShow(), sceneShowArrayBean.getIsHuiFu(),sceneShowArrayBean.getLaudCount(),sceneShowArrayBean.getSceneShowId(),true);
            }else {
                //其他提问
                MyMeetingQuestion.SceneShowArray2Bean sceneShowArrayBean = mAllQuestions.getSceneShowArray2().get(position - mAllQuestions.getQCount1() - 1 - 1);
                fillQuestionContent((QuestionViewHolder) holder,sceneShowArrayBean.getMeetingName(), sceneShowArrayBean.getTimeShow(), sceneShowArrayBean.getAnswerUserName(), sceneShowArrayBean.getContent(), sceneShowArrayBean.getAnswerContent(),
                        sceneShowArrayBean.getIsShow(), sceneShowArrayBean.getIsHuiFu(),sceneShowArrayBean.getLaudCount(),sceneShowArrayBean.getSceneShowId(),false);
            }
        }
    }

    /**
     * 填充提问卡片
     */
    private void fillQuestionContent(QuestionViewHolder holder, String meeting, String timeShow, String answerUserName, String content, String answerContent, final int isShow, int isHuiFu, int laudCount,final int sceneShowId,boolean isFromMe) {
        holder.tvQuestionTime.setText(timeShow);
        holder.tvAskName.setText(answerUserName);
        if(isFromMe) {
            holder.me.setVisibility(View.VISIBLE);
            if(AppApplication.systemLanguage ==1){
                holder.tvLast.setText("提问");
            }else{
                holder.tvLast.setText("Initiate question");
            }
        }else {
            holder.me.setVisibility(View.GONE);
            if(AppApplication.systemLanguage ==1){
                holder.tvLast.setText("收到提问");
            }else{
                holder.tvLast.setText("Receive question");
            }
        }
        String meetingName = "";
        String question = "";
        String answer = "";

        try {
            meetingName =  "#" + meeting + "#";
            question= "Q:"+URLDecoder.decode(content, Constants.ENCODING_UTF8);
            answer = "A:" + URLDecoder.decode(answerContent, Constants.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.tvQuestion.setText(question);
        holder.tvMeetingName.setText(meetingName);
        holder.tvAnswer.setText(answer);

        if(isShow == 1) {
            holder.llPraise.setVisibility(View.VISIBLE);
            holder.tvPraise.setText(mContext.getString(R.string.xxx_praise, laudCount + ""));
        }else {
            holder.llPraise.setVisibility(View.GONE);
        }

        if(isHuiFu == 1) {
            holder.tvAnswer.setVisibility(View.VISIBLE);
            holder.llShare.setVisibility(View.VISIBLE);
        }else {
            holder.tvAnswer.setVisibility(View.GONE);
            holder.llShare.setVisibility(View.GONE);
        }


        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                if(isShow == 1) {
                    ToastUtils.showShorToast("不能重复分享哦");
                }else {
                    CHYHttpClientUsage.getInstanse().doShareMeetingQuestion(Constants.getConId(), sceneShowId, AppApplication.getSystemLanuageCode(), 1, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            LogUtils.println("onSuccess response:" + response);
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mAllQuestions != null) {
            return mAllQuestions.getQCount1() + 1 + mAllQuestions.getQCount2() + 1;
        }else {
            return 0;
        }
    }

    class QuestionTitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        public QuestionTitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionTime;
        TextView tvAskName;
        TextView tvMeetingName;
        TextView tvQuestion;
        TextView tvAnswer;
        LinearLayout llPraise;
        TextView tvPraise;
        TextView me;
        LinearLayout llShare;
        TextView tvLast;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestionTime = (TextView) itemView.findViewById(R.id.tv_question_time);
            tvAskName = (TextView) itemView.findViewById(R.id.tv_ask_name);
            tvMeetingName = (TextView) itemView.findViewById(R.id.tv_meeting_name);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            llPraise = (LinearLayout) itemView.findViewById(R.id.ll_praise);
            tvPraise = (TextView) itemView.findViewById(R.id.tv_praise);
            llShare = (LinearLayout) itemView.findViewById(R.id.ll_share);
            me = (TextView) itemView.findViewById(R.id.item_question_meeting_me);
            tvLast = (TextView) itemView.findViewById(R.id.item_question_meeting_tw);
        }
    }
}
