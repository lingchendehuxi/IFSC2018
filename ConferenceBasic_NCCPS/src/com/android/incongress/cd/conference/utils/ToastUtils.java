package com.android.incongress.cd.conference.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/1/15.
 */
public class ToastUtils {
    private static Toast toastString;
    private static Toast toastView;
    /** Data */
    private static String oldMsg;
    private static long oneTime = 0;
    private static long twoTime = 0;
    /*********************************/
    public static void showToast( String s){
        if(toastString==null){
            toastString =Toast.makeText(AppApplication.getInstance(), s, Toast.LENGTH_SHORT);
            toastString.show();
            oneTime=System.currentTimeMillis();
        }else{
            twoTime=System.currentTimeMillis();
            if(s.equals(oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_SHORT){
                    toastString.show();
                }
            }else{
                oldMsg = s;
                toastString.setText(s);
                toastString.show();
            }
        }
        oneTime=twoTime;
    }

    //通知框中间
    public static void showRoundRectToast(Context context,String mindText, int layout) {
        if (layout == 0) {
            return;
        }
        new Builder(context,mindText)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(Gravity.CENTER)
                .setOffset(0)
                .setLayout(layout)
                .build()
                .show();
    }

    public static final class Builder {

        private Context context;
        private CharSequence title;
        private CharSequence desc;
        private int gravity = Gravity.TOP;
        private boolean isFill;
        private int yOffset;
        private int duration = Toast.LENGTH_SHORT;
        private int textColor = Color.WHITE;
        private int backgroundColor = Color.BLACK;
        private float radius;
        private int elevation;
        private int layout;
        private String mindText;


        public Builder(Context context,String mindText) {
            this.context = context;
            this.mindText = mindText;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setDesc(CharSequence desc) {
            this.desc = desc;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setFill(boolean fill) {
            isFill = fill;
            return this;
        }

        public Builder setOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder setElevation(int elevation) {
            this.elevation = elevation;
            return this;
        }

        public Builder setLayout(@LayoutRes int layout) {
            this.layout = layout;
            return this;
        }

        public Toast build() {
            if (toastView == null) {
                toastView = new Toast(context);
            }
            if (isFill) {
                toastView.setGravity(gravity | Gravity.FILL_HORIZONTAL, 0, yOffset);
            } else {
                toastView.setGravity(gravity, 0, yOffset);
            }
            toastView.setDuration(duration);
            toastView.setMargin(0, 0);
            View view = LayoutInflater.from(context).inflate(layout, null);
            TextView tv_mind = view.findViewById(R.id.tv_mind);
            tv_mind.setText(mindText);
            toastView.setView(view);
            return toastView;
        }
    }
}
