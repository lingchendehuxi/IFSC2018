package com.android.incongress.cd.conference;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

public class MakeQuestionActivity extends BaseActivity {
    private TextView mTitle,mQuestion;
    private ImageView ivback;
    private EditText mEtQuestion;
    private EditText mEtEmail;
    private TextView mTvMeetingName;

    private String mPosterTitle;
    private int mPosterId;
    private String mAuthorName;
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_make_question);
    }

    @Override
    protected void initViewsAction() {
        mPosterTitle=getIntent().getStringExtra("title");
        mPosterId=getIntent().getIntExtra("id",0);
        mAuthorName=getIntent().getStringExtra("name");
        mEtQuestion = (EditText)findViewById(R.id.et_question);
        mTvMeetingName = (TextView)findViewById(R.id.tv_topic);
        mTitle = (TextView)findViewById(R.id.title_text);
        mQuestion = (TextView)findViewById(R.id.title_question);
        mEtEmail = (EditText)findViewById(R.id.et_email);
        ivback = (ImageView) findViewById(R.id.title_back);

        mTitle.setText(getString(R.string.ask_sb, mAuthorName));
        mTvMeetingName.setText("#" + mPosterTitle + "#");
        if (TextUtils.isEmpty(mPosterTitle)) {
            mTvMeetingName.setVisibility(View.GONE);
        }
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    askPoster();
            }
        });
    }
    private void askPoster() {
        String content = mEtQuestion.getText().toString().trim();
        String posterTitle = "";
        String email = mEtEmail.getText().toString().trim();
        String authorName = "";

        try {
            content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
            posterTitle = URLEncoder.encode(mPosterTitle, Constants.ENCODING_UTF8);
            authorName = URLEncoder.encode(mAuthorName, Constants.ENCODING_UTF8);

            if (!StringUtils.isEmpty(content)) {

                if(StringUtils.isEmpty(email)) {
                    if(AppApplication.getSystemLanuageCode()=="cn"){
                        ToastUtils.showShorToast("请输入您的email地址");
                    }else{
                        ToastUtils.showShorToast("Please enter your email");
                    }
                }else{
                    CHYHttpClientUsage.getInstanse().doCreatePosterQuestion(Constants.getConId(), AppApplication.userId, AppApplication.userType, posterTitle, content,
                            mPosterId, authorName, email, new JsonHttpResponseHandler("gbk") {
                                @Override
                                public void onStart() {
                                    super.onStart();
                                    showProgressBar("正在提问");
                                }

                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                    dismissProgressBar();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    ToastUtils.showShorToast("服务器开小差了，请稍后重试");
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        int state = response.getInt("state");
                                        if (state == 1) {
                                            if(AppApplication.getSystemLanuageCode()=="cn"){
                                                ToastUtils.showShorToast("提问成功");
                                            }else{
                                                ToastUtils.showShorToast("Question success");
                                            }
                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(mEtQuestion.getWindowToken(), 0);
                                            finish();
                                        } else {
                                            ToastUtils.showShorToast("提问失败，请稍后重试\n"+response.getString("msg"));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            } else {
                if(AppApplication.getSystemLanuageCode()=="cn"){
                    ToastUtils.showShorToast("提问不许为空");
                }else{
                    ToastUtils.showShorToast("Question are not empty");
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(Constants.FRAGMENT_MAKEQUESTION);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(Constants.FRAGMENT_MAKEQUESTION);
    }
}
