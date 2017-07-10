package com.mg.interstitial;

import android.app.Activity;

import com.mg.interf.MiiADListener;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgFixedInterstitialAD {

    public MgFixedInterstitialAD(Activity activity, boolean isShade,String appid, MiiADListener listener){

        MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadFixedInterstitialADMethod(activity,isShade,appid,listener);
    }

}
