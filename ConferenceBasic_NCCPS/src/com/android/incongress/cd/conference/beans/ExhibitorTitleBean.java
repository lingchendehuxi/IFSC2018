package com.android.incongress.cd.conference.beans;

import java.util.List;

public class ExhibitorTitleBean {

    /**
     * state : 1
     * menu : [{"index":1,"menuName":"HOT"},{"index":2,"menuName":"展商活动"},{"index":3,"menuName":"参展商"},{"index":4,"menuName":"企业直播"}]
     */

    private int state;
    private List<MenuBean> menu;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<MenuBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuBean> menu) {
        this.menu = menu;
    }

    public static class MenuBean {
        /**
         * index : 1
         * menuName : HOT
         */

        private int index;
        private String menuName;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }
    }
}
