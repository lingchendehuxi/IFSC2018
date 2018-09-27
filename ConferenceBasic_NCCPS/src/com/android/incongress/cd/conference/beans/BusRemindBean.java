package com.android.incongress.cd.conference.beans;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/5/18.
 */

public class BusRemindBean  extends DataSupport{

    /**
     * busInfoId : 29
     * busDate : 2017-05-26
     * busTime : 7:20
     * backTime : 18:30
     * busFrom : 大雁塔假日酒店
     * busTo : 曲江国际会议中心
     * isVip : 0
     */

    private int busInfoId;
    private String busDate;
    private String busTime;
    private String backTime;
    private String busFrom;
    private String busTo;
    private int isVip;
    private boolean isNotify;
    private int isStartOrBack; //是开始还是返回
    private int is30or15; //是15分钟的闹钟还是30分钟的闹钟

    public int getIs30or15() {
        return is30or15;
    }

    public void setIs30or15(int is30or15) {
        this.is30or15 = is30or15;
    }

    public int getIsStartOrBack() {
        return isStartOrBack;
    }

    public void setIsStartOrBack(int isStartOrBack) {
        this.isStartOrBack = isStartOrBack;
    }

    public boolean isNotify() {
        return isNotify;
    }

    public void setNotify(boolean notify) {
        isNotify = notify;
    }

    public int getBusInfoId() {
        return busInfoId;
    }

    public void setBusInfoId(int busInfoId) {
        this.busInfoId = busInfoId;
    }

    public String getBusDate() {
        return busDate;
    }

    public void setBusDate(String busDate) {
        this.busDate = busDate;
    }

    public String getBusTime() {
        return busTime;
    }

    public void setBusTime(String busTime) {
        this.busTime = busTime;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    public String getBusFrom() {
        return busFrom;
    }

    public void setBusFrom(String busFrom) {
        this.busFrom = busFrom;
    }

    public String getBusTo() {
        return busTo;
    }

    public void setBusTo(String busTo) {
        this.busTo = busTo;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }
}
