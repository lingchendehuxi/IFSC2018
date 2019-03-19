package com.android.incongress.cd.conference.beans;

import com.android.incongress.cd.conference.utils.StringUtils;

import java.util.List;

public class SatelliteInfoListBean {

    private int resultState;
    private List<ResultBean> result;

    public int getResultState() {
        return resultState;
    }

    public void setResultState(int resultState) {
        this.resultState = resultState;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 1
         * title : 雅培
         * content : 直播
         * img : http://app.incongress.cn/images/Upload/sceneShowLogo_2019-01-09_1547025169958.jpg
         * day : 2019-03-29
         * startTime : 17:15
         * endTime : 18:15
         * location : 一层多功能B厅
         * type : 4"id": 1,
         * 		"title": "雅培",
         * 		"islive": 0,
         * 		"liveUrl": "",
         * 		"content": "直播",
         * 		"img": "http:\/\/app.incongress.cn\/images\/Upload\/sceneShowLogo_2019-01-09_1547025169958.jpg",
         * 		"day": "2019-03-29",
         * 		"startTime": "17:15",
         * 		"endTime": "18:15",
         * 		"location": "一层多功能B厅",
         * 		"type": 4
         */

        private int id;
        private String title;
        private String content;
        private String img;
        private String day;
        private String startTime;
        private String endTime;
        private String location;
        private int type;
        private int islive;
        private String liveUrl;

        public int getIslive() {
            return islive;
        }

        public void setIslive(int islive) {
            this.islive = islive;
        }

        public String getLiveUrl() {
            return liveUrl;
        }

        public void setLiveUrl(String liveUrl) {
            this.liveUrl = liveUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
