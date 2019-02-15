package com.android.incongress.cd.conference.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Class extends DataSupport{

    /**
     * classesCapacity : 200
     * classesCode : 国宴厅1
     * classesId : 3277
     * classesLocation :
     * conferencesId : 194
     * level : 0
     * mapName :
     */
    private int id;
    private int classesCapacity;
    private String classesCode;

    private int classesId;
    private String classesLocation;
    private int conferencesId;
    private int level;
    private String mapName;
    private String classCodeEn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
