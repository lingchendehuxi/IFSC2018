package com.android.incongress.cd.conference.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

/**
 * Created by Jacky on 2015/12/19.
 */
public class CHYHttpClient {
    //----------------------  接口地址1  正式+测试-------------------------
//    public static final String BASE_URL = "http://app.incongress.cn/Conferences/chyApi.do";
//    private static final String BASE_URL = "http://incongress.cn/chyApi.do";
//    private static final String BASE_URL = "http://incongress.cn/Conferences/chyApi.do";
//private static final String BASE_URL = "http://incongress.cn/Conferences/chyApiHttps.do"; //新街口地址都在这里面，因为Https的关系 测试服
    private static final String BASE_URL = "http://app.incongress.cn/Conferences/chyApiHttps.do"; //正式服

    //----------------------  接口地址2   正式+测试 hession接口所在地，有点乱-------------------------
//    private static final String BASE_URL2 = "http://incongress.cn/conferences.do";
//    private static final String BASE_URL2 = "http://incongress.cn/Conferences/conferences.do";
//  private static final String BASE_URL2= "http://incongress.cn/Conferences/conferences.do";//部分接口在另外一个地址中
    public static final String BASE_URL2 = "http://app.incongress.cn/Conferences/conferencesHttps.do";


    //本地调试地址
//    public static final String BASE_LOCAL = "http://192.168.0.123/chyApi.do";

    private static final int TIME_OUT =  0;

    private static AsyncHttpClient client = getHttpClient();

    private static AsyncHttpClient getHttpClient() {
        client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        return client;
    }

    public static void get(String methodName, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.get(getAbsoluteUrl(methodName),params,responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL, params, responseHandler);
    }

//    public static void postLocal(RequestParams params, JsonHttpResponseHandler responseHandler) {
//        client.setTimeout(TIME_OUT);
//        client.post(BASE_LOCAL, params, responseHandler);
//    }

    private static String getAbsoluteUrl(String methodName) {
        return BASE_URL + "?method=" + methodName;
    }

    public static void post2(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL2, params, responseHandler);
    }
}
