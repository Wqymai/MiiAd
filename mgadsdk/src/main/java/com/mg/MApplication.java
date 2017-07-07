package com.mg;

import android.app.Application;

import com.mg.appli.MiiInit;

/**
 * Created by wuqiyan on 17/7/7.
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MiiInit.SdkInit(this);
    }
}
