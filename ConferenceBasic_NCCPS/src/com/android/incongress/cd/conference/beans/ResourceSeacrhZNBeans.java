package com.android.incongress.cd.conference.beans;

import java.util.List;

/**
 * Created by Admin on 2017/7/22.
 */

public class ResourceSeacrhZNBeans {

    /**
     * state : 1
     * jsonArray : [{"dataId":5009,"title":"原发性肺癌诊疗指南","enTitle":"PRIMARY LUNG CANCER","qici":"2017.V1","color":"#193269","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5010,"title":"原发性肺癌诊疗指南","enTitle":"PRIMARY LUNG CANCER","qici":"2017.V1","color":"#193269","typeField":"解读","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5011,"title":"原发性胃癌诊疗指南","enTitle":"PRIMARY GASTRIC CANCER","qici":"2017.V1","color":"#0B6038","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5012,"title":"乳腺癌诊疗指南","enTitle":"BREAST CANCER","qici":"2017.V1","color":"#4c2a69","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5013,"title":"黑色素瘤诊疗指南","enTitle":"MELANOMA","qici":"2017.V1","color":"#f25c19","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5014,"title":"结直肠癌诊疗指南","enTitle":"COLORECTAL CANCER","qici":"2017.V1","color":"#bf1a2c","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"},{"dataId":5015,"title":"原发性肺癌诊疗指南","enTitle":"PRIMARY LUNG CANCER","qici":"2016.V1","color":"#193269","typeField":"指南","pdfUrl":"http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf","cnSubject":"中国临床肿瘤学会 (CSCO)","enSubject":"Guidelines of Chinese Society of Clinical Oncology CSCO"}]
     */

    private int state;
    private List<JsonArrayBean> jsonArray;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<JsonArrayBean> getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(List<JsonArrayBean> jsonArray) {
        this.jsonArray = jsonArray;
    }

    public static class JsonArrayBean {
        /**
         * dataId : 5009
         * title : 原发性肺癌诊疗指南
         * enTitle : PRIMARY LUNG CANCER
         * qici : 2017.V1
         * color : #193269
         * typeField : 指南
         * pdfUrl : http://114.80.201.49:80//files/pdf/CSCOfeiaijiedu_vip.pdf
         * cnSubject : 中国临床肿瘤学会 (CSCO)
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
}
