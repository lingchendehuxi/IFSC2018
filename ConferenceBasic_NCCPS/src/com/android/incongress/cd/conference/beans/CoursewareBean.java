package com.android.incongress.cd.conference.beans;

/**
 * Created by Admin on 2018/2/2.
 */

public class CoursewareBean {

    /**
     * dataId : 6999
     * title : 闭幕致辞：血管闭合装置 - 它们为何重要？,Closing Remarks: Closure Devices - Why Are They Important?
     * url : http://app.incongress.cn/Exam/data?method=getDataByDataId&dataId=6999
     * author : Walker Craig M.
     * firstPic : /Exam/files/img/2017-12-21_1513835266847.jpg
     */

    private int dataId;
    private String title;
    private String url;
    private String author;
    private String firstPic;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }
}
