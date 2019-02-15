package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersAdapter;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.google.android.exoplayer.C;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Jacky on 2016/1/8.
 */
public class MeetingScheduleListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private List<Session> mSessions;
    private List<Class> mClassBean;
    private LayoutInflater mInflater;
    private List<Meeting> mMeetingBeanList = new ArrayList<>();
    private LinkedList<Session> linkedList = new LinkedList<>();

    //1）sectionIndices数组用来存放每一轮分组的第一个item的位置。
    //2）sectionHeaders数组用来存放每一个分组要展现的数据，
    private int[] mSectionIndices;
    private String[] mSectionHeaders;

    public MeetingScheduleListAdapter(Context ctx, List<Session> session) {
        this.mSessions = session;
        this.mInflater = LayoutInflater.from(ctx);
        this.mClassBean = ConferenceDbUtils.getAllClasses();
        Collections.sort(mSessions, new Comparator<Session>() {
            @Override
            public int compare(Session session, Session t1) {
                return ((Integer)(ConferenceDbUtils.getClassOrder(session.getClassesId()+""))).compareTo(ConferenceDbUtils.getClassOrder(t1.getClassesId()+""));
            }
        });
        mSectionIndices = getSectionIndices();
        mSectionHeaders = getSectionHeaders();
    }

    @Override
    public Object[] getSections() {
        return mSectionHeaders;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (sectionIndex >= mSectionIndices.length) {
            sectionIndex = mSectionIndices.length - 1;
        } else if (sectionIndex < 0) {
            sectionIndex = 0;
        }
        return mSectionIndices[sectionIndex];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    /**
     * sectionIndices数组用来存放每一轮分组的第一个item的位置。
     * @return
     */
    private int[] getSectionIndices() {
        List<Integer> secionIndices = new ArrayList<Integer>();
        int classId = mSessions.get(0).getClassesId();
        secionIndices.add(0);

        for(int i=1; i< mSessions.size(); i++) {
            int tempClassId = mSessions.get(i).getClassesId();

            if(classId != tempClassId) {
                classId = tempClassId;
                secionIndices.add(i);
            }
        }

        int[] secions = new int[secionIndices.size()];

        for(int i=0; i<secionIndices.size(); i++) {
            secions[i] = secionIndices.get(i);
        }
        return secions;
    }

    /**
     * header中存放的数据
     * @return
     */
    private String[] getSectionHeaders(){
        String[] sectionHeader = new String[mSectionIndices.length];
        for(int i=0; i< mSectionIndices.length; i++) {
            sectionHeader[mSectionIndices.length-i-1] = mSessions.get(mSectionIndices[i]).getClassesId() + "";
        }

        return sectionHeader;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if(convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.item_headerview_schedule_list, parent, false);
            holder.tvClassRoom = convertView.findViewById(R.id.tv_schedule_list_location);
            holder.view_begin = convertView.findViewById(R.id.view_begin);
            convertView.setTag(holder);
        }else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        if(position == 0){
            holder.view_begin.setVisibility(View.GONE);
        }else {
            holder.view_begin.setVisibility(View.VISIBLE);
        }
        Log.d("sgqTest", "getHeaderView: "+position);
        for(int i=0; i<mClassBean.size(); i++) {
            if(mClassBean.get(i).getClassesId() == mSessions.get(position).getClassesId()) {
                if(AppApplication.systemLanguage == 1) {
                    holder.tvClassRoom.setText(mClassBean.get(i).getClassesCode());
                }else {
                    holder.tvClassRoom.setText(mClassBean.get(i).getClassCodeEn());
                }
                break;
            }
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mSessions.get(position).getClassesId();
    }

    @Override
    public int getCount() {
        return mSessions.size();
    }

    @Override
    public Object getItem(int position) {
        return mSessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_single_schedule_list, parent, false);
            holder.tvMeetingName = convertView.findViewById(R.id.schedule_list_title);
            holder.tvMeetingTime = convertView.findViewById(R.id.schedule_list_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        getMeetingBeanBySessionId(String.valueOf(mSessions.get(position).getSessionGroupId()));

        if(AppApplication.systemLanguage == 1) {
            holder.tvMeetingName.setText(mSessions.get(position).getSessionName());
        }else {
            holder.tvMeetingName.setText(mSessions.get(position).getSessionNameEN());
        }

        Date date = DateUtil.getDate(mSessions.get(position).getSessionDay(), DateUtil.DEFAULT);
        holder.tvMeetingTime.setText(DateUtil.getDateShort(date)  + " " + mSessions.get(position).getStartTime() + "-" + mSessions.get(position).getEndTime());

        return convertView;
    }

    class HeaderViewHolder {
        TextView tvClassRoom;
        View view_begin;
    }

    class ViewHolder {
        TextView tvMeetingName;
        TextView tvMeetingTime;
    }
    /**
     * 根据sessionGroupId去查找所有的meetingId
     *
     * @param sessionGroupId
     */
    private void getMeetingBeanBySessionId(String sessionGroupId) {
        mMeetingBeanList.addAll(ConferenceDbUtils.getMeetingBySessionGroupId(sessionGroupId));
    }
}
