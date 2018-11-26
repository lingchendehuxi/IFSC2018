package com.android.incongress.cd.conference.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.android.incongress.cd.conference.base.AppApplication;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * Created by Jacky on 2016/1/15.
 */
public class ToastUtils {
    private static Toast toast;

    public static void showShorToast(String msg) {
        Toast.makeText(AppApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShorToast(int msg) {
        Toast.makeText(AppApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(AppApplication.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    //通知框中间
    public static void showRoundRectToast(Context context, int layout) {
        if (layout == 0) {
            return;
        }
        new Builder(context)
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


        public Builder(Context context) {
            this.context = context;
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
            if (toast == null) {
                toast = new Toast(context);
            }
            if (isFill) {
                toast.setGravity(gravity | Gravity.FILL_HORIZONTAL, 0, yOffset);
            } else {
                toast.setGravity(gravity, 0, yOffset);
            }
            toast.setDuration(duration);
            toast.setMargin(0, 0);
            View view = LayoutInflater.from(context).inflate(layout, null);
            toast.setView(view);
            return toast;
        }
    }
}
