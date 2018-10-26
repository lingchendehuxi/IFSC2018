package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

public class MyScheduleDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<Session> mSesionBeans;
    private List<Meeting> mMeetingBeans;
    private List<Class> mClassesBeans;
    private boolean isAlarmShow = false;

    public MyScheduleDetailAdapter(Context context, List<Session> sesionBeans, List<Meeting> meetingBeans) {
        this.mContext = context;
        this.mSesionBeans = sesionBeans;
        this.mMeetingBeans = meetingBeans;
        this.mClassesBeans = ConferenceDbUtils.getAllClasses();
    }

    public void setAlarmShow(boolean isShow) {
        this.isAlarmShow = isShow;
        notifyDataSetChanged();
    }

    public boolean getAlarmMode() {
        return isAlarmShow;
    }

    @Override
    public int getCount() {
        return mSesionBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mSesionBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_schedule, null);
            holder = new ViewHolder();
            holder.tvSessionName = (TextView) convertView.findViewById(R.id.tv_session_name);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
            holder.llMeetings = (LinearLayout) convertView.findViewById(R.id.ll_time_and_meeting);
            holder.ivAlarm = (ImageView) convertView.findViewById(R.id.iv_alarm);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Session session = mSesionBeans.get(position);
        if(AppApplication.systemLanguage == 1){
            holder.tvSessionName.setText(session.getSessionName());
        }else{
            holder.tvSessionName.setText(session.getSessionNameEN());
        }

        holder.tvTime.setText(session.getStartTime() + " - " + session.getEndTime());

        for (int i = 0; i < mClassesBeans.size(); i++) {
            if (session.getClassesId() == mClassesBeans.get(i).getClassesId()) {
                if(AppApplication.systemLanguage == 1){
                    holder.tvLocation.setText(mClassesBeans.get(i).getClassesCode());
                }else{
                    holder.tvLocation.setText(mClassesBeans.get(i).getClassCodeEn());
                }
                break;
            }
        }

        holder.llMeetings.removeAllViews();
        for (int i = 0; i < mMeetingBeans.size(); i++) {
            if (mMeetingBeans.get(i).getSessionGroupId() == session.getSessionGroupId()) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_meeting_and_time, null);
                ((TextView) view).setText(mMeetingBeans.get(i).getStartTime() + " - " + mMeetingBeans.get(i).getTopic());
                holder.llMeetings.addView(view);
            }
        }

        if (isAlarmShow) {
            holder.ivAlarm.setVisibility(View.VISIBLE);
        } else {
            holder.ivAlarm.setVisibility(View.GONE);
        }

        holder.ivAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消关注，不过要弹窗提示
                Session temp = mSesionBeans.get(position);

                ConferenceDbUtils.addAttentionToSession(temp.getSessionGroupId(), Constants.NOATTENTION);
                mSesionBeans.remove(position);
                //移除闹铃

                Alert tempAlert = ConferenceDbUtils.getAlertByAlertId(temp.getClassesId());
                if (tempAlert != null) {
                    ConferenceDbUtils.deleteAlert(tempAlert);
                }

                List<Integer> positionToRemove = new ArrayList<>();

                for(int j=0; j<mMeetingBeans.size(); j++) {
                    if(mMeetingBeans.get(j).getSessionGroupId() == temp.getSessionGroupId()) {
                        ConferenceDbUtils.addAttentionToMeeting(mMeetingBeans.get(j).getMeetingId(), Constants.NOATTENTION);
                        positionToRemove.add(j);
                    }
                }

                for(int i=0; i<positionToRemove.size(); i++) {
                    mMeetingBeans.remove(positionToRemove.get(i));
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvSessionName;
        TextView tvTime;
        TextView tvLocation;
        LinearLayout llMeetings;
        ImageView ivAlarm;
    }
}
