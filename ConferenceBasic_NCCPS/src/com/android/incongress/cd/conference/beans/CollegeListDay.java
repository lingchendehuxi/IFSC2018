package com.android.incongress.cd.conference.beans;

import java.util.List;

public class CollegeListDay {
    /**
     * dateArray : [{"date":"30��","timeStart":"2017-03-30"},{"date":"31��","timeStart":"2017-03-31"},{"date":"01��","timeStart":"2017-04-01"},{"date":"02��","timeStart":"2017-04-02"}]
     * state : 1
     */

    private String state;
    private List<DateArrayBean> dateArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<DateArrayBean> getDateArray() {
        return dateArray;
    }

    public void setDateArray(List<DateArrayBean> dateArray) {
        this.dateArray = dateArray;
    }

    public static class DateArrayBean {
        /**
         * date : 30��
         * timeStart : 2017-03-30
         */

        private String date;
        private String timeStart;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTimeStart() {
            return timeStart;
        }

        public void setTimeStart(String timeStart) {
            this.timeStart = timeStart;
        }
    }
}
