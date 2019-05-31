package com.android.incongress.cd.conference.save;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.incongress.cd.conference.base.AppApplication;

import java.util.Map;
import java.util.Set;

public class SharePreferenceUtils {
	public static final String user_msg = "user_msg";
	public static final String base_app = "base_app";
	public static final String base_data = "base_data";

	/** 保存appConfig配置 */
	public static void saveApp(Map<String, String> map) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		Set<String> set = map.keySet();
		for (String string : set) {
			editor.putString(string, map.get(string));
		}
		editor.commit();
	}

	/** 读取appConfig配置 */
	public static String getAppString(String key) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		return share.getString(key, "");
	}
	public static int getAppInt(String key,int value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		return share.getInt(key, value);
	}
	public static boolean getAppBoolean(String key,Boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		return share.getBoolean(key, value);
	}

	public static void saveAppString(String key, String value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static void saveAppInt(String key, int value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static void saveAppBoolean(String key, boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void cleanApp() {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_app, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.clear().commit();
	}

	/** 保存用户信息 */
	public static void saveUser(Map<String, String> map) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		Set<String> set = map.keySet();
		for (String key : set) {
			editor.putString(key, map.get(key));
		}
		editor.commit();
	}
	public static void saveUserBoolean(String key, boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static boolean getUserBoolean(String key, boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		return share.getBoolean(key,value);
	}
	public static void saveUserInt(String key, int value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static int getUserInt(String key, int value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		return share.getInt(key,value);
	}

	/** 开发测试用 */
	public static void saveUserString(String key, String value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/***/
	public static String getUser(String key) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		return share.getString(key, "");
	}

	public static void cleanUser() {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(user_msg, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.clear().commit();
	}
	//存储数据包状态
	public static void saveDataBoolean(String key, boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_data, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static boolean getDataBoolean(String key, boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_data, Context.MODE_PRIVATE);
		return share.getBoolean(key,value);
	}
	public static void saveDataInt(String key, int value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_data, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static int getDataInt(String key, int value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_data, Context.MODE_PRIVATE);
		return share.getInt(key,value);
	}
}
