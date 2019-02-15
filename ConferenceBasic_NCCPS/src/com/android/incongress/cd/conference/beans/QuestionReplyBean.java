package com.android.incongress.cd.conference.beans;

import java.util.List;

public class QuestionReplyBean {

    /**
     * sceneShowArray : [{"sceneShowId":3914,"content":"%E6%B5%8B%E8%AF%95","meetingName":"冠脉分叉病变介入治疗中怎样合理对吻球囊扩张？","speakerName":"李丽","imgUrl":""}]
     * state : 1
     */

    private String state;
    private List<SceneShowArrayBean> sceneShowArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<SceneShowArrayBean> getSceneShowArray() {
        return sceneShowArray;
    }

    public void setSceneShowArray(List<SceneShowArrayBean> sceneShowArray) {
        this.sceneShowArray = sceneShowArray;
    }

    public static class SceneShowArrayBean {
        /**
         * sceneShowId : 3914
         * content : %E6%B5%8B%E8%AF%95
         * meetingName : 冠脉分叉病变介入治疗中怎样合理对吻球囊扩张？
         * speakerName : 李丽
         * imgUrl :
         */

        private int sceneShowId;
        private String content;
        private String meetingName;
        private String speakerName;
        private String imgUrl;

        public int getSceneShowId() {
            return sceneShowId;
        }

        public void setSceneShowId(int sceneShowId) {
            this.sceneShowId = sceneShowId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMeetingName() {
            return meetingName;
        }

        public void setMeetingName(String meetingName) {
            this.meetingName = meetingName;
        }

        public String getSpeakerName() {
            return speakerName;
        }

        public void setSpeakerName(String speakerName) {
            this.speakerName = speakerName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
