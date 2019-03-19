package com.android.incongress.cd.conference.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Exhibitor extends LitePalSupport{

    /**
     * exhibitorsId : 1125
     * address :
     * info :
     * phone :
     * fax :
     * net :
     * title : 欧莱雅（中国）有限公司
     * location : 特装113-122       标展215
     * level : 1
     * logo :
     * exhibitorsLocation : null
     * mapName :
     */
    private int id;
    private int exhibitorsId;
    private String address;
    private String info;
    private String infoEn;
    private String phone;
    private String fax;
    private String net;
    private String title;
    private String titleEn;
    private String location;
    private int level;
    private String logo;
    private String exhibitorsLocation;
    private String mapName;
    private String addressEn;
    private String otherUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtherUrl() {
        return otherUrl;
    }

    public void setOtherUrl(String otherUrl) {
        this.otherUrl = otherUrl;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getInfoEn() {
        return infoEn;
    }

    public void setInfoEn(String infoEn) {
        this.infoEn = infoEn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public int getExhibitorsId() {
        return exhibitorsId;
    }

    public void setExhibitorsId(int exhibitorsId) {
        this.exhibitorsId = exhibitorsId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getExhibitorsLocation() {
        return exhibitorsLocation;
    }

    public void setExhibitorsLocation(String exhibitorsLocation) {
        this.exhibitorsLocation = exhibitorsLocation;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
