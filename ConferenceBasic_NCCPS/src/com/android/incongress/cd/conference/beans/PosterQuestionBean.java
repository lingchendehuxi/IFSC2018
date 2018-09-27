package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2017/1/4.
 */

public class PosterQuestionBean {

    /**
     * sceneShowArray : [{"answerUserImg":"","content":"%E8%BF%99%E6%98%AF%E6%88%91%E7%9A%84%E9%97%AE%E9%A2%98","sceneShowId":110,"timeShow":"38分钟前","answerUserName":"邱妙珍","type":7,"posterTitle":"Evaluation of MET and HER2 expression in primary and metastatic tumor in Chinese advanced gastric cancer patients"}]
     * qCount : 1
     * state : 1
     * pageState : 0
     */

    private int qCount;
    private int state;
    private int pageState;
    private List<SceneShowArrayBean> sceneShowArray;

    public int getQCount() {
        return qCount;
    }

    public void setQCount(int qCount) {
        this.qCount = qCount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPageState() {
        return pageState;
    }

    public void setPageState(int pageState) {
        this.pageState = pageState;
    }

    public List<SceneShowArrayBean> getSceneShowArray() {
        return sceneShowArray;
    }

    public void setSceneShowArray(List<SceneShowArrayBean> sceneShowArray) {
        this.sceneShowArray = sceneShowArray;
    }

    public static class SceneShowArrayBean {

        /**
         * answerUserImg :
         * content : %E8%BF%99%E6%98%AF%E6%88%91%E7%9A%84%E9%97%AE%E9%A2%98
         * sceneShowId : 110
         * timeShow : 38分钟前
         * answerUserName : 邱妙珍
         * type : 7
         * posterTitle : Evaluation of MET and HER2 expression in primary and metastatic tumor in Chinese advanced gastric cancer patients
         */

        private String answerUserImg;
        private String content;
        private int sceneShowId;
        private String timeShow;
        private String answerUserName;
        private int type;
        private String posterTitle;
        private int posterId;

        public int getPosterId() {
            return posterId;
        }

        public void setPosterId(int posterId) {
            this.posterId = posterId;
        }

        public String getAnswerUserImg() {
            return answerUserImg;
        }

        public void setAnswerUserImg(String answerUserImg) {
            this.answerUserImg = answerUserImg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSceneShowId() {
            return sceneShowId;
        }

        public void setSceneShowId(int sceneShowId) {
            this.sceneShowId = sceneShowId;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPosterTitle() {
            return posterTitle;
        }

        public void setPosterTitle(String posterTitle) {
            this.posterTitle = posterTitle;
        }
    }
}
