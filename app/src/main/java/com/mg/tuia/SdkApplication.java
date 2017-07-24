package com.mg.tuia;

import android.app.Application;

import com.db.ta.sdk.TaSDK;


public class SdkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TaSDK.init(this);
    }
}