package com.android.incongress.cd.conference.api;

import android.content.SharedPreferences;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.model.ConferenceDb;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.FileUtils;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.google.gson.stream.JsonToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jacky on 2015/12/19.
 */
public class CHYHttpClientUsage {
    private static CHYHttpClientUsage mInstance;

    private CHYHttpClientUsage() {
    }

    public static final CHYHttpClientUsage getInstanse() {
        if (mInstance == null) {
            synchronized (CHYHttpClientUsage.class) {
                if (mInstance == null) {
                    mInstance = new CHYHttpClientUsage();
                }
            }
        }
        return mInstance;
    }

    /**
     * 现场动态接口 下方列表数据
     */
    public void doGetSceneShowDown(String conferencesId, String lastSceneShowId, String userId, String userType, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getSceneShowDown");
        params.put("conferencesId", conferencesId);
        params.put("lastSceneShowId", lastSceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("lan", AppApplication.getSystemLanuageCode());

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 现场动态上方新闻和展商活动
     */
    public void doGetSceneShowTop(String conferencesId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getSceneShowTop");
        params.put("conferencesId", conferencesId);

        CHYHttpClient.post("getSceneShowTop", params, responseHandler);
    }

    /**
     * 发表图片
     */
    public void doCreateSceneShowImg(String conferencesId, String userId, String userType, String sceneShowId, File file, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createSceneShowImg");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("sceneShowId", sceneShowId);
        try {
            params.put("img", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 发表文字
     *
     * @param conferencesId
     * @param userId
     * @param sceneShowId
     * @param content
     * @param responseHandler
     */
    public void doCreateSceneShowTxt(String conferencesId, String userId, String userType, String sceneShowId, String content, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createSceneShowTxt");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("sceneShowId", sceneShowId);
        params.put("content", content);

        CHYHttpClient.post(params, responseHandler);
    }

    public void doGetAlertAd(int version, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("conId", Constants.getConId());
        params.put("version", version);
        CHYHttpClient.get("getAlertAd", params, responseHandler);
    }

    /**
     * 点赞
     *
     * @param sceneShowId
     * @param userId
     */
    public void doSceneShowLaud(String sceneShowId, String userId, String userType, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "sceneShowLaud");
        params.put("sceneShowId", sceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 获取专家问题
     *
     * @param conferencesId
     * @param speakerId
     */
    public void doGetSceneShowQuestions(String conferencesId, String speakerId, String lastQuestionId, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getSceneShowQuestions");
        params.put("conferencesId", conferencesId);
        params.put("speakerId", speakerId);
        params.put("lastQuestionId", lastQuestionId);
        params.put("lan", lan);
        CHYHttpClient.post(params, responseHandler);
    }


    /**
     * 现场动态接口 下方列表数据
     */
    public void doGetCoursewareStream(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getCoursewareStream");
        params.put("conId", Constants.getConId());

        CHYHttpClient.post(params, responseHandler);
    }

    public void doGetContentStream(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getContentStream");
        params.put("conId", Constants.getConId());

        CHYHttpClient.post(params, responseHandler);
    }

    public void doChyModuleTongji(String moduleName, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "chyModuleTongji");
        params.put("moduleName", moduleName);
        params.put("conId", Constants.getConId());
        params.put("fromWhere", Constants.getFromWhere());
        params.put("type", 1);
        params.put("phoneType", 2);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口getHdSession互动Session
     *
     * @param conferencesId
     * @param lan
     * @param responseHandler
     */
    public void doGetHdSession(String conferencesId, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getHdSessionV2");
        params.put("conferencesId", conferencesId);
        params.put("lan", lan);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 消息站
     *
     * @param conId
     * @param count
     * @param pageIndex
     * @param responseHandler
     */
    public void doGetTokenList(String conId, String count, String pageIndex, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getTokenList");
        params.put("conId", conId);
        params.put("count", count);
        params.put("pageIndex", pageIndex);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 消息站个人信息
     *
     * @param userId
     * @param userType
     * @param responseHandler
     */
    public void doGetUserMessage(String userId, String userType, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("userId", userId);
        params.put("userType", userType);
        params.put("conferencesId", Constants.getConId());


        CHYHttpClient.post("getUserMessageReminderByUserIdAndUserType", params, responseHandler);
    }

    /**
     * 用户注册
     *
     * @param name
     * @param email
     * @param fromWhere
     * @param lan
     * @param responseHandler
     */
    public void doEmailRegUser(String name, String familyName, String givenName, String email, String password, String fromWhere, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("regByEmail", "");
        params.put("name", name);
        params.put("familyName", familyName);
        params.put("giveName", givenName);
        params.put("email", email);
        params.put("password", password);
        params.put("fromWhere", fromWhere);
        params.put("lan", lan);
        params.put("conId", Constants.getConId());

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 用户注册
     *
     * @param name
     * @param mobile
     * @param fromWhere
     * @param lan
     * @param responseHandler
     */
    public void doMobileRegUser(String name, String familyName, String givenName, String mobile, String fromWhere, String lan, String sms, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("regUser", "");
        params.put("name", name);
        params.put("familyName", familyName);
        params.put("giveName", givenName);
        params.put("mobile", mobile);
        params.put("fromWhere", fromWhere);
        params.put("lan", lan);
        params.put("sms", sms);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 获取验证码 type:1注册 2登录
     *
     * @param mobile
     * @param type
     * @param lan
     */
    public void doGetSmsMobile(int conid, String mobile, String type, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("getSmsMobile", "");
        params.put("mobile", mobile);
        params.put("type", type);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", conid + "");
        params.put("lan", lan);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 登陆接口
     *
     * @param id              参会的注册号
     * @param name            utf8编码
     * @param mobile
     * @param sms
     * @param lan             cn,en
     * @param fromWhere
     * @param conId
     * @param responseHandler
     */
    public void doLoginV4(String id, String name, String familyName, String giveName, String mobile, String sms, String lan, String fromWhere, String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("loginV4", "");
        params.put("id", id);
        params.put("name", name);
        params.put("familyName", familyName);
        params.put("giveName", giveName);
        params.put("mobile", mobile);
        params.put("sms", sms);
        params.put("lan", lan);
        params.put("fromWhere", fromWhere);
        params.put("conId", conId);

        CHYHttpClient.post2(params, responseHandler);
    }

    public void doLoginWX(String openId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("loginV6", "");
        params.put("mobile", "");
        params.put("lan", AppApplication.getSystemLanuageCode());
        params.put("sms", "");
        params.put("openId", openId);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());

        CHYHttpClient.post2(params, responseHandler);
    }

    public void doEmailLoginV1(String email, String password, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("loginByEmailV1", "");
        params.put("email", email);
        params.put("password", password);
        params.put("lan", lan);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());


        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 通过c码登录接口 csco开始用的
     *
     * @param
     * @param
     * @param mobile
     * @param lan
     * @param fromWhere
     * @param responseHandler
     */
    public void doLoginByCode(String mobile, String sms, String lan, String fromWhere, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("loginV6", "");
        params.put("mobile", mobile);
        params.put("lan", lan);
        params.put("fromWhere", fromWhere);
        params.put("sms", sms);
        params.put("conId", Constants.getConId());
        /*params.put("loginByCode","");
        params.put("name", name);
        params.put("familyName", "");
        params.put("giveName", "");
        params.put("mobile", mobile);
        params.put("sms", sms);
        params.put("lan", lan);
        params.put("fromWhere", fromWhere);
        params.put("conId", conId +"");
//        params.put("conId", -1);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("code", ccode);
        params.put("type",type+"");*/

        CHYHttpClient.post2(params, responseHandler);
    }
    //第一次登录后，设置用户名和头像
    public void doUpdateUserInfo(String mobile, String sms, String openId, String nickName, String imgUrl, String sex, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("mobilePhone", mobile);
        params.put("lan", AppApplication.getSystemLanuageCode());
        params.put("fromWhere", Constants.getFromWhere());
        params.put("sms", sms);
        params.put("conId", Constants.getConId());
        params.put("openId", openId);
        params.put("imgUrl", imgUrl);
        if (!TextUtils.isEmpty(nickName)) {
            params.put("nickName", StringUtils.utf8Encode(nickName));
        }
        if (!TextUtils.isEmpty(sex)) {
            params.put("sex", StringUtils.utf8Encode(sex));
        }
        CHYHttpClient.postOther(params, "updateUserInfoWx", responseHandler);
    }
    //更改用户名和头像
    public void doModifyUserInfo(String nickName, String imgUrl, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", Constants.getConId());
        params.put("imgUrl", imgUrl);
        params.put("userId", SharePreferenceUtils.getUser(Constants.USER_ID));
        if (!TextUtils.isEmpty(nickName)) {
            params.put("nickName", StringUtils.utf8Encode(nickName));
        }
        CHYHttpClient.postOther(params, "updateUserInfoWxV2", responseHandler);
    }

    /**
     * WCES
     *
     * @param email
     * @param password
     * @param lan
     * @param responseHandler
     */
    public void doLoginByEmail(String email, String password, String lan, String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("loginByEmail", "");
        params.put("email", email);
        params.put("password", password);
        params.put("lan", lan);
        params.put("fromWhere", Constants.getFromWhere());
        params.put("conId", conId);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 接口createSceneShowQuestion提问
     * 向专家提问
     * 更新，提问针对meetingId
     *
     * @param conferencesId
     * @param userId
     * @param userType
     * @param content
     * @param speakerId
     * @param responseHandler
     */
    public void doCreateSceneShowQuestion(String conferencesId, String userId, String userType, String content, String speakerId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "createSceneShowQuestion");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("content", content);
        params.put("speakerId", speakerId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * @param content
     * @param speakerId
     * @param meetingId
     * @param meetingName
     * @param responseHandler clientType 2代表安卓   tokenType 1代表文本 2代表富文本
     */
    public void doCreateSceneShowQuestionNew(String content, String speakerId, int meetingId, String meetingName, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createSceneShowQuestionV1");
        params.put("conId", 367);
        params.put("userId", AppApplication.userId);
        params.put("userType", AppApplication.userType);
        params.put("content", content);
        params.put("speakerId", speakerId);
        params.put("meetingId", meetingId);
        params.put("meetingName", meetingName);
        params.put("clientType", 2);
        params.put("tokenType", 1);
        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口sceneShowComment评论
     *
     * @param sceneShowId
     * @param userId
     * @param userType
     * @param content
     * @param parentId
     * @param responseHandler
     */
    public void doSceneShowComment(String sceneShowId, String userId, String userType, String content, String parentId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "sceneShowComment");
        params.put("sceneShowId", sceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("content", content);
        params.put("parentId", parentId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 每次更新完毕数据包后的提示信息
     *
     * @param conId
     * @param responseHandler
     */
    public void doUpdateInfo(String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("getUplaodInfo", "");
        params.put("conId", conId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * createUserLooked记录查看时间（现场秀，消息站）
     *
     * @param userId
     * @param userType
     * @param userToken
     * @param type
     * @param responseHandler
     */
    public void doCreateUserLooked(String userId, String userType, String userToken, String type, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "createUserLooked");

        params.put("userId", userId);
        params.put("userType", userType);
        params.put("userToken", userToken);
        params.put("type", type);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口getLookCount获取最新数据数
     *
     * @param conferencesId
     * @param userId
     * @param userType
     * @param userToken
     * @param responseHandler
     */
    public void doGetLookCount(String conferencesId, String userId, String userType, String userToken, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getLookCount");
        params.put("conferencesId", conferencesId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("userToken", userToken);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口sceneShowAnswer讲者回复提问
     *
     * @param sceneShowId
     * @param speakerId
     * @param content
     * @param responseHandler
     */
    public void doSceneShowAnswer(String sceneShowId, String speakerId, String content, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "sceneShowAnswer");
        params.put("sceneShowId", sceneShowId);
        params.put("speakerId", speakerId);
        params.put("content", content);

        CHYHttpClient.post(params, responseHandler);
    }

    public void doCoursewareReservation(String topic, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "coursewareReservation");
        params.put("conId", Constants.getConId());
        params.put("topic", topic);
        params.put("userId", AppApplication.userId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 接口getSceneShowByUser我的发帖
     *
     * @param conferencesId
     * @param lastSceneShowId
     * @param userId
     * @param userType
     * @param responseHandler
     */
    public void doGetSceneShowByUser(String conferencesId, String lastSceneShowId, String userId, String userType, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getSceneShowByUser");
        params.put("conferencesId", conferencesId);
        params.put("lastSceneShowId", lastSceneShowId);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }


    /**
     * 发送推送的绑定ID
     *
     * @param conId
     * @param clientType
     * @param registerId
     * @param responseHandler
     */
    public void doSendToken(String conId, String clientType, String registerId, String userId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "sendJpushRegisterId");
        params.put("conId", conId);
        params.put("clientType", clientType);
        params.put("registerId", registerId);
        params.put("userId", userId);

        CHYHttpClient.postNew(params, responseHandler);
    }

    /**
     * 上传用户头像
     *
     * @param userId
     * @param userType
     * @param userImg
     * @param responseHandler
     * @throws FileNotFoundException
     */
    public void doCreateUserImg(String userId, String userType, File userImg, JsonHttpResponseHandler responseHandler) throws FileNotFoundException {
        RequestParams params = new RequestParams();
        params.put("method", "createUserImg");
        params.put("userImg", userImg);
        params.put("userId", userId);
        params.put("userType", userType);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 获取是否正在审核
     *
     * @param conId
     * @param responseHandler
     */
    public void doQueryShenHe(String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "isShenHeAnd");
        params.put("conId", conId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 找回C码
     *
     * @param name
     * @param mobile
     * @param lan
     * @param fromWhere
     * @param conId
     * @param responseHandler
     */
    public void doFindbackCCode(String name, String mobile, String lan, String fromWhere, String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("findCode", "");
        params.put("name", name);
        params.put("mobile", mobile);
        params.put("lan", lan);
        params.put("fromWhere", fromWhere);
        params.put("conId", conId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 程序初始化调用的接口
     */
    public void doGetInitData(int cId, int dataVersion, int clientType, String appVersion, String token,int totalConId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "getInitData");
        params.put("cId", cId + "");
        params.put("dataVersion", dataVersion + "");
        params.put("clientType", clientType + "");
        params.put("appVersion", appVersion + "");
        params.put("token", token);
        params.put("totalConId", 1);

        CHYHttpClient.post(params, responseHandler);

    }

    /**
     * 反馈接口
     *
     * @param cId
     * @param phone
     * @param email
     * @param content
     */
    public void doSendFeedbackV2(int cId, String phone, String email, String content, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.put("method", "sendFeedbackV2");
        params.put("cId", cId + "");
        params.put("phone", phone);
        params.put("email", email);
        params.put("content", content);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 获取电子壁报的数据类型
     */
    public void doGetWallPosterType(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getField");
        params.put("conId", Constants.getConId());

        CHYHttpClient.postNew(params, responseHandler);
    }

    /**
     * 获取电子壁报数据
     */
    public void doGetWallPoster(int pageIndex, String searchString, int orderBy, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getPosterByField");
        params.put("conId", Constants.getConId());
        params.put("page", pageIndex);
        params.put("search", StringUtils.utf8Encode(searchString));
        params.put("count", Constants.PAGE_SIZE);
        if(!TextUtils.isEmpty(searchString)){
            orderBy = -1;
        }
        params.put("fieldId", orderBy + "");

        CHYHttpClient.postNew(params, responseHandler);
    }

    /**
     * 给电子壁报点赞
     * <p>
     * map.put("method", "zanPoster");
     * map.put("posterId", posterId);
     */
    public void doZanPoster(String posterId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "zanPoster");
        params.put("posterId", posterId);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 根据壁报id获取壁报讨论列表
     * map.put("method", "getPosterDiscussListByPid");
     * map.put("posterId", mDZBBBean.getPosterId() + "");
     * map.put("count", mPageSize + "");
     * map.put("pageIndex", mPageIndex + "");
     */
    public void doGetPosterDiscussListByPid(int posterId, int pageIndex, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("posterId", posterId + "");
        params.put("count", Constants.PAGE_SIZE);
        params.put("pageIndex", pageIndex + "");

        CHYHttpClient.get("getPosterDiscussListByPid", params, responseHandler);
    }

    /**
     * 根据壁报评论id获取壁报评论内容
     * <p>
     * map.put("method", "getPosterDiscussById");
     * map.put("posterDiscussById", mPosterCommentId + "");
     */
    public void doGetPosterDiscussByID(int discussId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("posterDiscussById", discussId + "");

        CHYHttpClient.get("getPosterDiscussById", params, responseHandler);
    }

    /**
     * 发送评论V2 createPosterDiscussV2
     */
    public void doCreatePosterDiscuss(int userId, Integer userType, String userName, String content, int conId, int posterId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createPosterDiscussV2");
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("userName", userName);
        params.put("posterId", posterId + "");
        params.put("content", content);
        params.put("conId", conId + "");

        CHYHttpClient.post(params, responseHandler);
    }


    /**
     * 根据userId获取用户的token
     *
     * @param userId
     * @param responseHandler
     */
    public void doGetToken(int userId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getTokenByUserId");
        params.put("userId", userId + "");
        params.put("conId", Constants.getConId() + "");
        params.put("fromWhere", Constants.getFromWhere());

//        CHYHttpClient.post(params, responseHandler);
//        CHYHttpClient.postLocal(params, responseHandler);
    }

    /**
     * 根据userId获取用户所有好友和待审核的
     *
     * @param userId
     * @param conId
     * @param responseHandler
     */
    public void doGetFriendList(int userId, int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getFriendListOne");
        params.put("userId", userId + "");
        params.put("conId", conId + "");
        params.put("fromWhere", Constants.getFromWhere());

//        CHYHttpClient.post(params, responseHandler);

//        CHYHttpClient.postLocal(params, responseHandler);
    }


    /**
     * 获取所有用户，根据好友状态分类
     *
     * @param userId
     * @param conId
     */
    public void doGetAllUserList(int userId, int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getAllUserList");
        params.put("userId", userId + "");
        params.put("conId", conId + "");
        params.put("fromWhere", Constants.getFromWhere());
//        CHYHttpClient.post(params, responseHandler);

//        CHYHttpClient.postLocal(params, responseHandler);
    }

    /**
     * 添加好友
     *
     * @param userId
     * @param friendsId
     * @param conId
     * @param responseHandler
     */
    public void doCreateFriends(int userId, int userType, String userName, String userImgUrl, int friendsId, int friendsUserType, int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createFriends");
        params.put("userId", userId + "");
        params.put("friendsId", friendsId);
        params.put("conId", conId + "");
        params.put("userType", userType + "");
        params.put("userName", userName);
        params.put("userImgUrl", userImgUrl);
        params.put("friendsType", friendsUserType + "");

//        CHYHttpClient.post(params, responseHandler);

//        CHYHttpClient.postLocal(params, responseHandler);
    }

    /**
     * 同意好友申请
     *
     * @param userId
     * @param friendsId
     * @param conId
     * @param responseHandler
     */
    public void doAgreeApply(int userId, int friendsId, int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "agreeApply");
        params.put("userId", userId + "");
        params.put("friendsId", friendsId);
        params.put("conId", conId + "");

//        CHYHttpClient.post(params, responseHandler);

//        CHYHttpClient.postLocal(params, responseHandler);
    }

    /**
     * 同意好友申请
     *
     * @param userId
     * @param friendsId
     * @param conId
     * @param responseHandler
     */
    public void doRefuseApply(int userId, int friendsId, int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "refuseApply");
        params.put("userId", userId + "");
        params.put("friendsId", friendsId);
        params.put("conId", conId + "");

//        CHYHttpClient.post(params, responseHandler);

//        CHYHttpClient.postLocal(params, responseHandler);
    }

    /**
     * 删除好友
     *
     * @param userId
     * @param friendsId
     * @param conId
     * @param responseHandler
     */
    public void doDeleteFriends(int userId, int friendsId, int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "deleteFriends");
        params.put("userId", userId + "");
        params.put("friendsId", friendsId);
        params.put("conId", conId + "");

//        CHYHttpClient.post(params, responseHandler);

//        CHYHttpClient.postLocal(params, responseHandler);
    }


    /**
     * 根据userId获取用户信息
     *
     * @param responseHandler
     */
    public void doGetEmailUserInfoByMobile(String mobile, String trueName, String lan, String conId, String fromWhere, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("getUserInfoByEmail", "");
        params.put("email", mobile);
        params.put("trueName", trueName);
        params.put("lan", lan);
        params.put("conId", conId);
        params.put("fromWhere", fromWhere);

        CHYHttpClient.post(params, responseHandler);
    }

    public void doGetMobileUserInfoByMobile(String lan, String conId, String fromWhere, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("getUserInfoByIdV2", "");
        params.put("userId", SharePreferenceUtils.getUser(Constants.USER_ID));
        //params.put("mobile", mobile);
        //params.put("trueName", trueName);
        params.put("lan", lan);
        params.put("conId", conId);
        params.put("fromWhere", fromWhere);

        CHYHttpClient.post2(params, responseHandler);
    }

    /**
     * 检查数据包接口
     *
     * @param conId
     * @param dataVersion
     */
    public void doGetEsmoData(String conId, String dataVersion, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getEsmoDatas");
        params.put("conId", conId);
        params.put("dataVersion", dataVersion);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * http://incongress.cn/Conferences/conferences.do?method=getConpassDatas&conpassId=1&dataVersion=0
     * 首页数据包
     */
    public void doGetConpassDatas(String appVersion, String conpassId, String dataVersion, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getConpassDatas");
        params.put("client", Constants.CLIENT_TYPE);
        params.put("appVersion", appVersion);
        params.put("conpassId", conpassId);
        params.put("dataVersion", dataVersion);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 针对Meeting进行提问
     *
     * @param conId
     * @param userId
     * @param userType
     * @param content
     * @param speakerId
     * @param meetingId
     * @param meetingName
     */
    public void doCreateMeetingQuestion(int conId, int userId, int userType, String content, int speakerId, int meetingId, String meetingName, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createSceneShowQuestion");
        params.put("conferencesId", conId + "");
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("content", content);
        params.put("speakerId", speakerId + "");
        params.put("meetingId", meetingId + "");
        params.put("meetingName", meetingName);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 获取所有针对Meeting的提问
     *
     * @param conferenceId
     * @param lastSceneShowId
     * @param userId
     * @param userType
     * @param lan
     * @param responseHandler
     */
    public void doGetMyMeetingQuestion(int conferenceId, int lastSceneShowId, int userId, int userType, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getQuestionsBySession");
        params.put("conferencesId", conferenceId + "");
        params.put("lastSceneShowId", lastSceneShowId + "");
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("lan", lan + "");

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 分享已回答的提问到博客中
     *
     * @param conferencesId
     * @param sceneShowId
     * @param lan
     * @param jsonHttpResponseHandler
     */
    public void doShareMeetingQuestion(int conferencesId, int sceneShowId, String lan, int isShow, JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "changeIsShow");
        params.put("conferencesId", conferencesId + "");
        params.put("sceneShowId", sceneShowId);
        params.put("isShow", isShow + "");
        params.put("lan", lan + "");

        CHYHttpClient.post(params, jsonHttpResponseHandler);
    }


    /**
     * 向壁报机提问
     */
    public void doCreatePosterQuestion(int conId, int userId, int userType, String posterTitle, String content, int posterId, String posterFirstAuthor,
                                       String askQuestionUserEmail, JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createPosterQuestion");
        params.put("conferencesId", conId + "");
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("posterTitle", posterTitle);
        params.put("content", content);
        params.put("posterId", posterId + "");
        params.put("posterFirstAuthor", posterFirstAuthor);
        params.put("askQuestionUserEmail", askQuestionUserEmail);

        CHYHttpClient.post(params, jsonHttpResponseHandler);
    }

    /**
     * 获取壁报提问列表
     */
    public void doGetQuestionByPoster(int conId, int lastSceneShowId, int userId, int userType, String lan, JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getQuestionsByPoster");
        params.put("conferencesId", conId + "");
        params.put("lastSceneShowId", lastSceneShowId + "");
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("lan", lan);

        CHYHttpClient.post(params, jsonHttpResponseHandler);
    }

    /**
     * 根据posterId获取必报信息
     *
     * @param conId
     * @param posterId
     * @param lan
     */
    public void doGetPosterByID(int conId, String posterId, String lan, JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getPosterById");
        params.put("conferencesId", conId + "");
        params.put("posterId", posterId + "");
        params.put("lan", lan);

        CHYHttpClient.post(params, jsonHttpResponseHandler);
    }

    /**
     * 班车
     *
     * @param conId
     */
    public void doGetBusInfo(int conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("conId", conId + "");

        CHYHttpClient.post("getBusInfo", params, responseHandler);
    }

    /**
     * 提问列表
     */
    public void doGetQuestionsBySessionV2(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getQuestionsBySessionV2");
        params.put("conId", Constants.getConId());
        params.put("userId", AppApplication.userId);
        params.put("userType", AppApplication.userType);
        params.put("lan", AppApplication.getSystemLanuageCode());

        CHYHttpClient.post(params, responseHandler);
    }

    public void doGetQuestionsByMySession(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getMySceneShowByIdAndUserType");
        params.put("conId", 367);
        params.put("userId", AppApplication.userId);
        params.put("facultyId", AppApplication.facultyId);
        params.put("userType", AppApplication.userType);
        params.put("lan", AppApplication.getSystemLanuageCode());

        CHYHttpClient.postMyQuestion(params, responseHandler);
    }

    /*
     *参展商上方列表接口
     */
    public void doGetCzs(String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getCzs");
        params.put("conId", conId);
        params.put("lan", "cn");

        CHYHttpClient.post(params, responseHandler);
    }

    public void doGetPhotoWallTypes(String conId, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getPhotoWallTypes");
        params.put("conId", conId);
        params.put("lan", lan);

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 上传图片到照片墙
     *
     * @param conId
     * @param userId
     * @param userType
     * @param typeId
     * @param file
     * @param responseHandler
     */
    public void doCreatePhotoImage(String conId, String userId, String userType, String typeId, String lan, File file, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "createPhotoWallImg");
        params.put("conId", conId);
        params.put("userId", userId);
        params.put("userType", userType);
        params.put("typeId", typeId);
        params.put("lan", lan);

        try {
            params.put("photoImg", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CHYHttpClient.post(params, responseHandler);
    }

    public void doGetPhotoWallImgs(int userId, int userType, String conId, String lan, int typeId, int lastId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getPhotoWallImgs");
        params.put("conId", conId);
        params.put("lan", lan);
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("typeId", typeId + "");
        params.put("lastId", lastId);
        CHYHttpClient.post(params, responseHandler);
    }

    public void doPhotoWallLaud(int userId, int userType, int photoWallId, String conId, String lan, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "photoWallLaud");
        params.put("conId", conId);
        params.put("lan", lan);
        params.put("userId", userId + "");
        params.put("userType", userType + "");
        params.put("photoWallId", photoWallId + "");

        CHYHttpClient.post(params, responseHandler);
    }

    /**
     * 学院界面title列表
     *
     * @param responseHandler
     */
    public void doGetCollegeTitle(JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getModelListByConId");
        params.put("totalConId", Constants.PROJECT_ID);
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    public void doGetSearchCollegeTitle(String searchString, String lastId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getDataListByConIdAndSearchString");
        params.put("totalConId", Constants.PROJECT_ID);
        params.put("searchString", StringUtils.utf8Encode(searchString));
        params.put("lastId", lastId);
        params.put("row", 10);
        params.put("lan", AppApplication.getSystemLanuageCode());
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    /**
     * 火速上线列表
     *
     * @param modelId         未知
     * @param lastId          第一次请求需要传-1，后面再传收到的lastId的值，主要用于获取是否有更多数据
     * @param searchString    根据搜索的字符串搜索
     * @param responseHandler
     */
    public void doGetFastOnLine(String modelId, String lastId, String searchString, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getZuiXinOrTopData");
        params.put("modelId", modelId);
        params.put("lastId", lastId);
        params.put("row", Constants.MAXDATA);
        params.put("searchString", searchString);
        CHYHttpClient.postLocal_Other(params, responseHandler);
    }

    /**
     * 通过modelId获取当年课件天数
     * @param modelId
     * @param responseHandler
     */

    public void doGetDayArrayListData(String modelId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getDayArrayByModelId");
        params.put("modelId", modelId);
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    //获取某一年的课件信息
    public void doGetCollegeListDetail(String day, String modelId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getClassArryAndSessionArray");
        params.put("modelId", modelId);
        params.put("timeStart", day);
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    //获取当年可预约的课件天数
    public void doGetCollegeBookDays(String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getDayArrayByConId");
        params.put("conId", conId);
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    //获取当年可预约的课件信息
    public void doGetCollegeBookDetailList(String timeStart, String conId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getClassArryAndSessionArrayByConIdKYY");
        params.put("conId", conId);
        params.put("timeStart", timeStart);
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    //获取当年预约的课件信息
    public void doGetCollegeBookDetail(String sessionId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getMeetingListBySessionIdKYY");
        params.put("sessionId", sessionId);
        params.put("conId", Constants.getConId());
        params.put("userId", SharePreferenceUtils.getUser(Constants.USER_ID));
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    //获取可播放的课件信息
    public void doGetCollegeVideoDetail(String sessionId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getDataListBySessionId");
        params.put("sessionId", sessionId);
        CHYHttpClient.postExamTable(params, responseHandler);
    }

    //预约课件
    public void doGetOrderCourse(String conId, String topic, String userId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "coursewareReservation");
        params.put("conId", conId);
        params.put("topic", StringUtils.utf8Encode(topic));
        params.put("userId", userId);
        CHYHttpClient.post(params, responseHandler);
    }

    //获取我的预约课件
    public void doGetMYOrderCourse(int conId, String userId, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "getUserReservationMeetingByConIdAndUserId");
        params.put("conId", conId);
        params.put("userId", userId);
        CHYHttpClient.post(params, responseHandler);
    }
    //删除我的预约课件
    public void doDeleteMYOrderCourse(String topic, JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("method", "cancelCoursewareReservation");
        params.put("conId", Constants.getConId());
        params.put("userId", SharePreferenceUtils.getUser(Constants.USER_ID));
        params.put("topic", StringUtils.utf8Encode(topic));
        CHYHttpClient.post(params, responseHandler);
    }
    //获取展商头部信息
    public void doGetExhibitorTopInfo(JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("method", "getExhibitorMenu");
        params.put("conId", Constants.getConId());
        CHYHttpClient.postNew(params, responseHandler);
    }
    //获取展商列表信息
    public void doGetExhibitorListInfo(String lastId, JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("method", "getExhibitorsV2ListByConIdV1");
        params.put("conId", Constants.getConId());
        params.put("row", 20);
        params.put("lastId", lastId);
        CHYHttpClient.postNew(params, responseHandler);
    }
    //获取展商详细信息
    public void doGetExhibitorDetailInfo(int exhibitorsId, JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("method", "getExhibitorsV2ById");
        params.put("exhibitorsId", exhibitorsId);
        CHYHttpClient.postNew(params, responseHandler);
    }
    //上传二维码信息
    public void doUploadQRCodeInfo(String userId, JsonHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        params.put("method", "mealsAndCardsCheck");
        params.put("conId", "387");
        params.put("firstuserId", "204907");
        params.put("seconduserId", userId);
        CHYHttpClient.postLOCAL(params, responseHandler);
    }

}
