package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion2;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Admin on 2017/5/19.
 */

public class MyMeetingQuestionsAdapter2 extends RecyclerView.Adapter<MyMeetingQuestionsAdapter2.QuestionViewHolder>{
    private Context mContext;
    private List<MyMeetingQuestion2.SceneShowArrayBean> mMyMeetingQuestions;

    public MyMeetingQuestionsAdapter2(Context context, List<MyMeetingQuestion2.SceneShowArrayBean> meetingQuestions) {
        this.mContext = context;
        this.mMyMeetingQuestions = meetingQuestions;
    }


    @Override
    public MyMeetingQuestionsAdapter2.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_meeting2,parent, false);
        MyMeetingQuestionsAdapter2.QuestionViewHolder questionViewHolder = new MyMeetingQuestionsAdapter2.QuestionViewHolder(view);
        return questionViewHolder;
    }


    @Override
    public void onBindViewHolder(MyMeetingQuestionsAdapter2.QuestionViewHolder holder, int position) {
        final MyMeetingQuestion2.SceneShowArrayBean myMeetingQuestionBean = mMyMeetingQuestions.get(position);
        if(myMeetingQuestionBean != null) {
            holder.llPraise.setVisibility(View.GONE);
            holder.tvQuestionTime.setText(myMeetingQuestionBean.getTimeShow());
            holder.tw.setText("收到提问");
            holder.tvAskName.setText(myMeetingQuestionBean.getAnswerUserName());
            String meetingName = "";
            String question = "";
            String answer = "";
            try {
                meetingName =  "#" +myMeetingQuestionBean.getMeetingName() + "#";
                question= "Q:"+ URLDecoder.decode(myMeetingQuestionBean.getContent(), Constants.ENCODING_UTF8);
                answer = "A:" + URLDecoder.decode(myMeetingQuestionBean.getAnswerContent(), Constants.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            holder.tvQuestion.setText(question);
            holder.tvMeetingName.setText(meetingName);
            holder.tvAnswer.setText(answer);

            if(myMeetingQuestionBean.getIsShow() == 1) {
                holder.llPraise.setVisibility(View.VISIBLE);
                holder.tvPraise.setText(mContext.getString(R.string.xxx_praise, myMeetingQuestionBean.getLaudCount()+""));
            }

            if(myMeetingQuestionBean.getIsHuiFu() == 1) {
                holder.tvAnswer.setVisibility(View.VISIBLE);
                //holder.llShare.setVisibility(View.VISIBLE);
            }else {
                holder.tvAnswer.setVisibility(View.GONE);
                holder.llShare.setVisibility(View.GONE);
            }


           /* holder.llShare.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View v) {
                    if(myMeetingQuestionBean.getIsShow() == 1) {
                        ToastUtils.showShorToast("不能重复分享哦");
                    }else {
                        CHYHttpClientUsage.getInstanse().doShareMeetingQuestion(AppApplication.conId,myMeetingQuestionBean.getSceneShowId(),AppApplication.getSystemLanuageCode(), 1, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                LogUtils.println("onSuccess response:" + response);
                            }
                        });
                    }
                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return mMyMeetingQuestions != null? mMyMeetingQuestions.size() : 0;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionTime;
        TextView tvAskName;
        TextView tw;
        TextView tvMeetingName;
        TextView tvQuestion;
        TextView tvAnswer;
        LinearLayout llPraise;
        TextView tvPraise;
        LinearLayout llShare;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestionTime = (TextView) itemView.findViewById(R.id.tv_question_time);
            tvAskName = (TextView) itemView.findViewById(R.id.tv_ask_name);
            tvMeetingName = (TextView) itemView.findViewById(R.id.tv_meeting_name);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            tvAnswer = (TextView) itemView.findViewById(R.id.tv_answer);
            llPraise = (LinearLayout) itemView.findViewById(R.id.ll_praise);
            tvPraise = (TextView) itemView.findViewById(R.id.tv_praise);
            tw = (TextView) itemView.findViewById(R.id.item_question_meeting_tw);
            llShare = (LinearLayout) itemView.findViewById(R.id.ll_share);
        }
    }
}
