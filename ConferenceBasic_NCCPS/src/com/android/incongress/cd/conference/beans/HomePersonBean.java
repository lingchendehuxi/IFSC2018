package com.android.incongress.cd.conference.beans;

import java.util.List;

public class HomePersonBean {
    private List<ArrayBean> array;

    public List<ArrayBean> getArray() {
        return array;
    }

    public void setArray(List<ArrayBean> array) {
        this.array = array;
    }

    public static class ArrayBean {
        /**
         * name : 朱富娇
         * imgUrl : http://app.incongress.cn:80/files/icUser/1678952019-05-19_1558229709298.jpg
         */

        private String name;
        private String imgUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
