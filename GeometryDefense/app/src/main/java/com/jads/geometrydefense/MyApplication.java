package com.jads.geometrydefense;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context mContext;
    private static boolean debug = false;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    public static boolean debugging() {
        return debug;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
    }
}