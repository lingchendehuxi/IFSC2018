package com.android.incongress.cd.conference.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.android.incongress.cd.conference.fragments.question.MeetingQuestionFragment;
import com.android.incongress.cd.conference.fragments.question.PosterQuestionFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/12/22.
 */

public class QuestionsAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private static final int TITLE[] = {R.string.question_meeting, R.string.question_post};
    private static final int COUNTS[] = new int[2];
    private MeetingQuestionFragment mMeetingQuestionFragment;
    private PosterQuestionFragment mPostQuestionFragment;

    public QuestionsAdapter(FragmentManager fm, Context ctx, int questionMeetingCounts, int questionPostCounts) {
        super(fm);
        this.mContext = ctx;
        COUNTS[0] = questionMeetingCounts;
        COUNTS[1] = questionPostCounts;

        mMeetingQuestionFragment = new MeetingQuestionFragment();
        mPostQuestionFragment = new PosterQuestionFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return mContext.getString(TITLE[position],COUNTS[position]);
        return mContext.getString(TITLE[position]);
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ?  mMeetingQuestionFragment : mPostQuestionFragment;
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }
}
