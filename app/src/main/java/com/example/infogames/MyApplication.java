package com.example.infogames;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Data.initInstance();
    }
}
