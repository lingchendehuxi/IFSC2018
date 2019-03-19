package com.android.incongress.cd.conference.beans;

import java.util.List;

public class LiveClassBean {

    private List<ClassArrayBean> classArray;

    public List<ClassArrayBean> getClassArray() {
        return classArray;
    }

    public void setClassArray(List<ClassArrayBean> classArray) {
        this.classArray = classArray;
    }

    public static class ClassArrayBean {
        /**
         * className : Room1#@#Room1
         * classId : 9253
         */

        private String className;
        private int classId;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }
    }
}
