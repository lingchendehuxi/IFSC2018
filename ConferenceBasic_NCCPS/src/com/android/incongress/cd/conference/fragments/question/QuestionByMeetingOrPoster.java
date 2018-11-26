package com.android.incongress.cd.conference.fragments.question;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.incongress.cd.conference.adapters.QuestionsTypeAdapter;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.model.Class;
import com.android.incongress.cd.conference.model.ConferenceDbUtils;
import com.android.incongress.cd.conference.model.Session;
import com.android.incongress.cd.conference.widget.popup.BasePopupWindow;
import com.android.incongress.cd.conference.widget.popup.ChooseRomPopupWindow;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacky on 2016/12/29.
 * 通过检索讲者或者会议室发起提问
 */

public class QuestionByMeetingOrPoster extends BaseFragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private QuestionsTypeAdapter mQuestionTypeAdapter;
    private View mDownView;

    /**
     * 当前时间和选择的会议室
     */
    private ChooseRomPopupWindow mRoomPopupWindow;
    private List<Class> mChooseClasses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_by_meeting_or_poster, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

        mQuestionTypeAdapter = new QuestionsTypeAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mQuestionTypeAdapter);

        if(mDownView != null) {
            mDownView.setVisibility(View.GONE);
            mDownView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRoomPopupWindow == null) {
                        mRoomPopupWindow = new ChooseRomPopupWindow(getActivity());

                        mRoomPopupWindow.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                mChooseClasses = mRoomPopupWindow.getCurrentClass();
                                //刷新当前会议室
//                                getSessionsAndMeetings(mChooseClasses);
                                List<Session> sessions = new ArrayList<Session>();

                                if(mChooseClasses != null && mChooseClasses.size() > 0 ){
                                    for (int i = 0; i < mChooseClasses.size(); i++) {
                                        String classId = mChooseClasses.get(i).getClassesId() + "";

                                        List<Session> sessionByRoom = ConferenceDbUtils.getSessionByRoom(classId);
                                        if(sessionByRoom!= null && sessionByRoom.size()>0) {
                                            sessions.addAll(sessionByRoom);
                                        }
                                    }
                                }else {
                                    sessions.addAll(ConferenceDbUtils.getAllSession());
                                }

                                mQuestionTypeAdapter.getMeetingFragment().refreshSessions(sessions);
                            }
                        });
                    }
                    mRoomPopupWindow.showPopupWindowBelowView(mDownView);
                }
            });
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                    mDownView.setVisibility(View.GONE);
                else
                    mDownView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        return view;
    }

    public void setRightView(View view) {
        if(view != null) {
            mDownView = view;
        }
    }
}
