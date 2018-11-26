package com.android.incongress.cd.conference;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jacky on 2016/8/25.
 */
public class FindbackCCodeFirstActivity extends BaseActivity {
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.rb_team)
    RadioButton rbTeam;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_findback_ccode);
    }

    @Override
    protected void initViewsAction() {
        initTitleBar(getString(R.string.findback_ccode_title));

        if(AppApplication.systemLanguage != 1) {
            rbTeam.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.rb_person)
    void onPerson() {
        Intent intent = new Intent();
        intent.setClass(FindbackCCodeFirstActivity.this, FindbackCCodeSecondActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.rb_team)
    void onTeam(){
        showOnlyOneDialog(getString(R.string.find_back_team_tips));
    }

    @OnClick(R.id.rb_faculty
    )
    void onFaculty() {
        Intent intent = new Intent();
        intent.setClass(FindbackCCodeFirstActivity.this, FindbackCCodeSecondActivity.class);
        startActivity(intent);
    }

}
