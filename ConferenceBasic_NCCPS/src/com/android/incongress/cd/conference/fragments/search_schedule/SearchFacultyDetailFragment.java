package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.FacultySearchAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerDetailFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jacky on 2016/4/16.
 * 查询专家页面
 */
public class SearchFacultyDetailFragment extends BaseFragment {
    private ListView mLvSearchResult;
    private TextView mTvNoData;
    private EditText mCetSearch;

    private FacultySearchAdapter mFacultyAdapter;

    private List<Integer> mRoleIds = new ArrayList<Integer>();
    private List<MeetingBean_new> mAllMeetings = new ArrayList<MeetingBean_new>();
    private List<MeetingBean_new> mAllMeetings_sorted = new ArrayList<MeetingBean_new>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_search, null);

        mLvSearchResult = (ListView) view.findViewById(R.id.lv_search_result);
        mTvNoData = (TextView) view.findViewById(R.id.tv_tips);

        mLvSearchResult.setEmptyView(mTvNoData);

        mFacultyAdapter = new FacultySearchAdapter();
        mLvSearchResult.setAdapter(mFacultyAdapter);

        mCetSearch = (EditText) view.findViewById(R.id.search_search);
        mCetSearch.setHint(R.string.search_faculty_hint);

        mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Speaker bean = mFacultyAdapter.getDatasource().get(position);

                if (bean != null) {
                    getAllSessionAndMeetingBySpeakerId(bean.getSpeakerId());

                    SpeakerDetailFragment speakerDetailFragment = new SpeakerDetailFragment();
                    speakerDetailFragment.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), bean.getImg(), mAllMeetings, false);
                    action(speakerDetailFragment, R.string.speaker_detial, false, false, false);
                }
            }
        });
        mCetSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();

                if (!StringUtils.isBlank(searchText)) {
                    mFacultyAdapter.search(searchText);
                }
            }
        });
        return view;
    }


    private void getAllSessionAndMeetingBySpeakerId(int speakerId) {
        //获得了所有的身份类型
        mRoleIds.clear();
        mAllMeetings.clear();
        mAllMeetings_sorted.clear();


        List<Session> sessions = new ArrayList<>();
        sessions.addAll(ConferenceDbUtils.getSessionBySpeakerId(speakerId+""));

        //获取该speakerId下的所有presentation级别会议
        List<Meeting> meetings = new ArrayList<>();
        meetings.addAll(ConferenceDbUtils.getMeetingBySpeakerId(speakerId+""));

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
                    getClassNameByClassId(bean.getClassesId()).getClassesCode(), getClassNameByClassId(bean.getClassesId()).getClassCodeEn(),1);
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
                    getClassNameByClassId(bean.getClassesId()).getClassCodeEn(),2);
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

}
