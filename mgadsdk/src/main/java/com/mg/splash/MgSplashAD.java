package com.mg.splash;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.others.model.DynamicModel;
import com.mg.utils.MethodLoad;

/**
 * Created by wuqiyan on 17/7/5.
 */

public class MgSplashAD {

    private DynamicModel model;
    private Context mContext;
    public MgSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,String appid,String lid, MiiADListener adListener){
        this.mContext = activity.getApplicationContext();
        model = MethodLoad.getInstance(activity.getApplicationContext()).loadSplashADMethod(activity,adContainer,skipContainer,appid,lid,adListener);
    }

    public void recycle(){
        MethodLoad.getInstance(mContext).loadRecycleMethod(model);
    }

}
