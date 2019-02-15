package com.android.incongress.cd.conference.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/8/24.
 */
public class TimeBean extends DataSupport {

    /**
     * startTime : 8
     * endTime : 12
     * difference : 16
     */
    private int id;
    private int startTime;
    private int endTime;
    private int difference;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDifference() {
        return difference;
    }

    public void setDifference(int difference) {
        this.difference = difference;
    }
}
