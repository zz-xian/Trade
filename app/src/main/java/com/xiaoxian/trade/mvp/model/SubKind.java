package com.xiaoxian.trade.mvp.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class SubKind extends BmobObject {
    private Kind kindId;
    private String subKind;

    public Kind getKindId() {
        return kindId;
    }

    public void setKindId(Kind kindId) {
        this.kindId = kindId;
    }

    public String getSubKind() {
        return subKind;
    }

    public void setSubKind(String subKind) {
        this.subKind = subKind;
    }
}
