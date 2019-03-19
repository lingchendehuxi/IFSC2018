package com.android.incongress.cd.conference.model;

import java.util.List;
//泛型暂时无法存储，后续再看，重新创建一个实体

public class LiveForOrderInfoBean {

    /**
     * sessionGroupId : 1960007
     * classesName : 一层多功能B厅#@#Function Hall B, Level 1
     * sessionGroupName : CIT2019开幕式和全体大会#@#Opening of CIT 2019 in Partnership with TCT and Plenary Sessions
     * sessionDay : 2019-03-15
     * weeks : 五
     * liveUrl : https://live.polyv.cn/watch/293730
     * time : 19:30-20:00
     * startTime : 2019-03-15 19:30
     * scrRoles : 主持人
     * zcrArray : [{"主持人":"顾东风"}]
     * meetings : []
     */

    private int sessionGroupId;
    private String classesName;
    private String sessionGroupName;
    private String sessionDay;
    private String weeks;
    private String liveUrl;
    private String time;
    private String startTime;
    private String scrRoles;
    private List<?> zcrArray;
    private List<?> meetings;

    public int getSessionGroupId() {
        return sessionGroupId;
    }

    public void setSessionGroupId(int sessionGroupId) {
        this.sessionGroupId = sessionGroupId;
    }

    public String getLiveClassesName() {
        return classesName;
    }

    public void setLiveClassesName(String classesName) {
        this.classesName = classesName;
    }

    public String getSessionGroupName() {
        return sessionGroupName;
    }

    public void setSessionGroupName(String sessionGroupName) {
        this.sessionGroupName = sessionGroupName;
    }

    public String getSessionDay() {
        return sessionDay;
    }

    public void setSessionDay(String sessionDay) {
        this.sessionDay = sessionDay;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getScrRoles() {
        return scrRoles;
    }

    public void setScrRoles(String scrRoles) {
        this.scrRoles = scrRoles;
    }

    public List<?> getZcrArray() {
        return zcrArray;
    }

    public void setZcrArray(List<?> zcrArray) {
        this.zcrArray = zcrArray;
    }

    public List<?> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<?> meetings) {
        this.meetings = meetings;
    }

}
