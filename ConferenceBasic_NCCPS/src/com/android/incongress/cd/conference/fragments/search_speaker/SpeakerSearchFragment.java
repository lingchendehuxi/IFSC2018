package com.android.incongress.cd.conference.fragments.search_speaker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.FacultySearchAdapter;
import com.android.incongress.cd.conference.adapters.MettingCommunitySpeakerAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.MeetingBean_new;
import com.android.incongress.cd.conference.fragments.search_schedule.SearchFacultyDetailFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Meeting;
import com.android.incongress.cd.conference.model.Role;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.model.Speaker;
import com.android.incongress.cd.conference.utils.BladeView;
import com.android.incongress.cd.conference.utils.BladeView.OnItemClickListener;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.MySectionIndexer;
import com.android.incongress.cd.conference.utils.PinnedHeaderListView;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeakerSearchFragment extends BaseFragment {
    PinnedHeaderListView mSpeakerListView;
    MettingCommunitySpeakerAdapter mAdAdapter;
    BladeView mBladeView;
    private TextView mTvTips;
    ProgressBar mProgressBar;
    private RelativeLayout mSearchTitle;
    private LinearLayout mInitialSearch,mStartSearch;
    private EditText mSearchContent;
    private TextView mCancel;
    private ListView mLvSearchResult;
    private FacultySearchAdapter mFacultyAdapter;
    private List<Speaker> mSpeakers;
    private int mCurrentType = TYPE_FROM_HOME;

    public static final String TYPE_FROM_WHERE = "from_where";
    public static final int TYPE_FROM_HOME = 1;
    public static final int TYPE_FROM_SCENIC_XIU = 2;

    private MySectionIndexer mIndexer;
    private static final String ALL_CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
    protected static final String TAG = null;

    private String[] sections = {"","A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "#"};
    private int[] counts;
    //参数为了在切换到activity返回后，fragment重新设置导航栏字体颜色
    private boolean isBackView = true;

    public SpeakerSearchFragment() {
    }

    public static final SpeakerSearchFragment getInstance(int typeFrom) {
        SpeakerSearchFragment fragment = new SpeakerSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_FROM_WHERE, typeFrom);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentType = getArguments().getInt(TYPE_FROM_WHERE);
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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarDarkTheme(getActivity(),true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.hysqhome_jiangzhe, null);
        mSearchTitle = (RelativeLayout) view.findViewById(R.id.search_title);
        mInitialSearch = (LinearLayout) view.findViewById(R.id.initial_search);
        mStartSearch = (LinearLayout) view.findViewById(R.id.start_search);
        mSearchContent = (EditText) view.findViewById(R.id.speaker_search_content);
        mCancel = (TextView) view.findViewById(R.id.search_cancel);
        mLvSearchResult = (ListView) view.findViewById(R.id.lv_search_result);
        mSpeakerListView = (PinnedHeaderListView) view.findViewById(R.id.hysq_home_jiangzhe_pinnedListView);
        mBladeView = (BladeView) view.findViewById(R.id.hysq_home_jiangzhe_mLetterListView);
        mTvTips = (TextView) view.findViewById(R.id.tv_tips);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        counts = new int[sections.length];
        if(mCurrentType == TYPE_FROM_SCENIC_XIU ){
            mSearchTitle.setVisibility(View.GONE);
        }else{
            mSearchTitle.setVisibility(View.VISIBLE);
            mFacultyAdapter = new FacultySearchAdapter();
            mLvSearchResult.setAdapter(mFacultyAdapter);
        }
        mInitialSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInitialSearch.setVisibility(View.GONE);
                mStartSearch.setVisibility(View.VISIBLE);
                mSpeakerListView.setVisibility(View.GONE);
                mLvSearchResult.setVisibility(View.VISIBLE);
                mBladeView.setVisibility(View.GONE);
                mSearchContent.setFocusable(true);
                mSearchContent.setFocusableInTouchMode(true);
                mSearchContent.requestFocus();
                mSearchContent.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInitialSearch.setVisibility(View.VISIBLE);
                mStartSearch.setVisibility(View.GONE);
                mSpeakerListView.setVisibility(View.VISIBLE);
                mBladeView.setVisibility(View.VISIBLE);
                mLvSearchResult.setVisibility(View.GONE);
                mTvTips.setVisibility(View.GONE);
                mFacultyAdapter.empty();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);}
        });
        mSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString();
                if(!searchText.equals("")){
                    if (!StringUtils.isBlank(searchText)) {
                        if(ConferenceDbUtils.getSpeakerBySpeakerName(searchText, false).size()>0){
                            mTvTips.setVisibility(View.GONE);
                            mFacultyAdapter.empty();
                            mFacultyAdapter.search(searchText);
                        }else{
                            mTvTips.setVisibility(View.VISIBLE);
                            mFacultyAdapter.empty();
                            if(AppApplication.systemLanguage == 1){
                                mTvTips.setText("搜索关键字 “"+searchText+"” 无结果，请重试");
                            }else{
                                mTvTips.setText("Sorry,we couldn't find any results matching “"+searchText+"”");
                            }
                        }
                    }
                }
            }
        });

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
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        new SpeakerTask().execute();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    public void setRightView(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View searchView = CommonUtils.initView(getActivity(), R.layout.title_search);
                SearchFacultyDetailFragment fragment = new SearchFacultyDetailFragment();
                //fragment.setCenterVie(searchView);
                action(fragment, R.string.search_result, false, false, false);
                //action(fragment, R.string.enter_blank,null,false,false,searchView, false);
            }
        });
    }
    class SpeakerTask extends AsyncTask {
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            mSpeakerListView.setAdapter(mAdAdapter);
            mSpeakerListView.setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.pinned_header_view, mSpeakerListView, false));


            if(mSpeakers.size() == 0) {
                mTvTips.setVisibility(View.VISIBLE);
                mSpeakerListView.setVisibility(View.GONE);
                mBladeView.setVisibility(View.GONE);
            }else {
                mTvTips.setVisibility(View.GONE);
                mBladeView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(String s) {
                        if (s != null) {
                            int section = ALL_CHARACTER.indexOf(s);
                            int position = mIndexer.getPositionForSection(section);

                            if (position != -1) {
                                mSpeakerListView.setSelection(position);
                            }
                        }
                    }
                });

                mSpeakerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Speaker bean = (Speaker) mAdAdapter.getItem(arg2);
                        getAllSessionAndMeetingBySpeakerId(bean.getSpeakerId());

                        View view = CommonUtils.initView(getActivity(), R.layout.title_right_image);
                        ((ImageView) view).setImageResource(R.drawable.scenicshow_ask_professor);

                        SpeakerDetailFragment speakerDetailFragment = new SpeakerDetailFragment();
                        speakerDetailFragment.setArguments(bean.getSpeakerId(), bean.getSpeakerName(), bean.getEnName(), mRoleIds.toString(), bean.getImg(), mAllMeetings, false);
                        speakerDetailFragment.setRightView(view);
                        action(speakerDetailFragment, R.string.speaker_detial, view, false, false, false);
                    }
                });
                mSpeakerListView.setOnScrollListener(mAdAdapter);
            }

            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mSpeakers = ConferenceDbUtils.getAllSpeakerWithOrder();
            Map<String, Integer> map = new HashMap<String, Integer>();

            for (int i = 0; i < sections.length; i++) {
                map.put(sections[i], 0);
            }

            for (int i = 0; i < mSpeakers.size(); i++) {
                Speaker bean = mSpeakers.get(i);
                String letter = bean.getFirstLetter();
                if (!map.containsKey(letter)) {
                    letter = sections[sections.length - 1];
                    bean.setFirstLetter(letter);
                }
                int count = map.get(bean.getFirstLetter());
                map.put(bean.getFirstLetter(), ++count);
            }

            for (int i = 0; i < sections.length; i++) {
                counts[i] = map.get(sections[i]);
            }

            if (counts[0] == 0) {
                String[] msectionslist = new String[sections.length - 1];
                int[] newcounts = new int[sections.length - 1];
                for (int i = 1; i < sections.length; i++) {
                    String section = sections[i];
                    msectionslist[i - 1] = section;
                    int count = counts[i];
                    newcounts[i - 1] = count;
                }
                sections = msectionslist;
                counts = newcounts;
            }

            mIndexer = new MySectionIndexer(sections, counts);
            mAdAdapter = new MettingCommunitySpeakerAdapter(mSpeakers, mIndexer);
            return null;
        }
    }
}
