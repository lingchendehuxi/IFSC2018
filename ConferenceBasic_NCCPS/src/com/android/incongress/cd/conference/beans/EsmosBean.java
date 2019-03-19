package com.android.incongress.cd.conference.beans;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Jacky on 2016/11/24.
 */
public class EsmosBean extends LitePalSupport {
    /**
     * backgroundUrl : http://incongress.cn/files/esmo/194/b1.png
     * conferencesAddress : 中国 厦门
     * conferencesDays : 2016年9月21-25日
     * conferencesName : CSCO年会 2016
     * conpassId : 1
     * createTime : {"date":22,"day":2,"hours":14,"minutes":34,"month":10,"nanos":0,"seconds":4,"time":1479796444000,"timezoneOffset":-480,"year":116}
     * dataConferencesId : 194
     * esmoId : 1
     * htmlUrl :
     * iconUrl : http://incongress.cn/files/esmo/194/csco.png
     * remark : 大会将秉承CSCO 的根本宗旨，进一步促进国际、国内 开展临床肿瘤学领域的学术交流和科技合作，鼓励支持临床研究和创新，提倡规范化综合治疗，积极推动学科发展。
     * sortNumber : 1
     * type : 1
     */

    private String backgroundUrl;
    private String conferencesAddress;
    private String conferencesDays;
    private String conferencesName;
    private int conpassId;
    private CreateTimeBean createTime;
    private int dataConferencesId;
    private int esmoId;
    private String htmlUrl;
    private String iconUrl;
    private String remark;
    private int sortNumber;
    private int type;
    private int state;
    private int isExist; //本地是否已经存在
    private int isNeedUpdate;//是否有更新

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public int getIsNeedUpdate() {
        return isNeedUpdate;
    }

    public void setIsNeedUpdate(int isNeedUpdate) {
        this.isNeedUpdate = isNeedUpdate;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getConferencesAddress() {
        return conferencesAddress;
    }

    public void setConferencesAddress(String conferencesAddress) {
        this.conferencesAddress = conferencesAddress;
    }

    public String getConferencesDays() {
        return conferencesDays;
    }

    public void setConferencesDays(String conferencesDays) {
        this.conferencesDays = conferencesDays;
    }

    public String getConferencesName() {
        return conferencesName;
    }

    public void setConferencesName(String conferencesName) {
        this.conferencesName = conferencesName;
    }

    public int getConpassId() {
        return conpassId;
    }

    public void setConpassId(int conpassId) {
        this.conpassId = conpassId;
    }

    public CreateTimeBean getCreateTime() {
        return createTime;
    }

    public void setCreateTime(CreateTimeBean createTime) {
        this.createTime = createTime;
    }

    public int getDataConferencesId() {
        return dataConferencesId;
    }

    public void setDataConferencesId(int dataConferencesId) {
        this.dataConferencesId = dataConferencesId;
    }

    public int getEsmoId() {
        return esmoId;
    }

    public void setEsmoId(int esmoId) {
        this.esmoId = esmoId;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(int sortNumber) {
        this.sortNumber = sortNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class CreateTimeBean {
        /**
         * date : 22
         * day : 2
         * hours : 14
         * minutes : 34
         * month : 10
         * nanos : 0
         * seconds : 4
         * time : 1479796444000
         * timezoneOffset : -480
         * year : 116
         */

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


