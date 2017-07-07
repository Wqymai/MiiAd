package com.mg.interstitial;

import android.app.Activity;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.d.c.DynamicModel;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class AutoInterstitialAD {

    private DynamicModel dynamicModel;
    public AutoInterstitialAD(Activity activity, ViewGroup adContainer, MiiADListener listener){
       dynamicModel = MethodDynamicLoad.loadAutoInterstitialADMethod(activity,adContainer,listener);
    }
    public void recycle(){
        MethodDynamicLoad.loadRecycleMethod(dynamicModel);
    }
}
