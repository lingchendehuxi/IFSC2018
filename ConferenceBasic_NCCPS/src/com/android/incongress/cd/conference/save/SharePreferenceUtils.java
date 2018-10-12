package com.android.incongress.cd.conference.save;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.incongress.cd.conference.base.AppApplication;

import java.util.Map;
import java.util.Set;

public class SharePreferenceUtils {
	public static String user_msg = "user_msg";
	public static String base_url = "base_url";

	/** 保存appConfig配置 */
	public static void saveApp(Map<String, String> map) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		Set<String> set = map.keySet();
		for (String string : set) {
			editor.putString(string, map.get(string));
		}
		editor.commit();
	}

	/** 读取appConfig配置 */
	public static String getApp(String key) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
		return share.getString(key, "");
	}

	public static void saveAppString(String key, String value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void cleanApp() {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
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
	//App配置布尔表
	public static void setSPBooleanValue(String key, boolean value) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putBoolean(key,value);
		editor.commit();
	}

	public static boolean getSPBooleanValueF(String key) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
		return share.getBoolean(key, false);
	}
	public static boolean getSPBooleanValueT(String key) {
		SharedPreferences share = AppApplication.getContext().getSharedPreferences(base_url, Context.MODE_PRIVATE);
		return share.getBoolean(key, true);
	}
}
