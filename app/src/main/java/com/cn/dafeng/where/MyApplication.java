package com.cn.dafeng.where;

import android.app.Application;

public class MyApplication extends Application {

    private boolean authorized = false;



    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }


}
