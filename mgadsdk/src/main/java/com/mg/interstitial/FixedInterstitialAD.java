package com.mg.interstitial;

import android.app.Activity;

import com.mg.interf.MiiADListener;
import com.mg.others.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class FixedInterstitialAD {

    public FixedInterstitialAD(Activity activity, boolean isShade, MiiADListener listener){

        MethodDynamicLoad.loadFixedInterstitialADMethod(activity,isShade,listener);
    }

}
