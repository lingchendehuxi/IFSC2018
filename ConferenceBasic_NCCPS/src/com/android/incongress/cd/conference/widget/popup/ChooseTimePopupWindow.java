package com.android.incongress.cd.conference.widget.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.incongress.cd.conference.adapters.MeetingYYAdapter;
import com.android.incongress.cd.conference.fragments.meeting_schedule.MeetingScheduleListActionFragment;
import com.android.incongress.cd.conference.model.Meeting;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;

/**
 * Created by Admin on 2017/9/12.
 */

public class ChooseTimePopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;

    public ListView getmListView() {
        return mListView;
    }

    public ChooseTimePopupWindow(Context context ) {
        super(context);
        calWidthAndHeight(context);

        mConvertView = LayoutInflater.from(context).inflate(R.layout.time_yd_selector, null);
        mListView = mConvertView.findViewById(R.id.list_not_scroll);
        setContentView(mConvertView);
        setWidth((int)(mWidth*0.4));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.4);
    }

}
