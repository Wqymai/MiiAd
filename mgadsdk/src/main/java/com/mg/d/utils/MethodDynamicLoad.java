package com.mg.d.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mg.d.c.DynamicModel;
import com.mg.d.c.a;
import com.mg.interf.MiiADListener;
import com.mg.interf.MiiNativeListener;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MethodDynamicLoad {



    private static volatile MethodDynamicLoad instance = null;
    Context context;
    File optimizedDexOutputPath;
    File dexOutputDir;
    DexClassLoader cl;

    private MethodDynamicLoad(Context context){
         this.context = context;
//         optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.so");
         optimizedDexOutputPath = new File(context.getFilesDir()+File.separator+ "patch_dex.jar");
         dexOutputDir = context.getDir("dex", 0);
         cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, context.getClassLoader());
    }
    public static MethodDynamicLoad getInstance(Context context) {
        if (instance == null) {
            synchronized (MethodDynamicLoad.class) {
                if (instance == null) {
                    instance = new MethodDynamicLoad(context);
                }
            }
        }
        return instance;
    }

    public  void loadReportMethod(a adModel, int type, Context context){

        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.d.b.d");
            Class[] method_class = new Class[3];
            method_class[0] = a.class;
            method_class[1] = int.class;
            method_class[2] = Context.class;
            Method method = libProviderClazz.getDeclaredMethod("a", method_class);
            method.setAccessible(true);
            Object[] method_arg = new Object[3];
            method_arg[0] = adModel;
            method_arg[1] = type;
            method_arg[2] = context;
            method.invoke(null, method_arg);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public  void loadApkDownloadMethod(a adModel, Context context){

        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.b.a");
            Class[] struct_class = new Class[1];
            struct_class[0] = Context.class;
            Constructor c = libProviderClazz.getConstructor(struct_class);
            Object[] struct_args = new Object[1];
            struct_args[0] = context;


            Class[] method_class = new Class[1];
            method_class[0] = a.class;
            Method method = libProviderClazz.getDeclaredMethod("apkDownload", method_class);
            method.setAccessible(true);
            Object[] method_arg = new Object[1];
            method_arg[0] = adModel;
            method.invoke(c.newInstance(struct_args), method_arg);

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }
    public  DynamicModel loadSplashADMethod(Activity activity, ViewGroup adContainer, View skipContainer,String appid, MiiADListener adListener){

        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.splash.MiiSplashAD");
            Class[] args1 = new Class[5];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = View.class;
            args1[3] = String.class;
            args1[4] = MiiADListener.class;

            Object[] argments = new Object[5];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = skipContainer;
            argments[3] = appid;
            argments[4] = adListener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            object = c.newInstance(argments);
            model.object = object;
            model.aClass = libProviderClazz;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return model;

    }
    public  void loadFixedInterstitialADMethod(Activity activity, boolean isShade,String appid, MiiADListener listener){

        Class<?> libProviderClazz = null;

        try {
            libProviderClazz = cl.loadClass("com.mg.interstitial.MiiFixedInterstitialAD");
            Class[] args1 = new Class[4];
            args1[0] = Activity.class;
            args1[1] = boolean.class;
            args1[2] = String.class;
            args1[3] = MiiADListener.class;

            Object[] argments = new Object[4];
            argments[0] = activity;
            argments[1] = isShade;
            argments[2] = appid;
            argments[3] = listener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            c.newInstance(argments);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public  DynamicModel loadAutoInterstitialADMethod(Activity activity, ViewGroup adContainer,String appid, MiiADListener listener){

        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.interstitial.MiiAutoInterstitialAD");
            Class[] args1 = new Class[4];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = String.class;
            args1[3] = MiiADListener.class;

            Object[] argments = new Object[4];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = appid;
            argments[3] = listener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            object = c.newInstance(argments);
            model.aClass = libProviderClazz;
            model.object = object;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return model;
    }

    public  DynamicModel loadBannerADMethod(Activity activity, ViewGroup adContainer,String appid, MiiADListener listener){

        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.banner.MiiBannerAD");
            Class[] args1 = new Class[4];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = String.class;
            args1[3] = MiiADListener.class;

            Object[] argments = new Object[4];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = appid;
            argments[3] = listener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            object = c.newInstance(argments);
            model.aClass = libProviderClazz;
            model.object = object;

        } catch (Exception exception) {

            exception.printStackTrace();
        }
        return model;
    }

    public  void loadAdClickMethod(Context context,a ad){

        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.b.a");
            Class[] struct_class = new Class[1];
            struct_class[0] = Context.class;
            Constructor c = libProviderClazz.getConstructor(struct_class);
            Object[] struct_args = new Object[1];
            struct_args[0] = context;


            Class[] method_class = new Class[1];
            method_class[0] = a.class;
            Method method = libProviderClazz.getDeclaredMethod("a", method_class);
            method.setAccessible(true);
            Object[] method_arg = new Object[1];
            method_arg[0] = ad;
            method.invoke(c.newInstance(struct_args), method_arg);

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public void loadNativeADMethod(Activity activity,String appid,MiiNativeListener listener){

        Class<?> libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.nativ.MiiNativeAD");
            Class[] args1 = new Class[3];
            args1[0] = Activity.class;
            args1[1] = String.class;
            args1[2] = MiiNativeListener.class;

            Object[] argments = new Object[3];
            argments[0] = activity;
            argments[1] = appid;
            argments[2] = listener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            c.newInstance(argments);

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public  void loadRecycleMethod(DynamicModel model){
        try {
            Method method = model.aClass.getDeclaredMethod("recycle", new Class[]{});
            method.setAccessible(true);
            method.invoke(model.object, new Object[]{});

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }


}
