package com.android.incongress.cd.conference.fragments.professor_secretary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.SecretaryFragmentAdapter;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.SceneShowArrayBean;
import com.android.incongress.cd.conference.widget.StatusBarUtil;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jacky on 2016/2/22.
 */
public class SecretaryActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SecretaryFragmentAdapter mAdapter;

    private ImageView mIvBack;
    private TextView mTvProfessorName;

    private int mTaskNum,mQuestionNum;
    private static final String TASK_NUM = "taskNum";
    private static final String QUESTION_NUM = "questionNum";
    public static final String TASK_LIST = "taskList";
    public static final String QUESTION_LIST = "questionList";

    private List<ActivityBean> mActivitys;
    private List<SceneShowArrayBean> mQuestions;

    private String[] mTitles =new String[2];

    public static void startSecretaryActivity(Context ctx, int taskNum, int questionNum, List<ActivityBean> activityBeans, List<SceneShowArrayBean> sceneShowBeans) {
        Intent intent = new Intent();
        intent.setClass(ctx, SecretaryActivity.class);
        intent.putExtra(TASK_NUM, taskNum);
        intent.putExtra(QUESTION_NUM, questionNum);
        intent.putExtra(TASK_LIST, (Serializable) activityBeans);
        intent.putExtra(QUESTION_LIST, (Serializable) sceneShowBeans);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        try {
            mTaskNum = getIntent().getIntExtra(TASK_NUM, 0);
            mQuestionNum = getIntent().getIntExtra(QUESTION_NUM, 0);
            mActivitys = (List<ActivityBean>) getIntent().getSerializableExtra(TASK_LIST);
            mQuestions = (List<SceneShowArrayBean>) getIntent().getSerializableExtra(QUESTION_LIST);
        }catch (Exception e){
            e.printStackTrace();
        }

        mTitles[0] = getString(R.string.my_task, mTaskNum +"");
        mTitles[1] = getString(R.string.my_question, mQuestionNum + "");

        initViews();
        initEvents();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_secretary);
    }

    @Override
    protected void initViewsAction() {

    }

    private void initEvents() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecretaryActivity.this.finish();
            }
        });
    }

    private void initViews() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTvProfessorName = (TextView) findViewById(R.id.tv_professor_name);
        mTvProfessorName.setText(AppApplication.username);

        mAdapter = new SecretaryFragmentAdapter(getSupportFragmentManager(), mTitles, mActivitys, mQuestions);
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_SECRETARY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_SECRETARY);
    }
}
