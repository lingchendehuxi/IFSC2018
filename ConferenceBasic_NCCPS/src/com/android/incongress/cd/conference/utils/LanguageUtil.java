package com.android.incongress.cd.conference.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.incongress.cd.conference.base.AppApplication;

import java.util.Locale;

public class LanguageUtil {
    //1 代表中文    2 代表英文    其他值  随系统语言变化而变化
    public static void setLanguage(Context context, int type) {
        Locale myLocale;
        // 本地语言设置
        switch (type) {
            case 1:
                myLocale = new Locale("zh");
                break;
            case 2:
                myLocale = new Locale("en");
                break;
            default:
                return;
        }
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    //获取当前语言的形式
    public static String getCurrentLan(Context context) {
        switch (AppApplication.systemLanguage) {
            case 1:
                return "cn";
            case 2:
                return "en";
            default:
                Resources res = context.getResources();
                Configuration config = res.getConfiguration();
                String locale = config.locale.getCountry();
                return locale;
        }
    }
}
