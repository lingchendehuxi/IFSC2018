package com.android.incongress.cd.conference.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Conferences extends DataSupport {


    /**
     * abbreviation : csco
     * adminuserId : 1
     * code : pwho
     * conferencesAddress : 厦门
     * conferencesEndTime : {"date":20,"day":0,"hours":0,"minutes":0,"month":8,"nanos":0,"seconds":0,"time":1442678400000,"timezoneOffset":-480,"year":115}
     * conferencesId : 194
     * conferencesName : 第十九届全国临床肿瘤学大会
     * conferencesRemark :
     * conferencesStartTime : {"date":17,"day":4,"hours":0,"minutes":0,"month":8,"nanos":0,"seconds":0,"time":1442419200000,"timezoneOffset":-480,"year":115}
     * conferencesState : 1
     * createTime : null
     * enLink :
     * posterShowState : 0
     * posterState : 0
     * viewState : 1
     * zhLink :
     */

    private String abbreviation;
    private int adminuserId;
    private String code;
    private String conferencesAddress;
    /**
     * date : 20
     * day : 0
     * hours : 0
     * minutes : 0
     * month : 8
     * nanos : 0
     * seconds : 0
     * time : 1442678400000
     * timezoneOffset : -480
     * year : 115
     */

    private ConferencesEndTimeBean conferencesEndTime;
    private int conferencesId;
    private String conferencesName;
    private String conferencesRemark;
    /**
     * date : 17
     * day : 4
     * hours : 0
     * minutes : 0
     * month : 8
     * nanos : 0
     * seconds : 0
     * time : 1442419200000
     * timezoneOffset : -480
     * year : 115
     */

    private ConferencesStartTimeBean conferencesStartTime;
    private int conferencesState;
    private Object createTime;
    private String enLink;
    private int posterShowState;
    private int posterState;
    private int viewState;
    private String zhLink;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getAdminuserId() {
        return adminuserId;
    }

    public void setAdminuserId(int adminuserId) {
        this.adminuserId = adminuserId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConferencesAddress() {
        return conferencesAddress;
    }

    public void setConferencesAddress(String conferencesAddress) {
        this.conferencesAddress = conferencesAddress;
    }

    public ConferencesEndTimeBean getConferencesEndTime() {
        return conferencesEndTime;
    }

    public void setConferencesEndTime(ConferencesEndTimeBean conferencesEndTime) {
        this.conferencesEndTime = conferencesEndTime;
    }

    public int getConferencesId() {
        return conferencesId;
    }

    public void setConferencesId(int conferencesId) {
        this.conferencesId = conferencesId;
    }

    public String getConferencesName() {
        return conferencesName;
    }

    public void setConferencesName(String conferencesName) {
        this.conferencesName = conferencesName;
    }

    public String getConferencesRemark() {
        return conferencesRemark;
    }

    public void setConferencesRemark(String conferencesRemark) {
        this.conferencesRemark = conferencesRemark;
    }

    public ConferencesStartTimeBean getConferencesStartTime() {
        return conferencesStartTime;
    }

    public void setConferencesStartTime(ConferencesStartTimeBean conferencesStartTime) {
        this.conferencesStartTime = conferencesStartTime;
    }

    public int getConferencesState() {
        return conferencesState;
    }

    public void setConferencesState(int conferencesState) {
        this.conferencesState = conferencesState;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public String getEnLink() {
        return enLink;
    }

    public void setEnLink(String enLink) {
        this.enLink = enLink;
    }

    public int getPosterShowState() {
        return posterShowState;
    }

    public void setPosterShowState(int posterShowState) {
        this.posterShowState = posterShowState;
    }

    public int getPosterState() {
        return posterState;
    }

    public void setPosterState(int posterState) {
        this.posterState = posterState;
    }

    public int getViewState() {
        return viewState;
    }

    public void setViewState(int viewState) {
        this.viewState = viewState;
    }

    public String getZhLink() {
        return zhLink;
    }

    public void setZhLink(String zhLink) {
        this.zhLink = zhLink;
    }

    public static class ConferencesEndTimeBean {
        private int date;
        private int day;
        private int hours;
        private int minutes;
        private int month;
        private int nanos;
        private int seconds;
        private long time;
        private int timezoneOffset;
        private int year;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getNanos() {
            return nanos;
        }

        public void setNanos(int nanos) {
            this.nanos = nanos;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

    public static class ConferencesStartTimeBean {
        private int date;
        private int day;
        private int hours;
        private int minutes;
        private int month;
        private int nanos;
        private int seconds;
        private long time;
        private int timezoneOffset;
        private int year;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getNanos() {
            return nanos;
        }

        public void setNanos(int nanos) {
            this.nanos = nanos;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}
