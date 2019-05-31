package com.android.incongress.cd.conference.ui.login.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.LoginForUpdateInfoActivity;
import com.android.incongress.cd.conference.RegisterActivity;
import com.android.incongress.cd.conference.WxForLoginActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.mvp.BaseMVPActivity;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.ui.login.contract.LoginContract;
import com.android.incongress.cd.conference.ui.login.presenter.LoginActivityPresenter;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.phonecode.CountDownButton;
import com.android.incongress.cd.conference.widget.phonecode.FocusPhoneCode;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Jacky on 2016/1/29.
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener ,LoginContract.View{
    private EditText mEtMobile, mEtPswCode, mEtFamilyName, mEtGivenName;
    private Button mBtLogin, mBtCode;
    private TextView tv_title, tv_number_code, mTvGoRegister;
    //private CheckBox mCbRegist;
    private ImageView backimg, we_login;
    private String mUserSMS, mUserMobile, mFamilyName, mGivenName;
    private String mCountryCode = "";
    private View view_background;
    private LinearLayout ll_getcode, ll_login, ll_bottom_wx;
    private CountDownButton countDownTextView;

    private int mCurrentType = 1;
    private ProgressDialog mProgressDialog;
    FocusPhoneCode mFpc;
    private String nickName, imgUrl, sex;
    private static int LOGIN_CODE_RESULT = 1000;
    private static int LOGIN_UPDATA_CODE_RESULT = 1001;

    //必须输入注册码类型，不能取消勾选框
    public static final String LOGIN_TYPE = "loginType";
    public static final String LOGIN_USERNAME = "loginUserName";
    public static final String LOGIN_USERMOBILE = "loginUserMobile";
    public static final String LOGIN_FAMILY_NAME = "familyName";
    public static final String LOGIN_GIVEN_NAME = "givenName";

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_SECRETARY = 2;
    public static final int TYPE_PROFESSOR = 3;
    public static final int REQUEST_COUNTRY_CODE = 4;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    public static final void startLoginActivity(Context ctx, int type, String userName, String mobilePhone, String familyName, String givenName) {
        Intent intent = new Intent();
        intent.setClass(ctx, LoginActivity.class);
        intent.putExtra(LOGIN_TYPE, type);
        intent.putExtra(LOGIN_USERNAME, userName);
        intent.putExtra(LOGIN_USERMOBILE, mobilePhone);
        intent.putExtra(LOGIN_FAMILY_NAME, familyName);
        intent.putExtra(LOGIN_GIVEN_NAME, givenName);
        ctx.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_getcode);
    }

    @Override
    protected void initViewsAction() {
        //不可以再强制退出
        SharePreferenceUtils.saveAppBoolean(Constants.FORCE_LOGOUT,true);
        try {
            mCurrentType = getIntent().getIntExtra(LOGIN_TYPE, TYPE_NORMAL);
            mUserMobile = getIntent().getStringExtra(LOGIN_USERMOBILE);
            mFamilyName = getIntent().getStringExtra(LOGIN_FAMILY_NAME);
            mGivenName = getIntent().getStringExtra(LOGIN_GIVEN_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppApplication.systemLanguage == 1) {
            initChinese();
        } else {
            initEnglish();
        }
    }

    private void initChinese() {
        //公共部分
        mEtMobile = findViewById(R.id.et_mobile);
        //StringUtils.setViewTextSize(mEtMobile,getResources().getString(R.string.telephone_number));
        //mEtRegistCode = (EditText) findViewById(R.id.et_regist_code);
        mBtLogin = findViewById(R.id.bt_login);
        mBtLogin.setOnClickListener(this);
        mBtCode = findViewById(R.id.bt_getcode);
        mBtLogin.setEnabled(false);
        mBtLogin.setClickable(false);
        mBtCode.setEnabled(false);
        mBtCode.setClickable(false);
        mBtCode.setOnClickListener(this);
        //mCbRegist = (CheckBox) findViewById(R.id.cb_regist);
        backimg = findViewById(R.id.iv_back);
        backimg.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.incongress_login));
        mBtCode.getBackground().setAlpha(204);
        mBtLogin.getBackground().setAlpha(204);
        view_background = findViewById(R.id.view_background);
        ll_getcode = findViewById(R.id.ll_getcode);
        ll_login = findViewById(R.id.ll_login);
        ll_bottom_wx = findViewById(R.id.ll_bottom_wx);
        tv_number_code = findViewById(R.id.tv_number_code);
        we_login = findViewById(R.id.we_login);
        we_login.setOnClickListener(this);
        mFpc = findViewById(R.id.fpc);
        countDownTextView = findViewById(R.id.countDownTextView);
        countDownTextView.setOnClickListener(this);
        mCountryCode = "86";
        if (!StringUtils.isEmpty(mUserMobile)) {
            mEtMobile.setText(mUserMobile);
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
        mEtMobile = findViewById(R.id.et_mobile);
        mEtPswCode = findViewById(R.id.et_psw);
        mBtLogin = findViewById(R.id.bt_login);
        tv_title = findViewById(R.id.tv_title);
        mTvGoRegister = findViewById(R.id.tv_go_regist);
        backimg = findViewById(R.id.iv_back);
        backimg.setOnClickListener(this);
        tv_title.setText(getString(R.string.login));

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //公共部分
        mEtMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mEtMobile.setHintTextColor(getResources().getColor(R.color.gray));
                } else {
                    mEtMobile.setHintTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        mTvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startActivity(LoginActivity.this);
            }
        });

        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mEtMobile.getText().toString();
                String pswCode = mEtPswCode.getText().toString();

                if (TextUtils.isEmpty(mobile)) {
                    ToastUtils.showToast(getString(R.string.phone_can_not_empty));
                } else if (TextUtils.isEmpty(pswCode)) {
                    ToastUtils.showToast(getString(R.string.psw_can_not_empty));
                } else {
                    ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setCanceledOnTouchOutside(false);
                    presenter.doLoginbyEmail(mobile, pswCode, pd);
                }
            }
        });
    }
    //登录成功标识符
    public static final String LOGIN_ACTION = "login";
    //退出登录标识符
    public static final String LOGOUT_ACTION = "logout";
    //去获取验证码
    private void toPutSms(){
        mUserMobile = mEtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mUserMobile)) {
            ToastUtils.showToast("请先输入手机号");
            return;
        }
        ll_getcode.setVisibility(View.GONE);
        ll_login.setVisibility(View.VISIBLE);
        ll_bottom_wx.setVisibility(View.GONE);
        we_login.setVisibility(View.GONE);
        countDownTextView.setCountDownMillis(60000);
        countDownTextView.start();
        tv_number_code.setText("验证码已发送   " + mUserMobile);
        ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        presenter.doGetSms(mUserMobile.replaceAll(" ",""),pd);
    }

    @Override
    public void showDialog(String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(msg).setPositiveButton(R.string.positive_button, okListener).setCancelable(false).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_COUNTRY_CODE) {
                String code = data.getStringExtra("code");
                //mTvCountryCode.setText("(+" + code + ")");
                mCountryCode = code;
            } else if (requestCode == LOGIN_CODE_RESULT || requestCode == LOGIN_UPDATA_CODE_RESULT) {
                this.finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if (!TextUtils.isEmpty(mUserMobile) && !TextUtils.isEmpty(mUserSMS)) {
                    ProgressDialog pd = new ProgressDialog(this);
                    pd.setCanceledOnTouchOutside(false);
                    presenter.doLogin___(mUserMobile.replaceAll(" ",""),mUserSMS,pd);
                    hideShurufa();
                }
                break;
            case R.id.we_login:
                if (ActivityUtils.isWxInstall(getApplicationContext())) {
                    authorization(SHARE_MEDIA.WEIXIN);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.dialog_tips).setMessage("检测到您手机没有安装微信，请安装后使用该功能").setPositiveButton("去下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://weixin.qq.com/m"));
                            startActivity(intent);
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                    break;
                    //直接跳转
                    //ToastUtils.showToast("检测到您手机没有安装微信，请安装后使用该功能");
                }

                break;
            case R.id.iv_back:
                if (AppApplication.systemLanguage == 1) {
                    if (ll_getcode.getVisibility() == View.VISIBLE) {
                        finish();
                    } else {
                        ll_getcode.setVisibility(View.VISIBLE);
                        ll_bottom_wx.setVisibility(View.VISIBLE);
                        we_login.setVisibility(View.VISIBLE);
                        ll_login.setVisibility(View.GONE);
                        mFpc.clearData();
                    }
                } else {
                    finish();
                }

                break;
            case R.id.bt_getcode:
            case R.id.countDownTextView:
                toPutSms();
                hideShurufa();
                break;

        }
    }

    //微信登录授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(AppApplication.getContext()).getPlatformInfo(LoginActivity.this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d("myTest", "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d("myTest", "onComplete: " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                nickName = map.get("name");
                sex = map.get("gender");
                imgUrl = map.get("iconurl");
                ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setCanceledOnTouchOutside(false);
                presenter.loginWX(openid,pd);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d("myTest", "onError: " + "授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d("myTest", "onCancel: " + "授权取消");
            }
        });
    }
    //处理微信登陆
    @Override
    public void handleWXLogin(JSONObject response){
        if (JSONCatch.parseInt("state", response)==1) {
            if (TextUtils.isEmpty(JSONCatch.parseString("name", response)) || TextUtils.isEmpty(JSONCatch.parseString("img", response))) {
                presenter.doModifyPersonInfo(String.valueOf(JSONCatch.parseInt(Constants.USER_IC_ID,response)),imgUrl,nickName);
            }
            if (TextUtils.isEmpty(JSONCatch.parseString("mobilePhone", response)) && !TextUtils.isEmpty(JSONCatch.parseString("openId", response))) {
                Intent intent = new Intent(LoginActivity.this, WxForLoginActivity.class);
                intent.putExtra(Constants.USER_OPENID, JSONCatch.parseString(Constants.USER_OPENID, response));
                intent.putExtra(Constants.USER_NICK_NAME, nickName);
                intent.putExtra(Constants.USER_IMG, imgUrl);
                intent.putExtra(Constants.USER_SEX, sex);
                startActivityForResult(intent, LOGIN_CODE_RESULT);
            } else {
                ParseUser.saveUserInfo(response.toString());
                SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                finish();
            }
        }
    }

    //对邮箱登陆处理
    @Override
    public void handleEmailLogin(JSONObject response){
        if (JSONCatch.parseInt("state", response) == 1) {
            if (TextUtils.isEmpty(JSONCatch.parseString("name", response)) || TextUtils.isEmpty(JSONCatch.parseString("img", response))) {
                Intent intent = new Intent(LoginActivity.this, LoginForUpdateInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_MOBILE, mUserMobile.replaceAll(" ", ""));
                bundle.putString("sms", mUserSMS);
                bundle.putString(Constants.USER_IC_ID, String.valueOf(JSONCatch.parseInt(Constants.USER_IC_ID, response)));
                bundle.putString(Constants.USER_TYPE, JSONCatch.parseString(Constants.USER_TYPE, response));
                intent.putExtras(bundle);
                mFpc.clearData();
                startActivityForResult(intent, LOGIN_UPDATA_CODE_RESULT);
                return;
            }
            ParseUser.saveUserInfo(response.toString());
            SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);
                    /*Intent loginIntent = new Intent();
                    loginIntent.setAction(LOGIN_ACTION);
                    sendBroadcast(loginIntent);*/
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        } else {
            ToastUtils.showToast(JSONCatch.parseString("msg", response));
        }

    }

    //对获取到是否有用户名和头像处理
    @Override
    public void handleUserNameAndImg(JSONObject response){
        if (JSONCatch.parseInt("state", response) == 1) {
            if (TextUtils.isEmpty(JSONCatch.parseString("name", response)) || TextUtils.isEmpty(JSONCatch.parseString("img", response))) {
                Intent intent = new Intent(LoginActivity.this, LoginForUpdateInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_MOBILE, mUserMobile.replaceAll(" ", ""));
                bundle.putString("sms", mUserSMS);
                bundle.putString(Constants.USER_IC_ID, String.valueOf(JSONCatch.parseInt(Constants.USER_IC_ID, response)));
                bundle.putString(Constants.USER_TYPE, JSONCatch.parseString(Constants.USER_TYPE, response));
                intent.putExtras(bundle);
                mFpc.clearData();
                startActivityForResult(intent, LOGIN_UPDATA_CODE_RESULT);
                return;
            }
            ParseUser.saveUserInfo(response.toString());
            SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);
                    /*Intent loginIntent = new Intent();
                    loginIntent.setAction(LOGIN_ACTION);
                    sendBroadcast(loginIntent);*/
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();

        } else {
            ToastUtils.showToast(JSONCatch.parseString("msg", response));
        }
    }

    private LoginContract.Presenter presenter = new LoginActivityPresenter(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }
}
