package com.android.incongress.cd.conference.save;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.ui.login.view.LoginActivity;
import com.android.incongress.cd.conference.utils.ConvertUtil;
import com.android.incongress.cd.conference.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;


public class ParseUser {
	//初始化用户数据
	public static void initUserInfo(){
		if (StringUtils.isAllNotEmpty(SharePreferenceUtils.getUser(Constants.USER_NAME),
				SharePreferenceUtils.getUser(Constants.USER_ID) , SharePreferenceUtils.getUser(Constants.USER_TYPE))) {
			AppApplication.userId = ConvertUtil.convertToInt(SharePreferenceUtils.getUser(Constants.USER_ID),-1);
			AppApplication.userType = ConvertUtil.convertToInt(SharePreferenceUtils.getUser(Constants.USER_TYPE),0);
			AppApplication.username = SharePreferenceUtils.getUser(Constants.USER_NAME);
			AppApplication.facultyId = ConvertUtil.convertToInt(SharePreferenceUtils.getUser(Constants.USER_FACULTYID),-1);
		}
	}
	//登陆保存
	public static void saveUserInfo(String userInfo) {
		HashMap<String,String> map = new Gson().fromJson(userInfo, new TypeToken<HashMap<String,String>>(){}.getType());
		SharePreferenceUtils.saveUser(map);
		AppApplication.userId = ConvertUtil.convertToInt(map.get(Constants.USER_ID),-1);
		AppApplication.username = map.get(Constants.USER_NAME);
		AppApplication.userType = ConvertUtil.convertToInt(map.get(Constants.USER_TYPE),0);
		AppApplication.facultyId = ConvertUtil.convertToInt(map.get(Constants.USER_FACULTYID),-1);
	}
	//微信登陆保存
	public static void saveWxUserInfo(JSONObject response) {
		/*SharePreferenceUtils.saveUserInt(Constants.USER_FACULTYID,JSONCatch.parseInt("facultyId",response));
		SharePreferenceUtils.saveUserString(Constants.USER_NAME,JSONCatch.parseString("name",response));
		SharePreferenceUtils.saveUserInt(Constants.USER_TYPE,JSONCatch.parseInt("userType",response));
		SharePreferenceUtils.saveUserString(Constants.USER_ID,JSONCatch.parseString("userId",response));
		SharePreferenceUtils.saveUserString(Constants.USER_OPENID,JSONCatch.parseString("openId",response));
		SharePreferenceUtils.saveUserString(Constants.USER_IMG,JSONCatch.parseString("img",response));
		SharePreferenceUtils.saveUserString(Constants.USER_MOBILE,JSONCatch.parseString("mobilePhone",response));*/
	}
	//退出账号
	public static void clearUserInfo(Context context){
		SharePreferenceUtils.cleanUser();
		SharePreferenceUtils.saveUserBoolean(Constants.USER_IS_LOGIN,false);
		AppApplication.userType = Constants.TYPE_USER_VISITOR;
		AppApplication.userId = -1;
		AppApplication.username = "";
		AppApplication.facultyId = -1;
		/*Intent loginIntent = new Intent();
		loginIntent.setAction(LoginActivity.LOGOUT_ACTION);
		context.sendBroadcast(loginIntent);
		((HomeActivity)context).performBackClick();
		((HomeActivity)context).mNavigationBar.selectTab(0);*/
		context.startActivity(new Intent(context,LoginActivity.class));
		((Activity)context).finish();
	}
	//保存数据信息
	public static void saveDataInfo(String dataInfo){
		HashMap<String,String> map = new Gson().fromJson(dataInfo, new TypeToken<HashMap<String,String>>(){}.getType());
		SharePreferenceUtils.saveApp(map);
	}

}
