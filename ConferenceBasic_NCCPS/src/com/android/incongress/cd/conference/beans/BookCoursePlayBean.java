package com.android.incongress.cd.conference.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13008 on 2019/2/13.
 * 学院中课件可以播放的实体
 */

public class BookCoursePlayBean{

    /**
     * videoArray : [{"dataId":8306,"title":"2012年青年医师研究者奖获奖者,2012 YIA Winner","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F2012%E5%B9%B4%E9%9D%92%E5%B9%B4%E5%8C%BB%E5%B8%88%E7%A0%94%E7%A9%B6%E8%80%85%E5%A5%96%E8%8E%B7%E5%A5%96%E8%80%85-%E4%BE%AF%E9%9D%99%E6%B3%A2.mp4","meetingId":303229,"timeStart":{"date":22,"day":4,"hours":8,"minutes":35,"month":2,"nanos":0,"seconds":0,"time":1521678900000,"timezoneOffset":-480,"year":118},"author":"侯静波","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524020797994.jpg","openTime":"2018-04-18 11:06上线","createTime":"2018-04-18 11:06:37","classesName":"","speakerName":"Jingbo Hou","speakerImg":"","roleName":"Lecturer"},{"dataId":8307,"title":"2013年青年医师研究者奖获奖者,2013 YIA Winner","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F2013%E5%B9%B4%E9%9D%92%E5%B9%B4%E5%8C%BB%E5%B8%88%E7%A0%94%E7%A9%B6%E8%80%85%E5%A5%96%E8%8E%B7%E5%A5%96%E8%80%85-%E5%8D%9C%E5%86%9B.mp4","meetingId":303230,"timeStart":{"date":22,"day":4,"hours":8,"minutes":39,"month":2,"nanos":0,"seconds":0,"time":1521679140000,"timezoneOffset":-480,"year":118},"author":"卜军","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524020942485.jpg","openTime":"2018-04-18 11:09上线","createTime":"2018-04-18 11:09:02","classesName":"","speakerName":"Jun Bu","speakerImg":"","roleName":"Lecturer"},{"dataId":8308,"title":"2015年青年医师研究者奖获奖者,2015 YIA Winner","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F2015%E5%B9%B4%E9%9D%92%E5%B9%B4%E5%8C%BB%E5%B8%88%E7%A0%94%E7%A9%B6%E8%80%85%E5%A5%96%E8%8E%B7%E5%A5%96%E8%80%85-%E5%BC%A0%E7%91%B6%E4%BF%8A.mp4","meetingId":303231,"timeStart":{"date":22,"day":4,"hours":8,"minutes":43,"month":2,"nanos":0,"seconds":0,"time":1521679380000,"timezoneOffset":-480,"year":118},"author":"张瑶俊","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524021044575.jpg","openTime":"2018-04-18 11:10上线","createTime":"2018-04-18 11:10:44","classesName":"","speakerName":"Yaojun Zhang","speakerImg":"","roleName":"Lecturer"},{"dataId":8309,"title":"2016年青年医师研究者奖获奖者,2016 YIA Winner","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F2016%E5%B9%B4%E9%9D%92%E5%B9%B4%E5%8C%BB%E5%B8%88%E7%A0%94%E7%A9%B6%E8%80%85%E5%A5%96%E8%8E%B7%E5%A5%96%E8%80%85-%E8%B4%BE%E6%B5%B7%E6%B3%A2.mp4","meetingId":303232,"timeStart":{"date":22,"day":4,"hours":8,"minutes":47,"month":2,"nanos":0,"seconds":0,"time":1521679620000,"timezoneOffset":-480,"year":118},"author":"贾海波","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524021148833.jpg","openTime":"2018-04-18 11:12上线","createTime":"2018-04-18 11:12:28","classesName":"","speakerName":"Haibo Jia","speakerImg":"","roleName":"Lecturer"},{"dataId":8310,"title":"2017年青年医师研究者奖获奖者,2017 YIA Winner","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F2017%E5%B9%B4%E9%9D%92%E5%B9%B4%E5%8C%BB%E5%B8%88%E7%A0%94%E7%A9%B6%E8%80%85%E5%A5%96%E8%8E%B7%E5%A5%96%E8%80%85-%E4%BD%99%E9%94%82%E9%95%AD.mp4","meetingId":303233,"timeStart":{"date":22,"day":4,"hours":8,"minutes":51,"month":2,"nanos":0,"seconds":0,"time":1521679860000,"timezoneOffset":-480,"year":118},"author":"余锂镭","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524021240887.jpg","openTime":"2018-04-18 11:14上线","createTime":"2018-04-18 11:14:00","classesName":"","speakerName":"Lilei Yu","speakerImg":"","roleName":"Lecturer"},{"dataId":8311,"title":"怎样开始和设立一个蓬勃发展的临床研究项目及成为一个关键的入选者：基本要素和平衡考量,How to Start and Set up a Thriving Clinical Research Program and Be a Key Enroller: The Basics and Balance","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F%E6%80%8E%E6%A0%B7%E5%BC%80%E5%A7%8B%E5%92%8C%E8%AE%BE%E7%AB%8B%E4%B8%80%E4%B8%AA%E8%93%AC%E5%8B%83%E5%8F%91%E5%B1%95%E7%9A%84%E4%B8%B4%E5%BA%8A%E7%A0%94%E7%A9%B6%E9%A1%B9%E7%9B%AE%E5%8F%8A%E6%88%90%E4%B8%BA%E4%B8%80%E4%B8%AA%E5%85%B3%E9%94%AE%E7%9A%84%E5%85%A5%E9%80%89%E8%80%85%EF%BC%9A%E5%9F%BA%E6%9C%AC%E8%A6%81%E7%B4%A0%E5%92%8C%E5%B9%B3%E8%A1%A1%E8%80%83%E9%87%8F-Sahil%20A.%20Parikh.mp4","meetingId":303235,"timeStart":{"date":22,"day":4,"hours":9,"minutes":5,"month":2,"nanos":0,"seconds":0,"time":1521680700000,"timezoneOffset":-480,"year":118},"author":"Sahil A. Parikh","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524022578322.jpg","openTime":"2018-04-18 11:36上线","createTime":"2018-04-18 11:36:18","classesName":"","speakerName":"Sahil A. Parikh","speakerImg":"","roleName":"Lecturer"},{"dataId":8312,"title":"特色演讲：CIT临床研究专题研讨峰会十年 - 我们已经走了多远？,Featured Lecture: 10 Years of Clinical Research Workshops at CIT - How Far We\u2019ve Come","videoUrl":"http://cit2018.bj.bcebos.com/A101-103%2F22%2F%E7%89%B9%E8%89%B2%E6%BC%94%E8%AE%B2%EF%BC%9ACIT%E4%B8%B4%E5%BA%8A%E7%A0%94%E7%A9%B6%E4%B8%93%E9%A2%98%E7%A0%94%E8%AE%A8%E5%B3%B0%E4%BC%9A%E5%8D%81%E5%B9%B4%20-%20%E6%88%91%E4%BB%AC%E5%B7%B2%E7%BB%8F%E8%B5%B0%E4%BA%86%E5%A4%9A%E8%BF%9C%EF%BC%9F-Kirtane%20Ajay%20J..mp4","meetingId":303237,"timeStart":{"date":22,"day":4,"hours":9,"minutes":30,"month":2,"nanos":0,"seconds":0,"time":1521682200000,"timezoneOffset":-480,"year":118},"author":"Kirtane Ajay J.","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-04-18_1524022748692.jpg","openTime":"2018-04-18 11:39上线","createTime":"2018-04-18 11:39:08","classesName":"","speakerName":"Ajay J. Kirtane","speakerImg":"","roleName":"Lecturer"}]
     * state : 1
     */

    private String state;
    private List<VideoArrayBean> videoArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<VideoArrayBean> getVideoArray() {
        return videoArray;
    }

    public void setVideoArray(List<VideoArrayBean> videoArray) {
        this.videoArray = videoArray;
    }

    public static class VideoArrayBean implements Serializable {
        /**
         * dataId : 8306
         * title : 2012年青年医师研究者奖获奖者,2012 YIA Winner
         * videoUrl : http://cit2018.bj.bcebos.com/A101-103%2F22%2F2012%E5%B9%B4%E9%9D%92%E5%B9%B4%E5%8C%BB%E5%B8%88%E7%A0%94%E7%A9%B6%E8%80%85%E5%A5%96%E8%8E%B7%E5%A5%96%E8%80%85-%E4%BE%AF%E9%9D%99%E6%B3%A2.mp4
         * meetingId : 303229
         * timeStart : {"date":22,"day":4,"hours":8,"minutes":35,"month":2,"nanos":0,"seconds":0,"time":1521678900000,"timezoneOffset":-480,"year":118}
         * author : 侯静波
         * videoImage : http://incongress.cn:8090/Exam/files/img/2018-04-18_1524020797994.jpg
         * openTime : 2018-04-18 11:06上线
         * createTime : 2018-04-18 11:06:37
         * classesName :
         * speakerName : Jingbo Hou
         * speakerImg :
         * roleName : Lecturer
         */

        private int dataId;
        private String title;
        private String videoUrl;
        private int meetingId;
        private TimeStartBean timeStart;
        private String author;
        private String videoImage;
        private int videoType;
        private String openTime;
        private String createTime;
        private String classesName;
        private String speakerName;
        private String speakerImg;
        private String roleName;

        public int getVideoType() {
            return videoType;
        }

        public void setVideoType(int videoType) {
            this.videoType = videoType;
        }

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

        public static class TimeStartBean implements Serializable{
            /**
             * date : 22
             * day : 4
             * hours : 8
             * minutes : 35
             * month : 2
             * nanos : 0
             * seconds : 0
             * time : 1521678900000
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
}
