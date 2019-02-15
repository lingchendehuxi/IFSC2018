package com.android.incongress.cd.conference.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.util.Const;

import com.android.incongress.cd.conference.beans.CommunityTopicContentBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.DZBBBean;
import com.android.incongress.cd.conference.beans.DZBBDiscussResponseBean;
import com.android.incongress.cd.conference.beans.IncongressBean;
import com.android.incongress.cd.conference.beans.VersionBean;
import com.android.incongress.cd.conference.save.SharePreferenceUtils;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonParser {

    public static String readJsonString(InputStream is) {
        String jsonStr = "";
        try {
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            jsonStr = EncodingUtils.getString(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }

    //解析初始化数据
    public static IncongressBean parseIncongress(JSONObject str) {
        IncongressBean bean = new IncongressBean();
        try {
            String client = JSONCatch.parseString("client",str);
            String appVersion = JSONCatch.parseString("appVersion",str);
            String clientVersion = "";
            if (client.equals("1")) {
                clientVersion = JSONCatch.parseString("clientVersion",str);
            }
            String url = "";
            if (client.equals("1")) {
                url = JSONCatch.parseString("url",str);
            }
            if(str.has("currentConId")){
                SharePreferenceUtils.saveAppInt(Constants.CONID,JSONCatch.parseInt("currentConId",str));
            }
            if(str.has("currentFromWhere")){
                SharePreferenceUtils.saveAppString(Constants.FROMWHERE,JSONCatch.parseString("currentFromWhere",str));
            }
            String version = JSONCatch.parseString("version",str);
            int newsCount = JSONCatch.parseInt("newsCount",str);
            int reCount = JSONCatch.parseInt("reCount",str);
            List<VersionBean> versions = new ArrayList<VersionBean>();
            Gson gson = new Gson();
            versions = gson.fromJson(version, new TypeToken<List<VersionBean>>() {}.getType());
            bean.setClient(client);
            bean.setAppVersion(appVersion);
            bean.setClientVersion(clientVersion);
            bean.setUrl(url);
            bean.setReCount(reCount);
            bean.setNewsCount(newsCount);
            bean.setVersionList(versions);
        } catch (Exception e) {

        }
        return bean;
    }

    //分别获取中文和英文的数据
    public static String[] getChinesAndEnBySplit(String content) {
        String name = "";
        String enName = "";

        String contents[] = content.split(Constants.ENCHINASPLIT);
        if (contents.length == 1) {
            name = contents[0];
            enName = contents[0];
        }

        if (contents.length == 2) {
            if (contents[0].length() == 0) {
                name = enName = contents[1];
            }
            if (contents[1].length() == 0) {
                name = enName = contents[0];
            } else {
                name = contents[0];
                enName = contents[1];
            }
        }

        return new String[]{name, enName};
    }


    //解析电子壁报中的评论 列表
    public static List<CommunityTopicContentBean> parseDZBBContent(String json) {
        List<CommunityTopicContentBean> list = new ArrayList<CommunityTopicContentBean>();
        int maxCount = JSONUtils.getInt(json, "maxCount", -1);
        JSONArray array = JSONUtils.getJSONArray(json, "array", null);
        System.out.println("数据===" + json);
        if (maxCount == 0) {
            return list;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                String userName = obj.getString("userName");
                String content = obj.getString("content");
                String createTime = obj.getString("createTime");

                try {
                    content = URLDecoder.decode(content,Constants.ENCODING_UTF8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    content = "Decode error";
                }

                CommunityTopicContentBean bean = new CommunityTopicContentBean();
                bean.setUserName(userName);
                bean.setContent(content);
                bean.setTime(createTime);

                list.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    //解析发表电子壁报评论后返回的数据，也就是是否成功
    public static DZBBDiscussResponseBean parseDiscussSuccess(String json) {
        DZBBDiscussResponseBean bean = new DZBBDiscussResponseBean();
        int state = JSONUtils.getInt(json, "state", 0);
        int userId = JSONUtils.getInt(json, "userId", -1);
        String userName = JSONUtils.getString(json, "userName", "游客未知");
        int posterDiscussId = JSONUtils.getInt(json, "posterDiscussId", -1);
        bean.setUserName(userName);
        bean.setUserId(userId);
        bean.setState(state);
        bean.setPosterDiscussId(posterDiscussId);
        return bean;
    }


    // 根据id获得一个评论
    public static CommunityTopicContentBean parseDZBBOneContent(String json) {
        CommunityTopicContentBean bean = new CommunityTopicContentBean();
        String userName = JSONUtils.getString(json, "userName", "");
        String content = JSONUtils.getString(json, "content", "");
        String createTime = JSONUtils.getString(json, "createTime", "");

        try {
            content = URLDecoder.decode(content, Constants.ENCODING_UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            content = "Decode error";
        }
        bean.setUserName(userName);
        bean.setTime(createTime);
        bean.setType(1);
        bean.setContent(content);

        if (userName.equals("-1")) {
            return null;
        }
        return bean;
    }
}
