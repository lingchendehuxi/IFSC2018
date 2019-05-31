package com.android.incongress.cd.conference.beans;

/**
 * Created by Admin on 2017/7/27.
 */

public class TuiJianBean {


    /**
     * tjId : 1
     * trueName : 孙燕
     * danwei : 广州南洋肿瘤医院
     * remark : 我国内科肿瘤学的开拓者和学科带头人。他四十多年来在开发抗肿瘤新药，常见肿瘤如睾丸肿瘤、淋巴瘤、肺癌等的综合治疗，扶正中药促进细胞免疫作用，癌症三阶梯止痛原则在我国的推广，以及教育和保健等方面卓有贡献。曾多次在国内外受奖。
     * imgUrl : http://app.incongress.cn/source/upload/cscoTuijian/sunyan.jpg
     * type : 1
     */

    private int tjId;
    private String trueName;
    private String danwei;
    private String remark;
    private String imgUrl;
    private int type;

    public int getTjId() {
        return tjId;
    }

    public void setTjId(int tjId) {
        this.tjId = tjId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
