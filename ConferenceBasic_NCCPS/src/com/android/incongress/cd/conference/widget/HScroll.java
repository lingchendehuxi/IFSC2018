package com.android.incongress.cd.conference.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by Jacky on 2015/12/1.
 */
public class HScroll extends HorizontalScrollView {
    private HorizontalScrollView hvScrollView;

    public HorizontalScrollView getHvScrollView() {
        return hvScrollView;
    }

    public void setHvScrollView(HorizontalScrollView hvScrollView) {
        this.hvScrollView = hvScrollView;
    }

    public HScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HScroll(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldl, int oldt) {
        super.onScrollChanged(scrollX, scrollY, oldl, oldt);
        hvScrollView.scrollTo(scrollX,0);
        // 判断 scrollView 当前滚动位置在顶部
        if(getScrollX() == 0){
            hvScrollView.fullScroll(ScrollView.FOCUS_LEFT);
        }

        // 判断scrollview 滑动到底部
        // scrollY 的值和子view的高度一样，这人物滑动到了底部
        if (getChildAt(0).getWidth() - getWidth()
                == getScrollX()){
            hvScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
        }
    }
}
