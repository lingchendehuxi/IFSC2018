package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion;
import com.android.incongress.cd.conference.beans.PosterQuestionBean;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/12/27.
 */

public class MyPosterQuestionsAdapter extends RecyclerView.Adapter<MyPosterQuestionsAdapter.QuestionViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<PosterQuestionBean.SceneShowArrayBean> mMyMeetingQuestions;

    public MyPosterQuestionsAdapter(Context context, List<PosterQuestionBean.SceneShowArrayBean> meetingQuestions) {
        this.mContext = context;
        this.mMyMeetingQuestions = meetingQuestions;
    }

    @Override
    public MyPosterQuestionsAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_poster,parent, false);
        view.findViewById(R.id.tv_share).setOnClickListener(this);
        QuestionViewHolder questionViewHolder = new QuestionViewHolder(view);
        return questionViewHolder;
    }

    @Override
    public void onBindViewHolder(MyPosterQuestionsAdapter.QuestionViewHolder holder, int position) {
        final PosterQuestionBean.SceneShowArrayBean myMeetingQuestionBean = mMyMeetingQuestions.get(position);
        if(myMeetingQuestionBean != null) {
            holder.tvQuestionTime.setText(myMeetingQuestionBean.getTimeShow());
            holder.tvAskName.setText(myMeetingQuestionBean.getAnswerUserName());
            String meetingName = "";
            String question = "";
            try {
                meetingName =  "#" + myMeetingQuestionBean.getPosterTitle() + "#";
                question= "Q:"+URLDecoder.decode(myMeetingQuestionBean.getContent(), Constants.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            holder.tvQuestion.setText(question);
            holder.tvMeetingName.setText(meetingName);
            holder.tvShare.setTag(myMeetingQuestionBean);
        }
    }

    @Override
    public int getItemCount() {
        return mMyMeetingQuestions != null? mMyMeetingQuestions.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if(mPosterClickListener != null)
            mPosterClickListener.onPosterClick((PosterQuestionBean.SceneShowArrayBean)v.getTag());
    }

    private OnPosterClickListener mPosterClickListener;

    public void setPosterClickListener(OnPosterClickListener posterClickListener) {
        this.mPosterClickListener = posterClickListener;
    }

    public interface OnPosterClickListener{
        void onPosterClick(PosterQuestionBean.SceneShowArrayBean bean);
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionTime;
        TextView tvAskName;
        TextView tvMeetingName;
        TextView tvQuestion;
        TextView tvShare;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestionTime = (TextView) itemView.findViewById(R.id.tv_question_time);
            tvAskName = (TextView) itemView.findViewById(R.id.tv_ask_name);
            tvMeetingName = (TextView) itemView.findViewById(R.id.tv_meeting_name);
            tvQuestion = (TextView) itemView.findViewById(R.id.tv_question);
            tvShare = (TextView) itemView.findViewById(R.id.tv_share);
        }
    }
}
