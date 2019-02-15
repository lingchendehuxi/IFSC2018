package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.AskQuestionListBean;
import com.android.incongress.cd.conference.model.Meeting;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/3/9.
 */

public class OrderMeetingAdapter extends BaseAdapter {
    private List<?> mList;
    private Context mContext;
    private String meetingType;

    public OrderMeetingAdapter(List<?> mList, Context mContext, String meetingType) {
        this.mList = mList;
        this.mContext = mContext;
        this.meetingType = meetingType;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_meeting_question, null);
        TextView questionTitle = convertView.findViewById(R.id.question_title);
        TextView questionName = convertView.findViewById(R.id.question_name);
        CheckBox questionRb = convertView.findViewById(R.id.question_cb);
        Meeting bean = (Meeting) mList.get(position);
        if ("alarm".equals(meetingType)) {
            if (0 == bean.getAttention()) {
                questionRb.setChecked(false);
            } else {
                questionRb.setChecked(true);
            }
        } else if ("order".equals(meetingType)) {
            if (bean.isOrder()) {
                questionRb.setChecked(true);
            } else {
                questionRb.setChecked(false);
            }
        }
        if (AppApplication.systemLanguage == 1) {
            questionName.setText(bean.getTopic());
        } else {
            questionName.setText(bean.getTopicEn());
        }
        questionTitle.setText(bean.getStartTime() + "-" + bean.getEndTime());
        return convertView;
    }


}



