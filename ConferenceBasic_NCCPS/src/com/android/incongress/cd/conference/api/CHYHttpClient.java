package com.android.incongress.cd.conference.api;

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
//    public static final String BASE_URL = "http://incongress.cn:8090"; //新接口地址都在这里面，因为Https的关系 测试服
    //新添加本地服务器
    private static final String BASE_LOCAL= "http://incongress.cn:8090/chyNewApi.do?";//"http://incongress.cn:8090/chyNewApi.do?"
    private static final String BASE_LOCAL2= "http://incongress.cn:8090/chyNewApi.do?";//"http://incongress.cn:8090/chyNewApi.do?"
    //----------------------  接口地址2   正式-------------------------
    //正式服务器
    public static final String BASE_URL = "http://app.incongress.cn"; //http://app.incongress.cn
    public static final String BASE_MVP_URL = "http://app.incongress.cn/"; //http://app.incongress.cn
    //
    public static final String BASE_MVP_INFO_LOGIN = "Conferences/conferencesHttps.do";
    //compass接口
    public static final String BASE_MVP_COMPASS_URL = "compassApiV2.do";
    /**
     * 接口集合
     */
    //学院接口
    private static final String BASE_EXAM_TABLE = "/Exam/data?";
    //学院火速上线列表接口
    private static final String BASE_FAST_ONLINE = "/sessiongroup.do?";
    //二维码扫描、直播列表
    private static final String BASE_LIVE_LIST = "/consWebapp.do?";
    //壁报
    public static final String BASE_POSTER_LIST = "/chyNewApi.do?";
    //邮箱 登陆 信息修改
    private static final String BASE_INFO_LOGIN = "/Conferences/conferencesHttps.do?";
    //大部分老接口
    private static final String BASE_COMMON_URL = "/Conferences/chyApiHttps.do?";
    //资源接口
    private static final String BASE_RESOURCE_URL = "/conpassApi.do?";
    //compass接口
    private static final String BASE_COMPASS_URL = "/compassApiV2.do?";


    //此超时不生效，使用的是默认的10*1000
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
    //大部分老接口
    public static void post(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_COMMON_URL, params, responseHandler);
    }
    //壁报
    public static void postNew(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_POSTER_LIST, params, responseHandler);
    }
    //壁报
    public static void postResource(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_RESOURCE_URL, params, responseHandler);
    }
    //仅仅为了测试本地
    public static void postLOCAL(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_LOCAL, params, responseHandler);
    }
    //仅仅为了测试本地2
    public static void postLOCAL2(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_LOCAL2, params, responseHandler);
    }
    public static void postMyQuestion(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL, params, responseHandler);
    }
    //火速title上线方法名
    public static void postExamTable(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_EXAM_TABLE, params, responseHandler);
    }
    //火速上线方法
    public static void postFastOnLine(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_FAST_ONLINE, params, responseHandler);
    }
    //直播列表 二维码
    public static void postLiveList(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_LIVE_LIST, params, responseHandler);
    }
    //compass接口 首页
    public static void postCompass(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_COMPASS_URL, params, responseHandler);
    }

    private static String getAbsoluteUrl(String methodName) {
        return BASE_URL+BASE_COMMON_URL + "method=" + methodName;
    }
    //邮箱 登陆等
    public static void post2(RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_INFO_LOGIN, params, responseHandler);
    }
    public static void postOther(RequestParams params, String method,JsonHttpResponseHandler responseHandler) {
        client.setTimeout(TIME_OUT);
        client.post(BASE_URL+BASE_INFO_LOGIN+method+"&", params, responseHandler);
    }
}
