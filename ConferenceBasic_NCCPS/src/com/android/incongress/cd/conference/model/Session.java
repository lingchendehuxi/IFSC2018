package com.android.incongress.cd.conference.model;

import android.os.Parcelable;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Session extends DataSupport implements Serializable {

    /**
     * sessionGroupId : 34143
     * sessionName : 继教-肝胆胰专场
     * classesId : 3277
     * sessionDay : 2016-09-22
     * startTime : 08:30
     * endTime : 12:00
     * conFieldId :
     * remark :
     * facultyId : 151240,151241
     * roleId : 856,856
     */
    private int id;
    private int sessionGroupId;
    private String sessionName;
    private String sessionNameEN;
    private int classesId;
    private String sessionDay;
    private String startTime;
    private String endTime;
    private String conFieldId;
    private String remark;
    private String facultyId;
    private String roleId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int attention;

    public String getSessionNameEN() {
        return sessionNameEN;
    }

    public void setSessionNameEN(String sessionNameEN) {
        this.sessionNameEN = sessionNameEN;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getSessionGroupId() {
        return sessionGroupId;
    }

    public void setSessionGroupId(int sessionGroupId) {
        this.sessionGroupId = sessionGroupId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }

    public String getSessionDay() {
        return sessionDay;
    }

    public void setSessionDay(String sessionDay) {
        this.sessionDay = sessionDay;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
