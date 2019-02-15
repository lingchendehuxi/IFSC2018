package com.android.incongress.cd.conference.fragments.my_schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MyScheduleDetailAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/1/15.
 * 我的日程中的子fragment
 */
public class MyScheduleDetailActionFragmentOrder extends BaseFragment {
    private static final String BUNDLE_TIME = "time";

    private String mTime;
    private ListView mLvMySchedule;
    private LinearLayout ll_tips;
    private MyScheduleDetailAdapter mAdapter;

    private List<Session> mSessionBeans = new ArrayList<>();
    private List<Meeting> mMeetingBeans = new ArrayList<>();

    private List<Session> mShowSessionList = new ArrayList<>();

    private List<Session> mAllSessionList = new ArrayList<>();
    private MyReceiver mReceiver;

    public MyScheduleDetailActionFragmentOrder() {
    }

    public static final MyScheduleDetailActionFragmentOrder getInstance(String time) {
        MyScheduleDetailActionFragmentOrder fragment = new MyScheduleDetailActionFragmentOrder();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, time);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTime = getArguments().getString(BUNDLE_TIME);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver, new IntentFilter(MyScheduleActionFragment.BROADCAST_EDIT));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_schedule_item, null);

        mLvMySchedule = (ListView) view.findViewById(R.id.lv_schedule);
        ll_tips =  view.findViewById(R.id.ll_tips);
        mReceiver = new MyReceiver();

        mShowSessionList.clear();
        mMeetingBeans.clear();
        new MyScheduleTask().execute();
        return view;
    }

    class MyScheduleTask extends AsyncTask<Void,Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter = new MyScheduleDetailAdapter(getActivity(),mShowSessionList, mMeetingBeans);
            mLvMySchedule.setAdapter(mAdapter);

            mLvMySchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Session session = mShowSessionList.get(position);
                    SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                    detail.setArguments(getSessionPosition(session.getSessionGroupId()), mAllSessionList);
                    action(detail, R.string.meeting_schedule_detail_title, false, false, false);
                }
            });

            if(mShowSessionList.size() == 0) {
                ll_tips.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mSessionBeans =ConferenceDbUtils.getSessionByAttention("1");
            mMeetingBeans = ConferenceDbUtils.getMeetingsByAttention("1");
            mShowSessionList.addAll(mSessionBeans);

            List<Integer> sessionGroupIdsFromMeetings = new ArrayList<>();

            for(int i=0; i<mMeetingBeans.size(); i++) {
                Meeting meeting = mMeetingBeans.get(i);

                boolean isMeetingInSessionList = false;
                for(int j=0; j<mSessionBeans.size(); j++) {
                    if(meeting.getSessionGroupId() == mSessionBeans.get(j).getSessionGroupId()) {
                        isMeetingInSessionList = true;
                        break;
                    }
                }

                if(!isMeetingInSessionList) {
                     if(!sessionGroupIdsFromMeetings.contains(meeting.getSessionGroupId())) {
                         sessionGroupIdsFromMeetings.add(meeting.getSessionGroupId());
                     }
                }
            }

            for(int i=0; i<sessionGroupIdsFromMeetings.size(); i++) {
                mShowSessionList.add(ConferenceDbUtils.getSessionBySessionId(sessionGroupIdsFromMeetings.get(i) + ""));
            }
            mAllSessionList.addAll(ConferenceDbUtils.getAllSession());
            return null;
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOpen = intent.getBooleanExtra(MyScheduleActionFragment.BROADCAST_OPEN_CLOSE,false);
            if(isOpen) {
                mAdapter.setAlarmShow(true);
            }else {
                mAdapter.setAlarmShow(false);
            }
        }
    }

    /**
     * 获取Meeting在session中的位置
     * @param sessionGroupId
     * @return
     */
    private int getSessionPosition(int sessionGroupId) {
        for (int i = 0; i < mAllSessionList.size(); i++) {
            Session bean = mAllSessionList.get(i);
            if (bean.getSessionGroupId() == sessionGroupId) {
                return i;
            }
        }
        return 0;
    }
}
