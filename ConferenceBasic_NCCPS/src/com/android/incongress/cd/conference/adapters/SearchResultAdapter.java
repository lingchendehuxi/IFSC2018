package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

public class SearchResultAdapter extends BaseAdapter {

    private Context mContext;
    private List<Session> mSesionBeans;
    private List<Meeting> mMeetingBeans;
    private List<Class> mClassesBeans;

    public SearchResultAdapter(Context context, List<Session> sesionBeans, List<Meeting> meetingBeans, List<Class> classesBeans) {
        this.mContext = context;
        this.mSesionBeans = sesionBeans;
        this.mMeetingBeans = meetingBeans;
        this.mClassesBeans = classesBeans;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_search_schedule, null);
            holder = new ViewHolder();
            holder.tvSessionName = convertView.findViewById(R.id.tv_session_name);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            holder.tvLocation = convertView.findViewById(R.id.tv_location);
            holder.llMeetings = convertView.findViewById(R.id.ll_time_and_meeting);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Session session = mSesionBeans.get(position);
        if (AppApplication.systemLanguage == 1) {
            holder.tvSessionName.setText(session.getSessionName());
        } else {
            holder.tvSessionName.setText(session.getSessionNameEN());
        }

        holder.tvTime.setText(session.getStartTime() + " - " + session.getEndTime());

        for (int i = 0; i < mClassesBeans.size(); i++) {
            if (session.getClassesId() == mClassesBeans.get(i).getClassesId()) {
                if (AppApplication.systemLanguage == 1) {
                    holder.tvLocation.setText(mClassesBeans.get(i).getClassesCode());
                } else {
                    holder.tvLocation.setText(mClassesBeans.get(i).getClassCodeEn());
                }
                break;
            }
        }

        holder.llMeetings.removeAllViews();
        for (int i = 0; i < mMeetingBeans.size(); i++) {
            if (mMeetingBeans.get(i).getSessionGroupId() == session.getSessionGroupId()) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_meeting_and_time, null);
                if (AppApplication.systemLanguage == 1) {
                    ((TextView) view).setText(mMeetingBeans.get(i).getStartTime() + " - " + mMeetingBeans.get(i).getTopic());
                } else {
                    ((TextView) view).setText(mMeetingBeans.get(i).getStartTime() + " - " + mMeetingBeans.get(i).getTopicEn());
                }

                holder.llMeetings.addView(view);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvSessionName;
        TextView tvTime;
        TextView tvLocation;
        LinearLayout llMeetings;
    }
}
