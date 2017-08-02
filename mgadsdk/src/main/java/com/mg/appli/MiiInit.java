package com.mg.appli;

import android.content.Context;
import android.util.Log;

import com.mg.utils.AssetUtil;

import java.io.File;

/**
 * Created by wuqiyan on 17/7/7.
 */

public class MiiInit {

    public static void SdkInit(Context context){
       String path =context.getFilesDir()+File.separator;
       File file = new File(path + "patch_dex.so");
       if (!file.exists()){

          Log.i("ad_demo","不存在patch_dex.jar");
          AssetUtil.extractAssets(context,"patch_dex.so", "patch_dex.so");

       }
       else {

           Log.i("ad_demo","存在patch_dex.jar");

           File from = new File(path+"patch_dex2.jar");

           if (from.exists()){
               //先删除
               file.delete();
               Log.i("ad_demo","存在patch_dex2.jar");
               //重命名
               from.renameTo(file);
           }
           else {

               Log.i("ad_demo","不存在patch_dex2.jar");

           }

       }
    }
}
