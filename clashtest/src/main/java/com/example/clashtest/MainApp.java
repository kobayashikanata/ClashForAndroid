package com.example.clashtest;

import android.app.Application;

import com.github.kr328.clash.common.Global;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Global.INSTANCE.init(this);
    }
}
