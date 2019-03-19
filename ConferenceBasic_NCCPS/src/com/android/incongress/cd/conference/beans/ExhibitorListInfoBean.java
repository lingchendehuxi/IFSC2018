package com.android.incongress.cd.conference.beans;

import java.util.List;

public class ExhibitorListInfoBean {

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

    public static class ResultBean {
        /**
         * id : 2724
         * title : 飞利浦（中国）投资有限公司
         * content : 荷兰皇家飞利浦公司是一家领先的健康科技公司，专注于健康科技领域，将战略重点聚焦于个人健康，精准诊断，引导治疗，互联关护，以及健康信息化和咨询服务五个方面，并致力于在心血管，肿瘤，呼吸，以及优生及母婴护理四大重点健康领域，通过“飞利浦数字健康平台”（Health Suite Digital Platform）连接健康关护全程的各个环节，与合作伙伴联合，不断推出整合的穿行解决方案组合，从而降低医疗成本，提供更好的健康保障和医疗关护。#@#
         * logoImg : http://incongress.cn:8090//files/conferences_44/feilipu.png
         * location : A01
         */

        private int id;
        private String title;
        private String content;
        private String logoImg;
        private String location;
        private String topImg;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
