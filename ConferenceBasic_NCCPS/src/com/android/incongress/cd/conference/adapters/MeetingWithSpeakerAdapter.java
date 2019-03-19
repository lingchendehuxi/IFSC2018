package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.widget.flow_layout.FlowLayout;
import com.android.incongress.cd.conference.widget.flow_layout.TagFlowLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/7.
 * 会议详情中的meeting的适配器，包含了meeting名字和对应的专家包括专家的身份
 */
public class MeetingWithSpeakerAdapter extends BaseAdapter {
    private List<Meeting> mMeetings;
    private List<List<Speaker>> mSpeakers;
    private Context mContext;
    private boolean mIsAlarmMode = false;
    private OnTagListener mOnTagListner;
    private List<Role> mAllRoles;

    private SessionAlarmListener mSessionAlarmListener;
    private String room;

    public MeetingWithSpeakerAdapter(Context ctx, String room, List<Meeting> meetings, List<List<Speaker>> speakers, OnTagListener listener, SessionAlarmListener sessionListener) {
        this.mContext = ctx;
        this.room = room;
        this.mMeetings = meetings;
        this.mSpeakers = speakers;
        this.mOnTagListner = listener;
        this.mSessionAlarmListener = sessionListener;
        this.mAllRoles = ConferenceDbUtils.getAllRoles();
    }

    public List<Meeting> getMeetingBeanList() {
        return mMeetings;
    }

    public void setAlarmMode(boolean isAlarmMode) {
        this.mIsAlarmMode = isAlarmMode;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMeetings.size();
    }

    @Override
    public Object getItem(int position) {
        return mMeetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_meeeting_with_flow, null);
            holder.tvMeetingTime = (TextView) convertView.findViewById(R.id.tv_session_time);
            holder.tvMeetingName = (TextView) convertView.findViewById(R.id.tv_session_name);
            holder.tflNames = (TagFlowLayout) convertView.findViewById(R.id.tfl_names);
            holder.ivAlarm = (ImageView) convertView.findViewById(R.id.iv_alarm);
            holder.meet_alarm = convertView.findViewById(R.id.meet_alarm);
            holder.meet_order = convertView.findViewById(R.id.meet_order);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Meeting bean = mMeetings.get(position);

        //sp会议时间+会议名称
        final SpannableString sp;
        if (AppApplication.systemLanguage == 1) {
            sp = new SpannableString(bean.getTopic());
        } else {
            sp = new SpannableString(bean.getTopicEn());
        }
        //sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
        holder.tvMeetingName.setText(sp);
        holder.tvMeetingTime.setText(bean.getStartTime());
        //该会议下的所有的speaker
        if(mSpeakers.size()>0) {
            final List<Speaker> speakers = mSpeakers.get(position);

            List<Role> roles = new ArrayList<>();
            String[] roleIds = bean.getRoleId().split(",");

            setRolesBySpeakers(roles, speakers, roleIds);

            final SpeakerTagAdapter adapter = new SpeakerTagAdapter(mContext, speakers);
            adapter.setRoleName(roles);

            holder.tflNames.setAdapter(adapter);
            holder.tflNames.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    Speaker bean = adapter.getSpeakerList().get(position);
                    mOnTagListner.tagListener(bean);
                    return true;
                }
            });

        }
        if(mMeetings.get(position).getAttention() == 1){
            holder.meet_alarm.setVisibility(View.VISIBLE);
        }else {
            holder.meet_alarm.setVisibility(View.INVISIBLE);
        }
        if(mMeetings.get(position).isOrder()){
            holder.meet_order.setVisibility(View.VISIBLE);
        }else {
            holder.meet_order.setVisibility(View.INVISIBLE);
        }
        if (mIsAlarmMode) {
            holder.ivAlarm.setVisibility(View.VISIBLE);
        } else {
            holder.ivAlarm.setVisibility(View.GONE);
        }

        if (bean.getAttention() == Constants.ATTENTION) {
            holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmon);
        } else {
            holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
        }

        holder.ivAlarm.setTag(position);
        holder.ivAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                if (mMeetings.get(position).getAttention() == 0) {
                    mMeetings.get(position).setAttention(1);
                    holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmon);

                    //设置该meeting的关注，闹钟不用理会
                    ConferenceDbUtils.addAttentionToMeeting(bean.getMeetingId(), Constants.ATTENTION);

                } else {
                    mMeetings.get(position).setAttention(0);
                    holder.ivAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
                    ConferenceDbUtils.addAttentionToMeeting(bean.getMeetingId(), Constants.NOATTENTION);
                }
                //一个都没有关注
                boolean isAllAttetion = true;
                for (int i = 0; i < mMeetings.size(); i++) {
                    if (mMeetings.get(i).getAttention() == 0) {
                        isAllAttetion = false;
                        break;
                    }
                }

                if (isAllAttetion) {
                    mSessionAlarmListener.doWhenMeetingAlarmClicked(true);
                } else {
                    mSessionAlarmListener.doWhenMeetingAlarmClicked(false);
                }
                //添加闹钟
                if(mMeetings.get(position).getAttention() == 1){
                    if(!isAllAttetion){
                        Alert alertbean = new Alert();
                        alertbean.setDate(bean.getMeetingDay());
                        alertbean.setEnable(1);
                        alertbean.setEnd(bean.getEndTime());
                        alertbean.setRelativeid(String.valueOf(bean.getSessionGroupId()));
                        alertbean.setRepeatdistance("5");
                        alertbean.setRepeattimes("0");
                        alertbean.setRoom(room);
                        alertbean.setIdenId(bean.getMeetingId());
                        alertbean.setStart(bean.getStartTime());
                        alertbean.setTitle(bean.getTopic() + "#@#" + bean.getTopicEn());
                        alertbean.setType(AlertBean.TYPE_MEETING); //5代表会议提醒

                        alertbean.save();
                        AlermClock.addClock(alertbean);
                    }
                }else {
                    Alert alert = ConferenceDbUtils.getAlertByAlertId(bean.getMeetingId());
                    if(alert!=null){
                        AlermClock.disableClock(alert);
                    }
                }

            }
        });

        return convertView;
    }

    private void setRolesBySpeakers(List<Role> roles, List<Speaker> speakers, String[] roleIds) {
        for (int i = 0; i < speakers.size(); i++) {
            roles.add(i, getRoleBeanById(roleIds[i]));
        }
    }

    class ViewHolder {
        ImageView ivAlarm,meet_alarm,meet_order;
        TextView tvMeetingName,tvMeetingTime;
        TagFlowLayout tflNames;
    }

    public interface OnTagListener {
        void tagListener(Speaker bean);
    }

    public interface SessionAlarmListener {
        void doWhenMeetingAlarmClicked(boolean sessionAlarmToggle);
    }


    /**
     * 根据roleId获取身份信息
     *
     * @param roleId
     * @return
     */
    private Role getRoleBeanById(String roleId) {
        Role role = null;
        for (int i = 0; i < mAllRoles.size(); i++) {
            role = mAllRoles.get(i);
            if (roleId.equals(role.getRoleId() + "")) {
                return role;
            }
        }
        return role;
    }
}
