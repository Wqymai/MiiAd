package com.mg.appli;

import android.content.Context;

import com.mg.d.utils.Uhelper;

/**
 * Created by wuqiyan on 17/7/7.
 */

public class MiiInit {

    public static void SdkInit(Context context){
        Uhelper.extractAssets(context,"patch_dex.so", "patch_dex.jar");
    }
}
