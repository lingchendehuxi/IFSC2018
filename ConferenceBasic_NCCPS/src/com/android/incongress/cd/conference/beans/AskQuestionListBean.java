package com.android.incongress.cd.conference.beans;

import java.util.List;

public class AskQuestionListBean {


    private int state1;
    private int qCount1;
    private int state2;
    private int qCount2;
    private List<?> sceneShowArray1;
    private List<SceneShowArray2Bean> sceneShowArray2;

    public int getState1() {
        return state1;
    }

    public void setState1(int state1) {
        this.state1 = state1;
    }

    public int getQCount1() {
        return qCount1;
    }

    public void setQCount1(int qCount1) {
        this.qCount1 = qCount1;
    }

    public int getState2() {
        return state2;
    }

    public void setState2(int state2) {
        this.state2 = state2;
    }

    public int getQCount2() {
        return qCount2;
    }

    public void setQCount2(int qCount2) {
        this.qCount2 = qCount2;
    }

    public List<?> getSceneShowArray1() {
        return sceneShowArray1;
    }

    public void setSceneShowArray1(List<?> sceneShowArray1) {
        this.sceneShowArray1 = sceneShowArray1;
    }

    public List<SceneShowArray2Bean> getSceneShowArray2() {
        return sceneShowArray2;
    }

    public void setSceneShowArray2(List<SceneShowArray2Bean> sceneShowArray2) {
        this.sceneShowArray2 = sceneShowArray2;
    }

    public static class SceneShowArray2Bean {
        /**
         * sceneShowId : 4510
         * type : 6
         * timeShow : 2018-11-16 07:22
         * meetingName : 肺炎支原体肺炎儿童上下呼吸道菌群一致性研究
         * content : %E4%B8%8B%E5%91%BC%E5%90%B8%E9%81%93%E8%8F%8C%E7%BE%A4%E6%9C%89%E5%93%AA%E4%BA%9B%E7%A7%8D%E7%B1%BB
         * isShow : 0
         * laudCount : 0
         * isLaud : 0
         * isHuiFu : 0
         * answerUserName : 王和平
         * answerUserImg :
         * answerContent :
         */

        private int sceneShowId;
        private int type;
        private String timeShow;
        private String meetingName;
        private String content;
        private int isShow;
        private int laudCount;
        private int isLaud;
        private int isHuiFu;
        private String answerUserName;
        private String answerUserImg;
        private String answerContent;

        public int getSceneShowId() {
            return sceneShowId;
        }

        public void setSceneShowId(int sceneShowId) {
            this.sceneShowId = sceneShowId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTimeShow() {
            return timeShow;
        }

        public void setTimeShow(String timeShow) {
            this.timeShow = timeShow;
        }

        public String getMeetingName() {
            return meetingName;
        }

        public void setMeetingName(String meetingName) {
            this.meetingName = meetingName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
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

        public int getIsHuiFu() {
            return isHuiFu;
        }

        public void setIsHuiFu(int isHuiFu) {
            this.isHuiFu = isHuiFu;
        }

        public String getAnswerUserName() {
            return answerUserName;
        }

        public void setAnswerUserName(String answerUserName) {
            this.answerUserName = answerUserName;
        }

        public String getAnswerUserImg() {
            return answerUserImg;
        }

        public void setAnswerUserImg(String answerUserImg) {
            this.answerUserImg = answerUserImg;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }
    }
}
