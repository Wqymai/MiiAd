//package com.mg.others;
//
//import android.app.Application;
//import android.util.Log;
//
//import com.mg.others.ooa.ActivityThreadHooker;
//
///**
// * Created by wuqiyan on 17/6/8.
// */
//
//public class MApplication extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.i("ci","application...");
//        try {
//            new ActivityThreadHooker();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//    }
//}
