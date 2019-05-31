package com.android.incongress.cd.conference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.incongress.cd.conference.api.CHYHttpClientUsage;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.ui.login.view.LoginActivity;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.LanguageUtil;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.PicUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.android.incongress.cd.conference.widget.CircleImageView;
import com.android.incongress.cd.conference.widget.ClearEditText;
import com.android.incongress.cd.conference.widget.IconChoosePopupWindow;
import com.android.incongress.cd.conference.widget.photo.galleryfinal.FunctionConfig;
import com.android.incongress.cd.conference.widget.photo.galleryfinal.GalleryFinal;
import com.android.incongress.cd.conference.widget.photo.galleryfinal.model.PhotoInfo;
import com.android.incongress.cd.conference.widget.popup.InputMethodUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2016/1/29.
 * 设置基本信息页面
 */
public class LoginForUpdateInfoActivity extends BaseActivity implements GalleryFinal.OnHanlderResultCallback {

    private TextView tv_title, title_tip;
    private ClearEditText et_userName;
    private ProgressDialog mProgressDialog;
    private String mobile, sms, nickName;
    private Button bt_save;
    private ImageView iv_back;
    private boolean isUpdate = false;
    /**
     * 页面是否处于打开状态
     **/
    private boolean mIsOpen = true;

    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    //头像上传相关
    private static final int UPLOAD_IMGURL_SUCCESS = 3;
    public static final String EXTRA_FROM_ME = "fromMe";
    /**
     * 头像上传后的地址
     **/
    private String mUploadFilePath = "", userIcId, userType, userId;
    private IconChoosePopupWindow mIconChoosePopupWindow;
    private CircleImageView iv_head;

    //登录成功标识符
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login_for_update);
        tv_title = findViewById(R.id.tv_title);
        et_userName = findViewById(R.id.et_user);
        bt_save = findViewById(R.id.bt_save);
        iv_head = findViewById(R.id.iv_my_head);
        iv_back = findViewById(R.id.iv_back);
        title_tip = findViewById(R.id.title_tip);
    }

    @Override
    protected void initViewsAction() {
        mobile = getIntent().getStringExtra(Constants.USER_MOBILE);
        userIcId = getIntent().getStringExtra(Constants.USER_IC_ID);
        userType = getIntent().getStringExtra(Constants.USER_TYPE);
        isUpdate = getIntent().getBooleanExtra("update", false);
        mUploadFilePath = SharePreferenceUtils.getUser(Constants.USER_IMG);
        userId = SharePreferenceUtils.getUser(Constants.USER_ID);
        if (isUpdate) {
            tv_title.setText(R.string.incongress_modify_person_info);
            title_tip.setVisibility(View.INVISIBLE);
        } else {
            tv_title.setText(R.string.incongress_fix_info);
            title_tip.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(SharePreferenceUtils.getUser(Constants.USER_IMG))) {
            PicUtils.loadCircleImage(this, SharePreferenceUtils.getUser(Constants.USER_IMG), iv_head);
        } else {
            iv_head.setImageResource(R.drawable.professor_default);
        }
        if (!TextUtils.isEmpty(SharePreferenceUtils.getUser(Constants.USER_NAME))) {
            et_userName.setText(SharePreferenceUtils.getUser(Constants.USER_NAME));
        } else {
            et_userName.setText("");
        }
        sms = getIntent().getStringExtra("sms");
        bt_save.getBackground().setAlpha(204);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodUtils.hideSoftInput(LoginForUpdateInfoActivity.this, et_userName);
                doModifyPersonInfo();
                hideShurufa();
            }
        });
        et_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    bt_save.getBackground().setAlpha(204);
                } else {
                    bt_save.getBackground().setAlpha(255);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow();
                mIconChoosePopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                lightOff();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //对用户名和头像进行修改
    private void doModifyPersonInfo() {
        if (TextUtils.isEmpty(mUploadFilePath)) {
            ToastUtils.showToast("请先上传头像");
            return;
        }
        nickName = et_userName.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showToast("请输入用户名");
            return;
        } else if (StringUtils.stringIsLegal(nickName)) {
            ToastUtils.showRoundRectToast(this, getString(R.string.username_rule), R.layout.view_toast_custom);
            return;
        }
        if (TextUtils.isEmpty(userIcId)) {
            userIcId = SharePreferenceUtils.getUser(Constants.USER_IC_ID);
        }
        CHYHttpClientUsage.getInstanse().doModifyUserInfo(userIcId, nickName, mUploadFilePath, new JsonHttpResponseHandler(Constants.ENCODING_GBK) {
            public void onStart() {
                super.onStart();
                mProgressDialog = ProgressDialog.show(LoginForUpdateInfoActivity.this, null, "loading...");
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
                if (JSONCatch.parseInt("state", response) == 0) {
                    ToastUtils.showToast(JSONCatch.parseString("msg", response));
                } else {
                    ParseUser.saveUserInfo(response.toString());
                    SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN, true);
                    Intent loginIntent = new Intent();
                    loginIntent.setAction(LoginActivity.LOGIN_ACTION);
                    sendBroadcast(loginIntent);
                    setResult(RESULT_OK);
                    startActivity(new Intent(LoginForUpdateInfoActivity.this, HomeActivity.class));
                    EventBus.getDefault().post(new HomeActivity.UpdateConferenceEvent(true));
                    finish();
                }
            }
        });
    }

    private void initPopupWindow() {
        mIconChoosePopupWindow = new IconChoosePopupWindow(this);
        mIconChoosePopupWindow.setAnimationStyle(R.style.icon_popup_window);
        mIconChoosePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionConfig config = new FunctionConfig.Builder().setMutiSelectMaxSize(1).build();
                GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, LoginForUpdateInfoActivity.this);
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, LoginForUpdateInfoActivity.this);
            }
        });

        mIconChoosePopupWindow.getContentView().findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIconChoosePopupWindow.dismiss();
            }
        });
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if (mIconChoosePopupWindow != null && mIconChoosePopupWindow.isShowing())
            mIconChoosePopupWindow.dismiss();

        String photoPath = "";
        if (reqeustCode == REQUEST_CODE_GALLERY) {
            photoPath = resultList.get(0).getPhotoPath();
        } else if (reqeustCode == REQUEST_CODE_CAMERA) {
            photoPath = resultList.get(0).getPhotoPath();
        }

        //图片进行压缩
        try {
            mUploadFilePath = PicUtils.saveFile(PicUtils.getSmallBitmap(photoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //上传
        if (!TextUtils.isEmpty(userId)) {
            doUploadFile(SharePreferenceUtils.getUser(Constants.USER_IC_ID) + "", new File(mUploadFilePath));
        } else {
            doUploadFile(userIcId + "", new File(mUploadFilePath));
        }
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        ToastUtils.showToast(getString(R.string.choose_photo_fail));
    }

    private void doUploadFile(String userId, File uploadFile) {

        try {
            CHYHttpClientUsage.getInstanse().doCreateUserImg(userId, uploadFile, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    mProgressDialog = ProgressDialog.show(LoginForUpdateInfoActivity.this, null, "loading...");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (JSONCatch.parseInt("state", response) == 1) {
                        mUploadFilePath = JSONCatch.parseString("imgUrl",response);
                        mHandler.sendEmptyMessage(UPLOAD_IMGURL_SUCCESS);
                        if(AppApplication.isUserLogIn()){
                            Intent loginIntent = new Intent();
                            loginIntent.setAction(LoginActivity.LOGIN_ACTION);
                            sendBroadcast(loginIntent);
                        }
                    } else {
                        mUploadFilePath = "";
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (mIsOpen == false) {
                return false;
            }

            int target = message.what;
            if (target == UPLOAD_IMGURL_SUCCESS) {
                SharePreferenceUtils.saveUserString(Constants.USER_IMG, mUploadFilePath);

                if (mUploadFilePath.contains("https:"))
                    mUploadFilePath = mUploadFilePath.replaceFirst("s", "");
                PicUtils.loadCircleImage(LoginForUpdateInfoActivity.this, mUploadFilePath, iv_head);
            }
            return false;
        }
    });

    /**
     * 内容区域变量
     */
    protected void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    protected void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

}
