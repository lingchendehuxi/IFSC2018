package com.android.incongress.cd.conference.beans;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2018/5/17.
 */

public class AllRooms extends DataSupport {
    private int classesCapacity;
    private String classesCode;

    private int classesId;
    private String classesLocation;
    private int conferencesId;
    private int level;
    private String mapName;
    private String classCodeEn;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getClassesCode() {
        return classesCode;
    }

    public void setClassesCode(String classesCode) {
        this.classesCode = classesCode;
    }

    public int getClassesId() {
        return classesId;
    }

    public void setClassesId(int classesId) {
        this.classesId = classesId;
    }

    public String getClassesLocation() {
        return classesLocation;
    }

    public void setClassesLocation(String classesLocation) {
        this.classesLocation = classesLocation;
    }

    public int getConferencesId() {
        return conferencesId;
    }

    public void setConferencesId(int conferencesId) {
        this.conferencesId = conferencesId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getClassCodeEn() {
        return classCodeEn;
    }

    public void setClassCodeEn(String classCodeEn) {
        this.classCodeEn = classCodeEn;
    }

    public int getClassesCapacity() {
        return classesCapacity;
    }

    public void setClassesCapacity(int classesCapacity) {
        this.classesCapacity = classesCapacity;
    }
}
