package com.android.incongress.cd.conference;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Vibrator;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.BaseActivity;
import com.mobile.incongress.cd.conference.basic.csccm.R;

public class AlarmActivity extends BaseActivity {
    //MediaPlayer alarmMusic;
    private Vibrator vibrator;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_alarm);
        //加载指定音乐，并为之创建MediaPlayer对象
        //alarmMusic = MediaPlayer.create(this, R.raw.beep);
        //alarmMusic.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
        //播放闹钟
        //alarmMusic.start();

        int type = getIntent().getIntExtra("type", 1);
        switch (type) {
            case 1:
                //创建一个对话框
                String from = getIntent().getStringExtra("from");
                String to = getIntent().getStringExtra("to");
                String time = getIntent().getStringExtra("time");

                new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("班车提醒")
                        .setMessage(getString(R.string.reminder_bus_tips, from, to, time))
                        .setPositiveButton("确定", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //停止音乐
                                //alarmMusic.stop();
                                vibrator.cancel();
                                AlarmActivity.this.finish();
                                dialog.dismiss();

                                Intent intent = new Intent();
                                intent.setClass(AlarmActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                break;
            case 2:
                String title = getIntent().getStringExtra("meetingName");

                new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("会议提醒")
                        .setMessage(title)
                        .setPositiveButton("确定", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //停止音乐
                                //alarmMusic.stop();
                                vibrator.cancel();
                                AlarmActivity.this.finish();
                                dialog.dismiss();
                                Intent intent = new Intent();
                                intent.setClass(AlarmActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }).show();
                break;
            case 3:
                final String newTitle = getIntent().getStringExtra("title");
                final String url = getIntent().getStringExtra("liveUrl");
                String liveName;
                if (newTitle.contains("#@#")) {
                    if (AppApplication.systemLanguage == 1) {
                        liveName = newTitle.split("#@#")[0];
                        final String finalLiveName = liveName;
                        new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("直播提醒")
                                .setMessage("您预约的直播会议 - " + finalLiveName + " - 已经开始，请前往观看直播。")
                                .setPositiveButton("确定", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //停止音乐
                                        //alarmMusic.stop();
                                        vibrator.cancel();
                                        String liveUrl = url + "?canShare=1";
                                        if (!liveUrl.contains("?"))
                                            liveUrl = liveUrl + "?";
                                        CollegeActivity.startCitCollegeActivity(AlarmActivity.this, finalLiveName, liveUrl);
                                        AlarmActivity.this.finish();
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {
                        liveName = newTitle.split("#@#")[1];
                        final String finalLiveName = liveName;
                        new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("Reminder")
                                .setMessage("The live you reserved has begun " + finalLiveName + ". Please click \"Yes\" and enjoy it.")
                                .setPositiveButton("Yes", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //停止音乐
                                        //alarmMusic.stop();
                                        vibrator.cancel();
                                        String liveUrl = url + "?canShare=1";
                                        CollegeActivity.startCitCollegeActivity(AlarmActivity.this, finalLiveName, liveUrl);
                                        AlarmActivity.this.finish();
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                } else {
                    new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("直播提醒")
                            .setMessage("您预约的直播会议 - " + newTitle + " - 已经开始，请前往观看直播。")
                            .setPositiveButton("确定", new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //停止音乐
                                    //alarmMusic.stop();
                                    vibrator.cancel();
                                    String liveUrl = url + "?canShare=1";
                                    CollegeActivity.startCitCollegeActivity(AlarmActivity.this, newTitle, liveUrl);
                                    AlarmActivity.this.finish();
                                    dialog.dismiss();
                                }
                            }).show();
                }
                break;
            case 5:
                String date = getIntent().getStringExtra("date");
                String newString = getIntent().getStringExtra("title");
                if (newString.contains("#@#")) {
                    String[] split = newString.split("#@#");
                    newString = split[split.length - 1];
                }
                String start = getIntent().getStringExtra("start");
                String end = getIntent().getStringExtra("end");
                String room = getIntent().getStringExtra("room");
                new AlertDialog.Builder(AlarmActivity.this).setCancelable(false).setTitle("日程提醒")
                        .setMessage(getString(R.string.reminder_meet_tips, date, newString, start, end, room))
                        .setPositiveButton("确定", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //停止音乐
                                //alarmMusic.stop();
                                vibrator.cancel();
                                dialog.dismiss();
                                AlarmActivity.this.finish();
								/*Intent intent = new Intent();
								intent.setClass(AlarmActivity.this, HomeActivity.class);
								startActivity(intent);*/
                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initViewsAction() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }
}