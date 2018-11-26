package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.widget.LinearLayoutForListView;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Jacky on 2016/12/14.
 */

public class NowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<Session> mAllSessions;
    private String[] mRoleWithName;
    private Map<Integer, List<Meeting>> mMeetingBySession;
    private List<Class> mAllClasses;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private String mCurrentDay;
    private int[] mCurrentHourAndMinute;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public NowAdapter(List<Session> sessions, String[] roleWithName, List<Class> classes, Map<Integer, List<Meeting>> meetingBySession, String currentDay, int[] currentHourAndMinute ,Context context) {
        this.mAllSessions = sessions;
        this.mRoleWithName = roleWithName;
        this.mAllClasses = classes;
        this.mContext = context;
        this.mMeetingBySession = meetingBySession;
        this.mCurrentDay = currentDay;
        this.mCurrentHourAndMinute = currentHourAndMinute;
    }

    private Class getClassById(int classId) {

        if(mAllClasses == null || mAllClasses.size() == 0)
            return null;

        Class returnClass = null;
        for (int i = 0; i < mAllClasses.size(); i++) {
            if(mAllClasses.get(i).getClassesId() == classId){
                returnClass = mAllClasses.get(i);
                break;
            }
        }
        return  returnClass;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_now_fragment, parent, false);
        view.setOnClickListener(this);
        NowAdapter.SessionViewHolder holder = new SessionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Session session = mAllSessions.get(position);
        ((SessionViewHolder)holder).tvSessionName.setText(session.getSessionName());
        ((SessionViewHolder)holder).tvSessionTime.setText(session.getStartTime() +" - " + session.getEndTime());
        ((SessionViewHolder)holder).tvSessionLocation.setText(getClassById(session.getClassesId()).getClassesCode());

        holder.itemView.setTag(session);

        List<Meeting> meetings = mMeetingBySession.get(position);
        MeetingAdapter meetingAdapter = new MeetingAdapter(meetings);
        ((SessionViewHolder)holder).llfMeetings.setAdapter(meetingAdapter);

        String[] speakerWithRole = mRoleWithName[position].split("#@#");
        SpeakerWithRoleAdapter speakerWithRoleAdapter = new SpeakerWithRoleAdapter(speakerWithRole);
        ((SessionViewHolder)holder).llfSpeakers.setAdapter(speakerWithRoleAdapter);
    }

    @Override
    public int getItemCount() {
        if(mAllSessions!= null && mAllSessions.size()>0) {
            return mAllSessions.size();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Session) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Session session);
    }

    class MeetingAdapter extends BaseAdapter {
        private List<Meeting> meetings;

        public MeetingAdapter(List<Meeting> meetings) {
            this.meetings = meetings;
        }

        @Override
        public int getCount() {
            if(meetings!= null && meetings.size()>0)
                return meetings.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return meetings.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView ==null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_meeting_in_session,parent,false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            Meeting meeting = meetings.get(position);
            String info = meeting.getStartTime() +" " + meeting.getTopic();
            holder.meetingInfo.setText(info);

            String startTime = meeting.getStartTime();
            String endTime = meeting.getEndTime();

            int[] startTimes = getHourAndTime(startTime);
            int[] endTimes = getHourAndTime(endTime);

            if(isInTime(mCurrentHourAndMinute, startTimes, endTimes)) {
                holder.ivNowTag.setVisibility(View.VISIBLE);
                holder.meetingInfo.setTextColor(mContext.getResources().getColor(R.color.theme_color));
            }else {
                holder.ivNowTag.setVisibility(View.GONE);
                holder.meetingInfo.setTextColor(mContext.getResources().getColor(R.color.gray));
            }

            return convertView;
        }

        class ViewHolder {
            ImageView ivNowTag;
            TextView meetingInfo;

            public ViewHolder(View view) {
                this.meetingInfo = (TextView) view.findViewById(R.id.tv_meeting_info);
                this.ivNowTag = (ImageView) view.findViewById(R.id.iv_now_tag);
            }
        }
    }

    class SpeakerWithRoleAdapter extends BaseAdapter {
        private String[] speakerWithRole;

        public SpeakerWithRoleAdapter(String[] speakerWithRole) {
            this.speakerWithRole = speakerWithRole;
        }

        @Override
        public int getCount() {
            if(speakerWithRole!= null && speakerWithRole.length>0)
                return speakerWithRole.length;
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return speakerWithRole[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView ==null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_meeting_roles,parent,false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            String info = speakerWithRole[position];
            holder.meetingInfo.setText(info);
            return convertView;
        }

        class ViewHolder {
            TextView meetingInfo;

            public ViewHolder(View view) {
                this.meetingInfo = (TextView) view.findViewById(R.id.tv_meeting_info);
            }
        }
    }


    class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSessionTime;
        TextView tvSessionLocation;
        TextView tvSessionName;
        LinearLayoutForListView llfMeetings;
        LinearLayoutForListView llfSpeakers;

        public SessionViewHolder(View itemView) {
            super(itemView);

            tvSessionName = (TextView) itemView.findViewById(R.id.tv_session_name);
            tvSessionLocation = (TextView) itemView.findViewById(R.id.tv_session_location);
            tvSessionTime = (TextView) itemView.findViewById(R.id.tv_session_time);
            llfMeetings = (LinearLayoutForListView) itemView.findViewById(R.id.llfl_meeting_names);
            llfSpeakers = (LinearLayoutForListView) itemView.findViewById(R.id.llfl_speakers);
        }
    }

    /**
     * 获取当前时间，并存入int数组中
     * @param time
     * @return
     */
    private int[] getHourAndTime(String time) {
        int[] tempHourAndMinute = {0,0};
        if(TextUtils.isEmpty(time))
            return tempHourAndMinute;
        try {
            String hour = time.substring(0,2);
            if(!TextUtils.isEmpty(hour)) {
                int tempHour = Integer.parseInt(hour);
                tempHourAndMinute[0] = tempHour;
            }else {
                tempHourAndMinute[0] = 0;
            }
            String minute = time.substring(3,5);
            if(!TextUtils.isEmpty(minute)) {
                int tempMinute = Integer.parseInt(minute);
                tempHourAndMinute[1] = tempMinute;
            }else {
                tempHourAndMinute[1] = 0;
            }
        }catch (Exception e) {
            LogUtils.println("time parse error:" + e.getMessage());
        }

        return tempHourAndMinute;
    }

    /**
     * 判断是否在指定时间范围内
     * @param currentHourAndMinute
     * @param startHourAndMinute
     * @param endHourAndMinute
     * @return
     */
    private boolean isInTime(int[] currentHourAndMinute , int[] startHourAndMinute, int[] endHourAndMinute) {
        Date currentData = DateUtil.getDate(mCurrentDay +" " + currentHourAndMinute[0] +":" + currentHourAndMinute[1], DateUtil.DEFAULT_MINUTE);
        Date startData = DateUtil.getDate(mCurrentDay +" " + startHourAndMinute[0] +":" + startHourAndMinute[1], DateUtil.DEFAULT_MINUTE);
        Date endData = DateUtil.getDate(mCurrentDay +" " + endHourAndMinute[0] +":" + endHourAndMinute[1], DateUtil.DEFAULT_MINUTE);

        if(currentData.getTime() >= startData.getTime() && currentData.getTime() <= endData.getTime())
            return true;
        return false;
    }
}
