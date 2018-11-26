package com.android.incongress.cd.conference.model;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Role extends DataSupport {

    /**
     * roleId : 856
     * name : 主持人
     * enName :
     */
    private int roleId;
    private String name;
    private String enName;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }
}
