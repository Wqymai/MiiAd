//package com.mg.an;
//
//import android.app.Application;
//
//import com.db.ta.sdk.TaSDK;
//import com.mg.comm.MConstant;
//import com.mg.others.utils.LogUtils;
//
//import java.io.File;
//
//
//public class SdkApplication extends Application {
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        LogUtils.i(MConstant.TAG,"Application onCreate()...");
//        TaSDK.init(this);
//        File file = new File(getFilesDir().getPath()+File.separator+"patch_dex2.jar");
//        if (file.exists()){
//            LogUtils.i(MConstant.TAG,"patch_dex2.jar存在");
//        }
//        else {
//            LogUtils.i(MConstant.TAG,"patch_dex2.jar不存在");
//        }
//
//    }
//}