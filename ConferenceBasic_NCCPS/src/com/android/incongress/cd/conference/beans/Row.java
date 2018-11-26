package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Jacky on 2016/5/13.
 */
public class Row {

    /**
     * row : 1
     * obj : [{"width":"33","iconName":"看日程","iconColor":"#222e73","iconCode":"1","iconUrl":"","newModel":""},{"width":"33","iconName":"查日程","iconColor":"#00a63b","iconCode":"2","iconUrl":"","newModel":""},{"width":"33","iconName":"我的日程","iconColor":"#222e73","iconCode":"3","iconUrl":"","newModel":""}]
     */

    private List<RowsBean> rows;

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private int row;
        /**
         * width : 33
         * iconName : 看日程
         * iconColor : #222e73
         * iconCode : 1
         * iconUrl :
         * newModel :
         */

        private List<ObjBean> obj;

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public List<ObjBean> getObj() {
            return obj;
        }

        public void setObj(List<ObjBean> obj) {
            this.obj = obj;
        }

        public static class ObjBean {
            private String width;
            private String iconName;//1 背景图 撑满 2 前景图
            private String iconEnName;
            private String iconColor;
            private String iconFontColor;
            private String iconCode;

            private String iconUrl;
            private String newModel; //model 类型16的跳转链接
            private String newUrl;//model 类型17的跳转链接
            private String imgSize;

            public String getIconFontColor() {
                return iconFontColor;
            }

            public void setIconFontColor(String iconFontColor) {
                this.iconFontColor = iconFontColor;
            }

            public String getNewUrl() {
                return newUrl;
            }

            public void setNewUrl(String newUrl) {
                this.newUrl = newUrl;
            }

            public String getImgSize() {
                return imgSize;
            }

            public void setImgSize(String imgSize) {
                this.imgSize = imgSize;
            }

            public String getIconEnName() {
                return iconEnName;
            }

            public void setIconEnName(String iconEnName) {
                this.iconEnName = iconEnName;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }
            public String getIconName() {
                return iconName;
            }

            public void setIconName(String iconName) {
                this.iconName = iconName;
            }

            public String getIconColor() {
                return iconColor;
            }

            public void setIconColor(String iconColor) {
                this.iconColor = iconColor;
            }

            public String getIconCode() {
                return iconCode;
            }

            public void setIconCode(String iconCode) {
                this.iconCode = iconCode;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getNewModel() {
                return newModel;
            }

            public void setNewModel(String newModel) {
                this.newModel = newModel;
            }
        }
    }
}
