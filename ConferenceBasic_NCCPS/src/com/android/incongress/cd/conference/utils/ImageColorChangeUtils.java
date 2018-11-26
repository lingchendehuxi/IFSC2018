package com.android.incongress.cd.conference.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

/**
 * Created by Jacky on 2017/3/20.
 */

public class ImageColorChangeUtils {

    public static Drawable changeIconColor(Context context, int resIcon, int color) {
        Drawable myIcon = context.getResources().getDrawable(resIcon);
        myIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return myIcon;
    }

}
