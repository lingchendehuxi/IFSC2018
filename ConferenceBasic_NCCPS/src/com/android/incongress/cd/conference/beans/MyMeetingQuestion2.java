package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Admin on 2017/5/19.
 */

public class MyMeetingQuestion2 {

    private int qCount2;
    private int state2;
    //private int pageState;
    private List<SceneShowArrayBean> sceneShowArray2;
    public int getQCount() {
        return qCount2;
    }

    public void setQCount(int qCount) {
        this.qCount2 = qCount;
    }

    public int getState() {
        return state2;
    }

    public void setState(int state) {
        this.state2 = state;
    }

    /*public int getPageState() {
        return pageState;
    }

    public void setPageState(int pageState) {
        this.pageState = pageState;
    }*/

    public List<SceneShowArrayBean> getSceneShowArray() {
        return sceneShowArray2;
    }

    public void setSceneShowArray(List<SceneShowArrayBean> sceneShowArray) {
        this.sceneShowArray2 = sceneShowArray;
    }


    public static class SceneShowArrayBean {
        /**
         * content : I am a question
         * answerContent :
         * laudCount : 0
         * isLaud : 0
         * timeShow : 3小时前
         * answerUserName : 韩宝惠
         * meetingName :
         * answerUserImg :
         * sceneShowId : 92
         * isHuiFu : 0
         * type : 6
         * isShow : 1
         */

        private String content;
        private String answerContent;
        private int laudCount;
        private int isLaud;
        private String timeShow;
        private String answerUserName;
        private String meetingName;
        private String answerUserImg;
        private int sceneShowId;
        private int isHuiFu;
        private int type;
        private int isShow;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public int getLaudCount() {
            return laudCount;
        }

        public void setLaudCount(int laudCount) {
            this.laudCount = laudCount;
        }

        public int getIsLaud() {
            return isLaud;
        }

        public void setIsLaud(int isLaud) {
            this.isLaud = isLaud;
        }

        public String getTimeShow() {
            return timeShow;
        }

        public void setTimeShow(String timeShow) {
            this.timeShow = timeShow;
        }

        public String getAnswerUserName() {
            return answerUserName;
        }

        public void setAnswerUserName(String answerUserName) {
            this.answerUserName = answerUserName;
        }

        public String getMeetingName() {
            return meetingName;
        }

        public void setMeetingName(String meetingName) {
            this.meetingName = meetingName;
        }

        public String getAnswerUserImg() {
            return answerUserImg;
        }

        public void setAnswerUserImg(String answerUserImg) {
            this.answerUserImg = answerUserImg;
        }

        public int getSceneShowId() {
            return sceneShowId;
        }

        public void setSceneShowId(int sceneShowId) {
            this.sceneShowId = sceneShowId;
        }

        public int getIsHuiFu() {
            return isHuiFu;
        }

        public void setIsHuiFu(int isHuiFu) {
            this.isHuiFu = isHuiFu;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }
}
