package com.xiaoxian.trade.mvp.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Sort extends BmobObject {
    private String kind;
    private BmobFile file1;
    private BmobFile file2;
    private BmobFile file3;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public BmobFile getFile1() {
        return file1;
    }

    public void setFile1(BmobFile file1) {
        this.file1 = file1;
    }

    public BmobFile getFile2() {
        return file2;
    }

    public void setFile2(BmobFile file2) {
        this.file2 = file2;
    }

    public BmobFile getFile3() {
        return file3;
    }

    public void setFile3(BmobFile file3) {
        this.file3 = file3;
    }
}
