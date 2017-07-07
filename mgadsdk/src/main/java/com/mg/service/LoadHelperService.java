package com.mg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mg.others.model.AdModel;
import com.mg.others.utils.MethodDynamicLoad;


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
           AdModel adModel= (AdModel) intent.getSerializableExtra("ad");
           MethodDynamicLoad.loadApkDownloadMethod(adModel,this);
//           File optimizedDexOutputPath = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "patch_dex.jar");
//           File dexOutputDir = getDir("dex", 0);
//           DexClassLoader cl = new DexClassLoader(optimizedDexOutputPath.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null, getClassLoader());
//           Class libProviderClazz = null;
//           try {
//               libProviderClazz = cl.loadClass("com.mg.others.comm.ADClickHelper");
//               Class[] struct_class = new Class[1];
//               struct_class[0] = Context.class;
//               Constructor c = libProviderClazz.getConstructor(struct_class);
//               Object[] struct_args = new Object[1];
//               struct_args[0] = this;
//
//
//               Class[] method_class = new Class[1];
//               method_class[0] = AdModel.class;
//               Method method = libProviderClazz.getDeclaredMethod("apkDownload", method_class);
//               method.setAccessible(true);
//               Object[] method_arg = new Object[1];
//               method_arg[0] = adModel;
//               method.invoke(c.newInstance(struct_args), method_arg);
//
//           } catch (Exception exception) {
//
//               exception.printStackTrace();
//           }
       }catch (Exception e){
           e.printStackTrace();
       }

        return START_STICKY;
    }
}
