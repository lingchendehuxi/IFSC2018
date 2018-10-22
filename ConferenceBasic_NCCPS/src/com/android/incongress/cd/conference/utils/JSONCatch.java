package com.android.incongress.cd.conference.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONCatch {
	public static Double parseDouble(String data, JSONObject object) {
		Double newdata = 0.00;
		try {
			if (object.has(data)) {
				newdata = object.getDouble(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newdata;

	}

	public static String parseString(String data, JSONObject object) {
		String newdata = "";
		try {
			if (object.has(data)&&!"null".equals(object.getString(data))) {
				newdata = object.getString(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newdata;

	}

	public static Integer parseInt(String data, JSONObject object) {
		Integer newdata = 0;
		try {
			if (object.has(data)) {
				newdata = object.getInt(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newdata;

	}

	public static JSONObject parseJsonobject(String data, JSONObject object) {
		JSONObject obj = null;
		try {
			if (object.has(data)) {
				obj = object.getJSONObject(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONArray parseJsonarray(String data, JSONObject object) {
		JSONArray array = null;
		try {
			if (object.has(data)) {
				array = object.getJSONArray(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
	}
}
