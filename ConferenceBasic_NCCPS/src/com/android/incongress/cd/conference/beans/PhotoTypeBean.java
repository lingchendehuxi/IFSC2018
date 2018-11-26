package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2017/1/20.
 */

public class PhotoTypeBean {

    /**
     * photoWallTypeArray : [{"typeName":"Rom1","typeId":1},{"typeName":"Rom9","typeId":9},{"typeName":"Rom8","typeId":8},{"typeName":"Rom7","typeId":7},{"typeName":"Rom6","typeId":6},{"typeName":"Rom5","typeId":5},{"typeName":"Rom4","typeId":4},{"typeName":"Rom3","typeId":3},{"typeName":"Rom2","typeId":2},{"typeName":"Rom10","typeId":10}]
     * state : 1
     * msg :
     */

    private int state;
    private String msg;
    private List<PhotoWallTypeArrayBean> photoWallTypeArray;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PhotoWallTypeArrayBean> getPhotoWallTypeArray() {
        return photoWallTypeArray;
    }

    public void setPhotoWallTypeArray(List<PhotoWallTypeArrayBean> photoWallTypeArray) {
        this.photoWallTypeArray = photoWallTypeArray;
    }

    public static class PhotoWallTypeArrayBean {
        /**
         * typeName : Rom1
         * typeId : 1
         */

        private String typeName;
        private int typeId;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }
    }
}
