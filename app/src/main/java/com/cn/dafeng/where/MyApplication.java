package com.cn.dafeng.where;

import android.app.Application;

public class MyApplication extends Application {

    private boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
