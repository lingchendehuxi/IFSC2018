package com.android.incongress.cd.conference.beans;

import java.util.List;

public class CollegeListDetailBean {


    private String state;
    private List<ClassArrayBean> classArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ClassArrayBean> getClassArray() {
        return classArray;
    }

    public void setClassArray(List<ClassArrayBean> classArray) {
        this.classArray = classArray;
    }

    public static class ClassArrayBean {

        private String classId;
        private String className;
        private List<SessionArrayBean> sessionArray;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<SessionArrayBean> getSessionArray() {
            return sessionArray;
        }

        public void setSessionArray(List<SessionArrayBean> sessionArray) {
            this.sessionArray = sessionArray;
        }

        public static class SessionArrayBean {
            /**
             * sessionId : 46642
             * sessionName : �ھŽ��ٴ��о�ר�����ַ���һ���� ��һ�����ٴ��о�׿Խ֮· - ������Ϊһ���о�����#@#The 9th Clinical Research Workshop Part I Session I: Road to Excellence in Clinical Research - How to Become a Premier Research Center
             */
            private String classId;
            private String sessionId;
            private String sessionName;

            public String getClassId() {
                return classId;
            }

            public void setClassId(String classId) {
                this.classId = classId;
            }

            public String getSessionId() {
                return sessionId;
            }

            public void setSessionId(String sessionId) {
                this.sessionId = sessionId;
            }

            public String getSessionName() {
                return sessionName;
            }

            public void setSessionName(String sessionName) {
                this.sessionName = sessionName;
            }
        }
    }
}
