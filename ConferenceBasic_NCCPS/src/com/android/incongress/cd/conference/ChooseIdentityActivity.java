package com.android.incongress.cd.conference;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RadioGroup;

import com.android.incongress.cd.conference.base.BaseActivity;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jacky on 2016/8/25.
 * 选择身份类型
 */
public class ChooseIdentityActivity extends BaseActivity {
    @BindView(R.id.rg_group)
    RadioGroup mRradioGroup;

    private int REQUEST_CODE_LOGIN_PARTICIPANT = 0X0001;
    private boolean mIsFromSplash = false;

    public static void startChooseIdentityActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ChooseIdentityActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        mIsFromSplash = getIntent().getBooleanExtra("fromSplash", false);
        setContentView(R.layout.activity_choose_identity);
    }

    @Override
    protected void initViewsAction() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==  RESULT_OK) {
            if(requestCode == REQUEST_CODE_LOGIN_PARTICIPANT) {
                if(mIsFromSplash) {
                    Intent intent = new Intent(ChooseIdentityActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

    @OnClick(R.id.rb_paticipant)
    void onParticipant() {
        Intent intent = new Intent();
        intent.setClass(ChooseIdentityActivity.this, ParticipantLoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_PARTICIPANT);
    }

    @OnClick(R.id.rb_unregister_one)
    void OnUnRegisterOne() {
        Intent intent = new Intent();
        intent.setClass(ChooseIdentityActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_PARTICIPANT);
    }

    @OnClick(R.id.rb_unregister_two)
    void OnUnRegisterTwo() {
        Intent intent = new Intent();
        intent.setClass(ChooseIdentityActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_PARTICIPANT);
    }
}
