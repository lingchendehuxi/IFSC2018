package com.android.incongress.cd.conference.fragments.professor_secretary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.beans.PSContactBean;
import com.android.incongress.cd.conference.fragments.bus_reminder.MeetingBusRemindAllFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerDetailFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.ArrayUtils;
import com.android.incongress.cd.conference.utils.DateTime;
import com.android.incongress.cd.conference.utils.DateUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.ListViewForFix;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/21.
 * 新的大会秘书
 */
public class ScretaryProfessorFragment extends BaseFragment {
    @BindView(R.id.ll_assign)
    LinearLayout ll_assign;
    @BindView(R.id.ll_ps_ppt)
    LinearLayout ll_ps_ppt;
    @BindView(R.id.tv_ps_ppt)
    TextView tv_ps_ppt;
    @BindView(R.id.ll_ps_work)
    LinearLayout ll_ps_work;
    @BindView(R.id.tv_ps_work)
    TextView tv_ps_work;
    @BindView(R.id.ll_ps_chzc)
    LinearLayout ll_ps_chzc;
    @BindView(R.id.tv_ps_chzc)
    TextView tv_ps_chzc;
    @BindView(R.id.ll_ps_contribute)
    LinearLayout ll_ps_contribute;
    @BindView(R.id.tv_ps_contribute)
    TextView tv_ps_contribute;
    @BindView(R.id.ll_ps_hall)
    LinearLayout ll_ps_hall;
    @BindView(R.id.tv_ps_hall)
    TextView tv_ps_hall;
    @BindView(R.id.ll_ps_bus)
    LinearLayout ll_ps_bus;
    @BindView(R.id.tv_ps_bus)
    TextView tv_ps_bus;
    @BindView(R.id.ll_ps_meet)
    LinearLayout ll_ps_meet;
    @BindView(R.id.list_meet)
    ListViewForFix list_meet;
    @BindView(R.id.tv_secretary_time)
    TextView mTvSecretaryTime;
    @BindView(R.id.tv_secretary_room)
    TextView mTvSecretaryRoom;
    @BindView(R.id.tv_secretary_task)
    TextView mTvSecretaryTask;
    @BindView(R.id.tv_secretary_session_name)
    TextView mTvSecretarySessionName;
    @BindView(R.id.tv_read_contribute_more)
    TextView tv_read_contribute_more;

    public static ScretaryProfessorFragment getInstance() {
        ScretaryProfessorFragment fragment = new ScretaryProfessorFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_scretary, null);
        ButterKnife.bind(this, view);
        getSecretary();
        getHomeAssistant();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
    }

    /**
     * 专家秘书
     */
    private void getSecretary() {
        CHYHttpClientUsage.getInstanse().doGetScretaryList(new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                String pptTxt = JSONCatch.parseString("pptTxt", response);
                String workTxt = JSONCatch.parseString("laowufeiTxt", response);
                String chzcTxt = JSONCatch.parseString("chzcText", response);
                String contributeTxt = JSONCatch.parseString("tougaoTxt", response);
                String hallTxt = JSONCatch.parseString("yongcanTxt", response);
                String busTxt = JSONCatch.parseString("busTxt", response);
                ArrayList<PSContactBean> listBean = new Gson().fromJson(JSONCatch.parseJsonarray("lxrArray", response).toString(), new TypeToken<List<PSContactBean>>() {
                }.getType());
                //幻灯片
                if (!TextUtils.isEmpty(pptTxt)) {
                    tv_ps_ppt.setText(pptTxt);
                } else {
                    ll_ps_ppt.setVisibility(View.GONE);
                }
                //会务费
                if (!TextUtils.isEmpty(workTxt)) {
                    tv_ps_work.setText(pptTxt);
                } else {
                    ll_ps_work.setVisibility(View.GONE);
                }
                //参会注册
                if (!TextUtils.isEmpty(chzcTxt)) {
                    tv_ps_chzc.setText(chzcTxt);
                } else {
                    ll_ps_chzc.setVisibility(View.GONE);
                }
                //投稿
                if (!TextUtils.isEmpty(contributeTxt)) {
                    tv_ps_contribute.setText(contributeTxt);
                } else {
                    ll_ps_contribute.setVisibility(View.GONE);
                }
                //用餐
                if (!TextUtils.isEmpty(hallTxt)) {
                    tv_ps_hall.setText(pptTxt);
                } else {
                    ll_ps_hall.setVisibility(View.GONE);
                }
                //班车
                if (!TextUtils.isEmpty(busTxt)) {
                    tv_ps_bus.setText(pptTxt);
                } else {
                    ll_ps_bus.setVisibility(View.GONE);
                }
                if (listBean != null && listBean.size() > 0) {
                    list_meet.setFocusable(false);
                    list_meet.setAdapter(new ListAdapter(getActivity(), listBean));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastUtils.showToast("获取信息失败，请联系管理员");
            }

        });
    }
    //专家秘书模块需要
    private List<ActivityBean> mAllActivitys = new ArrayList<>();
    private List<ActivityBean> mValidActivitys = new ArrayList<>();
    //我的专家秘书
    private void getHomeAssistant() {
        mAllActivitys = ConferenceDbUtils.getSessionAndMeetingBySpeakerId(AppApplication.facultyId);
        // 处理过期时间下的活动
        int size = mAllActivitys.size();
        Date date;
        String current_day = DateUtil.getNowDate(DateUtil.DEFAULT);
        String currentTime = current_day + " 00:00:00";
        Date currentDate = DateUtil.getDate(currentTime, DateUtil.DEFAULT_SECOND);
        long currentSecond = currentDate.getTime();

        for (int j = 0; j < size; j++) {
            String time = mAllActivitys.get(j).getTime() + ":00";
            date = DateUtil.getDate(time, DateUtil.DEFAULT_SECOND);
            long second = date.getTime();
            if (second > currentSecond) {
                mValidActivitys.add(mAllActivitys.get(j));
            }
        }
        //按照时间排序。
        ArrayUtils.quickSort(mValidActivitys);

        if (mValidActivitys != null && mValidActivitys.size() > 0) {
            //获取距离当前时间最近的
            ActivityBean activityBean = null;
            for (int k = 0; k < mValidActivitys.size(); k++) {
                Date date1 = DateUtil.getDate(mValidActivitys.get(k).getTime(), DateUtil.DEFAULT_ENGLISH2);
                if (date1.getTime() > System.currentTimeMillis()) {
                    activityBean = mValidActivitys.get(k);
                    break;
                }
            }

            if (activityBean != null) {
                if (AppApplication.systemLanguage == 1) {
                    mTvSecretaryTime.setText(DateUtil.getFormatMonthAndDayChinese(activityBean.getDate()) + " " + activityBean.getStart_time());
                    mTvSecretaryRoom.setText(activityBean.getLocation());
                    mTvSecretaryTask.setText("任务：" + activityBean.getTypeName());
                    mTvSecretarySessionName.setText(activityBean.getActivityName());
                } else {
                    mTvSecretaryTime.setText(DateUtil.getFormatMonthAndDayChinese(activityBean.getDate()) + " " + activityBean.getStart_time());
                    mTvSecretaryRoom.setText(activityBean.getLocationEn());
                    mTvSecretaryTask.setText("Task:" + activityBean.getTypeEnName());
                    mTvSecretarySessionName.setText(activityBean.getActivityNameEN());
                }
            }
        } else {
            //没有任务
            mTvSecretaryTime.setVisibility(View.GONE);
            mTvSecretaryRoom.setVisibility(View.GONE);
            mTvSecretaryTask.setVisibility(View.GONE);
            mTvSecretarySessionName.setText(R.string.secretary_no_task);
        }
    }
    @OnClick(R.id.tv_read_contribute_more)
    void clickContributeAll() {
        getAllSessionAndMeetingBySpeakerId(AppApplication.facultyId);
        Speaker speaker = ConferenceDbUtils.getSpeakerById(String.valueOf(AppApplication.facultyId));
        SpeakerDetailFragment speakerDetailFragment = new SpeakerDetailFragment();
        speakerDetailFragment.setArguments(speaker.getSpeakerId(), speaker.getSpeakerName(), speaker.getEnName(), mRoleIds.toString(), speaker.getImg(), mAllMeetings, false);
        action(speakerDetailFragment, R.string.speaker_detial, false, false, false);
    }
    @OnClick(R.id.tv_read_bus_more)
    void clickBusAll() {
        action(new MeetingBusRemindAllFragment(), R.string.home_bus, false, false, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
    private List<Integer> mRoleIds = new ArrayList<Integer>();
    private List<MeetingBean_new> mAllMeetings = new ArrayList<MeetingBean_new>();
    private List<MeetingBean_new> mAllMeetings_sorted = new ArrayList<MeetingBean_new>();
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
    private Role getRoleNameById(String roleId) {
        return ConferenceDbUtils.getRoleById(roleId);
    }
    private Class getClassNameByClassId(int classId) {
        return ConferenceDbUtils.findClassByClassId(classId);
    }

    class ListAdapter extends BaseAdapter {
        ArrayList<PSContactBean> listBeans;
        public Context context;
        public LayoutInflater layoutInflater;

        public ListAdapter(Context context, ArrayList<PSContactBean> listBeans) {
            this.context = context;
            this.listBeans = listBeans;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return listBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PSContactBean bean = listBeans.get(position);
            MyTimeHold myHold;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.scretary_ps_contact_item, null);
                myHold = new MyTimeHold();
                myHold.tv_name = convertView.findViewById(R.id.tv_name);
                myHold.tv_tel = convertView.findViewById(R.id.tv_tel);
                myHold.tv_email = convertView.findViewById(R.id.tv_email);
                convertView.setTag(myHold);
            } else {
                myHold = (MyTimeHold) convertView.getTag();
            }
            myHold.tv_name.setText(bean.getName());
            if (TextUtils.isEmpty(bean.getPhone())) {
                myHold.tv_tel.setVisibility(View.GONE);
            } else {
                myHold.tv_tel.setText("联系电话: " + bean.getPhone());
            }
            if (TextUtils.isEmpty(bean.getEmail())) {
                myHold.tv_email.setVisibility(View.GONE);
            } else {
                myHold.tv_email.setText("邮箱: " + bean.getEmail());
            }
            return convertView;
        }

        class MyTimeHold {
            TextView tv_name, tv_tel, tv_email;
        }
    }

}
