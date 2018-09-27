package com.android.incongress.cd.conference.fragments.question;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.QuestionSessionsAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailPageFragment;
import com.android.incongress.cd.conference.fragments.meeting_schedule.SessionDetailViewPageFragment;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2017/1/3.
 */

public class QuestionByMeetingFragment extends BaseFragment {
    private XRecyclerView mRecyclerView;
    private TextView mTvTips;
    private List<Session> mAllSessions = new ArrayList<>();
    private QuestionSessionsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetings_by_question, null);

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAllSessions = ConferenceDbUtils.getAllSession();
        mAdapter = new QuestionSessionsAdapter(getActivity(), mAllSessions);

        mRecyclerView.setEmptyView(mTvTips);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);

        mAdapter.setSessionClickListener(new QuestionSessionsAdapter.OnSessionClickListener() {
            @Override
            public void onItemClick(int position) {
                SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                detail.setArguments(position, mAllSessions);
                action(detail, R.string.meeting_schedule_detail_title, false, false, false);
            }
        });

        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.GRAY)
                        .sizeResId(R.dimen.divider_height)
                        .build());
        return view;
    }

    public void refreshSessions(List<Session> sessions) {
        mAllSessions.clear();
        mAllSessions.addAll(sessions);
        mAdapter.notifyDataSetChanged();
    }


}
