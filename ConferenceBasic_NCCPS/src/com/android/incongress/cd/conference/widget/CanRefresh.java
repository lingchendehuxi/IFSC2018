package com.android.incongress.cd.conference.widget;

public interface CanRefresh {

    /**
     * 重置
     */
    void onReset();

    /**
     * 下拉高度大于头部高度
     */
    void onPrepare();

    /**
     * 放手后
     */
    void onRelease();

    /**
     * 刷新完成
     */
     void onComplete();

    /**
     * 下拉高度与头部高度比例
     */
     void onPositionChange(float currentPercent);


    /**
     * 是下拉还是上拉
     * @param isHeader
     */
     void setIsHeaderOrFooter(boolean isHeader);
}
