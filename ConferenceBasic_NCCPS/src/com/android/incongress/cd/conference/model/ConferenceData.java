package com.android.incongress.cd.conference.model;

import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/11/27 0027.
 * 存储会议的id，以及对应的数据版本号
 */

public class ConferenceData extends DataSupport {
    private int conferenceId;
    private int dataVersion;


    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public int getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(int dataVersion) {
        this.dataVersion = dataVersion;
    }
}
