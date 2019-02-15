package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.LoginActivity;
import com.android.incongress.cd.conference.adapters.AskQuestionAdapter;
import com.android.incongress.cd.conference.adapters.OrderMeetingAdapter;
import com.android.incongress.cd.conference.adapters.SessionDetailViewPagerAdapter;
import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.AskPupBean;
import com.android.incongress.cd.conference.fragments.meeting_guide.NewMeetingInfoFragment;
import com.android.incongress.cd.conference.fragments.question.MakeQuestionFragment;
import com.android.incongress.cd.conference.model.Alert;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Note;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.ScrollControlViewpager;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.popup.QuestionPopupWindow;
import com.android.incongress.cd.conference.widget.stop_shake.EventListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2015/12/14.
 * 会议详情 目前有一个viewpager和页码标识
 */
public class SessionDetailViewPageFragment extends BaseFragment implements View.OnClickListener {
    private ScrollControlViewpager mViewPager;

    private RelativeLayout mSessionDetail;
    private SessionDetailViewPagerAdapter mAdapter;
    private CharSequence[] Titles;
    private LinearLayout mll_bottom_session, ll_study, ll_date, ll_question, ll_location, ll_note;
    private QuestionPopupWindow mQuestionPopupWindow; //提问
    private QuestionPopupWindow mMeetingPopupWindow;  //会议预约
    private QuestionPopupWindow mAlarmPopupWindow;  //闹钟提醒
    private static int SHOWWHAT = 100;
    private AskPupBean askBean;
    private List<String> meetingTopic;
    private List<Meeting> alarmList;
    private static float ScreenHeightLPercent = 0.35f;
    private static float ScreenHeightHPercent = 0.45f;
    private float fixHeight;
    private Class mClassBean;

    public SessionDetailViewPageFragment() {
    }

    private List<Session> mSessionBeanList = new ArrayList<>();

    private SoundPool sPool;
    public static int mPosition = 0;

    private List<SessionDetailPageFragment> mSessionDetailFragments = new ArrayList<>();

    public void setArguments(int position, List<Session> sessionBeanList) {
        this.mSessionBeanList = sessionBeanList;
        this.mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        View view = inflater.inflate(R.layout.fragment_session_detail, null, false);
        mSessionDetail = view.findViewById(R.id.session_detail_layout);
        mViewPager = view.findViewById(R.id.session_pager);
        mll_bottom_session = view.findViewById(R.id.ll_bottom_session);
        ll_study = view.findViewById(R.id.ll_study);
        ll_study.setOnClickListener(this);
        ll_study.setVisibility(Constants.SCHEDULE_BOOK?View.VISIBLE:View.GONE);
        ll_date = view.findViewById(R.id.ll_date);
        ll_date.setOnClickListener(this);
        ll_question = findClickView(view,R.id.ll_question);
        ll_question.setVisibility(Constants.SCHEDULE_ASK?View.VISIBLE:View.GONE);
        ll_location = view.findViewById(R.id.ll_location);
        ll_location.setOnClickListener(this);
        ll_note = view.findViewById(R.id.ll_note);
        ll_note.setOnClickListener(this);
        Titles = new CharSequence[mSessionBeanList.size()];

        for (int i = 0; i < mSessionBeanList.size(); i++) {
            Titles[i] = mSessionBeanList.get(i).getSessionName();
        }

        for (int i = 0; i < mSessionBeanList.size(); i++) {
            Session session = mSessionBeanList.get(i);

            SessionDetailPageFragment fragment = SessionDetailPageFragment.getInstance(session.getSessionGroupId());
            fragment.setViewPager(mViewPager);
            mSessionDetailFragments.add(fragment);
        }
        sPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        final int music = sPool.load(getActivity(), R.raw.fy, 1);
        mAdapter = new SessionDetailViewPagerAdapter(getChildFragmentManager(), mSessionDetailFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(1);
        MyHandler.sendEmptyMessageDelayed(SHOWWHAT, 1000);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mll_bottom_session.setVisibility(View.INVISIBLE);
                MyHandler.removeMessages(SHOWWHAT);
                MyHandler.sendEmptyMessageDelayed(SHOWWHAT, 1000);
                mPosition = position;
                sPool.play(music, 1, 1, 0, 0, 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    //课件预约
    private void InitMeetingPopupWindow() {
        final List<Meeting> meetings = ConferenceDbUtils.getMeetingBySessionGroupId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");
        if (meetings.size() == 0) {
            ToastUtils.showToast("暂无数据");
            return;
        }
        mMeetingPopupWindow = new QuestionPopupWindow(getActivity());

        mMeetingPopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mMeetingPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        ListView listView = mMeetingPopupWindow.getContentView().findViewById(R.id.list_scroll);
        TextView textView = mMeetingPopupWindow.getContentView().findViewById(R.id.tv_tip);
        textView.setText(getString(R.string.meeting_video_reservation));
        final OrderMeetingAdapter orderMeetingAdapter = new OrderMeetingAdapter(meetings, getActivity(),"order");
        listView.setAdapter(orderMeetingAdapter);
        int totalHeight = 0;
        for (int i = 0; i < orderMeetingAdapter.getCount(); i++) {
            View listItem = orderMeetingAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }
        if(DensityUtil.getScreenSize(getActivity())[1]<=1920){
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightLPercent;
        }else {
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightHPercent;
        }
        if (totalHeight > fixHeight) {
            totalHeight = (int) fixHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        meetingTopic = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = view.findViewById(R.id.question_cb);
                if (checkBox.isChecked()) {
                    meetingTopic.remove(meetings.get(position).getTopic() + "#@#" + meetings.get(position).getTopicEn());
                    meetings.get(position).setOrder(false);
                } else {
                    meetingTopic.add(meetings.get(position).getTopic() + "#@#" + meetings.get(position).getTopicEn());
                    meetings.get(position).setOrder(true);
                }
                orderMeetingAdapter.notifyDataSetChanged();
            }
        });
        mMeetingPopupWindow.getContentView().findViewById(R.id.rb_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (meetingTopic.size() > 0) {
                    for (int i = 0; i < meetingTopic.size(); i++) {
                        try {
                            String title = URLEncoder.encode(meetingTopic.get(i), "UTF-8");
                            CHYHttpClientUsage.getInstanse().doCoursewareReservation(title, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    ToastUtils.showRoundRectToast(getActivity(), R.layout.view_toast_custom);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                for(Meeting meeting : meetings){
                    meeting.save();
                }
                Intent intent = new Intent(Constants.ACTION_UPDATE_MEET);
                getActivity().sendBroadcast(intent);
                mMeetingPopupWindow.dismiss();
            }
        });
        mMeetingPopupWindow.getContentView().findViewById(R.id.rb_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meetingTopic.clear();
                mMeetingPopupWindow.dismiss();
            }
        });
        mMeetingPopupWindow.showAtLocation(mll_bottom_session, Gravity.TOP, 0, (int) (DensityUtil.getScreenSize(getActivity())[1] * 0.4));
        lightOff();
    }
    //课件提醒
    private void InitAlarmPopupWindow() {
        final List<Meeting> meetings = ConferenceDbUtils.getMeetingBySessionGroupId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");
        final List<Meeting> compareList = ConferenceDbUtils.getMeetingBySessionGroupId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");
        mClassBean = ConferenceDbUtils.findClassByClassId(mSessionBeanList.get(mPosition).getClassesId());
        if (meetings.size() == 0 && mClassBean !=null) {
            ToastUtils.showToast("暂无数据");
            return;
        }
        mAlarmPopupWindow = new QuestionPopupWindow(getActivity());

        mAlarmPopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mAlarmPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        ListView listView = mAlarmPopupWindow.getContentView().findViewById(R.id.list_scroll);
        TextView textView = mAlarmPopupWindow.getContentView().findViewById(R.id.tv_tip);
        textView.setText(getString(R.string.schedule_mind));
        final OrderMeetingAdapter orderMeetingAdapter = new OrderMeetingAdapter(meetings, getActivity(),"alarm");
        listView.setAdapter(orderMeetingAdapter);
        int totalHeight = 0;
        for (int i = 0; i < orderMeetingAdapter.getCount(); i++) {
            View listItem = orderMeetingAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }
        if(DensityUtil.getScreenSize(getActivity())[1]<=1920){
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightLPercent;
        }else {
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightHPercent;
        }
        if (totalHeight > fixHeight) {
            totalHeight = (int) fixHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        alarmList = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = view.findViewById(R.id.question_cb);
                if (checkBox.isChecked()) {
                    meetings.get(position).setAttention(0);
                    if(compareList.get(position).getAttention() == 1){
                        alarmList.add(meetings.get(position));
                    }else {
                        if(alarmList.contains(meetings.get(position))){
                            alarmList.remove(meetings.get(position));
                        }
                    }
                } else {
                    meetings.get(position).setAttention(1);
                    if(compareList.get(position).getAttention() == 0){
                        alarmList.add(meetings.get(position));
                    }else {
                        if(alarmList.contains(meetings.get(position))){
                            alarmList.remove(meetings.get(position));
                        }
                    }
                }
                orderMeetingAdapter.notifyDataSetChanged();
            }
        });
        mAlarmPopupWindow.getContentView().findViewById(R.id.rb_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmList.size() > 0) {
                    for (int position = 0; position < alarmList.size(); position++) {
                        //添加闹钟
                        Meeting meeting = alarmList.get(position);
                        if(meeting.getAttention() == 1){
                            Alert alertbean = new Alert();
                            alertbean.setDate(meeting.getMeetingDay());
                            alertbean.setEnable(1);
                            alertbean.setEnd(meeting.getEndTime());
                            alertbean.setRelativeid(String.valueOf(meeting.getSessionGroupId()));
                            alertbean.setRepeatdistance("5");
                            alertbean.setRepeattimes("0");
                            alertbean.setRoom(mClassBean.getClassesCode());
                            alertbean.setIdenId(meeting.getMeetingId());
                            alertbean.setStart(meeting.getStartTime());
                            alertbean.setTitle(meeting.getTopic() + "#@#" + meeting.getTopicEn());
                            alertbean.setType(AlertBean.TYPE_MEETING);
                            alertbean.save();
                            AlermClock.addClock(alertbean);
                        }else {
                            Alert alert = ConferenceDbUtils.getAlertByAlertId(meeting.getMeetingId());
                            if(alert!=null){
                                ConferenceDbUtils.deleteAlert(alert);
                            }
                        }
                    }
                }
                for(Meeting meeting : meetings){
                    meeting.save();
                }
                Intent intent = new Intent(Constants.ACTION_UPDATE_MEET);
                getActivity().sendBroadcast(intent);
                mAlarmPopupWindow.dismiss();
            }
        });
        mAlarmPopupWindow.getContentView().findViewById(R.id.rb_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmList.clear();
                mAlarmPopupWindow.dismiss();
            }
        });
        mAlarmPopupWindow.showAtLocation(mll_bottom_session, Gravity.TOP, 0, (int) (DensityUtil.getScreenSize(getActivity())[1] * 0.4));
        lightOff();
    }

    //专家提问
    private void InitQuestionPopupWindow() {
        final List<Meeting> meetings = ConferenceDbUtils.getMeetingBySessionGroupId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");
        final List<AskPupBean> askList = new ArrayList<>();
        for (int i = 0; i < meetings.size(); i++) {
            AskPupBean bean = new AskPupBean();
            String sps = meetings.get(i).getFacultyId();
            String[] speaker_ = sps.split(",");

            if(speaker_[0] !=null&&!TextUtils.isEmpty(speaker_[0])){
                Speaker speaker = SessionDetailPageFragment.getSpeakerById(speaker_[0]);
                if(speaker == null){
                    continue;
                }
                if(AppApplication.systemLanguage == 1){
                    bean.setFirName(speaker.getSpeakerName());
                    bean.setTopic(meetings.get(i).getTopic());
                    bean.setSpearkerId(speaker.getSpeakerId());
                }else {
                    bean.setFirName(speaker.getSpeakerNamePingyin());
                    bean.setTopic(meetings.get(i).getTopicEn());
                    bean.setSpearkerId(speaker.getSpeakerId());
                }
            }else {
                continue;
            }
            askList.add(bean);
        }
        if (askList.size() == 0) {
            ToastUtils.showToast("暂无数据");
            return;
        }
        mQuestionPopupWindow = new QuestionPopupWindow(getActivity());

        mQuestionPopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mQuestionPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        ListView listView = mQuestionPopupWindow.getContentView().findViewById(R.id.list_scroll);
        TextView textView = mQuestionPopupWindow.getContentView().findViewById(R.id.tv_tip);
        textView.setText(getString(R.string.question));
        final AskQuestionAdapter askQuestionAdapter = new AskQuestionAdapter(askList, getActivity());
        listView.setAdapter(askQuestionAdapter);
        int totalHeight = 0;
        for (int i = 0; i < askQuestionAdapter.getCount(); i++) {
            View listItem = askQuestionAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + listView.getDividerHeight();
        }
        if(DensityUtil.getScreenSize(getActivity())[1]<=1920){
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightLPercent;
        }else {
            fixHeight = DensityUtil.getScreenSize(getActivity())[1] * ScreenHeightHPercent;
        }
        if (totalHeight > fixHeight) {
            totalHeight = (int) fixHeight;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                askQuestionAdapter.clearCheck();
                askList.get(position).setSelected(true);
                askBean = askList.get(position);
                askBean.setMeetingId(meetings.get(position).getMeetingId());
                askQuestionAdapter.notifyDataSetChanged();
            }
        });
        mQuestionPopupWindow.getContentView().findViewById(R.id.rb_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askBean != null) {
                    View question = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                    MakeQuestionFragment instance = new MakeQuestionFragment();
                    instance.setMeetingQuestionInfo(askBean.getFirName(), askBean.getSpearkerId(), askBean.getMeetingId(), askBean.getTopic());
                    instance.setRightListener(question);
                    action(instance, getString(R.string.ask_sb, askBean.getFirName()), question, false, false, false);
                    mQuestionPopupWindow.dismiss();
                }
            }
        });
        mQuestionPopupWindow.getContentView().findViewById(R.id.rb_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askBean = null;
                mQuestionPopupWindow.dismiss();
            }
        });
        mQuestionPopupWindow.showAtLocation(mSessionDetail, Gravity.TOP, 0, (int) (DensityUtil.getScreenSize(getActivity())[1] * 0.4));
        lightOff();
    }

    private Handler MyHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == SHOWWHAT && getActivity() != null) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_in);
                mll_bottom_session.startAnimation(animation);
                mll_bottom_session.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_study:
                if (AppApplication.isUserLogIn()) {
                    InitMeetingPopupWindow();
                } else {
                    LoginActivity.startLoginActivity(getActivity(), LoginActivity.TYPE_NORMAL, "", "", "", "");
                }
                break;
            case R.id.ll_date:
                InitAlarmPopupWindow();
                break;
            case R.id.ll_question:
                InitQuestionPopupWindow();
                break;
            case R.id.ll_location:
                Class mClassBean = ConferenceDbUtils.findClassByClassId(mSessionBeanList.get(mPosition).getClassesId());
                MeetingScheduleRoomLocationActionFragment roomfragment = new MeetingScheduleRoomLocationActionFragment();
                roomfragment.setRoomBean(mClassBean, null, MeetingScheduleRoomLocationActionFragment.TYPE_MEETING);
                action(roomfragment, R.string.meeting_schedule_room_location, false, false, false);
                break;
            case R.id.ll_note:
                Note note = ConferenceDbUtils.getNoteBySessionId(mSessionBeanList.get(mPosition).getSessionGroupId() + "");
                //note不为空，代表已经记过笔记，否则重新创建笔记
                View myView = CommonUtils.initView(getActivity(), R.layout.title_right_textview);
                ((TextView) myView).setText(R.string.save);
                MeetingNoteEditorActionFragment fragment = MeetingNoteEditorActionFragment.getInstance(note != null ? MeetingNoteEditorActionFragment.TYPE_UPDATE : MeetingNoteEditorActionFragment.TYPE_NEW, mSessionBeanList.get(mPosition).getSessionGroupId());
                fragment.setRightView(myView);
                action(fragment, R.string.meeting_schedule_note, myView, false, false, false);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SESSIONDETAIL);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SESSIONDETAIL);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    /**
     * Find出来的View，自带防抖功能
     */
    public <T extends View> T findClickView(View parentView,int id) {

        T view = (T) parentView.findViewById(id);
        view.setOnClickListener(new EventListener(this));
        return view;
    }

}
