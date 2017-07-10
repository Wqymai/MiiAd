package com.mg.banner;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.mg.interf.MiiADListener;
import com.mg.d.c.DynamicModel;
import com.mg.d.utils.MethodDynamicLoad;

/**
 * Created by wuqiyan on 17/7/6.
 */

public class MgBannerAD {

    private DynamicModel model;
    private Context mContext;
    public MgBannerAD(Activity activity, ViewGroup adContainer, MiiADListener listener){
        this.mContext = activity.getApplicationContext();
       model = MethodDynamicLoad.getInstance(mContext).loadBannerADMethod(activity,adContainer,listener);
    }

    public void recycle(){
       MethodDynamicLoad.getInstance(mContext).loadRecycleMethod(model);
    }
}
