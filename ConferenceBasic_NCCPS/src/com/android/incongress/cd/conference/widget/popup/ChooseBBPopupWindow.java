package com.android.incongress.cd.conference.widget.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Admin on 2017/9/12.
 */

public class ChooseBBPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;

    public ListView getmListView() {
        return mListView;
    }

    public ChooseBBPopupWindow(Context context) {
        super(context);
        calWidthAndHeight();

        mConvertView = LayoutInflater.from(context).inflate(R.layout.time_yd_selector, null);
        mListView = mConvertView.findViewById(R.id.list_not_scroll);
        setContentView(mConvertView);
        setWidth(mWidth);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true); //外部可以点击
        setBackgroundDrawable(new BitmapDrawable());;//点击外部消失
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 计算popupWindow的高度和宽度
     */
    private void calWidthAndHeight() {
        WindowManager wm = (WindowManager) AppApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = (int)(outMetrics.widthPixels*0.75);
        mHeight = (int) (outMetrics.heightPixels * 0.4);
    }

}
