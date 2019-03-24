package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MeetingWithSpeakerAdapter;
import com.android.incongress.cd.conference.adapters.SpeakerTagAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.FacultyBean;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerDetailFragment;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ShareUtils;
import com.android.incongress.cd.conference.widget.ListViewForFix;
import com.android.incongress.cd.conference.widget.MyButton;
import com.android.incongress.cd.conference.widget.ScrollControlViewpager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.flow_layout.FlowLayout;
import com.android.incongress.cd.conference.widget.flow_layout.TagFlowLayout;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jacky on 2015/12/16.
 * Session详情页
 */
public class SessionDetailPageFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvScheduleTime, mTvRoom, mTvScheduleName, mTvScheduleDetailTime;
    private LinearLayout mLlSpeakerContainer;
    private ListViewForFix mLvMeetings;
    private ScrollView mScrollview;
    private MyButton mBtLocation, mBtAlarm;

    private Session mSessionBean;
    private Class mClassBean;
    private List<FacultyBean> mFacultyBeanList = new ArrayList<>();
    private String mFacultyIds, mRoles;

    private List<Speaker> mSpeakerList = new ArrayList<>();
    private List<Role> mRoleList = new ArrayList<>();
    private List<Meeting> mMeetingBeanList = new ArrayList<>();
    private List<List<Speaker>> mAllSpeakers = new ArrayList<>();

    //将需要查询的数据集合放进来
    private List<Class> mClasses = new ArrayList<>();
    private List<Role> mRoleListAll = new ArrayList<>();

    private ImageView mIvSessionAlarm,mShareSession;
    private ProgressBar mLoadingBar;

    public static final String BUNDLE_SESSION_ID = "sessionID";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.println("SessionDetail onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 闹铃模式
     **/
    private boolean mIsAlarmMode = false;
    private MeetingWithSpeakerAdapter mMeetingWithSpeakerAdapter;
    private ScrollControlViewpager mViewPager;

    public SessionDetailPageFragment() {
    }

    public static SessionDetailPageFragment getInstance(int sessionId) {
        SessionDetailPageFragment fragment = new SessionDetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_SESSION_ID, sessionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mSessionid = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.fragment_session_detail_page, container, false);
        try {
            mSessionid = getArguments().getInt(BUNDLE_SESSION_ID);
        } catch (Exception e) {
            mSessionBean = null;
            mClassBean = null;
            mClasses = null;
        }
        mTvScheduleTime =  view.findViewById(R.id.tv_schedule_time);
        mTvRoom =  view.findViewById(R.id.tv_schedule_room);
        mTvScheduleName =  view.findViewById(R.id.tv_schedule_name);
        mTvScheduleDetailTime =  view.findViewById(R.id.tv_schedule_detail_time);
        mLlSpeakerContainer =  view.findViewById(R.id.ll_speaker_container);
        mLvMeetings =  view.findViewById(R.id.lv_meetings);
        mScrollview =  view.findViewById(R.id.scrollview);
        mBtLocation =  view.findViewById(R.id.bt_location);
        mBtAlarm =  view.findViewById(R.id.bt_alarm);
        mIvSessionAlarm =  view.findViewById(R.id.iv_session_alarm);
        mShareSession = view.findViewById(R.id.share_session);
        mShareSession.setOnClickListener(this);
        mLoadingBar =  view.findViewById(R.id.progressbar);

        registerMessageReceiver();
        new MyAsynkTask().execute();

        //      showGuideInfo(); 引导页暂时去除
        startPostponedEnterTransition();
        return view;
    }

    public void setViewPager(ScrollControlViewpager pager) {
        this.mViewPager = pager;
    }

    private void initEvents() {
        mBtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsAlarmMode) {
                    MeetingScheduleRoomLocationActionFragment fragment = new MeetingScheduleRoomLocationActionFragment();
                    fragment.setRoomBean(mClassBean, null, MeetingScheduleRoomLocationActionFragment.TYPE_MEETING);
                    action(fragment, R.string.meeting_schedule_room_location, false, false, false);
                }
            }
        });

        mBtAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsAlarmMode) {
                    //进入闹铃模式
                    mIsAlarmMode = true;
                    mViewPager.setCanScroll(false);
                    mBtAlarm.setText(R.string.mymeeting_finish);
                    mIvSessionAlarm.setVisibility(View.VISIBLE);
                    mMeetingWithSpeakerAdapter.setAlarmMode(true);
                } else {
                    //完成闹铃设置
                    mIvSessionAlarm.setVisibility(View.GONE);
                    mMeetingWithSpeakerAdapter.setAlarmMode(false);
                    mIsAlarmMode = false;
                    mViewPager.setCanScroll(true);
                    mBtAlarm.setText(R.string.meeting_schedule_alarm);
                }
            }
        });

        mIvSessionAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIvSessionAlarm.getVisibility() == View.VISIBLE) {
                    if (mSessionBean.getAttention() == Constants.ATTENTION) {
                        mSessionBean.setAttention(Constants.NOATTENTION);
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);

                        List<Meeting> meetings = mMeetingWithSpeakerAdapter.getMeetingBeanList();
                        for (int i = 0; i < meetings.size(); i++) {
                            meetings.get(i).setAttention(Constants.NOATTENTION);
                        }

                        mMeetingWithSpeakerAdapter.notifyDataSetChanged();

                        //取消Session的关注,如果有闹钟则移除闹钟
                        ConferenceDbUtils.addAttentionToSession(mSessionBean.getSessionGroupId(), Constants.NOATTENTION);
                        unEnableSessionClick();
                        Log.d("sgqTest", "onClick: 删除session闹钟");

                        //取消meeting的关注
                        for (int i = 0; i < mMeetingWithSpeakerAdapter.getMeetingBeanList().size(); i++) {
                            Meeting bean = mMeetingWithSpeakerAdapter.getMeetingBeanList().get(i);
                            ConferenceDbUtils.addAttentionToMeeting(bean.getMeetingId(), Constants.NOATTENTION);
                        }
                    } else {
                        mSessionBean.setAttention(Constants.ATTENTION);
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmon);

                        //全部都关注起来
                        List<Meeting> meetings = mMeetingWithSpeakerAdapter.getMeetingBeanList();
                        for (int i = 0; i < meetings.size(); i++) {
                            meetings.get(i).setAttention(Constants.ATTENTION);
                        }

                        mMeetingWithSpeakerAdapter.notifyDataSetChanged();

                        //关注该session，包括下属所有Meeting,添加闹钟
                        ConferenceDbUtils.addAttentionToSession(mSessionBean.getSessionGroupId(), Constants.ATTENTION);

                        enableSessionClick();
                        Log.d("sgqTest", "onClick: 添加session闹钟");

                        //设置meeting的关注
                        for (int i = 0; i < mMeetingWithSpeakerAdapter.getMeetingBeanList().size(); i++) {
                            Meeting temp = mMeetingWithSpeakerAdapter.getMeetingBeanList().get(i);
                            ConferenceDbUtils.addAttentionToMeeting(temp.getMeetingId(), Constants.ATTENTION);
                        }
                    }
                }
            }
        });
    }

    /**
     * 根据speakerId获取SpeakerBean对象
     *
     * @param speakerId
     * @return
     */
    public static Speaker getSpeakerById(String speakerId) {
        if ("".equals(speakerId)) {
            return null;
        }
        return ConferenceDbUtils.getSpeakerById(speakerId);
    }

    /**
     * 根据sessionGroupId去查找所有的meetingId
     *
     * @param sessionGroupId
     */
    private void getMeetingBeanBySessionId(String sessionGroupId) {
        mMeetingBeanList.addAll(ConferenceDbUtils.getMeetingBySessionGroupId(sessionGroupId));
    }

    //老方法
    //查找一个人的所有身份类型和相对应类型下的meeting或者session
    //先查session表：session -- > roleId
    //查出所有的session  --> HashMap<Integer,List<MeetingBean>>
    //先把roleId放入integer中，如果有了就不加，然后把相应的session放到对应key的List<MeetingBean>中

    //首先得到这个人的speakerId，这个是唯一的，然后根据这个speakerId分别去session表和meeting表找对应的session和meeting然后存到队列中，最后返还给需要的数据
    private List<Integer> mRoleIds = new ArrayList<>();
    private List<MeetingBean_new> mAllMeetings = new ArrayList<>();
    private List<MeetingBean_new> mAllMeetings_sorted = new ArrayList<>();

    private void getAllSessionAndMeetingBySpeakerId(int speakerId) {
        //获得了所有的身份类型
        mRoleIds.clear();
        mAllMeetings.clear();
        mAllMeetings_sorted.clear();

        List<Session> sessions = new ArrayList<>();
        sessions.addAll(ConferenceDbUtils.getSessionBySpeakerId(speakerId + ""));

        //获取该speakerId下的所有presentation级别会议
        List<Meeting> meetings = new ArrayList<>();
        meetings.addAll(ConferenceDbUtils.getMeetingBySpeakerId(speakerId + ""));

        //设置每个session下对应的身份类型
        for (int i = 0; i < sessions.size(); i++) {
            MyLogger.jLog().i(sessions.get(i).toString());
            Session bean = sessions.get(i);
            String roleId = bean.getRoleId();

            int rolePosition = 0;
            String[] speakerIds = bean.getFacultyId().split(",");
            for (int j = 0; j < speakerIds.length; j++) {
                if (speakerIds[j].equals(speakerId + "")) {
                    rolePosition = j;
                    break;
                }
            }

            String[] roleIds = roleId.split(",");
            String tempRoleId = roleIds[rolePosition];
            if (!mRoleIds.contains(Integer.parseInt(tempRoleId))) {
                mRoleIds.add(Integer.parseInt(tempRoleId));
            }

            MeetingBean_new newBean = new MeetingBean_new(bean.getSessionGroupId(), bean.getSessionName(), bean.getSessionDay(),
                    bean.getStartTime(), bean.getEndTime(), bean.getClassesId(), bean.getSessionGroupId(), bean.getSessionNameEN(),
                    Integer.parseInt(tempRoleId), getRoleNameById(tempRoleId).getName(), getRoleNameById(tempRoleId).getEnName(),
                    getClassNameByClassId(bean.getClassesId()).getClassesCode(), getClassNameByClassId(bean.getClassesId()).getClassCodeEn(), 1);
            mAllMeetings.add(newBean);
        }

        for (int i = 0; i < meetings.size(); i++) {
            Meeting bean = meetings.get(i);
            String roleId = bean.getRoleId();

            int rolePosition = 0;
            String[] speakerIds = bean.getFacultyId().split(",");
            for (int j = 0; j < speakerIds.length; j++) {
                if (speakerIds[j].equals(speakerId + "")) {
                    rolePosition = j;
                    break;
                }
            }

            String[] roleIds = roleId.split(",");
            String tempRoleId = roleIds[rolePosition];

            if (!mRoleIds.contains(Integer.parseInt(tempRoleId))) {
                mRoleIds.add(Integer.parseInt(tempRoleId));
            }

            MeetingBean_new newBean = new MeetingBean_new(bean.getMeetingId(), bean.getTopic(),
                    bean.getMeetingDay(), bean.getStartTime(), bean.getEndTime(), bean.getClassesId(),
                    bean.getSessionGroupId(), bean.getTopicEn(), Integer.parseInt(tempRoleId),
                    getRoleNameById(tempRoleId).getName(), getRoleNameById(tempRoleId).getEnName(),
                    getClassNameByClassId(bean.getClassesId()).getClassesCode(),
                    getClassNameByClassId(bean.getClassesId()).getClassCodeEn(), 2);
            mAllMeetings.add(newBean);
        }

        //根据id将meeting重新排序
        Collections.sort(mRoleIds);

        //调整会议的顺序
        for (int i = 0; i < mRoleIds.size(); i++) {
            int roleId = mRoleIds.get(i);
            for (int j = 0; j < mAllMeetings.size(); j++) {
                if (mAllMeetings.get(j).getRoleId() == roleId) {
                    mAllMeetings_sorted.add(mAllMeetings.get(j));
                }
            }
        }
    }

    class MyAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mSessionBean.getAttention() == 1) {
                mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmon);
            } else {
                mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
            }

            if (AppApplication.systemLanguage == 1) {
                mTvScheduleTime.setText(DateUtil.getDateWithWeek(DateUtil.getDate(mSessionBean.getSessionDay(), DateUtil.DEFAULT)));
                mTvRoom.setText(mClassBean.getClassesCode());
                mTvScheduleName.setText(mSessionBean.getSessionName());
            } else {
                mTvScheduleTime.setText(DateUtil.getDateWithWeekInEnglish(DateUtil.getDate(mSessionBean.getSessionDay(), DateUtil.DEFAULT)));
                mTvRoom.setText(mClassBean.getClassCodeEn());
                mTvScheduleName.setText(mSessionBean.getSessionNameEN());
            }

            mTvScheduleDetailTime.setText(mSessionBean.getStartTime() + "-" + mSessionBean.getEndTime());


            mMeetingWithSpeakerAdapter = new MeetingWithSpeakerAdapter(getActivity(), mClassBean.getClassesCode(), mMeetingBeanList, mAllSpeakers, new MeetingWithSpeakerAdapter.OnTagListener() {
                @Override
                public void tagListener(Speaker bean) {
                    if (!mIsAlarmMode) {
                        //查出这个人所有的meeting內容
                        getAllSessionAndMeetingBySpeakerId(bean.getSpeakerId());
                        SpeakerDetailFragment speaker = new SpeakerDetailFragment();
                        speaker.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), bean.getImg(), mAllMeetings_sorted, true);
                        action(speaker, R.string.speaker_detial, null, false, false, false);
                    }
                }
            }, new MeetingWithSpeakerAdapter.SessionAlarmListener() {
                @Override
                public void doWhenMeetingAlarmClicked(boolean sessionAlarmToggle) {
                    if (sessionAlarmToggle) {
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmon);
                        mSessionBean.setAttention(Constants.ATTENTION);
                        ConferenceDbUtils.deleteAllMeetAlert(mSessionBean.getSessionGroupId());
                        Log.d("sgqTest", "doWhenMeetingAlarmClicked: 删除和session有关的meeting闹钟");
                        enableSessionClick();
                        Log.d("sgqTest", "doWhenMeetingAlarmClicked: 添加session闹钟");

                    } else {
                        mIvSessionAlarm.setImageResource(R.drawable.sessiondetail_alarmoff);
                        mSessionBean.setAttention(Constants.NOATTENTION);
                        Alert alert = ConferenceDbUtils.getAlertByAlertId(mSessionBean.getClassesId());
                        if (alert != null) {
                            Log.d("sgqTest", "doWhenMeetingAlarmClicked: 删除session闹钟");
                            AlermClock.disableClock(alert);
                        }
                    }
                }
            });

            mLvMeetings.setAdapter(mMeetingWithSpeakerAdapter);
            mLvMeetings.setFocusable(false);
            mScrollview.smoothScrollTo(0, 20);

            mLvMeetings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String summary = mMeetingBeanList.get(position).getSummary();
                    if (!TextUtils.isEmpty(summary)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(summary).setPositiveButton(R.string.positive_button, null).setCancelable(true).show();
                    }
                }
            });

            mLoadingBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //开始操作之前，初始化数据，存在刷新重复加载的问题
            mAllSpeakers.clear();
            mSpeakerList.clear();
            mFacultyBeanList.clear();
        }


        @Override
        protected Void doInBackground(Void... params) {
            mSessionBean = ConferenceDbUtils.getSessionBySessionId(mSessionid + "");
            mClassBean = ConferenceDbUtils.findClassByClassId(mSessionBean.getClassesId());
            mClasses = ConferenceDbUtils.getAllClasses();
            mRoleListAll = ConferenceDbUtils.getAllRoles();

            mFacultyIds = mSessionBean.getFacultyId();
            mRoles = mSessionBean.getRoleId();

            initEvents();

            //设置所有该session下的meeting
            getMeetingBeanBySessionId(String.valueOf(mSessionBean.getSessionGroupId()));

            String[] facults = mFacultyIds.split(",");
            String[] roles = mRoles.split(",");

            //获取所有的speaker，包括演讲者id和身份id
            for (int i = 0; i < facults.length; i++) {
                FacultyBean bean = new FacultyBean();
                bean.setFacultyId(facults[i]);
                bean.setRoleId(roles[i]);
                mFacultyBeanList.add(bean);
            }

            for (int i = 0; i < mFacultyBeanList.size(); i++) {
                FacultyBean bean = mFacultyBeanList.get(i);

                if ("".equals(bean.getFacultyId()))
                    continue;

                Speaker speaker = ConferenceDbUtils.getSpeakerById(bean.getFacultyId());

                if (speaker == null)
                    continue;

                Role role = getRoleNameById(bean.getRoleId());
                speaker.setType(Integer.parseInt(bean.getRoleId()));
                mSpeakerList.add(speaker);

                boolean isRoleExist = false;
                for (int j = 0; j < mRoleList.size(); j++) {
                    if (role.getRoleId() == mRoleList.get(j).getRoleId()) {
                        isRoleExist = true;
                    }
                }
                if (!isRoleExist) {
                    mRoleList.add(role);
                }
            }

            //添加对应身份类型和对应的人员
            for (int i = 0; i < mRoleList.size(); i++) {
                final int temp = i;
                List<Speaker> speakers = new ArrayList<>();
                for (int j = 0; j < mSpeakerList.size(); j++) {
                    if (mSpeakerList.get(j).getType() == mRoleList.get(i).getRoleId()) {
                        speakers.add(mSpeakerList.get(j));
                    }
                }

                final SpeakerTagAdapter speakerAdapter = new SpeakerTagAdapter(getActivity(), speakers);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //加上身份类型和，相应身份类型下面的人民
                            View roleView = LayoutInflater.from(AppApplication.getContext()).inflate(R.layout.speaker_flowlayout, null);
                            if (AppApplication.systemLanguage == 1) {
                                ((TextView) roleView.findViewById(R.id.tv_role_name)).setText(mRoleList.get(temp).getName());
                            } else {
                                ((TextView) roleView.findViewById(R.id.tv_role_name)).setText(mRoleList.get(temp).getEnName());
                            }
                            final TagFlowLayout flowLayout = roleView.findViewById(R.id.speaker_by_role);

                            mLlSpeakerContainer.addView(roleView);

                            flowLayout.setAdapter(speakerAdapter);
                            flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    if (!mIsAlarmMode) {
                                        Speaker bean = speakerAdapter.getSpeakerList().get(position);

                                        //查出这个人所有的meeting內容
                                        getAllSessionAndMeetingBySpeakerId(bean.getSpeakerId());
                                        SpeakerDetailFragment speaker = new SpeakerDetailFragment();
                                        speaker.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), bean.getImg(), mAllMeetings_sorted, true);

                                        ImageView askView = (ImageView) CommonUtils.initView(getActivity(), R.layout.title_right_image);
                                        askView.setImageResource(R.drawable.scenicshow_ask_professor);
                                        speaker.setRightView(askView);
                                        action(speaker, R.string.speaker_detial, askView, false, false, false);
                                    }
                                    return true;
                                }
                            });
                        }
                    });
                }
            }

            for (int i = 0; i < mMeetingBeanList.size(); i++) {
                String sps = mMeetingBeanList.get(i).getFacultyId();
                String[] speaker_ = sps.split(",");

                List<Speaker> speakersList = new ArrayList<>();

                for (int j = 0; j < speaker_.length; j++) {
                    Speaker bean = getSpeakerById(speaker_[j]);
                    if (bean != null)
                        speakersList.add(bean);
                }

                mAllSpeakers.add(speakersList);
            }
            return null;
        }
    }

    private Role getRoleNameById(String roleId) {
        for (int i = 0; i < mRoleListAll.size(); i++) {
            if (mRoleListAll.get(i).getRoleId() == Integer.parseInt(roleId)) {
                return mRoleListAll.get(i);
            }
        }

        return null;
    }

    private Class getClassNameByClassId(int classId) {
        Class classBean = null;
        for (int i = 0; i < mClasses.size(); i++) {
            if (mClasses.get(i).getClassesId() == classId) {
                classBean = mClasses.get(i);
            }
        }
        return classBean;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_session:
                //分享
                String shareTitle;
                if (AppApplication.systemLanguage == 1) {
                    shareTitle = mSessionBean.getSessionName();
                } else {
                    shareTitle = mSessionBean.getSessionNameEN();
                }

                //http://app.incongress.cn/chyWebApp/assetsCit/session_detail.jsp?sessionId=38105&conId=194&isShare=1

                //ShareUtils.shareTextWithUrl(getActivity(), shareTitle, "", Constants.APP_DOWNLOAD_SITE, null);
                ShareUtils.shareTextWithUrl(getActivity(), shareTitle, "会议日程",
                        "http://app.incongress.cn/chyWebApp/assetsCsc/session_detail.jsp?sessionId=" + mSessionBean.getSessionGroupId() + "&conId=" + Constants.getConId() + "&isShare=1", null);
                //252
                break;
        }
    }

    /**
     * 显示指示页
     */
    private void showGuideInfo() {
        if (!"1".equals(SharePreferenceUtils.getAppString(Constants.GUIDE_SESSION))) {
            getActivity().findViewById(R.id.home_guide).setVisibility(View.VISIBLE);
            ((ImageView) getActivity().findViewById(R.id.home_guide)).setImageResource(R.drawable.session_guide);
            (getActivity().findViewById(R.id.home_guide)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().findViewById(R.id.home_guide).setVisibility(View.GONE);
                    SharePreferenceUtils.saveAppString(Constants.GUIDE_SESSION, "1");
                }
            });
        }
    }

    //调用session订闹钟
    private void enableSessionClick() {
        Alert alertbean = new Alert();
        alertbean.setDate(mSessionBean.getSessionDay());
        alertbean.setEnable(1);
        alertbean.setEnd(mSessionBean.getEndTime());
        alertbean.setRelativeid(String.valueOf(mSessionBean.getSessionGroupId()));
        alertbean.setRepeatdistance("5");
        alertbean.setRepeattimes("0");
        alertbean.setIdenId(mSessionBean.getClassesId());
        alertbean.setRoom(mClassBean.getClassesCode());
        alertbean.setStart(mSessionBean.getStartTime());
        alertbean.setTitle(mSessionBean.getSessionName() + "#@#" + mSessionBean.getSessionNameEN());
        alertbean.setType(AlertBean.TYPE_SESSTION); //5代表session提醒

        ConferenceDbUtils.addAlert(alertbean);

        Alert bean = ConferenceDbUtils.getAlertByAlertId(mSessionBean.getClassesId());
        if (bean != null)
            AlermClock.addClock(bean);
    }

    //取消用session订闹钟
    private void unEnableSessionClick() {
        Alert alertForSession = ConferenceDbUtils.getAlertByAlertId(mSessionBean.getClassesId());
        if (alertForSession != null) {
            AlermClock.disableClock(alertForSession);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mSessionBean = ConferenceDbUtils.getSessionBySessionId(mSessionid + "");
        MobclickAgent.onPageStart(Constants.FRAGMENT_SESSIONDETAIL+mSessionBean.getSessionName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SESSIONDETAIL+mSessionBean.getSessionName());
    }

    private UpdateReceiver updateReceiver;
    //更新meeting广播接收器
    class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_UPDATE_MEET.equals(intent.getAction())) {
                if(mMeetingWithSpeakerAdapter!=null){
                    mMeetingBeanList.clear();
                    getMeetingBeanBySessionId(String.valueOf(mSessionBean.getSessionGroupId()));
                    mMeetingWithSpeakerAdapter.notifyDataSetChanged();
                }
            }
        }

    }

    //注册更新meet广播接收器
    public void registerMessageReceiver() {
        updateReceiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_UPDATE_MEET);
        getActivity().registerReceiver(updateReceiver, filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateReceiver != null) {
            getActivity().unregisterReceiver(updateReceiver);
        }
    }
}
