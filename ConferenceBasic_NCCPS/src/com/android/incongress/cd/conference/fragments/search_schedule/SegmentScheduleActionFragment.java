package com.android.incongress.cd.conference.fragments.search_schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.adapters.SearchRoomAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.SearchRoomBean;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.utils.DensityUtil;
import com.android.incongress.cd.conference.utils.DialogUtil;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.PickerView;
import com.android.incongress.cd.conference.widget.SpaceItemDecoration;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.android.incongress.cd.conference.widget.blws.PolyvKeyBoardUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Jacky on 2016/3/8.
 * 检索meeting
 */
public class SegmentScheduleActionFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mRvRoom;
    private GridLayoutManager mGridLayoutManager;
    private List<Class> mRoomsList;
    private SearchRoomAdapter mRoomAdapter;
    private List<String> mSessionDaysList = new ArrayList<>();
    private TextView mTvCurrentTime, mIvReset;

    private ImageView mIvPrev, mIvLast;
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

    private TextView mTvStartSearch, tv_cancel;
    private ProgressBar mProgressBar;

    private PickerView minute_from, second_from, minute_to, second_to;
    private String s_minute_from, s_second_from, s_minute_to, s_second_to;
    private boolean isCustomTime = false;
    private View custom_time;
    private LinearLayout ll_am, ll_pm, ll_ev;
    private EditText et_search;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        View view = inflater.inflate(R.layout.fragment_segment_schedule, container, false);
        initView(view);
        mCurrentSearchRoomName = getActivity().getResources().getString(R.string.search_all_class);
        dialogUtil = new DialogUtil();


        mGridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        mRvRoom.setLayoutManager(mGridLayoutManager);

        getRoomsInfo();
        getTimeInfo();

        mRoomAdapter = new SearchRoomAdapter(getActivity(), mRoomsList);
        mRvRoom.setAdapter(mRoomAdapter);
        mRvRoom.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 3)));
        mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        if (mSessionDaysList.contains(simpleDateFormat.format(date))) {
            for (int i = 0; i < mSessionDaysList.size(); i++) {
                if (simpleDateFormat.format(date).equals(mSessionDaysList.get(i))) {
                    String[] timeStrings = mSessionDaysList.get(i).split("-");
                    mTvCurrentTime.setText(timeStrings[1] + "月" + timeStrings[2] + "日");
                    mCurrentSearchDay = mSessionDaysList.get(i);
                    setArrowColor(i);
                }
            }
        }
        mProgressBar.setVisibility(View.GONE);
        initButtonState();
        ll_am.performClick();
        initPicker(view);
        mRoomAdapter.setOnItemClickListener(new SearchRoomAdapter.OnItemClickListener() {
            @Override
            public void doOnItemClick(View v, SearchRoomBean data) {
                if (data == null) {
                    mCurrentSearchRoom = "";
                    if (AppApplication.systemLanguage == 1) {
                        mCurrentSearchRoomName = "所有会议室";
                    } else {
                        mCurrentSearchRoomName = "All";
                    }
                } else {
                    mCurrentSearchRoom = data.getRoomId() + "";
                    mCurrentSearchRoomName = data.getRoomName();
                }
            }
        });
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String mSearchString = textView.getText().toString().trim();
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(mSearchString)) {
                        ToastUtils.showToast("请先输入搜索内容");
                        return true;
                    }
                    PolyvKeyBoardUtils.closeKeybord(et_search, getActivity());
                    action(SearchResultActionFragment.getSearchInstance(mSearchString), getString(R.string.search_search_result_title), false, false, false);
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private void initPicker(View view) {
        minute_from = view.findViewById(R.id.minute_from);
        second_from = view.findViewById(R.id.second_from);
        minute_to = view.findViewById(R.id.minute_to);
        second_to = view.findViewById(R.id.second_to);
        List<String> dataF = new ArrayList<>();
        List<String> dataT = new ArrayList<>();
        List<String> secondsF = new ArrayList<>();
        List<String> secondsT = new ArrayList<>();
        for (int i = 8; i < 21; i++) {
            if(i<10){
                dataF.add("0" + i);
                dataT.add("0" + i);
            }else {
                dataF.add("" + i);
                dataT.add("" + i);
            }

        }
        for (int i = 0; i < 6; i++) {
            secondsF.add( i + "0");
            secondsT.add( i + "0");
        }
        minute_from.setData(dataF);
        s_minute_from = minute_from.getText();
        minute_from.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                s_minute_from = text;
                mCurrentSearchStartTime = s_minute_from + ":" + s_second_from;
                mCurrentSearchEndTime = s_minute_to + ":" + s_second_to;
            }
        });
        second_from.setData(secondsF);
        s_second_from = second_from.getText();
        second_from.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                s_second_from = text;
                mCurrentSearchStartTime = s_minute_from + ":" + s_second_from;
                mCurrentSearchEndTime = s_minute_to + ":" + s_second_to;
            }
        });
        minute_to.setData(dataT);
        s_minute_to = minute_to.getText();
        minute_to.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                s_minute_to = text;
                mCurrentSearchStartTime = s_minute_from + ":" + s_second_from;
                mCurrentSearchEndTime = s_minute_to + ":" + s_second_to;
            }
        });
        second_to.setData(secondsT);
        s_second_to = second_to.getText();
        second_to.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                s_second_to = text;
                mCurrentSearchStartTime = s_minute_from + ":" + s_second_from;
                mCurrentSearchEndTime = s_minute_to + ":" + s_second_to;
            }
        });
        PickerView[] views = new PickerView[]{minute_from, minute_to, second_from, second_to};
        for (int i = 0; i < views.length; i++) {
            views[i].setOnTouchViewListener(new PickerView.onTouchViewListener() {
                @Override
                public void onTouchView() {
                    initButtonState();
                    custom_time.setBackground(getResources().getDrawable(R.drawable.bg_segment_selected));
                    mCurrentSearchStartTime = s_minute_from + ":" + s_second_from;
                    mCurrentSearchEndTime = s_minute_to + ":" + s_second_to;
                    isCustomTime = true;
                }
            });
        }
    }

    private void initView(View view) {
        mIvReset = view.findViewById(R.id.iv_reset);
        mIvPrev = view.findViewById(R.id.iv_prev);
        mIvLast = view.findViewById(R.id.iv_last);
        mTvCurrentTime = view.findViewById(R.id.tv_current_time);
        mTvStartSearch = view.findViewById(R.id.tv_start_search);
        mProgressBar = view.findViewById(R.id.progressbar);
        ll_am = view.findViewById(R.id.ll_am);
        ll_pm = view.findViewById(R.id.ll_pm);
        ll_ev = view.findViewById(R.id.ll_ev);
        custom_time = view.findViewById(R.id.custom_time);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        et_search = view.findViewById(R.id.et_search);
        mRvRoom = view.findViewById(R.id.rv_room);
        ll_am.setOnClickListener(this);
        ll_ev.setOnClickListener(this);
        ll_pm.setOnClickListener(this);
        mIvReset.setOnClickListener(this);
        mIvLast.setOnClickListener(this);
        custom_time.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        mTvStartSearch.setOnClickListener(this);
        mIvPrev.setOnClickListener(this);
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
            if (mSessionDaysList.size() > 0) {
                if (!(mSessionDaysList.get(mSessionDaysList.size() - 1).equals(session.getSessionDay()))) {
                    mSessionDaysList.add(session.getSessionDay());
                }
            } else {
                mSessionDaysList.add(session.getSessionDay());
            }
        }
        String[] timeStrings = mSessionDaysList.get(0).split("-");
        mTvCurrentTime.setText(timeStrings[1] + "月" + timeStrings[2] + "日");
        setArrowColor(0);
    }

    private void setArrowColor(int position) {
        if (mSessionDaysList.size() == 1) {
            PicUtils.setImageViewColor(mIvPrev, R.color.line_color);
            PicUtils.setImageViewColor(mIvLast, R.color.line_color);
            return;
        }
        if (position == mSessionDaysList.size() - 1) {
            PicUtils.setImageViewColor(mIvPrev, R.color.theme_color);
            PicUtils.setImageViewColor(mIvLast, R.color.line_color);
        } else if (position < mSessionDaysList.size() - 1) {
            if (position == 0) {
                PicUtils.setImageViewColor(mIvPrev, R.color.line_color);
                PicUtils.setImageViewColor(mIvLast, R.color.theme_color);
            } else {
                PicUtils.setImageViewColor(mIvPrev, R.color.theme_color);
                PicUtils.setImageViewColor(mIvLast, R.color.theme_color);
            }
        }
    }

    /**
     * 根据查询条件进行查询
     */
    private void doSearchQuery(String searchDay, String searchRoom, String searchRoomName, String searchStartTime, String searchEndTime) {
        action(SearchResultActionFragment.getInstance(searchDay, searchRoom, searchRoomName, searchStartTime, searchEndTime), getString(R.string.search_search_result_title), false, false, false);
    }

    //刷新重置按钮颜色(置灰)
    private void initButtonState() {
        ll_ev.setBackground(getResources().getDrawable(R.drawable.bg_segment_unselected));
        ll_am.setBackground(getResources().getDrawable(R.drawable.bg_segment_unselected));
        ll_pm.setBackground(getResources().getDrawable(R.drawable.bg_segment_unselected));
        custom_time.setBackground(getResources().getDrawable(R.drawable.bg_segment_unselected));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_am:
                initButtonState();
                ll_am.setBackground(getResources().getDrawable(R.drawable.bg_segment_selected));
                mCurrentSearchStartTime = "08:00";
                mCurrentSearchEndTime = "12:00";
                isCustomTime = false;
                break;
            case R.id.ll_pm:
                initButtonState();
                ll_pm.setBackground(getResources().getDrawable(R.drawable.bg_segment_selected));
                mCurrentSearchStartTime = "12:00";
                mCurrentSearchEndTime = "18:00";
                isCustomTime = false;
                break;
            case R.id.ll_ev:
                initButtonState();
                ll_ev.setBackground(getResources().getDrawable(R.drawable.bg_segment_selected));
                mCurrentSearchStartTime = "18:00";
                mCurrentSearchEndTime = "20:00";
                isCustomTime = false;
                break;
            case R.id.tv_cancel:
                ((HomeActivity) getActivity()).performBackClick();
                break;
            //重置搜索
            case R.id.iv_reset:
                mRoomAdapter.resetSearch();
                initButtonState();
                ll_am.setBackground(getResources().getDrawable(R.drawable.bg_segment_selected));
                //重置搜索条件
                mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
                mCurrentSearchRoom = "";
                mCurrentSearchStartTime = "08:00";
                mCurrentSearchEndTime = "12:00";
                break;
            //跳转下个内容
            case R.id.iv_last:
                if (mCurrentTimePosition == mSessionDaysList.size() - 1) {
                    return;
                }
                String[] timeStrings = mSessionDaysList.get(++mCurrentTimePosition).split("-");
                mTvCurrentTime.setText(timeStrings[1] + "月" + timeStrings[2] + "日");
                mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
                setArrowColor(mCurrentTimePosition);
                break;
            //回退上一个日期
            case R.id.iv_prev:
                if (mCurrentTimePosition == 0) {
                    return;
                }
                String[] timeStrings2 = mSessionDaysList.get(--mCurrentTimePosition).split("-");
                mTvCurrentTime.setText(timeStrings2[1] + "月" + timeStrings2[2] + "日");
                mCurrentSearchDay = mSessionDaysList.get(mCurrentTimePosition);
                setArrowColor(mCurrentTimePosition);
                break;
            //去搜索相关内容
            case R.id.tv_start_search:
                if (isCustomTime) {
                    if (Integer.parseInt(s_minute_to) > Integer.parseInt(s_minute_from)) {
                        doSearchQuery(mCurrentSearchDay, mCurrentSearchRoom, mCurrentSearchRoomName, mCurrentSearchStartTime, mCurrentSearchEndTime);
                    } else if (Integer.parseInt(s_minute_to) == Integer.parseInt(s_minute_from)) {
                        if (Integer.parseInt(s_second_to) > Integer.parseInt(s_second_from)) {
                            doSearchQuery(mCurrentSearchDay, mCurrentSearchRoom, mCurrentSearchRoomName, mCurrentSearchStartTime, mCurrentSearchEndTime);
                        } else {
                            ToastUtils.showToast(R.string.search_time);
                        }
                    } else {
                        ToastUtils.showToast(R.string.search_time);
                    }
                } else {
                    doSearchQuery(mCurrentSearchDay, mCurrentSearchRoom, mCurrentSearchRoomName, mCurrentSearchStartTime, mCurrentSearchEndTime);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isBackView){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isBackView = hidden;
        if(!hidden){
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        }
    }
}
