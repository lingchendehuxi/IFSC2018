package com.android.incongress.cd.conference.beans;

import org.litepal.crud.DataSupport;

/**
 * Created by Jacky on 2016/12/7 0007.
 * 我的领域字段
 */

public class MyFieldBean extends DataSupport {
    private String fieldName;
    private int fieldState;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int isFieldState() {
        return fieldState;
    }

    public void setFieldState(int fieldState) {
        this.fieldState = fieldState;
    }
}
