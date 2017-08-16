package com.mg.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.interf.MiiNativeListener;
import com.mg.others.model.AdModel;
import com.mg.others.model.DynamicModel;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MethodLoad {



    private static volatile MethodLoad instance = null;
    Context context;
    File optimizedDexOutputPath;
    File dexOutputDir;
    DexClassLoader cl;

    private MethodLoad(Context context){
         this.context = context;
//         optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "mgAdLite.so");
         optimizedDexOutputPath = new File(context.getFilesDir()+File.separator+ "adLite.so");
         dexOutputDir = context.getDir("dex", 0);
         cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, context.getClassLoader());
    }
    public static MethodLoad getInstance(Context context) {
        if (instance == null) {
            synchronized (MethodLoad.class) {
                if (instance == null) {
                    instance = new MethodLoad(context);
                }
            }
        }
        return instance;
    }

    public  void loadReportMethod(AdModel adModel, int type, Context context){

        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.others.manager.HttpManager");
            Class[] method_class = new Class[3];
            method_class[0] = AdModel.class;
            method_class[1] = int.class;
            method_class[2] = Context.class;
            Method method = libProviderClazz.getDeclaredMethod("reportEvent", method_class);
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
    public  void loadApkDownloadMethod(AdModel adModel, Context context){

        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.comm.ADClickHelper");
            Class[] struct_class = new Class[1];
            struct_class[0] = Context.class;
            Constructor c = libProviderClazz.getConstructor(struct_class);
            Object[] struct_args = new Object[1];
            struct_args[0] = context;


            Class[] method_class = new Class[1];
            method_class[0] = AdModel.class;
            Method method = libProviderClazz.getDeclaredMethod("apkDownload", method_class);
            method.setAccessible(true);
            Object[] method_arg = new Object[1];
            method_arg[0] = adModel;
            method.invoke(c.newInstance(struct_args), method_arg);

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }
    public  DynamicModel loadSplashADMethod(Activity activity, ViewGroup adContainer, View skipContainer,String appid,String lid, MiiADListener adListener){

        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.splash.MiiSplashAD");
            Class[] args1 = new Class[6];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = View.class;
            args1[3] = String.class;
            args1[4] = String.class;
            args1[5] = MiiADListener.class;

            Object[] argments = new Object[6];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = skipContainer;
            argments[3] = appid;
            argments[4] = lid;
            argments[5] = adListener;
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
    public  void loadInterstitialADMethod(Activity activity, boolean isShade,String appid,String lid, MiiADListener listener){

        Class<?> libProviderClazz = null;

        try {
            libProviderClazz = cl.loadClass("com.mg.interstitial.MiiInterstitialAD");
            Class[] args1 = new Class[5];
            args1[0] = Activity.class;
            args1[1] = boolean.class;
            args1[2] = String.class;
            args1[3] = String.class;
            args1[4] = MiiADListener.class;

            Object[] argments = new Object[5];
            argments[0] = activity;
            argments[1] = isShade;
            argments[2] = appid;
            argments[3] = lid;
            argments[4] = listener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            c.newInstance(argments);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }



    public  DynamicModel loadBannerADMethod(Activity activity, ViewGroup adContainer,String appid,String lid, MiiADListener listener){

        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.banner.MiiBannerAD");
            Class[] args1 = new Class[5];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = String.class;
            args1[3] = String.class;
            args1[4] = MiiADListener.class;

            Object[] argments = new Object[5];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = appid;
            argments[3] = lid;
            argments[4] = listener;
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



    public  void loadAdClickMethod(Context context,AdModel ad){

        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.comm.ADClickHelper");
            Class[] struct_class = new Class[1];
            struct_class[0] = Context.class;
            Constructor c = libProviderClazz.getConstructor(struct_class);
            Object[] struct_args = new Object[1];
            struct_args[0] = context;


            Class[] method_class = new Class[1];
            method_class[0] = AdModel.class;
            Method method = libProviderClazz.getDeclaredMethod("AdClick", method_class);
            method.setAccessible(true);
            Object[] method_arg = new Object[1];
            method_arg[0] = ad;
            method.invoke(c.newInstance(struct_args), method_arg);

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public void loadNativeADMethod(Activity activity,String appid,String lid,MiiNativeListener listener){

        Class<?> libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.nativ.MiiNativeAD");
            Class[] args1 = new Class[4];
            args1[0] = Activity.class;
            args1[1] = String.class;
            args1[2] = String.class;
            args1[3] = MiiNativeListener.class;

            Object[] argments = new Object[4];
            argments[0] = activity;
            argments[1] = appid;
            argments[2] = lid;
            argments[3] = listener;
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

    public void loadBanner(DynamicModel model){
        try {
            Method method = model.aClass.getDeclaredMethod("loadBannerAD", new Class[]{});
            method.setAccessible(true);
            method.invoke(model.object, new Object[]{});

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }


    public DynamicModel loadDobberADMethod(Activity activity, String appid, String lid, MiiADListener listener) {
        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.dobber.MiiDobberAD");
            Class[] args1 = new Class[4];
            args1[0] = Activity.class;
            args1[1] = String.class;
            args1[2] = String.class;
            args1[3] = MiiADListener.class;

            Object[] argments = new Object[4];
            argments[0] = activity;
            argments[1] = appid;
            argments[2] = lid;
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

    public void loadDobber(DynamicModel model) {
        try {
            Method method = model.aClass.getDeclaredMethod("loadDobberAD", new Class[]{});
            method.setAccessible(true);
            method.invoke(model.object, new Object[]{});

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public DynamicModel loadHeadupADMethod(Activity activity, boolean isTop, String appid, String lid,
                                   MiiADListener listener) {
        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.headup.MiiHeadupAD");
            Class[] args1 = new Class[5];
            args1[0] = Activity.class;
            args1[1] = boolean.class;
            args1[2] = String.class;
            args1[3] = String.class;
            args1[4] = MiiADListener.class;

            Object[] argments = new Object[5];
            argments[0] = activity;
            argments[1] =isTop;
            argments[2] = appid;
            argments[3] = lid;
            argments[4] = listener;
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

    public void loadHeadup(DynamicModel model) {
        try {
            Method method = model.aClass.getDeclaredMethod("loadHeadupAD", new Class[]{});
            method.setAccessible(true);
            method.invoke(model.object, new Object[]{});

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }
}
