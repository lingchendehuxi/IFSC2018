package com.android.incongress.cd.conference.beans;

import java.util.List;

public class AskPupBean {
    private String firName;
    private String topic;
    private int spearkerId;
    private int meetingId;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSpearkerId() {
        return spearkerId;
    }

    public void setSpearkerId(int spearkerId) {
        this.spearkerId = spearkerId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getFirName() {
        return firName;
    }

    public void setFirName(String firName) {
        this.firName = firName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
