package com.android.incongress.cd.conference;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.newf.ChooseConferenceFragment;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Jacky on 2016/10/26.
 * 选择大会页面
 */

public class ConferenceActivity extends BaseActivity {
    private FragmentManager mFragmentManager;
    private ChooseConferenceFragment mChooseConferenceFragment;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_choose_conference);
    }

    @Override
    protected void initViewsAction() {
        mChooseConferenceFragment =  new ChooseConferenceFragment();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_container, mChooseConferenceFragment, ChooseConferenceFragment.class.getName());
        fragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.ACTIVITY_CONFERENCES);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.ACTIVITY_CONFERENCES);
    }
}
