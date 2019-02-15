package com.android.incongress.cd.conference.api;

import com.android.incongress.cd.conference.base.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;

/**
 * Created by Jacky on 2015/12/19.
 */
public class CHYHttpClient {
    //----------------------  接口地址1  测试-------------------------
    //测试服务器
    private static final String BASE_URL = "http://incongress.cn:8090/Conferences/chyApiHttps.do"; //新接口地址都在这里面，因为Https的关系 测试服
    //新添加的服务器地址 包括壁报、
    private static final String BASE_NEW_URL = "http://incongress.cn:8090/chyNewApi.do?";//"http://incongress.cn:8090/chyNewApi.do?"
    //测试服务器
    public static final String BASE_URL2 = "http://incongress.cn:8090/Conferences/conferencesHttps.do?";
    //测试服务器
    public static final String BASE_URL_COLEGE = "http://incongress.cn:8090";
    //新添加本地服务器
    private static final String BASE_LOCAL= "http://192.168.0.157/consWebapp.do?";//"http://incongress.cn:8090/chyNewApi.do?"
    //----------------------  接口地址2   正式-------------------------
    /*//正式服务器
    private static final String BASE_URL = "http://app.incongress.cn/Conferences/chyApiHttps.do?"; //新接口地址都在这里面，因为Https的关系 测试服
    //正式 包括壁报、
    private static final String BASE_NEW_URL = "http://app.incongress.cn/chyNewApi.do?";//"http://incongress.cn:8090/chyNewApi.do?"
    //正式服务器
    public static final String BASE_URL2 = "http://app.incongress.cn/Conferences/conferencesHttps.do?";
    //正式服务器
    public static final String BASE_URL_COLEGE = "http://app.incongress.cn";*/

    private static final int TIME_OUT = 0;

    private static AsyncHttpClient client = getHttpClient();

    private static AsyncHttpClient getHttpClient() {
        client = new AsyncHttpClient();
        client.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
        return client;
    }

    public static void get(String methodName, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.get(getAbsoluteUrl(methodName), params, responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL, params, responseHandler);
    }
    public static void postNew(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_NEW_URL, params, responseHandler);
    }
    public static void postLOCAL(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_LOCAL, params, responseHandler);
    }
    public static void postMyQuestion(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL, params, responseHandler);
    }
    //火速title上线方法名
    public static void postExamTable(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL_COLEGE+Constants.BASE_EXAM_TABLE, params, responseHandler);
    }
    //火速上线方法
    public static void postLocal_Other(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL_COLEGE+Constants.BASE_LOCAL_LIST, params, responseHandler);
    }

    private static String getAbsoluteUrl(String methodName) {
        return BASE_URL + "?method=" + methodName;
    }

    public static void post2(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL2, params, responseHandler);
    }
    public static void postOther(RequestParams params, String method,JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL2+method+"&", params, responseHandler);
    }
}
