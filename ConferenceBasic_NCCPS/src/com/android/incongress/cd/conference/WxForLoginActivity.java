package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.phonecode.CountDownButton;
import com.android.incongress.cd.conference.widget.phonecode.FocusPhoneCode;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/29.
 * 登录页面
 */
public class WxForLoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mEtMobile, mEtRegistCode, mEtFamilyName, mEtGivenName, mEtName;
    private Button mBtLogin, mBtCode;
    private TextView tv_title, tv_number_code,title_tip;
    //private CheckBox mCbRegist;
    private ImageView backimg,we_login;
    private String mUserSMS, mUserMobile, openId, nickName,imgUrl,sex;
    private View view_background;
    private LinearLayout ll_getcode, ll_login, ll_bottom_wx;
    private CountDownButton countDownTextView;

    private int mCurrentType = 1;
    private ProgressDialog mProgressDialog;
    FocusPhoneCode mFpc;

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SECRETARY = 2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_getcode);
        //公共部分
        mEtMobile =  findViewById(R.id.et_mobile);
        mBtLogin =  findViewById(R.id.bt_login);
        mBtCode =  findViewById(R.id.bt_getcode);
        backimg =  findViewById(R.id.iv_back);
        we_login = findViewById(R.id.we_login);
        tv_title =  findViewById(R.id.tv_title);
        title_tip = findViewById(R.id.title_tip);
        view_background = findViewById(R.id.view_background);
        ll_getcode =  findViewById(R.id.ll_getcode);
        ll_login =  findViewById(R.id.ll_login);
        ll_bottom_wx =  findViewById(R.id.ll_bottom_wx);
        tv_number_code =  findViewById(R.id.tv_number_code);
        mFpc =  findViewById(R.id.fpc);
        countDownTextView =  findViewById(R.id.countDownTextView);
    }

    @Override
    protected void initViewsAction() {
        openId = getIntent().getStringExtra(Constants.USER_OPENID);
        nickName = getIntent().getStringExtra(Constants.USER_NICK_NAME);
        imgUrl = getIntent().getStringExtra(Constants.USER_IMG);
        sex = getIntent().getStringExtra(Constants.USER_SEX);
        mBtLogin.setOnClickListener(this);
        mBtLogin.setEnabled(false);
        mBtLogin.setClickable(false);
        mBtCode.setEnabled(false);
        mBtCode.setClickable(false);
        mBtCode.setOnClickListener(this);
        we_login.setVisibility(View.INVISIBLE);
        ll_bottom_wx.setVisibility(View.INVISIBLE);
        backimg.setOnClickListener(this);
        tv_title.setText(getString(R.string.incongress_fix_mobile));
        title_tip.setText(getString(R.string.fix_mobile_tip));
        mBtCode.getBackground().setAlpha(204);
        mBtLogin.getBackground().setAlpha(204);

        countDownTextView.setOnClickListener(this);

        if (AppApplication.systemLanguage == 1) {
            initChinese();
        } else {
            initEnglish();
        }

        mEtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                //删除数字
                if (count == 0) {
                    if (length == 4) {
                        mEtMobile.setText(s.subSequence(0, 3));
                    }
                    if (length == 9) {
                        mEtMobile.setText(s.subSequence(0, 8));
                    }
                }
                //添加数字
                if (count == 1) {
                    if (length == 4) {
                        String part1 = s.subSequence(0, 3).toString();
                        String part2 = s.subSequence(3, length).toString();
                        mEtMobile.setText(part1 + " " + part2);
                    }
                    if (length == 9) {
                        String part1 = s.subSequence(0, 8).toString();
                        String part2 = s.subSequence(8, length).toString();
                        mEtMobile.setText(part1 + " " + part2);
                    }
                }
                if (s.toString().length() == 13) {
                    mBtCode.setEnabled(true);
                    mBtCode.setClickable(true);
                    mBtCode.getBackground().setAlpha(255);
                    view_background.setBackgroundColor(getResources().getColor(R.color.theme_color));
                } else if (!TextUtils.isEmpty(s.toString())) {
                    mBtCode.setEnabled(false);
                    mBtCode.setClickable(false);
                    mBtCode.getBackground().setAlpha(204);
                    view_background.setBackgroundColor(getResources().getColor(R.color.theme_color));
                } else {
                    mBtCode.setEnabled(false);
                    mBtCode.setClickable(false);
                    mBtCode.getBackground().setAlpha(204);
                    view_background.setBackgroundColor(getResources().getColor(R.color.login_gray));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //将光标移动到末尾
                mEtMobile.setSelection(mEtMobile.getText().toString().length());
            }
        });

        //以下代码不用验证了，有些会议可能单独需要
        if (mCurrentType == TYPE_SECRETARY) {
            //mCbRegist.setVisibility(View.GONE);
        }

        mFpc.setFinishListener(new FocusPhoneCode.FinishListener() {
            @Override
            public void isFinish(boolean isFinish) {
                if (isFinish) {
                    mBtLogin.setEnabled(true);
                    mBtLogin.setClickable(true);
                    mBtLogin.getBackground().setAlpha(255);
                    mUserSMS = mFpc.getPhoneCode();
                    mBtLogin.performClick();
                } else {
                    mBtLogin.setEnabled(false);
                    mBtLogin.setClickable(false);
                    mBtLogin.getBackground().setAlpha(204);
                }
            }
        });
    }

    private void initEnglish() {
        //mTvCountryCode = (TextView) findViewById(R.id.tv_country_code);

        mEtGivenName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtGivenName.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtGivenName.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        mEtGivenName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtGivenName.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtGivenName.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        if (!StringUtils.isEmpty(mUserMobile)) {
            mEtMobile.setText(mUserMobile);
        }
    }

    private void initChinese() {

        if (!StringUtils.isEmpty(mUserMobile)) {
            mEtMobile.setText(mUserMobile);
        }
    }


    //登录成功标识符
    public static final String LOGIN_ACTION = "login";
    //退出登录标识符
    public static final String LOGOUT_ACTION = "logout";

    private void doLoginForWx(String mobile, String sms) {
        CHYHttpClientUsage.getInstanse().doUpdateUserInfo(mobile.replaceAll(" ",""), sms,openId,nickName,imgUrl,sex,new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(WxForLoginActivity.this, null, "loading...");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ToastUtils.showToast("服务器开小差了，请稍后重试");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(JSONCatch.parseInt("state",response) == 0){
                    ToastUtils.showToast(JSONCatch.parseString("msg",response));
                }else {
                    ParseUser.saveUserInfo(response.toString());
                    SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);
                        /*AppApplication.userId = user.getUserId();
                        AppApplication.username = user.getName();
                        AppApplication.userType = user.getUserType();*/

//                        setResult(RESULT_OK);
                    //发送广播
                    Intent loginIntent = new Intent();
                    loginIntent.setAction(LOGIN_ACTION);
                    sendBroadcast(loginIntent);
                    setResult(RESULT_OK);
                    finish();
                }

            }
        });
    }

    private void doGetSms(String mobile, String lan) {
        CHYHttpClientUsage.getInstanse().doGetSmsMobile(Constants.getConId(), mobile.replaceAll(" ",""), Constants.ConfirmTypeLogin, lan, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(WxForLoginActivity.this, null, "loading...");
            }

            /*
             返回内容{"state":1,"code":"156114","msg":""}
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                MyLogger.jLog().i(response.toString());
                String msg = JSONCatch.parseString("msg", response);
                if (JSONCatch.parseInt("state", response) == 0) {
                    showDialog(msg, null);
                } else {
                    ToastUtils.showToast(getString(R.string.success_send_regist_code));
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast(getString(R.string.server_error));
            }
        });
    }

    private void showDialog(String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(msg).setPositiveButton(R.string.positive_button, okListener).setCancelable(false).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if (!TextUtils.isEmpty(mUserMobile) && !TextUtils.isEmpty(mUserSMS)) {
                    doLoginForWx(mUserMobile, mUserSMS);
                }
                break;
            case R.id.countDownTextView:
                break;
            case R.id.iv_back:
                if (ll_getcode.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    ll_getcode.setVisibility(View.VISIBLE);
                    ll_login.setVisibility(View.GONE);
                }

                break;
            case R.id.bt_getcode:
                mUserMobile = mEtMobile.getText().toString().trim();
                if(TextUtils.isEmpty(mUserMobile)){
                    ToastUtils.showToast("请先输入手机号");
                    return;
                }
                if (AppApplication.systemLanguage == 1) {
                    ll_getcode.setVisibility(View.GONE);
                    ll_login.setVisibility(View.VISIBLE);
                    countDownTextView.setCountDownMillis(30000);
                    countDownTextView.start();
                    tv_number_code.setText("验证码已发送   " + mUserMobile);
                    doGetSms(mUserMobile, Constants.LanguageChinese);
                } else {
                    String familyName = mEtFamilyName.getText().toString();
                    String givenName = mEtGivenName.getText().toString();
                }
                break;

        }
    }
}
