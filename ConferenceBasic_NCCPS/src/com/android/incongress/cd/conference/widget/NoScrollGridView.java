package com.android.incongress.cd.conference.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import android.view.MotionEvent;
//自定义不滑动的gridview和点击空白处的监听事件

public class NoScrollGridView extends GridView {

  /**
   * 自定义GridView
   */

    private float mTouchX;
    private float mTouchY;
    private OnTouchBlankPositionListener mTouchBlankPosListener;

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
      super(context, attrs);
    }

    public NoScrollGridView(Context context) {
      super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
      if (mTouchBlankPosListener != null) {
        if (!isEnabled()) {
          return isClickable() || isLongClickable();
        }
        int action = event.getActionMasked();
        float x = event.getX();
        float y = event.getY();
        final int motionPosition = pointToPosition((int) x, (int) y);
        if (motionPosition == INVALID_POSITION) {
          switch (action) {
            case MotionEvent.ACTION_DOWN:
              mTouchX = x;
              mTouchY = y;
              if(x <10 && y<10){
                mTouchBlankPosListener.onTouchBlank(event);
              }
              break;
            case MotionEvent.ACTION_MOVE:
              if (Math.abs(mTouchX - x) < 10
                      && Math.abs(mTouchY - y) < 10) {
                mTouchBlankPosListener.onTouchBlank(event);
              }
              break;
            case MotionEvent.ACTION_UP:
              mTouchX = 0;
              mTouchY = 0;
              mTouchBlankPosListener.onTouchBlank(event);
              break;
          }
        }
      }
      return super.onTouchEvent(event);
    }

    /**
     * 设置GridView的空白区域的触摸事件
     *
     * @param listener
     */
    public void setOnTouchBlankPositionListener(
            OnTouchBlankPositionListener listener) {
      mTouchBlankPosListener = listener;
    }

    public interface OnTouchBlankPositionListener {
      void onTouchBlank(MotionEvent event);
    }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
    super.onMeasure(widthMeasureSpec, expandSpec);
  }

}