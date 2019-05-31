package com.android.incongress.cd.conference.beans;


import org.litepal.crud.LitePalSupport;

/**
 * Created by Jacky on 2017/3/21.
 * 数据包中的数据
 * {
 "backgroundHeight": 195,
 "backgroundUrl": "http://app.incongress.cn/Conferences/files/conpass_1/compass-top-ad.png",
 "backgroundWidth": 1242,
 "homeAdUrl": "http://app.incongress.cn/Conferences/files/conpass_1/MainAd.png",
 "id": 1,
 "name": "CSCO-ESMO\r\n",
 "version": 8
 }
 */

public class CompasEsmoBean extends LitePalSupport{


    /**
     * backgroundHeight : 195
     * backgroundUrl : http://app.incongress.cn/Conferences/files/conpass_1/compass-top-ad.png
     * backgroundWidth : 1242
     * homeAdUrl : http://app.incongress.cn/Conferences/files/conpass_1/MainAd.png
     * id : 1
     * name : CSCO-ESMO

     * version : 8
     */

    private int backgroundHeight;
    private String backgroundUrl;
    private int backgroundWidth;
    private String homeAdUrl;
    private int id;
    private String name;
    private int version;

    public int getBackgroundHeight() {
        return backgroundHeight;
    }

    public void setBackgroundHeight(int backgroundHeight) {
        this.backgroundHeight = backgroundHeight;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public int getBackgroundWidth() {
        return backgroundWidth;
    }

    public void setBackgroundWidth(int backgroundWidth) {
        this.backgroundWidth = backgroundWidth;
    }

    public String getHomeAdUrl() {
        return homeAdUrl;
    }

    public void setHomeAdUrl(String homeAdUrl) {
        this.homeAdUrl = homeAdUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
