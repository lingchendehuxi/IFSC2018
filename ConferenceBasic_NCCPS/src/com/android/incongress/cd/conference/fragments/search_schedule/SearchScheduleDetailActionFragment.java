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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.MeetingSearchAdapter;
import com.android.incongress.cd.conference.adapters.SessionSearchAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/4/16.
 * 查询页面
 */
public class SearchScheduleDetailActionFragment extends BaseFragment {
    private ListView mLvSearchResult,mLvSearchMeetingResult;
    private TextView mTtitle,mTvNoData,mSessionSize,mMeetingSize;
    private EditText mCetSearch;
    private LinearLayout mSizeLayou,mMeetingSizeLayout;
    private SessionSearchAdapter mSessionAdapter;
    private MeetingSearchAdapter mMeetingAdapter;
    private List<Session> mSessionList = new ArrayList<>();
    private List<Meeting> mMeetingList = new ArrayList<>();
    private List<Role> mRoleList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_search,null);
        mLvSearchResult = (ListView) view.findViewById(R.id.lv_search_result);
        mLvSearchMeetingResult = (ListView) view.findViewById(R.id.lv_search_meetingresult);
        mSizeLayou = (LinearLayout) view.findViewById(R.id.session_sizeLayou);
        mMeetingSizeLayout = (LinearLayout) view.findViewById(R.id.meeting_sizeLayou);
        mTvNoData = (TextView) view.findViewById(R.id.tv_tips);
        mTtitle = (TextView) getActivity().findViewById(R.id.title_text);
        mSessionSize = (TextView) view.findViewById(R.id.session_size);
        mMeetingSize = (TextView) view.findViewById(R.id.meeting_size);
        mLvSearchResult.setEmptyView(mTvNoData);
        mLvSearchMeetingResult.setEmptyView(mTvNoData);
        mSessionAdapter = new SessionSearchAdapter(getActivity());
        mMeetingAdapter = new MeetingSearchAdapter(getActivity());
        mLvSearchResult.setAdapter(mSessionAdapter);
        mLvSearchMeetingResult.setAdapter(mMeetingAdapter);

        mSessionList.addAll(ConferenceDbUtils.getAllSession());
        mRoleList.addAll(ConferenceDbUtils.getAllRoles());


        mLvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Session session = mSessionAdapter.getDatasource().get(position);

                if(session != null) {
                    SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                    detail.setArguments(getSessionPosition(session.getSessionGroupId()), mSessionList);
                    action(detail, getString(R.string.meeting_schedule_detail_title), false, false, false);
                }
            }
        });
        mLvSearchMeetingResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meeting meeting = mMeetingAdapter.getDatasource().get(position);

                if(meeting != null) {
                    SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                    detail.setArguments(getSessionPosition(meeting.getSessionGroupId()), mSessionList);
                    action(detail, getString(R.string.meeting_schedule_detail_title), false, false, false);
                }
            }
        });
        mCetSearch = (EditText) view.findViewById(R.id.search_search);

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

                if(!StringUtils.isBlank(searchText)) {
                    mSessionAdapter.search(searchText);
                    mMeetingAdapter.searchMeeting(searchText);
                    setListViewHeightBasedOnChildren(mLvSearchResult);
                    setListViewHeightBasedOnChildren(mLvSearchMeetingResult);
                    if(mSessionAdapter.getDatasource().size()>0){
                        mSizeLayou.setVisibility(View.VISIBLE);
                        mMeetingSizeLayout.setVisibility(View.VISIBLE);
                        if(AppApplication.systemLanguage == 1){
                            int size = mSessionAdapter.getDatasource().size()+mMeetingAdapter.getDatasource().size();
                            mTtitle.setText("搜索结果("+size+")");
                        }else{
                            int size = mSessionAdapter.getDatasource().size()+mMeetingAdapter.getDatasource().size();
                            mTtitle.setText("Search Result("+size+")");
                        }
                        mSessionSize.setText("("+mSessionAdapter.getDatasource().size()+")");
                        mMeetingSize.setText("("+mMeetingAdapter.getDatasource().size()+")");
                    }else{
                        mSizeLayou.setVisibility(View.GONE);
                        mMeetingSizeLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
        return view;
    }

    /*public void setCenterVie(View view) {
        mCetSearch = (EditText) view.findViewById(R.id.search_search);

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

                if(!StringUtils.isBlank(searchText)) {
                    mSessionAdapter.search(searchText);
                }
            }
        });

    }*/

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

}
