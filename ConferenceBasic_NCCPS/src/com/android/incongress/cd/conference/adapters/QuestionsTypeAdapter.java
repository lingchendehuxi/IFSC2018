package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.fragments.question.QuestionByMeetingFragment;
import com.android.incongress.cd.conference.fragments.search_speaker.SpeakerSearchFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/12/22.
 * 日程提问的不同类型
 */

public class QuestionsTypeAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private static final int TITLE[] = {R.string.question_by_faculty, R.string.question_by_meeting};
    private SpeakerSearchFragment mSpeakerFragment;
    private QuestionByMeetingFragment mQuestionByMeeting;

    public QuestionsTypeAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.mContext = ctx;
        mSpeakerFragment = SpeakerSearchFragment.getInstance(SpeakerSearchFragment.TYPE_FROM_HOME);
        mQuestionByMeeting = new QuestionByMeetingFragment();
    }

    public SpeakerSearchFragment getSpeakerFragment() {
        return mSpeakerFragment;
    }

    public QuestionByMeetingFragment getMeetingFragment() {
        return  mQuestionByMeeting;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(TITLE[position]);
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? mSpeakerFragment : mQuestionByMeeting;
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }


}
