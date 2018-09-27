package com.android.incongress.cd.conference.utils;

import android.content.Context;

import com.android.incongress.cd.conference.ChooseIdentityActivity;
import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;

/**
 * Created by Jacky on 2016/10/10.
 * 各个功能的权限控制，在跳转之前进行判断，统一处理
 *
 *
 */

public class AuthorityUtils {

//    public static final int TYPE_USER_VISITOR = 0; 游客身份
//    public static final int TYPE_USER_REGISTER_NOT_BIND_CODE = 1; 注册用户，未绑定注册码
//    public static final int TYPE_USER_REGISTER_BIND_CODE = 2; 注册用户，已绑定注册码
//    public static final int TYPE_USER_FACUTY = 3; 无敌权限的专家

    /** 发帖时的权限检测 **/
    public static final int TYPE_MAKE_POST = 1;
    /** 专家秘书的权限检测 **/
    public static final int TYPE_SECRETARY = 2;
    /** 壁报图片的权限检测 **/
    public static final int TYPE_POSTER_IMAGE = 3;

    private static final int AUTHORITY_SUCCESS = 0x1000; //权限通过
    private static final int AUTHORITY_FAIL_LOGIN = 0x1001; //权限失败，需要登录
    private static final int AUTHORITY_FAIL_PROFESSOR_ONLY = 0x1002; //权限失败，用户类型较低，需要专家级别，也就是type=3

    public static int checkAuthority(int type) {
        int userType = AppApplication.userType;
        if(type == TYPE_MAKE_POST) {
            //发帖 权限判断
            if (userType == Constants.TYPE_USER_VISITOR) {
                return AUTHORITY_SUCCESS;
            }

            return AUTHORITY_FAIL_LOGIN;
        } else if(type == TYPE_SECRETARY) {
            //专家秘书 权限判断
            if(userType == Constants.TYPE_USER_VISITOR) {
                return AUTHORITY_FAIL_LOGIN;
            }else if(userType != Constants.TYPE_USER_FACUTY) {
                return AUTHORITY_FAIL_PROFESSOR_ONLY;
            }

            return AUTHORITY_SUCCESS;
        } else if(type == TYPE_POSTER_IMAGE) {
            //壁报图片 权限检测
            if (userType == Constants.TYPE_USER_VISITOR) {
                return AUTHORITY_FAIL_LOGIN;
            }
            return AUTHORITY_SUCCESS;
        }

        //默认需要登录才可以操作
        return AUTHORITY_SUCCESS;
    }
}
