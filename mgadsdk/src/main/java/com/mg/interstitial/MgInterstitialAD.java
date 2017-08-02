package com.mg.interstitial;

import android.app.Activity;

import com.mg.interf.MiiInterADListener;
import com.mg.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgInterstitialAD {

    public MgInterstitialAD(Activity activity, boolean isShade, String appid,String lid, MiiInterADListener listener){

        MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadInterstitialADMethod(activity,isShade,appid,lid,listener);
    }

}
