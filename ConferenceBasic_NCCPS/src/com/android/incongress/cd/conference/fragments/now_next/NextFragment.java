package com.android.incongress.cd.conference.fragments.now_next;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.NextAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.popup.BasePopupWindow;
import com.android.incongress.cd.conference.widget.popup.ChooseRomPopupWindow;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jacky on 2016/12/13.
 */

public class NextFragment extends BaseFragment {
    //当前时间，以及选择的会议室
    private TextView mTvTime, mTvRoom;
    private String mCurrentTime, mCurrentRoom, mCurrentDay;
    private int[] mCurrenntHourAndMinute = new int[2]; //分别放当前的小时和分钟

    /**
     * 当前时间和选择的会议室
     */
    private RelativeLayout mLlTimeRoom;
    private ChooseRomPopupWindow mRoomPopupWindow;
    private List<Class> mAllClasses;
    private List<Class> mChooseClasses;

    /**
     * 正在进行的sssion列表
     **/
    private List<Session> mNextSessions;
    private List<Session> mNextSessionsByRoom;
    private List<Session> mNextSessionsByRoomAndTime;
    private Map<Integer, List<Meeting>> mMeetingsBySession;
    private String[] mRoleWithNamess;
    private SuperRecyclerView mRvNowSession;
    private NextAdapter mNextAdapter;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.fragment_next, container, false);
        mLlTimeRoom =  view.findViewById(R.id.ll_time_room);
        mTvTime =  view.findViewById(R.id.tv_time);
        mTvRoom =  view.findViewById(R.id.tv_room);

        mRvNowSession =  view.findViewById(R.id.srv_session);
        mChooseClasses = new ArrayList<>();

        mTvRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(mRoomPopupWindow == null) {
                        SharePreferenceUtils.saveAppBoolean("popup",false);
                        mRoomPopupWindow = new ChooseRomPopupWindow(getActivity());

                        mRoomPopupWindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                if(look){
                                    mChooseClasses = mRoomPopupWindow.getCurrentClass();
                                    getSessionsAndMeetings(mChooseClasses);
                                }
                            }
                        });
                    }else{
                        SharePreferenceUtils.saveAppBoolean("popup",false);
                    }
                    mRoomPopupWindow.showPopupWindowBelowView(mLlTimeRoom);
            }
        });

        getSessionsAndMeetings(null);
        return view;
    }
    private boolean look = true;

    @Override
    public void onStop() {
        super.onStop();
        look = false;
        if(mRoomPopupWindow != null){
            mRoomPopupWindow.dismiss();
        }
    }
    private void getSessionsAndMeetings(final List<Class> classes) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mRvNowSession.showProgress();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                //mTvTime.setText(getString(R.string.current_time, mCurrentTime));
                mTvTime.setText(mCurrentRoom);

                if (mMeetingsBySession.size() > 0 && mMeetingsBySession.size() == mNextSessionsByRoomAndTime.size()) {
                    mNextAdapter = new NextAdapter(mNextSessionsByRoomAndTime, mRoleWithNamess, mAllClasses, mMeetingsBySession, mCurrentDay, mCurrenntHourAndMinute, getActivity());
                    mRvNowSession.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRvNowSession.setAdapter(mNextAdapter);

                    mNextAdapter.setOnItemClickListener(new NextAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, Session session) {
                            int tartgetPosition = 0;
                            for (int i = 0; i < mNextSessionsByRoomAndTime.size(); i++) {
                                if (mNextSessionsByRoomAndTime.get(i).getSessionGroupId() == session.getSessionGroupId()) {
                                    tartgetPosition = i;
                                }
                            }

                            SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                            detail.setArguments(tartgetPosition, mNextSessionsByRoomAndTime);
                            action(detail, R.string.meeting_schedule_detail_title, null, false, false, false);
                        }
                    });
                } else {
                    mNextAdapter = new NextAdapter(mNextSessionsByRoomAndTime, mRoleWithNamess, mAllClasses, mMeetingsBySession, mCurrentDay, mCurrenntHourAndMinute, getActivity());
                    mRvNowSession.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRvNowSession.setAdapter(mNextAdapter);
                }
                mRvNowSession.showRecycler();
            }

            @Override
            protected Void doInBackground(Void... params) {
                //时间获取
                mCurrentDay = DateUtil.getNowDate(DateUtil.DEFAULT);
                mCurrenntHourAndMinute = getHourAndTime(DateUtil.getNowTime());
                mCurrentTime = mCurrenntHourAndMinute[0] + ":" + mCurrenntHourAndMinute[1] + " " + getPMorAM(mCurrenntHourAndMinute);
                mCurrentTime = DateUtil.getNowTime();

                //会议室获取
                mCurrentRoom = "";
                if (mChooseClasses == null || mChooseClasses.size() == 0) {
                    mCurrentRoom = getString(R.string.all_room);
                } else {
                    for (int i = 0; i < mChooseClasses.size(); i++) {
                        if(AppApplication.systemLanguage == 1){
                            mCurrentRoom = mCurrentRoom + "，" + mChooseClasses.get(i).getClassesCode();
                        }else {
                            mCurrentRoom = mCurrentRoom + "，" + mChooseClasses.get(i).getClassCodeEn();
                        }
                    }
                    mCurrentRoom = mCurrentRoom.substring(1);
                }

                //所有房间获取
                mAllClasses = ConferenceDbUtils.getAllClasses();

                mNextSessions = ConferenceDbUtils.getSessionsBySessionDayOrderByClassIdAndStartTime(mCurrentDay);
                mNextSessionsByRoom = new ArrayList<>();

                if (classes != null && classes.size() > 0) {
                    for (int i = 0; i < mNextSessions.size(); i++) {
                        Session session = mNextSessions.get(i);
                        boolean isInClass = false;
                        for (int j = 0; j < classes.size(); j++) {
                            if (session.getClassesId() == classes.get(j).getClassesId()) {
                                isInClass = true;
                                break;
                            }
                        }
                        if (isInClass) {
                            mNextSessionsByRoom.add(session);
                        }
                    }
                } else {
                    mNextSessionsByRoom.addAll(mNextSessions);
                }

                //根据具体时间点栓选
                mNextSessionsByRoomAndTime = new ArrayList<>();
                for (int i = 0; i < mNextSessionsByRoom.size(); i++) {
                    Session session = mNextSessionsByRoom.get(i);
                    if (isInTime(mCurrenntHourAndMinute, getHourAndTime(session.getStartTime()), getHourAndTime(session.getEndTime())) && !isContainSameRoom(session)) {
                        mNextSessionsByRoomAndTime.add(session);
                    }
                }

                //只留下第一个session
                if (mNextSessionsByRoomAndTime != null && mNextSessionsByRoomAndTime.size() >= 0) {
                    mMeetingsBySession = new HashMap<>();
                    mRoleWithNamess = new String[mNextSessionsByRoomAndTime.size()];

                    for (int i = 0; i < mNextSessionsByRoomAndTime.size(); i++) {
                        Session session = mNextSessionsByRoomAndTime.get(i);

                        List<Meeting> meetingBySessionGroupId = ConferenceDbUtils.getMeetingBySessionGroupId(session.getSessionGroupId() + "");
                        mMeetingsBySession.put(i, meetingBySessionGroupId);

                        String[] facultyIds = session.getFacultyId().split(",");
                        String[] roleIds = session.getRoleId().split(",");

                        if (!TextUtils.isEmpty(session.getFacultyId()) && facultyIds.length > 0 && facultyIds.length == roleIds.length) {
                            //保存讲者信息，并保存对应讲者的身份类型
                            List<Speaker> speakers = new ArrayList<>();
                            for (int j = 0; j < facultyIds.length; j++) {
                                Speaker temp = new Speaker();
                                String facultyId = facultyIds[j];
                                String roleId = roleIds[j];
                                temp.setSpeakerId(Integer.parseInt(facultyId));
                                temp.setType(Integer.parseInt(roleId));
                                Speaker searchSpeaker = ConferenceDbUtils.getSpeakerById(facultyId);
                                if(searchSpeaker == null)
                                    continue;
                                temp.setSpeakerName(searchSpeaker.getSpeakerName());
                                speakers.add(temp);
                            }

                            //获取唯一，不重复的身份类型
                            Set<String> noRepeatRolesSet = new HashSet<>();
                            for (int j = 0; j < roleIds.length; j++) {
                                noRepeatRolesSet.add(roleIds[j]);
                            }
                            String[] noRepeatRoles = noRepeatRolesSet.toArray(new String[noRepeatRolesSet.size()]);

                            //将对应身份的人加入
                            for (int j = 0; j < noRepeatRoles.length; j++) {
                                String roleId = noRepeatRoles[j];
                                Role role = ConferenceDbUtils.getRoleById(roleId);
                                if(role == null)
                                    continue;
                                String roleWithName;
                                if(AppApplication.systemLanguage == 1||TextUtils.isEmpty(role.getEnName())){
                                    roleWithName = role.getName() + ":";
                                }else {
                                    roleWithName = role.getEnName() + ":";
                                }
                                for (int z = 0; z < speakers.size(); z++) {
                                    Speaker speaker = speakers.get(z);
                                    if (roleId.equals(speaker.getType() + "")) {
                                        if (roleWithName.endsWith(":")) {
                                            roleWithName = roleWithName + speakers.get(z).getSpeakerName();
                                        } else {
                                            roleWithName = roleWithName + "," + speakers.get(z).getSpeakerName();
                                        }
                                    }
                                }

                                if (TextUtils.isEmpty(mRoleWithNamess[i])) {
                                    mRoleWithNamess[i] = roleWithName;
                                } else {
                                    mRoleWithNamess[i] = mRoleWithNamess[i] + "#@#" + roleWithName;
                                }
                            }
                        } else {
                            //没有就放空字符串
                            mRoleWithNamess[i] = "";
                        }
                    }
                } else {

                }

                return null;
            }
        }.execute();
    }

    /**
     * 获取当前时间，并存入int数组中
     *
     * @param time
     * @return
     */
    private int[] getHourAndTime(String time) {
        int[] tempHourAndMinute = {0, 0};
        if (TextUtils.isEmpty(time))
            return tempHourAndMinute;
        try {
            String hour = time.substring(0, 2);
            if (!TextUtils.isEmpty(hour)) {
                int tempHour = Integer.parseInt(hour);
                tempHourAndMinute[0] = tempHour;
            } else {
                tempHourAndMinute[0] = 0;
            }
            String minute = time.substring(3, 5);
            if (!TextUtils.isEmpty(minute)) {
                int tempMinute = Integer.parseInt(minute);
                tempHourAndMinute[1] = tempMinute;
            } else {
                tempHourAndMinute[1] = 0;
            }
        } catch (Exception e) {
            LogUtils.println("time parse error:" + e.getMessage());
        }

        return tempHourAndMinute;
    }

    /**
     * 获取时间是AM还是PM
     *
     * @param hourAndMinute
     * @return
     */
    private String getPMorAM(int[] hourAndMinute) {
        int hour = hourAndMinute[0];
        if (hour >= 12) {
            return "PM";
        }
        return "AM";
    }

    /**
     * 判断是否在指定时间范围内
     *
     * @param currentHourAndMinute
     * @param startHourAndMinute
     * @param endHourAndMinute
     * @return
     */
    private boolean isInTime(int[] currentHourAndMinute, int[] startHourAndMinute, int[] endHourAndMinute) {
        Date currentData = DateUtil.getDate(mCurrentDay + " " + currentHourAndMinute[0] + ":" + currentHourAndMinute[1], DateUtil.DEFAULT_MINUTE);
        Date startData = DateUtil.getDate(mCurrentDay + " " + startHourAndMinute[0] + ":" + startHourAndMinute[1], DateUtil.DEFAULT_MINUTE);
        Date endData = DateUtil.getDate(mCurrentDay + " " + endHourAndMinute[0] + ":" + endHourAndMinute[1], DateUtil.DEFAULT_MINUTE);

//        if(currentData.getTime() >= startData.getTime() && currentData.getTime() <= endData.getTime())
//            return true;
        if (startData.getTime() > currentData.getTime())
            return true;

        return false;
    }

    /**
     * 是否有相同的room的session
     *
     * @return
     */
    private boolean isContainSameRoom(Session session) {
        if (mNextSessionsByRoomAndTime == null || mNextSessionsByRoomAndTime.size() == 0)
            return false;

        boolean isContain = false;
        for (int i = 0; i < mNextSessionsByRoomAndTime.size(); i++) {
            if (session.getClassesId() == mNextSessionsByRoomAndTime.get(i).getClassesId()) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_NEXTFRAGMENT);
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_NEXTFRAGMENT);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
