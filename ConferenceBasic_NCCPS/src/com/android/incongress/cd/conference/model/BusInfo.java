package com.android.incongress.cd.conference.model;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Created by qichen on 2017/5/20.
 */

public class BusInfo extends LitePalSupport {

    /**
     * dateArray : [{"busDate":"2017-05-25","busArray":[{"busInfoId":3,"busDate":"2017-05-25","busTime":"10:00","backTime":"22:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":4,"busDate":"2017-05-25","busTime":"12:00","backTime":"12:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":5,"busDate":"2017-05-25","busTime":"14:00","backTime":"14:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":6,"busDate":"2017-05-25","busTime":"15:00","backTime":"15:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":7,"busDate":"2017-05-25","busTime":"17:00","backTime":"17:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1}]},{"busDate":"2017-05-26","busArray":[{"busInfoId":8,"busDate":"2017-05-26","busTime":"7:40","backTime":"18:30","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":9,"busDate":"2017-05-26","busTime":"9:00","backTime":"9:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":10,"busDate":"2017-05-26","busTime":"11:00","backTime":"11:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":11,"busDate":"2017-05-26","busTime":"12:30","backTime":"12:30","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":12,"busDate":"2017-05-26","busTime":"14:00","backTime":"14:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":13,"busDate":"2017-05-26","busTime":"16:00","backTime":"16:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":24,"busDate":"2017-05-26","busTime":"7:30","backTime":"18:30","busFrom":"豪享来温德姆至尊酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":25,"busDate":"2017-05-26","busTime":"7:20","backTime":"18:30","busFrom":"威斯汀酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":26,"busDate":"2017-05-26","busTime":"7:20","backTime":"18:30","busFrom":"盛美利亚酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":27,"busDate":"2017-05-26","busTime":"7:30","backTime":"18:30","busFrom":"天宇菲尔德酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":28,"busDate":"2017-05-26","busTime":"7:10","backTime":"18:30","busFrom":"曲江国际饭店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":29,"busDate":"2017-05-26","busTime":"7:20","backTime":"18:30","busFrom":"大雁塔假日酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":30,"busDate":"2017-05-26","busTime":"7:10","backTime":"18:30","busFrom":"绿地假日酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":31,"busDate":"2017-05-26","busTime":"7:10","backTime":"18:30","busFrom":"吉源国际酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":32,"busDate":"2017-05-26","busTime":"7:20","backTime":"18:30","busFrom":"维也纳国际酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":33,"busDate":"2017-05-26","busTime":"7:30","backTime":"18:30","busFrom":"骐美商务酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":34,"busDate":"2017-05-26","busTime":"7:40","backTime":"18:30","busFrom":"曲江惠宾苑宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":35,"busDate":"2017-05-26","busTime":"7:40","backTime":"18:30","busFrom":"曲江宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":36,"busDate":"2017-05-26","busTime":"7:10","backTime":"18:30","busFrom":"陕西宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":37,"busDate":"2017-05-26","busTime":"7:40","backTime":"18:30","busFrom":"长安雅集酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":38,"busDate":"2017-05-26","busTime":"7:40","backTime":"18:30","busFrom":"金日鸿孚商务酒店","busTo":"曲江国际会议中心","isVip":0}]},{"busDate":"2017-05-27","busArray":[{"busInfoId":14,"busDate":"2017-05-27","busTime":"7:40","backTime":"18:30","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":15,"busDate":"2017-05-27","busTime":"9:00","backTime":"9:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":16,"busDate":"2017-05-27","busTime":"11:00","backTime":"11:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":17,"busDate":"2017-05-27","busTime":"12:30","backTime":"12:30","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":18,"busDate":"2017-05-27","busTime":"14:00","backTime":"14:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":19,"busDate":"2017-05-27","busTime":"16:00","backTime":"16:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":39,"busDate":"2017-05-27","busTime":"7:30","backTime":"18:30","busFrom":"豪享来温德姆至尊酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":40,"busDate":"2017-05-27","busTime":"7:20","backTime":"18:30","busFrom":"威斯汀酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":41,"busDate":"2017-05-27","busTime":"7:20","backTime":"18:30","busFrom":"盛美利亚酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":42,"busDate":"2017-05-27","busTime":"7:30","backTime":"18:30","busFrom":"天宇菲尔德酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":43,"busDate":"2017-05-27","busTime":"7:10","backTime":"18:30","busFrom":"曲江国际饭店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":44,"busDate":"2017-05-27","busTime":"7:20","backTime":"18:30","busFrom":"大雁塔假日酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":45,"busDate":"2017-05-27","busTime":"7:10","backTime":"18:30","busFrom":"绿地假日酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":46,"busDate":"2017-05-27","busTime":"7:10","backTime":"18:30","busFrom":"吉源国际酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":47,"busDate":"2017-05-27","busTime":"7:20","backTime":"18:30","busFrom":"维也纳国际酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":48,"busDate":"2017-05-27","busTime":"7:30","backTime":"18:30","busFrom":"骐美商务酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":49,"busDate":"2017-05-27","busTime":"7:40","backTime":"18:30","busFrom":"曲江惠宾苑宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":50,"busDate":"2017-05-27","busTime":"7:40","backTime":"18:30","busFrom":"曲江宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":51,"busDate":"2017-05-27","busTime":"7:10","backTime":"18:30","busFrom":"陕西宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":52,"busDate":"2017-05-27","busTime":"7:40","backTime":"18:30","busFrom":"长安雅集酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":53,"busDate":"2017-05-27","busTime":"7:40","backTime":"18:30","busFrom":"金日鸿孚商务酒店","busTo":"曲江国际会议中心","isVip":0}]},{"busDate":"2017-05-28","busArray":[{"busInfoId":20,"busDate":"2017-05-28","busTime":"7:40","backTime":"13:30","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":21,"busDate":"2017-05-28","busTime":"9:00","backTime":"9:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":22,"busDate":"2017-05-28","busTime":"10:00","backTime":"10:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":23,"busDate":"2017-05-28","busTime":"11:00","backTime":"11:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":54,"busDate":"2017-05-28","busTime":"7:30","backTime":"13:15","busFrom":"豪享来温德姆至尊酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":55,"busDate":"2017-05-28","busTime":"7:20","backTime":"13:15","busFrom":"威斯汀酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":56,"busDate":"2017-05-28","busTime":"7:20","backTime":"13:15","busFrom":"盛美利亚酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":57,"busDate":"2017-05-28","busTime":"7:30","backTime":"13:15","busFrom":"天宇菲尔德酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":58,"busDate":"2017-05-28","busTime":"7:10","backTime":"13:15","busFrom":"曲江国际饭店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":59,"busDate":"2017-05-28","busTime":"7:20","backTime":"13:15","busFrom":"大雁塔假日酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":60,"busDate":"2017-05-28","busTime":"7:10","backTime":"13:15","busFrom":"绿地假日酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":61,"busDate":"2017-05-28","busTime":"7:10","backTime":"13:15","busFrom":"吉源国际酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":62,"busDate":"2017-05-28","busTime":"7:20","backTime":"13:15","busFrom":"维也纳国际酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":63,"busDate":"2017-05-28","busTime":"7:30","backTime":"13:15","busFrom":"骐美商务酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":64,"busDate":"2017-05-28","busTime":"7:40","backTime":"13:15","busFrom":"曲江惠宾苑宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":65,"busDate":"2017-05-28","busTime":"7:40","backTime":"13:15","busFrom":"曲江宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":66,"busDate":"2017-05-28","busTime":"7:10","backTime":"13:15","busFrom":"陕西宾馆","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":67,"busDate":"2017-05-28","busTime":"7:40","backTime":"13:15","busFrom":"长安雅集酒店","busTo":"曲江国际会议中心","isVip":0},{"busInfoId":68,"busDate":"2017-05-28","busTime":"7:40","backTime":"13:15","busFrom":"金日鸿孚商务酒店","busTo":"曲江国际会议中心","isVip":0}]}]
     * state : 1
     */

    private int state;
    private List<DateArrayBean> dateArray;

    public int getState() {
        return state;
    }

    public void setState(int state) {
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
         * busDate : 2017-05-25
         * busArray : [{"busInfoId":3,"busDate":"2017-05-25","busTime":"10:00","backTime":"22:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":4,"busDate":"2017-05-25","busTime":"12:00","backTime":"12:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":5,"busDate":"2017-05-25","busTime":"14:00","backTime":"14:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":6,"busDate":"2017-05-25","busTime":"15:00","backTime":"15:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1},{"busInfoId":7,"busDate":"2017-05-25","busTime":"17:00","backTime":"17:00","busFrom":"曲江银座酒店","busTo":"曲江国际会议中心","isVip":1}]
         */

        private String busDate;
        private List<BusArrayBean> busArray;

        public String getBusDate() {
            return busDate;
        }

        public void setBusDate(String busDate) {
            this.busDate = busDate;
        }

        public List<BusArrayBean> getBusArray() {
            return busArray;
        }

        public void setBusArray(List<BusArrayBean> busArray) {
            this.busArray = busArray;
        }

        public static class BusArrayBean {
            /**
             * busInfoId : 3
             * busDate : 2017-05-25
             * busTime : 10:00
             * backTime : 22:00
             * busFrom : 曲江银座酒店
             * busTo : 曲江国际会议中心
             * isVip : 1
             */

            private int busInfoId;
            private String busDate;
            private String busTime;
            private String backTime;
            private String busFrom;
            private String busTo;
            private int isVip;
            private boolean isStartNotify;
            private boolean isEndNotify;

            public boolean isStartNotify() {
                return isStartNotify;
            }

            public void setStartNotify(boolean startNotify) {
                isStartNotify = startNotify;
            }

            public boolean isEndNotify() {
                return isEndNotify;
            }

            public void setEndNotify(boolean endNotify) {
                isEndNotify = endNotify;
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
    }
}
