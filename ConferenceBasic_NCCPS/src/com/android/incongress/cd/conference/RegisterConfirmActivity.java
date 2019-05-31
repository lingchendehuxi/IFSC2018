package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.UserInfoBean;
import com.android.incongress.cd.conference.beans.UserInfoEnBean;
import com.android.incongress.cd.conference.fragments.me.PersonCenterFragment;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.ui.login.view.LoginActivity;
import com.android.incongress.cd.conference.widget.ClearEditText;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Jacky on 2016/2/4.
 */
public class RegisterConfirmActivity extends BaseActivity {
    private ClearEditText mCetConfirm, mCetName, mCetGivenName,mCetFamilyName;
    private Button mBtRegister;
    private TextView mTvTitle,mTvGetConfirm;
    private ImageView mIvBack;
    private String mMobile;
    public static final String BUNDLE_MOBILE = "mobile";

    public static void startActivity(Context ctx, String mobile) {
        Intent intent = new Intent();
        intent.setClass(ctx, RegisterConfirmActivity.class);
        intent.putExtra(BUNDLE_MOBILE, mobile);
        ctx.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMobile = getIntent().getStringExtra(BUNDLE_MOBILE);
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        initEvents();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_register_confirm);
    }

    @Override
    protected void initViewsAction() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        mIvBack = (ImageView)findViewById(R.id.title_back);
        mBtRegister = (Button) findViewById(R.id.bt_register);
        mCetConfirm = (ClearEditText) findViewById(R.id.cet_confirm_code);

        mTvTitle = (TextView) findViewById(R.id.title_text);

        mTvTitle.setText(R.string.register_title);

        if(AppApplication.systemLanguage == 1) {
            mTvGetConfirm = (TextView) findViewById(R.id.tv_get_confirm);
            mCetName = (ClearEditText) findViewById(R.id.cet_name);
        }else {
            mCetFamilyName = (ClearEditText) findViewById(R.id.cet_family_name);
            mCetGivenName = (ClearEditText) findViewById(R.id.cet_given_name);
        }
    }

    private void initEvents() {
        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmCode = mCetConfirm.getText().toString().trim();
                if(confirmCode.length()<6){
                    ToastUtils.showToast("Password should not be less than 6 digits.");
                    return;
                }
                if(AppApplication.systemLanguage == 1) {
                    String name = mCetName.getText().toString().trim();
                    try {
                        name = URLEncoder.encode(name, Constants.ENCODING_UTF8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (!StringUtils.isAllNotEmpty(name, mMobile, confirmCode)) {
                        ToastUtils.showToast(getString(R.string.complete_your_info));
                    } else {
                        doMobileRegister(name,"", "", mMobile,confirmCode,Constants.LanguageChinese);
                    }
                }else {
                    String familyName = mCetFamilyName.getText().toString();
                    String givenName = mCetGivenName.getText().toString();

                    if (!StringUtils.isAllNotEmpty(givenName, mMobile, confirmCode)) {
                        ToastUtils.showToast(getString(R.string.complete_your_info));
                    } else {
                        doEmailRegister("",givenName, familyName, mMobile, confirmCode, Constants.LanguageEnglish);
                    }
                }
        }});
        if(AppApplication.systemLanguage == 1) {
            mTvGetConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppApplication.systemLanguage == 1) {
                        doGetSms(mMobile,Constants.LanguageChinese);
                    }
                }
            });
        }

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterConfirmActivity.this.finish();
            }
        });
    }

    private void showDialog(String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_tips)).setMessage(msg).setPositiveButton(R.string.positive_button, okListener).setCancelable(false).show();
    }

    private void doGetSms(String mobile, String lan) {
        CHYHttpClientUsage.getInstanse().doGetSmsMobile(Constants.getConId(), mobile, Constants.ConfirmTypeRegister,lan ,new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(RegisterConfirmActivity.this, null, "loading...");
            }

            /*
             返回内容{"state":1,"code":"156114","msg":""}
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");
                    String msg = response.getString("msg");

                    if (state == 0) {
                        showDialog(msg, null);
                    } else {
                        ToastUtils.showToast(getString(R.string.success_send_regist_code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    private void doMobileRegister(String name, String givenName, String familyName,String mobile, String confirmCode, String language) {
        CHYHttpClientUsage.getInstanse().doMobileRegUser(name, familyName, givenName, mobile, Constants.getFromWhere(),language, confirmCode, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            @Override
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(RegisterConfirmActivity.this, null, "loading...");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                ToastUtils.showToast(getString(R.string.server_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }

            /*
              注册成功： {"name":"闄堝己","state":1,"userType":1,"userId":15988,"mobilePhone(":"15000979730","img":""}
              注册失败： {"state":-1,"msg":"您注册的账号已存在，请直接登录"}
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                MyLogger.jLog().i(response.toString());
                try {
                    int state = response.getInt("state");
                    if (state == -1) {
                        String msg = response.getString("msg");
                        showDialog(msg, null);
                    } else {
                        ParseUser.saveUserInfo(response.toString());

                        SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);

                        Intent intent = new Intent(RegisterConfirmActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //发送广播
                        Intent loginIntent = new Intent();
                        loginIntent.setAction(LoginActivity.LOGIN_ACTION);
                        sendBroadcast(loginIntent);

                        ToastUtils.showToast(getString(R.string.register_success));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void doEmailRegister(String name, String givenName, String familyName,String mobile, String confirmCode, String language) {
        try {
            mobile = URLEncoder.encode(mobile, Constants.ENCODING_UTF8);
            givenName = URLEncoder.encode(givenName, Constants.ENCODING_UTF8);
            familyName = URLEncoder.encode(familyName, Constants.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CHYHttpClientUsage.getInstanse().doEmailRegUser(name, familyName, givenName, mobile, confirmCode, Constants.getFromWhere(),language, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                @Override
                public void onStart() {
                    super.onStart();
                    mProgressDialog = ProgressDialog.show(RegisterConfirmActivity.this, null, "loading...");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    ToastUtils.showToast(getString(R.string.server_error));
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressDialog.dismiss();
                }

                /*
                  注册成功： {"name":"闄堝己","state":1,"userType":1,"userId":15988,"mobilePhone(":"15000979730","img":""}
                  注册失败： {"state":-1,"msg":"您注册的账号已存在，请直接登录"}
                 */
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    MyLogger.jLog().i(response.toString());
                    try {
                        int state = response.getInt("state");
                        if (state == -1) {
                            String msg = response.getString("msg");
                            showDialog(msg, null);
                        } else {
                            ParseUser.saveUserInfo(response.toString());
                            SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);
                            /*Gson gson = new Gson();
                            UserInfoEnBean user = gson.fromJson(response.toString(), UserInfoEnBean.class);

                            SharePreferenceUtils.saveUserString(Constants.USER_IS_LOGIN, "true");
                            AppApplication.setSPStringValue(Constants.USER_NAME, user.getName());
                            AppApplication.setSPStringValue(Constants.USER_IMG, user.getImg());
                            AppApplication.setSPStringValue(Constants.USER_MOBILE, user.getEmail());
                            AppApplication.setSPIntegerValue(Constants.USER_ID, user.getUserId());
                            AppApplication.setSPIntegerValue(Constants.USER_TYPE, user.getUserType());

                            AppApplication.userId = user.getUserId();
                            AppApplication.username = user.getName();
                            AppApplication.userType = user.getUserType();*/

                            Intent intent = new Intent(RegisterConfirmActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //发送广播
                            Intent loginIntent = new Intent();
                            loginIntent.setAction(LoginActivity.LOGIN_ACTION);
                            sendBroadcast(loginIntent);

                            ToastUtils.showToast(getString(R.string.register_success));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
