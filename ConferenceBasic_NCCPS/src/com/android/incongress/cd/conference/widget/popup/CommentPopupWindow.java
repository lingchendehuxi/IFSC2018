package com.android.incongress.cd.conference.widget.popup;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Jacky on 2016/4/2.
 */
public class CommentPopupWindow extends BasePopupWindow implements View.OnClickListener {
    private TextView mTvSendUserName, mTvCancel, mTvSend;
    private EditText mEtComment;
    private View popupView;

    //发表评论需要的一些参数
    private String mReceiveUserId = "-1";//@人的id,默认是-1
    private String sceneShowId = "";//@人的名字
    private String parentId = ""; //资源标示
    private String mContent = "";//评论内容
    private JsonHttpResponseHandler mJsonHttp;

    public CommentPopupWindow(final Activity context, String sendUserName, String name, String sceneShowId, String parentId, JsonHttpResponseHandler mJsonHttp) {
        super(context);

        mTvSendUserName = mPopupView.findViewById(R.id.tv_name);
        mTvCancel = mPopupView.findViewById(R.id.tv_cancle);
        mTvSend = mPopupView.findViewById(R.id.tv_send);
        mEtComment = mPopupView.findViewById(R.id.et_comment);

        this.sceneShowId = sceneShowId;
        this.parentId = parentId;
        this.mJsonHttp = mJsonHttp;

        mTvSendUserName.setText(sendUserName);
        if (!name.equals("")) {
            mEtComment.setHint("@" + name);
        }

        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mTvSend.setTextColor(context.getResources().getColor(R.color.theme_color));
                }
            }
        });
        mTvCancel.setOnClickListener(this);
        mTvSend.setOnClickListener(this);

        setAutoShowInputMethod(true);
    }

    @Override
    public View getInputView() {
        return mEtComment;
    }

    @Override
    protected Animation getShowAnimation() {
        return getDefaultAlphaInAnimation();
    }

    @Override
    public Animation getExitAnimation() {
        return getDefaultAlphaOutAnimation();
    }

    @Override
    public View getPopupViewById(int resId) {
        popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_comment, null);
        return popupView;
    }

    @Override
    protected View getClickToDismissView() {
        return popupView.findViewById(R.id.rl_bg);
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_comment);
    }

    @Override
    public View getAnimaView() {
        return popupView.findViewById(R.id.ll_popup_container);
    }

    private boolean first = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.tv_send:
                if (first) {
                    first = false;
                    String content = mEtComment.getText().toString().trim();
                    try {
                        content = URLEncoder.encode(content, Constants.ENCODING_UTF8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (StringUtils.isEmpty(content)) {
                        ToastUtils.showToast(R.string.comment_no_empty);
                    } else {
                        CHYHttpClientUsage.getInstanse().doSceneShowComment(sceneShowId, AppApplication.userId + "", AppApplication.userType + "", content, parentId, mJsonHttp);
                        dismiss();
                    }
                }
                break;
        }
    }
}
