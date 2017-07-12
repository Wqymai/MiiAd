package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mg.d.c.DynamicModel;
import com.mg.d.utils.MethodDynamicLoad;
import com.mg.interf.MiiADListener;

/**
 * Created by wuqiyan on 17/7/5.
 */

public class MgSplashAD {

    private DynamicModel model;
    private Context mContext;
    public MgSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,String appid, MiiADListener adListener){
        this.mContext = activity.getApplicationContext();
        model = MethodDynamicLoad.getInstance(activity.getApplicationContext()).loadSplashADMethod(activity,adContainer,skipContainer,appid,adListener);
    }

    public void recycle(){
        MethodDynamicLoad.getInstance(mContext).loadRecycleMethod(model);
    }

}
