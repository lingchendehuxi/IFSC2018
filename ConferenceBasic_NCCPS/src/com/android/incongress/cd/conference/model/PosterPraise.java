package com.android.incongress.cd.conference.model;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Jacky on 2016/8/9.
 */
public class PosterPraise extends LitePalSupport {
    private String posterId;

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }
}
