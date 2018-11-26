package com.android.incongress.cd.conference.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.fragments.professor_secretary.ProfessorQuestionActionFragment;
import com.android.incongress.cd.conference.fragments.professor_secretary.ProfessorTaskActionFragment;

import java.util.List;

/**
 * Created by Jacky on 2016/1/21.
 */
public class SecretaryFragmentAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private String[] mTitles;
    private List<ActivityBean> mTasks;
    private List<SceneShowArrayBean> mQuestions;

    public SecretaryFragmentAdapter(FragmentManager fm, String[] titles, List<ActivityBean> activityBeans, List<SceneShowArrayBean> questions) {
        super(fm);
        this.mTitles = titles;
        this.mTasks = activityBeans;
        this.mQuestions = questions;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ProfessorTaskActionFragment.getInstance(mTasks);
        }else
            return ProfessorQuestionActionFragment.getInstance(mQuestions);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}