package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionSearchAdapter extends BaseAdapter {
    private Context mContext;
    private String sessionname;
    private List<Session> datasource = new ArrayList<>();

    public SessionSearchAdapter(Context context) {
        this.mContext = context;
    }

    public List<Session> getDatasource() {
        return datasource;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //AppApplication.applyFont(mContext,convertView,"fonts/zd.TTF");
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.search_child);
            holder = new Holder();
            TextView titleView = (TextView) convertView.findViewById(R.id.search_child_title);
            TextView timeView = (TextView) convertView.findViewById(R.id.search_child_time);
            TextView roomView = (TextView) convertView.findViewById(R.id.search_child_room);
            holder.titleView = titleView;
            holder.timeView = timeView;
            holder.roomView = roomView;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Session bean = datasource.get(position);
        Date date = DateUtil.getDate(bean.getSessionDay(), DateUtil.DEFAULT);
        Class classBean = ConferenceDbUtils.findClassByClassId(bean.getClassesId());

        if (AppApplication.systemLanguage == 1) {
            holder.titleView.setText(setKeyWordColor(bean.getSessionName(),sessionname));
            holder.roomView.setText(classBean.getClassesCode());
            holder.timeView.setText(DateUtil.getDateString(date, DateUtil.DEFAULT_CHINA_TWO) + " " + bean.getStartTime() + "-" + bean.getEndTime());
        } else {
            holder.titleView.setText(setKeyWordColor(bean.getSessionNameEN(),sessionname));
            holder.roomView.setText(classBean.getClassCodeEn());
            holder.timeView.setText(DateUtil.getDateShort(date) + " " + bean.getStartTime() + "-" + bean.getEndTime());
        }
        return convertView;
    }


    private class Holder {
        TextView titleView;
        TextView timeView;
        TextView roomView;
    }

    public void search(String sessionname) {
        this.sessionname = sessionname;
        if (AppApplication.systemLanguage == 1) {
            datasource = ConferenceDbUtils.getSessionByName(sessionname, false);
        } else {
            datasource = ConferenceDbUtils.getSessionByName(sessionname, true);
        }
        notifyDataSetChanged();
    }
    public void searchMeeting(String meetingname) {
        List<Meeting> list = new ArrayList<>();
        if (AppApplication.systemLanguage == 1) {
            list = ConferenceDbUtils.getMeetingByName(meetingname, false);
        } else {
            list = ConferenceDbUtils.getMeetingByName(meetingname, true);
        }
        int i = list.size();
    }
    /**
     * 设置搜索关键字高亮
     * @param content 原文本内容
     * @param keyword 关键字
     */
    private SpannableString setKeyWordColor(String content, String keyword){
        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()){
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.theme_color)),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }
}
