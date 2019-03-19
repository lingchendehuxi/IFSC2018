package com.android.incongress.cd.conference.beans;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class LiveInfoBean extends LitePalSupport implements Serializable {
    /**
     * sessionId : 1959922
     * sessionName : Session1#@#Session1
     * classId : 9253
     * className : Room1#@#Room1
     * startTime : 2019-03-13 09:00:00
     * endTime : 2019-03-13 11:00:00
     * zanCount : 0
     * lookCount : 0
     * isNow : 1
     * liveUrl : http://live.incongress.cn/watch/293730
     * yuyueCount : 1
     */
    private int id;
    private int sessionId;
    private String sessionName;
    private int classId;
    private String className;
    private String startTime;
    private String endTime;
    private int zanCount;
    private int lookCount;
    private int isNow;
    private String liveUrl;
    private int yuyueCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getLiveClassName() {
        return className;
    }

    public void setLiveClassName(String className) {
        this.className = className;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getZanCount() {
        return zanCount;
    }

    public void setZanCount(int zanCount) {
        this.zanCount = zanCount;
    }

    public int getLookCount() {
        return lookCount;
    }

    public void setLookCount(int lookCount) {
        this.lookCount = lookCount;
    }

    public int getIsNow() {
        return isNow;
    }

    public void setIsNow(int isNow) {
        this.isNow = isNow;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public int getYuyueCount() {
        return yuyueCount;
    }

    public void setYuyueCount(int yuyueCount) {
        this.yuyueCount = yuyueCount;
    }
}
