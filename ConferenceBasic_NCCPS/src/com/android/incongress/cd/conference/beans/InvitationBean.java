package com.android.incongress.cd.conference.beans;

/**
 * Created by 13008 on 2019/4/21.
 */

public class InvitationBean {

    /**
     * id : 1
     * title : TEST
     * url : http://live.incongress.cn/watch/293730
     * img : http://app.incongress.cn:80/Conferences/files/image/20190420/20190420193726_408.png
     * type : 5
     */

    private int id;
    private String title;
    private String url;
    private String img;
    private int type;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
