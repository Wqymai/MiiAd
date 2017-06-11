package com.mg.comm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mg.others.model.AdModel;

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
        Log.i("AD_DEMO","service onCreate...");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AD_DEMO","service onStartCommand...");
        AdModel adModel= (AdModel) intent.getSerializableExtra("ad");
        new ADClickHelper(this).apkDownload(adModel);
        return super.onStartCommand(intent, flags, startId);
    }
}
