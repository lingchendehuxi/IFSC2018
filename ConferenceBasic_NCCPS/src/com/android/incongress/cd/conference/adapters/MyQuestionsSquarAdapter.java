package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MyMeetingQuestion;
import com.android.incongress.cd.conference.beans.QuestionReplyBean;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.text_view.MySpannableTextView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Jacky on 2016/12/27.
 */

public class MyQuestionsSquarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<QuestionReplyBean.SceneShowArrayBean> mAllQuestions;

    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_QUESTION = 2;
    private QuestionsAnswerClick answerClick;

    public MyQuestionsSquarAdapter(Context context, List<QuestionReplyBean.SceneShowArrayBean> questions, QuestionsAnswerClick answerClick) {
        this.mContext = context;
        this.mAllQuestions = questions;
        this.answerClick = answerClick;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_question_squar_title, parent, false);
            QuestionTitleViewHolder titleViewHolder = new QuestionTitleViewHolder(view);
            return titleViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_item_question_squar, parent, false);
            QuestionContextViewHolder questionViewHolder = new QuestionContextViewHolder(view);
            return questionViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        /*if (1 == mAllQuestions.get(position).getIsHuiFu()) {
            return VIEW_TYPE_TITLE;
        } else {
            return VIEW_TYPE_QUESTION;
        }*/
        return VIEW_TYPE_QUESTION;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder.getItemViewType() == VIEW_TYPE_TITLE){
                try {
                    if(!TextUtils.isEmpty(mAllQuestions.get(position).getContent())){
                        ((MyQuestionsSquarAdapter.QuestionTitleViewHolder) holder).tvTitle.setText(URLDecoder.decode(mAllQuestions.get(position).getContent(), Constants.ENCODING_UTF8));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            ((MyQuestionsSquarAdapter.QuestionTitleViewHolder) holder).tvTip.setText("来自日程 #" + mAllQuestions.get(position).getMeetingName() + "#");
            ((MyQuestionsSquarAdapter.QuestionTitleViewHolder) holder).tv_question_user_name.setText(mAllQuestions.get(position).getSpeakerName());
            PicUtils.loadImageUrl(mContext, mAllQuestions.get(position).getImgUrl(), ((MyQuestionsSquarAdapter.QuestionTitleViewHolder) holder).cvHead);
        }else {
            try {
                if(!TextUtils.isEmpty(mAllQuestions.get(position).getContent())){
                    ((MyQuestionsSquarAdapter.QuestionContextViewHolder) holder).tvTitle.setText(URLDecoder.decode(mAllQuestions.get(position).getContent(), Constants.ENCODING_UTF8));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ((MyQuestionsSquarAdapter.QuestionContextViewHolder) holder).tvTip.setText("来自日程 #" + mAllQuestions.get(position).getMeetingName() + "#");
            ((MyQuestionsSquarAdapter.QuestionContextViewHolder) holder).tv_question_user_name.setText(mAllQuestions.get(position).getSpeakerName());
            PicUtils.loadImageUrl(mContext, mAllQuestions.get(position).getImgUrl(), ((MyQuestionsSquarAdapter.QuestionContextViewHolder) holder).cvHead);
            ((QuestionContextViewHolder)holder).tv_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    answerClick.qustionAnswer(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if (mAllQuestions != null) {
            return mAllQuestions.size();
        } else {
            return 0;
        }
    }

    class QuestionTitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvTip;
        TextView tv_question_user_name;
        TextView tv_question_user_time;
        MySpannableTextView tv_context;
        TextView tv_answered;
        CircleImageView cvHead;

        public QuestionTitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTip = itemView.findViewById(R.id.tv_tip);
            cvHead = itemView.findViewById(R.id.cv_head);
            tv_question_user_name = itemView.findViewById(R.id.tv_question_user_name);
            tv_question_user_time = itemView.findViewById(R.id.tv_question_user_time);
            tv_context = itemView.findViewById(R.id.tv_context);
            tv_answered = itemView.findViewById(R.id.tv_answered);

        }
    }
    class QuestionContextViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvTip;
        TextView tv_question_user_name;
        TextView tv_question_user_time;
        TextView tv_context;
        TextView tv_answered,tv_answer;
        CircleImageView cvHead;

        public QuestionContextViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTip = itemView.findViewById(R.id.tv_tip);
            cvHead = itemView.findViewById(R.id.cv_head);
            tv_question_user_name = itemView.findViewById(R.id.tv_question_user_name);
            tv_question_user_time = itemView.findViewById(R.id.tv_question_user_time);
            tv_context = itemView.findViewById(R.id.tv_context);
            tv_answered = itemView.findViewById(R.id.tv_answered);
            tv_answer = itemView.findViewById(R.id.tv_answer);
        }
    }
    public interface QuestionsAnswerClick{
        void qustionAnswer(int position);
    }

}
