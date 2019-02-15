package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by 13008 on 2019/1/31.
 */

public class BBFiledTypeBean {

    private List<ClassArrayBean> classArray;

    public List<ClassArrayBean> getClassArray() {
        return classArray;
    }

    public void setClassArray(List<ClassArrayBean> classArray) {
        this.classArray = classArray;
    }

    public static class ClassArrayBean {
        /**
         * fieldId : 1363
         * fieldName : 9.Nursing and Technology
         */

        private int fieldId;
        private String fieldName;

        public int getFieldId() {
            return fieldId;
        }

        public void setFieldId(int fieldId) {
            this.fieldId = fieldId;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
    }
}
