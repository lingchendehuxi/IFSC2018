package com.android.incongress.cd.conference.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Created by Jacky on 2016/7/25.
 */
public class Confield extends LitePalSupport {
    private int conFieldId;// 领域编号
    private String conFieldName;// 领域名称
    private boolean checked = true;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConFieldId() {
        return conFieldId;
    }

    public void setConFieldId(int conFieldId) {
        this.conFieldId = conFieldId;
    }

    public String getConFieldName() {
        return conFieldName;
    }

    public void setConFieldName(String conFieldName) {
        this.conFieldName = conFieldName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
