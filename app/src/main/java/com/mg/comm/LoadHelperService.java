//package com.mg.comm;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//import com.mg.others.model.AdModel;
//
///**
// * Created by wuqiyan on 17/6/9.
// */
//
//public class LoadHelperService extends Service {
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    @Override
//    public void onCreate() {
//
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//       try {
//           if (intent.getSerializableExtra("ad") == null){
//               return START_STICKY;
//           }
//           AdModel adModel= (AdModel) intent.getSerializableExtra("ad");
//           new ADClickHelper(this).apkDownload(adModel);
//
//       }catch (Exception e){
//           e.printStackTrace();
//       }
//
//        return START_STICKY;
//    }
//}
