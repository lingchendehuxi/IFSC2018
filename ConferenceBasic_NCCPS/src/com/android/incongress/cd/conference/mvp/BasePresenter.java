package com.android.incongress.cd.conference.mvp;

/**
 * Created by 13008 on 2019/4/29.
 */

public interface BasePresenter {
    //绑定数据
    void subscribe();
    //解除绑定
    void unSubscribe();
}
