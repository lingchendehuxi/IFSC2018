package com.android.incongress.cd.conference;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.UserInfoBean;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.ActivityUtils;
import com.android.incongress.cd.conference.utils.LogUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Jacky on 2016/8/25.
 */
public class ParticipantLoginActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_ccode)
    EditText mEtCode;
    @BindView(R.id.name_tips)
    TextView nameTips;
    @BindView(R.id.mobile_tips)
    TextView mobileTips;
    @BindView(R.id.ll_ccode)
    LinearLayout mLlCode;
    @BindView(R.id.ccode)
    ImageView mIvCodeTips;
    @BindView(R.id.ccode_info)
    TextView mTvCodeTips;

    AnimationSet mInAnimation;
    AnimationSet mOutAnimation;

    public static void startParticipantLoginActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ParticipantLoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_participant_login);
    }

    @Override
    protected void initViewsAction() {
        mEtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    nameTips.setVisibility(View.VISIBLE);
                else
                    nameTips.setVisibility(View.INVISIBLE);
            }
        });

        mEtMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mobileTips.setVisibility(View.VISIBLE);
                else
                    mobileTips.setVisibility(View.INVISIBLE);
            }
        });

        mInAnimation = new AnimationSet(true);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(500);
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        mInAnimation.addAnimation(alpha);
        mInAnimation.addAnimation(animation);

        mOutAnimation = new AnimationSet(true);
        AlphaAnimation alphaOut = new AlphaAnimation(1,0);
        alphaOut.setDuration(500);
        ScaleAnimation animationOut = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationOut.setDuration(500);
        animationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLlCode.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mOutAnimation.addAnimation(alphaOut);
        mOutAnimation.addAnimation(animationOut);

        //设置特殊字体颜色
        SpannableStringBuilder builder = new SpannableStringBuilder(mTvCodeTips.getText().toString());
        ForegroundColorSpan theme = new ForegroundColorSpan(ContextCompat.getColor(ParticipantLoginActivity.this, R.color.theme_color));
        if(AppApplication.systemLanguage == 1)
            builder.setSpan(theme, mTvCodeTips.getText().length() - 4, mTvCodeTips.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        else {
            builder.setSpan(theme, mTvCodeTips.getText().length() - 22, mTvCodeTips.getText().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTvCodeTips.setText(builder);
    }

    @OnClick(R.id.find_back_ccode)
    void findBackCCode() {
        Intent intent = new Intent(this, FindbackCCodeFirstActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login)
    void login() {
        String name = mEtName.getText().toString().trim();
        String mobile = mEtMobile.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();

        if (StringUtils.isAllNotEmpty(name, mobile, code)) {
            try {
                name = URLEncoder.encode(name, Constants.ENCODING_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            /*CHYHttpClientUsage.getInstanse().doLoginByCode(Constants.LOGIN_TYPE_PATICIPATER, name, code, mobile,"",
                    AppApplication.getSystemLanuageCode(), Constants.getFromWhere(), Constants.conId,
                    new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
                        @Override
                        public void onStart() {
                            super.onStart();
                            showProgressBar("login...");
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            dismissProgressBar();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            LogUtils.println("loginInfo:===" + response);
                            try {
                                int state = response.getInt("state");
                                if (state == 1) {
                                    ParseUser.saveUserInfo(response.toString());
                                    *//*Gson gson = new Gson();
                                    UserInfoBean user = gson.fromJson(response.toString(), UserInfoBean.class);

                                    SharePreferenceUtils.saveUserString(Constants.USER_IS_LOGIN, "true");
                                    AppApplication.setSPStringValue(Constants.USER_NAME, user.getName());
                                    AppApplication.setSPStringValue(Constants.USER_IMG, user.getImg());
                                    AppApplication.setSPStringValue(Constants.USER_MOBILE, user.getMobilePhone());
                                    AppApplication.setSPIntegerValue(Constants.USER_ID, user.getUserId());
                                    AppApplication.setSPIntegerValue(Constants.USER_TYPE, user.getUserType());*//*

                                    *//*AppApplication.userId = user.getUserId();
                                    AppApplication.username = user.getName();
                                    AppApplication.userType = user.getUserType();*//*

                                    setResult(RESULT_OK);
                                    finish();
                                } else {
                                    String msg = response.getString("msg");
                                    if (!StringUtils.isEmpty(msg)) {
                                        showOnlyOneDialog(msg);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });*/
        } else {
            ToastUtils.showToast(R.string.login_empty_tips);
        }
    }

    @OnClick(R.id.ccode_info)
    void onCCodeInfoClick() {
        mLlCode.setVisibility(View.VISIBLE);
        mIvCodeTips.startAnimation(mInAnimation);
    }

    @OnClick(R.id.ll_ccode)
    void dismissCCode() {
        mIvCodeTips.startAnimation(mOutAnimation);
    }
}
