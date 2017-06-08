package com.mg.others.ooa;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;




/**
 * Created by wuqiyan on 17/6/7.
 */

public class MiiService extends Service {
    MAdSDK adSDK=MAdSDK.getInstance();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //广告相关
        adSDK.init(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adSDK.startAd(intent,getApplication());
        return START_STICKY;
    }
}
