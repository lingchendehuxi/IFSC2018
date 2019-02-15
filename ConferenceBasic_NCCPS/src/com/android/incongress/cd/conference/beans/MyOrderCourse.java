package com.android.incongress.cd.conference.beans;

import java.util.List;

public class MyOrderCourse {
    /**
     * array : [{"monthDay":"03月28日","time":"13:45-14:30","topic":"示教演示1：复旦大学中山医院（慢性完全闭塞介入治疗）#@#Live Case 1: Fudan University Zhongshan Hospital (CTO PCI)","userReservationMeetingId":72845},{"monthDay":"03月28日","time":"13:30-13:45","topic":"如果你是术者 - 术前病例讨论#@#If You Were the Operator - Today\u2019s Case Preview","userReservationMeetingId":72846}]
     * state : 1
     */

    private String state;
    private List<ArrayBean> array;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ArrayBean> getArray() {
        return array;
    }

    public void setArray(List<ArrayBean> array) {
        this.array = array;
    }

    public static class ArrayBean {
        /**
         * monthDay : 03月28日
         * time : 13:45-14:30
         * topic : 示教演示1：复旦大学中山医院（慢性完全闭塞介入治疗）#@#Live Case 1: Fudan University Zhongshan Hospital (CTO PCI)
         * userReservationMeetingId : 72845
         */

        private String monthDay;
        private String time;
        private String topic;
        private int userReservationMeetingId;
        private int type;
        private boolean isShow;

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMonthDay() {
            return monthDay;
        }

        public void setMonthDay(String monthDay) {
            this.monthDay = monthDay;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public int getUserReservationMeetingId() {
            return userReservationMeetingId;
        }

        public void setUserReservationMeetingId(int userReservationMeetingId) {
            this.userReservationMeetingId = userReservationMeetingId;
        }
    }
}
