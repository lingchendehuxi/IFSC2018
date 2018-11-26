/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.incongress.cd.conference.utils;

import com.android.incongress.cd.conference.AlarmActivity;
import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.model.Alert;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Glue class: connects AlarmAlert IntentReceiver to AlarmAlert
 * activity.  Passes through Alarm ID.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Vibrator vibrator;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("sgqTest", "onReceive: 收到了");
        vibrator = (Vibrator) AppApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
        int type = intent.getIntExtra("type", 1);
        Log.d("sgqTest", "onReceive: type"+type);
        switch (type) {
            case 5:
                String date = intent.getStringExtra("date");
                String newString = intent.getStringExtra("title");
                if (newString.contains("#@#")) {
                    newString = newString.replace("#@#", "");
                }
                String start = intent.getStringExtra("start");
                String end = intent.getStringExtra("end");
                String room = intent.getStringExtra("room");
                new AlertDialog.Builder(AppApplication.getContext()).setCancelable(false).setTitle("议程提醒")
                        .setMessage(AppApplication.getContext().getResources().getString(R.string.reminder_meet_tips,date,newString, start, end, room))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //停止音乐
                                //alarmMusic.stop();
                                vibrator.cancel();
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setClass(context, HomeActivity.class);
                                context.startActivity(intent);
                            }
                        }).show();
                break;
        }
    }

}
