package com.android.incongress.cd.conference.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.FastOnLineBean;
import com.android.incongress.cd.conference.beans.JpushDataBean;
import com.android.incongress.cd.conference.utils.ExampleUtil;
import com.android.incongress.cd.conference.utils.JSONCatch;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private static final long NOTIFICATION_ID = 10086;
    private static boolean first = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            if (HomeActivity.isForeground) {
                processCustomMessage(context, bundle, 2);
            } else {
                //得到具体的推送内容id，将其打开
                String urlJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
                //String title = bundle.getString(JPushInterface.EXTRA_TITLE);
                String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                JSONObject url = null;
                try {
                    if (!StringUtils.isEmpty(urlJson)) {
                        url = new JSONObject(urlJson);
                        JPushLocalNotification ln = new JPushLocalNotification();
                        ln.setContent(content);
                        //需要标题，简介，dataId，
                        ln.setNotificationId(NOTIFICATION_ID);
                        ln.setBuilderId(1);
                        String trueUrlJson = null;
                        String trueShareJson = null;
                        String trueTitleJson = null;
                        if (urlJson.indexOf("H5URL") != -1) {
                            trueUrlJson = url.getString("H5URL").replace("\\\\", "");
                        }
                        if (urlJson.indexOf("H5SHARE") != -1) {
                            trueShareJson = url.getString("H5SHARE");
                        }
                        if (urlJson.indexOf("H5TITLE") != -1) {
                            trueTitleJson = url.getString("H5TITLE");
                        }
                        Map<String, Object> map = new HashMap<String, Object>();
                        if (trueUrlJson != null) {
                            map.put("H5URL", trueUrlJson);
                        }
                        if (trueTitleJson != null) {
                            map.put("H5TITLE", trueTitleJson);
                        }
                        if (trueShareJson != null) {
                            map.put("H5SHARE", trueShareJson);
                        }
                        map.put("notificationId", NOTIFICATION_ID);
                        JSONObject json = new JSONObject(map);
                        ln.setExtras(json.toString());
                        JPushInterface.addLocalNotification(AppApplication.getContext(), ln);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知: " + content);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //打开自定义的Activity
            //这里可以根据具体的消息打开需要的Activity
            String info = bundle.getString(JPushInterface.EXTRA_EXTRA).replace("\\", "");
            Log.d(TAG, "info = " + info);
            if(info.contains("ModelId")){
                Intent i = new Intent(context, HomeActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                return;
            }
            String url = "";
            String title = "";
            String share = "";
            long notificationId;
            try {
                JSONObject obj = new JSONObject(info);
                url = obj.getString("H5URL");
                title = obj.getString("H5TITLE");
                share = obj.getString("H5SHARE");
                //notificationId = obj.getLong("notificationId");

                bundle.putString("H5URL", url);
                bundle.putString("H5TITLE", title);
                bundle.putString("H5SHARE", share);
                bundle.putLong("notificationId", 10086);
                Intent i = new Intent(context, HomeActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))

        {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))

        {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected + "---" + JPushInterface.getRegistrationID(context));
        } else

        {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to HomeActivity
    private void processCustomMessage(Context context, Bundle bundle, int type) {
        if (HomeActivity.isForeground) {
            String message;
            if (type == 1) {
                message = bundle.getString(JPushInterface.EXTRA_ALERT);
            } else {
                message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            }
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(HomeActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(HomeActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(HomeActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {
                }
            }
            context.sendBroadcast(msgIntent);
        }
    }


}
