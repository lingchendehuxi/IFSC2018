package com.android.incongress.cd.conference.beans;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class LiveListInfoBean{

    private List<LiveInfoBean> sessionArray;

    public List<LiveInfoBean> getSessionArray() {
        return sessionArray;
    }

    public void setSessionArray(List<LiveInfoBean> sessionArray) {
        this.sessionArray = sessionArray;
    }


}
