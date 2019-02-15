package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.SearchResultAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Session;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/3/11.
 * 查询结果
 */
public class SearchResultActionFragment extends BaseFragment implements View.OnClickListener{
    private ListView mLvSearchResult;
    private SearchResultAdapter mAdapter;
    private List<Session> mSessionBeans;
    private List<Meeting> mMeetingBeans;
    private List<Class> mClasses;

    private TextView mTitle,mDay,mTime,mRoom;
    private TextView mTvNoDataTips,mResultsTips;

    private List<Session> mSessionList = new ArrayList<>();

    private static String BUNDLE_SEARCH_STRING = "searchString";
    private static String BUNDLE_SEARCH_DAY = "searchDay";
    private static String BUNDLE_SEARCH_ROOM = "searchRoom";
    private static String BUNDLE_SEARCH_ROOM_NAME = "searchRoomName";
    private static String BUNDLE_SEARCH_START_TIME = "searchStartTime";
    private static String BUNDLE_SEARCH_END_TIME = "searchEndTime";

    private String mSearchDay ,mSearchRoom,mSearchRoomName,mSearchStartTime,mSearchEndTime,mSearchString;
    private CardView mSessionDayCard,mSessionTimeCard,mSessionRoomCard;

    public static final SearchResultActionFragment getInstance(String searchDay, String searchRoom, String searchRoomName, String searchStartTime, String searchEndTime) {
        SearchResultActionFragment fragment = new SearchResultActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_SEARCH_DAY, searchDay);
        bundle.putString(BUNDLE_SEARCH_ROOM, searchRoom);
        bundle.putString(BUNDLE_SEARCH_ROOM_NAME, searchRoomName);
        bundle.putString(BUNDLE_SEARCH_START_TIME, searchStartTime);
        bundle.putString(BUNDLE_SEARCH_END_TIME, searchEndTime);
        fragment.setArguments(bundle);
        return fragment;
    }
    public static final SearchResultActionFragment getSearchInstance(String searchString) {
        SearchResultActionFragment fragment = new SearchResultActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_SEARCH_STRING, searchString);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, null);
        mDay =  view.findViewById(R.id.session_day);
        mRoom =  view.findViewById(R.id.session_room);
        mTime =  view.findViewById(R.id.session_time);
        mTitle =  getActivity().findViewById(R.id.title_text);
        mTvNoDataTips =  view.findViewById(R.id.tv_tips);
        mResultsTips =  view.findViewById(R.id.tips_results);
        mLvSearchResult =  view.findViewById(R.id.lv_search_result);
        mSessionDayCard = view.findViewById(R.id.session_day_card);
        mSessionTimeCard = view.findViewById(R.id.session_time_card);
        mSessionRoomCard = view.findViewById(R.id.session_room_card);
        Bundle bundle = getArguments();

        mSearchDay = bundle.getString(BUNDLE_SEARCH_DAY);
        mSearchRoom = bundle.getString(BUNDLE_SEARCH_ROOM);
        mSearchRoomName = bundle.getString(BUNDLE_SEARCH_ROOM_NAME);
        mSearchStartTime = bundle.getString(BUNDLE_SEARCH_START_TIME);
        mSearchEndTime = bundle.getString(BUNDLE_SEARCH_END_TIME);
        mSearchString = bundle.getString(BUNDLE_SEARCH_STRING);
        if(!TextUtils.isEmpty(mSearchString)){
            mSessionDayCard.setVisibility(View.GONE);
            mSessionTimeCard.setVisibility(View.GONE);
            mSessionRoomCard.setVisibility(View.GONE);
            mSessionBeans = ConferenceDbUtils.getSessionBySearch(mSearchString);
        }else {
            mSessionBeans = ConferenceDbUtils.getSessionByTimeAndRoom(mSearchDay,mSearchRoom,mSearchStartTime,mSearchEndTime);
            mDay.setText(mSearchDay.subSequence(5, 10));
            mTime.setText(mSearchStartTime+" - "+mSearchEndTime);
            mRoom.setText(mSearchRoomName);
            mDay.setOnClickListener(this);
            mTime.setOnClickListener(this);
            mRoom.setOnClickListener(this);
        }
        mMeetingBeans = ConferenceDbUtils.getMeetingBySessions(mSessionBeans);
        mClasses = ConferenceDbUtils.getAllClasses();

        mSessionList.addAll(ConferenceDbUtils.getAllSession());
        if(mSessionBeans.size() == 0) {
            mTvNoDataTips.setVisibility(View.VISIBLE);
            mLvSearchResult.setVisibility(View.GONE);
            mResultsTips.setVisibility(View.GONE);
        }else {
            mResultsTips.setVisibility(View.VISIBLE);
            mResultsTips.setText(String.format(getString(R.string.retrieval_results),mSessionBeans.size()));
            mAdapter = new SearchResultAdapter(getActivity(), mSessionBeans, mMeetingBeans, mClasses);
            mLvSearchResult.setAdapter(mAdapter);
        }
        mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Session session = mSessionBeans.get(position);
                SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                detail.setArguments(getSessionPosition(session.getSessionGroupId()), mSessionList);
                action(detail, R.string.meeting_schedule_detail_title, false, false, false);
            }
        });
        return view;
    }

    /**
     * 获取Meeting在session中的位置
     * @param sessionGroupId
     * @return
     */
    private int getSessionPosition(int sessionGroupId) {
        for (int i = 0; i < mSessionList.size(); i++) {
            Session bean = mSessionList.get(i);
            if (bean.getSessionGroupId() == sessionGroupId) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SEARCHRESULT);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SEARCHRESULT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.session_day:
                mTvNoDataTips.setVisibility(View.GONE);
                mLvSearchResult.setVisibility(View.VISIBLE);
                mDay.setVisibility(View.GONE);
                mSessionDayCard.setVisibility(View.GONE);
                mSearchDay = "";
                mSessionBeans = ConferenceDbUtils.getSessionByTimeAndRoom(mSearchDay,mSearchRoom,mSearchStartTime,mSearchEndTime);
                mAdapter = new SearchResultAdapter(getActivity(), mSessionBeans, mMeetingBeans, mClasses);
                mLvSearchResult.setAdapter(mAdapter);
                if(AppApplication.systemLanguage == 1){
                    mTitle.setText("检索结果("+mSessionBeans.size()+")");
                    mResultsTips.setText(String.format(getString(R.string.retrieval_results),mSessionBeans.size()));
                }else{
                    mTitle.setText("Search result("+mSessionBeans.size()+")");
                }

                break;
            case R.id.session_time:
                mTvNoDataTips.setVisibility(View.GONE);
                mLvSearchResult.setVisibility(View.VISIBLE);
                mTime.setVisibility(View.GONE);
                mSessionTimeCard.setVisibility(View.GONE);
                mSearchStartTime = "06:00";
                mSearchEndTime = "24:00";
                mSessionBeans = ConferenceDbUtils.getSessionByTimeAndRoom(mSearchDay,mSearchRoom,mSearchStartTime,mSearchEndTime);
                mAdapter = new SearchResultAdapter(getActivity(), mSessionBeans, mMeetingBeans, mClasses);
                mLvSearchResult.setAdapter(mAdapter);
                if(AppApplication.systemLanguage == 1){
                    mTitle.setText("检索结果("+mSessionBeans.size()+")");
                    mResultsTips.setText(String.format(getString(R.string.retrieval_results),mSessionBeans.size()));
                }else{
                    mTitle.setText("Search result("+mSessionBeans.size()+")");
                }
                break;
            case R.id.session_room:
                mTvNoDataTips.setVisibility(View.GONE);
                mLvSearchResult.setVisibility(View.VISIBLE);
                mRoom.setVisibility(View.GONE);
                mSessionRoomCard.setVisibility(View.GONE);
                mSearchRoom = "";
                mSessionBeans = ConferenceDbUtils.getSessionByTimeAndRoom(mSearchDay,mSearchRoom,mSearchStartTime,mSearchEndTime);
                mAdapter = new SearchResultAdapter(getActivity(), mSessionBeans, mMeetingBeans, mClasses);
                mLvSearchResult.setAdapter(mAdapter);
                if(AppApplication.systemLanguage == 1){
                    mTitle.setText("检索结果("+mSessionBeans.size()+")");
                    mResultsTips.setText(String.format(getString(R.string.retrieval_results),mSessionBeans.size()));
                }else{
                    mTitle.setText("Search result("+mSessionBeans.size()+")");
                }
                break;
        }
    }
}
