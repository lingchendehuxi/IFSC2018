package com.android.incongress.cd.conference.utils;

import com.android.incongress.cd.conference.base.AppApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {
    public static final String DEFAULT = "yyyy-MM-dd";
    public static final String DEFAULT_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_CHINA = "yyyy年MM月dd日";
    public static final String DEFAULT_CHINA_TWO = "MM月dd日";
    public static final String DEFAULT_ENGLISH = "MM dd,yyyy";
    public static final String DEFAULT_ENGLISH2 = "yyyy-MM-dd HH:mm";

    private DateUtil() {
    }

    public static Date getDate(String string, String pattern) {
        Date date = null;
        DateFormat f1 = new SimpleDateFormat(pattern);

        try {
            date = f1.parse(string);
        } catch (Exception ex) {
            System.out.println((new StringBuilder("DateTime getDate(String string,String pattern) Exception ")).append(ex.getMessage()).toString());
        }
        return date;
    }

    public static Date getDate(Date date, String pattern) {
        DateFormat f1 = new SimpleDateFormat(pattern);
        String string = getDateString(date, pattern);
        try {
            date = f1.parse(string);
        } catch (Exception ex) {
            System.out.println((new StringBuilder("DateTime getDate(String string,String pattern) Exception ")).append(ex.getMessage()).toString());
        }
        return date;
    }

    public static String getNowDate(String pattern) {
        Date objDate = new Date();
        SimpleDateFormat objSDateFormat = new SimpleDateFormat(pattern);
        String strConstructDate = objSDateFormat.format(objDate);
        return strConstructDate;
    }

    public static String getNowTime() {
        Date objDateTime = new Date();
        SimpleDateFormat objSDateFormat = new SimpleDateFormat("HH:mm");
        String strCurrentTime = objSDateFormat.format(objDateTime);
        return strCurrentTime;
    }


    public static String getDateString(Date date, String pattern) {
        try {
            SimpleDateFormat format = null;
            if (AppApplication.systemLanguage == 1) {
                format = new SimpleDateFormat(pattern);
            } else {
                format = new SimpleDateFormat(pattern, Locale.ENGLISH);
            }
            return format.format(date);
        } catch (Exception e) {
            System.out.println("DateTime method getDateString error");
        }
        return date.toString();
    }

    /**
     * 获取本月的天数
     */
    public static int getDays(int year, int month) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(year, month, 1);
        c2.set(year, month - 1, 1);
        long c1m = c1.getTimeInMillis();
        long c2m = c2.getTimeInMillis();
        long dm = 0x5265c00L;
        int days = new Long((c1m - c2m) / dm).intValue();
        return days;
    }

    public static String getChinaWeeks(Date date) {
        int weeks;
        try {
            Calendar cld = Calendar.getInstance();
            cld.setTime(date);
            weeks = cld.get(Calendar.DAY_OF_WEEK) - 1;
            switch (weeks) {
                case 1:
                    return "一";
                case 2:
                    return "二";
                case 3:
                    return "三";
                case 4:
                    return "四";
                case 5:
                    return "五";
                case 6:
                    return "六";
                case 0:
                    return "日";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static Date getDate(String stringDate) {
        Date dt = null;

        String ymdhms = (new StringBuilder(String.valueOf(stringDate))).append(" 00:00:000").toString();
        try {
            dt = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(stringDate);
        } catch (Exception e) {
            System.out.println((new StringBuilder("11")).append(e.getMessage()).toString());
        }
        return dt;
    }

    /**
     * @see 2011-1-1 获取30天的时间 int i 天数
     */
    public static Date getDate(Date date, int i) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.DAY_OF_MONTH, i);
        return cale.getTime();
    }

    public static String getDateWithWeek(Date date) {
        return getDateString(date, DEFAULT_CHINA) + " 星期" + getChinaWeeks(date);
    }

    public static String getDateWithWeekInEnglish(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String getDateShort(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. d", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    //检测时间格式是否正确
    private static boolean checkDate(String date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date d;
        try {
            d = df.parse(date);
        } catch (Exception e) {
            //如果不能转换,肯定是错误格式
            return false;
        }
        String s1 = df.format(d);
        // 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
        // "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期
        // 逻辑上不对.
        return date.equals(s1);
    }

    //对输入的中文时间格式处理返回英文格式 返回格式为xx.xx xxxx
    public static String getDateFormatEnglish(String date) {
        if (checkDate(date, DEFAULT)) {
            String[] strings = date.split("-");
            String yearString = strings[0];
            String monthString = strings[1];
            String dayString = strings[2];
            switch (Integer.parseInt(monthString)) {
                case 1:
                    monthString = "Jan";
                    break;
                case 2:
                    monthString = "Feb";
                    break;
                case 3:
                    monthString = "Mar";
                    break;
                case 4:
                    monthString = "Apr";
                    break;
                case 5:
                    monthString = "May";
                    break;
                case 6:
                    monthString = "Jun";
                    break;
                case 7:
                    monthString = "Jul";
                    break;
                case 8:
                    monthString = "Aug";
                    break;
                case 9:
                    monthString = "Sep";
                    break;
                case 10:
                    monthString = "Oct";
                    break;
                case 11:
                    monthString = "Nov";
                    break;
                case 12:
                    monthString = "Dec";
                    break;

            }
            return monthString + "." + dayString + " " + yearString;
        }
        ToastUtils.showToast("时间格式错误");
        return "";
    }

    //对输入的中文时间格式处理返回英文格式 返回格式为xx.xx
    public static String getFormatMonthAndDayEnglish(String date) {
        if (checkDate(date, DEFAULT)) {
            String[] strings = date.split("-");
            String monthString = strings[1];
            String dayString = strings[2];
            switch (Integer.parseInt(monthString)) {
                case 1:
                    monthString = "Jan";
                    break;
                case 2:
                    monthString = "Feb";
                    break;
                case 3:
                    monthString = "Mar";
                    break;
                case 4:
                    monthString = "Apr";
                    break;
                case 5:
                    monthString = "May";
                    break;
                case 6:
                    monthString = "Jun";
                    break;
                case 7:
                    monthString = "Jul";
                    break;
                case 8:
                    monthString = "Aug";
                    break;
                case 9:
                    monthString = "Sep";
                    break;
                case 10:
                    monthString = "Oct";
                    break;
                case 11:
                    monthString = "Nov";
                    break;
                case 12:
                    monthString = "Dec";
                    break;

            }
            return monthString + "." + dayString + " ";
        }
        ToastUtils.showToast("时间格式错误");
        return "";
    }
    //对输入的中文时间格式处理返回中文格式 返回格式为x月x日
    public static String getFormatMonthAndDayChinese(String date) {
        if (checkDate(date, DEFAULT)) {
            String[] strings = date.split("-");
            return strings[1]+"月"+strings[2]+"日";
        }
        ToastUtils.showToast("时间格式错误");
        return "";
    }


}