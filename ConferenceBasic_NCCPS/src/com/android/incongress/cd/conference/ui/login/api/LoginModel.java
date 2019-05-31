package com.android.incongress.cd.conference.ui.login.api;


import com.android.incongress.cd.conference.api.CHYHttpClient;
import com.android.incongress.cd.conference.http.RetrofitWrapper;
import com.android.incongress.cd.conference.ui.login.model.LoginBean;

import java.util.HashMap;

import io.reactivex.Observable;


public class LoginModel {

    private static LoginModel model;
    private LoginApi mApiService;

    private LoginModel() {
        mApiService = RetrofitWrapper
                .getInstance(CHYHttpClient.BASE_MVP_URL)
                .create(LoginApi.class);
    }

    public static LoginModel getInstance(){
        if(model == null) {
            model = new LoginModel();
        }
        return model;
    }

    public Observable<LoginBean> getTxNews(String key , int num , int page) {
        return mApiService.getWeChatNews(key, num, page);
    }


    public Observable<LoginBean> getWXHotSearch(String key , int num , int page , String word) {
        return mApiService.getWXHotSearch(key, num, page ,word);
    }
    //获取验证码
    public Observable<Object> getLoginSms(HashMap<String,Object> hashMap) {
        return mApiService.getLoginSms(hashMap);
    }
    //手机登陆
    public Observable<Object> getToLogin(HashMap<String,Object> hashMap) {
        return mApiService.getToLogin(hashMap);
    }
    //获取用户名和头像
    public Observable<Object> getUserNameAndImg(HashMap<String,Object> hashMap) {
        return mApiService.getUserNameAndImg(hashMap);
    }
    //微信登陆
    public Observable<Object> getWXToLogin(HashMap<String,Object> hashMap) {
        return mApiService.getWXToLogin(hashMap);
    }
    //修改用户名和头像
    public Observable<Object> getModifyHeadAndImg(HashMap<String,Object> hashMap) {
        return mApiService.getModifyHeadAndImg(hashMap);
    }
    //邮箱登陆
    public Observable<Object> getToEmailLogin(HashMap<String,Object> hashMap) {
        return mApiService.getToEmailLogin(hashMap);
    }

}
