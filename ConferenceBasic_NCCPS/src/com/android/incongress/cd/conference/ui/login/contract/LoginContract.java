package com.android.incongress.cd.conference.ui.login.contract;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.android.incongress.cd.conference.mvp.BasePresenter;
import com.android.incongress.cd.conference.mvp.BaseView;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by 13008 on 2019/4/29.
 */

public interface LoginContract {
    interface View extends BaseView {
        void showDialog(String msg, DialogInterface.OnClickListener okListener);
        void handleUserNameAndImg(JSONObject jsonObject);
        void handleWXLogin(JSONObject jsonObject);
        void handleEmailLogin(JSONObject jsonObject);
    }


    //Presenter控制器
    interface Presenter extends BasePresenter {
        void doGetSms(String mobile, ProgressDialog progressDialog);
        void doLogin___(String mobile, String sms,ProgressDialog progressDialog);
        void doLoginbyEmail(String email,String psw , ProgressDialog progressDialog);
        void doModifyPersonInfo(String icUserId,String imgUrl,String nickName);
        void getUserHeadAndNickName(String icUserId);
        void loginWX(String openId, ProgressDialog progressDialog);
    }
}
