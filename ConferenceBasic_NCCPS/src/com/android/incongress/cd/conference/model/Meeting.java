package com.android.incongress.cd.conference.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Meeting extends DataSupport {

    /**
     * meetingId : 170360
     * topic : 肝癌手术/肝移植
     * summary :
     * classesId : 3277
     * meetingDay : 2016-09-22
     * startTime : 08:30
     * endTime : 08:50
     * conFieldId : -1
     * sessionGroupId : 34143
     * facultyId : 151242
     * roleId : 857
     */
    private int meetingId;
    private String topic;
    private String topicEn;
    private String summary;
    private int classesId;
    private String meetingDay;
    private String startTime;
    private String endTime;
    private String conFieldId;
    private int sessionGroupId;
    private String facultyId;
    private String roleId;
    private int attention;

    public String getTopicEn() {
        return topicEn;
    }

    public void setTopicEn(String topicEn) {
        this.topicEn = topicEn;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }

    public String getMeetingDay() {
        return meetingDay;
    }

    public void setMeetingDay(String meetingDay) {
        this.meetingDay = meetingDay;
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

    public String getConFieldId() {
        return conFieldId;
    }

    public void setConFieldId(String conFieldId) {
        this.conFieldId = conFieldId;
    }

    public int getSessionGroupId() {
        return sessionGroupId;
    }

    public void setSessionGroupId(int sessionGroupId) {
        this.sessionGroupId = sessionGroupId;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
