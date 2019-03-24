package com.android.incongress.cd.conference.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.android.incongress.cd.conference.AlarmActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.BusRemindBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.model.Alert;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Admin on 2017/5/18.
 */

public class AlarmUtils {
    /**
     * type 1班车提醒
     * @param context
     * @param bean
     */
    public static void addAlarm(Context context, BusRemindBean bean) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);

        String from = "";
        String to = "";
        if(bean.getIsStartOrBack() == 0) {
            from = bean.getBusFrom();
            to = bean.getBusTo();
        }else if(bean.getIsStartOrBack() == 1) {
            from = bean.getBusTo();
            to = bean.getBusFrom();
        }

        intent.putExtra("from", from);
        intent.putExtra("to", to);
        intent.putExtra("busId", bean.getBusInfoId());
        intent.putExtra("type", 1);

        try {
            String s = bean.getBusDate() + " " + bean.getBusTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(s.replace("\n"," "));
            long time30 = 30*60*1000;//30分钟
            long time15 = 15*60*1000;
            if (System.currentTimeMillis() < date.getTime()) {

                int isStartOrBack ;
                if(bean.getIsStartOrBack() == 1) {
                    isStartOrBack = 1;
                }else {
                    isStartOrBack = 2;
                }
                intent.putExtra("time", "30分钟");
                PendingIntent pi30 = PendingIntent.getActivity(context, bean.getBusInfoId() + 30 + isStartOrBack, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                intent.putExtra("time", "15分钟");
                PendingIntent pi15 = PendingIntent.getActivity(context, bean.getBusInfoId() + 15 + isStartOrBack, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime() - time30, pi30);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime() - time15, pi15);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP,date.getTime() - time30, pi30);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,date.getTime() - time15, pi15);
                }
                bean.save();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加会议闹钟 type 2 会议提醒
     * @param context
     * @param bean
     */
    public static void addMeetingAlarm(Context context, Alert bean) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);
        String tip = "";
        if(bean.getTitle().contains("#@#")){
            if(AppApplication.systemLanguage == 1)  {
                tip = bean.getTitle().split("#@#")[0];
            }else {
                tip =  bean.getTitle().split("#@#")[1];
            }
        }else{
            tip =  bean.getTitle();
        }
        intent.putExtra("meetingName", tip);

        intent.putExtra("type", 2);

        Random random = new Random();
        int num = Math.abs(random.nextInt());
        PendingIntent pi = PendingIntent.getActivity(context,num, intent, 0);

        try {
            String s = bean.getDate() + " " + bean.getStart();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(s.replace("\n"," "));

            long time5 = 5*60*1000;//5分钟

            if (System.currentTimeMillis() < date.getTime()) {
                alarmManager.set(AlarmManager.RTC,date.getTime() - time5, pi);
                bean.save();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加会议闹钟 type 3 直播预约
     * @param context
     * @param startTime
     */
    public static void addLiveYuyue(Context context,String liveName, String startTime,String liveUrl) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);

        intent.putExtra("liveName", liveName);
        intent.putExtra("liveUrl", liveUrl);
        intent.putExtra("type", 3);

        Random random = new Random();
        int num = Math.abs(random.nextInt());
        PendingIntent pi = PendingIntent.getActivity(context,num, intent, 0);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(startTime.replace("\n"," "));
            if (System.currentTimeMillis() < date.getTime()) {
                alarmManager.set(AlarmManager.RTC,date.getTime(), pi);
            }else{
                Toast.makeText(context,"直播已结束",Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除闹钟提醒
     * @param context
     * @param bean
     */
    public static void deleteMeetingAlarm(Context context, SessionBean bean) {

    }

    public static void deleteMeetingAlarm(Context context, Alert bean) {

    }

    public static void deleteAlarm(Context context, BusRemindBean bean) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("from", bean.getBusFrom());
        intent.putExtra("to", bean.getBusTo());

        PendingIntent pi = PendingIntent.getActivity(context, bean.getBusInfoId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pi);

        deleteBusResmindByBusInfoIdAndTime(bean.getBusInfoId(),bean.getIsStartOrBack());
    }


    public static List<BusRemindBean> getAllBusRemind() {
        List<BusRemindBean> reminds = LitePal.findAll(BusRemindBean.class);
        if (reminds != null)
            return reminds;
        else
            return new ArrayList<BusRemindBean>();
    }


    /**
     * 查找是否有某个闹钟
     *
     * @param busInfoId
     * @param isStartOrBack
     * @return
     */
    public static boolean findBusRemindByBusInfoIdAndTime(int busInfoId, int isStartOrBack) {
        List<BusRemindBean> busRemindBeen = LitePal.where("busInfoId = " + busInfoId + " and isStartOrBack = " + isStartOrBack).find(BusRemindBean.class);

        if (busRemindBeen != null && busRemindBeen.size() > 0)
            return true;
        else
            return false;
    }

    /**
     * 删除某个闹钟
     * @param busInfoId
     * @param isStartOrBack
     * @return
     */
    public static void deleteBusResmindByBusInfoIdAndTime(int busInfoId, int isStartOrBack) {
        List<BusRemindBean> busRemindBeenList = LitePal.where("busInfoId = " + busInfoId + " and isStartOrBack = " + isStartOrBack).find(BusRemindBean.class);

        if(busRemindBeenList != null && busRemindBeenList.size() > 0) {
            for (int i = 0; i < busRemindBeenList.size(); i++) {
                busRemindBeenList.get(i).delete();
            }
        }
    }
    /*private static void enableAlert(Context context, final Alert alarm,
                                    final long atTimeInMillis) {
        long time15 = 15*60*1000;
        boolean enable = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_ENABLE, true);
        if (!enable) {
            return;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("object", alarm);
        intent.putExtras(bundle);
        PendingIntent sender = PendingIntent.getActivity(
                AppApplication.getContext(), 0, intent, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, atTimeInMillis, sender);
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(atTimeInMillis);
        String timeString = CommonUtils.fortmatDate(c.getTime());
        Log.d("cccc", "enableAlert is " + timeString);

    }*/
}
