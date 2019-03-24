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

public class MyTempQuestionsSquarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<QuestionReplyBean> mAllQuestions;

    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_QUESTION = 2;

    public MyTempQuestionsSquarAdapter(Context context, List<QuestionReplyBean> questions) {
        this.mContext = context;
        this.mAllQuestions = questions;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TITLE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.question_squar_title, parent, false);
            QuestionTitleViewHolder titleViewHolder = new QuestionTitleViewHolder(view);
            return titleViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_question_squar, parent, false);
            QuestionTitleViewHolder questionViewHolder = new QuestionTitleViewHolder(view);
            return questionViewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_QUESTION;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(mAllQuestions.get(position).getContent())) {
            try {
                ((QuestionTitleViewHolder) holder).tvTitle.setText(URLDecoder.decode(mAllQuestions.get(position).getContent(), Constants.ENCODING_UTF8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ((QuestionTitleViewHolder) holder).tvTip.setText(mContext.getString(R.string.from_schedule)+" #" + mAllQuestions.get(position).getMeetingName() + "#");
        ((QuestionTitleViewHolder) holder).tv_question_user_name.setText(mAllQuestions.get(position).getAnswerUserName());
        if (holder.getItemViewType() == VIEW_TYPE_TITLE) {
            PicUtils.loadImageUrl(mContext, mAllQuestions.get(position).getAnswerUserImg(), ((QuestionTitleViewHolder) holder).cvHead);
            ((QuestionTitleViewHolder) holder).tv_question_user_time.setText(mAllQuestions.get(position).getTimeShow());
            try {
                if (!TextUtils.isEmpty(mAllQuestions.get(position).getAnswerContent())) {
                    ((QuestionTitleViewHolder) holder).tv_context.setText(URLDecoder.decode(mAllQuestions.get(position).getAnswerContent(), Constants.ENCODING_UTF8));
                    ((QuestionTitleViewHolder) holder).tv_context.flashView();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
        CircleImageView cvHead;

        public QuestionTitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTip = itemView.findViewById(R.id.tv_tip);
            cvHead = itemView.findViewById(R.id.cv_head);
            tv_question_user_name = itemView.findViewById(R.id.tv_question_user_name);
            tv_question_user_time = itemView.findViewById(R.id.tv_question_user_time);
            tv_context = itemView.findViewById(R.id.tv_context);

        }
    }

}
