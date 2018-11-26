package com.android.incongress.cd.conference.fragments.photo_album;

import java.util.List;

/**
 * Created by Jacky on 2017/1/20.
 */

public class PhotoAlbumDetailBean {

    /**
     * state : 1
     * pageState : 0
     * photoWallArray : [{"photoWallId":3,"laudCount":0,"imageUrl":"https://app.incongress.cn//Conferences/files/photoWall/231/speaker_1740642017-01-20_1484899095143.jpg","isLaud":0},{"photoWallId":5,"laudCount":0,"imageUrl":"https://app.incongress.cn//Conferences/files/photoWall/231/speaker_1740642017-01-20_1484899452103.jpg","isLaud":0}]
     */

    private int state;
    private int pageState;
    private List<PhotoWallArrayBean> photoWallArray;

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

    public List<PhotoWallArrayBean> getPhotoWallArray() {
        return photoWallArray;
    }

    public void setPhotoWallArray(List<PhotoWallArrayBean> photoWallArray) {
        this.photoWallArray = photoWallArray;
    }

    public static class PhotoWallArrayBean {
        /**
         * photoWallId : 3
         * laudCount : 0
         * imageUrl : https://app.incongress.cn//Conferences/files/photoWall/231/speaker_1740642017-01-20_1484899095143.jpg
         * isLaud : 0
         */

        private int photoWallId;
        private int laudCount;
        private String imageUrl;
        private int isLaud;

        public int getPhotoWallId() {
            return photoWallId;
        }

        public void setPhotoWallId(int photoWallId) {
            this.photoWallId = photoWallId;
        }

        public int getLaudCount() {
            return laudCount;
        }

        public void setLaudCount(int laudCount) {
            this.laudCount = laudCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getIsLaud() {
            return isLaud;
        }

        public void setIsLaud(int isLaud) {
            this.isLaud = isLaud;
        }
    }
}
