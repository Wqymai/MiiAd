package com.mg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mg.c.c.a;
import com.mg.c.utils.MethodDynamicLoad;


/**
 * Created by wuqiyan on 17/6/9.
 */

public class LoadHelperService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       try {
           if (intent.getSerializableExtra("ad") == null){
               return START_STICKY;
           }
           a adModel= (a) intent.getSerializableExtra("ad");
           MethodDynamicLoad.getInstance(this).loadApkDownloadMethod(adModel,this);

       }catch (Exception e){
           e.printStackTrace();
       }

        return START_STICKY;
    }
}
