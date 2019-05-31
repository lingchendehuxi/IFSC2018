package com.android.incongress.cd.conference.beans;

/**
 * Created by 13008 on 2019/4/12.
 */

public class HomeLiveBean {

    /**
     * sessionGroupId : 1961001
     * sessionGroupName : 北京林特医药科技有限公司卫星会
     * sessionStartTime : 2019-04-25 12:30:00
     * sessionEndTime : 2019-04-25 13:00:00
     * className : 十字门厅B#@#十字门厅B
     * liveUrl : https://live.polyv.cn/watch/293730
     */

    private int sessionGroupId;
    private String sessionGroupName;
    private String sessionStartTime;
    private String sessionEndTime;
    private String className;
    private String liveUrl;

    public int getSessionGroupId() {
        return sessionGroupId;
    }

    public void setSessionGroupId(int sessionGroupId) {
        this.sessionGroupId = sessionGroupId;
    }

    public String getSessionGroupName() {
        return sessionGroupName;
    }

    public void setSessionGroupName(String sessionGroupName) {
        this.sessionGroupName = sessionGroupName;
    }

    public String getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }
}
