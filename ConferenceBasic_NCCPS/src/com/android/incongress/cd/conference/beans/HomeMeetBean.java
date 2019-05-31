package com.android.incongress.cd.conference.beans;

import java.io.Serializable;

/**
 * Created by 13008 on 2019/4/10.
 */

public class HomeMeetBean implements Serializable{

    /**
     * {
     * 		"totalConId": 5,
     * 		"iconUrl": "http:\/\/app.incongress.cn\/files\/esmo\/3\/csccm2019.png",
     * 		"state": 1,
     * 		"conId": 79,
     * 		"fromWhere": "csccm",
     * 		"conferencesName": "CSCCM2019",
     * 		"conferencesDays": "2019年5月23-26日",
     * 		"conferencesAddress": "中国 珠海",
     * 		"conferencesStartDay": "2019-05-22",
     * 		"type": 1,
     * 		"htmlUrl": ""
     *        }
     */

    private int totalConId;
    private String iconUrl;
    private int state;
    private int conId;
    private String fromWhere;
    private String conferencesName;
    private String conferencesDays;
    private String conferencesAddress;
    private String conferencesStartDay;
    private int type;
    private String htmlUrl;
    //新添加的月份
    private int monthString;
    private boolean isCurrentMonth;
    private boolean isBegin;

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        isCurrentMonth = currentMonth;
    }

    public boolean isBegin() {
        return isBegin;
    }

    public void setBegin(boolean begin) {
        isBegin = begin;
    }

    public int getMonthString() {
        return monthString;
    }

    public void setMonthString(int monthString) {
        this.monthString = monthString;
    }

    public int getTotalConId() {
        return totalConId;
    }

    public void setTotalConId(int totalConId) {
        this.totalConId = totalConId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getConferencesName() {
        return conferencesName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setConferencesName(String conferencesName) {
        this.conferencesName = conferencesName;
    }

    public String getConferencesDays() {
        return conferencesDays;
    }

    public void setConferencesDays(String conferencesDays) {
        this.conferencesDays = conferencesDays;
    }

    public String getConferencesAddress() {
        return conferencesAddress;
    }

    public void setConferencesAddress(String conferencesAddress) {
        this.conferencesAddress = conferencesAddress;
    }

    public String getConferencesStartDay() {
        return conferencesStartDay;
    }

    public void setConferencesStartDay(String conferencesStartDay) {
        this.conferencesStartDay = conferencesStartDay;
    }
}
