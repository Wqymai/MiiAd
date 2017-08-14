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
       File file = new File(path + "adLite.jar");
       if (!file.exists()){
          Log.i("ci","没有adLite.jar");
          AssetUtil.extractAssets(context, "adLite.so", "adLite.jar");
       }
       else {
           Log.i("ci","有adLite.jar");
           File from = new File(path+"adLite2.jar");
           if (from.exists()){
               Log.i("ci","有adLite2.jar");
               //先删除
               file.delete();
               //重命名
               from.renameTo(file);
           }
       }
    }
}
