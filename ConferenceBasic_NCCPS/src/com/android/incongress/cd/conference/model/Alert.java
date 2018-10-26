package com.android.incongress.cd.conference.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Jacky on 2016/7/25.
 * 声明： Alert主要用于闹钟，idenId是闹钟唯一标识，relativeid是meeting和session的关联标识
 * meeting 使用meetID,session使用classID 为唯一标识
 */
public class Alert extends DataSupport {
    private int id;//提醒id
    private String date;//提醒时间
    private String repeatdistance;//提醒间隔
    private String repeattimes;//提醒次数
    private int enable;//提醒可用 0不可用 1为可用
    private String title;//提醒内容
    private int type;//提醒类型 0会议 1为发言
    private String relativeid;//提醒关联ID 关联sessionid或meetingid；
    private String start;
    private String end;
    private String room;
    private long time;
    private int idenId;

    public int getIdenId() {
        return idenId;
    }

    public void setIdenId(int idenId) {
        this.idenId = idenId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeatdistance() {
        return repeatdistance;
    }

    public void setRepeatdistance(String repeatdistance) {
        this.repeatdistance = repeatdistance;
    }

    public String getRepeattimes() {
        return repeattimes;
    }

    public void setRepeattimes(String repeattimes) {
        this.repeattimes = repeattimes;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRelativeid() {
        return relativeid;
    }

    public void setRelativeid(String relativeid) {
        this.relativeid = relativeid;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
