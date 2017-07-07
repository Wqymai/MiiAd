package com.mg.interstitial;

import android.app.Activity;

import com.mg.interf.MiiADListener;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class FixedInterstitialAD {

    public FixedInterstitialAD(Activity activity, boolean isShade, MiiADListener listener){

        MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadFixedInterstitialADMethod(activity,isShade,listener);
    }

}
