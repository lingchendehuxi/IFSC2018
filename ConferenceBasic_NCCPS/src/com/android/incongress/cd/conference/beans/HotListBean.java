package com.android.incongress.cd.conference.beans;

import java.util.List;

public class HotListBean {

    /**
     * resultState : 0
     * result : [{"id":4579,"title":"关于CIT，美敦力这次很用心（000）","img":"http://incongress.cn:8090/images/Upload/sceneShowLogo_2019-03-11_1552297325302.png","introduction":"%%%%%%%    @@@@@"},{"id":4577,"title":"                                            rr                                                                      ","img":"http://incongress.cn:8090/images/Upload/sceneShowLogo_2019-03-11_1552295888156.png","introduction":"shsh1iujiwjdjsdksncks"},{"id":4576,"title":"11%%","img":"http://incongress.cn:8090/images/Upload/sceneShowLogo_2019-03-11_1552295200793.png","introduction":"AAAAAAAAAA"},{"id":4534,"title":"CIT","img":"","introduction":" "}]
     */

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
         * id : 4579
         * title : 关于CIT，美敦力这次很用心（000）
         * img : http://incongress.cn:8090/images/Upload/sceneShowLogo_2019-03-11_1552297325302.png
         * introduction : %%%%%%%    @@@@@"id": 4,
         * 		"title": "微创",
         * 		"islive": 0,
         * 		"liveUrl": "",
         * 		"content": "直播",
         * 		"img": "http:\/\/app.incongress.cn\/images\/Upload\/sceneShowLogo_2019-01-09_1547025169958.jpg",
         * 		"day": "2019-03-30",
         * 		"startTime": "17:15",
         * 		"endTime": "18:15",
         * 		"location": "一层多功能B厅",
         * 		"type": 4
         */

        private int id;
        private String title;
        private String img;
        private String introduction;
        private String url;
        private int islive;
        private String liveUrl;
        private String content;
        private String day;
        private String startTime;
        private String endTime;
        private String location;
        private int type;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
