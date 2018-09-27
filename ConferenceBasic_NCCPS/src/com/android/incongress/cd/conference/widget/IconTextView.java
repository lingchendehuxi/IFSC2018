package com.android.incongress.cd.conference.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jacky on 2017/1/4.
 */

public class IconTextView extends TextView {
    public IconTextView(Context context) {
        super(context);
        initIconFont(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initIconFont(context);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initIconFont(context);
    }

    private void initIconFont(Context context) {
        AssetManager assetsManager = context.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetsManager, "iconfont.ttf");
        setTypeface(typeface);
    }
}
