package com.android.incongress.cd.conference.fragments.question;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.umeng.analytics.MobclickAgent;

import cz.msebera.android.httpclient.Header;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Jacky on 2016/2/2.
 * 向某专家或提问提问
 */
public class MakeQuestionFragment extends BaseFragment {

    //Meeting相关的问题
    public static final String ASK_SPEAKER_NAME = "speakerName";
    public static final String ASK_SPEAKER_ID = "speakerId";
    public static final String ASK_TOPIC = "topic";
    public static final String ASK_MEETING_ID = "meetingId";

    private int mSpeakerId;
    private String mSpeakerName;
    private int mMeetingId;
    private String mMeetingName;

    //Poster相关的问题
    public static final String ASK_POSTER_TITLE = "posterTitle";
    public static final String ASK_POSTER_POSTER_ID = "posterId";
    public static final String ASK_POSTER_AUTHOR_NAME = "authorName";

    private String mPosterTitle;
    private int mPosterId;
    private String mAuthorName;

    private EditText mEtQuestion;
    private EditText mEtEmail;
    private TextView mTvMeetingName;

    private static final int QUESTION_MEETING = 1;
    private static final int QUESTION_POSTER = 2;
    private int mCurrentQuestionType = QUESTION_MEETING;

    @Override
    public void onStart() {
        super.onStart();
    }


    public void setMeetingQuestionInfo(String speakerName, int speakerId, int meetingId, String meetingName) {
            mCurrentQuestionType = QUESTION_MEETING;
            mSpeakerName = speakerName;
            mSpeakerId = speakerId;
            mMeetingId = meetingId;
            mMeetingName = meetingName;
    }

    public void setPosterQuestionInfo(String posterTitle, int posterId, String authorName) {
        mCurrentQuestionType = QUESTION_POSTER;
        mPosterId = posterId;
        mPosterTitle = posterTitle;
        mAuthorName = authorName;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_question, null);

        mEtQuestion = (EditText) view.findViewById(R.id.et_question);
        mTvMeetingName = (TextView) view.findViewById(R.id.tv_topic);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);

        if (mCurrentQuestionType == QUESTION_MEETING) {
            mTvMeetingName.setText("#" + mMeetingName + "#");

            if (TextUtils.isEmpty(mMeetingName)) {
                mTvMeetingName.setVisibility(View.GONE);
            }
            mEtEmail.setVisibility(View.GONE);
        } else {
            mTvMeetingName.setText("#" + mPosterTitle + "#");

            if (TextUtils.isEmpty(mPosterTitle))
                mTvMeetingName.setVisibility(View.GONE);
        }


        return view;
    }

    public void setRightListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentQuestionType == QUESTION_MEETING)
                    askMeeting();
                else
                    askPoster();
            }
        });
    }

    private void askMeeting() {
        String content = mEtQuestion.getText().toString();
        String meetingName = "";

        try {
            content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
            meetingName = URLEncoder.encode(mMeetingName, Constants.ENCODING_UTF8);

            if (!StringUtils.isEmpty(content)) {
                CHYHttpClientUsage.getInstanse().doCreateSceneShowQuestionNew(AppApplication.conId + "", AppApplication.userId + "", AppApplication.userType + "", content, mSpeakerId + "",
                        mMeetingId, meetingName, new JsonHttpResponseHandler() {
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
                                MyLogger.jLog().i(response.toString());
                                try {
                                    int state = response.getInt("state");
                                    if (state == 1) {
                                        ToastUtils.showShorToast("提问成功");
                                        InputMethodManager imm = (InputMethodManager)
                                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mEtQuestion.getWindowToken(), 0);
                                        performback();
                                    } else {
                                        ToastUtils.showShorToast("提问失败，请稍后重试");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtils.showShorToast("提问不许为空");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


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
                    ToastUtils.showShorToast("请输入您的email地址");
                }else{
                    CHYHttpClientUsage.getInstanse().doCreatePosterQuestion(AppApplication.conId, AppApplication.userId, AppApplication.userType, posterTitle, content,
                            mPosterId, authorName, email, new JsonHttpResponseHandler() {
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
                                            ToastUtils.showShorToast("提问成功");
                                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(mEtQuestion.getWindowToken(), 0);
                                            performback();
                                        } else {
                                            ToastUtils.showShorToast("提问失败，请稍后重试");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            } else {
                ToastUtils.showShorToast("提问不许为空");
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
