package com.mg.interstitial;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.d.c.DynamicModel;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgAutoInterstitialAD {

    private DynamicModel dynamicModel;
    private Context mContext;
    public MgAutoInterstitialAD(Activity activity, ViewGroup adContainer,String appid, MiiADListener listener){
        this.mContext = activity.getApplicationContext();
       dynamicModel = MethodDynamicLoad.getInstance(mContext).loadAutoInterstitialADMethod(activity,adContainer,appid,listener);
    }
    public void recycle(){
        MethodDynamicLoad.getInstance(mContext).loadRecycleMethod(dynamicModel);
    }
}
