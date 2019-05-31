package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Admin on 2017/7/21.
 */

public class ResourceGuideBeans {


    /**
     * msg : 特此说明：此指南版权由CSCO所拥有，禁止未经许可随意盗用指南内容，必要时我们将诉讼法律武器维权
     * state : 1
     * zhiNanList : [{"dataId":5009,"title":"原发性肺癌诊疗指南","enTitle":"PRIMARY LUNG CANCER","qici":"2017.V1","color":"#193269","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5010,"title":"原发性肺癌诊疗指南","enTitle":"PRIMARY LUNG CANCER","qici":"2017.V1","color":"#193269","typeField":"解读","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5011,"title":"原发性胃癌诊疗指南","enTitle":"PRIMARY GASTRIC CANCER","qici":"2017.V1","color":"#0B6038","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5012,"title":"乳腺癌诊疗指南","enTitle":"BREAST CANCER","qici":"2017.V1","color":"#4c2a69","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5013,"title":"黑色素瘤诊疗指南","enTitle":"MELANOMA","qici":"2017.V1","color":"#f25c19","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5014,"title":"结直肠癌诊疗指南","enTitle":"COLORECTAL CANCER","qici":"2017.V1","color":"#bf1a2c","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5015,"title":"原发性肺癌诊疗指南","enTitle":"PRIMARY LUNG CANCER","qici":"2016.V1","color":"#193269","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"指南中文项目名","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"}]
     * diaoChaList : [{"dataId":5016,"title":"CSCO结直肠癌诊疗指南(2017 V1)读者意见反馈表","imgUrl":"http://114.80.201.49:80//files/conpass_1/fankuibiao.png","linkUrl":"https://sojump.com/jq/14461329.aspx","width":520,"height":177}]
     */

    private String msg;
    private int state;
    private List<ZhiNanListBean> zhiNanList;
    private List<DiaoChaListBean> diaoChaList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<ZhiNanListBean> getZhiNanList() {
        return zhiNanList;
    }

    public void setZhiNanList(List<ZhiNanListBean> zhiNanList) {
        this.zhiNanList = zhiNanList;
    }

    public List<DiaoChaListBean> getDiaoChaList() {
        return diaoChaList;
    }

    public void setDiaoChaList(List<DiaoChaListBean> diaoChaList) {
        this.diaoChaList = diaoChaList;
    }

    public static class ZhiNanListBean {
        /**
         * dataId : 5009
         * title : 原发性肺癌诊疗指南
         * enTitle : PRIMARY LUNG CANCER
         * qici : 2017.V1
         * color : #193269
         * typeField : 指南
         * pdfUrl : http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf
         * cnSubject : 指南中文项目名
         * enSubject : Guidelines of Chinese Society of Clinical Oncology CSCO
         */

        private int dataId;
        private String title;
        private String enTitle;
        private String qici;
        private String color;
        private String typeField;
        private String pdfUrl;
        private String cnSubject;
        private String enSubject;

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

        public String getEnTitle() {
            return enTitle;
        }

        public void setEnTitle(String enTitle) {
            this.enTitle = enTitle;
        }

        public String getQici() {
            return qici;
        }

        public void setQici(String qici) {
            this.qici = qici;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getTypeField() {
            return typeField;
        }

        public void setTypeField(String typeField) {
            this.typeField = typeField;
        }

        public String getPdfUrl() {
            return pdfUrl;
        }

        public void setPdfUrl(String pdfUrl) {
            this.pdfUrl = pdfUrl;
        }

        public String getCnSubject() {
            return cnSubject;
        }

        public void setCnSubject(String cnSubject) {
            this.cnSubject = cnSubject;
        }

        public String getEnSubject() {
            return enSubject;
        }

        public void setEnSubject(String enSubject) {
            this.enSubject = enSubject;
        }
    }

    public static class DiaoChaListBean {
        /**
         * dataId : 5016
         * title : CSCO结直肠癌诊疗指南(2017 V1)读者意见反馈表
         * imgUrl : http://114.80.201.49:80//files/conpass_1/fankuibiao.png
         * linkUrl : https://sojump.com/jq/14461329.aspx
         * width : 520
         * height : 177
         */

        private int dataId;
        private String title;
        private String imgUrl;
        private String linkUrl;
        private int width;
        private int height;

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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLinkUrl() {
            return linkUrl;
        }

        public void setLinkUrl(String linkUrl) {
            this.linkUrl = linkUrl;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
