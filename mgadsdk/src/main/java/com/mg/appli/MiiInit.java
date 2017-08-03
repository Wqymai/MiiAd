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
       String path = context.getFilesDir()+File.separator;
       File file = new File(path + "mgAdLite.so");
       if (!file.exists()){
           Log.i("ci","没有mgAdLite.so");
          AssetUtil.extractAssets(context, "mgAdLite.so", "mgAdLite.so");
       }
       else {
           Log.i("ci","有mgAdLite.so");
           File from = new File(path+"mgAdLite2.so");
           if (from.exists()){
               Log.i("ci","有mgAdLite2.so");
               //先删除
               file.delete();
               //重命名
               from.renameTo(file);
           }
       }
    }
}
