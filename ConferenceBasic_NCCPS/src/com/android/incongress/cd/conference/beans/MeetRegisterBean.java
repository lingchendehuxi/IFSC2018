package com.android.incongress.cd.conference.beans;

/**
 * Created by 13008 on 2019/4/12.
 */

public class MeetRegisterBean {

    /**
     * conId : 148
     * fromWhere : CSD2019
     * conName : 中华医学会第二十五次全国皮肤性病学术会议(CSD 2019)
     */

    private int conId;
    private String fromWhere;
    private String conName;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getConId() {
        return conId;
    }

    public void setConId(int conId) {
        this.conId = conId;
    }

    public String getFromWhere() {
        return fromWhere;
    }

    public void setFromWhere(String fromWhere) {
        this.fromWhere = fromWhere;
    }

    public String getConName() {
        return conName;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }
}
