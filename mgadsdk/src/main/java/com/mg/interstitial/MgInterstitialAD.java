package com.mg.interstitial;

import android.app.Activity;

import com.mg.interf.MiiADListener;
import com.mg.utils.MethodLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgInterstitialAD {

    public MgInterstitialAD(Activity activity, boolean isShade, String appid,String lid, MiiADListener listener){

        MethodLoad.getInstance(activity.getApplicationContext()).loadInterstitialADMethod(activity,isShade,appid,lid,listener);
    }

}
