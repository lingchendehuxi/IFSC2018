package com.android.incongress.cd.conference.beans;

/**
 * Created by Admin on 2018/9/12.
 */

public class UserInfoEnBean {

    /**
     * name : yw g
     * userType : 1
     * userId : 184047
     * email : 1169833729@qq.com
     * img :
     * state : 1
     * msg :
     */

    private String name;
    private int userType;
    private int userId;
    private String email;
    private String img;
    private int state;
    private String msg;
    /**
     * facultyId : -1
     * mobilePhone :
     */

    private int facultyId;
    private String mobilePhone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
