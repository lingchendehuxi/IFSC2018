package com.android.incongress.cd.conference.utils;

import android.util.Log;

/**
 * @author great.maronghua@gmail.com
 * @since 2012-8-15
 * TODO 发布的时候清空这些方法
 */
public final class LogUtils {
	private static final boolean DEBUG = true;
	
	public static void println(String msg) {
		if (DEBUG) {
			Log.i("LogUtils", msg);
		}
	}

	public static void printStackTrace(Exception e) {
		if (DEBUG) {
			e.printStackTrace();
		}
	}
	
	public static void printStackTrace(Error e) {
		if (DEBUG) {
			e.printStackTrace();
		}
	}
	
	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable thr) {
		if (DEBUG) {
			Log.e(tag, msg, thr);
		}
	}
}
