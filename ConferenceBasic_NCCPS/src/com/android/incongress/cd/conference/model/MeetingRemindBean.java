package com.android.incongress.cd.conference.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2017/5/23.
 */

public class MeetingRemindBean extends DataSupport {
    private String sessionName;
    private int sessionGroupId;
    private String sesionStartTime;
    private String sessionEndTime;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getSessionGroupId() {
        return sessionGroupId;
    }

    public void setSessionGroupId(int sessionGroupId) {
        this.sessionGroupId = sessionGroupId;
    }

    public String getSesionStartTime() {
        return sesionStartTime;
    }

    public void setSesionStartTime(String sesionStartTime) {
        this.sesionStartTime = sesionStartTime;
    }

    public String getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(String sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }
}
