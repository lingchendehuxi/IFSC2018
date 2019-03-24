package com.android.incongress.cd.conference.beans;

import java.io.Serializable;
import java.util.List;

public class ExhibitorListInfoBean implements Serializable{


    private int resultState;
    private List<ResultBean> topArray;
    private List<ResultBean> result;

    public int getResultState() {
        return resultState;
    }

    public void setResultState(int resultState) {
        this.resultState = resultState;
    }

    public List<ResultBean> getTopArray() {
        return topArray;
    }

    public void setTopArray(List<ResultBean> topArray) {
        this.topArray = topArray;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {

        private int id;
        private String title;
        private String content;
        private String logoImg;
        private String companyAddress;
        private String phone;
        private String url;
        private String location;
        private int type;
        private String topImg;

        public String getTopImg() {
            return topImg;
        }

        public void setTopImg(String topImg) {
            this.topImg = topImg;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLogoImg() {
            return logoImg;
        }

        public void setLogoImg(String logoImg) {
            this.logoImg = logoImg;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
