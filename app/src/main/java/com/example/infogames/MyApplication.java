package com.example.infogames;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Data.initInstance();
    }

}
