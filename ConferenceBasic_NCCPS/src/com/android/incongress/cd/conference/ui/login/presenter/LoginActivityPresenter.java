package com.android.incongress.cd.conference.ui.login.presenter;

import android.app.ProgressDialog;
import android.text.TextUtils;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.http.ExceptionUtils;
import com.android.incongress.cd.conference.save.ParseUser;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.ui.login.api.LoginModel;
import com.android.incongress.cd.conference.ui.login.contract.LoginContract;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.google.gson.Gson;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 13008 on 2019/4/29.
 */

public class LoginActivityPresenter implements LoginContract.Presenter{
    private LoginContract.View mView;
    private Gson gson;

    public LoginActivityPresenter(LoginContract.View homeView) {
        this.mView = homeView;
        gson = new Gson();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void doGetSms(String mobile, final ProgressDialog progressDialog) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("getSmsMobile", "");
        params.put("mobile", mobile);
        params.put("type", Constants.ConfirmTypeLogin);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());
        params.put("lan", AppApplication.getSystemLanuageCode());
        LoginModel model = LoginModel.getInstance();
        model.getLoginSms(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object object) {
                        JSONObject jsonObject =JSONCatch.parseJsonobject(gson.toJson(object));
                        String msg = JSONCatch.parseString("msg", jsonObject);
                        if (JSONCatch.parseInt("state", jsonObject) == 0) {
                            mView.showDialog(msg, null);
                        } else {
                            ToastUtils.showToast(AppApplication.getContext().getString(R.string.success_send_regist_code));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.cancel();
                    }
                });
    }

    @Override
    public void doLogin___(String mobile,String sms ,final ProgressDialog progressDialog) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("method", "login");
        params.put("mobile", mobile);
        params.put("lan", AppApplication.getSystemLanuageCode());
        params.put("fromWhere", Constants.getFromWhere());
        params.put("sms", sms);
        params.put("conId", Constants.getConId());
        LoginModel model = LoginModel.getInstance();
        model.getToLogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object object) {
                        JSONObject response =JSONCatch.parseJsonobject(gson.toJson(object));
                        if (JSONCatch.parseInt("state", response) == 1) {
                            ParseUser.saveUserInfo(response.toString());
                            getUserHeadAndNickName(JSONCatch.parseInt("icUserId", response)+"");
                        } else {
                            ToastUtils.showToast(JSONCatch.parseString("msg",response));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.cancel();
                    }
                });
    }

    @Override
    public void doLoginbyEmail(String email,String psw ,final ProgressDialog progressDialog) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("method", "loginByEmail");
        params.put("email", email);
        params.put("password", psw);
        params.put("lan", AppApplication.getSystemLanuageCode());
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());
        LoginModel model = LoginModel.getInstance();
        model.getToEmailLogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object object) {
                        JSONObject response =JSONCatch.parseJsonobject(gson.toJson(object));
                        mView.handleEmailLogin(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.cancel();
                    }
                });
    }

    @Override
    public void doModifyPersonInfo(String icUserId,String imgUrl,String nickName) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("method", "updateIcUserWx");
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());
        params.put("imgUrl", imgUrl);
        params.put("icUserId", icUserId);
        if (!TextUtils.isEmpty(nickName)) {
            params.put("nickName", StringUtils.utf8Encode(nickName));
        }
        LoginModel model = LoginModel.getInstance();
        model.getModifyHeadAndImg(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object object) {
                        JSONObject response =JSONCatch.parseJsonobject(gson.toJson(object));
                        if (JSONCatch.parseInt("state", response) == 1) {
                            ParseUser.saveUserInfo(response.toString());
                        } else {
                            ToastUtils.showToast(JSONCatch.parseString("msg", response));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void getUserHeadAndNickName(String icUserId) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("method", "getIcUserById");
        params.put("icUserId", icUserId);
        params.put("lan", AppApplication.getSystemLanuageCode());
        LoginModel model = LoginModel.getInstance();
        model.getUserNameAndImg(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object object) {
                        JSONObject response =JSONCatch.parseJsonobject(gson.toJson(object));
                        mView.handleUserNameAndImg(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void loginWX(String openId,final ProgressDialog progressDialog) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("method", "login");
        params.put("mobile", SharePreferenceUtils.getUser(Constants.USER_MOBILE));
        params.put("lan", AppApplication.getSystemLanuageCode());
        params.put("sms", "");
        params.put("compassId", Constants.COMPASS_ID);
        params.put("openId", openId);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());
        LoginModel model = LoginModel.getInstance();
        model.getWXToLogin(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object object) {
                        JSONObject response =JSONCatch.parseJsonobject(gson.toJson(object));
                        mView.handleWXLogin(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.cancel();
                    }
                });
    }
}
