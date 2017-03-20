package com.xiaoxian.trade.mvp.model;

/**
 * GridView中Item图标实体
 */

public class Icon {
    public int iconRes;
    public String name;

    public Icon(int iconRes, String name) {
        this.iconRes = iconRes;
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
