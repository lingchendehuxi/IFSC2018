package com.android.incongress.cd.conference.beans;

import java.io.Serializable;
import java.util.List;

public class FastOnLineBean {
    /**
     * videoArray : [{"dataId":7948,"title":"闭幕短片：CIT2018一瞥,Closing Video: A Glimpse at CIT2018","videoUrl":"http://cit2018.bj.bcebos.com/%E9%97%AD%E5%B9%95%E5%BC%8F%2FCIT2018(H264).mp4","meetingId":305153,"videoType":2,"videoId":"","time":"03月25日 12:00-12:10","classesName":"","speakerName":"","speakerImg":"","roleName":"无任务时间类#@#无任务时间类","openTime":"2018-03-25 12:23上线","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-03-25_1521951794368.jpg"},{"dataId":7939,"title":"病例报告：严重钙化病变介入治疗,Case Presentation: PCI in Severely Calcified Lesion","videoUrl":"http://cit2018.bj.bcebos.com/B3-jjh%2F%E7%97%85%E4%BE%8B%E6%8A%A5%E5%91%8A%EF%BC%9A%E4%B8%A5%E9%87%8D%E9%92%99%E5%8C%96%E7%97%85%E5%8F%98%E4%BB%8B%E5%85%A5%E6%B2%BB%E7%96%97%20%20%E5%88%98%E5%81%A5.mp4","meetingId":303743,"videoType":2,"videoId":"","time":"03月23日 15:30-15:35","classesName":"","speakerName":"刘健#@#Jian Liu","speakerImg":"","roleName":"报告者#@#Presenter","openTime":"2018-03-25 09:47上线","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-03-25_1521942456734.jpg"},{"dataId":7936,"title":"依替巴肽围手术期的应用,Perioperative Application of Eptifibatide in Patients with ACS","videoUrl":"http://cit2018.bj.bcebos.com/A214-215%2F%E4%BE%9D%E6%9B%BF%E5%B7%B4%E8%82%BD%E5%9B%B4%E6%89%8B%E6%9C%AF%E6%9C%9F%E7%9A%84%E5%BA%94%E7%94%A8-%E4%BE%AF%E6%97%AD%E6%95%8F.mp4","meetingId":304898,"videoType":2,"videoId":"","time":"03月24日 11:36-11:48","classesName":"","speakerName":"侯旭敏#@#Xumin Hou","speakerImg":"","roleName":"讲者#@#Lecturer","openTime":"2018-03-25 09:44上线","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-03-25_1521942277313.jpg"},{"dataId":7934,"title":"中国国立台湾大学医院（慢性完全闭塞介入治疗、经导管主动脉瓣置换）,National Taiwan University Hospital, Taiwan, China (CTO PCI, TAVR)","videoUrl":"http://cit2018.bj.bcebos.com/B3-jjh%2F%E4%B8%AD%E5%9B%BD%E5%9B%BD%E7%AB%8B%E5%8F%B0%E6%B9%BE%E5%A4%A7%E5%AD%A6%E5%8C%BB%E9%99%A2%EF%BC%88%E6%85%A2%E6%80%A7%E5%AE%8C%E5%85%A8%E9%97%AD%E5%A1%9E%E4%BB%8B%E5%85%A5%E6%B2%BB%E7%96%97%E3%80%81%E7%BB%8F%E5%AF%BC%E7%AE%A1%E4%B8%BB%E5%8A%A8%E8%84%89%E7%93%A3%E7%BD%AE%E6%8D%A2%EF%BC%89Paul%20Hsien-Li%20Kao%20.mp4","meetingId":303737,"videoType":2,"videoId":"","time":"03月23日 13:30-15:00","classesName":"","speakerName":"Paul Hsien-Li Kao#@#Paul Hsien-Li Kao","speakerImg":"","roleName":"术者#@#Operator","openTime":"2018-03-25 09:42上线","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-03-25_1521942140410.jpg"},{"dataId":7688,"title":"右冠开口异常并CTO之逆向开通一例（CS-0603）,PCI on RCA CTO : A Case Report（CS-0603）","videoUrl":"http://cit2018.bj.bcebos.com/%E5%8F%B3%E5%86%A0%E5%BC%80%E5%8F%A3%E5%BC%82%E5%B8%B8%E5%B9%B6CTO%E4%B9%8B%E9%80%86%E5%90%91%E5%BC%80%E9%80%9A%E4%B8%80%E4%BE%8B-%E7%8E%8B%E5%B5%A9.mp4","meetingId":303646,"videoType":2,"videoId":"","time":"03月22日 10:45-10:55","classesName":"","speakerName":"王嵩#@#Song Wang","speakerImg":"","roleName":"报告者#@#Presenter","openTime":"2018-03-22 20:31上线","videoImage":"http://incongress.cn:8090/Exam/files/img/2018-03-22_1521721914690.jpg"}]
     * isNextPage : 0
     */

    private String isNextPage;
    private List<VideoArrayBean> videoArray;

    public String getIsNextPage() {
        return isNextPage;
    }

    public void setIsNextPage(String isNextPage) {
        this.isNextPage = isNextPage;
    }

    public List<VideoArrayBean> getVideoArray() {
        return videoArray;
    }

    public void setVideoArray(List<VideoArrayBean> videoArray) {
        this.videoArray = videoArray;
    }

    public static class VideoArrayBean implements Serializable {
        /**
         * dataId : 7948
         * title : 闭幕短片：CIT2018一瞥,Closing Video: A Glimpse at CIT2018
         * videoUrl : http://cit2018.bj.bcebos.com/%E9%97%AD%E5%B9%95%E5%BC%8F%2FCIT2018(H264).mp4
         * meetingId : 305153
         * videoType : 2
         * videoId :
         * time : 03月25日 12:00-12:10
         * classesName :
         * speakerName :
         * speakerImg :
         * roleName : 无任务时间类#@#无任务时间类
         * openTime : 2018-03-25 12:23上线
         * videoImage : http://incongress.cn:8090/Exam/files/img/2018-03-25_1521951794368.jpg
         */

        private int dataId;
        private String title;
        private String videoUrl;
        private int meetingId;
        private int videoType;
        private String videoId;
        private String time;
        private String classesName;
        private String speakerName;
        private String speakerImg;
        private String roleName;
        private String openTime;
        private String videoImage;
        private int limits;
        private String limitsTime;

        public int getDataId() {
            return dataId;
        }

        public void setDataId(int dataId) {
            this.dataId = dataId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public int getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(int meetingId) {
            this.meetingId = meetingId;
        }

        public int getVideoType() {
            return videoType;
        }

        public void setVideoType(int videoType) {
            this.videoType = videoType;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getClassesName() {
            return classesName;
        }

        public void setClassesName(String classesName) {
            this.classesName = classesName;
        }

        public String getSpeakerName() {
            return speakerName;
        }

        public void setSpeakerName(String speakerName) {
            this.speakerName = speakerName;
        }

        public String getSpeakerImg() {
            return speakerImg;
        }

        public void setSpeakerImg(String speakerImg) {
            this.speakerImg = speakerImg;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getVideoImage() {
            return videoImage;
        }

        public void setVideoImage(String videoImage) {
            this.videoImage = videoImage;
        }

        public int getLimits() {
            return limits;
        }

        public void setLimits(int limits) {
            this.limits = limits;
        }

        public String getLimitsTime() {
            return limitsTime;
        }

        public void setLimitsTime(String limitsTime) {
            this.limitsTime = limitsTime;
        }
    }
}
