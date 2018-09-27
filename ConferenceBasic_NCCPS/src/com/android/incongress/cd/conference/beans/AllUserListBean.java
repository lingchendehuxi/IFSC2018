package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2016/10/18.
 */

public class AllUserListBean {

    /**
     * state : 1
     * message : success
     * jsonArray : [{"state":0,"userInfoId":17312,"rongUserId":"csco17312","userName":"Qi Ma","imgUrl":"http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png"},{"state":2,"userInfoId":17476,"rongUserId":"csco17476","userName":"刘静","imgUrl":"http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png"},{"state":3,"userInfoId":17477,"rongUserId":"csco17477","userName":"程先平","imgUrl":"http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png"}]
     */

    private int state;
    private String message;
    /**
     * state : 0
     * userInfoId : 17312
     * rongUserId : csco17312
     * userName : Qi Ma
     * imgUrl : http://app.incongress.cn:8082/Conferences/images/NewSpeakerDetail_avatardefault@3x.png
     */

    private List<JsonArrayBean> jsonArray;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<JsonArrayBean> getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(List<JsonArrayBean> jsonArray) {
        this.jsonArray = jsonArray;
    }

    public static class JsonArrayBean {
        private int state;
        private int userInfoId;
        private String rongUserId;
        private String userName;
        private String imgUrl;
        private String pinyin;
        private String firstLetter;
        private int friendsUserType;

        public int getFriendsUserType() {
            return friendsUserType;
        }

        public void setFriendsUserType(int friendsUserType) {
            this.friendsUserType = friendsUserType;
        }

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(int userInfoId) {
            this.userInfoId = userInfoId;
        }

        public String getRongUserId() {
            return rongUserId;
        }

        public void setRongUserId(String rongUserId) {
            this.rongUserId = rongUserId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
