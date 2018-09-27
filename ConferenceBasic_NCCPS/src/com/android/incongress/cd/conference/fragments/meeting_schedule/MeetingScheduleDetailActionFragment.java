package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.TimeBean;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.widget.HScroll;
import com.android.incongress.cd.conference.widget.HVScrollView;
import com.android.incongress.cd.conference.widget.VScroll;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jacky on 2015/12/15.
 * <p/>
 * 主题色：00a63b  0,166,59 --> rgba
 * session色块：   80%的主题色 0,166,59,.8   --> rgba
 * session详情页： 20%的主题色 0,166,59,.2   --> rgba
 * <p/>
 * 当前时间分隔线： EC6941 没有透明度
 */
public class MeetingScheduleDetailActionFragment extends BaseFragment {
    private TableLayout mTableLayout;
    private LinearLayout mLlLeftContainer, mLlTopContainer;

    private VScroll mLeftScrollView;
    private HVScrollView mHvScrollView;
    private HScroll mTopScrollView;

    private AbsoluteLayout mAbsContainer;
    private Context mContext;

    private ProgressDialog mDialog;

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

    private float mx, my;
    private String mTime;//当前页面的时间

    public static final String BUNDLE_TIME = "time";
    public static final String BUNDLE_SESSION_DAY = "sessionDay";

    //所有房间的list集合
    private List<Class> mRoomsList;
    private List<Class> mNewRoomsList = new ArrayList<>();
    //所有session的list集合
    private List<Session> mAllSessionsList;

    private List<String> mSessionDaysList;
    private Map<String, List<Session>> mSessionsByDay;
    private TimeBean mTimeBean;

    public MeetingScheduleDetailActionFragment() {
    }

    public static MeetingScheduleDetailActionFragment getSingleScheduleFragmetn(String time, ArrayList<String> sessionDayList) {
        MeetingScheduleDetailActionFragment meetingScheduleDetailFragment = new MeetingScheduleDetailActionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TIME, time);
        bundle.putStringArrayList(BUNDLE_SESSION_DAY, sessionDayList);
        meetingScheduleDetailFragment.setArguments(bundle);
        return meetingScheduleDetailFragment;
    }

    private List<String> mTimeList = new ArrayList<>();
    private long firstSessionTouchTime = 0;

    private int mHour = 0;
    private int mMinute = 0;

    private String getHourAndMinute(int hour, int minute) {
        String h = "";
        String m = "";
        if(hour <= 9) {
            h = "0" + hour;
        }else {
            h = hour +"";
        }

        if(minute == 0) {
            m = "0" + minute;
        }else {
            m = minute +"";
        }

        return  h +":"+ m;
    }

    /**
     * 开始时间，结束时间，时间间隔
     * @param startTime
     */
    private void setTimeList(int startTime) {
        mHour = startTime;
        mMinute = 0;

        for(int i=0; i<60; i=i+15) {
            mMinute = i;
            mTimeList.add(getHourAndMinute(mHour,mMinute));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mTime = getArguments().getString(BUNDLE_TIME);
            mRoomsList = ConferenceDbUtils.getAllClasses();
            mSessionDaysList = getArguments().getStringArrayList(BUNDLE_SESSION_DAY);
            for (int i = 0; i < mRoomsList.size(); i++){
                List<Session> sessionByRoom = ConferenceDbUtils.getDayClassSession(mRoomsList.get(i).getClassesId()+"",mTime);
                if(sessionByRoom!= null && sessionByRoom.size()>0) {
                    mNewRoomsList.add(mRoomsList.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View returnView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_single_schedule, null, false);
        //getActivity().findViewById(R.id.title_back).setVisibility(View.GONE);
        mTableLayout = (TableLayout) returnView.findViewById(R.id.table_layout);
        mLlLeftContainer = (LinearLayout) returnView.findViewById(R.id.ll_left_container);
        mLeftScrollView = (VScroll) returnView.findViewById(R.id.left_scrollview);
        mHvScrollView = (HVScrollView) returnView.findViewById(R.id.hv_scrollview);
        mTopScrollView = (HScroll) returnView.findViewById(R.id.top_scrollview);
        mLlTopContainer = (LinearLayout) returnView.findViewById(R.id.ll_top_container);
        mAbsContainer = (AbsoluteLayout) returnView.findViewById(R.id.abs_container);
        mContext = getActivity();
        mHvScrollView.setSmoothScrollingEnabled(true);
        mHvScrollView.setVScroll(mLeftScrollView);
        mHvScrollView.setHScroll(mTopScrollView);
        new MyAsyncTask().execute();
        return returnView;
    }

    private void setTablesLayout() {
        final TableRow.LayoutParams tableParams = new TableRow.LayoutParams(DensityUtil.dip2px(mContext, (float) (100 * mNewRoomsList.size())), DensityUtil.dip2px(mContext, 30f));

        for (int row = 0; row < mTimeList.size(); row++) {
            final int row_temp = row;
            if (row % 4 == 0) {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider, null);

                final TableRow tableRow = new TableRow(mContext);
                final View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_table, null);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tableRow.addView(view1, tableParams);
                        if (row_temp != 0) {
                            mTableLayout.addView(view);
                        }
                        mTableLayout.addView(tableRow);
                    }
                });
            } else {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider_gap, null);

                final TableRow tableRow = new TableRow(mContext);
                final View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_table, null);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tableRow.addView(view1, tableParams);
                        if (row_temp != 0) {
                            mTableLayout.addView(view);
                        }
                        mTableLayout.addView(tableRow);
                    }
                });
            }
        }
    }


    private boolean frist = true;
    private void setSessionLayout() {
        mSessionsByDay = new HashMap<>();
        mAllSessionsList = new ArrayList<>();

        for (int i = 0; i < mSessionDaysList.size(); i++) {
            List<Session> sessionList = ConferenceDbUtils.getSessionsBySessionDayOrderByClassIdAndStartTime(mSessionDaysList.get(i));
            mAllSessionsList.addAll(sessionList);//获取会议室
            mSessionsByDay.put(mSessionDaysList.get(i), sessionList);
        }

        //开始时间--> y坐标值 ，Room位置-->x坐标值， 宽固定值， 高-->结束时间-开始时间
        //"startTime":"14:00","endTime":"16:00" sessionName":"国家心血管病中心心脏培训：第一场: 冠心病 classesId":2423
        Session sessionBean;
        for (int i = 0; i < mSessionsByDay.get(mTime).size(); i++) {
            sessionBean = mSessionsByDay.get(mTime).get(i);

            final AbsoluteLayout.LayoutParams absParams = new AbsoluteLayout.LayoutParams(DensityUtil.dip2px(mContext, 100f),
                    DensityUtil.dip2px(mContext, getSessionHeight(sessionBean.getStartTime(), sessionBean.getEndTime())),
                    DensityUtil.dip2px(mContext, getStartXLocation(sessionBean.getClassesId() + "")),
                    DensityUtil.dip2px(mContext, getYLocation(sessionBean.getStartTime())));
                //mLeftScrollView.setScrollY(370);

            final View temp = LayoutInflater.from(mContext).inflate(R.layout.temp_session, null);
            temp.setTag("" + sessionBean.getSessionGroupId());
            if (AppApplication.systemLanguage == 1) {
                ((TextView) temp.findViewById(R.id.tv_session)).setText(sessionBean.getSessionName());
            } else {
                ((TextView) temp.findViewById(R.id.tv_session)).setText(sessionBean.getSessionNameEN());
            }
            temp.findViewById(R.id.tv_session).setBackgroundColor( Color.parseColor(getString(R.string.alpha_theme_color)));
            temp.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        firstSessionTouchTime = System.currentTimeMillis();
                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        return false;
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        long endSessionTouchTime = System.currentTimeMillis();
                        if (endSessionTouchTime - firstSessionTouchTime < 1000) {
                            int sessionGroupId = Integer.parseInt((String) temp.getTag());

                            int tartgetPosition = 0;
                            for (int i = 0; i < mAllSessionsList.size(); i++) {
                                if (mAllSessionsList.get(i).getSessionGroupId() == sessionGroupId) {
                                    tartgetPosition = i;
                                }
                            }

                            SessionDetailViewPageFragment detail = new SessionDetailViewPageFragment();
                            detail.setArguments(tartgetPosition, mAllSessionsList);
                            View moreView = CommonUtils.initView(getActivity(), R.layout.titlebar_session_detail_more);
                            detail.setRightListener(moreView);
                            action(detail, R.string.meeting_schedule_detail_title, moreView, false, false, false);

                            return true;
                        } else {
                            return false;
                        }
                    }
                    return false;
                }
            });

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAbsContainer.addView(temp, absParams);
                }
            });
        }
       // mAbsContainer.setTranslationY(-370);
    }

    //获取Y坐标值
    private float getYLocation(String time) {
        if (StringUtils.isBlank(time))
            return 0;
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        //加上分隔线高度
        return ((hour - mTimeBean.getStartTime()) * 4 * 30 + minute * 2 + (float) ((hour - mTimeBean.getStartTime()) * 4 * 30 + minute * 2) / 30f * 1) + 1;
    }

    //获取X坐标值
    private float getStartXLocation(String classRoomId) {
        int position = 0;
        for (int i = 0; i < mNewRoomsList.size(); i++) {
            if (classRoomId.equals(mNewRoomsList.get(i).getClassesId() + "")) {
                position = i;
            }
        }
        return (float) (position * 100);
    }

    //获取控件的高度
    private float getSessionHeight(String startTime, String endTime) {
        return getYLocation(endTime) - getYLocation(startTime);
    }

    private void setTimeLayout() {
        final LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 80f), DensityUtil.dip2px(mContext, 30f));
        for (int i = 0; i < mTimeList.size(); i++) {
            final int temp = i;
            if (i % 4 == 0) {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider, null);

                final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_table, null);
                tv.setText(mTimeList.get(i));
                tv.setGravity(Gravity.CENTER);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp != 0) {
                            mLlLeftContainer.addView(view);
                        }
                        mLlLeftContainer.addView(tv, timeParams);
                    }
                });
            } else {
                final View view = LayoutInflater.from(mContext).inflate(R.layout.table_divider_gap, null);

                final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_table, null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp != 0) {
                            mLlLeftContainer.addView(view);
                        }
                        mLlLeftContainer.addView(tv, timeParams);
                    }
                });
            }
        }
    }

    private void setRoomsLayout() {
        final LinearLayout.LayoutParams roomParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 100f), FP);

        /**/

        for (int i = 0; i < mNewRoomsList.size(); i++) {
            final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_table, null);
            String location = "";
            String newLocation = "";
            if (AppApplication.systemLanguage == 1) {
                location = mNewRoomsList.get(i).getClassesCode();
            } else {
                location = mNewRoomsList.get(i).getClassCodeEn();
            }

            int leftBracket = location.indexOf("(");
            if(leftBracket == -1) {
                newLocation = location;
            }else {
                newLocation = location.substring(0, leftBracket) +"\n" + location.substring(leftBracket, location.length());
            }

            tv.setText(newLocation);
            tv.setPadding(8, 8, 8, 8);
            tv.setGravity(Gravity.CENTER);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLlTopContainer.addView(tv, roomParams);
                }
            });
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null, "loading...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            mTimeBean = ConferenceDbUtils.getTime();

            for(int i=mTimeBean.getStartTime(); i<= mTimeBean.getEndTime(); i++) {
                setTimeList(i);
            }

            setTimeLayout();
            setRoomsLayout();
            setTablesLayout();
            setSessionLayout();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDialog.dismiss();
        }
    }
}
