package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Jacky on 2017/1/3.
 */

public class QuestionSessionsAdapter extends RecyclerView.Adapter<QuestionSessionsAdapter.MeetingHolder> implements View.OnClickListener{
    private Context mContext;
    private List<Session> mAllSessions;
    private OnSessionClickListener mOnSessionClickListener;

    public QuestionSessionsAdapter(Context context, List<Session> sessions) {
        this.mContext = context;
        this.mAllSessions = sessions;
    }

    public void setSessionClickListener(OnSessionClickListener listener) {
        this.mOnSessionClickListener = listener;
    }

    @Override
    public MeetingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_question_by_meeting, parent, false);
        view.setOnClickListener(this);
        MeetingHolder holder = new MeetingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MeetingHolder holder, int position) {
        Session session = mAllSessions.get(position);

        holder.itemView.setTag(position);

        String tempFacultyId = session.getFacultyId();
        Speaker tempSpeaker = null;
        if (!TextUtils.isEmpty(tempFacultyId.trim())) {
            tempSpeaker = ConferenceDbUtils.getSpeakerById(tempFacultyId.split(",")[0]);
        }

        String time = session.getSessionDay() + " " + session.getStartTime() + "-" + session.getEndTime();
        Class tempClass = ConferenceDbUtils.findClassByClassId(session.getClassesId());

        LogUtils.println("Session[" + position + "]:" + session.getSessionName());
        if (AppApplication.systemLanguage == 1) {
            holder.tvSessionName.setText(session.getSessionName());
            if (null != tempSpeaker)
                holder.tvFacultyName.setText(tempSpeaker.getSpeakerName());
            if (null != tempClass)
                holder.tvRoom.setText(tempClass.getClassesCode());
        } else {
            holder.tvSessionName.setText(session.getSessionNameEN());
            if (null != tempSpeaker)
                holder.tvFacultyName.setText(tempSpeaker.getEnName());
            if (null != tempClass)
                holder.tvRoom.setText(tempClass.getClassCodeEn());
        }

        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return mAllSessions == null ? 0 : mAllSessions.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnSessionClickListener != null) {
            mOnSessionClickListener.onItemClick((int)v.getTag());
        }
    }

    class MeetingHolder extends RecyclerView.ViewHolder {
        TextView tvSessionName, tvFacultyName, tvTime, tvRoom;

        public MeetingHolder(View itemView) {
            super(itemView);
            tvSessionName = (TextView) itemView.findViewById(R.id.tv_session_name);
            tvFacultyName = (TextView) itemView.findViewById(R.id.tv_faculty_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvRoom = (TextView) itemView.findViewById(R.id.tv_room);
        }
    }

    public interface OnSessionClickListener{
        void onItemClick(int position);
    }
}
