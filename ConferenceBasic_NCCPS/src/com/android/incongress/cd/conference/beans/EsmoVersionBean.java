package com.android.incongress.cd.conference.beans;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Jacky on 2016/11/24.
 */

public class EsmoVersionBean  extends DataSupport{
    /**
     * version : [{"type":2,"version":2,"zipUrl":"/zip/conferences_194/data2.zip"},{"type":2,"version":3,"zipUrl":"/zip/conferences_194/data1.zip"},{"type":2,"version":4,"zipUrl":"/zip/conferences_194/data4.zip"},{"type":2,"version":5,"zipUrl":"/zip/conferences_194/data5.zip"},{"type":2,"version":6,"zipUrl":"/zip/conferences_194/data6.zip"},{"type":2,"version":7,"zipUrl":"/zip/conferences_194/data7.zip"},{"type":2,"version":8,"zipUrl":"/zip/conferences_194/data8.zip"},{"type":2,"version":9,"zipUrl":"/zip/conferences_194/data9.zip"},{"type":2,"version":10,"zipUrl":"/zip/conferences_194/data10.zip"},{"type":2,"version":11,"zipUrl":"/zip/conferences_194/data1.zip},{"type":2,"version":12,"zipUrl":"/zip/conferences_194/data12.zip"},{"type":2,"version":13,"zipUrl":"/zip/conferences_194/data13.zip"},{"type":2,"version":14,"zipUrl":"/zip/conferences_194/data14.zip"},{"type":2,"version":15,"zipUrl":"/zip/conferences_194/data15.zip"},{"type":2,"version":16,"zipUrl":"/zip/conferences_194/data16.zip"},{"type":2,"version":17,"zipUrl":"/zip/conferences_194/data17.zip"},{"type":2,"version":18,"zipUrl":"/zip/conferences_194/data18.zip"},{"type":2,"version":19,"zipUrl":"/zip/conferences_194/data19.zip"},{"type":2,"version":20,"zipUrl":"/zip/conferences_194/data20.zip"},{"type":2,"version":21,"zipUrl":"/zip/conferences_194/data21.zip"},{"type":2,"version":22,"zipUrl":"/zip/conferences_194/data22.zip"},{"type":2,"version":23,"zipUrl":"/zip/conferences_194/data23.zip"},{"type":2,"version":24,"zipUrl":"/zip/conferences_194/data24.zip"},{"type":2,"version":25,"zipUrl":"/zip/conferences_194/data25.zip"},{"type":2,"version":26,"zipUrl":"/zip/conferences_194/data26.zip"},{"type":2,"version":27,"zipUrl":"/zip/conferences_194/data27.zip"},{"type":2,"version":28,"zipUrl":"/zip/conferences_194/data28.zip"},{"type":2,"version":29,"zipUrl":"/zip/conferences_194/data29.zip"},{"type":2,"version":30,"zipUrl":"/zip/conferences_194/data30.zip"},{"type":2,"version":31,"zipUrl":"/zip/conferences_194/data31.zip"},{"type":2,"version":32,"zipUrl":"/zip/conferences_194/data32.zip"},{"type":2,"version":33,"zipUrl":"/zip/conferences_194/data33.zip"},{"type":2,"version":34,"zipUrl":"/zip/conferences_194/data34.zip"},{"type":2,"version":35,"zipUrl":"/zip/conferences_194/data35.zip"},{"type":2,"version":36,"zipUrl":"/zip/conferences_194/data36.zip"},{"type":2,"version":37,"zipUrl":"/zip/conferences_194/data37.zip"},{"type":2,"version":38,"zipUrl":"/zip/conferences_194/data38.zip"},{"type":2,"version":39,"zipUrl":"/zip/conferences_194/data39.zip"},{"type":2,"version":40,"zipUrl":"/zip/conferences_194/data40.zip"},{"type":2,"version":41,"zipUrl":"/zip/conferences_194/data41.zip"},{"type":2,"version":42,"zipUrl":"/zip/conferences_194/data42.zip"},{"type":2,"version":43,"zipUrl":"/zip/conferences_194/data43.zip"},{"type":2,"version":44,"zipUrl":"/zip/conferences_194/data44.zip"},{"type":2,"version":45,"zipUrl":"/zip/conferences_194/data45.zip"},{"type":2,"version":46,"zipUrl":"/zip/conferences_194/data46.zip"},{"type":2,"version":47,"zipUrl":"/zip/conferences_194/data47.zip"},{"type":2,"version":48,"zipUrl":"/zip/conferences_194/data48.zip"},{"type":2,"version":49,"zipUrl":"/zip/conferences_194/data49.zip"},{"type":2,"version":50,"zipUrl":"/zip/conferences_194/data50.zip"},{"type":2,"version":51,"zipUrl":"/zip/conferences_194/data51.zip"},{"type":2,"version":52,"zipUrl":"/zip/conferences_194/data52.zip"}]
     * showMessage : Data52
     */

    private String showMessage;
    private List<VersionBean> version;

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public List<VersionBean> getVersion() {
        return version;
    }

    public void setVersion(List<VersionBean> version) {
        this.version = version;
    }

    public static class VersionBean {
        /**
         * type : 2
         * version : 2
         * zipUrl : /zip/conferences_194/data2.zip
         */

        private int type;
        private int version;
        private String zipUrl;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getZipUrl() {
            return zipUrl;
        }

        public void setZipUrl(String zipUrl) {
            this.zipUrl = zipUrl;
        }
    }
}
