package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2016/12/27.
 * 日程提问列表字段
 */

public class MyMeetingQuestion {

    /**
     * state1 : 1
     * qCount1 : 2
     * sceneShowArray1 : [{"sceneShowId":2079,"type":6,"timeShow":"4分钟前","meetingName":"病史汇报 Case Report","content":"%E8%BF%99%E4%B8%AA%E7%97%85%E5%8F%B2%E7%9A%84%E6%97%B6%E9%97%B4%E6%98%AF%E5%A4%9A%E5%B0%91%EF%BC%9F","isShow":0,"laudCount":0,"isLaud":0,"isHuiFu":0,"answerUserName":"饶歆","answerUserImg":"","answerContent":""},{"sceneShowId":2078,"type":6,"timeShow":"5分钟前","meetingName":"下腔静脉变异度评估容量反应性是否可靠？正    Is inferior vena cava variability reliable to evaluation of volume responsiveness? Pro","content":"%E4%BD%A0%E5%A5%BD%EF%BC%8C%E9%97%AE%E4%B8%AA%E9%97%AE%E9%A2%98","isShow":0,"laudCount":0,"isLaud":0,"isHuiFu":0,"answerUserName":"高心晶","answerUserImg":"","answerContent":""}]
     * state2 : 1
     * qCount2 : 3
     * sceneShowArray2 : [{"sceneShowId":2077,"type":6,"timeShow":"6分钟前","meetingName":"EDGT已经过时了吗？反     Is EDGT out of fashion?Con","content":"%E8%BF%99%E4%B8%AA%E7%9C%9F%E7%9A%84%E8%BF%87%E6%97%B6%E4%BA%86%E5%90%97%EF%BC%9F","isShow":0,"laudCount":0,"isLaud":0,"isHuiFu":0,"answerUserName":"唐建军","answerUserImg":"","answerContent":""},{"sceneShowId":2076,"type":6,"timeShow":"7分钟前","meetingName":"重症超声指导肠内营养   Critical ultrasound-guided assessment of enteral nutrition","content":"%E6%8D%A2%E4%B8%AA%E9%97%AE%E9%A2%98%E9%97%AE%E4%B8%80%E4%B8%8B","isShow":0,"laudCount":0,"isLaud":0,"isHuiFu":0,"answerUserName":"何伟","answerUserImg":"","answerContent":""},{"sceneShowId":2075,"type":6,"timeShow":"8分钟前","meetingName":"下腔静脉变异度评估容量反应性是否可靠？正    Is inferior vena cava variability reliable to evaluation of volume responsiveness? Pro","content":"%E6%82%A8%E5%A5%BD%EF%BC%8C%E6%9C%89%E4%B8%AA%E9%97%AE%E9%A2%98%E6%83%B3%E5%92%A8%E8%AF%A2%E4%B8%80%E4%B8%8B","isShow":0,"laudCount":0,"isLaud":0,"isHuiFu":0,"answerUserName":"高心晶","answerUserImg":"","answerContent":""}]
     */

    private int state1;
    private int qCount1;
    private int state2;
    private int qCount2;
    private List<SceneShowArray1Bean> sceneShowArray1;
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

    public List<SceneShowArray1Bean> getSceneShowArray1() {
        return sceneShowArray1;
    }

    public void setSceneShowArray1(List<SceneShowArray1Bean> sceneShowArray1) {
        this.sceneShowArray1 = sceneShowArray1;
    }

    public List<SceneShowArray2Bean> getSceneShowArray2() {
        return sceneShowArray2;
    }

    public void setSceneShowArray2(List<SceneShowArray2Bean> sceneShowArray2) {
        this.sceneShowArray2 = sceneShowArray2;
    }

    public static class SceneShowArray1Bean {
        /**
         * sceneShowId : 2079
         * type : 6
         * timeShow : 4分钟前
         * meetingName : 病史汇报 Case Report
         * content : %E8%BF%99%E4%B8%AA%E7%97%85%E5%8F%B2%E7%9A%84%E6%97%B6%E9%97%B4%E6%98%AF%E5%A4%9A%E5%B0%91%EF%BC%9F
         * isShow : 0
         * laudCount : 0
         * isLaud : 0
         * isHuiFu : 0
         * answerUserName : 饶歆
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

    public static class SceneShowArray2Bean {
        /**
         * sceneShowId : 2077
         * type : 6
         * timeShow : 6分钟前
         * meetingName : EDGT已经过时了吗？反     Is EDGT out of fashion?Con
         * content : %E8%BF%99%E4%B8%AA%E7%9C%9F%E7%9A%84%E8%BF%87%E6%97%B6%E4%BA%86%E5%90%97%EF%BC%9F
         * isShow : 0
         * laudCount : 0
         * isLaud : 0
         * isHuiFu : 0
         * answerUserName : 唐建军
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
