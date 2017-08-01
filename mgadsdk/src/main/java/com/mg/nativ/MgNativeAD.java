package com.mg.nativ;

import android.app.Activity;

import com.mg.c.utils.MethodDynamicLoad;
import com.mg.interf.MiiNativeListener;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgNativeAD {

    public MgNativeAD(Activity activity,String appid,String lid, MiiNativeListener listener){

        MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadNativeADMethod(activity,appid,lid,listener);
    }
}
