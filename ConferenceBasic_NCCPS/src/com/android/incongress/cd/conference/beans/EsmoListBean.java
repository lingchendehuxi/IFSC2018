package com.android.incongress.cd.conference.beans;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Jacky on 2016/11/24.
 */
public class EsmoListBean{
    private List<EsmosBean> esmos;

    public List<EsmosBean> getEsmos() {
        return esmos;
    }

    public void setEsmos(List<EsmosBean> esmos) {
        this.esmos = esmos;
    }
}
