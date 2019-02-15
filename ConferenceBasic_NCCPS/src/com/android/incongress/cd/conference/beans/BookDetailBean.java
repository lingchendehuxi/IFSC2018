package com.android.incongress.cd.conference.beans;

import java.util.List;

public class BookDetailBean {

    /**
     * meetingArray : [{"meetingId":"382080","topic":"如果你是术者 - 术前病例讨论#@#If You Were the Operator - Today\u2019s Case Preview","timeStart":"2019-03-28 13:30:00.0","facultyId":"0","yuyue":"0"},{"meetingId":"382081","topic":"示教演示1：复旦大学中山医院（慢性完全闭塞介入治疗）#@#Live Case 1: Fudan University Zhongshan Hospital (CTO PCI)","timeStart":"2019-03-28 13:45:00.0","facultyId":"75","yuyue":"0"},{"meetingId":"382082","topic":"慢性完全闭塞逆向介入治疗中国现状#@#Current Status of Retrograde CTO PCI in China","timeStart":"2019-03-28 14:30:00.0","facultyId":"633","yuyue":"0"},{"meetingId":"382083","topic":"讨论#@#Discussion","timeStart":"2019-03-28 14:45:00.0","facultyId":"0","yuyue":"0"},{"meetingId":"382084","topic":"使用APCTO公式指导完成最难忘的慢性完全闭塞病例#@#My Most Memorable CTO Case Under the Guidance of APCTO Algorithm","timeStart":"2019-03-28 14:50:00.0","facultyId":"865","yuyue":"0"},{"meetingId":"382085","topic":"讨论#@#Discussion","timeStart":"2019-03-28 15:00:00.0","facultyId":"0","yuyue":"0"}]
     * state : 1
     */

    private String state;
    private List<MeetingArrayBean> meetingArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<MeetingArrayBean> getMeetingArray() {
        return meetingArray;
    }

    public void setMeetingArray(List<MeetingArrayBean> meetingArray) {
        this.meetingArray = meetingArray;
    }

    public static class MeetingArrayBean {
        /**
         * meetingId : 382080
         * topic : 如果你是术者 - 术前病例讨论#@#If You Were the Operator - Today’s Case Preview
         * timeStart : 2019-03-28 13:30:00.0
         * facultyId : 0
         * yuyue : 0
         */

        private String meetingId;
        private String topic;
        private String timeStart;
        private String facultyId;
        private String yuyue;

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }

        public String getFacultyId() {
            return facultyId;
        }

        public void setFacultyId(String facultyId) {
            this.facultyId = facultyId;
        }

        public String getYuyue() {
            return yuyue;
        }

        public void setYuyue(String yuyue) {
            this.yuyue = yuyue;
        }
    }
}
