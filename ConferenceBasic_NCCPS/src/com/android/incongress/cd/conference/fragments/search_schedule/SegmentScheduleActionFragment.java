package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.adapters.SearchRoomAdapter;
import com.android.incongress.cd.conference.adapters.SearchTimeAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.SearchRoomBean;
import com.android.incongress.cd.conference.beans.SearchTimeBean;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.DialogUtil;
import com.android.incongress.cd.conference.widget.LWheelDialog;
import com.android.incongress.cd.conference.widget.SpaceItemDecoration;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jacky on 2016/3/8.
 * 检索meeting
 */
public class SegmentScheduleActionFragment extends BaseFragment {
    private RecyclerView mRvTime, mRvRoom;
    private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private SearchTimeAdapter mTimeAdapter;
    private List<Class> mRoomsList;
    private SearchRoomAdapter mRoomAdapter;
    private List<String> mSessionDaysList= new ArrayList<>();
    private TextView mTvCurrentTime;

    private ImageView mIvReset, mIvPrev, mIvLast;
    private int mCurrentTimePosition = 0;
    private DialogUtil dialogUtil;

    //查询条件 日期
    private String mCurrentSearchDay = "";
    //查询条件 会议室ID
    private String mCurrentSearchRoom = "";
    private String mCurrentSearchRoomName = "所有会议室";
    //查询条件 开始时间
    private String mCurrentSearchStartTime = "07:00";
    //查询条件 结束时间
    private String mCurrentSearchEndTime = "12:00";

    private TextView mTvStartSearch;
    private ProgressBar mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_segment_schedule, container, false);
        mRvTime = (RecyclerView) view.findViewById(R.id.rv_time);
        mIvReset = (ImageView) view.findViewById(R.id.iv_reset);
        mIvPrev = (ImageView) view.findViewById(R.id.iv_prev);
        mIvLast = (ImageView) view.findViewById(R.id.iv_last);
        mTvCurrentTime = (TextView) view.findViewById(R.id.tv_current_time);
        mTvStartSearch = (TextView) view.findViewById(R.id.tv_start_search);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        mCurrentSearchRoomName = getActivity().getResources().getString(R.string.search_all_class);
        dialogUtil = new DialogUtil();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTimeAdapter = new SearchTimeAdapter(getActivity(), DensityUtil.getScreenSize(getActivity()));
        mRvTime.setAdapter(mTimeAdapter);
        mRvTime.setLayoutManager(mLayoutManager);
        mTimeAdapter.setOnItemClickListener(new SearchTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SearchTimeBean data) {
                int position = data.getPosition();

                if(position == 0) {
                    mCurrentSearchStartTime = "08:00";
                    mCurrentSearchEndTime = "12:00";
                }else if(position == 1) {
                    mCurrentSearchStartTime = "12:00";
                    mCurrentSearchEndTime = "18:00";
                } else if(position == 2) {
                    mCurrentSearchStartTime = "18:00";
                    mCurrentSearchEndTime = "20:00";
                }else if (position == 3) {
                    dialogUtil.getWheelDialog(getActivity(), LWheelDialog.LWheelDialogType.ALL, null, new LWheelDialog.OnCheckedListener() {
                        @Override
                        public void onCheckedClicked(String time) {
                            String[] times = time.split("-");
                            String s1 = times[0];
                            String s2 = times[1];
                            int res = s2.compareTo(s1);
                            if(res>0) {
                                mTimeAdapter.setCustomTime(time);
                                mCurrentSearchStartTime = s1;
                                mCurrentSearchEndTime =s2 ;
                                dialogUtil.diss();
                            }else{
                                mTimeAdapter.resetSearch();
                                Toast.makeText(getActivity(), R.string.search_time,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mRvRoom = (RecyclerView) view.findViewById(R.id.rv_room);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        mRvRoom.setLayoutManager(mGridLayoutManager);

        getRoomsInfo();
        getTimeInfo();

        onClickListener();

        mRoomAdapter = new SearchRoomAdapter(getActivity(), mRoomsList);
        mRvRoom.setAdapter(mRoomAdapter);
        mRvRoom.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 3)));
        mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);

        mRoomAdapter.setOnItemClickListener(new SearchRoomAdapter.OnItemClickListener() {
            @Override
            public void doOnItemClick(View v, SearchRoomBean data) {
                if (data == null) {
                    mCurrentSearchRoom = "";
                    if(AppApplication.systemLanguage == 1){
                        mCurrentSearchRoomName = "所有会议室";
                    }else{
                        mCurrentSearchRoomName = "All";
                    }
                } else {
                    mCurrentSearchRoom = data.getRoomId() + "";
                    mCurrentSearchRoomName = data.getRoomName();
                }
            }
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if(mSessionDaysList.contains(simpleDateFormat.format(date))){
            for (int i = 0;i<mSessionDaysList.size();i++){
                if(simpleDateFormat.format(date).equals(mSessionDaysList.get(i))){
                    mTvCurrentTime.setText(mSessionDaysList.get(i).subSequence(5, 10));
                    mCurrentSearchDay = mSessionDaysList.get(i);
                    setArrowColor(i);
                }
            }
        }
        mProgressBar.setVisibility(View.GONE);

        return view;
    }

    private void onClickListener() {
        mIvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRoomAdapter.resetSearch();
                mTimeAdapter.resetSearch();
                mTvCurrentTime.setText(mSessionDaysList.get(mCurrentTimePosition = 0).subSequence(5, 10));

                //重置搜索条件
                mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
                mCurrentSearchRoom = "";
                mCurrentSearchStartTime = "08:00";
                mCurrentSearchEndTime = "12:00";
            }
        });

        mIvLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTimePosition == mSessionDaysList.size() - 1) {
                    return;
                }
                mTvCurrentTime.setText(mSessionDaysList.get(++mCurrentTimePosition).subSequence(5, 10));
                mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
                setArrowColor(mCurrentTimePosition);
            }
        });

        mIvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentTimePosition == 0) {
                    return;
                }
                mTvCurrentTime.setText(mSessionDaysList.get(--mCurrentTimePosition).subSequence(5, 10));
                mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
                setArrowColor(mCurrentTimePosition);
            }
        });

        mTvStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery(mCurrentSearchDay, mCurrentSearchRoom,mCurrentSearchRoomName, mCurrentSearchStartTime, mCurrentSearchEndTime);
            }
        });
    }

    /**
     * 获取所有的房间
     */
    private void getRoomsInfo() {
        mRoomsList = ConferenceDbUtils.getAllClasses();
    }

    /**
     * 获取所有的时间
     */
    private void getTimeInfo() {
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

        mTvCurrentTime.setText(mSessionDaysList.get(0).subSequence(5, 10));
    }

    private void setArrowColor(int position) {
        if (position == 0) {
            mIvPrev.setImageResource(R.drawable.left_arrow);
            mIvLast.setImageResource(R.drawable.right_arrow_clickable);
        } else if (position == (mSessionDaysList.size() - 1)) {
            mIvPrev.setImageResource(R.drawable.left_arrow_clickable);
            mIvLast.setImageResource(R.drawable.right_arrow);
        } else {
            mIvPrev.setImageResource(R.drawable.left_arrow_clickable);
            mIvLast.setImageResource(R.drawable.right_arrow_clickable);
        }
    }

    /**
     * 根据查询条件进行查询
     */
    private void doSearchQuery(String searchDay, String searchRoom,String searchRoomName ,String searchStartTime, String searchEndTime) {
        action(SearchResultActionFragment.getInstance(searchDay,searchRoom,searchRoomName,searchStartTime,searchEndTime), getString(R.string.search_search_result_title),false,false, false);
    }
}
