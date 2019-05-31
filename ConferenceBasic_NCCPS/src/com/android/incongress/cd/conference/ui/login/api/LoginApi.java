package com.android.incongress.cd.conference.ui.login.api;

import com.android.incongress.cd.conference.api.CHYHttpClient;
import com.android.incongress.cd.conference.ui.login.model.LoginBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by PC on 2017/9/5.
 * 作者：PC
 */

public interface LoginApi {

    /**
     * http://api.tianapi.com/wxnew/?key=APIKEY&num=10
     */
    @GET("wxnew/")
    Observable<LoginBean> getWeChatNews(@Query("key") String key,
                                        @Query("num") int num,
                                        @Query("page") int page);


    /**
     * 微信精选列表
     */
    @GET("wxnew")
    Observable<LoginBean> getWXHotSearch(@Query("key") String key,
                                         @Query("num") int num,
                                         @Query("page") int page,
                                         @Query("word") String word);
    @FormUrlEncoded
    @POST(CHYHttpClient.BASE_MVP_INFO_LOGIN)
    Observable<Object> getLoginSms(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST(CHYHttpClient.BASE_MVP_COMPASS_URL)
    Observable<Object> getWXToLogin(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST(CHYHttpClient.BASE_MVP_COMPASS_URL)
    Observable<Object> getToLogin(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST(CHYHttpClient.BASE_MVP_COMPASS_URL)
    Observable<Object> getUserNameAndImg(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST(CHYHttpClient.BASE_MVP_COMPASS_URL)
    Observable<Object> getModifyHeadAndImg(@FieldMap Map<String, Object> map);
    @FormUrlEncoded
    @POST(CHYHttpClient.BASE_MVP_COMPASS_URL)
    Observable<Object> getToEmailLogin(@FieldMap Map<String, Object> map);


}
