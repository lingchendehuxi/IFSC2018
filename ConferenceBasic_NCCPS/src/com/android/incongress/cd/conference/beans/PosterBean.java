package com.android.incongress.cd.conference.beans;

/**
 * Created by Jacky on 2017/1/5.
 */

public class PosterBean {

    /**
     * author : 邱妙珍
     * title : Evaluation of MET and HER2 expression in primary and metastatic tumor in Chinese advanced gastric cancer patients
     * disCount : 0
     * posterId : 15451
     * posterPicUrl : http://app.incongress.cn/files/LED/194/img/P-03-E0744.jpg
     * state : 1
     * conField : 胃肠肿瘤
     * posterCode : P-03
     * maxCount : 0
     */

    private String author;
    private String title;
    private int disCount;
    private int posterId;
    private String posterPicUrl;
    private int state;
    private String conField;
    private String posterCode;
    private int maxCount;
    private int isJingxuan;

    public int getIsJingxuan() {
        return isJingxuan;
    }

    public void setIsJingxuan(int isJingxuan) {
        this.isJingxuan = isJingxuan;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDisCount() {
        return disCount;
    }

    public void setDisCount(int disCount) {
        this.disCount = disCount;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public String getPosterPicUrl() {
        return posterPicUrl;
    }

    public void setPosterPicUrl(String posterPicUrl) {
        this.posterPicUrl = posterPicUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getConField() {
        return conField;
    }

    public void setConField(String conField) {
        this.conField = conField;
    }

    public String getPosterCode() {
        return posterCode;
    }

    public void setPosterCode(String posterCode) {
        this.posterCode = posterCode;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
