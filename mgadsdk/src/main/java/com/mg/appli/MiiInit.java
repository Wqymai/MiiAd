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
       File file = new File(path + "adLite.so");
       if (!file.exists()){
           Log.i("ci","没有adLite.so");
          AssetUtil.extractAssets(context, "adLite.so", "adLite.so");
       }
       else {
           Log.i("ci","有adLite.so");
           File from = new File(path+"adLite.so");
           if (from.exists()){
               Log.i("ci","有adLite.so");
               //先删除
               file.delete();
               //重命名
               from.renameTo(file);
           }
       }
    }
}
