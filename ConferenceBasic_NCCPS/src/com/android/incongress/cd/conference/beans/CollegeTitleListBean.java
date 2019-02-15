package com.android.incongress.cd.conference.beans;

import java.util.List;

public class CollegeTitleListBean {

    /**
     * itemArray : [{"itemId":"52-1","itemName":"��������"},{"itemId":52,"itemName":"CIT2017"}]
     * state : 1
     */

    private String state;
    private List<ItemArrayBean> itemArray;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ItemArrayBean> getItemArray() {
        return itemArray;
    }

    public void setItemArray(List<ItemArrayBean> itemArray) {
        this.itemArray = itemArray;
    }

    public static class ItemArrayBean {
        /**
         * itemId : 52-1
         * itemName : ��������
         */

        private String itemId;
        private String itemName;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }
}
