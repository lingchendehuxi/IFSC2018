package com.android.incongress.cd.conference.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Map extends LitePalSupport {
    private int conferencesmapId;// 地图编号
    private int conferencesId;// 参会议编号
    private String mapRemark;// 地图楼层 显示名字
    private String mapUrl;// 图片名称 去查找资源
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConferencesmapId() {
        return conferencesmapId;
    }

    public void setConferencesmapId(int conferencesmapId) {
        this.conferencesmapId = conferencesmapId;
    }

    public int getConferencesId() {
        return conferencesId;
    }

    public void setConferencesId(int conferencesId) {
        this.conferencesId = conferencesId;
    }

    public String getMapRemark() {
        return mapRemark;
    }

    public void setMapRemark(String mapRemark) {
        this.mapRemark = mapRemark;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }
}
