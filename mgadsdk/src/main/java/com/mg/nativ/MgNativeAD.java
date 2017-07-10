package com.mg.nativ;

import android.app.Activity;

import com.mg.d.utils.MethodDynamicLoad;
import com.mg.interf.MiiNativeListener;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgNativeAD {

    public MgNativeAD(Activity activity,String appid, MiiNativeListener listener){
//        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.so");
//        File dexOutputDir = activity.getDir("dex", 0);
//        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, activity.getClassLoader());
//        Class<?> libProviderClazz = null;
//        try {
//            libProviderClazz = cl.loadClass("com.mg.nativ.MiiNativeAD");
//            Class[] args1 = new Class[2];
//            args1[0] = Activity.class;
//            args1[1] = MiiNativeListener.class;
//
//            Object[] argments = new Object[2];
//            argments[0] = activity;
//            argments[1] = listener;
//            Constructor c = libProviderClazz.getConstructor(args1);
//            c.setAccessible(true);
//            c.newInstance(argments);
//
//        } catch (Exception exception) {
//
//            exception.printStackTrace();
//        }
        MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadNativeADMethod(activity,appid,listener);
    }
}
