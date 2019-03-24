package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.incongress.cd.conference.beans.AskPupBean;
import com.android.incongress.cd.conference.beans.AskQuestionListBean;
import com.android.incongress.cd.conference.model.Meeting;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 2018/3/9.
 */

public class AskQuestionAdapter extends BaseAdapter {
    private List<AskPupBean> mList;
    private Context mContext;
    public List<RadioButton> radioList;

    public AskQuestionAdapter(List<AskPupBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        radioList = new ArrayList<>();
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ask_question, null);
        TextView questionTitle = convertView.findViewById(R.id.question_title);
        TextView questionName = convertView.findViewById(R.id.question_name);
        RadioButton questionRb = convertView.findViewById(R.id.question_rb);
        AskPupBean bean = (AskPupBean)mList.get(position);
        questionName.setText(bean.getFirName());
        questionTitle.setText(bean.getTopic());
        if(bean.isSelected()){
            questionRb.setChecked(true);
        }else {
            questionRb.setChecked(false);
        }
        radioList.add(questionRb);
        return convertView;
    }

}



