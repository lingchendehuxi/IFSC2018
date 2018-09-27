package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.android.incongress.cd.conference.adapters.MeetingScheduleListAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.widget.stick_header.StickyListHeadersListView;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jacky on 2016/2/1.
 */
public class MeetingScheduleListDetailActionFragment extends BaseFragment {
    private StickyListHeadersListView mStickLVSpeaker;
    private MeetingScheduleListAdapter mScheduleListAdapter;
    private String mCurrentMeetingDay = "2016-03-17";

    private static final String BUNDLE_MEETING_DAY = "meetingDay";

    private ProgressBar mPbLoading;
    private boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        }else {
            isVisible = false;
        }
    }

    private void onVisible() {
        lazyLoad();
    }

    //标志位，是否已经初始化完毕
    private boolean isPrepared;

    private void lazyLoad() {
        if(!isPrepared || !isVisible)
            return;

        new MyMeetingAsyncTask().execute();
    }

    public static MeetingScheduleListDetailActionFragment getInstance(String meetingDay) {
        MeetingScheduleListDetailActionFragment fragment = new MeetingScheduleListDetailActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_MEETING_DAY, meetingDay);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mCurrentMeetingDay = getArguments().getString(BUNDLE_MEETING_DAY);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_list, null);
        mStickLVSpeaker = (StickyListHeadersListView) view.findViewById(R.id.slhlv_sessions);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);

        mStickLVSpeaker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int tartgetPosition = 0;
                for (int i = 0; i < mAllSessionsList.size(); i++) {
                    if (mAllSessionsList.get(i).getSessionGroupId() == mCurrentTimeSessions.get(position).getSessionGroupId()) {
                        tartgetPosition = i;
                    }
                }

                SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                detail.setArguments(tartgetPosition, mAllSessionsList);
                View moreView = CommonUtils.initView(getActivity(), R.layout.titlebar_session_detail_more);
                detail.setRightListener(moreView);
                action(detail, R.string.meeting_schedule_detail_title, moreView, false, false, false);

            }
        });

        isPrepared = true;
        lazyLoad();

        return view;
    }

    class MyMeetingAsyncTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mScheduleListAdapter = new MeetingScheduleListAdapter(getActivity(), mCurrentTimeSessions);
            mStickLVSpeaker.setAdapter(mScheduleListAdapter);

            mPbLoading.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            getCurrentDaySessions(mCurrentMeetingDay);
            getAllSessionList();
            return null;
        }
    }


    //一共有几天
    private List<Session> mCurrentTimeSessions;
    private List<String> mSessionDaysList = new ArrayList<>();
    private List<Session> mAllSessionsList;

    /**
     * 获取当前一天的session数据
     */
    private void getCurrentDaySessions(String time) {
        mSessionDaysList.clear();

        List<Session> allSession = ConferenceDbUtils.getAllSession();
        for (int i = 0; i < allSession.size(); i++) {
            Session session = allSession.get(i);
            if(mSessionDaysList.size()>0) {
                if(!(mSessionDaysList.get(mSessionDaysList.size()-1).equals(session.getSessionDay()))) {
                    mSessionDaysList.add(session.getSessionDay());
                }
            }else {
                mSessionDaysList.add(session.getSessionDay());
            }
        }
        mCurrentTimeSessions = new ArrayList<>();
        mCurrentTimeSessions = ConferenceDbUtils.getSessionsBySessionDayOrderByClassIdAndStartTime(time);
    }


    private void getAllSessionList() {
        mAllSessionsList = new ArrayList<>();
        for(int i=0; i<mSessionDaysList.size(); i++) {
            List<Session> temp = ConferenceDbUtils.getSessionsBySessionDayOrderByClassIdAndStartTime(mSessionDaysList.get(i));
            mAllSessionsList.addAll(temp);//获取会议室
        }
    }
}
