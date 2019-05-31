package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Admin on 2017/7/18.
 */

public class ResourceBeans {

    private List<KjArrayBean> kjArray;
    private List<DataNewArrBean> dataNewArr;
    private List<DataHotArrBean> dataHotArr;
    private List<ResourcesBottomArrayBean> resourcesBottomArray;

    public List<KjArrayBean> getKjArray() {
        return kjArray;
    }

    public void setKjArray(List<KjArrayBean> kjArray) {
        this.kjArray = kjArray;
    }

    public static class KjArrayBean {
        /**
         * dataId : 3151
         * title : Risk-adapted therapy for early-stage NK/T-cell lymphoma
         * dataUrl : http://app.incongress.cn/Exam/data?method=getDataByDataId&dataId=3151
         * firstPic : http://app.incongress.cn/Exam/files/img/2016-11-01_1477967344875.png
         * timeShow : 2016-11-01 10:29 发布
         * type : 1
         */

        private int dataId;
        private String title;
        private String dataUrl;
        private String firstPic;
        private String timeShow;
        private int type;

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDataUrl() {
            return dataUrl;
        }

        public void setDataUrl(String dataUrl) {
            this.dataUrl = dataUrl;
        }

        public String getFirstPic() {
            return firstPic;
        }

        public void setFirstPic(String firstPic) {
            this.firstPic = firstPic;
        }

        public String getTimeShow() {
            return timeShow;
        }

        public void setTimeShow(String timeShow) {
            this.timeShow = timeShow;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }


    public List<DataNewArrBean> getDataNewArr() {
        return dataNewArr;
    }

    public void setDataNewArr(List<DataNewArrBean> dataNewArr) {
        this.dataNewArr = dataNewArr;
    }

    public List<DataHotArrBean> getDataHotArr() {
        return dataHotArr;
    }

    public void setDataHotArr(List<DataHotArrBean> dataHotArr) {
        this.dataHotArr = dataHotArr;
    }

    public List<ResourcesBottomArrayBean> getResourcesBottomArray() {
        return resourcesBottomArray;
    }

    public void setResourcesBottomArray(List<ResourcesBottomArrayBean> resourcesBottomArray) {
        this.resourcesBottomArray = resourcesBottomArray;
    }


    public static class DataNewArrBean {
        private int dataId;
        private String title;
        private String videoUrl;
        private int videoType;
        private String videoId;
        private int meetingId;
        private TimeStartBean timeStart;
        private String author;
        private String videoImage;
        private String openTime;
        private String createTime;
        private String classesName;
        private String speakerName;
        private String speakerImg;
        private String roleName;

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getVideoType() {
            return videoType;
        }

        public void setVideoType(int videoType) {
            this.videoType = videoType;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public int getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(int meetingId) {
            this.meetingId = meetingId;
        }

        public TimeStartBean getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(TimeStartBean timeStart) {
            this.timeStart = timeStart;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getClassesName() {
            return classesName;
        }

        public void setClassesName(String classesName) {
            this.classesName = classesName;
        }

        public String getSpeakerName() {
            return speakerName;
        }

        public void setSpeakerName(String speakerName) {
            this.speakerName = speakerName;
        }

        public String getSpeakerImg() {
            return speakerImg;
        }

        public void setSpeakerImg(String speakerImg) {
            this.speakerImg = speakerImg;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public static class TimeStartBean {
            /**
             * date : 23
             * day : 6
             * hours : 16
             * minutes : 15
             * month : 5
             * nanos : 0
             * seconds : 0
             * time : 1529741700000
             * timezoneOffset : -480
             * year : 118
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

    public static class DataHotArrBean {

        private int dataId;
        private String title;
        private String videoUrl;
        private int videoType;
        private String videoId;
        private int meetingId;
        private TimeStartBean timeStart;
        private String author;
        private String videoImage;
        private String openTime;
        private String createTime;
        private String classesName;
        private String speakerName;
        private String speakerImg;
        private String roleName;

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getVideoType() {
            return videoType;
        }

        public void setVideoType(int videoType) {
            this.videoType = videoType;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public int getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(int meetingId) {
            this.meetingId = meetingId;
        }

        public TimeStartBean getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(TimeStartBean timeStart) {
            this.timeStart = timeStart;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getClassesName() {
            return classesName;
        }

        public void setClassesName(String classesName) {
            this.classesName = classesName;
        }

        public String getSpeakerName() {
            return speakerName;
        }

        public void setSpeakerName(String speakerName) {
            this.speakerName = speakerName;
        }

        public String getSpeakerImg() {
            return speakerImg;
        }

        public void setSpeakerImg(String speakerImg) {
            this.speakerImg = speakerImg;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public static class TimeStartBean {
            /**
             * date : 21
             * day : 4
             * hours : 14
             * minutes : 0
             * month : 5
             * nanos : 0
             * seconds : 0
             * time : 1529560800000
             * timezoneOffset : -480
             * year : 118
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

    public static class ResourcesBottomArrayBean {
        /**
         * resourceName : CSD
         * imgUrl : http://app.incongress.cn/files/esmo/2/csd2019.png
         * resourceInfo : 2018年会议课件
         * type : 0
         * width : null
         * height : null
         * linkUrl : null
         * totalConId : 2
         */

        private String resourceName;
        private String imgUrl;
        private String resourceInfo;
        private int type;
        private int width;
        private int height;
        private String linkUrl;
        private int totalConId;

        public String getResourceName() {
            return resourceName;
        }

        public void setResourceName(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getResourceInfo() {
            return resourceInfo;
        }

        public void setResourceInfo(String resourceInfo) {
            this.resourceInfo = resourceInfo;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public int getTotalConId() {
            return totalConId;
        }

        public void setTotalConId(int totalConId) {
            this.totalConId = totalConId;
        }
    }
}
