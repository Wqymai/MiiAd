package com.mg.others.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.others.model.AdModel;
import com.mg.others.model.DynamicModel;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MethodDynamicLoad {

    public static void loadReportMethod(AdModel adModel, int type, Context context){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = context.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, context.getClassLoader());
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
    public static void loadApkDownloadMethod(AdModel adModel, Context context){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = context.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, context.getClassLoader());
        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.others.comm.ADClickHelper");
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
    public static void loadSplashADMethod(Activity activity, ViewGroup adContainer, View skipContainer, MiiADListener adListener){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = activity.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, activity.getClassLoader());
        Class<?> libProviderClazz = null;

        try {
            libProviderClazz = cl.loadClass("com.mg.splash.MiiSplashAD");
            Class[] args1 = new Class[4];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = View.class;
            args1[3] = MiiADListener.class;

            Object[] argments = new Object[4];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = skipContainer;
            argments[3] = adListener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            c.newInstance(argments);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    public static void loadFixedInterstitialADMethod(Activity activity, boolean isShade, MiiADListener listener){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = activity.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, activity.getClassLoader());
        Class<?> libProviderClazz = null;

        try {
            libProviderClazz = cl.loadClass("com.mg.Interstitial.MiiFixedInterstitialAD");
            Class[] args1 = new Class[3];
            args1[0] = Activity.class;
            args1[1] = boolean.class;
            args1[2] = MiiADListener.class;

            Object[] argments = new Object[3];
            argments[0] = activity;
            argments[1] = isShade;
            argments[2] = listener;
            Constructor c = libProviderClazz.getConstructor(args1);
            c.setAccessible(true);
            c.newInstance(argments);
        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }

    public static DynamicModel loadAutoInterstitialADMethod(Activity activity, ViewGroup adContainer, MiiADListener listener){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = activity.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, activity.getClassLoader());
        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.Interstitial.MiiAutoInterstitialAD");
            Class[] args1 = new Class[3];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = MiiADListener.class;

            Object[] argments = new Object[3];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = listener;
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

    public static DynamicModel loadBannerADMethod(Activity activity, ViewGroup adContainer, MiiADListener listener){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = activity.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, activity.getClassLoader());
        Class<?> libProviderClazz = null;
        Object object = null;
        DynamicModel model = new DynamicModel();
        try {
            libProviderClazz = cl.loadClass("com.mg.banner.MiiBannerAD");
            Class[] args1 = new Class[3];
            args1[0] = Activity.class;
            args1[1] = ViewGroup.class;
            args1[2] = MiiADListener.class;

            Object[] argments = new Object[3];
            argments[0] = activity;
            argments[1] = adContainer;
            argments[2] = listener;
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

    public static void loadAdClickMethod(Context context,AdModel ad){
        File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
        File dexOutputDir = context.getDir("dex", 0);
        DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, context.getClassLoader());
        Class libProviderClazz = null;
        try {
            libProviderClazz = cl.loadClass("com.mg.others.comm.ADClickHelper");
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

    public static void loadRecycleMethod(DynamicModel model){
        try {
            Method method = model.aClass.getDeclaredMethod("recycle", new Class[]{});
            method.setAccessible(true);
            method.invoke(model.object, new Object[]{});

        } catch (Exception exception) {

            exception.printStackTrace();
        }
    }


}
